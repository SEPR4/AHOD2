package uk.ac.york.sepr4.ahod2.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import uk.ac.york.sepr4.ahod2.GameInstance;
import uk.ac.york.sepr4.ahod2.io.FileManager;
import uk.ac.york.sepr4.ahod2.io.StyleManager;

public class EndScreen extends AHODScreen {

    private GameInstance gameInstance;
    private boolean win;

    public EndScreen(GameInstance gameInstance, boolean win) {
        super(new Stage(new ScreenViewport()), FileManager.menuScreenBG, false);

        this.gameInstance = gameInstance;
        this.win = win;

        setupScreen();
    }

    private void setupScreen() {
        if(win) {
            getStage().addActor(new Label("You Won!", StyleManager.generateLabelStyle(50, Color.GOLD)));
        } else {
            getStage().addActor(new Label("You Lost!", StyleManager.generateLabelStyle(50, Color.RED)));

        }
        TextButton textButton = new TextButton("Exit to Menu!",StyleManager.generateTBStyle(40, Color.RED, Color.GRAY));
        textButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ev, float x, float y) {
                gameInstance.getGame().getMenuScreen().newGame();
                gameInstance.fadeSwitchScreen(gameInstance.getGame().getMenuScreen());
            }
        });
        getStage().addActor(textButton);
    }

    @Override
    public void renderInner(float delta) {

    }
}
