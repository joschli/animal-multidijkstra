package generators.graph.multicriteria;

import java.util.ArrayList;
import java.util.List;

public class Edge {
	public Edge(int start, int end, int... weights) {
		this.start = start;
		this.end = end;
		for (int w : weights) {
			this.weights.add(w);
		}
	}

	public int start;
	public int end;
	public List<Integer> weights = new ArrayList<>();
}