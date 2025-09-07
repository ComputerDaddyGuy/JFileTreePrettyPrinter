package io.github.computerdaddyguy.jfiletreeprettyprinter.benchmarks;

import io.github.computerdaddyguy.jfiletreeprettyprinter.FileTreePrettyPrinter;
import io.github.computerdaddyguy.jfiletreeprettyprinter.PrettyPrintOptions.Implementation;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

/**
 * JMH benchmark to compare performance of different FileTreePrettyPrinter implementations.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
public class FileTreePrettyPrinterBenchmark {

	@Param({ "VISITOR", "RECURSIVE" })
	private String implementationName;

	private FileTreePrettyPrinter printer;

	private Path inputPath;

	/**
	 * To run from your IDE.
	 */
	public static void main(String[] args) throws Exception {
		org.openjdk.jmh.Main.main(args);
	}

	@Setup(Level.Trial)
	public void setup() {
		Implementation impl = Implementation.valueOf(implementationName);
		printer = FileTreePrettyPrinter.builder()
			.customizeOptions(options -> options.withImplementation(impl))
			.build();

		inputPath = Path.of("src/example/resources/children_limit_dynamic");
	}

	@Benchmark
	public String benchmarkPrettyPrint() {
		return printer.prettyPrint(inputPath);
	}

}
