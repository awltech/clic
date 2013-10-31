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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;

import com.worldline.clic.commands.AbstractCommand;
import com.worldline.clic.internal.Activator;

/**
 * This {@link CommandRegistry} object allows to read all the commands
 * registered in the extension point, and make them available to the other
 * components of the framework.
 * 
 * This singleton aims at providing various utilities methods allowing to give
 * information about the available commands and information linked to those
 * commands.
 * 
 * @author mvanbesien / aneveux
 * @version 1.0
 * @since 1.0
 */
public class CommandRegistry {

	/**
	 * This inner class allows to store the singleton instance.
	 * 
	 * @author mvanbesien
	 * @since 1.0
	 */
	private static final class SingletonHolder {
		/**
		 * contains the instance of {@link CommandRegistry} to be used as a
		 * singleton
		 */
		private static CommandRegistry instance = new CommandRegistry();
	}

	/**
	 * The private constructor allows to match with the singleton pattern. It'll
	 * take care of loading all the information coming from the extension point.
	 */
	private CommandRegistry() {
		loadExtensionPoint();
	}

	/**
	 * The {@link #loadExtensionPoint()} method allows to read this plugin's
	 * extension point in order to retrieve all the commands which have been
	 * contributed and store them into a {@link Map}
	 */
	private void loadExtensionPoint() {
		commands.clear();
		
		commandFlowCollection.clear();
		final IExtensionPoint extension = Platform.getExtensionRegistry()
				.getExtensionPoint(Activator.PLUGIN_ID, "commands");
		for (final IConfigurationElement element : extension
				.getConfigurationElements()) {
			if ("command".equals(element.getName())) {
				final String id = element.getAttribute("id");
				final String description = element.getAttribute("description");
				commands.put(id, new CommandWrapper(id, description, element));
			}
			if ("commandFlow".equals(element.getName())) {
				commandRefList.clear();
				final String name = element.getAttribute("name");
				for (IConfigurationElement subElement : element.getChildren()) {
					if ("commandRef".equals(subElement.getName())) {
						final String nameRef = subElement.getAttribute("name");
						commandRefList.add(nameRef);
					}
				}
				CommandFlowWrapper commandFlow = new CommandFlowWrapper(name, commandRefList);
				this.commandFlowCollection.add(commandFlow);
			}
			
		}
	}

	/**
	 * This {@link Map} allows to store all the commands which have been
	 * contributed through the extension point. The {@link Map} is linked a
	 * {@link String} correponding to the command's id to a particular
	 * {@link CommandWrapper} containing all the required information needed to
	 * actually build the command.
	 */
	private final Map<String, CommandWrapper> commands = new HashMap<String, CommandWrapper>();
	
	//private final Map<String, CommandFlowWrapper> commandsFlow = new HashMap<String, CommandFlowWrapper>();
	
	public final List<String> commandRefList = new ArrayList<>();
	
	public Collection <CommandFlowWrapper> getCommandFlow() {
		return commandFlowCollection;
	}
	
	private Collection<CommandFlowWrapper> commandFlowCollection = new ArrayList<CommandFlowWrapper>();
	
	/**
	 * Allows to get the singleton instance in order to query for some
	 * information about commands
	 * 
	 * @return the singleton instance of {@link CommandRegistry}
	 */
	public static CommandRegistry getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * Allows to search the registry {@link Map} ({@link #commands}) for a
	 * particular command by its id. If the command exists, the
	 * {@link CommandWrapper} will allow to actually create the
	 * {@link AbstractCommand} object.
	 * 
	 * @param commandId
	 *            the id of the command you'd like to search in the registry
	 * @return an instance of {@link AbstractCommand} linked to the specified id
	 *         if it exists. null otherwise.
	 */
	public AbstractCommand createCommand(final String commandId) {
		return commands.containsKey(commandId) ? commands.get(commandId)
				.createCommand() : null;
	}

	/**
	 * Allows to search the registry {@link Map} ({@link #commands}) for the
	 * description of a particular command.
	 * 
	 * @param commandId
	 *            the id of the command you'd like to search in the registry
	 * @return the command's description (which is specified in the extension
	 *         point) if it exists, null otherwise.
	 */
	public String getCommandDescription(final String commandId) {
		return commands.containsKey(commandId) ? commands.get(commandId)
				.getDescription() : null;
	}

	/**
	 * Allows to query the registry in order to get all the available commands.
	 * 
	 * @return A {@link Set} containing all the available commands ids. The
	 *         available commands are actually matching with all the extension
	 *         point contributions
	 */
	public Set<String> getCommandsList() {
		return Collections.unmodifiableSet(commands.keySet());
	}

}
