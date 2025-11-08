package io.github.computerdaddyguy.jfiletreeprettyprinter.cli.io;

import java.io.PrintStream;
import java.util.Objects;
import java.util.logging.Logger;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class DefaultConsoleOutput implements ConsoleOutput {

	private static final Logger ROOT_LOGGER = Logger.getLogger("");

	private boolean debug;
	private final PrintStream out;
	private final PrintStream err;

	public DefaultConsoleOutput(PrintStream out, PrintStream err) {
		this.out = Objects.requireNonNull(out, "out stream is null");
		this.err = Objects.requireNonNull(err, "err stream is null");
		disableAllLoggers();
	}

	/**
	 * Require otherwise some embedded loggers (i.e. (Hibernate validator) print in the console.
	 */
	private void disableAllLoggers() {
		ROOT_LOGGER.setLevel(java.util.logging.Level.OFF);
		Logger.getGlobal().setLevel(java.util.logging.Level.OFF);
		Logger.getLogger("org.hibernate.validator").setLevel(java.util.logging.Level.OFF);
	}

	// ---------------------------------------------------------

	@Override
	public boolean isDebugEnabled() {
		return this.debug;
	}

	@Override
	public void enableDebug(boolean enabled) {
		this.debug = enabled;
	}

	@Override
	public void print(String msg, Object... args) {
		printOut(msg, args);
	}

	@Override
	public void printDebug(String msg, Object... args) {
		// Could not (yet) make Logger level work within native image, so fallback to plain console output (for now)
		if (debug) {
			printOut("[DEBUG] " + msg, args);
		}
	}

	@Override
	public void printError(String msg, Object... args) {
		printErr("[ERROR] " + msg, args);
	}

	// ---------------------------------------------------------

	private void printOut(String msg, Object... args) {
		printfln(out, msg, args);
	}

	private void printErr(String msg, Object... args) {
		printfln(err, msg, args);
	}

	private void printfln(PrintStream dest, String msg, Object... args) {
		dest.printf(msg + System.lineSeparator(), args);// Because "printf" does not print line return
	}

}
