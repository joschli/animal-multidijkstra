package generators.graph.multicriteria;

import java.util.ArrayList;
import java.util.List;

public class Edge {
	
	public int start;
	public int end;
	public List<Integer> weights = new ArrayList<>();
	
	public Edge(int start, int end, int... weights) {
		this.start = start;
		this.end = end;
		for (int w : weights) {
			this.weights.add(w);
		}
	}

	public Edge invert() {
		Edge invertedEdge = new Edge(this.end, this.start);
		invertedEdge.weights = this.weights;
		return invertedEdge;
	}
}