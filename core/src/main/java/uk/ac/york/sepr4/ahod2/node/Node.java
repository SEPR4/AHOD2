package uk.ac.york.sepr4.ahod2.node;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import lombok.Data;
import uk.ac.york.sepr4.ahod2.GameInstance;
import uk.ac.york.sepr4.ahod2.io.FileManager;
import uk.ac.york.sepr4.ahod2.object.encounter.Encounter;
import uk.ac.york.sepr4.ahod2.screen.EncounterScreen;

import java.util.ArrayList;
import java.util.List;

@Data
public class Node {

    private Integer id, row, col;
    private List<Node> connected = new ArrayList<>();
    private Drawable texture = new TextureRegionDrawable(new TextureRegion(FileManager.randEncounterIcon));

    public Node(Integer id, Integer row, Integer col) {
        this.id = id;
        this.row = row;
        this.col = col;
    }

    public void action(GameInstance gameInstance){
        Encounter encounter = gameInstance.getEncounterManager().generateEncounter();
        gameInstance.fadeSwitchScreen(new EncounterScreen(gameInstance, encounter));
    }

    public void addConnectedNode(Node node) {
        connected.add(node);
    }


}
