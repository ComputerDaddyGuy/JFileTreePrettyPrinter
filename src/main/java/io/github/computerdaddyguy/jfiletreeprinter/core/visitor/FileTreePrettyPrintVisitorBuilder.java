package io.github.computerdaddyguy.jfiletreeprinter.core.visitor;

import io.github.computerdaddyguy.jfiletreeprinter.core.visitor.depth.DepthFormatter;
import io.github.computerdaddyguy.jfiletreeprinter.core.visitor.depth.DepthFormatters;
import io.github.computerdaddyguy.jfiletreeprinter.core.visitor.filename.FileNameFormatter;
import io.github.computerdaddyguy.jfiletreeprinter.core.visitor.limit.ChildrenLimitConfig;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class FileTreePrettyPrintVisitorBuilder {

	private DepthFormatter depthFormatter = DepthFormatters.UNICODE_TREE;

	private ChildrenLimitConfig childrenLimitConfig = ChildrenLimitConfig.createDefault();

	private FileNameFormatter fileNameFormatter = FileNameFormatter.createDefault();

	public FileTreePrettyPrintVisitor build() {
		return new DefaultFileTreePrettyPrintVisitor(depthFormatter, childrenLimitConfig, fileNameFormatter);
	}

	public FileTreePrettyPrintVisitorBuilder withDepthFormatter(DepthFormatter depthFormatter) {
		this.depthFormatter = Objects.requireNonNull(depthFormatter, "depthFormatter is null");
		return this;
	}

	public FileTreePrettyPrintVisitorBuilder withChildrenLimitConfig(ChildrenLimitConfig config) {
		this.childrenLimitConfig = Objects.requireNonNull(config, "config is null");
		return this;
	}

	public FileTreePrettyPrintVisitorBuilder withFileNameFormatter(FileNameFormatter fileNameFormatter) {
		this.fileNameFormatter = Objects.requireNonNull(fileNameFormatter, "fileNameFormatter is null");
		return this;
	}

}
