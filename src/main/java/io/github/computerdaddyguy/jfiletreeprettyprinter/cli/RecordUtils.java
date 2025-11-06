package io.github.computerdaddyguy.jfiletreeprettyprinter.cli;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class RecordUtils {

	private RecordUtils() {
		// Helper class
	}

	/**
	 * Inspired from: https://sormuras.github.io/blog/2020-05-06-records-to-text-block.html
	 */
	public static String toTextBlock(@Nullable Record rec) {
		if (rec == null) {
			return "null";
		}
		var lines = new ArrayList<String>();
		toTextBlock(lines, "", null, rec, "  ");
		return String.join(System.lineSeparator(), lines);
	}

	private static void toTextBlock(List<String> lines, String shift, @Nullable String attrName, Object value, String indent) {
		if (value == null) {
			return;
		}
		var nested = value.getClass();
		if (nested.isRecord()) {
			toTextBlockRecord(lines, shift, attrName, (Record) value, indent);
		} else if (value instanceof Collection<?> coll) {
			toTextBlockCollection(lines, shift, attrName, coll, indent);
		} else {
			lines.add(String.format("%s%s = %s", shift, attrName, value));
		}
	}

	private static void toTextBlockRecord(List<String> lines, String shift, @Nullable String attrName, Record rec, String indent) {
		if (attrName == null) {
			lines.add(String.format("%s%s", shift, rec.getClass().getSimpleName()));
		} else {
			lines.add(String.format("%s%s -> %s", shift, attrName, rec.getClass().getSimpleName()));
		}

		var components = rec.getClass().getRecordComponents();

		for (var component : components) {
			var compName = component.getName();
			try {
				var value = component.getAccessor().invoke(rec);
				toTextBlock(lines, shift + indent, compName, value, indent);
			} catch (ReflectiveOperationException e) {
				lines.add("// Reflection over " + component + " failed: " + e);
			}
		}
	}

	private static void toTextBlockCollection(List<String> lines, String shift, @Nullable String attrName, Collection<?> coll, String indent) {
		if (attrName == null) {
			lines.add(String.format("%s[", shift));
		} else {
			lines.add(String.format("%s%s = [", shift, attrName));
		}
		int i = 0;
		for (var item : coll) {
			toTextBlock(lines, shift + indent, "[" + i + "]", item, indent);
			i++;
		}
		lines.add(String.format("%s]", shift));
	}

}
