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
package com.worldline.wlrobots.clic.internal.view;

import java.io.Writer;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;

import com.worldline.wlrobots.clic.internal.ClicMessages;
import com.worldline.wlrobots.clic.internal.commands.CommandProcessor;

/**
 * This extension of a {@link JobChangeAdapter} allows to get information about
 * the execution status of a particular command
 * 
 * @author mvanbesien / aneveux
 * @version 1.0
 * @since 1.0
 * 
 * @see JobChangeAdapter
 */
public class CommandProcessorFinalizer extends JobChangeAdapter {

	/**
	 * The {@link CommandProcessor} which is used in order to execute a
	 * particular command
	 */
	private final CommandProcessor processor;

	/**
	 * The {@link Writer} which is used in order to write information in the GUI
	 */
	private final HistoryBufferedWriter writer;

	/**
	 * Constructor
	 * 
	 * @param processor
	 *            {@link #processor}
	 * @param writer
	 *            {@link #writer}
	 */
	public CommandProcessorFinalizer(final CommandProcessor processor,
			final HistoryBufferedWriter writer) {
		this.processor = processor;
		this.writer = writer;
	}

	/**
	 * Allows to be notified of the end of a command execution, and write its
	 * status in the console
	 */
	@Override
	public void done(final IJobChangeEvent event) {
		if (processor != null && processor.getResult() != null) {
			final IStatus result = processor.getResult();
			if (result instanceof MultiStatus)
				for (final IStatus status : ((MultiStatus) result)
						.getChildren())
					writer.write(ClicMessages.COMMAND_RETURN.value(status
							.getMessage()));
			else
				writer.write(ClicMessages.COMMAND_RETURN.value(result
						.getMessage()));
		}
	}

}
