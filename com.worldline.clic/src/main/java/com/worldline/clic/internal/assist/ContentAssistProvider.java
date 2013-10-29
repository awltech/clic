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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

/**
 * This class allows to deal with autocompletion for all commands.
 * 
 * @author mvanbesien
 * @since 1.0
 * @version 1.0
 */
public class ContentAssistProvider {

	private final StyledText text;
	private final IProcessor[] processors;
	private final Map<String, Object> properties = new HashMap<String, Object>();
	private IStructuredContentProvider assistContentProvider;

	private Listener textKeyListener;
	private Listener assistTablePopulationListener;
	private Listener onSelectionListener;
	private Listener onEscapeListener;
	private IDoubleClickListener tableDoubleClickListener;
	private Listener focusOutListener;
	private Listener vanishListener;
	private Table table;
	private TableViewer viewer;
	private Shell popupShell;

	public ContentAssistProvider(final StyledText text,
			final IProcessor... processors) {
		this.text = text;
		this.processors = processors;
		build();
	}

	public void setProperty(final String key, final Object value) {
		properties.put(key, value);
	}

	private void build() {
		// Creation of graphical elements
		final Display display = text.getDisplay();
		popupShell = new Shell(display, SWT.ON_TOP);
		popupShell.setLayout(new FillLayout());
		table = new Table(popupShell, SWT.SINGLE);
		viewer = new TableViewer(table);

		assistContentProvider = newAssistContentProvider();
		textKeyListener = newTextKeyListener();
		assistTablePopulationListener = newAssistTablePopulationListener();
		onSelectionListener = newOnSelectionListener();
		onEscapeListener = newOnEscapeListener();
		tableDoubleClickListener = newDoubleClickListener();
		focusOutListener = newFocusOutListener();
		vanishListener = newVanishListener();

		viewer.setContentProvider(assistContentProvider);
		text.addListener(SWT.KeyDown, textKeyListener);
		text.addListener(SWT.Modify, assistTablePopulationListener);
		table.addListener(SWT.DefaultSelection, onSelectionListener);
		table.addListener(SWT.KeyDown, onEscapeListener);
		viewer.addDoubleClickListener(tableDoubleClickListener);
		table.addListener(SWT.FocusOut, focusOutListener);
		text.addListener(SWT.FocusOut, focusOutListener);
		text.getShell().addListener(SWT.Move, vanishListener);

		text.addDisposeListener(newDisposeListener());
	}

	private DisposeListener newDisposeListener() {
		return new DisposeListener() {

			@Override
			public void widgetDisposed(final DisposeEvent e) {
				text.removeListener(SWT.KeyDown, textKeyListener);
				text.removeListener(SWT.Modify, assistTablePopulationListener);
				table.removeListener(SWT.DefaultSelection, onSelectionListener);
				table.removeListener(SWT.KeyDown, onEscapeListener);
				viewer.removeDoubleClickListener(tableDoubleClickListener);
				table.removeListener(SWT.FocusOut, focusOutListener);
				text.removeListener(SWT.FocusOut, focusOutListener);
				text.getShell().removeListener(SWT.Move, vanishListener);

				table.dispose();
				popupShell.dispose();

			}
		};
	}

	private IStructuredContentProvider newAssistContentProvider() {

		return new IStructuredContentProvider() {

			@Override
			public void inputChanged(final Viewer viewer,
					final Object oldInput, final Object newInput) {
			}

			@Override
			public void dispose() {
			}

			@Override
			public Object[] getElements(final Object inputElement) {
				final Collection<String> results = new ArrayList<String>();
				if (inputElement instanceof String) {
					final String input = (String) inputElement;
					final ProcessorContext pc = new ProcessorContext(input,
							text.getCaretOffset(), properties);
					for (final IProcessor processor : processors)
						results.addAll(processor.getProposals(pc));
				}
				return results.toArray();
			}
		};
	}

	private Listener newAssistTablePopulationListener() {
		return new Listener() {

			@Override
			public void handleEvent(final Event event) {
				if (event.widget instanceof Text) {
					final Text text = (Text) event.widget;
					final String string = text.getText();
					if (string.length() == 0)
						// if (popupShell.isVisible())
						popupShell.setVisible(false);
					else {
						viewer.setInput(string);
						final Rectangle textBounds = text.getDisplay().map(
								text.getParent(), null, text.getBounds());
						popupShell.setBounds(textBounds.x, textBounds.y
								+ textBounds.height, textBounds.width, 80);
						// if (!popupShell.isVisible())
						popupShell.setVisible(true);
					}
				}
			}
		};
	}

	private Listener newVanishListener() {
		return new Listener() {

			@Override
			public void handleEvent(final Event event) {
				popupShell.setVisible(false);
			}
		};
	}

	private Listener newTextKeyListener() {
		return new Listener() {

			@Override
			public void handleEvent(final Event event) {
				switch (event.keyCode) {
				case SWT.ARROW_DOWN:
					int index = (table.getSelectionIndex() + 1)
							% table.getItemCount();
					table.setSelection(index);
					event.doit = false;
					break;
				case SWT.ARROW_UP:
					index = table.getSelectionIndex() - 1;
					if (index < 0)
						index = table.getItemCount() - 1;
					table.setSelection(index);
					event.doit = false;
					break;
				case SWT.ARROW_RIGHT:
					if (popupShell.isVisible()
							&& table.getSelectionIndex() != -1) {
						text.setText(table.getSelection()[0].getText());
						text.setSelection(text.getText().length());
						popupShell.setVisible(false);
					}
					break;
				case SWT.ESC:
					popupShell.setVisible(false);
					break;
				}

			}

		};
	}

	private IDoubleClickListener newDoubleClickListener() {
		return new IDoubleClickListener() {

			@Override
			public void doubleClick(final DoubleClickEvent event) {
				if (popupShell.isVisible() && table.getSelectionIndex() != -1) {
					text.setText(table.getSelection()[0].getText());
					text.setSelection(text.getText().length());
					popupShell.setVisible(false);
				}
			}
		};
	}

	private Listener newOnSelectionListener() {
		return new Listener() {

			@Override
			public void handleEvent(final Event event) {
				if (event.widget instanceof Text) {
					final Text text = (Text) event.widget;

					final ISelection selection = viewer.getSelection();
					if (selection instanceof IStructuredSelection) {
						text.setText(((IStructuredSelection) selection)
								.getFirstElement().toString());
						text.setSelection(text.getText().length() - 1);
					}
					popupShell.setVisible(false);
				}

			}
		};
	}

	private Listener newOnEscapeListener() {
		return new Listener() {

			@Override
			public void handleEvent(final Event event) {
				if (event.keyCode == SWT.ESC)
					popupShell.setVisible(false);
			}
		};
	}

	private Listener newFocusOutListener() {
		return new Listener() {

			@Override
			public void handleEvent(final Event event) {
				popupShell.setVisible(false);
			}
		};
	}

}
