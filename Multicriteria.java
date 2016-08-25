/*
 * Multicriteria.java
 * Tim Witzel, Jonas Schlitzer, 2016 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.graph;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import java.util.Locale;
import algoanim.primitives.generators.Language;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.ArrayProperties;

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