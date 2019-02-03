package uk.ac.york.sepr4.ahod2.node;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import uk.ac.york.sepr4.ahod2.GameInstance;
import uk.ac.york.sepr4.ahod2.io.FileManager;
import uk.ac.york.sepr4.ahod2.object.GameStage;

import java.util.Optional;

public class StartNode extends Node {

    public StartNode(Node node) {
        super(node.getId(), node.getRow(), node.getCol());
        setConnected(node.getConnected());
        this.setTexture(new TextureRegionDrawable(new TextureRegion(FileManager.nodeIcon)));
    }

    @Override
    public void action(GameInstance gameInstance) {
        Optional<Node> loc = gameInstance.getPlayer().getLocation();
        if(!loc.isPresent()) {
            gameInstance.getPlayer().setLocation(Optional.of(this));
            gameInstance.setGameStage(GameStage.SELECT);
        }
    }
}
