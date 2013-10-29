#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${groupId}.${rootArtifactId};

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "${groupId}.${rootArtifactId}.core"; //${symbol_dollar}NON-NLS-1${symbol_dollar}

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
	 * org.eclipse.ui.plugin.AbstractUIPlugin${symbol_pound}start(org.osgi.framework.BundleContext
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
	 * org.eclipse.ui.plugin.AbstractUIPlugin${symbol_pound}stop(org.osgi.framework.BundleContext
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
