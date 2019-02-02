package uk.ac.york.sepr4.ahod2.screen.sail;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import lombok.Getter;
import uk.ac.york.sepr4.ahod2.GameInstance;
import uk.ac.york.sepr4.ahod2.io.FileManager;
import uk.ac.york.sepr4.ahod2.io.SailInputProcessor;
import uk.ac.york.sepr4.ahod2.node.Node;
import uk.ac.york.sepr4.ahod2.object.GameStage;
import uk.ac.york.sepr4.ahod2.object.entity.Player;

import java.util.Optional;


public class SailScreen implements Screen {

    @Getter
    private GameInstance gameInstance;

    @Getter
    private OrthographicCamera orthographicCamera;
    @Getter
    private Stage stage;
    @Getter
    private SpriteBatch batch;

    private SailInputProcessor sailInputProcessor;

    private NodeView nodeView;

    private InputMultiplexer inputMultiplexer;

    public SailScreen(GameInstance gameInstance) {
        this.gameInstance = gameInstance;

        // Local widths and heights.
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        // Set up camera.
        orthographicCamera = new OrthographicCamera();
        orthographicCamera.setToOrtho(false, w, h);
        orthographicCamera.update();

        // Set up stages (for entities and HUD).
        StretchViewport viewport = new StretchViewport(w, h, orthographicCamera);
        batch = new SpriteBatch();
        stage = new Stage(viewport, batch);

        inputMultiplexer = new InputMultiplexer();
        sailInputProcessor = new SailInputProcessor(this);
        inputMultiplexer.addProcessor(sailInputProcessor);
        inputMultiplexer.addProcessor(stage);

        Gdx.input.setInputProcessor(inputMultiplexer);

        nodeView = new NodeView(this);

        gameInstance.setGameStage(GameStage.SELECT_START);
    }


    @Override
    public void render(float delta) {
        // clear the screen ready for next set of images to be drawn
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        sailInputProcessor.scrollCamera();

        batch.begin();
        drawBackground();
        batch.end();

        nodeView.update();

        // tell our stage to do actions and draw itself
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
        gameInstance.getHud().update();
    }

    public void nodeClick(Node node) {
        Player player = gameInstance.getPlayer();
        Optional<Node> loc = player.getLocation();
        if(!loc.isPresent()) {
            //must be starting
            if(node.getRow() == 0) {
                player.setLocation(Optional.of(node));
                gameInstance.setGameStage(GameStage.SELECT);
            } else {
                Gdx.app.debug("SailScreen", "Non-starting position clicked!");
            }
        } else {
            if(node.getRow() > loc.get().getRow()) {
                gameInstance.nodeAction(node);
            } else {
                Gdx.app.debug("SailScreen", "Lower or current position node clicked!");
            }
        }
     }


    public Vector2 screenToWorld(Vector2 vector2) {
        Vector3 conv = orthographicCamera.project(new Vector3(vector2.x,vector2.y,0));
        return new Vector2(conv.x, conv.y);
    }




    private void drawBackground() {
        //sets background texture
        Texture texture = FileManager.sailScreenBG;
        TextureRegion textureRegion = new TextureRegion(texture);
        textureRegion.setRegion(0,0,texture.getWidth(),texture.getHeight());
        batch.draw(texture,0,0);
    }




    @Override
    public void show() {
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void resume() {
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
