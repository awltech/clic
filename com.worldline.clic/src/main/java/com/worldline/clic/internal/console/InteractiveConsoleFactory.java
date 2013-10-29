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
package com.worldline.clic.internal.console;

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleFactory;

/**
 * This {@link IConsoleFactory} implementation allows to create an
 * {@link InteractiveConsole} to be used for CLiC
 * 
 * @author mvanbesien / aneveux
 * @version 1.0
 * @since 1.0
 * 
 * @see IConsoleFactory
 */
public class InteractiveConsoleFactory implements IConsoleFactory {

	/**
	 * Allows to open a an {@link InteractiveConsole}
	 * 
	 * @see {@link #openConsole()}
	 */
	@Override
	public void openConsole() {
		final InteractiveConsole ic = new InteractiveConsole();
		ConsolePlugin.getDefault().getConsoleManager()
				.addConsoles(new IConsole[] { ic });
		ic.activate();
	}

}
