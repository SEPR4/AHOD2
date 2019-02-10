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

public class MinigameScreen extends AHODScreen {

    private GameInstance gameInstance;
    private DepartmentScreen departmentScreen;

    private Table table = new Table();

    public MinigameScreen(GameInstance gameInstance, DepartmentScreen departmentScreen) {
        super(new Stage(new ScreenViewport()), FileManager.menuScreenBG);
        this.gameInstance = gameInstance;
        this.departmentScreen = departmentScreen;

        createTable();
    }

    private void update() {

    }

    private void createTable() {
        table.top();
        table.setFillParent(true);

        TextButton exitButton = new TextButton("Exit", StyleManager.generateTBStyle(30, Color.RED, Color.GRAY));
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ev, float x, float y) {
                gameInstance.fadeSwitchScreen(departmentScreen);
            }
        });

        table.add(exitButton);


        getStage().addActor(table);
    }

    @Override
    public void renderInner(float delta) {
        update();
    }
}
