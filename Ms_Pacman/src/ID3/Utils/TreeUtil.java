package ID3.Utils;

import ID3.constants.Attribute;
import ID3.constants.NodeType;
import ID3.informationGain.InformationGain;
import ID3.models.AttributeValueMap;
import ID3.models.Branch;
import ID3.models.Node;
import ID3.models.ProcessedDataTuple;
import pacman.game.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A tree is an Arraylist of nodes, this class constucts and trains the tree.
 */
public class TreeUtil {
    int nodeIndex = 0;
    private HashMap<Attribute, ArrayList<Object>> attributeValuesMap;   //HashMap containing the keys: Attributes and their corresponding values

    public TreeUtil() {
        attributeValuesMap = AttributeValueMap.getAttributeValueMap();

    }

    /**
     *
     * Generates a decision tree from a training dataset and a list of relevant attributes
     *
     *
     * @param dataPartition The dataset
     * @param startingAttributeList The list of attributes
     * @return The starting node of the tree. I.e the Root node
     */
    public Node generateDecisionTree(ArrayList<ProcessedDataTuple> dataPartition, ArrayList<Attribute> startingAttributeList) {

        ArrayList<Attribute> attributeList = (ArrayList) startingAttributeList.clone();

        nodeIndex++; //To check if root note.. necessary?

        Node n;

        if (sameClass(dataPartition)) {
            //Leaf Node with Move
            n = new Node(NodeType.LEAF);
            n.setDirection(dataPartition.get(0).getMove());
            n.setIndex(nodeIndex++); //could be deleted

            return n;
        } else if (attributeList.isEmpty()) {

            //Leaf Node with most occurring move
            n = new Node(NodeType.LEAF);
            n.setDirection(mostOccurringMove(dataPartition));
            n.setIndex(nodeIndex++); //could be deleted

            return n;
        }

        //Node will be an attribute with children for all branch values
        Attribute a = InformationGain.attributeSelection(dataPartition, attributeList);

        //Delete this? Only NodeType == Leaf is used. Change to isLeaf?
        if (nodeIndex == 1) {
            n = new Node(NodeType.ROOT);

        } else {
            n = new Node(NodeType.ATTRIBUTE);
        }

        n.setAttribute(a);

        //Creates a list of branches which will contain the child nodes and which value was used
        ArrayList<Branch> branches = new ArrayList<>();
        n.setBranches(branches);

        attributeList.remove(a);

        //HashMap needed to seperate the tuples based on their attribute values
        HashMap<Object, ArrayList<ProcessedDataTuple>> splitDataset = new HashMap<>();

        ArrayList<Object> attributeValues = attributeValuesMap.get(a);

        for (Object value : attributeValues) {
            splitDataset.put(value, new ArrayList<>());
        }


        for (ProcessedDataTuple tuple : dataPartition) {
            Object value = tuple.getAttributeValue(a);
            splitDataset.get(value).add(tuple);

        }

        for (Map.Entry<Object, ArrayList<ProcessedDataTuple>> entry : splitDataset.entrySet()) {

            //If there are no tuples with the current value we add a branch and node to the current node with the most common direction in the previous dataset
            if (entry.getValue().isEmpty()) {
                Node tempNode = new Node(NodeType.LEAF);
                tempNode.setDirection(mostOccurringMove(dataPartition));

                Branch tempBranch = new Branch(n, tempNode, entry.getKey());

                branches.add(tempBranch);

            } else {
                Node tempNode = generateDecisionTree(entry.getValue(), attributeList);
                Branch tempBranch = new Branch(n, tempNode, entry.getKey());

                branches.add(tempBranch);

            }

        }

        return n;

    }


    /**
     * Helper Method
     */
    private boolean sameClass(ArrayList<ProcessedDataTuple> listToCheck) {
        Constants.MOVE move = listToCheck.get(0).getMove();    //get the first items attribute to compare if rest is same
        for (int i = 0; i < listToCheck.size(); i++) {
            if (move != listToCheck.get(i).getMove())
                return false;
        }
        return true;
    }

    private Constants.MOVE mostOccurringMove(ArrayList<ProcessedDataTuple> tuples) {


        int up = 0, down = 0, left = 0, right = 0;
        for (ProcessedDataTuple pdt : tuples) {
            if (pdt.getMove().equals(Constants.MOVE.UP))
                up++;
            else if (pdt.getMove().equals(Constants.MOVE.DOWN))
                down++;
            else if (pdt.getMove().equals(Constants.MOVE.LEFT))
                left++;
            else if (pdt.getMove().equals(Constants.MOVE.RIGHT))
                right++;
        }
        if (up > down && up > left && up > right)
            return Constants.MOVE.UP;
        else if (down > up && down > left && down > right)
            return Constants.MOVE.DOWN;
        else if (left > up && left > down && left > right)
            return Constants.MOVE.LEFT;
        else if (right > up && right > down && right > left)
            return Constants.MOVE.RIGHT;
        else {
            return Constants.MOVE.NEUTRAL;      //Needed since variables are sometimes equal
        }

    }

    public Constants.MOVE findMoveInTree(Node decisionTreeStart,  HashMap<Attribute, Object> currentAttributeValues){
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
    
}
