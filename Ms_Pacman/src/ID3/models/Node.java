package ID3.models;

import ID3.constants.Attribute;
import ID3.constants.NodeType;
import dataRecording.DataTuple;
import pacman.game.Constants;

import java.util.ArrayList;

/**
 * The node will essentially be the whole Tree, containing values (attributes if non-leaf or Classes if leaf)
 * A branch of the ID3 tree has the attribute value, which is essentially stored in the Node, so no need for branch
 * models. Each Node will have an array list of children and a Node as a parent (if parent = null => root)
 *
 */
public class Node {
    ArrayList<Branch> branches;
    //Node parent;
    NodeType type;
    Constants.MOVE direction;
    Attribute attribute;
    DataTuple.DiscreteTag branchValue;       //the actual value for the branch to the node.
    //Boolean root = false;
    int index;

    public Node(NodeType type) {
//        if(parent == null)
//            root = true;
//        else
//            this.parent = parent;
        this.type = type;
    }

    public ArrayList<Branch> getBranches() {
        return branches;
    }

    public void setBranches(ArrayList<Branch> branches) {
        this.branches = branches;
    }

    public NodeType getType() {
        return type;
    }

    public void setType(NodeType type) {
        this.type = type;
    }

    public Constants.MOVE getDirection() {
        return direction;
    }

    public void setDirection(Constants.MOVE direction) {
        this.direction = direction;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }

    public DataTuple.DiscreteTag getBranchValue() {
        return branchValue;
    }

//    public boolean isRoot(){
//        return root;
//    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
