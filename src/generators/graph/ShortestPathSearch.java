package generators.graph;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.stream.Collectors;

import algoanim.animalscript.addons.InfoBox;
import algoanim.counter.enumeration.ControllerEnum;
import algoanim.counter.view.TwoValueView;
import algoanim.primitives.Graph;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CounterProperties;
import algoanim.properties.GraphProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import generators.graph.multicriteria.Edge;
import generators.graph.multicriteria.Label;
import generators.graph.multicriteria.LowerBounds;
import generators.graph.multicriteria.StringUtil;
import generators.graph.multicriteria.Util;

public class ShortestPathSearch {

	private Language language;
	private Text header;
	private Text optiHeader;
	private Text header2;
	private SourceCode src;
	private SourceCode dominatedSrc;
	private SourceCodeProperties sourceCodeProps;
	private Graph graph;
	private StringArray pq;
	private StringArray lb;
	private StringArray ps;
	private NodeLabelList labels;
	private StringArray newLabelArray;

	private Queue<Label> queue = new PriorityQueue<>();
	private Map<Integer, List<Edge>> graphEdges;
	private Map<Integer, List<Label>> nodeLabels = new HashMap<>();
	private int target = 0;
	private List<Label> terminalList = new ArrayList<>();

	private boolean goalDirected = false;
	private boolean earlyTermination = false;
	private LowerBounds lowerBounds;

	private ArrayProperties arrayProps;
	private TwoValueView counter;
	private int createdLabels;
	private int dominatedLabels;

	private boolean removed = false;
	List<Edge> edges = new ArrayList<>();

	public ShortestPathSearch(Language lang) {
		this.language = lang;
		this.language.setStepMode(true);
		graphEdges = new HashMap<Integer, List<Edge>>();
		nodeLabels = new HashMap<Integer, List<Label>>();
	}

	public void start(List<Integer[][]> edgeWeights, Coordinates[] graphNodes, String[] nodeLabels, int start,
			int target, boolean goalDirected, boolean dominationByTerminal, ArrayProperties props) {
		language.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
		this.earlyTermination = dominationByTerminal;
		this.goalDirected = goalDirected;
		System.out.println(earlyTermination);
		if (goalDirected) {
			queue = new PriorityQueue<>((l, r) -> {
				return l.lowerBound.compareTo(r.lowerBound);
			});
		}
		arrayProps = props;
		this.target = target;
		Util.setGraphColors(props);
		Util.setUpOffset(graphNodes);
		Util.setUpQuestions(language);
		setupGraph(edgeWeights, graphNodes, nodeLabels, start, target);
		this.lowerBounds = new LowerBounds(graphEdges, target, 2);
		showIntroduction();
		if (goalDirected || earlyTermination) {
			showOptimization();
		}
		showMainPanel();
		showAlgorithm();
		showConclusion();
	}

	private void setupCounter() {
		CounterProperties cp = new CounterProperties();
		cp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		cp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.DARK_GRAY);
		counter = new TwoValueView(language, new Coordinates(Util.newLabelX, Util.counterY), true, true, cp,
				new String[] { "Labels Created", "Labels Dominated" });
		counter.show();
	}

	private void incDominated() {
		dominatedLabels++;
		counter.update(ControllerEnum.access, 1);
	}

	private void incCreated() {
		createdLabels++;
		counter.update(ControllerEnum.assignments, 1);
	}

	private void showAlgorithm() {
		TextProperties headerProps2 = new TextProperties();
		headerProps2.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 16));
		headerProps2.set("centered", true);
		header2 = language.newText(new Coordinates(450, 30), "Search from " + graph.getNodeLabel(graph.getStartNode())
				+ " to " + graph.getNodeLabel(graph.getTargetNode()), "header2", null, headerProps2);
		findShortestSolution(graph.getPositionForNode(graph.getStartNode()),
				graph.getPositionForNode(graph.getTargetNode()));
	}

	private void findShortestSolution(int start, int target) {

		for (int i = 0; i < graphEdges.size(); i++) {
			nodeLabels.put(i, new ArrayList<Label>());
		}
		int labelIndex = 0;
		// Highlight Start Code
		src.highlight(3);
		language.nextStep();
		src.unhighlight(3);
		src.highlight(4); // Create Start Labels
		language.nextStep();
		// Create Start Labels
		Label startLabel = new Label(start, 0, 0, 0);
		queue.add(startLabel);
		nodeLabels.put(start, Arrays.asList(startLabel));
		// Create Start Labels Animation
		pq.highlightCell(1, 1, null, null);
		labels.highlightCell(1, 1, null, null);
		String labelStr = StringUtil.getLabelString(graph, startLabel);
		labelIndex++;
		pq.put(1, labelStr, null, null);
		labels.addLabelToNodeList(labelStr, graph.getNodeLabel(start));
		incCreated();
		language.nextStep();
		// Queue Highlight
		unhighlightEverything();
		src.highlight(5);
		language.nextStep();

		while (!queue.isEmpty()) {
			// Queue Poll Animation
			unhighlightEverything();
			src.highlight(6);
			language.nextStep();
			pq.highlightCell(1, null, null);
			language.nextStep();
			// Queue Poll
			Label label = queue.poll();
			unhighlightSource(src);
			src.highlight(7);
			language.nextStep();
			graph.highlightNode(label.node, null, null);
			Util.getLabelCreationQuestion(language, true, label, null, graphEdges.get(label.node).size(),
					graphEdges.get(label.node).stream().map(x -> graph.getNodeLabel(x.end))
							.collect(Collectors.toList()),
					nodeLabels.keySet().stream().map(x -> graph.getNodeLabel(x)).collect(Collectors.toList()));
			language.nextStep();

			for (Edge e : graphEdges.get(label.node)) {
				// For Each Animation
				unhighlightSource(src);
				src.highlight(7);
				// HighlightEdge
				graph.highlightEdge(e.start, e.end, null, null);
				graph.setEdgeHighlightTextColor(e.start, e.end, Color.BLACK, null, null);
				language.nextStep();

				Label newLabel = new Label(label, e, labelIndex, lowerBounds.getLowerBounds(e.end));
				// New Label Creation
				unhighlightSource(src);
				src.highlight(8);
				Util.getLabelCreationQuestion(language, false, label, e, graphEdges.get(label.node).size(),
						graphEdges.get(label.node).stream().map(x -> graph.getNodeLabel(x.end))
								.collect(Collectors.toList()),
						nodeLabels.keySet().stream().map(x -> graph.getNodeLabel(x)).collect(Collectors.toList()));

				language.nextStep();
				labelStr = StringUtil.getLabelString(graph, newLabel);
				labelIndex++;
				newLabelArray.put(1, labelStr, null, null);
				newLabelArray.highlightCell(1, null, null);
				incCreated();
				language.nextStep();
				// show lower bounds
				if (earlyTermination) {
					lb.highlightCell(newLabel.node + 1, null, null);
				}
				// Dominated by Terminal Label + lowerBounds Highlight
				src.unhighlight(8);
				src.highlight(9);
				language.nextStep();
				if (earlyTermination && isDominated(newLabel.lowerBound, terminalList)) {
					incDominated();
					src.unhighlight(9);
					src.highlight(11);
					language.nextStep();
					graph.unhighlightEdge(e.start, e.end, null, null);
					lb.unhighlightCell(newLabel.node + 1, null, null);
					language.nextStep();
					continue;
				}

				// Dominated by labels at node Highlight
				if (earlyTermination) {
					src.unhighlight(9);
					src.highlight(10);
					language.nextStep();
				}
				if (isDominated(newLabel, nodeLabels.get(newLabel.node))) {
					// Continue Highligh und Clear
					incDominated();
					src.unhighlight(changedHighlightIndex(10));
					src.highlight(changedHighlightIndex(11));
					language.nextStep();
					graph.unhighlightEdge(e.start, e.end, null, null);
					if (earlyTermination) {
						lb.unhighlightCell(newLabel.node + 1, null, null);
					}
					language.nextStep();
					continue;
				}
				// Is Terminal Reached
				src.unhighlight(changedHighlightIndex(10));
				src.highlight(changedHighlightIndex(12));
				language.nextStep();

				if (newLabel.node == target) {
					// Insert in Terminal List
					src.unhighlight(changedHighlightIndex(12));
					src.highlight(changedHighlightIndex(13));
					language.nextStep();
					labels.addLabelToNodeList(labelStr, graph.getNodeLabel(target));
					labels.highlightLastAdded(graph.getNodeLabel(target));
					language.nextStep();
					Util.getTerminalQuestion(language, terminalList.size());
					terminalList.add(newLabel);
					nodeLabels.get(newLabel.node).add(newLabel);
				} else {
					src.unhighlight(changedHighlightIndex(12));
					src.highlight(changedHighlightIndex(15));
					language.nextStep();
					removed = false;

					nodeLabels.get(newLabel.node).removeIf(x -> {
						if (dominates(newLabel, x)) {
							incDominated();
							removed = true;
							labels.remove(nodeLabels.get(newLabel.node).indexOf(x), graph.getNodeLabel(newLabel.node),
									language);
						}
						return dominates(newLabel, x);
					});

					// Adding Animation
					src.unhighlight(changedHighlightIndex(15));
					src.highlight(changedHighlightIndex(16));
					language.nextStep();
					labels.addLabelToNodeList(labelStr, graph.getNodeLabel(newLabel.node));
					labels.highlightLastAdded(graph.getNodeLabel(newLabel.node));
					Util.getLabelListQuestion(language, removed);
					language.nextStep();
					labels.unhighlightAll();

					nodeLabels.get(newLabel.node).add(newLabel);

					// PQ Add Animation
					src.unhighlight(changedHighlightIndex(16));
					src.highlight(changedHighlightIndex(17));
					language.nextStep();

					addToPq(labelStr);
					queue.add(newLabel);

					// Cleaning up
					src.unhighlight(changedHighlightIndex(17));
					language.nextStep();
				}
				// Cleaning up
				graph.unhighlightEdge(e.start, e.end, null, null);
				newLabelArray.put(1, "          ", null, null);
				newLabelArray.unhighlightCell(1, null, null);
				if (earlyTermination) {
					lb.unhighlightCell(newLabel.node + 1, null, null);
				}
				language.nextStep();
			}
			// Cleaning up
			graph.unhighlightNode(label.node, null, null);
			language.nextStep();
			// Extract from Queue
			sortPq();
			unhighlightEverything();
			language.nextStep();
		}

		src.highlight(changedHighlightIndex(18));
		language.nextStep();

		terminalList.removeIf(x -> {
			for (Label l : terminalList) {
				if (dominates(l, x)) {
					incDominated();
					labels.remove(terminalList.indexOf(x), graph.getNodeLabel(target), language);
					return true;
				}
			}
			return false;
		});
		src.unhighlight(changedHighlightIndex(18));
		labels.highlightNodeList(graph.getNodeLabel(target));
		language.nextStep();
	}

	private void sortPq() {
		List<Label> labels = new ArrayList<>();
		for (Label l : queue) {
			labels.add(l);
		}
		for(int i = 1; i < pq.getLength(); i++){
			pq.put(i, "", null, null);
		}
		for (int i = 0; i < queue.size(); i++) {
			Label l = labels.get(i);
			pq.unhighlightCell(i + 1, null, null);
			pq.put(i + 1, StringUtil.getLabelString(graph, l), null, null);
		}

	}

	private int changedHighlightIndex(int index) {
		int result = index;
		if (!earlyTermination) {
			result = result - 1;
		}
		return result;
	}

	private void addToPq(String labelStr) {
		for (int i = 0; i < pq.getLength(); i++) {
			if (pq.getData(i).equals("")) {
				pq.put(i, labelStr, null, null);
				pq.highlightCell(i, null, null);
				language.nextStep();
				pq.unhighlightCell(i, null, null);
				break;
			}
		}
	}

	private void unhighlightEverything() {
		unhighlightArray(pq);
		labels.unhighlightAll();
		unhighlightArray(newLabelArray);
		unhighlightSource(src);
		unhighlightSource(dominatedSrc);
	}

	private void unhighlightArray(StringArray array) {
		for (int i = 0; i < array.getLength(); i++) {
			array.unhighlightCell(i, null, null);
		}
	}

	private void unhighlightSource(SourceCode code) {
		for (int i = 0; i < code.length(); i++) {
			code.unhighlight(i);
		}
	}

	private boolean isDominated(Label newLabel, List<Label> labelList) {
		// Code Highlight
		ps.put(0, StringUtil.createLabelStringWithoutNumber(graph, newLabel), null, null);
		ps.show();
		dominatedSrc.highlight(0);
		language.nextStep();
		dominatedSrc.unhighlight(0);
		dominatedSrc.highlight(1);
		language.nextStep();
		labels.highlightNodeList(graph.getNodeLabel(newLabel.node));
		if (nodeLabels.get(newLabel.node) != null && nodeLabels.get(newLabel.node).size() != 0) {
			Util.getDominatedQuestion(language,
					willBeDominated(newLabel) ? Util.WILLBEDOMINATED : Util.WILLNOTBEDOMINATED, null, newLabel, null,
					StringUtil.createLabelStringWithoutNumber(graph, newLabel));
		}
		language.nextStep();
		if (labelList != null) {
			for (Label l : labelList) {
				// HighlightFurtherCode
				dominatedSrc.unhighlight(1);
				dominatedSrc.highlight(2);
				dominatedSrc.highlight(3);
				Util.getDominatedQuestion(language, dominates(l, newLabel) ? Util.LABELDOMINATED : Util.NOTDOMINATED, l,
						newLabel, StringUtil.createLabelStringWithoutNumber(graph, l),
						StringUtil.createLabelStringWithoutNumber(graph, newLabel));
				language.nextStep();

				if (dominates(l, newLabel)) {
					dominatedSrc.unhighlight(2);
					dominatedSrc.unhighlight(3);
					dominatedSrc.highlight(4);
					language.nextStep();
					dominatedSrc.unhighlight(4);
					labels.unhighlightAll();

					language.nextStep();
					ps.hide();
					return true;
				}
				dominatedSrc.unhighlight(2);
				dominatedSrc.unhighlight(3);
				dominatedSrc.highlight(1);
				language.nextStep();
			}
		}
		// Unhighlight
		unhighlightSource(dominatedSrc);
		dominatedSrc.highlight(5);
		language.nextStep();
		unhighlightSource(dominatedSrc);
		labels.unhighlightAll();
		language.nextStep();
		ps.hide();
		return false;
	}

	private boolean willBeDominated(Label newLabel) {
		if (nodeLabels.get(newLabel.node) != null) {
			for (Label l : nodeLabels.get(newLabel.node)) {
				if (dominates(l, newLabel)) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean dominates(Label l1, Label l2) {
		boolean smallerEqual = true;
		boolean smaller = false;
		for (int i = 0; i < l1.weights.size(); i++) {
			smallerEqual = smallerEqual && (l1.weights.get(i) <= l2.weights.get(i));
			smaller = smaller || (l1.weights.get(i) < l2.weights.get(i));
		}
		return smallerEqual && smaller;
	}

	private void showIntroduction() {
		TextProperties headerProps = new TextProperties();
		headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 16));
		headerProps.set("centered", true);
		header = language.newText(new Coordinates(450, 10), "Multicriterial Shortest Path Search", "header", null,
				headerProps);
		sourceCodeProps = new SourceCodeProperties();
		sourceCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 14));
		sourceCodeProps.set("highlightColor", Color.RED);
		src = language.newSourceCode(new Coordinates(100, 70), "introduction", null, sourceCodeProps);
		src.addMultilineCode(
				"The multicriterial shortest path search is used to find Pareto optimal paths through a directed graph,\n where the edges have weights for multiple criteria."
						+ "It is loosely based on Dijkstras Shortest Path Algorithm, \n but has several adjustments to support more than one search criterion."
						+ "Because multiple criteria are used, \n the algorithm can result in more than one Pareto optimal path.",
				"test", null);
		language.nextStep();
	}

	private void showOptimization() {
		String text = "Lower Bounds for every criterium are calculated by finding the shortest path "
				+ "\nfrom the target node to every node with only one criteria. "
				+ "\nTherefore graphs with only one criteria with inverted edges are created "
				+ "\nand the dijkstra algorithm is used to find the shortest paths from the "
				+ "\ntarget node to every other node. ";
		if (earlyTermination) {
			text += "\nThese Lower Bounds are then used to implement Domination by Labels at the Terminal. "
					+ "\nIf any Label that is created with its Lower Bounds from the node it is created on "
					+ "\nis dominated by a label that at the terminal it is discarded, because it already cannot "
					+ "\nget better than a Label at the terminal. ";
		}
		if (goalDirected) {
			text += "\nLower Bounds are used to sort the Priority Queue to extract the most promising "
					+ "\nLabel first. This is done by adding the Lower Bounds of a labels node to the label and then "
					+ "\nsorting the labels. ";
		}
		text += "\n Lower Bounds for every node in this graph with these weights are:";
		src.hide();
		header.hide();
		graph.show();
		TextProperties headerProps = new TextProperties();
		headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 16));
		headerProps.set("centered", true);
		optiHeader = language.newText(new Coordinates(450, 10), "Optimizations with Lower Bounds", "header", null,
				headerProps);
		SourceCode optis = language.newSourceCode(new Coordinates(Util.pqX, 70), "introduction", null, sourceCodeProps);
		optis.addMultilineCode(text, "", null);
		StringArray s = null;
		String[] arr = new String[graphEdges.size() + 1];
		arr[0] = "Lower Bounds: ";
		for (int n = 0; n < graphEdges.size(); n++) {
			List<Integer> list = lowerBounds.getLowerBounds(n);
			int[] lower = new int[list.size()];
			for (int i = 0; i < list.size(); i++) {
				lower[i] = list.get(i);
			}
			Label l = new Label(target, -1, lower);
			l.prev = new Label(n, -1);
			arr[n + 1] = StringUtil.createLabelStringWithoutNumber(graph, l);
		}
		s = language.newStringArray(new Coordinates(Util.pqX + 100, Util.pqY + 300), arr, "", null, arrayProps);
		lb = language.newStringArray(new Coordinates(Util.pqX, Util.pqY + 520), arr, "lb", null, arrayProps);
		lb.hide();
		s.put(0, arr[0], null, null);
		lb.put(0, arr[0], null, null);
		s.showIndices(false, null, null);
		lb.showIndices(false, null, null);
		language.nextStep();
		s.hide();
		lb.show();
		optis.hide();
		optiHeader.hide();
		header.show();
		src.show();
	}

	private void showConclusion() {
		language.hideAllPrimitives();
		labels.hide();
		if (earlyTermination) {
			lb.hide();
		}
		ps.hide();
		newLabelArray.hide();
		pq.hide();
		dominatedSrc.hide();
		header.show();
		header2.show();
		counter.hide();

		Util.MAXLABELLENGTH = 100;
		InfoBox info = new InfoBox(language, new Coordinates(Util.pqX, Util.pqY), 50, "Resulting Paths");
		graph.show();
		terminalList.sort((l, r) -> {
			return l.compareTo(r);
		});
		List<String> text = terminalList.stream().map(x -> StringUtil.createLabelStringWithoutNumber(graph, x))
				.collect(Collectors.toList());
		Util.MAXLABELLENGTH = 14;
		if (terminalList.size() > 1) {
			text.addAll(Arrays.asList("",
					"Pareto optimality ensures multiple results, that are all optimal in regards to one criterion or a combination of both.",
					"The first result is the best in regards to the first criterion.",
					"The last result is the best in regards to the second criterion."));
			if (terminalList.size() > 2) {
				text.add("All results, except the first and the last, are Pareto optimal,");
				text.add(
						"because they can not be dominated, as their values for the criteria are in between the best values for both criteria.");
			}

		} else {
			text.addAll(Arrays.asList("",
					"This label dominated every other label at the goal or before and therefore there is only one result.",
					"With a different graph more results might be found."));
		}
		text.addAll(Arrays.asList("", "Statistics:", "Created Labels: " + createdLabels,
				"Dominated Labels: " + dominatedLabels,
				"The number of created Labels greatly influences the runtime and memory the algorithm needs, especially for more complex data types and bigger graphs. ",
				"Therefore, it is important to keep the number of created Labels as low as possible."));
		info.setText(text);
		src.hide();
	}

	private void showSourceCode() {
		src.hide();
		src = language.newSourceCode(new Coordinates(Util.sourceCodeX, Util.sourceCodeY), "code", null,
				sourceCodeProps);
		src.addCodeLine("Input: A timetable graph and a query", "code", 0, null);
		src.addCodeLine("Output: a set of advanced pareto optimal labels at the terminal", "code", 0, null);
		src.addCodeLine("", "code", 0, null);
		src.addCodeLine("PriorityQueue pq := {};", "code", 0, null);
		src.addCodeLine("createStartLabels();", "code", 0, null);
		src.addCodeLine("while !pq.isEmpty() do", "code", 0, null);
		src.addCodeLine("Label label := pq.extractLabel();", "code", 1, null);
		src.addCodeLine("foreach outgoing edge e=(v,w) do", "code", 1, null);
		src.addCodeLine("Label newLabel := createLabel(label, e);", "code", 2, null);
		if (earlyTermination) {
			src.addCodeLine("if isDominated(newLabel + lowerBoundsAt(w), terminalList) ", "code", 2, null);
			src.addCodeLine("|| isDominated(newLabel, labelListAt(w) then", "code", 3, null);
			src.addCodeLine("continue;", "code", 4, null);
		} else {
			src.addCodeLine("if isDominated(newLabel, labelListAt(w) then", "code", 2, null);
			src.addCodeLine("continue;", "code", 3, null);
		}
		src.addCodeLine("if isTerminalReached(newLabel) then", "code", 2, null);
		src.addCodeLine("terminalList.insert(newLabel);", "code", 3, null);
		src.addCodeLine("else", "code", 2, null);
		src.addCodeLine("labelList(w).removeLabelDominatedBy(newLabel);", "code", 3, null);
		src.addCodeLine("labelListAt(w).insert(newLabel);", "code", 3, null);
		src.addCodeLine("pq.insert(newLabel);", "code", 3, null);
		src.addCodeLine("removeDominated(terminalList);", "code", 0, null);
		src.show();
		dominatedSrc = language.newSourceCode(new Coordinates(Util.sourceCodeX, Util.dominatesY), "dominate", null,
				sourceCodeProps);
		dominatedSrc.addCodeLine("bool isDominated(newLabel, labelList)", "", 0, null);
		dominatedSrc.addCodeLine("foreach label in labelList", "", 1, null);
		dominatedSrc.addCodeLine("if allOf(label.criteria <= newLabel.criteria)", "", 2, null);
		dominatedSrc.addCodeLine("&& oneOf(label.criteria < newLabel.criteria) then", "", 3, null);
		dominatedSrc.addCodeLine("return true", "", 4, null);
		dominatedSrc.addCodeLine("return false", "", 1, null);
		dominatedSrc.show();
	}

	private void showMainPanel() {
		showSourceCode();
		setupCounter();
		graph.show();
		showArrays();
		language.nextStep();
	}

	private void showArrays() {
		String[] queue = new String[20];
		String[] paras = new String[1];
		for (int i = 0; i < queue.length; i++) {
			queue[i] = "";
		}
		queue[0] = "PQ:            ";
		paras[0] = "            ";

		ArrayProperties opt = arrayProps;
		ArrayProperties optLabel = new ArrayProperties();
		Util.setArrayProperties(optLabel);
		optLabel.set(AnimationPropertiesKeys.DIRECTION_PROPERTY, false);
		opt.set(AnimationPropertiesKeys.DIRECTION_PROPERTY, true);

		int labelsSize = graph.getSize() * 5;

		pq = language.newStringArray(new Coordinates(Util.pqX, Util.pqY), queue, "pq", null, opt);
		labels = new NodeLabelList(language.newStringArray(new Coordinates(Util.labelsX, Util.labelsY),
				new String[labelsSize], "labels", null, opt), graph);
		newLabelArray = language.newStringArray(new Coordinates(Util.newLabelX, Util.newLabelY),
				new String[] { "newLabel: ", "               " }, "newLabel", null, optLabel);

		ps = language.newStringArray(new Coordinates(Util.sourceCodeX + 100, Util.dominatesY - 20), paras, "paras",
				null, opt);
		ps.highlightCell(0, null, null);
		pq.put(2, "", null, null);
		pq.showIndices(false, null, null);
		labels.showIndices(false, null, null);
		newLabelArray.showIndices(false, null, null);
		ps.showIndices(false, null, null);
		ps.hide();
		labels.show();
		newLabelArray.show();
		pq.show();
	}

	private void setupGraph(List<Integer[][]> edgeWeights, Node[] nodes, String[] nodeLabels, int start, int target) {

		for (int i = 0; i < nodes.length; i++) {
			graphEdges.put(i, new ArrayList<Edge>());
		}

		int[][][] weights = new int[edgeWeights.size()][edgeWeights.get(0).length][edgeWeights.get(0)[0].length];
		for (int w = 0; w < edgeWeights.size(); w++) {
			for (int i = 0; i < edgeWeights.get(w).length; i++) {
				for (int j = 0; j < edgeWeights.get(w)[i].length; j++) {
					weights[w][i][j] = edgeWeights.get(w)[i][j];
				}
			}
		}
		GraphProperties prop = Util.getGraphProperties();
		graph = language.newGraph("Test", weights[0], nodes, nodeLabels, null, prop);
		for (int i = 0; i < weights[0].length; i++) {
			for (int j = 0; j < weights[0][0].length; j++) {
				boolean connected = true;
				String weightStr = "(";
				int[] edge = new int[weights.length];
				for (int w = 0; w < weights.length; w++) {
					connected = connected && weights[w][i][j] != 0;
					edge[w] = weights[w][i][j];
					if (w != weights.length - 1) {
						weightStr += weights[w][i][j] + ",";
					} else {
						weightStr += weights[w][i][j] + ")";
					}
				}

				if (connected) {
					graph.setEdgeWeight(i, j, weightStr, null, null);
					graphEdges.get(i).add(new Edge(i, j, edge));
				}
			}
		}

		graph.setStartNode(graph.getNode(start));
		graph.setTargetNode(graph.getNode(target));
		graph.hide();
	}
}
