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
package com.worldline.clic.mvn;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * Stores all i18n messages linked to Clic Maven plugin
 * 
 * @author aneveux
 * @version 1.0
 * @since 1.0
 */
public enum ClicMavenMessages {

	// Templates

	POM_TEMPLATE,

	// Commands

	MAVEN_REFERENCE, MAVEN_REFERENCE_DESCRIPTION, MAVEN_REFERENCE_ARG, GENERATE_POM, GENERATE_POM_DESCRIPTION, MAVEN_CMD, MAVEN_CMD_DESCRIPTION, MAVEN_CMD_ARG, JVM_PARAM, JVM_PARAM_DESCRIPTION, JVM_PARAM_ARG,

	// Messages

	POM_GENERATION, POM_CREATED, COMMAND_GENERATION, NO_PARAMETERS, PARAMETERS_FOUND,

	// Errors

	POM_ERROR, MAVEN_EXEC_ERROR

	;

	/**
	 * ResourceBundle instance
	 */
	private static ResourceBundle resourceBundle = ResourceBundle
			.getBundle("ClicMavenMessages");

	/**
	 * @return value of the message
	 */
	public String value() {
		if (ClicMavenMessages.resourceBundle == null
				|| !ClicMavenMessages.resourceBundle.containsKey(name()))
			return "!!" + name() + "!!";
		return ClicMavenMessages.resourceBundle.getString(name());
	}

	/**
	 * @return value of the formatted message
	 */
	public String value(final Object... args) {
		if (ClicMavenMessages.resourceBundle == null
				|| !ClicMavenMessages.resourceBundle.containsKey(name()))
			return "!!" + name() + "!!";
		return MessageFormat.format(
				ClicMavenMessages.resourceBundle.getString(name()), args);
	}

}
