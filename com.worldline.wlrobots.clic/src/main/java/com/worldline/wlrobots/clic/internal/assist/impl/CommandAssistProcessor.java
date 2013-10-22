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
package com.worldline.wlrobots.clic.internal.assist.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import com.worldline.wlrobots.clic.internal.assist.IProcessor;
import com.worldline.wlrobots.clic.internal.assist.ProcessorContext;
import com.worldline.wlrobots.clic.internal.commands.CommandRegistry;

/**
 * This {@link CommandAssistProcessor} class allows to deal with command
 * autocompletion. It'll actually compute a list of all available commands
 * matching a particular context.
 * 
 * @author mvanbesien
 * @version 1.0
 * @since 1.0
 * 
 * @see IProcessor
 */
public class CommandAssistProcessor implements IProcessor {

	/**
	 * Allows to get all the proposals as a {@link Collection} of {@link String}
	 * corresponding to a particular {@link ProcessorContext}
	 */
	@Override
	public Collection<String> getProposals(final ProcessorContext context) {

		final Collection<String> proposals = new ArrayList<String>();

		final String input = context.getInput();
		final int spaceIndex = input.indexOf(" ");

		final String pattern = spaceIndex == -1 ? input : input.substring(0,
				spaceIndex - 1);

		if (spaceIndex == -1 || context.getCursorPosition() <= spaceIndex) {
			final Set<String> commandsList = CommandRegistry.getInstance()
					.getCommandsList();
			for (final String result : commandsList)
				if (result.startsWith(pattern))
					proposals.add(result);
		}
		return proposals;
	}
}
