/*
 * CLiC, Framework for Command Line Interpretation in Eclipse
 *
 *     Copyright (C) 2013 Worldline or third-party contributors as
 *     indicated by the @author tags or express copyright attribution
 *     statements applied by the authors.
 *
 *     This library is free software; you can redistribute it and/or
 *     modify it under the terms of the GNU Lesser General Public
 *     License as published by the Free Software Foundation; either
 *     version 2.1 of the License.
 *
 *     This library is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *     Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public
 *     License along with this library; if not, write to the Free Software
 *     Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */
package com.worldline.clic.internal.commands;

import static com.worldline.clic.internal.ClicMessages.COMMAND_NOT_FOUND;
import static com.worldline.clic.internal.ClicMessages.COMMAND_PARSING_ERROR;
import static com.worldline.clic.internal.ClicMessages.PARSER_UNBALANCED_QUOTES;
import static com.worldline.clic.internal.ClicMessages.SEPARATOR;

import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;

import joptsimple.OptionException;
import joptsimple.OptionParser;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import com.google.common.collect.ObjectArrays;
import com.worldline.clic.commands.AbstractCommand;
import com.worldline.clic.commands.CommandContext;
import com.worldline.clic.internal.Activator;
import com.worldline.clic.listeners.ProcessedCommandEvent;
import com.worldline.clic.listeners.ProcessedCommandListener;
import com.worldline.clic.listeners.internal.ListenerRegistry;

/**
 * This {@link CommandProcessor} is an extension of a {@link Job} which aims at
 * processing all the commands defined in the framework. Extending a {@link Job}
 * allows to deal with a standard Eclipse execution of a process.
 * 
 * Internally, a {@link CommandProcessor} will allow to parse a command (relying
 * on some Ant implementation for that), then use JOpt-Simple to parse all the
 * parameters and inject them properly in the specific command options. Finally,
 * it'll execute the implementation defined by the command.
 * 
 * @author mvanbesien / aneveux
 * @version 1.0
 * @since 1.0
 * 
 * @see Job
 * @see AbstractCommand
 */
public class CommandProcessor extends Job {
	/**
	 * {@link #commandChain} is a {@link String} containing the exact command
	 * which has been provided by the end-user
	 */
	private final String commandChain;

	/**
	 * {@link #context} contains the {@link CommandContext} to be used during
	 * the whole execution of the processed command
	 */
	private final CommandContext context;

	/**
	 * Constructor
	 * 
	 * @param commandChain
	 *            the command which has been provided by the end-user
	 * @param context
	 *            the execution context to be used during the whole lifecycle of
	 *            the processed command
	 */
	public CommandProcessor(final String commandChain, final CommandContext context) {
		super("Command Processor");
		this.context = context;
		this.commandChain = commandChain;
	}

	/**
	 * This method is called internally in order to start the command's
	 * execution. It allows to parse all the options and execute the command.
	 */
	@Override
	protected IStatus run(final IProgressMonitor monitor) {
		processCommand(commandChain, context);
		return Status.OK_STATUS;
	}

	/**
	 * This {@link #processCommand(String, CommandContext)} function allows to
	 * parse the command in chunks, compute all the options thanks to
	 * JOpt-Simple, and finally, to execute the command if no error has been
	 * raised.
	 * 
	 * @param command
	 *            the command which has been provided by the end-user
	 * @param context
	 *            the execution context to be used
	 */
	private static void processCommand(final String command, final CommandContext context) {
		String firstChunk = "";
		String[] parameters = new String[0];
		boolean flow = false;
		context.clearOutputs();
		if (command.indexOf(" ") == -1)
			firstChunk = command;
		else {
			try {
				parameters = parseCommandLine(command.substring(command.indexOf(" ")).trim());
			} catch (final CommandParsingException e) {
				context.write(COMMAND_PARSING_ERROR.value(e.getMessage()));
				Activator.sendErrorToErrorLog(COMMAND_PARSING_ERROR.value(e.getMessage()), e);
				return;
			}
			firstChunk = command.substring(0, command.indexOf(" "));
		}

		flow = CommandRegistry.getInstance().getFlows().containsKey(firstChunk);

		if (flow) {
			final CommandFlowWrapper wrapper = CommandRegistry.getInstance().getFlows().get(firstChunk);
			for (final String commandReference : wrapper.getCommandReferences()) {
				final String[] allParameters = ObjectArrays.concat(parameters,
						context.getOutputs().toArray(new String[0]), String.class);
				launchCommand(commandReference, allParameters, command, context);
			}
		} else
			launchCommand(firstChunk, parameters, command, context);

		// Now, calls the listeners related to Command Process.
		Collection<ProcessedCommandListener> listeners = ListenerRegistry.getInstance().getListenersFor(
				ProcessedCommandListener.class);
		for (ProcessedCommandListener listener : listeners) {
			try {
				listener.onEvent(new ProcessedCommandEvent(command));
			} catch (Exception e) {
				Activator
						.getDefault()
						.getLog()
						.log(new Status(IStatus.WARNING, Activator.PLUGIN_ID,
								"An exception was caught while executing listener", e));
			}
		}

	}

	/**
	 * Allows to launch a particular command's execution
	 * 
	 * @param firstChunk
	 *            the command reference to be executed. this parameter will
	 *            allow to retrieve the command from the extension point.
	 * @param parameters
	 *            contains all the paramters to be provided to the command to be
	 *            executed
	 * @param command
	 *            contains the whole command which has been provided by the end
	 *            user, without any interpretation
	 * @param context
	 *            the command context
	 */
	protected static void launchCommand(final String firstChunk, final String[] parameters, final String command,
			final CommandContext context) {
		final AbstractCommand commandImplementation = CommandRegistry.getInstance().createCommand(firstChunk);
		if (commandImplementation != null) {
			try {
				computeParameters(commandImplementation, context, parameters);
			} catch (final OptionException e) {
				context.write(COMMAND_PARSING_ERROR.value(e.getMessage()));
				Activator.sendErrorToErrorLog(COMMAND_PARSING_ERROR.value(e.getMessage()), e);
				return;
			}
			try {
				commandImplementation.execute(context);
			} catch (Exception e) {
				Activator
						.getDefault()
						.getLog()
						.log(new Status(IStatus.WARNING, Activator.PLUGIN_ID,
								"An exception was caught while executing command", e));
			}
			context.write(SEPARATOR.value());
		} else
			context.write(COMMAND_NOT_FOUND.value(command));
	}

	/**
	 * This internal function allows to invoke the {@link OptionParser} of a
	 * specific provided {@link AbstractCommand} in order to compute all of its
	 * parameters.
	 * 
	 * @param commandImplementation
	 *            an {@link AbstractCommand} on which we want to compute the
	 *            options
	 * @param context
	 *            the execution's context
	 * @param parameters
	 *            all the parameters to be computed by the {@link OptionParser}
	 * 
	 * @see AbstractCommand#parse(String[])
	 */
	private static void computeParameters(final AbstractCommand commandImplementation, final CommandContext context,
			final String[] parameters) {
		commandImplementation.parse(parameters);
	}

	/**
	 * http://api.dpml.net/ant/1.6.4/org/apache/tools/ant/types/Commandline.
	 * html#translateCommandline%28java.lang.String%29
	 * 
	 * Command-line cracker coming from Ant.
	 * 
	 * @param toProcess
	 *            the command line to process.
	 * @return the command line broken into strings. An empty or null toProcess
	 *         parameter results in a zero sized array.
	 * 
	 * @throws CommandParsingException
	 */
	private static String[] parseCommandLine(final String toProcess) throws CommandParsingException {
		if (toProcess == null || toProcess.length() == 0)
			// no command? no string
			return new String[0];
		final int normal = 0;
		final int inQuote = 1;
		final int inDoubleQuote = 2;
		int state = normal;
		final StringTokenizer tok = new StringTokenizer(toProcess, "\"\' ", true);
		final ArrayList<String> result = new ArrayList<String>();
		final StringBuilder current = new StringBuilder();
		boolean lastTokenHasBeenQuoted = false;

		while (tok.hasMoreTokens()) {
			final String nextTok = tok.nextToken();
			switch (state) {
			case inQuote:
				if ("\'".equals(nextTok)) {
					lastTokenHasBeenQuoted = true;
					state = normal;
				} else
					current.append(nextTok);
				break;
			case inDoubleQuote:
				if ("\"".equals(nextTok)) {
					lastTokenHasBeenQuoted = true;
					state = normal;
				} else
					current.append(nextTok);
				break;
			default:
				if ("\'".equals(nextTok))
					state = inQuote;
				else if ("\"".equals(nextTok))
					state = inDoubleQuote;
				else if (" ".equals(nextTok)) {
					if (lastTokenHasBeenQuoted || current.length() != 0) {
						result.add(current.toString());
						current.setLength(0);
					}
				} else
					current.append(nextTok);
				lastTokenHasBeenQuoted = false;
				break;
			}
		}
		if (lastTokenHasBeenQuoted || current.length() != 0)
			result.add(current.toString());
		if (state == inQuote || state == inDoubleQuote)
			throw new CommandParsingException(PARSER_UNBALANCED_QUOTES.value(toProcess));
		return result.toArray(new String[result.size()]);
	}

	/**
	 * This {@link Exception} allows to specify an issue while parsing a command
	 * 
	 * @author aneveux
	 * @version 1.0
	 * @since 1.0
	 */
	@SuppressWarnings("serial")
	static class CommandParsingException extends Exception {
		public CommandParsingException(final String msg) {
			super(msg);
		}
	}

}
