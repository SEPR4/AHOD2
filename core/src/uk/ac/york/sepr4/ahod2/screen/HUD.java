package uk.ac.york.sepr4.ahod2.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import lombok.Getter;
import uk.ac.york.sepr4.ahod2.GameInstance;
import uk.ac.york.sepr4.ahod2.io.FileManager;
import uk.ac.york.sepr4.ahod2.io.FontManager;
import uk.ac.york.sepr4.ahod2.object.entity.Player;

public class HUD {

    @Getter
    private Stage hudStage;

    private GameInstance gameInstance;

    private Table hudTable;

    private Label turnLabel, gameStageLabel, scoreLabel,
            turnValueLabel, goldValueLabel, supplyValueLabel, scoreValueLabel;

    public HUD(GameInstance gameInstance) {
        this.gameInstance = gameInstance;

        // Local widths and heights.
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        hudStage = new Stage(new FitViewport(w, h, new OrthographicCamera()));

        createTable();
    }

    public void update() {
        Player player = gameInstance.getPlayer();
        goldValueLabel.setText(player.getGold());
        supplyValueLabel.setText(player.getSupplies());
        scoreValueLabel.setText(player.getScore());

        gameStageLabel.setText(gameInstance.getGameStage().getStageText().toUpperCase());

        hudStage.act();
        hudStage.draw();
    }

    private void createTable() {

        hudTable = new Table();
        hudTable.top();
        hudTable.setFillParent(true);

        Table messageTable = new Table();
        messageTable.setFillParent(true);
        messageTable.bottom();

        //all labels follow this basic layout
        turnLabel = new Label("Turn", new Label.LabelStyle(new BitmapFont(), Color.RED));
        turnValueLabel = new Label("0", new Label.LabelStyle(new BitmapFont(), Color.RED));

        Image goldImage = new Image(FileManager.hudGold);
        goldValueLabel = new Label("0", new Label.LabelStyle(new BitmapFont(), Color.GOLD));

        Image supplyImage = new Image(FileManager.hudSupplies);
        supplyValueLabel = new Label("0", new Label.LabelStyle(new BitmapFont(), Color.GREEN));

        scoreLabel = new Label("Score", new Label.LabelStyle(new BitmapFont(), Color.MAGENTA));
        scoreValueLabel = new Label("0", new Label.LabelStyle(new BitmapFont(), Color.MAGENTA));

        gameStageLabel = new Label("", new Label.LabelStyle(FontManager.generatePirateFont(30, Color.WHITE), Color.CORAL));

        hudTable.add(turnLabel).expandX().padTop(5);
        hudTable.add(goldImage).expandX().padTop(5);
        hudTable.add(supplyImage).expandX().padTop(5);
        hudTable.add(scoreLabel).expandX().padTop(5);
        hudTable.row();
        hudTable.add(turnValueLabel).expandX();
        hudTable.add(goldValueLabel).expandX();
        hudTable.add(supplyValueLabel).expandX();
        hudTable.add(scoreValueLabel).expandX();
        hudTable.row();

        messageTable.add(gameStageLabel).expandX().padBottom(5);

        hudStage.addActor(hudTable);
        hudStage.addActor(messageTable);

    }

}
