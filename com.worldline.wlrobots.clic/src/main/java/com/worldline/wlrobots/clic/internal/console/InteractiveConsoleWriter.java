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
package com.worldline.wlrobots.clic.internal.console;

import java.io.IOException;
import java.io.Writer;

import org.eclipse.ui.console.IOConsoleOutputStream;

/**
 * This extension of a {@link Writer} allows to define a Writer to be used for
 * the {@link InteractiveConsole}
 * 
 * @author mvanbesien / aneveux
 * @version 1.0
 * @since 1.0
 */
public class InteractiveConsoleWriter extends Writer {

	/**
	 * The {@link IOConsoleOutputStream} used by the console in order to write
	 * information
	 */
	private final IOConsoleOutputStream outputStream;

	/**
	 * Constructor
	 * 
	 * @param outputStream
	 *            the {@link IOConsoleOutputStream} used by the console
	 */
	InteractiveConsoleWriter(final IOConsoleOutputStream outputStream) {
		this.outputStream = outputStream;
	}

	/**
	 * Allows to close the {@link #outputStream}
	 */
	@Override
	public void close() throws IOException {
		outputStream.close();
	}

	/**
	 * Allows to flush the {@link #outputStream}
	 */
	@Override
	public void flush() throws IOException {
		outputStream.flush();
	}

	/**
	 * Allows to write information on the {@link #outputStream}
	 */
	@Override
	public void write(final char[] cbuf, final int off, final int len)
			throws IOException {
		final String valueOf = String.valueOf(cbuf);
		this.write(valueOf.substring(off, off + len));
	}

	/**
	 * Allows to write information on the {@link #outputStream}
	 */
	@Override
	public void write(final String str) throws IOException {
		outputStream.write(str + "\n");
	}

	/**
	 * Allows to write information on the {@link #outputStream}
	 */
	@Override
	public void write(final char[] cbuf) throws IOException {
		outputStream.write(String.valueOf(cbuf));
	}

	/**
	 * Allows to write information on the {@link #outputStream}
	 */
	@Override
	public void write(final int c) throws IOException {
		outputStream.write(c);
	}

	/**
	 * Allows to write information on the {@link #outputStream}
	 */
	@Override
	public void write(final String str, final int off, final int len)
			throws IOException {
		this.write(str.substring(off, off + len));
	}
}
