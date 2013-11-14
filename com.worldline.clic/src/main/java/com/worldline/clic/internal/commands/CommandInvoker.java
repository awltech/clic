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

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;

import com.worldline.clic.commands.CommandContext;
import com.worldline.clic.internal.Activator;

/**
 * A {@link CommandInvoker} allows to invoke programmatically a CLiC command. It
 * allows you to create from your code a complete command as a {@link String}
 * and execute it just like the console does.
 * 
 * Using the {@link CommandInvoker} from your command won't treat the new
 * executed command in a new thread. Moreover, it'll require a
 * {@link CommandContext} to be executed so it can share the execution context
 * you want.
 * 
 * @author aneveux
 * @version 1.0
 * @since 1.0
 * 
 * @see CommandProcessor
 */
public class CommandInvoker {

	/**
	 * Invokes a command (specified as a complete {@link String}) in a
	 * particular context. It won't execute the command in a new thread.
	 * 
	 * @param commandChain
	 *            the command line to be executed as a {@link String}
	 * @param context
	 *            the execution context to be used
	 * @return an {@link IStatus} allowing to give information about the
	 *         execution status
	 * 
	 * @since 1.0
	 */
	public static IStatus invoke(final String commandChain,
			final CommandContext context) {
		try {
			return new CommandProcessor(commandChain, context)
					.run(new NullProgressMonitor());
		} catch (final Exception e) {
			Activator.sendErrorToErrorLog(e.getMessage(), e);
			return new Status(IStatus.ERROR, Activator.PLUGIN_ID,
					e.getMessage(), e);
		}
	}

}
