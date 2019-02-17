package uk.ac.york.sepr4.ahod2.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import uk.ac.york.sepr4.ahod2.GameInstance;
import uk.ac.york.sepr4.ahod2.io.FileManager;
import uk.ac.york.sepr4.ahod2.object.card.Card;

import java.util.List;

public class CardSelectionScreen extends AHODScreen {

    private List<Card> selection;
    private GameInstance gameInstance;

    private boolean selected = false;

    public CardSelectionScreen(GameInstance gameInstance, List<Card> selection) {
        super(new Stage(new ScreenViewport()), FileManager.menuScreenBG);

        this.gameInstance = gameInstance;
        this.selection = selection;

        setStatsHUD(gameInstance);

        createSelectionTable();
    }

    private void createSelectionTable() {
        Table selectionTable = new Table();
        selectionTable.setFillParent(true);
        selectionTable.top();
        selectionTable.debug();

        for(Card card: selection) {
            Gdx.app.debug("CardSelScreen", card.getName());
            ImageButton imageButton = new ImageButton(new TextureRegionDrawable(card.getTexture()));
            imageButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent ev, float x, float y) {
                    if(!selected) {
                        selectCard(card);
                    }
                }
            });
            selectionTable.add(imageButton).expandX().expandY()
                    .align(Align.center);
        }


        getStage().addActor(selectionTable);

    }

    private void selectCard(Card card) {
        selected = true;
        gameInstance.getPlayer().getShip().addCard(card);
        gameInstance.fadeSwitchScreen(gameInstance.getSailScreen(), true);
    }

    @Override
    public void renderInner(float delta) {

    }
}
