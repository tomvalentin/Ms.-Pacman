package pacman.controllers;

import ID3.Utils.DataProcessorUtil;
import ID3.Utils.PrintUtil;
import ID3.Utils.TreeAccuracy;
import ID3.Utils.TreeUtil;
import ID3.constants.Attribute;
import ID3.constants.NodeType;
import ID3.models.Branch;
import ID3.models.Node;
import ID3.models.ProcessedDataTuple;
import dataRecording.DataSaverLoader;
import dataRecording.DataTuple;
import pacman.game.Constants;
import pacman.game.Game;

import java.util.ArrayList;
import java.util.HashMap;

public class AIController extends Controller<Constants.MOVE> {
    private Node decisionTreeStart;
    private DataProcessorUtil dataProcessorUtil = new DataProcessorUtil();
    private TreeUtil treeUtil = new TreeUtil();



    public AIController() {

        ArrayList<Attribute> attributeList = new ArrayList<>();

        for (Attribute attribute : Attribute.values()) {
            attributeList.add(attribute);
        }

        //generate data from recorded session
        ArrayList<ProcessedDataTuple>[] allData = dataProcessorUtil.generateData();

        ArrayList<ProcessedDataTuple> temp = allData[0]; //training data 80% of generated data

        decisionTreeStart = treeUtil.generateDecisionTree(temp, attributeList);
        //test the accuracy of the generated tree with 20% of the generated data
        System.out.println(new TreeAccuracy().testAccuracy(decisionTreeStart, allData[1]));

        //Print a graphical representation of the tree
        //PrintUtil.printTree(decisionTreeStart, 0);
    }

    public Constants.MOVE getMove(Game game, long timeDue) {

        //retrieve current game info needed to navigate decision tree
        boolean isBlinkyEdible = game.isGhostEdible(Constants.GHOST.BLINKY);
        boolean isInkyEdible = game.isGhostEdible(Constants.GHOST.INKY);
        boolean isPinkyEdible = game.isGhostEdible(Constants.GHOST.PINKY);
        boolean isSueEdible = game.isGhostEdible(Constants.GHOST.SUE);

        int blinkyDist = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(Constants.GHOST.BLINKY));
        int inkyDist = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(Constants.GHOST.INKY));
        int pinkyDist = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(Constants.GHOST.PINKY));
        int sueDist = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(Constants.GHOST.SUE));

        Constants.MOVE blinkyDir = game.getGhostLastMoveMade(Constants.GHOST.BLINKY);
        Constants.MOVE inkyDir = game.getGhostLastMoveMade(Constants.GHOST.INKY);
        Constants.MOVE pinkyDir = game.getGhostLastMoveMade(Constants.GHOST.PINKY);
        Constants.MOVE sueDir = game.getGhostLastMoveMade(Constants.GHOST.SUE);

        //Stores them in a HashMap for later access
        HashMap<Attribute, Object> currentAttributeValues = new HashMap<>();

        if (blinkyDist < inkyDist && blinkyDist < pinkyDist && blinkyDist < sueDist) {
            currentAttributeValues.put(Attribute.DIRECTIONOFCLOSESTGHOST, blinkyDir);
            currentAttributeValues.put(Attribute.ISCLOSESESTGHOSTEDIBLE, isBlinkyEdible);
            currentAttributeValues.put(Attribute.DISTANCETOCLOSESTGHOST, DataTuple.DiscreteTag.DiscretizeDouble(blinkyDist));

        } else if (inkyDist < blinkyDist && inkyDist < pinkyDist && inkyDist < sueDist) {
            currentAttributeValues.put(Attribute.DIRECTIONOFCLOSESTGHOST, inkyDir);
            currentAttributeValues.put(Attribute.ISCLOSESESTGHOSTEDIBLE, isInkyEdible);
            currentAttributeValues.put(Attribute.DISTANCETOCLOSESTGHOST, DataTuple.DiscreteTag.DiscretizeDouble(inkyDist));

        } else if (pinkyDist < blinkyDist && pinkyDist < inkyDist && pinkyDist < sueDist) {
            currentAttributeValues.put(Attribute.DIRECTIONOFCLOSESTGHOST, pinkyDir);
            currentAttributeValues.put(Attribute.ISCLOSESESTGHOSTEDIBLE, isPinkyEdible);
            currentAttributeValues.put(Attribute.DISTANCETOCLOSESTGHOST, DataTuple.DiscreteTag.DiscretizeDouble(pinkyDist));

        } else {
            currentAttributeValues.put(Attribute.DIRECTIONOFCLOSESTGHOST, sueDir);
            currentAttributeValues.put(Attribute.ISCLOSESESTGHOSTEDIBLE, isSueEdible);
            currentAttributeValues.put(Attribute.DISTANCETOCLOSESTGHOST, DataTuple.DiscreteTag.DiscretizeDouble(sueDist));

        }
        currentAttributeValues.put(Attribute.PILLSLEFT,DataTuple.DiscreteTag.DiscretizeDouble(game.getNumberOfActivePills()));
        currentAttributeValues.put(Attribute.POWERPILLSLEFT,DataTuple.DiscreteTag.DiscretizeDouble(game.getNumberOfActivePowerPills()));
        currentAttributeValues.put(Attribute.LIVESLEFT,DataTuple.DiscreteTag.DiscretizeDouble(game.getPacmanNumberOfLivesRemaining()));

        //traverse and return the move found from decision tree
        return treeUtil.findMoveInTree(decisionTreeStart, currentAttributeValues);

    }

    //For testing
    public static void main(String[] args) {
        AIController ai = new AIController();

    }


}
