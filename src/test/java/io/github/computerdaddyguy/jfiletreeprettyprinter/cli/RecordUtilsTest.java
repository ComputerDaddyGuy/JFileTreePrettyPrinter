package io.github.computerdaddyguy.jfiletreeprettyprinter.cli;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.computerdaddyguy.jfiletreeprettyprinter.cli.options.model.ChildLimit.DynamicLimit;
import io.github.computerdaddyguy.jfiletreeprettyprinter.cli.options.model.ChildLimit.DynamicLimitItem;
import io.github.computerdaddyguy.jfiletreeprettyprinter.cli.options.model.ExternalOptions;
import io.github.computerdaddyguy.jfiletreeprettyprinter.cli.options.model.Matcher.NameGlob;
import io.github.computerdaddyguy.jfiletreeprettyprinter.cli.options.model.Matcher.PathGlob;
import java.util.List;
import org.junit.jupiter.api.Test;

class RecordUtilsTest {

	@Test
	void nominal() {

		var dynLimit1 = new DynamicLimitItem(1, new NameGlob("*.java"));
		var dynLimit2 = new DynamicLimitItem(1, new PathGlob("pictures"));
		var childLimit = new DynamicLimit(List.of(dynLimit1, dynLimit2));
		var rec = new ExternalOptions(Boolean.TRUE, childLimit);

		var expected = """
			ExternalOptions
			  emojis = true
			  childLimit -> DynamicLimit
			    limits = [
			      [0] -> DynamicLimitItem
			        limit = 1
			        matcher -> NameGlob
			          glob = *.java
			      [1] -> DynamicLimitItem
			        limit = 1
			        matcher -> PathGlob
			          glob = pictures
			    ]""";

		assertThat(RecordUtils.toTextBlock(rec)).isEqualToNormalizingNewlines(expected);
	}

}
