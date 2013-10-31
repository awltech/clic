package com.worldline.clic.internal.commands.impl;

import java.util.Collection;

import com.worldline.clic.commands.AbstractCommand;
import com.worldline.clic.commands.CommandContext;
import com.worldline.clic.internal.commands.CommandFlowWrapper;
import com.worldline.clic.internal.commands.CommandRegistry;

/**
 * The {@link ListCommandsCommandFlow} is an internal implementation of an
 * {@link AbstractCommand} to be used by the user in order to get the list of
 * all the available commands flow in CLiC. The list of all commands flow will be
 * displayed directly in the console.
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

public class ListCommandsCommandFlow extends AbstractCommand {

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
	public void execute(CommandContext context) {
		final Collection<CommandFlowWrapper> cmdList = CommandRegistry.getInstance().getCommandFlow();
		for (CommandFlowWrapper cmdFlow : cmdList) {
			context.write(cmdFlow.getName()+" - [ COMMAND FLOW ]");
		}
	}

}
