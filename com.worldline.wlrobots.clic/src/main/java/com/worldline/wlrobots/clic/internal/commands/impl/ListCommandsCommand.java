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
package com.worldline.wlrobots.clic.internal.commands.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.worldline.wlrobots.clic.commands.AbstractCommand;
import com.worldline.wlrobots.clic.commands.CommandContext;
import com.worldline.wlrobots.clic.internal.commands.CommandRegistry;

/**
 * The {@link ListCommandsCommand} is an internal implementation of an
 * {@link AbstractCommand} to be used by the user in order to get the list of
 * all the available commands in CLiC. The list of all commands will be
 * displayed directly in the console.
 * 
 * The command will internally rely on the all the commands which has been
 * defined in the extension point, so any command registered through the
 * extension point will be displayed by this command.
 * 
 * @author mvanbesien / aneveux
 * @version 1.0
 * @since 1.0
 * 
 * @see AbstractCommand
 */
public class ListCommandsCommand extends AbstractCommand {

	/**
	 * We don't define anything related to the parser since this command doesn't
	 * need any parameters.
	 */
	@Override
	public void configureParser() {
	}

	/**
	 * This command will use the {@link CommandRegistry} in order to get all the
	 * registered commands and display them on the console.
	 */
	@Override
	public void execute(final CommandContext context) {
		final Set<String> commandsList = CommandRegistry.getInstance()
				.getCommandsList();
		final List<String> sortedCommandsList = new ArrayList<String>(
				commandsList);
		Collections.sort(sortedCommandsList);

		for (final String commandId : sortedCommandsList)
			context.write(String.format("%s - %s", commandId, CommandRegistry
					.getInstance().getCommandDescription(commandId)));
	}

}
