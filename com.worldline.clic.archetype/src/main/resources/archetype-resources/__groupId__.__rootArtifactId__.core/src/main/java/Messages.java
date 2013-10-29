#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${groupId}.${rootArtifactId}.core;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * Allows to store i18n messages
 * 
 * @author aneveux
 * @version 1.0
 * @since 1.0
 */
public enum Messages {

	// Messages

	HELLO

	;

	/**
	 * ResourceBundle instance
	 */
	private static ResourceBundle resourceBundle = ResourceBundle
			.getBundle("Messages");

	/**
	 * Returns value of the message
	 */
	public String value() {
		if (Messages.resourceBundle == null
				|| !Messages.resourceBundle.containsKey(name()))
			return "!!" + name() + "!!";
		return Messages.resourceBundle.getString(name());
	}

	/**
	 * Returns value of the formatted message
	 */
	public String value(final Object... args) {
		if (Messages.resourceBundle == null
				|| !Messages.resourceBundle.containsKey(name()))
			return "!!" + name() + "!!";
		return MessageFormat.format(
				Messages.resourceBundle.getString(name()), args);
	}

}