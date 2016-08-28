package generators.graph.multicriteria;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class LowerBounds {

	private Map<Integer, List<Edge>> graph;
	private Dijkstra dijkstra;
	private List<List<Integer>> lowerBounds;

	public LowerBounds(Map<Integer, List<Edge>> graph, int start, int weightCount) {
		this.lowerBounds = new ArrayList<>();
		this.dijkstra = new Dijkstra();
		this.graph = new HashMap<>();
		invertGraph(graph);
		for (int i = 0; i < weightCount; i++) {
			for (Node n : dijkstra.findShortestPaths(this.graph, start, i)) {
				lowerBounds.get(n.n).add(n.d);
			}
		}
	}

	private void invertGraph(Map<Integer, List<Edge>> graph) {
		List<Edge> edges = new ArrayList<>();
		for (Entry<Integer, List<Edge>> e : graph.entrySet()) {
			this.graph.put(e.getKey(), new ArrayList<>());
			lowerBounds.add(new ArrayList<Integer>());
			for (Edge edge : e.getValue()) {
				edges.add(edge.invert());
			}
		}
		for (Edge e : edges) {
			this.graph.get(e.start).add(e);
		}
	}

	public List<Integer> getLowerBounds(int node) {
		return lowerBounds.get(node);
	}

}
