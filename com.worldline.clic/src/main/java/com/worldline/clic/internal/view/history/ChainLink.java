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
 * Implementation of a link of a chained list. Gives methods for browsing it,
 * and modifying it.
 * 
 * @author mvanbesien
 * 
 * @param <T>
 */
public class ChainLink<T> {

	/**
	 * Next element
	 */
	private ChainLink<T> next;

	/**
	 * Previous element
	 */
	private ChainLink<T> previous;

	/**
	 * Value element
	 */
	private T value;

	/**
	 * Creates a new chain link, holding the value passed as parameter.
	 * 
	 * @param value
	 */
	public ChainLink(T value) {
		this.value = value;
	}

	/**
	 * Returns the next link in the chain this instance is involved in.
	 * 
	 * @return
	 */
	public ChainLink<T> next() {
		return this.next;
	}

	/**
	 * Returns the previous link in the chain this instance is involved in.
	 * 
	 * @return
	 */
	public ChainLink<T> previous() {
		return this.previous;
	}

	/**
	 * Returns the value hold by the chain
	 * 
	 * @return
	 */
	public T value() {
		return this.value;
	}

	/**
	 * Returns the size of the chain, this instance is involved in.
	 * 
	 * @return
	 */
	public int getSize() {
		int elements = 0;
		ChainLink<T> temp = this;
		while (temp.previous != null) {
			elements++;
			temp = temp.previous;
		}
		temp = this;
		while (temp.next != null) {
			elements++;
			temp = temp.next;
		}
		return elements + 1;
	}

	/**
	 * Returns the last link of the chain, this instance is involved in.
	 * 
	 * @return
	 */
	public ChainLink<T> getLast() {
		ChainLink<T> temp = this;
		while (temp.next != null) {
			temp = temp.next;
			if (temp == this)
				throw new IllegalStateException("Cannot get last as cycle is detected !");
		}
		return temp;
	}

	/**
	 * Returns the first link of the chain, this instance is involved in.
	 * 
	 * @return
	 */
	public ChainLink<T> getFirst() {
		ChainLink<T> temp = this;
		while (temp.previous != null) {
			temp = temp.previous;
			if (temp == this) {
				throw new IllegalStateException("Cannot get first as cycle is detected !");
			}
		}
		return temp;
	}

	/**
	 * Removes the last element of the chain, this instance is involved in, and
	 * returns it.
	 * 
	 * @return
	 */
	public ChainLink<T> removeLast() {
		ChainLink<T> last = this.getLast();

		if (last.previous != null) {
			last.previous.next = null;
			last.previous = null;
		}

		return last;
	}

	/**
	 * Removes the first element of the chain, this instance is involved in, and
	 * returns it.
	 * 
	 * @return
	 */
	public ChainLink<T> removeFirst() {

		ChainLink<T> first = this.getFirst();

		if (first.next != null) {
			first.next.previous = null;
			first.next = null;
		}

		return first;
	}

	/**
	 * Add the provided element as the first element of the chain, this instance
	 * is involved in
	 * 
	 * @param newChainLink
	 */
	public void addFirst(ChainLink<T> newChainLink) {
		if (isInChain(newChainLink)) {
			throw new IllegalStateException("Cannot add a link which is already in the chain.");
		}
		ChainLink<T> first = this.getFirst();
		first.previous = newChainLink;
		newChainLink.next = first;
	}

	/**
	 * Add the provided element as the last element of the chain, this instance
	 * is involved in.
	 * 
	 * @param newChainLink
	 */
	public void addLast(ChainLink<T> newChainLink) {
		if (isInChain(newChainLink)) {
			throw new IllegalStateException("Cannot add a link which is already in the chain.");
		}

		ChainLink<T> last = this.getLast();
		last.next = newChainLink;
		newChainLink.previous = last;
	}

	/**
	 * Adds the provided element after the current one, in the chain it is
	 * involved in.
	 * 
	 * @param newChainLink
	 */
	public void insertAfter(ChainLink<T> newChainLink) {
		if (isInChain(newChainLink)) {
			throw new IllegalStateException("Cannot add a link which is already in the chain.");
		}
		// We take the one after the insertion
		ChainLink<T> next = this.next;

		// Update of the current structure to add the link;
		if (this.next != null) {
			this.next = newChainLink;
		}
		next.previous = newChainLink;

		// Update the link itself.
		newChainLink.previous = this;
		newChainLink.next = next;

	}

	/**
	 * Adds the provided element before the current one, in the chain it is
	 * involved in.
	 * 
	 * @param newChainLink
	 */
	public void insertBefore(ChainLink<T> newChainLink) {
		if (isInChain(newChainLink)) {
			throw new IllegalStateException("Cannot add a link which is already in the chain.");
		}

		// We take the one after the insertion
		ChainLink<T> previous = this.previous;

		// Update of the current structure to add the link;
		this.previous = newChainLink;
		if (previous != null) {
			previous.next = newChainLink;
		}

		// Update the link itself.
		newChainLink.next = this;
		newChainLink.previous = previous;
	}

	/**
	 * Returns true if the element passed as parameter is in the same chain as
	 * the current instance.
	 * 
	 * @param chainLink
	 * @return
	 */
	public boolean isInChain(ChainLink<T> chainLink) {
		// We prevent the validation of something easy...
		if (chainLink == null) {
			return false;
		}

		if (chainLink == this) {
			return true;
		}

		// We look for the link in the chain before this
		ChainLink<T> temp = this;
		while (temp.previous != null) {
			temp = temp.previous;
			if (temp == chainLink) {
				return true;
			}
		}

		// We look for the link in the chain after this
		temp = this;
		while (temp.next != null) {
			temp = temp.next;
			if (temp == chainLink) {
				return true;
			}
		}

		// Not found ! we return false
		return false;
	}

}
