package io.github.computerdaddyguy.jfiletreeprettyprinter.cli;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RecordUtils {

	/**
	 * Inspired from: https://sormuras.github.io/blog/2020-05-06-records-to-text-block.html
	 */
	public static String toTextBlock(Record record) {
		var lines = new ArrayList<String>();
		toTextBlock(lines, "", null, record, "  ");
		return String.join(System.lineSeparator(), lines);
	}

	private static void toTextBlock(List<String> lines, String shift, String attrName, Object value, String indent) {
		var nested = value.getClass();
		if (nested.isRecord()) {
			toTextBlockRecord(lines, shift, attrName, (Record) value, indent);
		} else if (value instanceof Collection coll) {
			toTextBlockColl(lines, shift, attrName, coll, indent);
		} else {
			lines.add(String.format("%s%s = %s", shift, attrName, value));
		}
	}

	private static void toTextBlockRecord(List<String> lines, String shift, String attrName, Record record, String indent) {
		if (attrName == null) {
			lines.add(String.format("%s%s", shift, record.getClass().getSimpleName()));
		} else {
			lines.add(String.format("%s%s -> %s", shift, attrName, record.getClass().getSimpleName()));
		}

		var components = record.getClass().getRecordComponents();

		for (var component : components) {
			var compName = component.getName();
			try {
				var value = component.getAccessor().invoke(record);
				toTextBlock(lines, shift + indent, compName, value, indent);
			} catch (ReflectiveOperationException e) {
				lines.add("// Reflection over " + component + " failed: " + e);
			}
		}
	}

	private static void toTextBlockColl(List<String> lines, String shift, String attrName, Collection<?> coll, String indent) {
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
