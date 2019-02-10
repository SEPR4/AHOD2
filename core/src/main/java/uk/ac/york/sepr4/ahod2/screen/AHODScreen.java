package uk.ac.york.sepr4.ahod2.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import lombok.Data;

@Data
public abstract class AHODScreen implements Screen {

    private Stage stage;
    private Texture background;
    private InputMultiplexer inputMultiplexer = new InputMultiplexer();

    private ShapeRenderer shapeRenderer;

    private boolean fading = false;
    private float fade = 0;

    public AHODScreen(Stage stage, Texture background) {
        this.stage = stage;
        this.background = background;

        shapeRenderer = new ShapeRenderer();

        inputMultiplexer.addProcessor(stage);
    }

    @Override
    public void render(float delta) {
        // clear the screen ready for next set of images to be drawn
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        drawBackground();
        renderInner(delta);
        if(isFading()) {
            applyFadeOverlay();
        }
        stage.act(delta);
        stage.draw();
    }

    public abstract void renderInner(float delta);

    public Vector2 getCameraLowerBound() {
        Vector2 pos = new Vector2(stage.getCamera().position.x, stage.getCamera().position.y);
        pos.add(-stage.getWidth()/2, -stage.getHeight()/2);
        return pos;
    }

    public void applyFadeOverlay(){
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.setProjectionMatrix(stage.getBatch().getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(0,0,0, fade));
        shapeRenderer.rect(0,getCameraLowerBound().y, stage.getWidth(), stage.getHeight());
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

    }

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
        stage.getViewport().update(width, height, false);
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
