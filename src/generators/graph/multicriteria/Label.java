package generators.graph.multicriteria;

import java.util.ArrayList;
import java.util.List;

public class Label implements Comparable<Label> {

	public int node;
	public int index = 0;
	public Label prev;
	public List<Integer> weights = new ArrayList<>();
	public Label lowerBound = null;

	public Label(Label l) {
		this.prev = l.prev;
		this.node = l.node;
		this.index = l.index;
		this.weights = l.weights;
		this.lowerBound = l.lowerBound;
	}

	public Label(int node, int index, int... weights) {
		this.prev = null;
		this.node = node;
		this.index = index;
		for (int w : weights) {
			this.weights.add(w);
		}
	}

	public Label(int node, int index, List<Integer> lowerBounds, int... weights) {
		this(node, index, weights);
		this.lowerBound = this.addLowerBounds(lowerBounds);
	}

	public Label(Label prev, Edge next, int index, List<Integer> lowerBounds) {
		this.node = next.end;
		this.prev = prev;
		this.index = index;
		for (int i = 0; i < next.weights.size(); i++) {
			weights.add(prev.weights.get(i) + next.weights.get(i));
		}
		this.lowerBound = this.addLowerBounds(lowerBounds);
	}

	private Label addLowerBounds(List<Integer> lowerBounds) {
		if (lowerBounds.isEmpty())
			return new Label(this);
		Label newLabel = new Label(node, index);
		for (int i = 0; i < weights.size(); i++) {
			newLabel.weights.add(weights.get(i) + lowerBounds.get(i));
		}
		return newLabel;
	}

	public int getPathLength() {
		if (prev == null) {
			return 1;
		}
		return prev.getPathLength() + 1;
	}

	@Override
	public int compareTo(Label o) {
		for (int i = 0; i < o.weights.size(); i++) {
			if (o.weights.get(i) < weights.get(i)) {
				return 1;
			} else if (o.weights.get(i) > weights.get(i)) {
				return -1;
			}
		}
		return 0;
	}

}
