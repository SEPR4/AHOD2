package uk.ac.york.sepr4.ahod2.node;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Node {

    private Integer id, row, col;
    private List<Node> connected = new ArrayList<>();

    public Node(Integer id, Integer row, Integer col) {
        this.id = id;
        this.row = row;
        this.col = col;
    }

    public void addConnectedNode(Node node) {
        connected.add(node);
    }


}
