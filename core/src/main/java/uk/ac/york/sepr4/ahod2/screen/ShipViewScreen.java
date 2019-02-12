package uk.ac.york.sepr4.ahod2.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import lombok.Setter;
import uk.ac.york.sepr4.ahod2.GameInstance;
import uk.ac.york.sepr4.ahod2.io.FileManager;
import uk.ac.york.sepr4.ahod2.io.StyleManager;

public class ShipViewScreen extends AHODScreen {

    private GameInstance gameInstance;

    @Setter
    private AHODScreen previousScreen;

    private Table table = new Table();

    private Label manaValueLabel;

    public ShipViewScreen(GameInstance gameInstance) {
        super(new Stage(new ScreenViewport()), FileManager.menuScreenBG, false);

        this.gameInstance = gameInstance;

        createTable();
    }


    private void createTable() {

        table.top();
        table.setFillParent(true);

        manaValueLabel = new Label("Mana: ", StyleManager.generateLabelStyle(30, Color.CYAN));

        TextButton exitButton = new TextButton("Exit", StyleManager.generateTBStyle(30, Color.RED, Color.GRAY));
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ev, float x, float y) {
                gameInstance.fadeSwitchScreen(previousScreen);
            }
        });

        table.add(manaValueLabel);
        table.row();
        table.add(exitButton);


        getStage().addActor(table);
    }

    private void update() {
        manaValueLabel.setText("Mana: "+gameInstance.getPlayer().getShip().getMaxMana());
    }

    @Override
    public void renderInner(float delta) {
        update();
    }
}
