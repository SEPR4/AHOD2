package uk.ac.york.sepr4.ahod2.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import uk.ac.york.sepr4.ahod2.GameInstance;
import uk.ac.york.sepr4.ahod2.io.FileManager;
import uk.ac.york.sepr4.ahod2.screen.sail.AHODScreen;

public class BattleScreen extends AHODScreen {

    private GameInstance gameInstance;
    private Integer difficulty, gold, supplies;

    /***
     * Used by generic battle encounters
     * @param gameInstance
     */
    public BattleScreen(GameInstance gameInstance) {
        this(gameInstance,
                gameInstance.getCurrentLevel().getDifficulty(),
                gameInstance.getCurrentLevel().getBattleGold(),
                gameInstance.getCurrentLevel().getBattleSupplies());
    }

    /***
     * Used when battle initiated through encounter (by choice)
     * @param gameInstance
     * @param difficulty
     * @param gold
     * @param supplies
     */
    public BattleScreen(GameInstance gameInstance, Integer difficulty, Integer gold, Integer supplies) {
        super(new Stage(new ScreenViewport(), new SpriteBatch()), FileManager.battleScreenBG);
        this.gameInstance = gameInstance;
        this.difficulty = difficulty;
        this.gold = gold;
        this.supplies = supplies;
    }

    @Override
    public void renderInner(float delta) {

    }
}
