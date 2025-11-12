package io.github.computerdaddyguy.jfiletreeprettyprinter.example;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class BasicUsageTest {

	@Test
	void test() {
		var result = BasicUsage.run();

		var expected = """
			base/
			├─ businessPlan.pdf
			├─ businessProject.pdf
			├─ cars/
			│  ├─ Ferrari.doc
			│  └─ Porsche.doc
			├─ diyIdeas.docx
			├─ giftIdeas.txt
			└─ images/
			   ├─ funnyCat.gif
			   ├─ holidays/
			   │  ├─ meAtTheBeach.jpeg
			   │  └─ meAtTheZoo.jpeg
			   └─ landscape.jpeg""";

		assertThat(result).isEqualTo(expected);
	}

}
