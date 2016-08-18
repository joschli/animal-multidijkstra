package algoviz;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import algoanim.primitives.Graph;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.AnimationType;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.GraphProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;

public class ShortestPathSearch {

	private Language language;
	private Text header;
	private SourceCode src;
	private SourceCode dominatedSrc;
	private SourceCodeProperties sourceCodeProps;
	private Graph graph;
	private StringArray pq;
	private StringArray labels;
	private StringArray newLabel;
	private Queue<Label> queue = new PriorityQueue<>();
	private HashMap<Integer, List<Label>> nodeLabels = new HashMap<>();
	private List<Label> terminalList = new ArrayList<>();

	List<Edge> edges = new ArrayList<>();

	public ShortestPathSearch(Language lang) {
		this.language = lang;
		this.language.setStepMode(true);
	}

	public void start() {
		showIntroduction();
		showMainPanel();
		showAlgorithm();
		showConclusion();
	}

	private void showAlgorithm() {

	}

	private void findShortestSolution(HashMap<Integer, List<Edge>> graph, int start, int target) {
		for (int i = 0; i < graph.size(); i++) {
			nodeLabels.put(i, new ArrayList<Label>());
		}
		Label startLabel = new Label(start, 0, 0);
		queue.add(startLabel);
		nodeLabels.put(start, Arrays.asList(startLabel));
		while (!queue.isEmpty()) {
			Label label = queue.poll();
			for (Edge e : graph.get(label.node)) {
				Label newLabel = new Label(label, e);
				if (isDominated(newLabel)) {
					continue;
				}
				if (newLabel.node == target) {
					terminalList.add(newLabel);
				} else {
					nodeLabels.get(newLabel.node).removeIf(x -> {
						return dominates(newLabel, x);
					});
					nodeLabels.get(newLabel.node).add(newLabel);
					queue.add(newLabel);
				}
			}
		}
		terminalList.removeIf(x -> {
			for (Label l : terminalList) {
				if (dominates(l, x)) {
					return true;
				}
			}
			return false;
		});
	}

	private boolean isDominated(Label newLabel) {
		for (Label l : nodeLabels.get(newLabel.node)) {
			if (dominates(l, newLabel)) {
				return true;
			}
		}
		return false;
	}

	private boolean dominates(Label l1, Label l2) {
		return (l1.weights[0] <= l2.weights[0] && l1.weights[1] <= l2.weights[1])
				&& (l1.weights[0] < l2.weights[0] || l1.weights[1] < l2.weights[1]);
	}

	public void showIntroduction() {
		TextProperties headerProps = new TextProperties();
		headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 16));
		headerProps.set("centered", true);
		header = language.newText(new Coordinates(450, 10), "Multicriterial Shortest Path Search", "header", null,
				headerProps);
		sourceCodeProps = new SourceCodeProperties();
		sourceCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 14));
		sourceCodeProps.set("highlightColor", Color.RED);
		src = language.newSourceCode(new Coordinates(100, 40), "introduction", null, sourceCodeProps);
		src.addMultilineCode(
				"The multicriterial shortest path search is used to find Pareto optimal paths through a graph,\n where the edges have weights for multiple criteria."
						+ "It is loosely based on Dijkstras Shortest Path Algorithm, \n but has several adjustments to support more than one search criterion."
						+ "Because multiple criteria are used, \n the algorithm can result in more than one Pareto optimal path.",
				"test", null);
		language.nextStep();
	}

	public void showConclusion() {
		language.hideAllPrimitives();
		labels.hide();
		graph.hide();
		newLabel.hide();
		pq.hide();
		dominatedSrc.hide();
		header.show();
		src.hide();
		src = language.newSourceCode(new Coordinates(100, 40), "conclusion", null, sourceCodeProps);
		src.addMultilineCode("Pareto Optimal Paths: L5 with {35,3} from start to goal over a."
				+ "\nL5 {35,3} dominates L4 {40,3} at the terminal node, "
				+ "\nbecause it is better in the first criterion and not worse in the second."
				+ "\nIf L4 would have been {40,2}, both paths would have been Pareto optimal, "
				+ "\nbecause neither of them can dominate the other one.", "end", null);
	}

	public void showSourceCode() {
		src.hide();
		src = language.newSourceCode(new Coordinates(600, 50), "code", null, sourceCodeProps);
		src.addCodeLine("Input: A timetable graph and a query", "code", 0, null);
		src.addCodeLine("Output: a set of advanced pareto optimal labels at the terminal", "code", 0, null);
		src.addCodeLine("", "code", 0, null);
		src.addCodeLine("PriorityQueue pq := {};", "code", 0, null);
		src.addCodeLine("createStartLabels();", "code", 0, null);
		src.addCodeLine("while !pq.isEmpty() do", "code", 0, null);
		src.addCodeLine("Label label := pq.extractLabel();", "code", 1, null);
		src.addCodeLine("foreach outgoing edge e=(v,w) do", "code", 1, null);
		src.addCodeLine("Label newLabel := createLabel(label, e);", "code", 2, null);
		src.addCodeLine("if isDominated(newLabel) then", "code", 2, null);
		src.addCodeLine("continue;", "code", 3, null);
		src.addCodeLine("if isTerminalReached(newLabel) then", "code", 2, null);
		src.addCodeLine("terminalList.insert(newLabel);", "code", 3, null);
		src.addCodeLine("else", "code", 2, null);
		src.addCodeLine("labelListAt(w).insert(newLabel);", "code", 3, null);
		src.addCodeLine("labelList(w).removeLabelDominatedBy(newLabel);", "code", 3, null);
		src.addCodeLine("pq.insert(newLabel);", "code", 3, null);
		src.addCodeLine("removeDominated(terminalList);", "code", 0, null);
		src.show();
		dominatedSrc = language.newSourceCode(new Coordinates(600, 500), "dominate", null, sourceCodeProps);
		dominatedSrc.addCodeLine("bool isDominated(newLabel)", "", 0, null);
		dominatedSrc.addCodeLine("foreach label in labeListAt(w)", "", 1, null);
		dominatedSrc.addCodeLine("if allOf(label.criteria <= newLabel.criteria)", "", 2, null);
		dominatedSrc.addCodeLine("&& oneOf(label.criteria < newLabel.criteria) then", "", 3, null);
		dominatedSrc.addCodeLine("return true", "", 4, null);
		dominatedSrc.addCodeLine("return false", "", 1, null);
		dominatedSrc.show();
	}

	public void showMainPanel() {
		showSourceCode();
		showGraph();
		showArrays();
		language.nextStep();
	}

	private void showArrays() {
		String[] queue = new String[20];
		for (int i = 0; i < queue.length; i++) {
			queue[i] = "";
		}
		queue[0] = "PQ:            ";
		String[] labelStrings = { "Start:           ", "", "", "", "A:", "", "", "", "", "", "", "", "B:", "", "", "",
				"", "Goal:", "", "" };
		ArrayProperties opt = new ArrayProperties();
		ArrayProperties optLabel = new ArrayProperties();
		setArrayProperties(optLabel);
		setArrayProperties(opt);
		opt.set(AnimationPropertiesKeys.DIRECTION_PROPERTY, true);
		pq = language.newStringArray(new Coordinates(400, 70), queue, "pq", null, opt);
		labels = language.newStringArray(new Coordinates(500, 70), labelStrings, "labels", null, opt);
		newLabel = language.newStringArray(new Coordinates(180, 470), new String[] { "newLabel: ", "               " },
				"newLabel", null, optLabel);
		pq.put(2, "", null, null);
		labels.put(2, "", null, null);
		pq.showIndices(false, null, null);
		labels.showIndices(false, null, null);
		newLabel.showIndices(false, null, null);
		labels.show();
		newLabel.show();
		pq.show();
	}

	private void setArrayProperties(ArrayProperties opt) {
		opt.set("fillColor", new Color(192, 192, 192));
		opt.set("elementColor", new Color(0, 0, 0));
		opt.set("elemHighlight", new Color(0, 171, 17));
		opt.set("filled", false);
		opt.set(AnimationPropertiesKeys.DIRECTION_PROPERTY, true);
		opt.set("font", new Font(Font.SANS_SERIF, Font.PLAIN, 12));
	}

	public void showGraph() {
		int[][] nodes = { { 0, 10, 15, 0 }, { 1, 0, 0, 30 }, { 0, 0, 0, 20 }, { 0, 2, 3, 0 } };
		Node[] graphNodes = { new Coordinates(200, 100), new Coordinates(50, 250), new Coordinates(350, 250),
				new Coordinates(200, 400) };
		GraphProperties prop = new GraphProperties();
		prop.set("directed", false);
		prop.set("nodeColor", Color.RED);
		prop.set("fillColor", Color.PINK);
		prop.set("weighted", true);
		graph = language.newGraph("Test", nodes, graphNodes, new String[] { "Start", "A", "B", "Goal" }, null, prop);
		graph.show();
	}

	private class Edge {
		public Edge(int start, int end, int weight1, int weight2) {
			this.start = start;
			this.end = end;
			this.weights[0] = weight1;
			this.weights[1] = weight2;
		}

		public int start;
		public int end;
		public int[] weights = new int[2];

		public String toString() {
			return "From " + start + " to " + end + "with (" + weights[0] + ", " + weights[1] + ")";
		}
	}

	private class Label {
		public Label(int node, int weight1, int weight2) {
			this.node = node;
			this.weights[0] = weight1;
			this.weights[1] = weight2;
		}

		public Label(Label prev, Edge next) {
			this.node = next.end;
			this.weights[0] = prev.weights[0] + next.weights[0];
			this.weights[1] = prev.weights[1] + next.weights[1];
		}

		public int node;
		public int[] weights = new int[2];
	}

	public static void main(String[] args) {
		Language lang = Language.getLanguageInstance(AnimationType.ANIMALSCRIPT, "Shortest Multicriteria Path Search",
				"Tim Witzel, Jonas Schlitzer", 800, 600);
		new ShortestPathSearch(lang).start();
		System.out.println(lang);
	}
}
