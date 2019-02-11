package uk.ac.york.sepr4.ahod2.util;

import org.junit.Assert;
import org.junit.Test;
import uk.ac.york.sepr4.ahod2.node.Node;

import java.util.List;

import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void generateNodeMapTest() {

        Integer targetDepth = 10, actualDepth = 0;
        List<Node> nodeList = NodeUtil.generateNodeMap(10);

        for(Node node: nodeList) {
            if(node.getRow()> actualDepth) {
                actualDepth = node.getRow();
            }
        }
        Assert.assertEquals(targetDepth, actualDepth);
    }
}