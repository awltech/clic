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
