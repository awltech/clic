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
package com.worldline.clic.mvn.commands;

import static com.worldline.clic.mvn.ClicMavenMessages.*;
import static joptsimple.util.RegexMatcher.regex;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Properties;
import joptsimple.OptionSpec;
import joptsimple.util.KeyValuePair;

import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationOutputHandler;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;

import com.google.common.io.Files;
import com.worldline.clic.commands.AbstractCommand;
import com.worldline.clic.commands.CommandContext;
import com.worldline.clic.mvn.Activator;

/**
 * This extension of {@link AbstractCommand} allows to execute tasks based on
 * Maven. It can deal both with tasks defined in a pom.xml file, or with direct
 * executions of Maven plugins.
 * 
 * @author aneveux
 * @version 1.0
 * @since 1.0
 */
public class MavenCommand extends AbstractCommand {

	/**
	 * This {@link InvocationRequest} will actually be enriched while computing
	 * the provided parameters, in order to construct the Maven request to be
	 * executed.
	 */
	private final InvocationRequest mavenRequest;

	/**
	 * This {@link OptionSpec} contains all the Maven parameters provided using
	 * the syntax <code>-Dparam=value</code>
	 */
	private OptionSpec<KeyValuePair> mavenParameters;

	/**
	 * This {@link OptionSpec} contains the Maven reference which is provided by
	 * the user using the format <code>groupId:artifactId:version</code>
	 */
	private OptionSpec<String> mavenReference;

	/**
	 * This {@link OptionSpec} contains the Maven command which should be
	 * executed on top of the parameters provided
	 */
	private OptionSpec<String> mavenCommand;

	public MavenCommand() {
		mavenRequest = new DefaultInvocationRequest();
	}

	/**
	 * Configures the parser and specifies all the options which are available
	 * for the command. It allows to perform validation of all the command line.
	 */
	@Override
	public void configureParser() {
		mavenReference = parser
				.accepts(MAVEN_REFERENCE.value(),
						MAVEN_REFERENCE_DESCRIPTION.value())
				.withRequiredArg()
				.describedAs(MAVEN_REFERENCE_ARG.value())
				.ofType(String.class)
				.withValuesConvertedBy(
						regex("([a-zA-Z_0-9-_.])+[:]([a-zA-Z_0-9-_.])+[:]([a-zA-Z_0-9-_.])+"))
				.required();
		parser.accepts(GENERATE_POM.value(), GENERATE_POM_DESCRIPTION.value());
		mavenCommand = parser
				.accepts(MAVEN_CMD.value(), MAVEN_CMD_DESCRIPTION.value())
				.withRequiredArg()
				.describedAs(MAVEN_CMD_ARG.value())
				.ofType(String.class)
				.withValuesConvertedBy(
						regex("([a-zA-Z_0-9-_.])+([:]([a-zA-Z_0-9-_.])+)?"))
				.ofType(String.class).required();
		mavenParameters = parser
				.accepts(JVM_PARAM.value(), JVM_PARAM_DESCRIPTION.value())
				.withRequiredArg().describedAs(JVM_PARAM_ARG.value())
				.ofType(KeyValuePair.class);
	}

	/**
	 * Executes the command. Two modes are available depending if the Maven
	 * command needs to be executed from a pom.xml file or not.
	 */
	@Override
	public void execute(final CommandContext context) {
		final MavenReference reference = new MavenReference(
				options.valueOf(mavenReference));
		// Depending if pom generation is needed, we don't set the maven command
		// & pom in the same way
		if (options.has(GENERATE_POM.value())) {
			context.write(POM_GENERATION.value());
			generatePomFile(context, reference);
			generatePomCommand();
		} else {
			// 1. Set tmp basedir
			mavenRequest.setBaseDirectory(Files.createTempDir());
			// 2. Generate the command
			generateCommand(context, reference);
		}
		// Then, we need to compute all the provided parameters for Maven
		computeMavenParameters(context);
		// And finally execute the command
		executeMavenRequest(context);
	}

	/**
	 * Allows to generate a temporary pom.xml file based on information provided
	 * in the command line, and stores related information in the
	 * {@link #mavenRequest} to be used
	 * 
	 * @param context
	 *            execution context allowing to write on the console
	 * @param reference
	 *            Maven reference to be used for generating the pom.xml file
	 */
	private void generatePomFile(final CommandContext context,
			final MavenReference reference) {
		final File tmpdir = Files.createTempDir();
		final File pom = new File(tmpdir.getAbsolutePath() + "/pom.xml");
		context.write(POM_CREATED.value(pom.getAbsolutePath()));
		final String pomContent = POM_TEMPLATE.value(reference.groupId,
				reference.artifactId, reference.version, "tmp");
		try {
			Files.newOutputStreamSupplier(pom).getOutput()
					.write(pomContent.getBytes());
		} catch (final IOException e) {
			context.write(POM_ERROR.value(e.getMessage()));
			Activator.sendErrorToErrorLog(POM_ERROR.value(e.getMessage()), e);
		}
		mavenRequest.setBaseDirectory(tmpdir);
		mavenRequest.setPomFile(pom);
	}

	/**
	 * Adds in {@link #mavenRequest} the Maven command to be executed based on
	 * what has been provided by the user
	 */
	private void generatePomCommand() {
		mavenRequest.setGoals(Collections.singletonList(options
				.valueOf(mavenCommand)));
	}

	/**
	 * Adds in {@link #mavenRequest} the Maven command to be executed if no pom
	 * has to be used
	 * 
	 * @param context
	 *            the command context used for writing in the console
	 * @param reference
	 *            the {@link MavenReference} to be used for generating the
	 *            command
	 */
	private void generateCommand(final CommandContext context,
			final MavenReference reference) {
		final String cmd = reference.toString() + ":"
				+ options.valueOf(mavenCommand);
		context.write(COMMAND_GENERATION.value(cmd));
		mavenRequest.setGoals(Collections.singletonList(cmd));
	}

	/**
	 * Computes all the maven parameters provided in the command line and adds
	 * them in the {@link #mavenRequest}
	 * 
	 * @param context
	 *            the command context used for writing in the console
	 */
	private void computeMavenParameters(final CommandContext context) {
		if (!options.has(mavenParameters)) {
			context.write(NO_PARAMETERS.value());
			return;
		}
		context.write(PARAMETERS_FOUND.value(options.valuesOf(mavenParameters)
				.size()));
		final Properties props = new Properties();
		for (final KeyValuePair pair : options.valuesOf(mavenParameters))
			props.put(pair.key, pair.value);
		mavenRequest.setProperties(props);
	}

	/**
	 * Runs Maven in order to execute the {@link #mavenRequest} which has been
	 * configured
	 * 
	 * @param context
	 *            the command context for writing in the console
	 */
	private void executeMavenRequest(final CommandContext context) {
		final Invoker invoker = new DefaultInvoker();
		try {
			invoker.setOutputHandler(new ContextOutputHandler(context));
			invoker.execute(mavenRequest);
		} catch (final MavenInvocationException e) {
			context.write(MAVEN_EXEC_ERROR.value(e.getMessage()));
			Activator.sendErrorToErrorLog(
					MAVEN_EXEC_ERROR.value(e.getMessage()), e);
			return;
		}
	}

	/**
	 * Wrapper allowing to store a Maven reference
	 * 
	 * @author aneveux
	 * @version 1.0
	 * @since 1.0
	 */
	static class MavenReference {
		public MavenReference(final String ref) {
			groupId = ref.split(":")[0];
			artifactId = ref.split(":")[1];
			version = ref.split(":")[2];
		}

		public String groupId;
		public String artifactId;
		public String version;

		@Override
		public String toString() {
			return groupId + ":" + artifactId + ":" + version;
		}
	}

	/**
	 * Custom {@link InvocationOutputHandler} allowing to write the execution
	 * output in a {@link CommandContext}
	 * 
	 * @author aneveux
	 * @version 1.0
	 * @since 1.0
	 */
	static class ContextOutputHandler implements InvocationOutputHandler {
		CommandContext context;

		public ContextOutputHandler(final CommandContext ctx) {
			context = ctx;
		}

		@Override
		public void consumeLine(final String line) {
			context.write(line);
		}
	}

}
