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

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import com.worldline.clic.commands.CommandContext;
import com.worldline.clic.internal.ClicMessages;
import com.worldline.clic.internal.assist.ContentAssistProcessor;
import com.worldline.clic.internal.commands.CommandProcessor;
import com.worldline.clic.internal.view.history.CommandHistory;

/**
 * This {@link CommandLineClientView} is actually an Eclipse {@link ViewPart}
 * allowing to use a terminal-like GUI to specify commands and get feedbacks
 * from their execution.
 * 
 * It consists in a simple textfield where you specify the commands, and a place
 * where you'll be able to read the results.
 * 
 * @author mvanbesien / aneveux
 * @version 1.0
 * @since 1.0
 * 
 * @see ViewPart
 */
public class CommandLineClientView extends ViewPart {

	/**
	 * The {@link Writer} extension allowing to write in the view
	 */
	private HistoryBufferedWriter writer;

	/**
	 * A {@link StyledText} instance representing the command which has been
	 * specified
	 */
	private StyledText commandText;

	/**
	 * A {@link StyledText} instance representing the commands' executions
	 */
	private StyledText historyText;

	/**
	 * The {@link CommandContext} to be linked to the command's execution
	 */
	private CommandContext context;

	/**
	 * The {@link CommandHistory} used to retrieved previously typed commands.
	 */
	private CommandHistory commandHistory;

	/**
	 * Allows to create all the graphical components to be used in the GUI
	 */
	@Override
	public void createPartControl(final Composite parent) {
		final Composite background = new Composite(parent, SWT.NONE);
		background.setLayout(new FormLayout());

		this.commandText = new StyledText(background, SWT.BORDER);
		new FormDataBuilder().left().bottom().right().apply(commandText);

		this.historyText = new StyledText(background, SWT.READ_ONLY | SWT.MULTI | SWT.BORDER | SWT.V_SCROLL
				| SWT.H_SCROLL);
		new FormDataBuilder().left().right().top().bottom(commandText).apply(historyText);
		this.writer = new HistoryBufferedWriter(historyText, 10000);
		this.context = new CommandContext(writer);
		this.commandHistory = new CommandHistory(50);

		commandText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(final KeyEvent e) {
				if (e.keyCode == SWT.CR && commandText.getText().trim().length() > 0) {
					final String command = commandText.getText().trim();
					commandHistory.addCommand(command);
					writer.write("> " + command);
					commandText.setText("");

					historyText.update();
					commandText.update();

					final CommandProcessor commandProcessor = new CommandProcessor(command, context);
					commandProcessor.addJobChangeListener(new CommandProcessorFinalizer(commandProcessor, writer));
					commandProcessor.schedule();
				} else if (e.keyCode == SWT.TAB || (e.stateMask == SWT.CTRL && e.keyCode == SWT.SPACE)) {
					String initialCommand = commandText.getText().trim();
					int initialCaretOffset = commandText.getCaretOffset();
					if (e.keyCode == SWT.TAB)
						initialCaretOffset--;
					String finalCommand = ContentAssistProcessor.assist(initialCommand, initialCaretOffset);
					int finalCaretOffset = initialCaretOffset + finalCommand.length() - initialCommand.length();

					commandText.setText(finalCommand);
					commandText.setCaretOffset(finalCaretOffset);
					commandText.update();
				} else if (e.keyCode == SWT.ARROW_UP) {
					String newCommand = commandHistory.getPreviousCommand();
					if (newCommand != null) {
						commandText.setText(newCommand);
						commandText.setCaretOffset(newCommand.length());
						commandText.update();
					}
				} else if (e.keyCode == SWT.ARROW_DOWN) {
					String newCommand = commandHistory.getNextCommand();
					if (newCommand != null) {
						commandText.setText(newCommand);
						commandText.setCaretOffset(newCommand.length());
						commandText.update();
					}
				}
			}
		});

		getViewSite().getActionBars().getToolBarManager().add(new HistoryCleanAction(writer));

		writer.write(ClicMessages.CLIC_WELCOME.value());
	}

	@Override
	public void setFocus() {
	}

	@Override
	public void dispose() {
		super.dispose();
	}

}
