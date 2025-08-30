package io.github.computerdaddyguy.jfiletreeprettyprinter.visitor;

import io.github.computerdaddyguy.jfiletreeprettyprinter.PrettyPrintOptions;
import io.github.computerdaddyguy.jfiletreeprettyprinter.visitor.renderer.LineRenderer;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
class DefaultFileTreePrettyPrintVisitor implements FileTreePrettyPrintVisitor {

	private final LineRenderer lineRenderer;

	private StringBuilder buff;
	private Depth depth;
	private ChildVisitCounter counter;

	public DefaultFileTreePrettyPrintVisitor(PrettyPrintOptions options, LineRenderer lineRenderer) {
		super();
		this.lineRenderer = lineRenderer;

		this.buff = new StringBuilder();
		this.depth = new Depth();
		this.counter = new ChildVisitCounter(options.getChildrenLimitFunction());
	}

	@Override
	public String getResult() {
		return buff.toString();
	}

	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
		// COUNTER
		if (counter.exceedsCurrentLimit()) {
			return FileVisitResult.SKIP_SIBLINGS;
		}

		// DEPTH
		updateDepth(dir);

		// FILE
		appendNewLine(lineRenderer.renderDirectoryBegin(depth, dir, attrs));
		depth = depth.append(DepthSymbol.NON_LAST_FILE); // assume not last until proven otherwise

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

		// FILE
		appendNewLine(lineRenderer.renderFile(depth, file, attrs));

		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFileFailed(Path file, @Nullable IOException exc) throws IOException {
		// DEPTH
		updateDepth(file);

		// FILE
		if (exc != null) {
			appendNewLine(lineRenderer.renderFileException(depth, file, exc));
		}

		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult postVisitDirectory(Path dir, @Nullable IOException exc) throws IOException {

		if (exc != null) {
			appendNewLine(lineRenderer.renderDirectoryException(depth, dir, exc));
		}

		// DEPTH
		depth = depth.pop();

		// COUNTER
		var limitReached = counter.hasSomeNotVisitedChildren();
		if (limitReached) {
			depth = depth.append(DepthSymbol.LAST_FILE);
			appendNewLine(lineRenderer.renderLimitReached(depth, counter.notVisitedInCurrentDir()));
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

	private void appendNewLine(@Nullable String str) {
		if (str != null) {
			if (buff.length() > 0) {
				buff.append('\n');
			}
			buff.append(str);
		}
	}

}
