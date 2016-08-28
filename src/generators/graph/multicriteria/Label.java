package generators.graph.multicriteria;

import java.util.ArrayList;
import java.util.List;

public class Label implements Comparable<Label> {
	
	public int node;
	public Label prev;
	public List<Integer> weights = new ArrayList<>();
	
	public Label(int node, int... weights) {
		this.prev = null;
		this.node = node;
		for (int w : weights) {
			this.weights.add(w);
		}
	}

	public Label(Label prev, Edge next) {
		this.node = next.end;
		this.prev = prev;
		for (int i = 0; i < next.weights.size(); i++) {
			weights.add(prev.weights.get(i) + next.weights.get(i));
		}
	}

	public Label addLowerBounds(List<Integer> lowerBounds) {
		Label newLabel = new Label(node);
		for (int i = 0; i < weights.size(); i++) {
			newLabel.weights.add(weights.get(i) + lowerBounds.get(i));
		}
		return newLabel;
	}

	public int getPathLength(){
		  if(prev == null){
		    return 1;
		  }
		  return prev.getPathLength()+1;
		}


	@Override
	public int compareTo(Label o) {
		for (int i = 0; i < o.weights.size(); i++) {
			if (o.weights.get(i) < weights.get(i)) {
				return 1;
			} else if (o.weights.get(i) > weights.get(i)) {
				return -1;
			}
		}
		return 0;
	}

}