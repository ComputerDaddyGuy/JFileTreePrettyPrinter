package io.github.computerdaddyguy.jfiletreeprettyprinter.visitor;

import io.github.computerdaddyguy.jfiletreeprettyprinter.PrettyPrintOptions;
import io.github.computerdaddyguy.jfiletreeprettyprinter.visitor.renderer.DirectoryInterruptionCause;
import io.github.computerdaddyguy.jfiletreeprettyprinter.visitor.renderer.LineRenderer;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
class DefaultFileTreePrettyPrintVisitor implements FileTreePrettyPrintVisitor {

	private final PrettyPrintOptions options;
	private final LineRenderer lineRenderer;

	private StringBuilder buff;
	private Depth depth;
	private ChildVisitRegister register;

	private List<Path> directoryChain = new ArrayList<>();

	public DefaultFileTreePrettyPrintVisitor(PrettyPrintOptions options, LineRenderer lineRenderer) {
		super();
		this.options = Objects.requireNonNull(options, "options is null");
		this.lineRenderer = Objects.requireNonNull(lineRenderer, "lineRenderer is null");

		this.buff = new StringBuilder();
		this.depth = new Depth();
		this.register = new ChildVisitRegister(options.getChildrenLimitFunction());
	}

	@Override
	public String getResult() {
		return buff.toString();
	}

	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {

		if (register.exceedsCurrentLimit()) {
			return FileVisitResult.SKIP_SIBLINGS;
		}

		directoryChain.add(dir); // Continue chain
		register.registerChildVisitInCurrentDir(dir);
		updateDepthForLastFile(dir);

		if (isMaxDepthReached()) {
			handleMaxDepth(dir, attrs);
			return FileVisitResult.SKIP_SIBLINGS; // Skip -> postVisitDirectory method is not invoked
		}

		register.enterNewDirectory(dir);

		var doRenderDir = true;
		if (options.areCompactDirectoriesUsed()) {
			doRenderDir = !register.hasSingleDirectoryChild(); // Is this folder parent of a single folder?
		}

		if (doRenderDir) {
			appendNewLine(lineRenderer.renderDirectoryBegin(depth, directoryChain, attrs));
			directoryChain.clear();
			depth = depth.append(DepthSymbol.NON_LAST_FILE); // assume not last until proven otherwise
		}

		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

		if (register.exceedsCurrentLimit()) {
			return FileVisitResult.SKIP_SIBLINGS;
		}

		register.registerChildVisitInCurrentDir(file);

		updateDepthForLastFile(file);

		appendNewLine(lineRenderer.renderFile(depth, file, attrs));

		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFileFailed(Path file, @Nullable IOException exc) throws IOException {
		updateDepthForLastFile(file);

		if (exc != null) {
			appendNewLine(lineRenderer.renderFileException(depth, file, exc));
		}

		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult postVisitDirectory(Path dir, @Nullable IOException exc) throws IOException {

		depth = depth.pop();

		FileVisitResult result = null;

		result = handleDirException(dir, exc);

		if (result == null) {
			result = handleChildrenLimit(dir);
		}

		register.exitCurrentDirectory();
		return result == null ? FileVisitResult.CONTINUE : result;
	}

	private FileVisitResult handleDirException(Path dir, @Nullable IOException exc) {
		if (exc != null) {
			appendNewLine(lineRenderer.renderDirectoryException(depth, dir, exc));
		}
		return null;
	}

	private void handleMaxDepth(Path dir, BasicFileAttributes attrs) {
		appendNewLine(lineRenderer.renderDirectoryBegin(depth, directoryChain, attrs));
		depth = depth.append(DepthSymbol.LAST_FILE);
		appendNewLine(lineRenderer.renderDirectoryInterrupted(depth, dir, register.notVisitedInCurrentDir(), DirectoryInterruptionCause.MAX_DEPTH));
		depth = depth.pop();
		directoryChain.clear();
	}

	private FileVisitResult handleChildrenLimit(Path dir) {
		var limitReached = register.hasSomeNotVisitedChildren();
		if (limitReached) {
			depth = depth.append(DepthSymbol.LAST_FILE);
			appendNewLine(lineRenderer.renderDirectoryInterrupted(depth, dir, register.notVisitedInCurrentDir(), DirectoryInterruptionCause.CHILDREN_LIMIT));
			depth = depth.pop();
			return FileVisitResult.SKIP_SIBLINGS;
		}
		return null;
	}

	private boolean isMaxDepthReached() {
		if (options.getMaxDepth() < 0) {
			return false;
		}
		return (depth.getSize() + directoryChain.size()) > options.getMaxDepth();
	}

	private void updateDepthForLastFile(Path path) {
		if (depth.isEmpty()) {
			return;
		}
		var isLast = register.isLastChildInCurrentDir(path);
		depth = depth.pop();
		depth = depth.append(isLast ? DepthSymbol.LAST_FILE : DepthSymbol.NON_LAST_FILE);
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
