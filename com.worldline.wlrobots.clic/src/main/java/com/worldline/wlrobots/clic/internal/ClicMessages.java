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
package com.worldline.wlrobots.clic.internal;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * This {@link ClicMessages} enum allows actually to get i18n messages from a
 * properties file. It uses internally a {@link ResourceBundle} which is linked
 * to all the value you could find in this enum.
 * 
 * In order to use it, simply call the {@link #value()} method on a particular
 * value from the enum. If you'd like to provide some parameters, the values can
 * be computed using {@link #value(Object...)}, which will
 * {@link MessageFormat#format(String, Object...)} internally.
 * 
 * @author aneveux
 * @version 1.0
 * @since 1.0
 */
public enum ClicMessages {
	// Error messages

	COMMAND_NOT_FOUND, COMMAND_EXECUTION_ERROR, COMMAND_PARSING_ERROR, PARSER_UNBALANCED_QUOTES,

	// Formatting messages

	COMMAND_HELP, SEPARATOR,

	// Messages

	CLIC_TITLE, CLIC_WELCOME, CONSOLE_CLEAR, COMMAND_RETURN

	;

	/**
	 * {@link ResourceBundle} instance to be used to get messages
	 */
	private static ResourceBundle resourceBundle = ResourceBundle
			.getBundle("ClicMessages");

	/**
	 * Returns the value of a specific messages
	 * 
	 * @return the value of the message corresponding to a particular key
	 */
	public String value() {
		if (ClicMessages.resourceBundle == null
				|| !ClicMessages.resourceBundle.containsKey(name()))
			return "!!" + name() + "!!";
		return ClicMessages.resourceBundle.getString(name());
	}

	/**
	 * Returns value of the formatted message
	 * 
	 * @param args
	 *            all the args to be used while formatting your message using
	 *            {@link MessageFormat#format(String, Object...)}
	 * @return the i18n message matching to a particular key, containing all the
	 *         provided parameters
	 */
	public String value(final Object... args) {
		if (ClicMessages.resourceBundle == null
				|| !ClicMessages.resourceBundle.containsKey(name()))
			return "!!" + name() + "!!";
		return MessageFormat.format(
				ClicMessages.resourceBundle.getString(name()), args);
	}

}