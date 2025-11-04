package io.github.computerdaddyguy.jfiletreeprettyprinter.cli;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.computerdaddyguy.jfiletreeprettyprinter.FileTreePrettyPrinter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FileTreePrettyPrinterCommandLineTest {

	private final PrintStream DEFAULT_SYSTEM_OUT = System.out;

	private ByteArrayOutputStream modifiedOut;

	@BeforeEach
	void prepareSystemOut() {
		modifiedOut = new ByteArrayOutputStream();
		System.setOut(new PrintStream(modifiedOut));
	}

	@AfterEach
	void restoreSystemOut() {
		if (modifiedOut != null) {

		}
		System.setOut(DEFAULT_SYSTEM_OUT);
	}

	@Test
	void nominal() throws IOException {
		String targetPath = "src/example/resources/base";
		String[] args = { targetPath };

		FileTreePrettyPrinterCommandLine.executeCommand(args);

		modifiedOut.flush();
		String allWrittenLines = new String(modifiedOut.toByteArray());
		System.setOut(DEFAULT_SYSTEM_OUT);

		assertThat(allWrittenLines).isEqualTo(FileTreePrettyPrinter.createDefault().prettyPrint(targetPath) + "\n");
	}

}
