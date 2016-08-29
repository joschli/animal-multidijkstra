/*
 * Multicriteria.java
 * Tim Witzel, Jonas Schlitzer, 2016 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.generators.Language;
import algoanim.properties.ArrayProperties;
import algoanim.util.Coordinates;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;

public class Multicriteria implements ValidatingGenerator {
	private Language lang;
	private String[] nodes;
	private String query;
	private String[] edges;
	List<Integer[][]> edgeWeights;
	private String[] nodeLabels;
	private Coordinates[] graphNodes;
	private int startIndex;
	private int targetIndex;
	private ArrayProperties props;

	public void init() {
		lang = new AnimalScript("Shortest Path Multicriteria", "Tim Witzel, Jonas Schlitzer", 800, 600);
	}

	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		this.props = (ArrayProperties) props.getPropertiesByName("Color Properties");
		nodes = (String[]) primitives.get("Nodes (N|X|Y)");
		query = (String) primitives.get("Query (From|To)");
		edges = (String[]) primitives.get("Edges (From|To|Weight1|Weight2|...)");
		int perc = (int) primitives.get("% Questions");
		int corr = (int) primitives.get("Max correct");
		boolean goalDirected = (boolean) primitives.get("Goal-directed search");
		boolean dominationByTerminal = (boolean) primitives.get("Domination by Terminal");
		System.out.println("EarlyTermination|GoalDirected: (" + dominationByTerminal + "|" + goalDirected + ")");
		Util.rightPerCategories = corr;
		Util.percentageOfQuestions = perc;
		parseInput();
		new ShortestPathSearch(lang).start(edgeWeights, graphNodes, nodeLabels, startIndex, targetIndex, goalDirected,
				dominationByTerminal, this.props);
		lang.finalizeGeneration();
		return lang.toString();
	}

	private void parseInput() {
		nodeLabels = new String[nodes.length];
		graphNodes = new Coordinates[nodes.length];
		edgeWeights = new ArrayList<>();
		for (int i = 0; i < edges[0].split("\\|").length - 2; i++) {
			Integer[][] ew = new Integer[nodes.length][nodes.length];
			for (int j = 0; j < ew.length; j++) {
				for (int s = 0; s < ew[j].length; s++) {
					ew[j][s] = new Integer(0);
				}
			}
			edgeWeights.add(ew);
		}
		// Parse Nodes
		for (int i = 0; i < nodes.length; i++) {
			String[] nodeStrings = nodes[i].split("\\|");
			nodeLabels[i] = nodeStrings[0].substring(1, nodeStrings[0].length());
			int x = Integer.parseInt(nodeStrings[1]);
			int y = Integer.parseInt(nodeStrings[2].substring(0, nodeStrings[2].length() - 1));
			graphNodes[i] = new Coordinates(x, y);
		}

		// Parse Query
		String[] queryNodes = query.split("\\|");
		startIndex = Arrays.asList(nodeLabels).indexOf(queryNodes[0].substring(1, queryNodes[0].length()));
		targetIndex = Arrays.asList(nodeLabels).indexOf(queryNodes[1].substring(0, queryNodes[1].length() - 1));

		// Parse Edges

		Arrays.asList(edges).stream().forEach(x -> {
			x = x.replaceAll("\\(", "");
			x = x.replaceAll("\\)", "");

			String[] edgeStrings = x.split("\\|");
			int i = Arrays.asList(nodeLabels).indexOf(edgeStrings[0]);
			int j = Arrays.asList(nodeLabels).indexOf(edgeStrings[1]);

			for (int w = 2; w < edgeStrings.length; w++) {
				edgeWeights.get(w - 2)[i][j] = Integer.parseInt(edgeStrings[w]);
			}

		});
	}

	public String getName() {
		return "Shortest Path Multicriteria";
	}

	public String getAlgorithmName() {
		return "Shortest Path";
	}

	public String getAnimationAuthor() {
		return "Tim Witzel, Jonas Schlitzer";
	}

	public String getDescription() {
		return "The multicriterial shortest path search is used to find Pareto optimal paths through a directed graph, where the edges have weights for multiple criteria."
				+ "\n"
				+ "It is loosely based on Dijkstras Shortest Path Algorithm, but has several adjustments to support more than one search criterion."
				+ "\n"
				+ "Because multiple criteria are used, the algorithm can result in more than one Pareto optimal path.";
	}

	public String getCodeExample() {
		return "Input: A timetable graph and a query" + "\n"
				+ "Output: A set of advanced pareto optimal labels at the terminal" + "\n" + "\n"
				+ "PriorityQueue pq := {};" + "\n" + "createStartLabels();" + "\n" + "while !pq.isEmpty() do" + "\n"
				+ "       Label label := pq.extractLabel();" + "\n" + "       foreach outgoing edge  e = (v,w) do"
				+ "\n" + "             Label newLabel := createLabel(label,e);" + "\n"
				+ "             if isDominated(newLabel) then " + "\n" + "                    continue;" + "\n"
				+ "             if isTerminalReached(newLabel) then" + "\n"
				+ "                    terminalList.insert(newLabel);" + "\n" + "             else" + "\n"
				+ "                    labelList(w).removeLabelDominatedBy(newLabel);" + "\n"
				+ "                    labelListAt(w).insert(newLabel);" + "\n"
				+ "                    pq.insert(newLabel);" + "\n" + "removeDominated(terminalList);" + "\n" + "\n"
				+ "\n" + "bool isDominated(newLabel)" + "\n" + "       foreach label in labelListAt(w)" + "\n"
				+ "              if allOf(label.criteria <= newLabel.criteria)" + "\n"
				+ "                     && oneOf(label.criteria < newLabel.criteria) then" + "\n"
				+ "                              return true" + "\n" + "       return false";
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return Locale.ENGLISH;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPH);
	}

	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives)
			throws IllegalArgumentException {
		String[] checkNodes = (String[]) primitives.get("Nodes (N|X|Y)");
		String checkQuery = (String) primitives.get("Query (From|To)");
		String[] checkEdges = (String[]) primitives.get("Edges (From|To|Weight1|Weight2|...)");
		ArrayList<String> nodeLabels = new ArrayList<String>();

		// Validate Nodes
		boolean nodeBool = Arrays.asList(checkNodes).stream().allMatch(x -> {
			if ((x.charAt(0) != '(') || x.charAt(x.length() - 1) != ')') {
				return false;
			}
			String[] strings = x.split("\\|");
			if (strings.length != 3) {
				return false;
			}
			if (!isPositiveInteger(strings[1])
					|| !isPositiveInteger(strings[2].substring(0, strings[2].length() - 1))) {
				return false;
			}
			nodeLabels.add(strings[0].substring(1, strings[0].length()));
			return true;
		});

		// Validate Query
		boolean queryBool = true;
		if ((checkQuery.charAt(0) != '(') || checkQuery.charAt(checkQuery.length() - 1) != ')') {
			return false;
		}

		String[] queryNodes = checkQuery.split("\\|");
		if (queryNodes.length != 2) {
			queryBool = false;
		} else {
			if (!nodeLabels.contains(queryNodes[0].substring(1, queryNodes[0].length()))
					|| !nodeLabels.contains(queryNodes[1].substring(0, queryNodes[1].length() - 1))) {
				queryBool = false;
			}
		}

		// Validate Edges
		Set<Integer> weightCounts = new HashSet<>();
		boolean edgeBool = Arrays.asList(checkEdges).stream().allMatch(x -> {
			if ((x.charAt(0) != '(') || x.charAt(x.length() - 1) != ')') {
				return false;
			}
			String[] edgeStrings = x.split("\\|");
			if (edgeStrings.length < 4) {
				return false;
			}
			if (!nodeLabels.contains(edgeStrings[0].substring(1, edgeStrings[0].length()))
					|| !nodeLabels.contains(edgeStrings[1])) {
				return false;
			}
			boolean valid = true;
			weightCounts.add(edgeStrings.length - 2);
			for (int i = 2; i < edgeStrings.length; i++) {
				if (i != edgeStrings.length - 1) {
					valid = valid && isPositiveInteger(edgeStrings[i]);
				} else {
					valid = valid && isPositiveInteger(edgeStrings[i].substring(0, edgeStrings[i].length() - 1));
				}
			}
			return valid;
		});
		// Check if all edges have the same amount of weights
		edgeBool = edgeBool && (weightCounts.size() == 1);
		System.out.println("NODESVALID:" + nodeBool + " | QUERYVALID: " + queryBool + " | EDGESVALID: " + edgeBool);

		return nodeBool && queryBool && edgeBool;
	}

	public boolean isPositiveInteger(String str) {
		if (str == null) {
			return false;
		}
		int length = str.length();
		if (length == 0) {
			return false;
		}
		int i = 0;
		if (str.charAt(0) == '-') {
			return false;
		}
		for (; i < length; i++) {
			char c = str.charAt(i);
			if (c < '0' || c > '9') {
				return false;
			}
		}
		return true;
	}

}