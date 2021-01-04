package ID3.models;

public class Branch {
    private Node parent;
    private Node child;
    private Object value;

    public Branch(Node parent, Node child, Object value) {
        this.parent = parent;
        this.child = child;
        this.value = value;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Node getChild() {
        return child;
    }

    public void setChild(Node child) {
        this.child = child;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
