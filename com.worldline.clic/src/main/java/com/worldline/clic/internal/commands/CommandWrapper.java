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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;

import com.worldline.clic.commands.AbstractCommand;
import com.worldline.clic.internal.Activator;

/**
 * The {@link CommandWrapper} object allows to store all the information which
 * are linked to a command, and build the instance of {@link AbstractCommand} to
 * be linked to the command when needed.
 * 
 * A {@link CommandWrapper} object will be created for each entry of the
 * extension point, and will allow to create the {@link AbstractCommand} only
 * when needed.
 * 
 * @author mvanbesien / aneveux
 * @version 1.0
 * @since 1.0
 */
public class CommandWrapper {

	/**
	 * the command's id (matching with the id specified in the extension point)
	 */
	private final String id;

	/**
	 * the command's description, coming from the extension point
	 */
	private final String description;

	/**
	 * the {@link IConfigurationElement} directly coming from the extension
	 * point
	 */
	private final IConfigurationElement element;

	/**
	 * Constructor
	 * 
	 * @param id
	 *            the command's id
	 * @param description
	 *            the command's description
	 * @param element
	 *            the {@link IConfigurationElement} coming directly from the
	 *            extension point
	 */
	public CommandWrapper(final String id, final String description,
			final IConfigurationElement element) {
		super();
		this.id = id;
		this.element = element;
		this.description = description;
	}

	/**
	 * Simple getter for {@link #id}
	 * 
	 * @return {@link #id}
	 */
	public String getId() {
		return id;
	}

	/**
	 * Allows to create the {@link AbstractCommand} instance which is linked to
	 * this {@link CommandWrapper}. It'll use the information coming from the
	 * extension point in order to get the executable.
	 * 
	 * @return an instance of {@link AbstractCommand} linked to this
	 *         {@link CommandWrapper}, null if an error is raised
	 */
	public AbstractCommand createCommand() {
		try {
			final AbstractCommand cmd = (AbstractCommand) element
					.createExecutableExtension("implementation");
			cmd.configureParser();
			return cmd;
		} catch (final CoreException e) {
			Activator.sendErrorToErrorLog(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * Simple getter for {@link #description}
	 * 
	 * @return {@link #description}
	 */
	public String getDescription() {
		return description;
	}

}
