package uk.ac.york.sepr4.ahod2.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import lombok.Data;
import uk.ac.york.sepr4.ahod2.GameInstance;
import uk.ac.york.sepr4.ahod2.io.FileManager;
import com.badlogic.gdx.math.Rectangle;

import java.lang.reflect.Array;

@Data
public abstract class AHODScreen implements Screen {

    private Stage stage;
    private Texture background;
    private InputMultiplexer inputMultiplexer = new InputMultiplexer();

    private ShapeRenderer shapeRenderer;

    private int waveXcoords[];
    private int waveYcoords[];

    private boolean backgroundAffects;


    private boolean enableMessageHUD = false, enableStatsHUD = false;

    private GameInstance gameInstance;

    private boolean fading = false;
    private float fade = 0;

    public AHODScreen(Stage stage, Texture background, boolean backgroundAffects) {
        this.stage = stage;
        this.background = background;
        this.backgroundAffects = backgroundAffects;


        int newX = 0;
        int newY = 0;

        if (backgroundAffects) {
            this.waveXcoords = new int[100];
            this.waveYcoords = new int[100];
            for (int i = 0; i < 100; i++) {
//                boolean collided = true;
//                while (collided) {
//                    int counter = 0;
                    newX = 0 + (int) (Math.random() * ((stage.getWidth() - 0) + 1));
                    newY = 0 + (int) (Math.random() * ((stage.getHeight() - 0) + 1));
//                    for (int j = 0; j < i; j++) {
//                        if (new Rectangle(newX, newY, 100, 100).contains(new Rectangle(this.waveXcoords[j], this.waveYcoords[j], 100, 100))) {
//                            counter++;
//                        }
//                    }
//                    if (counter == 0) {
//                        collided = false;
//                    }
//                }
                this.waveXcoords[i] = newX;
                this.waveYcoords[i] = newY;
            }
        }

        shapeRenderer = new ShapeRenderer();

        inputMultiplexer.addProcessor(stage);

    }

    public void setMessageHUD(GameInstance gameInstance) {
        this.gameInstance = gameInstance;
        enableMessageHUD = true;
        inputMultiplexer.addProcessor(GameInstance.INSTANCE.getMessageHUD().getHudStage());
    }

    public void setStatsHUD(GameInstance gameInstance) {
        this.gameInstance = gameInstance;
        enableStatsHUD = true;
    }

    @Override
    public void render(float delta) {
        // clear the screen ready for next set of images to be drawn
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        drawBackground();
        renderInner(delta);
        if (isFading()) {
            applyFadeOverlay();
        }
        stage.act(delta);
        stage.draw();
        if (enableMessageHUD) {
            gameInstance.getMessageHUD().update(delta);
        }
        if (enableStatsHUD) {
            gameInstance.getStatsHud().update();
        }
    }

    public abstract void renderInner(float delta);

    public Vector2 getCameraLowerBound() {
        Vector2 pos = new Vector2(stage.getCamera().position.x, stage.getCamera().position.y);
        pos.add(-stage.getWidth() / 2, -stage.getHeight() / 2);
        return pos;
    }

    public void applyFadeOverlay() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.setProjectionMatrix(stage.getBatch().getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(0, 0, 0, fade));
        shapeRenderer.rect(0, getCameraLowerBound().y, stage.getWidth(), stage.getHeight());
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

    }

    private void drawBackground() {
        //sets background texture
        getBatch().begin();
        Texture texture = background;
        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        getBatch().draw(texture, 0, 0, stage.getWidth(), stage.getHeight());
        getBatch().end();

        if (backgroundAffects) {
            getBatch().begin();
            Texture compass = FileManager.compass;
            compass.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
            getBatch().draw(compass, 6 * stage.getWidth() / 8, stage.getHeight() / 16, 300, 300);
            getBatch().end();

            for (int i = 1; i < this.waveXcoords.length + 1; i++) {
                Gdx.app.log("", this.waveXcoords.length+"");
                getBatch().begin();
                Texture wave = FileManager.wave((i % 2) + 1);
                wave.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
                getBatch().draw(wave, this.waveXcoords[i - 1], this.waveYcoords[i - 1], FileManager.wave((i % 2) + 1).getWidth() / 3, FileManager.wave((i % 2) + 1).getHeight() / 3);
                getBatch().end();

                getBatch().begin();
                Texture wave2 = FileManager.wave((i % 2) + 1);
                wave2.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
                getBatch().draw(wave2, this.waveXcoords[i - 1], this.waveYcoords[i - 1] + stage.getHeight() / 2, FileManager.wave((i % 2) + 1).getWidth() / 3, FileManager.wave((i % 2) + 1).getHeight() / 3);
                getBatch().end();
            }
        }
    }

    public OrthographicCamera getOrthographicCamera() {
        return (OrthographicCamera) stage.getViewport().getCamera();
    }

    public Batch getBatch() {
        return stage.getBatch();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void resize(int width, int height) {
        //resizing causes errors with sailscreen
    }

    @Override
    public void pause() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void resume() {
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
