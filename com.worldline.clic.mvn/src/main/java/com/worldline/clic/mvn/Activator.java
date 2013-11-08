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
package com.worldline.clic.mvn;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.worldline.clic.mvn"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void start(final BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void stop(final BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Sends the message to the Error Log with the INFO severity
	 * 
	 * @param message
	 */
	public static void sendInfoToErrorLog(final String message) {
		Activator.plugin.getLog().log(
				new Status(IStatus.INFO, Activator.PLUGIN_ID, message));
	}

	/**
	 * Sends the message to the Error Log with the WARN severity
	 * 
	 * @param message
	 */
	public static void sendWarningToErrorLog(final String message) {
		Activator.plugin.getLog().log(
				new Status(IStatus.WARNING, Activator.PLUGIN_ID, message));
	}

	/**
	 * Sends the message to the Error Log with the ERROR severity
	 * 
	 * @param message
	 */
	public static void sendErrorToErrorLog(final String message) {
		Activator.plugin.getLog().log(
				new Status(IStatus.ERROR, Activator.PLUGIN_ID, message));
	}

	/**
	 * Sends the message and the Throwable to the Error Log with the ERROR
	 * severity
	 * 
	 * @param message
	 * @param t
	 *            Throwable
	 */
	public static void sendErrorToErrorLog(final String message,
			final Throwable t) {
		Activator.plugin.getLog().log(
				new Status(IStatus.ERROR, Activator.PLUGIN_ID, message, t));
	}

}
