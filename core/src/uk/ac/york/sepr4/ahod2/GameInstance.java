package uk.ac.york.sepr4.ahod2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import lombok.Data;
import uk.ac.york.sepr4.ahod2.node.CollegeNode;
import uk.ac.york.sepr4.ahod2.node.DepartmentNode;
import uk.ac.york.sepr4.ahod2.node.Node;
import uk.ac.york.sepr4.ahod2.object.GameLevel;
import uk.ac.york.sepr4.ahod2.object.building.College;
import uk.ac.york.sepr4.ahod2.object.card.CardManager;
import uk.ac.york.sepr4.ahod2.object.encounter.Encounter;
import uk.ac.york.sepr4.ahod2.object.encounter.EncounterManager;
import uk.ac.york.sepr4.ahod2.object.GameStage;
import uk.ac.york.sepr4.ahod2.object.building.BuildingManager;
import uk.ac.york.sepr4.ahod2.object.entity.Player;
import uk.ac.york.sepr4.ahod2.screen.HUD;
import uk.ac.york.sepr4.ahod2.screen.sail.SailScreen;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

@Data
public class GameInstance {

    private AHOD2 game;
    public static GameInstance INSTANCE;

    private CardManager cardManager;
    private BuildingManager buildingManager;
    private EncounterManager encounterManager;

    private SailScreen sailScreen;
    private HUD hud;

    private List<GameLevel> levels = new ArrayList<>();

    private Player player;

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

        loadLevels();

        if(levels.size() > 0) {
            player = new Player(levels.get(0));
        } else {
            Gdx.app.error("GameInstance", "No levels found! Exiting!");
            Gdx.app.exit();
        }

    }

    public void start() {
        Gdx.app.debug("GameInstance", "Starting Instance");

        switchScreen(sailScreen);
    }

    public GameLevel getCurrentLevel() {
        return getPlayer().getLevel();
    }

    public void switchScreen(Screen screen) {
        game.setScreen(screen);
    }

    private void loadLevels() {
        Json json = new Json();
        Array<GameLevel> tempLevels = json.fromJson(Array.class, GameLevel.class, Gdx.files.internal("data/levels.json"));
        for(GameLevel level : tempLevels) {
            if(level.load(this)) {
                levels.add(level);
            }
        }
    }

    public void nodeAction(Node node) {
        //enter college/dept or get/enter encounter
        node.action(this);
        player.setLocation(Optional.of(node));
    }

}
