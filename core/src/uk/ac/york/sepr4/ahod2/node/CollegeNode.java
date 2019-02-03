package uk.ac.york.sepr4.ahod2.node;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import lombok.Getter;
import uk.ac.york.sepr4.ahod2.io.FileManager;
import uk.ac.york.sepr4.ahod2.object.building.College;

@Getter
public class CollegeNode extends Node {

    private College college;

    public CollegeNode(Node node, College college) {
        super(node.getId(), node.getRow(), node.getCol());
        setConnected(node.getConnected());
        this.college = college;
        this.setTexture(new TextureRegionDrawable(new TextureRegion(FileManager.nodeIcon)));
    }
}
