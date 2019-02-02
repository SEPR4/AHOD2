package uk.ac.york.sepr4.ahod2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import lombok.Data;
import uk.ac.york.sepr4.ahod2.node.CollegeNode;
import uk.ac.york.sepr4.ahod2.node.DepartmentNode;
import uk.ac.york.sepr4.ahod2.node.Node;
import uk.ac.york.sepr4.ahod2.object.CardManager;
import uk.ac.york.sepr4.ahod2.object.Encounter;
import uk.ac.york.sepr4.ahod2.object.EncounterManager;
import uk.ac.york.sepr4.ahod2.object.GameStage;
import uk.ac.york.sepr4.ahod2.object.building.BuildingManager;
import uk.ac.york.sepr4.ahod2.object.entity.Player;
import uk.ac.york.sepr4.ahod2.screen.HUD;
import uk.ac.york.sepr4.ahod2.screen.sail.SailScreen;

@Data
public class GameInstance {

    private AHOD2 game;
    public static GameInstance INSTANCE;

    private CardManager cardManager;
    private BuildingManager buildingManager;
    private EncounterManager encounterManager;

    private SailScreen sailScreen;
    private HUD hud;

    Player player = new Player();

    private GameStage gameStage = GameStage.LOADING;

    public GameInstance(AHOD2 ahod2) {
        game = ahod2;
        INSTANCE = this;

        //Initialize Managers
        cardManager = new CardManager();
        buildingManager = new BuildingManager(this);
        encounterManager = new EncounterManager();

        //Initialize Screens and views
        hud = new HUD(this);
        sailScreen = new SailScreen(this);

    }

    public void start() {
        Gdx.app.debug("GameInstance", "Starting Instance");

        game.setScreen(sailScreen);
    }

    public void nodeAction(Node node) {
        //enter college/dept or get/enter encounter
        if(node instanceof CollegeNode) {

        } else if(node instanceof DepartmentNode) {

        } else {
            //normal node
            Encounter encounter = encounterManager.generateEncounter(node);

        }
    }

}
