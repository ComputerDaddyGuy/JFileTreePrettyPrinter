package io.github.computerdaddyguy.jfiletreeprettyprinter.renderer;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class DepthTest {

	@Test
	void isEmpty_when_empty() {
		var depth = Depth.createNewEmpty();
		assertThat(depth.isEmpty()).isTrue();
	}

	@Test
	void isEmpty_when_notEmpty() {
		var depth = Depth.createNewEmpty();
		depth = depth.append(TreeSymbol.LAST_FILE);
		assertThat(depth.isEmpty()).isFalse();
	}

	@Test
	void getSize_when_empty() {
		var depth = Depth.createNewEmpty();
		assertThat(depth.getSize()).isZero();
	}

	@Test
	void getSize_when_notEmpty() {
		var depth = Depth.createNewEmpty();
		depth = depth.append(TreeSymbol.LAST_FILE);
		depth = depth.append(TreeSymbol.LAST_FILE);
		depth = depth.append(TreeSymbol.LAST_FILE);
		assertThat(depth.getSize()).isEqualTo(3);
	}

	@Test
	void pop_when_empty() {
		var depth = Depth.createNewEmpty();
		var result = depth.pop();
		assertThat(result.getSymbols()).isEmpty();
	}

	@Test
	void pop_when_single_item_then_empty() {
		var depth = Depth.createNewEmpty();
		depth = depth.append(TreeSymbol.LAST_FILE);
		var result = depth.pop();
		assertThat(result.getSymbols()).isEmpty();
	}

	@Test
	void toString_nominal() {
		var depth = Depth.createNewEmpty();
		depth = depth.append(TreeSymbol.LAST_FILE);
		depth = depth.append(TreeSymbol.NON_LAST_FILE);
		depth = depth.append(TreeSymbol.EMPTY);
		depth = depth.append(TreeSymbol.CONTINUATION);
		assertThat(depth).hasToString(depth.getSymbols().toString());
	}

	@Test
	void equals_and_hashCode_nominal() {
		var depth1 = Depth.createNewEmpty();
		depth1 = depth1.append(TreeSymbol.LAST_FILE);
		depth1 = depth1.append(TreeSymbol.NON_LAST_FILE);
		depth1 = depth1.append(TreeSymbol.EMPTY);
		depth1 = depth1.append(TreeSymbol.CONTINUATION);

		var depth1Copy = Depth.createNewEmpty();
		depth1Copy = depth1Copy.append(TreeSymbol.LAST_FILE);
		depth1Copy = depth1Copy.append(TreeSymbol.NON_LAST_FILE);
		depth1Copy = depth1Copy.append(TreeSymbol.EMPTY);
		depth1Copy = depth1Copy.append(TreeSymbol.CONTINUATION);

		var depth2 = Depth.createNewEmpty();
		depth2 = depth2.append(TreeSymbol.LAST_FILE);
		depth2 = depth2.append(TreeSymbol.NON_LAST_FILE);
		depth2 = depth2.append(TreeSymbol.CONTINUATION);
		depth2 = depth2.append(TreeSymbol.EMPTY);

		assertThat(depth1)
			.hasSameHashCodeAs(depth1)
			.isEqualTo(depth1)

			.hasSameHashCodeAs(depth1Copy)
			.isEqualTo(depth1Copy)

			.isNotEqualTo(depth2)
			.isNotEqualTo(new Object());
	}

}
