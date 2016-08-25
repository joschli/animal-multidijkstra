package generators.graph;


import java.awt.Color;
import java.awt.Font;

import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.GraphProperties;
import algoanim.util.Coordinates;

public class Util {

	//Graph Colors
	public final static Color FILLCOLORNODE = Color.GRAY;
	public final static Color NODETEXTCOLOR = Color.WHITE;
	public final static Color GRAPHHIGHLIGHTCOLOR = new Color(139,0,0);
	public final static Color GRAPHELEMHIGHLIGHTCOLOR = Color.WHITE;
	
	//ArrayColors
	public final static Color ARRAYFILLCOLOR = Color.GRAY;
	public final static Color ARRAYTEXTCOLOR = Color.WHITE;
	public final static Color ARRAYELEMHIGHLIGHT = Color.WHITE;
	public final static Color ARRAYCELLHIGHLIGHT = new Color(120,30,0);
	
	//Positions
	public static int pqX = 400;
	public static int labelsX = 500;
	public static int sourceCodeX = 600;
	public static int newLabelX = 180;
	public static int pqY = 70;
	public static int labelsY = 70;
	public static int sourceCodeY = 50;
	public static int dominatesY = 500;
	public static int newLabelY = 470;
	
	public static GraphProperties getGraphProperties(){
		GraphProperties prop = new GraphProperties();
		prop.set("directed", true);
		prop.set("weighted", true);
		
		prop.set("nodeColor", NODETEXTCOLOR);
		prop.set("fillColor", FILLCOLORNODE);
		prop.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, GRAPHELEMHIGHLIGHTCOLOR);
		prop.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, GRAPHHIGHLIGHTCOLOR);
		
		return prop;
	}
	
	public static void setArrayProperties(ArrayProperties opt) {
		opt.set("fillColor", ARRAYFILLCOLOR);
		opt.set("elementColor", ARRAYTEXTCOLOR);
		opt.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, ARRAYELEMHIGHLIGHT);
		opt.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, ARRAYCELLHIGHLIGHT);
		opt.set("filled", false);
		opt.set("font", new Font(Font.SANS_SERIF, Font.PLAIN, 12));
	}

	public static void setUpOffset(Coordinates[] graphNodes) {
		int maxX = pqX - 50;
		int maxY = newLabelY - 50;
		for(Coordinates c : graphNodes){
			if(c.getX() > maxX){
				maxX = c.getX();
			}
			if(c.getY() > maxY){
				maxY = c.getY();
			}
		}
		Util.pqX = maxX + 50;
		Util.labelsX = pqX + 150;
		Util.sourceCodeX = labelsX + 150;
		Util.newLabelX = pqX - 175;
		Util.newLabelY = maxY + 75;
	}
}
