package uk.ac.york.sepr4.ahod2.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import uk.ac.york.sepr4.ahod2.AHOD2;
import uk.ac.york.sepr4.ahod2.GameInstance;
import uk.ac.york.sepr4.ahod2.io.FileManager;
import uk.ac.york.sepr4.ahod2.io.StyleManager;

public class MenuScreen extends AHODScreen {

    private AHOD2 game;
    private GameInstance gameInstance;

    //TODO: Change background
    public MenuScreen(AHOD2 game) {
        super(new Stage(new ScreenViewport()), FileManager.menuScreenBG);
        this.game = game;

        loadMenu();
    }

    private GameInstance getOrCreateGameInstance() {
        if(gameInstance == null) {
            gameInstance = new GameInstance(game);
        }
        return gameInstance;
    }

    @Override
    public void renderInner(float delta) {

    }

    private void loadMenu() {
        Gdx.app.debug("MenuScreen", "Loading Menu!");
        Table table = new Table();
        table.setFillParent(true);

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = StyleManager.generatePirateFont(50, Color.RED);

        //create buttons
        TextButton newGame = new TextButton("New Game", style);
        TextButton preferences = new TextButton("Preferences", style);
        TextButton exit = new TextButton("Exit", style);

        //add buttons to table
        table.add(newGame).fillX().uniformX();
        table.row().pad(10, 0, 10, 0);
        //table.add(preferences).fillX().uniformX();
        table.row();
        table.add(exit).fillX().uniformX();

        // create button listeners
        exit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        newGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getOrCreateGameInstance().start();
            }
        });

        if(game.DEBUG) {
            table.setDebug(true);
        }
        getStage().addActor(table);
    }
}
