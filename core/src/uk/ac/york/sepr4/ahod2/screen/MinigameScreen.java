package uk.ac.york.sepr4.ahod2.screen;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import uk.ac.york.sepr4.ahod2.GameInstance;
import uk.ac.york.sepr4.ahod2.io.FileManager;

public class MinigameScreen extends AHODScreen {

    private GameInstance gameInstance;
    private DepartmentScreen departmentScreen;

    public MinigameScreen(GameInstance gameInstance, DepartmentScreen departmentScreen) {
        super(new Stage(new ScreenViewport()), FileManager.menuScreenBG);
        this.gameInstance = gameInstance;
        this.departmentScreen = departmentScreen;
    }

    private void exitScreen() {
        gameInstance.fadeSwitchScreen(departmentScreen);
    }

    @Override
    public void renderInner(float delta) {

    }
}
