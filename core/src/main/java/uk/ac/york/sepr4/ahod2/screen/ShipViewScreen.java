package uk.ac.york.sepr4.ahod2.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import lombok.Setter;
import uk.ac.york.sepr4.ahod2.GameInstance;
import uk.ac.york.sepr4.ahod2.io.FileManager;
import uk.ac.york.sepr4.ahod2.io.StyleManager;
import uk.ac.york.sepr4.ahod2.object.card.Card;

public class ShipViewScreen extends AHODScreen {

    private GameInstance gameInstance;

    @Setter
    private AHODScreen previousScreen;

    private Table table = new Table(), cardTable = new Table();

    private Label levelValueLabel;

    private final Integer cardsPerRow = 8;

    public ShipViewScreen(GameInstance gameInstance) {
        super(new Stage(new ScreenViewport()), FileManager.menuScreenBG);

        this.gameInstance = gameInstance;

        createTable();
    }


    private void createTable() {

        table.top();
        table.setFillParent(true);

        cardTable.top();
        cardTable.setFillParent(true);
        cardTable.padTop(Value.percentHeight(0.05f, cardTable));

        levelValueLabel = new Label("Level: ", StyleManager.generateLabelStyle(30, Color.GREEN));

        TextButton exitButton = new TextButton("Exit", StyleManager.generateTBStyle(30, Color.RED, Color.GRAY));
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ev, float x, float y) {
                gameInstance.fadeSwitchScreen(previousScreen);
            }
        });

        table.add(levelValueLabel)
                .expandX()
                .height(Value.percentHeight(0.05f, table));
        table.add(exitButton)
                .expandX()
                .height(Value.percentHeight(0.05f, table));

        getStage().addActor(table);
        getStage().addActor(cardTable);
    }

    private void update() {
        levelValueLabel.setText("Level: "+gameInstance.getPlayer().getLevel().getId());

        cardTable.clear();

        Integer cardCount = 0;
        for(Card card: gameInstance.getCardManager().getFullDeck(gameInstance.getPlayer().getShip())) {
            cardCount++;
            Image image = new Image(card.getTexture());
            image.setScaling(Scaling.fit);
            cardTable.add(image)
                    .expandX()
                    .width(Value.percentWidth(1f/cardsPerRow, cardTable));
            if(cardCount == cardsPerRow) {
                cardTable.row();
                cardCount = 0;
            }
        }
    }

    @Override
    public void renderInner(float delta) {
        update();
    }
}
