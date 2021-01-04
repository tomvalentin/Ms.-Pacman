package pacman.controllers;

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

    private DataTuple[] trainingData = DataSaverLoader.LoadPacManData();

    public AIController() {

        ArrayList<Attribute> attributeList = new ArrayList<>();

        for (Attribute attribute : Attribute.values()) {
            attributeList.add(attribute);
        }

        ArrayList<ProcessedDataTuple> temp = processTuples();

        decisionTreeStart = new TreeUtil().generateDecisionTree(temp, attributeList);

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

        //Traversing the tree with the root as the starting point:
        Node currentNode = decisionTreeStart;

        while (true) {

            if (currentNode.getType() == NodeType.LEAF) {
                return currentNode.getDirection();
            }

            //Loop through the branches to find the relevant one to our current game state
            ArrayList<Branch> currentBranches = currentNode.getBranches();

            for (Branch branch : currentBranches) {
                if (branch.getValue() == currentAttributeValues.get(currentNode.getAttribute())) {

                    currentNode = branch.getChild();
                    break;
                }
            }
        }

    }

    //For testing
    public static void main(String[] args) {
        AIController ai = new AIController();

    }

    //Could be moved?
    public ArrayList<ProcessedDataTuple> processTuples() {

        ArrayList<ProcessedDataTuple> processedDataTuples = new ArrayList<>();

        for (int i = 0; i < trainingData.length; i++) {

            ProcessedDataTuple tempTuple = new ProcessedDataTuple();

            tempTuple.setMove(trainingData[i].DirectionChosen);

            /**
             * Adding direction and closest edible to processed Datatuple hashmap
             */
            if (trainingData[i].blinkyDist < trainingData[i].inkyDist && trainingData[i].blinkyDist < trainingData[i].pinkyDist && trainingData[i].blinkyDist < trainingData[i].sueDist) {
                tempTuple.setAttributeValue(Attribute.DIRECTIONOFCLOSESTGHOST, trainingData[i].blinkyDir);
                tempTuple.setAttributeValue(Attribute.ISCLOSESESTGHOSTEDIBLE, trainingData[i].isBlinkyEdible);
                tempTuple.setAttributeValue(Attribute.DISTANCETOCLOSESTGHOST, DataTuple.DiscreteTag.DiscretizeDouble(trainingData[i].blinkyDist));
            } else if (trainingData[i].inkyDist < trainingData[i].blinkyDist && trainingData[i].inkyDist < trainingData[i].pinkyDist && trainingData[i].inkyDist < trainingData[i].sueDist) {
                tempTuple.setAttributeValue(Attribute.DIRECTIONOFCLOSESTGHOST, trainingData[i].inkyDir);
                tempTuple.setAttributeValue(Attribute.ISCLOSESESTGHOSTEDIBLE, trainingData[i].isInkyEdible);
                tempTuple.setAttributeValue(Attribute.DISTANCETOCLOSESTGHOST, DataTuple.DiscreteTag.DiscretizeDouble(trainingData[i].inkyDist));

            } else if (trainingData[i].pinkyDist < trainingData[i].blinkyDist && trainingData[i].pinkyDist < trainingData[i].inkyDist && trainingData[i].pinkyDist < trainingData[i].sueDist) {
                tempTuple.setAttributeValue(Attribute.DIRECTIONOFCLOSESTGHOST, trainingData[i].pinkyDir);
                tempTuple.setAttributeValue(Attribute.ISCLOSESESTGHOSTEDIBLE, trainingData[i].isPinkyEdible);
                tempTuple.setAttributeValue(Attribute.DISTANCETOCLOSESTGHOST, DataTuple.DiscreteTag.DiscretizeDouble(trainingData[i].pinkyDist));

            } else {
                tempTuple.setAttributeValue(Attribute.DIRECTIONOFCLOSESTGHOST, trainingData[i].sueDir);
                tempTuple.setAttributeValue(Attribute.ISCLOSESESTGHOSTEDIBLE, trainingData[i].isSueEdible);
                tempTuple.setAttributeValue(Attribute.DISTANCETOCLOSESTGHOST, DataTuple.DiscreteTag.DiscretizeDouble(trainingData[i].sueDist));

            }


            processedDataTuples.add(tempTuple);


        }

        return processedDataTuples;


    }


}
