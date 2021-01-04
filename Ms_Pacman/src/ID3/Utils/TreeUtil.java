package ID3.Utils;

import ID3.constants.Attribute;
import ID3.constants.NodeType;
import ID3.informationGain.informationGain;
import ID3.models.AttributeValueMap;
import ID3.models.Node;
import ID3.models.ProcessedDataTuple;
import dataRecording.DataTuple;
import pacman.game.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A tree is an Arraylist of nodes, this class constucts and trains the tree.
 */
public class TreeUtil {
    private ArrayList<Node> tree = new ArrayList<>();
    int nodeIndex = 0;
    int iteration = 0;
    private HashMap<Attribute, ArrayList<Object>> attributeValuesMap;

    public TreeUtil() {
        attributeValuesMap = AttributeValueMap.getAttributeValueMap();

    }

    public ArrayList<Node> generateDecisionTree(ArrayList<ProcessedDataTuple> dataPartition, ArrayList<Attribute> attributeList) { //Not sure if we use DataTuple as the model for data?
        //calculate information gain, take attribute and call recursively
        recursion(dataPartition, attributeList);

        return tree;
    }

    private Node recursion(ArrayList<ProcessedDataTuple> dataPartition, ArrayList<Attribute> startingAttributeList) {

        ArrayList<Attribute> attributeList = (ArrayList) startingAttributeList.clone();

        nodeIndex++;

        Node n;


        if (sameClass(dataPartition)) {
            //Leaf Node with Move
            n = new Node(NodeType.LEAF);
            n.setDirection(dataPartition.get(0).getMove());
            n.setIndex(nodeIndex++);
            tree.add(n);

            return n;
        } else if (attributeList.isEmpty() || dataPartition.isEmpty()) {

            if (dataPartition.isEmpty()) {
            }
            //Leaf Node with most occurring move
            n = new Node(NodeType.LEAF);
            n.setDirection(mostOccurringMove(dataPartition));

            n.setIndex(nodeIndex++);
            tree.add(n);

            return n;
        }

        //Node will be an attribute with children for all branch values
        Attribute a = informationGain.attributeSelection(dataPartition, attributeList);
        //System.out.println("Attribute: " + a + "chosen");

        if (nodeIndex == 1) {
            n = new Node(NodeType.ROOT);

        } else {
            n = new Node(NodeType.ATTRIBUTE);
        }

        n.setAttribute(a);
        ArrayList<Node> children = new ArrayList<>();
        n.setChildren(children);
        tree.add(n);

        attributeList.remove(a);

        HashMap<Object, ArrayList<ProcessedDataTuple>> splitDataset = new HashMap<>();

        ArrayList<Object> attributeValues = attributeValuesMap.get(a);

        for (Object value : attributeValues) {
            splitDataset.put(value, new ArrayList<>());
        }


        for (ProcessedDataTuple tuple : dataPartition) {

            Object value = tuple.getAttributeValue(a);

//            if(!splitDataset.containsKey(value)) {
//                splitDataset.put(value, new ArrayList<ProcessedDataTuple>());
//            }

            splitDataset.get(value).add(tuple);

        }

        System.out.println(splitDataset.keySet());

        for (Map.Entry<Object, ArrayList<ProcessedDataTuple>> entry : splitDataset.entrySet()) {

            if (entry.getValue().isEmpty()) {
                Node tempNode = new Node(NodeType.LEAF);
                tempNode.setDirection(mostOccurringMove(dataPartition));

                children.add(tempNode);

            } else {
                children.add(recursion(entry.getValue(), attributeList));

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
            return Constants.MOVE.NEUTRAL;
        }

    }
}
