package uk.ac.york.sepr4.ahod2.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import uk.ac.york.sepr4.ahod2.GameInstance;
import uk.ac.york.sepr4.ahod2.io.FileManager;
import uk.ac.york.sepr4.ahod2.io.StyleManager;

public class ShipViewScreen extends AHODScreen {

    private GameInstance gameInstance;

    private Table table = new Table();

    public ShipViewScreen(GameInstance gameInstance) {
        super(new Stage(new ScreenViewport()), FileManager.menuScreenBG);

        this.gameInstance = gameInstance;

        createTable();
    }

    private void createTable() {

        table.top();
        table.setFillParent(true);

        TextButton exitButton = new TextButton("Exit", StyleManager.generateTBStyle(30, Color.RED, Color.GRAY));
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ev, float x, float y) {
                gameInstance.fadeSwitchScreen(gameInstance.getSailScreen());
            }
        });

        table.add(exitButton);


        getStage().addActor(table);
    }

    private void update() {

    }

    @Override
    public void renderInner(float delta) {
        update();
    }
}
