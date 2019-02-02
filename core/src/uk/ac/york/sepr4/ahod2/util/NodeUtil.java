package uk.ac.york.sepr4.ahod2.util;

import com.badlogic.gdx.Gdx;
import uk.ac.york.sepr4.ahod2.node.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NodeUtil {

    private static Integer triSplitChance = 5, splitChance = 40, singleChance = 25, mergeChance = 30;
    private static Integer maxRowWidth = 4, minRowWidth = 2;

    //TODO: Add some other measures - minMergeDepth (dont merge on first or second level?)

    public static List<Node> generateNodeMap(Integer depth) {
        Random random = new Random();
        List<Node> nodes = new ArrayList();

        //between 1 and 3 start locations
        Integer noNodes = random.nextInt(2) + 1;
        for (int i = 0; i < noNodes; i++) {
            nodes.add(new Node(i, 0, i));
        }
        List<Node> prevNodes = new ArrayList<>(nodes);
        for (int i = 1; i <= depth; i++) {
            List<Node> tempNodes = new ArrayList<>();
            List<Node> mergeNodes = new ArrayList<>();
            //generate between 3-5 encounters per row
            for (Node node : prevNodes) {
                NodeRowAction action;
                if (tempNodes.size() >= maxRowWidth) {
                    action = NodeRowAction.MERGE;
                } else {
                    action = randomRowAction();
                }

                switch (action) {
                    case TRISPLIT:
                        for (int j = 0; j < 3; j++) {
                            Node cNode = new Node(nodes.size() + tempNodes.size(), i, tempNodes.size());
                            tempNodes.add(cNode);
                            node.addConnectedNode(cNode);
                        }
                        break;
                    case MERGE:
                        mergeNodes.add(node);
                        break;
                    case SINGLE:
                        Node cNode = new Node(nodes.size() + tempNodes.size(), i, tempNodes.size());
                        tempNodes.add(cNode);
                        node.addConnectedNode(cNode);
                        break;
                    case SPLIT:
                        for (int j = 0; j < 2; j++) {
                            Node dNode = new Node(nodes.size() + tempNodes.size(), i, tempNodes.size());
                            tempNodes.add(dNode);
                            node.addConnectedNode(dNode);
                        }
                        break;
                }
                if (tempNodes.size() > 0) {
                    //want to choose the most similar column number
                    mergeNodes.forEach(node1 -> {
                        if (tempNodes.size()-1 < node1.getCol()) {
                            node1.addConnectedNode(tempNodes.get(tempNodes.size() - 1));
                        } else {
                            node1.addConnectedNode(tempNodes.get(node1.getCol()));
                        }
                    });
                }
            }
            if (tempNodes.size() <= minRowWidth) {
                //all tried to merge?
                Node newNode =new Node(nodes.size() + tempNodes.size(), i, tempNodes.size());
                tempNodes.add(newNode);
                prevNodes.forEach(node1 -> node1.addConnectedNode(newNode));
            }
            nodes.addAll(tempNodes);
            prevNodes = tempNodes;

            //last iteration
            if(i == depth) {
                Node finalNode = new Node(nodes.size() + tempNodes.size(), i+1, 0);
                nodes.add(finalNode);
                prevNodes.forEach(node1 -> node1.addConnectedNode(finalNode));
            }
        }
        Gdx.app.debug("NodeUtil", "Generated NodeMap with " +nodes.size());
        return nodes;
    }

    private static NodeRowAction randomRowAction() {
        List<NodeRowAction> nodeRowActions = new ArrayList<>();
        for(int i = 0; i < triSplitChance; i++) {
            nodeRowActions.add(NodeRowAction.TRISPLIT);
        }
        for(int i = 0; i < mergeChance; i++) {
            nodeRowActions.add(NodeRowAction.MERGE);
        }
        for(int i = 0; i < singleChance; i++) {
            nodeRowActions.add(NodeRowAction.SINGLE);
        }
        for(int i = 0; i < splitChance; i++) {
            nodeRowActions.add(NodeRowAction.SPLIT);
        }
        Random random = new Random();

        return nodeRowActions.get(random.nextInt(nodeRowActions.size()-1));
    }

}

enum NodeRowAction {
    SPLIT, TRISPLIT, SINGLE, MERGE
}
