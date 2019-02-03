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
import uk.ac.york.sepr4.ahod2.object.GameLevel;

public class BattleScreen implements Screen {

    private GameInstance gameInstance;
    private Integer difficulty, gold, supplies;

    private SpriteBatch batch = new SpriteBatch();
    private Stage battleStage;

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
        this.gameInstance = gameInstance;
        this.difficulty = difficulty;
        this.gold = gold;
        this.supplies = supplies;

        this.battleStage = new Stage(new ScreenViewport(), batch);
    }

    @Override
    public void render(float delta) {
        // clear the screen ready for next set of images to be drawn
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        drawBackground();

        battleStage.act();
        battleStage.draw();
    }

    private void drawBackground() {
        //sets background texture
        batch.begin();
        Texture texture = FileManager.encounterScreenBG;
        TextureRegion textureRegion = new TextureRegion(texture);
        textureRegion.setRegion(0,0,texture.getWidth(),texture.getHeight());
        batch.draw(texture,0,0);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
        Gdx.input.setInputProcessor(battleStage);
    }
    @Override
    public void show() {
        Gdx.input.setInputProcessor(battleStage);
    }
    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }
    @Override
    public void dispose() {
        battleStage.dispose();
    }
}
