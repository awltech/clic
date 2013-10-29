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

import java.util.Collection;

/**
 * This {@link IProcessor} interface allows to specify the methods you should
 * declare in order to allow autocomplete
 * 
 * @author mvanbesien
 * @version 1.0
 * @since 1.0
 */
public interface IProcessor {

	/**
	 * Allows to compute all the proposals to display to the end-user from a
	 * given {@link ProcessorContext}
	 * 
	 * @param context
	 *            the {@link ProcessorContext} which is used while matching the
	 *            proposals for commands autocomplete
	 * @return a {@link Collection} containing all the proposals for a given
	 *         {@link ProcessorContext}
	 */
	public Collection<String> getProposals(ProcessorContext context);

}
