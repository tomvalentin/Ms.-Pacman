package ID3.models;

import ID3.constants.Attribute;
import ID3.constants.Direction;
import ID3.constants.NodeType;
import dataRecording.DataTuple;

import java.util.ArrayList;

/**
 * The node will essentially be the whole Tree, containing values (attributes if non-leaf or Classes if leaf)
 * A branch of the ID3 tree has the attribute value, which is essentially stored in the Node, so no need for branch
 * models. Each Node will have an array list of children and a Node as a parent (if parent = null => root)
 *
 */
public class Node {
    ArrayList<Node> children;
    Node parent;
    NodeType type;
    Direction direction;
    Attribute attribute;
    DataTuple.DiscreteTag branchValue;       //the actual value for the branch to the node.
    Boolean root = false;

    public Node(Node parent, NodeType type) {
        if(parent == null)
            root = true;
        else
            this.parent = parent;

        this.type = type;
    }

    public ArrayList<Node> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<Node> children) {
        this.children = children;
    }

    public NodeType getType() {
        return type;
    }

    public void setType(NodeType type) {
        this.type = type;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
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

    public boolean isRoot(){
        return root;
    }
}
