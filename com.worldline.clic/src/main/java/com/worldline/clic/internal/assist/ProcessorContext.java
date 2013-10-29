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
package com.worldline.clic.internal.assist;

import java.util.Map;

/**
 * This {@link ProcessorContext} object allows to represent the execution
 * context of the autocomplete processor. It'll allow to store values to be used
 * while searching for potential proposals for commands.
 * 
 * @author mvanbesien / aneveux
 * @version 1.0
 * @since 1.0
 */
public class ProcessorContext {

	/**
	 * contains the value specified by the user for which we'd like to compute
	 * some autocomplete
	 */
	private final String input;

	/**
	 * contains the cursor position...
	 */
	private final int cursorPosition;

	/**
	 * a {@link Map} describing the properties to be used while processing the
	 * autocomplete
	 */
	private final Map<String, Object> properties;

	/**
	 * Constructor
	 * 
	 * @param input
	 *            {@link #input}
	 * @param cursorPosition
	 *            {@link #cursorPosition}
	 * @param properties
	 *            {@link #properties}
	 */
	public ProcessorContext(final String input, final int cursorPosition,
			final Map<String, Object> properties) {
		super();
		this.input = input;
		this.cursorPosition = cursorPosition;
		this.properties = properties;
	}

	/**
	 * Simple getter for {@link #input}
	 * 
	 * @return {@link #input}
	 */
	public String getInput() {
		return input;
	}

	/**
	 * Simple getter for {@link #properties}
	 * 
	 * @return {@link #properties}
	 */
	public Map<String, Object> getProperties() {
		return properties;
	}

	/**
	 * Simple getter for {@link #cursorPosition}
	 * 
	 * @return {@link #cursorPosition}
	 */
	public int getCursorPosition() {
		return cursorPosition;
	}

}
