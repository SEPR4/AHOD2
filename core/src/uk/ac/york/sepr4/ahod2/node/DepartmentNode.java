package uk.ac.york.sepr4.ahod2.node;

public class DepartmentNode extends Node {
    public DepartmentNode(Node node) {
        super(node.getId(), node.getRow(), node.getCol());
        setConnected(node.getConnected());

    }
}
