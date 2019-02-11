package uk.ac.york.sepr4.ahod2.screen.sail;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import lombok.Getter;
import uk.ac.york.sepr4.ahod2.GameInstance;
import uk.ac.york.sepr4.ahod2.io.FileManager;
import uk.ac.york.sepr4.ahod2.io.SailInputProcessor;
import uk.ac.york.sepr4.ahod2.node.Node;
import uk.ac.york.sepr4.ahod2.object.entity.Player;
import uk.ac.york.sepr4.ahod2.screen.AHODScreen;

import java.util.Optional;

public class SailScreen extends AHODScreen {

    @Getter
    private GameInstance gameInstance;

    private SailInputProcessor sailInputProcessor;

    public SailScreen(GameInstance gameInstance) {
        super(new Stage(new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),
                new OrthographicCamera())), FileManager.sailScreenBG);
        this.gameInstance = gameInstance;

        sailInputProcessor = new SailInputProcessor(this);
        getInputMultiplexer().addProcessor(sailInputProcessor);

        gameInstance.getMessageHUD().addStatusMessage("Select Starting Node!");

        setHUD(gameInstance);
    }

    @Override
    public void renderInner(float delta) {
        gameInstance.getStatsHud().update();
        sailInputProcessor.scrollCamera();
        gameInstance.getPlayer().getLevel().getNodeView().update();
    }

    public void nodeClick(Node node) {
        Player player = gameInstance.getPlayer();
        Optional<Node> loc = player.getLocation();
        //node action if player location is unset (starting) or node clicked is above current
        if(loc.isPresent()) {
            if(node.getRow() == loc.get().getRow()+1 || gameInstance.getGame().DEBUG) {
                gameInstance.nodeAction(node);
            } else {
                Gdx.app.debug("SailScreen", "Lower or current position node clicked!");
            }
        } else {
            gameInstance.nodeAction(node);
        }

     }

}
