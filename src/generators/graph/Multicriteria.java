/*
 * Multicriteria.java
 * Tim Witzel, Jonas Schlitzer, 2016 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.graph;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import java.util.Locale;

import algoanim.primitives.generators.AnimationType;
import algoanim.primitives.generators.Language;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.ArrayProperties;
import algoanim.util.Coordinates;

public class Multicriteria implements Generator {
    private Language lang;
    private ArrayProperties ArrayProperties;
    private String graph;

    public void init(){
        lang = new AnimalScript("Multicriteria Shortest Path Search", "Tim Witzel, Jonas Schlitzer", 800, 600);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        ArrayProperties = (ArrayProperties)props.getPropertiesByName("ArrayProperties");
        graph = (String)primitives.get("graph");
        		
		//Changeable Graph
		//First and second Weight of Edges, two weights of zero mean no edge
		int[][] edgeweights1 = { { 0, 10, 15, 0, 21 }, { 0, 0, 0, 30 , 10  }, { 0, 0, 0, 20, 0 }, { 0, 0, 0, 0,0 }, { 0, 0, 10, 10,0 }  };
		int[][] edgeweights2 = { { 0, 2, 1, 0 ,1 }, { 0, 0, 0, 3 , 1 }, { 0, 0, 0, 2 ,0 }, { 0, 0, 0, 0,0 }, { 0, 0, 2, 4,0 }  };
		//Coordinates of graphNodes
		Coordinates[] graphNodes= { new Coordinates(300, 100), new Coordinates(50, 300), new Coordinates(500, 300),
				new Coordinates(300, 600), new Coordinates(300, 300) };
		//Labels of graph Nodes
		String[] nodeLabels =  new String[] { "Start", "A", "B", "Goal", "C" };
		//Startindex of the search
		int startIndex = 0;
		//Targetindex of the search
		int targetIndex = 3;
		
		new ShortestPathSearch(lang).start(edgeweights1, edgeweights2, graphNodes, nodeLabels, startIndex, targetIndex);
        return lang.toString();
    }

    public String getName() {
        return "Multicriteria Shortest Path Search";
    }

    public String getAlgorithmName() {
        return "Shortest Path";
    }

    public String getAnimationAuthor() {
        return "Tim Witzel, Jonas Schlitzer";
    }

    public String getDescription(){
        return "The multicriterial shortest path search is used to find Pareto optimal paths through a directed graph, where the edges have weights for multiple criteria."
 +"\n"
 +" It is loosely based on Dijkstras Shortest Path Algorithm, but has several adjustments to support more than one search criterion."
 +"\n"
 +" Because multiple criteria are used, the algorithm can result in more than one Pareto optimal path.";
    }

    public String getCodeExample(){
        return "Input: A timetable graph and a query"
 +"\n"
 +"Output: A set of advanced pareto optimal labels at the terminal"
 +"\n"
 +"\n"
 +"PriorityQueue pq := {};"
 +"\n"
 +"createStartLabels();"
 +"\n"
 +"while !pq.isEmpty() do"
 +"\n"
 +"       Label label := pq.extractLabel();"
 +"\n"
 +"       foreach outgoing edge  e = (v,w) do"
 +"\n"
 +"             Label newLabel := createLabel(label,e);"
 +"\n"
 +"             if isDominated(newLabel) then "
 +"\n"
 +"                    continue;"
 +"\n"
 +"             if isTerminalReached(newLabel) then"
 +"\n"
 +"                    terminalList.insert(newLabel);"
 +"\n"
 +"             else"
 +"\n"
 +"                    labelList(w).removeLabelDominatedBy(newLabel);"
 +"\n"
 +"                    labelListAt(w).insert(newLabel);"
 +"\n"
 +"                    pq.insert(newLabel);"
 +"\n"
 +"removeDominated(terminalList);"
 +"\n"
 +"\n"
 +"\n"
 +"bool isDominated(newLabel)"
 +"\n"
 +"       foreach label in labelListAt(w)"
 +"\n"
 +"              if allOf(label.criteria <= newLabel.criteria)"
 +"\n"
 +"                     && oneOf(label.criteria < newLabel.criteria) then"
 +"\n"
 +"                              return true"
 +"\n"
 +"       return false";
    }

    public String getFileExtension(){
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

}