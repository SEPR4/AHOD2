package uk.ac.york.sepr4.ahod2.screen;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import uk.ac.york.sepr4.ahod2.GameInstance;
import uk.ac.york.sepr4.ahod2.io.StyleManager;
import uk.ac.york.sepr4.ahod2.object.ShipFactory;
import uk.ac.york.sepr4.ahod2.object.encounter.Encounter;
import uk.ac.york.sepr4.ahod2.object.encounter.EncounterOption;
import uk.ac.york.sepr4.ahod2.object.entity.Player;

public class EncounterScreen extends AHODScreen {

    private Encounter encounter;
    private GameInstance gameInstance;

    public EncounterScreen(GameInstance gameInstance, Encounter encounter) {
        super(new Stage(new ScreenViewport(), new SpriteBatch()),
                new Texture(Gdx.files.internal("images/screen/encounter/" + encounter.getBackground())));
        this.gameInstance = gameInstance;
        this.encounter = encounter;

        createEncounterInfo();

    }


    @Override
    public void renderInner(float delta) {

    }

    private void createEncounterInfo() {
        Table table1 = new Table();
        table1.setFillParent(true);
        table1.top();

        Label encounterText = new Label(encounter.getText(), StyleManager.generateLabelStyle(60, Color.BLACK));


        table1.add(encounterText).expandX().padTop(Gdx.graphics.getHeight() / 4);

        Table table2 = new Table();
        table2.setFillParent(true);
        table2.top();
        for (EncounterOption encounterOption : encounter.getOptions()) {
            TextButton tB = new TextButton(encounterOption.getText(),
                    StyleManager.generateTBStyle(30, Color.BLACK, Color.GRAY));

            tB.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent ev, float x, float y) {
                    optionClick(encounterOption);
                }
            });

            table2.add(tB).expandX().padTop(Gdx.graphics.getHeight() / 2);
        }

        getStage().addActor(table1);
        getStage().addActor(table2);

    }

    private void optionClick(EncounterOption encounterOption) {
        Gdx.app.debug("EncounterScreen", "Option clicked!");
        if (encounterOption.isBattle()) {
            BattleScreen battleScreen = new BattleScreen(gameInstance,
                    ShipFactory.generateEnemyShip(encounterOption.getDifficulty()),
                    encounterOption.getGold(),
                    encounterOption.getSupplies());
            gameInstance.fadeSwitchScreen(battleScreen);
        } else {
            Player player = gameInstance.getPlayer();
            player.setGold(player.getGold() + encounterOption.getGold());
            player.setSupplies(player.getSupplies() + encounterOption.getSupplies());
            gameInstance.fadeSwitchScreen(gameInstance.getSailScreen());
        }
    }
}
