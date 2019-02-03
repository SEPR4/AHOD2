package uk.ac.york.sepr4.ahod2.screen;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import uk.ac.york.sepr4.ahod2.io.FileManager;
import uk.ac.york.sepr4.ahod2.object.encounter.Encounter;

public class EncounterScreen implements Screen {

    private Stage encounterStage;
    private SpriteBatch batch;

    private Encounter encounter;

    public EncounterScreen(Encounter encounter) {
        this.encounter = encounter;

        this.batch = new SpriteBatch();
        this.encounterStage = new Stage(new ScreenViewport(), batch);


        Gdx.input.setInputProcessor(encounterStage);
    }


    @Override
    public void render(float delta) {

        batch.begin();
        drawBackground();
        batch.end();

        encounterStage.act();
        encounterStage.draw();
    }

    private void drawBackground() {
        //sets background texture
        Texture texture = FileManager.encounterScreenBG;
        TextureRegion textureRegion = new TextureRegion(texture);
        textureRegion.setRegion(0,0,texture.getWidth(),texture.getHeight());
        batch.draw(texture,0,0);
    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(encounterStage);
    }
    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }



    @Override
    public void resume() {
        Gdx.input.setInputProcessor(encounterStage);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        encounterStage.dispose();

    }
}
