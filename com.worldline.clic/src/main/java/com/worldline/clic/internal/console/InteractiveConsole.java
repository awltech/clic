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
package com.worldline.clic.internal.console;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Writer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.console.IOConsole;
import org.eclipse.ui.console.IOConsoleOutputStream;

import com.worldline.clic.commands.CommandContext;
import com.worldline.clic.internal.Activator;
import com.worldline.clic.internal.ClicMessages;
import com.worldline.clic.internal.commands.CommandProcessor;

/**
 * This {@link InteractiveConsole} extending {@link IOConsole} allows to use
 * CLiC directly from an Eclipse console, as a terminal-like tool.
 * 
 * @author mvanbesien / aneveux
 * @version 1.0
 * @since 1.0
 * 
 * @see IOConsole
 */
public class InteractiveConsole extends IOConsole implements Runnable {

	/**
	 * The {@link InputStreamReader} allowing to read information from the
	 * console
	 */
	private final InputStreamReader isr;

	/**
	 * A {@link BufferedReader} instance linked to {@link #isr} in order to read
	 * from the console easily
	 */
	private final BufferedReader br;

	/**
	 * An {@link IOConsoleOutputStream} instance allowing to write information
	 * directly on the console
	 */
	private final IOConsoleOutputStream iocos;

	/**
	 * A {@link Writer} object linked to {@link #iocos} allowing to write easily
	 * on the console
	 */
	private final Writer writer;

	/**
	 * A boolean to indicate if the console's active or not
	 */
	private boolean active = true;

	/**
	 * A {@link Thread} instance to be used for the console
	 */
	private final Thread thread;

	/**
	 * A {@link CommandContext} wrapper matching the command's execution context
	 */
	private final CommandContext context;

	/**
	 * Constructor, setting up the console, and starting the linked thread
	 */
	public InteractiveConsole() {
		super(ClicMessages.CLIC_TITLE.value(), null);
		isr = new InputStreamReader(getInputStream());
		br = new BufferedReader(isr);
		getInputStream().setColor(
				Display.getDefault().getSystemColor(SWT.COLOR_DARK_GREEN));
		iocos = newOutputStream();
		writer = new InteractiveConsoleWriter(iocos);
		context = new CommandContext(writer);
		iocos.setColor(Display.getDefault().getSystemColor(SWT.COLOR_BLUE));

		try {
			iocos.write(ClicMessages.CLIC_WELCOME.value());
		} catch (final Exception e) {
			Activator.sendErrorToErrorLog(e.getMessage(), e);
		}
		initialize();
		thread = new Thread(this);
		thread.setDaemon(true);
		thread.setName(ClicMessages.CLIC_TITLE.value());
		thread.start();
	}

	/**
	 * This method is called internally while constructing the console, it
	 * allows to read lines directly from the console and execute the specified
	 * commands
	 */
	@Override
	public void run() {
		while (active)
			try {
				final String line = br.readLine();
				final CommandProcessor commandProcessor = new CommandProcessor(
						line, context);
				try {
					commandProcessor.schedule();
					commandProcessor.join();
				} catch (final Exception e) {
					Activator.sendErrorToErrorLog(e.getMessage(), e);
				}
			} catch (final Exception e) {
				Activator.sendErrorToErrorLog(e.getMessage(), e);
			}
	}

	/**
	 * This method is called internally and allows to dispose all the resources
	 * used for the console
	 */
	@Override
	protected void dispose() {
		try {
			active = false;
		} catch (final Exception e) {
			Activator.sendErrorToErrorLog(e.getMessage(), e);
		}
		try {
			getInputStream().close();
		} catch (final Exception e) {
			Activator.sendErrorToErrorLog(e.getMessage(), e);
		}
		try {
			thread.interrupt();
		} catch (final Exception e) {
			Activator.sendErrorToErrorLog(e.getMessage(), e);
		}
		super.dispose();
	}

}
