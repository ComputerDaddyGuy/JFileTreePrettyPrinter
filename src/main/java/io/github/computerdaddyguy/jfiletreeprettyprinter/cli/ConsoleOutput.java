package io.github.computerdaddyguy.jfiletreeprettyprinter.cli;

import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class ConsoleOutput {

	private static final Logger ROOT_LOGGER = Logger.getLogger("");

	private boolean debug;

	public ConsoleOutput(boolean debug) {
		this.debug = debug;
		disableAllLoggers();
	}

	private void disableAllLoggers() {
		ROOT_LOGGER.setLevel(Level.OFF);
		Logger.getLogger("org.hibernate.validator").setLevel(Level.OFF);
	}

	private void printOut(String msg, Object... args) {
		printfln(System.out, msg, args);
	}

	private void printErr(String msg, Object... args) {
		printfln(System.err, msg, args);
	}

	private void printfln(PrintStream dest, String msg, Object... args) {
		dest.printf(msg + "\n", args);// Because "printf" does not print line return
	}

	public void printDebug(String msg, Object... args) {
		// Could not (yet) make Logger level work within native image, so fallback to plain console output (for now)
		if (debug) {
			printOut("[DEBUG] " + msg, args);
		}
	}

	public void print(String msg, Object... args) {
		printOut(msg, args);
	}

	public void printError(String msg, Object... args) {
		printErr("[ERROR] " + msg, args);
	}

}
