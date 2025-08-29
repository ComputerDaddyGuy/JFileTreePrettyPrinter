package io.github.computerdaddyguy.jfiletreeprinter.core.visitor;

import io.github.computerdaddyguy.jfiletreeprinter.core.visitor.depth.Depth;
import io.github.computerdaddyguy.jfiletreeprinter.core.visitor.depth.DepthFormatter;
import io.github.computerdaddyguy.jfiletreeprinter.core.visitor.depth.DepthSymbol;
import io.github.computerdaddyguy.jfiletreeprinter.core.visitor.filename.FileNameFormatter;
import io.github.computerdaddyguy.jfiletreeprinter.core.visitor.limit.ChildVisitCounter;
import io.github.computerdaddyguy.jfiletreeprinter.core.visitor.limit.ChildrenLimitConfig;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
class DefaultFileTreePrettyPrintVisitor implements FileTreePrettyPrintVisitor {

	private final DepthFormatter depthFormatter;
	private final ChildrenLimitConfig childrenLimitConfig;
	private final FileNameFormatter fileNameFormatter;

	private StringBuilder buff;
	private Depth depth;
	private ChildVisitCounter counter;

	public DefaultFileTreePrettyPrintVisitor(
		DepthFormatter depthFormatter,
		ChildrenLimitConfig childrenLimitConfig, FileNameFormatter fileNameFormatter
	) {
		super();
		this.depthFormatter = depthFormatter;
		this.childrenLimitConfig = childrenLimitConfig;
		this.fileNameFormatter = fileNameFormatter;

		reset();
	}

	@Override
	public String getResult() {
		return buff.toString();
	}

	@Override
	public void reset() {
		this.buff = new StringBuilder();
		this.depth = new Depth();
		this.counter = new ChildVisitCounter(childrenLimitConfig.getChildrenLimitFunction());
	}

	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
		// DEPTH
		updateDepth(dir);
		_appendBuff_depth();
		depth = depth.append(DepthSymbol.NON_LAST_FILE); // assume not last until proven otherwise

		// FILE
		_appendBuff_path(fileNameFormatter.formatDirectoryBegin(dir, attrs));

		// COUNTER
		counter.registerChildVisitInCurrentDir(dir);
		counter.enterNewDirectory(dir);

		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		// COUNTER
		if (counter.exceedsCurrentLimit()) {
			return FileVisitResult.SKIP_SIBLINGS;
		}
		counter.registerChildVisitInCurrentDir(file);

		// DEPTH
		updateDepth(file);
		_appendBuff_depth();

		// FILE
		_appendBuff_path(fileNameFormatter.formatFile(file, attrs));

		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFileFailed(Path file, @Nullable IOException exc) throws IOException {
		// DEPTH
		updateDepth(file);
		_appendBuff_depth();

		// FILE
		if (exc != null) {
			_appendBuff_path(fileNameFormatter.formatFileException(file, exc));
		}

		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult postVisitDirectory(Path dir, @Nullable IOException exc) throws IOException {

		if (exc != null) {
			_appendBuff_path(fileNameFormatter.formatDirectoryException(dir, exc));
		}

		// DEPTH
		depth = depth.pop();

		// COUNTER
		var limitReached = counter.exceedsCurrentLimit();
		if (limitReached) {
			depth = depth.append(DepthSymbol.LAST_FILE);
			_appendBuff_depth();
			_appendBuff_path(
				childrenLimitConfig.getFormatter().formatLimitReached(counter.notVisitedInCurrentDirCount())
			);
			depth = depth.pop();
		}
		counter.exitCurrentDirectory();

		if (limitReached) {
			return FileVisitResult.SKIP_SIBLINGS;
		}
		return FileVisitResult.CONTINUE;
	}

	private void updateDepth(Path path) {
		if (depth.isEmpty()) {
			return;
		}

		var isLast = isLastChild(path);
		depth = depth.pop();
		depth = depth.append(isLast ? DepthSymbol.LAST_FILE : DepthSymbol.NON_LAST_FILE);
	}

	private boolean isLastChild(Path path) {
		Path parent = path.getParent();
		File[] siblings = parent.toFile().listFiles();
		return siblings != null && siblings[siblings.length - 1].toPath().equals(path);
	}

	private void _appendBuff_depth() {
		buff.append(depthFormatter.format(depth));
	}

	private void _appendBuff_path(@Nullable String str) {
		if (str != null) {
			buff.append(str).append('\n');
		}
	}

}
