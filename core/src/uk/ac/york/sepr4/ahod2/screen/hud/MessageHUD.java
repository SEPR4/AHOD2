package uk.ac.york.sepr4.ahod2.screen.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import lombok.Getter;
import uk.ac.york.sepr4.ahod2.GameInstance;
import uk.ac.york.sepr4.ahod2.io.FileManager;
import uk.ac.york.sepr4.ahod2.io.StyleManager;
import uk.ac.york.sepr4.ahod2.object.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageHUD {

    @Getter
    private Stage hudStage;

    private GameInstance gameInstance;

    private Label gameStageLabel;

    private HashMap<Label, Float> tempMessages = new HashMap<>();

    private final Float tempMessageTime = 4f;

    public MessageHUD(GameInstance gameInstance) {
        this.gameInstance = gameInstance;

        // Local widths and heights.
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        hudStage = new Stage(new FitViewport(w, h, new OrthographicCamera()));

        createTable();
    }

    public void addGoldMessage(Integer gold) {
        Label label = new Label("+ "+gold+" GOLD", StyleManager.generateLabelStyle(30, Color.GREEN));
        tempMessages.put(label, 0f);
    }

    public void update(float delta) {
        gameStageLabel.setText(gameInstance.getGameStage().getStageText().toUpperCase());

        List<Label> toRemove = new ArrayList<>();
        for(Label label : tempMessages.keySet()) {
            Float time = (tempMessages.get(label));
            if(time + delta > tempMessageTime) {
                toRemove.add(label);
            } else {
                tempMessages.replace(label, time+delta);
                if(!hudStage.getActors().contains(label, false)){
                    hudStage.addActor(label);
                }
                label.setPosition(5, 5 + (Gdx.graphics.getHeight()/2)*(time/tempMessageTime));

            }
        }


        for(Label remove : toRemove) {
            tempMessages.remove(remove);
            hudStage.getActors().removeValue(remove, false);
        }

        hudStage.act();
        hudStage.draw();
    }

    private void createTable() {

        Table messageTable = new Table();
        messageTable.setFillParent(true);
        messageTable.bottom();


        gameStageLabel = new Label("", new Label.LabelStyle(StyleManager.generatePirateFont(40, Color.WHITE), Color.CORAL));
        ImageButton imageButton = new ImageButton(new TextureRegionDrawable(FileManager.hudShipView));

        imageButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ev, float x, float y) {
                gameInstance.fadeSwitchScreen(gameInstance.getShipViewScreen());
            }
        });

        messageTable.add(gameStageLabel)
                .expandX()
                .padBottom(Value.percentWidth(0.02f, messageTable))
                .padLeft(Value.percentWidth(0.02f, messageTable))
                .left();

        messageTable.add(imageButton)
                .expandX()
                .padBottom(Value.percentWidth(0.02f, messageTable))
                .padRight(Value.percentWidth(0.02f, messageTable))
                .right()
                .bottom()
                .width(Value.percentWidth(0.04f, messageTable))
                .height(Value.percentWidth(0.04f, messageTable));

        hudStage.addActor(messageTable);

    }

}
