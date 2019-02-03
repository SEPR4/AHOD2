package uk.ac.york.sepr4.ahod2.node;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import lombok.Data;
import uk.ac.york.sepr4.ahod2.io.FileManager;

import java.util.ArrayList;
import java.util.List;

@Data
public class Node {

    private Integer id, row, col;
    private List<Node> connected = new ArrayList<>();
    private Drawable texture = new TextureRegionDrawable(new TextureRegion(FileManager.nodeIcon));

    public Node(Integer id, Integer row, Integer col) {
        this.id = id;
        this.row = row;
        this.col = col;
    }

    public void addConnectedNode(Node node) {
        connected.add(node);
    }


}
