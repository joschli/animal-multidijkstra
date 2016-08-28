package generators.graph.multicriteria;

import java.util.List;

import algoanim.primitives.Graph;

public class StringUtil {
	public static String getLabelWeights(List<Integer> weights) {
		String out = "";
		for (int i : weights) {
			out += i + ",";
		}
		return out;
	}

	public static String getLabelPath(Graph graph, Label label) {
		String path = "";
		Label l = label;
		while (l != null) {
			path = path + graph.getNodeLabel(l.node) + ",";
			l = l.prev;
		}
		path = path.substring(0, path.length() - 1);
		return path;
	}

	public static String getLabelString(Graph graph, Label label, int labelIndex) {
		String labelStr = createFullLabelString(graph, label, labelIndex);
		labelStr = labelStr.length() > Util.MAXLABELLENGTH ? labelStr.substring(0, Util.MAXLABELLENGTH) + "..."
				: labelStr;
		return labelStr;
	}
	
	public static String createFullLabelString(Graph graph, Label label, int labelIndex){
		return "L" + (labelIndex != -1 ? labelIndex : "") + ":[" + getLabelWeights(label.weights) + "("
				+ getLabelPath(graph, label) + ")]";
	}
}
