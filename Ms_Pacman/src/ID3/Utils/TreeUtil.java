package ID3.Utils;

import ID3.constants.Attribute;
import ID3.constants.NodeType;
import ID3.informationGain.informationGain;
import ID3.models.Node;
import ID3.models.ProcessedDataTuple;
import dataRecording.DataTuple;
import pacman.game.Constants;

import java.util.ArrayList;

/**
 * A tree is an Arraylist of nodes, this class constucts and train the tree.
 */
public class TreeUtil {
    private ArrayList<Node> tree;
    private ArrayList<Attribute> attributeList;
    int nodeIndex = 0;

    public TreeUtil(ArrayList<Attribute> attributeList) {
        this.attributeList = attributeList;
    }

    public void generateDecisionTree(ArrayList<ProcessedDataTuple> dataPartition){ //Not sure if we use DataTuple as the model for data?
        //calculate information gain, take attribute and call recursively
        Attribute a = informationGain.attributeSelection(dataPartition, attributeList);
        recursion(a, dataPartition);
    }

    private Node recursion(Attribute attribute, ArrayList<ProcessedDataTuple> dataPartition){
        Node n;
        if(sameClass(dataPartition)) {
            //Leaf Node with Move
            n = new Node(NodeType.LEAF);
            n.setDirection(dataPartition.get(0).getMove());
            n.setIndex(nodeIndex++);
            return n;
        } else if(attributeList.isEmpty()) {
            //Leaf Node with most occurring move
            n = new Node(NodeType.LEAF);
            n.setDirection(mostOccurringMove(dataPartition));
            n.setIndex(nodeIndex++);
            return n;
        }

        //Node will be an attribute with children for all branch values
        n = new Node(NodeType.ATTRIBUTE);
        ArrayList<Node> children = new ArrayList<>();

        return n;

        //calculate information gain, take attribute and call recursively
        Attribute a = informationGain.attributeSelection(dataPartition, attributeList);

    }

    /**
     * Helper Method
     */
    private boolean sameClass(ArrayList<ProcessedDataTuple> listToCheck){
        Constants.MOVE move = listToCheck.get(0).getMove();    //get the first items attribute to compare if rest is same
        for(int i = 0; i<listToCheck.size(); i++){
            if(move!=listToCheck.get(i).getMove())
                return false;
        }
        return true;
    }

    private Constants.MOVE mostOccurringMove(ArrayList<ProcessedDataTuple> tuples){
        int up = 0, down = 0, left = 0, right = 0;
        for (ProcessedDataTuple pdt : tuples){
            if(pdt.getMove().equals(Constants.MOVE.UP))
                up ++;
            else if(pdt.getMove().equals(Constants.MOVE.DOWN))
                down ++;
            else if(pdt.getMove().equals(Constants.MOVE.LEFT))
                left ++;
            else if(pdt.getMove().equals(Constants.MOVE.RIGHT))
                right ++;
        }
        if(up>down && up>left && up>right)
            return Constants.MOVE.UP;
        else if(down > up && down>left && down>right)
            return Constants.MOVE.DOWN;
        else if(left>up && left>down && left>right)
            return Constants.MOVE.LEFT;
        else if(right>up && right>down && right>left)
            return Constants.MOVE.RIGHT;

        return null; //This shouldn't happen
    }


}
