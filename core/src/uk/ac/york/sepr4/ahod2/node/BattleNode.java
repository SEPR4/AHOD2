package uk.ac.york.sepr4.ahod2.node;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import uk.ac.york.sepr4.ahod2.io.FileManager;

public class BattleNode extends Node {

    private Integer difficulty;

    public BattleNode(Node node, Integer difficulty) {
        super(node.getId(), node.getRow(), node.getCol());
        setConnected(node.getConnected());
        this.difficulty = difficulty;
        this.setTexture(new TextureRegionDrawable(new TextureRegion(FileManager.battleNodeIcon)));
    }
}
