package generators.graph.multicriteria;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.GraphProperties;
import algoanim.util.Coordinates;
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.QuestionGroupModel;
import interactionsupport.models.QuestionModel;
import interactionsupport.models.TrueFalseQuestionModel;
import interactionsupport.patterns.Answer;

public class Util {

	//Graph Colors
	public static Color FILLCOLORNODE = Color.GRAY;
	public static Color NODETEXTCOLOR = Color.WHITE;
	public static Color GRAPHHIGHLIGHTCOLOR = new Color(139,0,0);
	public static Color GRAPHELEMHIGHLIGHTCOLOR = Color.WHITE;
	
	//ArrayColors
	public final static Color ARRAYFILLCOLOR = Color.GRAY;
	public final static Color ARRAYTEXTCOLOR = Color.WHITE;
	public final static Color ARRAYELEMHIGHLIGHT = Color.WHITE;
	public final static Color ARRAYCELLHIGHLIGHT = new Color(120, 30, 0);

	// Positions
	public static int pqX = 400;
	public static int labelsX = 500;
	public static int sourceCodeX = 600;
	public static int newLabelX = 100;
	public static int pqY = 70;
	public static int labelsY = 70;
	public static int sourceCodeY = 50;
	public static int dominatesY = 500;
	public static int newLabelY = 470;
	public static int counterY = 470;

	// Label length
	public static int MAXLABELLENGTH = 14;

	public static final String labels1Prompt = "On which nodes will labels be created for the current label?";
	public static final String labels2Prompt = "What are the weights for the next created label, with the previous Label being XY and the Edge being XE? (Answer: Weight1,Weight2)";
	public static final String labels3Prompt = "How are the weights for the next created label calculated?";
	public static final String labels4Prompt = "How many labels will be generated in the next iteration of the while-loop?";
	
	public static final String dom1Prompt = "Why was the Label XY dominated by Label XZ?";
	public static final String dom2Prompt = "The current Label will be dominated by the Labels at the node.";
	public static final String dom4Prompt = "The current Label will be dominated by the Labels at the node.";
  
	public static final String dom3Prompt = "Why was the Label XY not dominated by Label XZ?";
	
	public static final String term1Prompt = "What happens now, after the first result has been found?";
	public static final String term2Prompt = "Now that the first result was found, the algorithm terminates";
	public static final String term3Prompt = "All results in the terminal list are Pareto optimal";
	public static final String term4Prompt = "In theory, what is the highest number of possible pareto optimal solutions in a search with two criteria?";
	
	public static final String labLi1Prompt = "Why was the label in the labellist deleted, before the new one was inserted?";
	public static final String labLi2Prompt = "There can be more than one label in the labellist of each node";
	public static final String labLi3Prompt = "The labels in the labellists at each node serve the purpose of dominating labels at their nodes";
	public static final String labLi4Prompt = "The labels in the labellists can be used to dominate labels at other nodes.";
	public static final String labLi5Prompt = "Why do we need to save the Labels for the specific nodes?";
	
	public static final String group1 = "labelQuestions";
	public static final String group2 = "dominationQuestions";
	public static final String group3 = "terminalQuestions";
	public static final String group4 = "labelListQuestions";
	
	public static int percentageOfQuestions = 100;
	public static int rightPerCategories = 7;

  public static  QuestionGroupModel g1 = new QuestionGroupModel(group1, rightPerCategories);
  public static  QuestionGroupModel g2 = new QuestionGroupModel(group2, rightPerCategories);
  public static  QuestionGroupModel g3 = new QuestionGroupModel(group3, rightPerCategories);
  public static  QuestionGroupModel g4 = new QuestionGroupModel(group4, rightPerCategories);
	
  public static final int LABELDOMINATED = 0;
  public static final int WILLBEDOMINATED = 1;
  public static final int WILLNOTBEDOMINATED = 2;
  public static final int NOTDOMINATED = 3;
  
  public static int copyId = 0;
  
	public static final HashMap<Integer,List<QuestionModel>> questions = new HashMap<Integer, List<QuestionModel>>();
	public static final List<String> needsAnswer = new ArrayList<String>();
	
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

	public static void setGraphColors(ArrayProperties props) {
		Util.FILLCOLORNODE = (Color) props.get("fillColor");
		Util.NODETEXTCOLOR = (Color) props.get("elementColor");
		Util.GRAPHELEMHIGHLIGHTCOLOR = (Color) props.get("elementColor");
		Util.GRAPHHIGHLIGHTCOLOR = (Color) props.get("cellHighlight");
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
		for (Coordinates c : graphNodes) {
			if (c.getX() > maxX) {
				maxX = c.getX();
			}
			if (c.getY() > maxY) {
				maxY = c.getY();
			}
		}
		Util.pqX = maxX + 50;
		Util.labelsX = pqX + 150;
		Util.sourceCodeX = labelsX + 150;
		Util.newLabelX = pqX - 220;
		Util.newLabelY = maxY + 50;
		Util.counterY = newLabelY + 50;
	}
	
	public static void setUpQuestions(Language language){
	  g1 = new QuestionGroupModel(group1, rightPerCategories);
	  g2 = new QuestionGroupModel(group2, rightPerCategories);
	  g3 = new QuestionGroupModel(group3, rightPerCategories);
	  g4 = new QuestionGroupModel(group4, rightPerCategories);
	  language.addQuestionGroup(g1);
	  language.addQuestionGroup(g2);
	  language.addQuestionGroup(g3);
	  language.addQuestionGroup(g4);
	  setUpGroup1();
	  setUpGroup2();
	  setUpGroup3();
	  setUpGroup4();
	}


  private static void setUpGroup1() {
    MultipleChoiceQuestionModel q1 = new MultipleChoiceQuestionModel("q1");
    q1.setPrompt(labels1Prompt);
    q1.addAnswer("On all nodes at the end of all the outgoing edges of the current node.", 1, "Correct!");
    q1.addAnswer("On the node, which is connected to the current node with the best edge.", 0, "Incorrect! All Edges are treated equally and labels will be created for each outgoing edge");
    q1.addAnswer("On all nodes, that are connected to the current node, including incoming and outcoming edges", 0 , "Incorrect. Only outgoing edges from the current node are important as the graph is directed");
    
    MultipleChoiceQuestionModel q2 = new MultipleChoiceQuestionModel("q2");
    q2.setPrompt(labels1Prompt);
    needsAnswer.add("q2");
    //Answers generated based on graph state
    
    FillInBlanksQuestionModel q3 = new FillInBlanksQuestionModel("q3");
    q3.setPrompt(labels2Prompt);
    needsAnswer.add("q3");
    //Answers generated based on graph state
    
    MultipleChoiceQuestionModel q4 = new MultipleChoiceQuestionModel("q4");
    q4.setPrompt(labels3Prompt);
    q4.addAnswer("The new weights are calculated by adding the according weights of the edge to the current weights of the labels", 1, "Correct");
    q4.addAnswer("Both weights of the edge will be added to each of the labels weights", 0, "Incorrect! The first criterion weight will be added only to the first criterion weight for the label");
    q4.addAnswer("The weights of the edge will be the weights for the new label", 0, "Incorrect. Weights are summed up over the duration of the search");
    
    MultipleChoiceQuestionModel q5 = new MultipleChoiceQuestionModel("q5");
    q5.setPrompt(labels4Prompt);
    needsAnswer.add("q5");
    

    q1.setGroupID(group1);
    q2.setGroupID(group1);
    q3.setGroupID(group1);
    q4.setGroupID(group1);
    q5.setGroupID(group1);   
    
    questions.put(1, Arrays.asList(q1,q2,q3,q4,q5));
  }
  
  private static void setUpGroup2(){
    MultipleChoiceQuestionModel q6 = new MultipleChoiceQuestionModel("q6");
    q6.setPrompt(dom1Prompt);
    needsAnswer.add("q6");
    
    TrueFalseQuestionModel q7 = new TrueFalseQuestionModel("q7");
    q7.setPrompt(dom2Prompt);
    needsAnswer.add("q7");
    
    TrueFalseQuestionModel q7_2 = new TrueFalseQuestionModel("q7_2");
    q7_2.setPrompt(dom4Prompt);
    needsAnswer.add("q7_2");
    
    MultipleChoiceQuestionModel q8 = new MultipleChoiceQuestionModel("q8");
    q8.setPrompt(dom3Prompt);
    needsAnswer.add("q8");

    q6.setGroupID(group2);
    q7.setGroupID(group2);
    q8.setGroupID(group2);
    
    questions.put(2, Arrays.asList(q6,q7,q8, q7_2));
  }
  
  private static void setUpGroup3(){
    MultipleChoiceQuestionModel q9 = new MultipleChoiceQuestionModel("q9");
    q9.setPrompt(term1Prompt);
    q9.addAnswer("The algorithm stops, as it has found an optimal result", 0, "Incorrect! With Pareto optimality multiple results will be optimal and we want to find all of them");
    q9.addAnswer("The algorithm continues to search for additional solutions, as more than one can be optimal", 1 , "Correct");
    q9.addAnswer("The algorithm continues until it finds the single most optimal solution", 0, "Incorrect. With Pareto optimality multiple results will be optimal.");
    q9.addAnswer("The algorithm stops after it finishes the iteration for the current node",0, "Incorrect");
    
    TrueFalseQuestionModel q10 = new TrueFalseQuestionModel("q10");
    q10.setPrompt(term2Prompt);
    q10.setCorrectAnswer(false);
    q10.setPointsPossible(1);
    
    TrueFalseQuestionModel q11 = new TrueFalseQuestionModel("q11");
    q11.setPrompt(term3Prompt);
    q11.setCorrectAnswer(false);
    q11.setPointsPossible(1);
    q11.setFeedbackForAnswer(false, "Incorrect, as the results in the terminal list may be dominated by later labels, therefore, making them not Pareto optimal");
    
    MultipleChoiceQuestionModel q12 = new MultipleChoiceQuestionModel("q12");
    q12.setPrompt(term4Prompt);
    q12.addAnswer("One", 0, "Incorrect");
    q12.addAnswer("Two", 0, "Incorrect");
    q12.addAnswer("Three", 0, "Incorrect");
    q12.addAnswer("Four", 0, "Incorrect");
    q12.addAnswer("Thirteen", 0 ,"Incorrect");
    q12.addAnswer("Theoretically an infinite number of optimal solutions is possible", 1, "Correct");
    
    q9.setGroupID(group3);
    q10.setGroupID(group3);
    q11.setGroupID(group3);
    q12.setGroupID(group3);
   
    questions.put(3, Arrays.asList(q9,q10,q11,q12));
  }
  
  private static void setUpGroup4(){
    MultipleChoiceQuestionModel q13 = new MultipleChoiceQuestionModel("q13");
    q13.setPrompt(labLi1Prompt);
    q13.addAnswer("There can be only one label in the labellist for each node: The best one",0,"Incorrect! There can be multiple Labels in the labelllist, as multiple labels can be optimal");
    q13.addAnswer("The label dominated the previous label in the labellist and therefore the dominated label is worse and can be deleted", 3 , "Correct");
    q13.addAnswer("Only the newest labels at the node are saved.", 0, "Incorrect. All Pareto optimal Labels at the node are saved");
    
    TrueFalseQuestionModel q14 = new TrueFalseQuestionModel("q14");
    q14.setPrompt(labLi2Prompt);
    q14.setCorrectAnswer(true);
    q14.setPointsPossible(1);
    q14.setFeedbackForAnswer(false, "Incorrect. The labellist for each node saves all Pareto optimal labels at that node");
    
    TrueFalseQuestionModel q15 = new TrueFalseQuestionModel("q15");
    q15.setPrompt(labLi3Prompt);
    q15.setCorrectAnswer(true);
    q15.setPointsPossible(1);
    q15.setFeedbackForAnswer(false, "Incorrect");
    
    TrueFalseQuestionModel q16 = new TrueFalseQuestionModel("q16");
    q16.setPrompt(labLi4Prompt);
    q16.setCorrectAnswer(false);
    q16.setPointsPossible(1);
    q16.setFeedbackForAnswer(false, "Incorrect. They may only be used to dominate labels at the specific node they are at");
    
    q13.setGroupID(group4);
    q14.setGroupID(group4);
    q15.setGroupID(group4);
    q16.setGroupID(group4);
    
    /*MultipleChoiceQuestionModel q17 = new MultipleChoiceQuestionModel("q17");
    q17.setPrompt(labLi5Prompt);
    q17.addAnswer("As nodes are visited multiple times, these labels are saved to dominate later ones.", 3, "Correct");
    */
    questions.put(4, Arrays.asList(q13,q14,q15,q16));
    
  }


  public static void getTerminalQuestion(Language language, int termSize) {
    if(ThreadLocalRandom.current().nextInt(0, 100) > percentageOfQuestions){
      return;
    }
    
    List<QuestionModel> qs = questions.get(3);
    
    QuestionModel q;
    
    if(termSize == 0){
      q = qs.get(ThreadLocalRandom.current().nextInt(0, qs.size()));
    }else{
      q = qs.get(ThreadLocalRandom.current().nextInt(2,qs.size()));
    }
    
    if(q.getID().equals("q9") || q.getID().equals("q12")){
      language.addMCQuestion((MultipleChoiceQuestionModel) q);
    }else{
      language.addTFQuestion((TrueFalseQuestionModel) q);
    }
  }
  
  public static void getLabelCreationQuestion(Language language, boolean beforeFor,Label l, Edge e, int numEdges, List<String> rightNodes, List<String> allNodes){
    if(ThreadLocalRandom.current().nextInt(0, 100) > percentageOfQuestions){
      return;
    }
    List<QuestionModel> qs = questions.get(1);
    QuestionModel q;
    if(beforeFor){
      //q1,q2,q5
      q = qs.get(Arrays.asList(0,1,4).get(ThreadLocalRandom.current().nextInt(0, 3)));
      if(needsAnswer.contains(q.getID())){
        ArrayList<Answer> answers = getAnswerForLabelCreationQuestion(q.getID(), numEdges, rightNodes, allNodes);
        MultipleChoiceQuestionModel mq = new MultipleChoiceQuestionModel("copy" +copyId);

        copyId++;
        mq.setPrompt(q.getPrompt());
        Collections.shuffle(answers);
        for(Answer a : answers){
          mq.addAnswer(a.getAnswer(), a.getPoints(), a.getComment());
        }
        mq.setGroupID(group1);
        q = mq;
      }
      language.addMCQuestion((MultipleChoiceQuestionModel) q);
    }else{
      q =  qs.get(ThreadLocalRandom.current().nextInt(2,4));
      if(needsAnswer.contains(q.getID())){
        FillInBlanksQuestionModel fq = new FillInBlanksQuestionModel("copy" + copyId);
        copyId++;
        fq.setPrompt(q.getPrompt().replace("XY", l.toString()).replace("XE", e.toString()));
        Label newLabel = new Label(l, e, l.index, new ArrayList<>());
        String weights = StringUtil.getLabelWeights(newLabel.weights);
        fq.addAnswer(weights.substring(0, weights.length()-1), 1, "Correct");
        fq.setGroupID(group1);
        language.addFIBQuestion(fq);
      }else{ 
        language.addMCQuestion((MultipleChoiceQuestionModel) q);
      }
    }
  }


  private static ArrayList<Answer> getAnswerForLabelCreationQuestion(String id, int numberOfEdges, List<String> rightNodes, List<String> allNodes) {
    ArrayList<Answer> answers = new ArrayList<Answer>();
    if(id.equals("q2")){
      //NODELIST
      answers.add(new Answer(rightNodes.stream().reduce("" , (a,b) -> {return a + "," + b;}).substring(1), "Correct!", 1 ,true));
      for(int i = 0; i < 3; i++){
        List<String> wrongNodes = getWrongNodeList(allNodes, rightNodes); 
        answers.add(new Answer(wrongNodes.stream().reduce("" , (a,b) -> {return a + "," + b;}).substring(1), "Incorrect!!", 0 ,false));
        
      }
    }
    if(id.equals("q5")){
      //NUMBER OF EDGES AT NODE
      answers.add(new Answer(String.valueOf(numberOfEdges), "Correct!", 1, true));
      answers.add(new Answer(String.valueOf(numberOfEdges + ThreadLocalRandom.current().nextInt(4,7)), "Incorrect!", 0, false));
      answers.add(new Answer(String.valueOf(numberOfEdges - ThreadLocalRandom.current().nextInt(4,7)), "Incorrect!", 0, false));
      answers.add(new Answer(String.valueOf(numberOfEdges + ThreadLocalRandom.current().nextInt(1,4)), "Incorrect!", 0, false));
      answers.add(new Answer(String.valueOf(numberOfEdges - ThreadLocalRandom.current().nextInt(1,4)), "Incorrect!", 0, false));
    }
    return answers;
  }


  private static List<String> getWrongNodeList(List<String> allNodes, List<String> rightNodes) {
    List<String> wrongNodes;
    while(true){
      wrongNodes = allNodes.stream().filter(x -> ThreadLocalRandom.current().nextBoolean()).collect(Collectors.toList());
      if(wrongNodes.isEmpty() || wrongNodes.stream().allMatch(x -> rightNodes.contains(x))){
        continue;
      }else{
        break; 
      }
    }
    return wrongNodes;
  }


  public static void getLabelListQuestion(Language language, boolean removed) {
    if(ThreadLocalRandom.current().nextInt(0, 100) > percentageOfQuestions){
      return;
    }
    
    List<QuestionModel> qs = questions.get(4);
    QuestionModel q;
    if(removed){
      q = qs.get(ThreadLocalRandom.current().nextInt(0, qs.size()));
    }else{
      q = qs.get(ThreadLocalRandom.current().nextInt(1,qs.size()));
    }
    
    if(q.getID().equals("q13")){
      language.addMCQuestion((MultipleChoiceQuestionModel) q);
    }else{
      language.addTFQuestion((TrueFalseQuestionModel) q);
    }
  }
  
  public static void getDominatedQuestion(Language language, int state, Label l, Label newL, String lstr, String newLstr){
    if(ThreadLocalRandom.current().nextInt(0, 100) > percentageOfQuestions){
      return;
    }
    
    switch(state){
      case LABELDOMINATED:
        MultipleChoiceQuestionModel mq = new MultipleChoiceQuestionModel("copy" + copyId);
        copyId++;
        mq.setGroupID(group2);
        mq.setPrompt(dom1Prompt.replace("XY", newLstr).replace("XZ", lstr));
        ArrayList<Answer> answers = Util.getDominatedAnswers(true, l, newL);
        Collections.shuffle(answers);
        for(Answer a : answers){
          mq.addAnswer(a.getAnswer(), a.getPoints(), a.getComment());
        }
        language.addMCQuestion(mq);
        
        break;
      case NOTDOMINATED:
        MultipleChoiceQuestionModel mq2 = new MultipleChoiceQuestionModel("copy"+ copyId);
        copyId++;
        mq2.setGroupID(group2);
        mq2.setPrompt(dom3Prompt.replace("XY", newLstr).replace("XZ", lstr));
        ArrayList<Answer> answers2 = Util.getDominatedAnswers(false, l, newL);
        Collections.shuffle(answers2);
        for(Answer a : answers2){
          mq2.addAnswer(a.getAnswer(), a.getPoints(), a.getComment());
        }
        
        language.addMCQuestion(mq2);
        break;
      case WILLBEDOMINATED:
        TrueFalseQuestionModel q = (TrueFalseQuestionModel) questions.get(2).get(1);
        q.setCorrectAnswer(true);
        q.setPointsPossible(1);
        q.setFeedbackForAnswer(true, "Correct!");
        q.setFeedbackForAnswer(false, "Incorrect!");
        language.addTFQuestion(q);
        break;
      case WILLNOTBEDOMINATED:
        TrueFalseQuestionModel q2 = (TrueFalseQuestionModel) questions.get(2).get(3);
        q2.setCorrectAnswer(false);
        q2.setPointsPossible(1);
        q2.setFeedbackForAnswer(false, "Correct!");
        q2.setFeedbackForAnswer(true, "Incorrect!");
        language.addTFQuestion(q2);
        break;
      default:
        break;
        
    }
  }


  private static ArrayList<Answer> getDominatedAnswers(boolean b, Label l, Label newL) {
    ArrayList<Answer> answers = new ArrayList<Answer>();
    
    Label dominating = l;
    Label dominated = newL;
    boolean w1isLess = dominating.weights.get(0) < dominated.weights.get(0);
    boolean w2isLess = dominating.weights.get(1) < dominated.weights.get(1);
    if(b){
      String wrong1 = "Because the sum of both weights is lower for the dominating Label.";
      answers.add(new Answer(wrong1, "Incorrect. The sum of weights is irrelevant, both weights are considered separately.", 0, false));

      if(w1isLess && w2isLess){
        String answerStr = "Because both weights of the dominating Label are lower than the weights of the dominated Label.";
        answers.add(new Answer(answerStr, "Correct!" , 1, true));
        String wrong2 = "Because at least one weight is less than the according weight of the dominated Label. The other weight would not necessarily be needed to dominate the other Label in this case.";
        answers.add(new Answer(wrong2, "Incorrect! The other weight matters as it has to be equal or lower to be able to dominate the other label", 0, false));
        
      }
      if((w1isLess && !w2isLess) || (!w1isLess && w2isLess)){
        String answerStr = "Because one weight is lower and the other weight is not greater than the according weights of the dominated label.";
        answers.add(new Answer(answerStr, "Correct", 1 , true));
        String wrong2 = "Because at least one weight is less than the according weight of the dominated label. The other weight does not matter in this case.";
        answers.add(new Answer(wrong2, "Incorrect! The other weight matters as it has to be equal or lower to dominate the other Label.", 0, false));
      }
      
    }else{
      int pathLengthDominating = l.getPathLength();
      int pathLengthDominated = newL.getPathLength();
      String correctAnswer = "";
      String wrong1;
      String wrong2;
      if(w1isLess && !w2isLess || !w1isLess && w2isLess){
        correctAnswer = "Because only one weight of the second Label is lower than the according weight of the first Label and the second weight is greater and not equal or lower. Therefore, they can not dominate each other and are both Pareto optimal.";
        answers.add(new Answer(correctAnswer, "Correct!", 1 ,true));
        wrong1 = "Because both weights are required to be lower than the according weights of the first Label to dominate the first Label";
        answers.add(new Answer(wrong1, "Incorrect! It would suffice if one weight is equal and one weight is lower to dominate the Label.", 0, false));
        wrong2  = "Because the sum of both weights of the second label is worse than the sum of the first label and therefore, the second label is objectively not better.";
        answers.add(new Answer(wrong2, "Incorrect! The sum does not matter at all for domination, as weights are considered separately.", 0, false));
      }
      
      if(!w1isLess && !w2isLess){
        correctAnswer = "Because both weights of the second label are worse or equal than the weights of the first label and therefore, the second label is objectively not better.";
        answers.add(new Answer(correctAnswer, "Correct!", 1 , true));
        wrong1  = "Because the sum of both weights of the second label is worse than the sum of the first label and therefore, the second label is objectively not better.";
        answers.add(new Answer(wrong1, "Incorrect! The sum does not matter at all for domination, as weights are considered separately.", 0, false));
        
      }
      
      String wrong4 = "Because the number of labels at the node is low enough, so that no domination is required yet.";
      answers.add(new Answer(wrong4, "Incorrect. Domination is always required to only keep the Pareto optimal labels at a node.", 0, false));
      
      if(pathLengthDominating > pathLengthDominated){
        String wrong3 = "Because the second Label has a longer path of nodes and can therefore not dominate the first Label with the shorter path.";
        answers.add(new Answer(wrong3, "Incorrect! The length of the path does not matter at all.", 0, false));
      }
  
    }
    return answers;
  }
	
}
