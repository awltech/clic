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

import java.util.ArrayList;
import java.util.List;

/**
 * The {@link CommandFlowWrapper} object allows to store all the information
 * linked to a particular instance of a command flow. It allows to retrieve all
 * the commands that should be invoked from a particular flow.
 * 
 * @author ahavez
 * @version 1.0
 * @since 1.0
 */
public class CommandFlowWrapper {

	/**
	 * The name of the command flow
	 */
	private final String name;

	/**
	 * A {@link List} containing all the commands to be invoked by the flow
	 */
	private final List<String> commandReferences;

	/**
	 * Default constructor
	 * 
	 * @param name
	 *            the command flow's name
	 * @param commandReferences
	 *            all the commands that should be invoked from this flow
	 */
	public CommandFlowWrapper(final String name,
			final List<String> commandReferences) {
		this.name = name;
		this.commandReferences = new ArrayList<String>(commandReferences);
	}

	/**
	 * Simple getter for {@link #name}
	 * 
	 * @return the name of the flow
	 */
	public String getName() {
		return name;
	}

	/**
	 * Simple getter for {@link #commandReferences}
	 * 
	 * @return all the command references to be invoked from the flow
	 */
	public List<String> getCommandReferences() {
		return commandReferences;
	}

	/**
	 * Overriding toString method in order to display all the information of the
	 * flow properly
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(name).append(": [");
		for (final String command : commandReferences)
			sb.append(command).append(", ");
		sb.delete(sb.length() - 2, sb.length() - 1);
		sb.append("]");
		return sb.toString();
	}
}
