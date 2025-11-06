package io.github.computerdaddyguy.jfiletreeprettyprinter.cli.io;

import org.jspecify.annotations.NullMarked;

/**
 * An interface abstracting the console output - usefull to unit test the CLI output!
 */
@NullMarked
public interface ConsoleOutput {

	boolean isDebugEnabled();

	void enableDebug(boolean enabled);

	void print(String msg, Object... args);

	void printDebug(String msg, Object... args);

	void printError(String msg, Object... args);

	public static ConsoleOutput createDefault() {
		return new DefaultConsoleOutput(System.out, System.err);
	}

}
