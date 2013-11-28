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
package com.worldline.clic.internal.view.history;

/**
 * Command history implementation. contains references to the previously entered
 * commands, and proposes API to browse them in a chained way
 * 
 * @author mvanbesien
 * @since 1.0
 */
public class CommandHistory {

	/**
	 * First link in the command history chain
	 */
	private ChainLink<String> first;

	/**
	 * Currently processed link in the command history chain
	 */
	private ChainLink<String> current;

	/**
	 * Size limit of the history
	 */
	private int maxSize;

	/**
	 * Creates new command history with the size limit, as provided
	 * 
	 * @param maxSize
	 */
	public CommandHistory(int maxSize) {
		this.maxSize = maxSize;
	}

	/**
	 * Adds a new command in the history. This action has a side impact, which
	 * consists in resetting the browsing cursor to the first one (aKa the one
	 * which is just being added now...).
	 * 
	 * @param command
	 */
	public void addCommand(String command) {
		ChainLink<String> link = new ChainLink<String>(command);
		if (first != null) {
			first.addFirst(link);
		}
		first = link;
		current = first;

		while (first.getSize() > maxSize) {
			first.removeLast();
		}
	}

	/**
	 * Returns the next command in the list. meaning the one that was typed just
	 * after the one being browsed.
	 * 
	 * @return
	 */
	public String getNextCommand() {
		// Be careful ! next command means one typed later than current, so it
		// is previous in the list !
		if (current == null) {
			return null;
		}
		if (current.previous() != null) {
			current = current.previous();
			return current != null ? current.value() : null;
		} else {
			return null;
		}
	}

	/**
	 * Returns the next command in the list. meaning the one that was typed just
	 * before the one being browsed.
	 * 
	 * @return
	 */
	public String getPreviousCommand() {
		// Be careful ! previous command means one typed sooner than current, so
		// it is next in the list !
		if (current == null) {
			return null;
		}
		if (current.next() != null) {
			current = current.next();
			return current != null ? current.value() : null;
		} else
			return null;
	}

}
