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
package com.worldline.clic.commands;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import com.worldline.clic.internal.Activator;

/**
 * {@link CommandContext} is a class used by CLiC in order to represent a
 * specific command execution's context. The main feature behind this object is
 * to allow to interact with the user by sending some feedbacks about a command
 * execution.
 * 
 * @author mvanbesien / aneveux
 * @version 1.0
 * @since 1.0
 */
public class CommandContext {

	/**
	 * Constructor
	 * 
	 * @param writer
	 *            the {@link Writer} object to be used by CLiC in order to send
	 *            data to the console
	 */
	public CommandContext(final Writer writer) {
		this.writer = writer;
	}

	/**
	 * The {@link Writer} object which is used internally by the console in
	 * order to display data
	 */
	private final Writer writer;

	/**
	 * This object allows to store the current object manipulated during the
	 * command's execution
	 */
	private Object currentObject;

	/**
	 * This {@link #context} object is actually a {@link Map} allowing to store
	 * any information in the execution's context.
	 */
	private final Map<String, Object> context = new HashMap<>();

	/**
	 * This {@link #getScope()} function allows to get the execution's context
	 * data. So you can put any kind of data in this context, and retrieve it
	 * from somewhere else sharing the same context.
	 * 
	 * @return {@link #context} a {@link Map} containing the execution's context
	 *         data
	 */
	public Map<String, Object> getScope() {
		return context;
	}

	/**
	 * Simple getter for {@link #currentObject}
	 * 
	 * @return {@link #currentObject}, the current object manipulated during
	 *         this execution's instance
	 */
	public Object getCurrentObject() {
		return currentObject;
	}

	/**
	 * Allows to specify a new {@link Object} for this instance of the command's
	 * execution
	 * 
	 * @param currentObject
	 *            the {@link Object} to be linked with this execution's instance
	 * @return the current instance of {@link CommandContext}
	 */
	public CommandContext withCurrentObject(final Object currentObject) {
		this.currentObject = currentObject;
		return this;
	}

	/**
	 * Allows to create a new context object containing an exact clone of this
	 * instance of {@link CommandContext}.
	 * 
	 * @return a new instance of {@link CommandContext} containing the exact
	 *         same information as this instance
	 */
	public CommandContext cloneContext() {
		final CommandContext context = new CommandContext(writer);
		context.context.putAll(this.context);
		return context;
	}

	/**
	 * Allows to write a message on the console using the context's
	 * {@link #writer}. You don't have to wait till the end of the command's
	 * execution, and the message will be displayed directly on the console.
	 * 
	 * @param message
	 *            the message you'd like to display on the console
	 */
	public void write(final String message) {
		if (writer != null)
			try {
				writer.write(message);
				writer.flush();
			} catch (final IOException e) {
				Activator.sendErrorToErrorLog(e.getMessage(), e);
			}
	}

	/**
	 * Simple getter for {@link #writer}. It allows to use the {@link #writer}
	 * directly from your command, if you'd like to link the execution result
	 * directly to the writer for example.
	 * 
	 * @return the {@link #writer} which is used by your command to display
	 *         information on the console.
	 */
	public Writer getWriter() {
		return writer;
	}

}
