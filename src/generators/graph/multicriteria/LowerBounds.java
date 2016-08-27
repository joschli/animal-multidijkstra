package generators.graph.multicriteria;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class LowerBounds {
	public LowerBounds(Map<Integer, List<Edge>> graph, int start, int weight) {
		for (Entry<Integer, List<Edge>> e : graph.entrySet()) {
			List<Edge> edges = new ArrayList<>();
			for (Edge edge : e.getValue()) {
				edges.add(new Edge(edge.start, edge.end, edge.weights.get(weight)));
			}
			this.graph.put(e.getKey(), edges);
		}
		for (Node n : new Dijkstra().start(this.graph, start)) {
			lowerBounds.add(n.d);
		}
	}

	private HashMap<Integer, List<Edge>> graph;
	public List<Integer> lowerBounds;
}
