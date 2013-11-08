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
package com.worldline.clic.internal.commands.impl;

import java.util.Collection;

import com.worldline.clic.commands.AbstractCommand;
import com.worldline.clic.commands.CommandContext;
import com.worldline.clic.internal.commands.CommandFlowWrapper;
import com.worldline.clic.internal.commands.CommandRegistry;

/**
 * The {@link ListFlowsCommand} is an internal implementation of an
 * {@link AbstractCommand} to be used by the user in order to get the list of
 * all the available commands flow in CLiC. The list of all commands flow will
 * be displayed directly in the console.
 * 
 * The command will internally rely on the all the commands flow which has been
 * defined in the extension point, so any command flow registered through the
 * extension point will be displayed by this command.
 * 
 * @author ahavez
 * @version 1.0
 * @since 1.0
 * 
 * @see AbstractCommand
 */

public class ListFlowsCommand extends AbstractCommand {

	/**
	 * We don't define anything related to the parser since this command doesn't
	 * need any parameters.
	 */
	@Override
	public void configureParser() {
	}

	/**
	 * This command will use the {@link CommandRegistry} in order to get all the
	 * registered commands flow and display them on the console.
	 */
	@Override
	public void execute(final CommandContext context) {
		final Collection<CommandFlowWrapper> flows = CommandRegistry
				.getInstance().getFlows().values();
		for (final CommandFlowWrapper flow : flows)
			context.write(flow.toString());
	}

}
