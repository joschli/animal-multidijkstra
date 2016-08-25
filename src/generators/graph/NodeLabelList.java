package generators.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import algoanim.primitives.Graph;
import algoanim.primitives.StringArray;
import algoanim.primitives.generators.Language;

public class NodeLabelList {

	private StringArray array;
	private List<String> nodes;
	private List<Integer> indices;
	
	public NodeLabelList(StringArray array, Graph graph){
		this.array = array;
		this.nodes = new ArrayList<String> ();
		this.indices = new ArrayList<Integer>();
		for(int i = 0; i < graph.getSize(); i++){
			String nodeString = graph.getNodeLabel(i);
			nodes.add(nodeString);
			indices.add(i*2);
		}
		if( nodes.indexOf(graph.getNodeLabel(graph.getTargetNode())) != nodes.size()-1 ){
			Collections.swap(nodes, nodes.size()-1, nodes.indexOf(graph.getNodeLabel(graph.getTargetNode())));
		}
		for(int k = 0; k < array.getLength(); k++){
			array.put(k, "", null, null);
		}
		
		for(int j = 0 ; j < nodes.size(); j++){
			array.put(indices.get(j), nodes.get(j), null, null);
		}
		
		
	}

	public void addLabelToNodeList(String label, String node){
		int startIndex = nodes.indexOf(node);
		//Change all 
		for(int i = array.getLength()-1; i > indices.get(startIndex)+1; i--){
			array.put(i, array.getData(i-1), null, null);
		}

		array.put(indices.get(startIndex)+1, label, null, null);
		for(int i = startIndex; i < nodes.size(); i++){
			indices.set(i, indices.get(i)+1);;
		}
	}
	
	public void highlightNodeList(String node){
		int nodeIndex = nodes.indexOf(node);
		int arrayIndexStart = 0; 
		int arrayIndexEnd = indices.get(nodeIndex)+1;
		if(nodeIndex != 0){
			arrayIndexStart = indices.get(nodeIndex-1)+2;
		}
		array.highlightCell(arrayIndexStart, arrayIndexEnd, null, null);
	}
	

	public void highlightLastAdded(String nodeLabel) {
		int nodeIndex = nodes.indexOf(nodeLabel);
		array.highlightCell(indices.get(nodeIndex), null, null);
	}
	

	public void remove(int indexOf, String nodeLabel, Language l) {
		int nodeIndex = nodes.indexOf(nodeLabel);
		int position = nodeIndex == 0 ? indexOf+1 : indices.get(nodeIndex-1)+indexOf+3;
		array.highlightCell(position, null, null);
		l.nextStep();
		array.unhighlightCell(position, null, null);
		array.put(position, "", null, null);
		
		for(int i = position; i < array.getLength()-1; i++){
			array.put(i, array.getData(i+1), null, null);
		}
		
		for(int i = nodeIndex; i < nodes.size(); i++){
			indices.set(i, indices.get(i)-1);;
		}
		l.nextStep();
	}

	
	
	//WRAPPING METHODS
	public void highlightCell(int i, int j, Object object, Object object2) {
		array.highlightCell(i, j, null, null);
	}

	public void unhighlightAll() {
		for(int i = 0; i < array.getLength(); i++){
			array.unhighlightCell(i, null, null);
		}
	}

	public void hide() {
		array.hide();
		
	}

	public void showIndices(boolean b, Object object, Object object2) {
		array.showIndices(b, null, null);
	}

	public void show() {
		array.show();
	}

	public void put(int i, String string, Object object, Object object2) {
		array.put(i, string, null, null);
	}



	
	
	

}
