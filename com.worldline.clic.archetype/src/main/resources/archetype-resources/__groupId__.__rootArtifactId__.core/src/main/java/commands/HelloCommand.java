#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${groupId}.${rootArtifactId}.core.commands;

import ${groupId}.${rootArtifactId}.core.Activator;
import ${groupId}.${rootArtifactId}.core.Messages;
import com.worldline.clic.commands.AbstractCommand;
import com.worldline.clic.commands.CommandContext;

/**
 * Just an example of a command...
 * 
 * @author aneveux
 * @version 1.0
 * @since 1.0
 */
public class HelloCommand extends AbstractCommand {

	/**
	 * Allows to configure the parser and specify all the options which are
	 * available for the command. It allows to perform validation of all the
	 * command line.
	 */
	@Override
	public void configureParser() {
		parser.accepts("name")
				.withRequiredArg()
				.ofType(String.class)
				.required();
	}

	/**
	 * Allows to deal with the command's execution...
	 */
	@Override
	public void execute(final CommandContext context) {
		context.write(Messages.HELLO.value(options.valueOf("name")));
	}

}
