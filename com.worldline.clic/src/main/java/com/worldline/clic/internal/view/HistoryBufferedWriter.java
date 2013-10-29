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

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Display;

/**
 * The {@link HistoryBufferedWriter} is an extension of {@link Writer} allowing
 * to interact with the GUI in order to display some information during the
 * commands' execution.
 * 
 * @author mvanbesien / aneveux
 * @version 1.0
 * @since 1.0
 * 
 * @see Writer
 */
final class HistoryBufferedWriter extends Writer {

	/**
	 * A {@link StyledText} instance to be used in order to display the
	 * information on the console
	 */
	private final StyledText text;

	/**
	 * an indication of the buffer limit
	 */
	private final int bufferLimit;

	/**
	 * Constructor
	 * 
	 * @param text
	 *            {@link #text}
	 * @param bufferLimit
	 *            {@link #bufferLimit}
	 */
	public HistoryBufferedWriter(final StyledText text, final int bufferLimit) {
		this.text = text;
		this.bufferLimit = bufferLimit;
	}

	/**
	 * Allows to write a {@link String} on the {@link Writer} linked to the
	 * console's GUI
	 */
	@Override
	public void write(final String line) {
		final Display display = text.getDisplay();
		display.syncExec(new Runnable() {

			@Override
			public void run() {
				String concat = text.getText().concat(line + "\r\n");
				if (concat.length() > bufferLimit) {
					final int index = concat.indexOf("\n", concat.length()
							- bufferLimit);
					concat = concat.substring(index + 1);
				}
				text.setText(concat);

				final List<StyleRange> ranges = new ArrayList<>();
				int index = 0;
				while (index != -1) {
					index = concat.indexOf(">", index);
					if (index > -1) {
						int end = concat.indexOf("\r\n", index);
						if (end == -1)
							end = concat.length();
						final StyleRange range = new StyleRange();
						range.start = index;
						range.length = end - index;
						range.fontStyle = range.fontStyle | SWT.BOLD;
						ranges.add(range);
						index = end;
					}
				}

				text.setStyleRanges(ranges.toArray(new StyleRange[ranges.size()]));

				text.setSelection(concat.length());
				text.update();
			}
		});
	}

	/**
	 * Allows to clear the history buffer in order to remove all the things
	 * written on the GUI
	 */
	public void clear() {
		final Display display = text.getDisplay();
		display.syncExec(new Runnable() {

			@Override
			public void run() {
				text.setText("");
				text.update();
			}
		});
	}

	/**
	 * Allows to close the writer
	 */
	@Override
	public void close() throws IOException {
		// does nothing
	}

	/**
	 * Allows to flush the writer
	 */
	@Override
	public void flush() throws IOException {
		// Does nothing
	}

	/**
	 * Allows to write in the writer
	 */
	@Override
	public void write(final char[] cbuf, final int off, final int len)
			throws IOException {
		this.write(String.valueOf(cbuf).substring(off, off + len));
	}
}