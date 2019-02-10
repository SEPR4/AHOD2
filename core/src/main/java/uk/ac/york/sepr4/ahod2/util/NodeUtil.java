package uk.ac.york.sepr4.ahod2.util;

import com.badlogic.gdx.Gdx;
import uk.ac.york.sepr4.ahod2.node.*;
import uk.ac.york.sepr4.ahod2.object.GameLevel;
import uk.ac.york.sepr4.ahod2.object.building.Department;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NodeUtil {

    private static Integer triSplitChance = 5, splitChance = 40, singleChance = 25, mergeChance = 30;
    private static Integer maxRowWidth = 4, minRowWidth = 2;
    private static Double battleNodeChance = 0.4;

    /***
     * Puts NodeTypes into generated NodeMap
     * @param gameLevel
     * @return
     */
    public static List<Node> generateRandomNodeMap(GameLevel gameLevel) {
        List<Node> nodes = generateNodeMap(gameLevel.getDepth());
        //replace college node into final node in list

        List<Node> finalNodes = new ArrayList<>();
        Random random = new Random();

        for(Node node: nodes) {
            //if not first or last row
            if(node.getRow() == 0){
                //first row
                finalNodes.add(new StartNode(node));
            } else if(node.getRow() == gameLevel.getDepth()+1) {
                //last row
                finalNodes.add(new CollegeNode(node, gameLevel.getCollege()));
            } else {
                //middle rows
                if(random.nextDouble()<=battleNodeChance) {
                    //replace with battle node
                    finalNodes.add(new BattleNode(node));
                } else {
                    finalNodes.add(node);
                }
            }
        }
        //insert departments retroactively
        for(Department department : gameLevel.getDepartments()) {
            boolean inserted = false;
            while(!inserted) {
                Integer loc = random.nextInt(finalNodes.size() - 1);
                Node node = finalNodes.get(loc);
                if (!(node instanceof CollegeNode || node instanceof StartNode || node instanceof DepartmentNode)) {
                    //acceptable position to set dept
                    finalNodes.set(loc, new DepartmentNode(node, department));
                    inserted = true;
                }
            }
        }

        return finalNodes;
    }

    //TODO: Add some other measures - minMergeDepth (dont merge on first or second level?)

    private static List<Node> generateNodeMap(Integer depth) {
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
            if (tempNodes.size() < minRowWidth) {
                //all tried to merge?
                Node newNode =new Node(nodes.size() + tempNodes.size(), i, tempNodes.size());
                tempNodes.add(newNode);
                prevNodes.forEach(node1 -> node1.addConnectedNode(newNode));
            }
            nodes.addAll(tempNodes);
            prevNodes = tempNodes;

            //last iteration
            if(i == depth) {
                Node finalNode = new Node(nodes.size(), i+1, 0);
                nodes.add(finalNode);
                prevNodes.forEach(node1 -> node1.addConnectedNode(finalNode));
            }
        }
        Gdx.app.debug("NodeUtil", "Generated NodeMap with " +nodes.size() + " nodes!");
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
