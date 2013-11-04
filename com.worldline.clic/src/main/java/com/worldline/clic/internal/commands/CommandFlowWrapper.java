package com.worldline.clic.internal.commands;

import java.util.ArrayList;
import java.util.List;

public class CommandFlowWrapper {

	private final String name;
	
	private List<String> commandRefList = new ArrayList<String>();
	
	public CommandFlowWrapper (final String name, final List<String> commandRefList) {
		super();
		this.name = name;
		this.commandRefList.clear();
		this.commandRefList.addAll(commandRefList);
	}
	
	public String getName() {
		return name;
	}
	
	public List<String> getCommandRefList() {
		return commandRefList;
	}
}
