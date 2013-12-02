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

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import com.worldline.clic.commands.AbstractCommand;
import com.worldline.clic.internal.commands.CommandRegistry;

/**
 * 
 * This class proposes a first Content Assist processor. When it detects that
 * you are using a command, it completes the command with the matching extended
 * pattern. It does the same with the arguments of the provided command.
 * 
 * @author mvanbesien
 * @since 0.1
 * @version 0.1
 * 
 */
public class ContentAssistProcessor {

	/**
	 * Set that contains all commands and command flows identifiers
	 */
	private static Set<String> allCommands = new HashSet<String>();
	
	/**
	 * Static initializer
	 */
	static {
		allCommands.addAll(CommandRegistry.getInstance().getCommandsList());
		allCommands.addAll(CommandRegistry.getInstance().getFlows().keySet());
	}
	
	// Private constructor as par static helper class
	private ContentAssistProcessor() {
		
	}
	
	/**
	 * Takes the command and the current caret position, and completes the
	 * command with matching auto completion.
	 * 
	 * TODO Misses the implementation of contributed completion processor, for
	 * completing values of arguments (to be contributed by user...)
	 * 
	 * @param fullCommand
	 * @param cursorPosition
	 * @return
	 */
	public static String assist(String fullCommand, int cursorPosition) {

		// Handle the case where nothing is specified.
		if (fullCommand.length() == 0)
			return fullCommand;

		// Now we split the command into chunks, and check the index of the
		// cursor.
		String[] commandChunks = fullCommand.split(" ");

		int cursorPositionInChunk = -1;
		int cursorChunkLocation = -1;

		for (int i = 0, temp = 0; i < commandChunks.length && cursorChunkLocation == -1; i++) {
			int chunkLength = commandChunks[i].length();
			if (cursorPosition >= temp && cursorPosition <= temp + chunkLength) {
				cursorChunkLocation = i;
				cursorPositionInChunk = cursorPosition - temp;
			}
			temp += chunkLength + 1;
		}

		if (cursorChunkLocation == 0) {
			// In this case, we are in the command.
			String initialValue = commandChunks[0];
			String commandPrefix = initialValue.substring(0, cursorPositionInChunk);
			String commonExpandedPrefix = getExpandedPrefix(commandPrefix, allCommands);
			if (commonExpandedPrefix != null && commonExpandedPrefix.length() > 0) {
				commandChunks[0] = commonExpandedPrefix
						+ (initialValue.length() > cursorPositionInChunk ? initialValue.substring(commonExpandedPrefix
								.length()) : "");
			}
		} else if (commandChunks[cursorChunkLocation].startsWith("-")) {
			// In this case, we are in an argument.
			String commandName = commandChunks[0];
			String argumentValue = commandChunks[cursorChunkLocation];
			String argumentPrefix = argumentValue.substring(1, cursorPositionInChunk);

			AbstractCommand command = CommandRegistry.getInstance().createCommand(commandName);
			Set<String> possibleOptions = new LinkedHashSet<String>();
			for (String option : command.getParser().recognizedOptions().keySet()) {
				if (option.startsWith(argumentPrefix))
					possibleOptions.add(option);
			}

			if (argumentPrefix.length() > 0) {
				String expandedArgument = getExpandedPrefix(argumentPrefix, possibleOptions);
				commandChunks[cursorChunkLocation] = "-"
						+ expandedArgument
						+ (argumentValue.length() > cursorPositionInChunk ? commandChunks[cursorChunkLocation]
								.substring(argumentPrefix.length() + 1) : "");
			} else if (argumentPrefix.length() == 0 && possibleOptions.size() == 1) {
				commandChunks[cursorChunkLocation] = "-"
						+ possibleOptions.iterator().next()
						+ (argumentValue.length() > cursorPositionInChunk ? commandChunks[cursorChunkLocation]
								.substring(argumentPrefix.length() + 1) : "");
			}
			// TODO : Handle the values with specific handlers (through
			// extension points ?).

		}

		return rebuildCommand(commandChunks);
	}

	/**
	 * Takes all the chunks of the command and rebuild a command from it.
	 * 
	 * Just a very simple algorithm to reaggregate an array into string.
	 * 
	 * @param commandChunks
	 * @return
	 */
	private static String rebuildCommand(String[] commandChunks) {
		StringBuilder builder = new StringBuilder();
		for (String chunk : commandChunks) {
			if (builder.length() > 0)
				builder.append(" ");
			builder.append(chunk);
		}
		return builder.toString();
	}

	/**
	 * Retrieves the elements in the provided proposals set, that start with the
	 * initial pattern, and returns the common longest prefix of the selected
	 * elements.
	 * 
	 * @param prefix
	 * @param proposals
	 * @return
	 */
	private static String getExpandedPrefix(String prefix, Set<String> proposals) {
		Set<String> acceptableCommands = new LinkedHashSet<String>();

		// We get all the items that have the same prefix, and in them, we
		// isolate the longest one.
		String matchingCommand = null;
		for (String proposal : proposals) {
			if (prefix.length() > 0 && proposal.startsWith(prefix)) {
				acceptableCommands.add(proposal);
				if (matchingCommand == null || proposal.length() > matchingCommand.length())
					matchingCommand = proposal;
			}
		}

		for (String command : acceptableCommands) {
			matchingCommand = getCommonPrefix(matchingCommand, command);
		}

		return matchingCommand;
	}

	/**
	 * Retrieves the longest common prefix of the two provided strings.
	 * 
	 * @param matchingCommand
	 * @param command
	 * @return
	 */
	private static String getCommonPrefix(String matchingCommand, String command) {
		int iterationUpperBound = matchingCommand.length() < command.length() ? matchingCommand.length() : command
				.length();
		if (matchingCommand.equals(command))
			return matchingCommand;
		int indexToStop = 0;
		boolean match = true;
		for (int i = 0; i < iterationUpperBound && match; i++) {
			indexToStop = i;
			match = command.charAt(i) == matchingCommand.charAt(i);
		}
		return matchingCommand.substring(0, indexToStop);
	}
}
