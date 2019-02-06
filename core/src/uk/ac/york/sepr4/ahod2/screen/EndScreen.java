package uk.ac.york.sepr4.ahod2.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import uk.ac.york.sepr4.ahod2.io.FileManager;
import uk.ac.york.sepr4.ahod2.io.StyleManager;

public class EndScreen extends AHODScreen {

    private boolean win;

    public EndScreen(boolean win) {
        super(new Stage(new ScreenViewport()), FileManager.menuScreenBG);

        this.win = win;
    }

    @Override
    public void renderInner(float delta) {
        if(win) {
            getStage().addActor(new Label("You Won!", StyleManager.generateLabelStyle(50, Color.GOLD)));
        } else {
            getStage().addActor(new Label("You Lost!", StyleManager.generateLabelStyle(50, Color.RED)));

        }
    }
}
