package generators.graph.multicriteria;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

public class Dijkstra {

	Queue<Node> pq = new PriorityQueue<Node>((l, r) -> {
		return l.d - r.d;
	});

	public List<Node> findShortestPaths(Map<Integer, List<Edge>> graphEdges, int start, int weight) {
		init(graphEdges.size(), start);
		List<Node> result = new ArrayList<>();
		while (!pq.isEmpty()) {
			Node n = pq.poll();
			result.add(n);
			for (Node v : pq) {
				int d = dist(n, v, graphEdges.get(n.n), weight);
				if (d < v.d) {
					v.d = d;
				}
			}
		}
		result.sort((l, r) -> {
			return l.n - r.n;
		});
		return result;
	}

	private int dist(Node n, Node v, List<Edge> edges, int weight) {
		for (Edge e : edges) {
			if (e.start == n.n && e.end == v.n) {
				return n.d + e.weights.get(weight);
			}
		}
		return Integer.MAX_VALUE;
	}

	private void init(int count, int end) {
		pq.clear();
		for (int i = 0; i < count; i++) {
			if (i == end) {
				pq.add(new Node(i, 0));
			} else {
				pq.add(new Node(i, Integer.MAX_VALUE));
			}
		}
	}

}

class Node {
	public Node(int n, int d) {
		this.n = n;
		this.d = d;
	}

	public int n;
	public int d;
}
