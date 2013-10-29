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
package com.worldline.clic.internal.view;

import java.io.Writer;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.worldline.clic.internal.Activator;
import com.worldline.clic.internal.ClicMessages;

/**
 * {@link HistoryCleanAction} is an {@link Action} allowing to clear the GUI's
 * screen from a button
 * 
 * @author mvanbesien / aneveux
 * @version 1.0
 * @since 1.0
 * 
 * @see Action
 */
public class HistoryCleanAction extends Action {

	/**
	 * Reference to the {@link Writer} which is used internally by the GUI
	 */
	private final HistoryBufferedWriter writer;

	/**
	 * Constructor
	 * 
	 * @param writer
	 *            allows to specify the Writer which needs to be cleared
	 */
	public HistoryCleanAction(final HistoryBufferedWriter writer) {
		this.writer = writer;
	}

	/**
	 * Allows to get the icon to be displayed for clearing the GUI
	 */
	@Override
	public ImageDescriptor getImageDescriptor() {
		return AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID,
				"icons/clean_history.gif");
	}

	/**
	 * Allows to actually run the command and clear the screen
	 */
	@Override
	public void run() {
		writer.clear();
	}

	/**
	 * Allows to get the text to be used for describing the action
	 */
	@Override
	public String getText() {
		return ClicMessages.CONSOLE_CLEAR.value();
	}

}
