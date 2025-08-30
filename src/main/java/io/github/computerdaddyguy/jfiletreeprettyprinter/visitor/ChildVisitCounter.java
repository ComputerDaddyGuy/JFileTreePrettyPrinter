package io.github.computerdaddyguy.jfiletreeprettyprinter.visitor;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import org.jspecify.annotations.NullMarked;

@NullMarked
class ChildVisitCounter {

	private final Function<Path, Integer> childrenLimitFunction;
	private final List<ChildVisitCounterRecord> records;

	public ChildVisitCounter(Function<Path, Integer> childrenLimitFunction) {
		this.childrenLimitFunction = childrenLimitFunction;
		this.records = new ArrayList<ChildVisitCounter.ChildVisitCounterRecord>();
	}

	public void enterNewDirectory(Path dir) {
		var max = childrenLimitFunction.apply(dir);
		var children = listChildren(dir);
		var newRecord = new ChildVisitCounterRecord(max, children);
		records.add(newRecord);
	}

	private Set<Path> listChildren(Path dir) {
		var children = new HashSet<Path>();
		try (var stream = Files.newDirectoryStream(dir)) {
			for (Path child : stream) {
				children.add(child);
			}
		} catch (IOException e) {
			throw new UncheckedIOException("Exception while listing children of " + dir.toAbsolutePath(), e);
		}
		return children;
	}

	public void registerChildVisitInCurrentDir(Path visited) {
		if (records.isEmpty()) {
			return;
		}
		records.getLast().registerChildVisit(visited);
	}

	public void exitCurrentDirectory() {
		records.removeLast();
	}

	public Set<Path> notVisitedInCurrentDir() {
		return records.isEmpty() ? Set.of() : records.getLast().notVisited();
	}

	public boolean exceedsCurrentLimit() {
		return records.isEmpty() ? false : records.getLast().exceeds();
	}

	public boolean hasSomeNotVisitedChildren() {
		return records.isEmpty() ? false : records.getLast().notVisited().size() > 0;
	}

	private class ChildVisitCounterRecord {

		private final int maxChildVisitCount;
		private Set<Path> notVisited;
		private Set<Path> alreadyVisited;

		ChildVisitCounterRecord(int maxChildVisitCount, Set<Path> childrenInDir) {
			this.maxChildVisitCount = maxChildVisitCount;
			this.notVisited = childrenInDir;
			this.alreadyVisited = new HashSet<Path>();
		}

		void registerChildVisit(Path visited) {
			notVisited.remove(visited);
			alreadyVisited.add(visited);
		}

		boolean exceeds() {
			if (maxChildVisitCount <= 0) {
				return false;
			}
			return alreadyVisited.size() >= maxChildVisitCount;
		}

		Set<Path> notVisited() {
			return notVisited;
		}

	}

}
