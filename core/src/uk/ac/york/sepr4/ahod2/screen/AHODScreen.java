package uk.ac.york.sepr4.ahod2.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import lombok.Getter;

@Getter
public abstract class AHODScreen implements Screen {

    private Stage stage;
    private Texture background;
    private InputMultiplexer inputMultiplexer = new InputMultiplexer();

    public AHODScreen(Stage stage, Texture background) {
        this.stage = stage;
        this.background = background;

        inputMultiplexer.addProcessor(stage);
    }

    @Override
    public void render(float delta) {
        // clear the screen ready for next set of images to be drawn
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        drawBackground();
        renderInner(delta);
        stage.act(delta);
        stage.draw();
    }

    public abstract void renderInner(float delta);

    private void drawBackground() {
        //sets background texture
        getBatch().begin();
        Texture texture = background;
        TextureRegion textureRegion = new TextureRegion(texture);
        textureRegion.setRegion(0,0,texture.getWidth(),texture.getHeight());
        getBatch().draw(texture,0,0);
        getBatch().end();
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
        stage.getViewport().update(width, height, true);
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
