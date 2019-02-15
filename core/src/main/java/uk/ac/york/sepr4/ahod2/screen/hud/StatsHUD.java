package uk.ac.york.sepr4.ahod2.screen.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import lombok.Getter;
import uk.ac.york.sepr4.ahod2.GameInstance;
import uk.ac.york.sepr4.ahod2.io.StyleManager;
import uk.ac.york.sepr4.ahod2.object.entity.Player;

public class StatsHUD {

    @Getter
    private Stage hudStage;

    private GameInstance gameInstance;

    private Table hudTable;

    private Label healthValueLabel, goldValueLabel, scoreValueLabel;

    public StatsHUD(GameInstance gameInstance) {
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
        healthValueLabel.setText(player.getShip().getHealth()+"/"+player.getShip().getMaxHealth());
        scoreValueLabel.setText(player.getScore());

        hudStage.act();
        hudStage.draw();
    }

    private void createTable() {

        hudTable = new Table();
        hudTable.top();
        hudTable.setFillParent(true);

        //all labels follow this basic layout
        Label healthLabel = new Label("Health", StyleManager.generateLabelStyle(25, Color.RED));
        healthValueLabel = new Label("10/10", StyleManager.generateLabelStyle(25, Color.RED));

        Label goldLabel = new Label("Gold", StyleManager.generateLabelStyle(25, Color.GOLD));
        goldValueLabel = new Label("0", StyleManager.generateLabelStyle(25, Color.GOLD));

        Label scoreLabel = new Label("Score", StyleManager.generateLabelStyle(25, Color.MAGENTA));
        scoreValueLabel = new Label("0", StyleManager.generateLabelStyle(25, Color.MAGENTA));


        hudTable.add(goldLabel).expandX().padTop(5);
        hudTable.add(healthLabel).expandX().padTop(5);
        hudTable.add(scoreLabel).expandX().padTop(5);
        hudTable.row();
        hudTable.add(goldValueLabel).expandX();
        hudTable.add(healthValueLabel).expandX();
        hudTable.add(scoreValueLabel).expandX();
        hudTable.row();

        hudStage.addActor(hudTable);

    }

}
