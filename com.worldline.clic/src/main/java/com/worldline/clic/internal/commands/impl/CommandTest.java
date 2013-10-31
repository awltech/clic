package com.worldline.clic.internal.commands.impl;

import joptsimple.OptionSpec;

import com.worldline.clic.commands.AbstractCommand;
import com.worldline.clic.commands.CommandContext;

public class CommandTest extends AbstractCommand {

	OptionSpec<String> named;
	@Override
	public void configureParser() {
		named = parser.accepts("name").withRequiredArg().ofType(String.class);
	
	}
	

	@Override
	public void execute(CommandContext context) {
		context.write("Hello "+options.valueOf(named));
	}

}
