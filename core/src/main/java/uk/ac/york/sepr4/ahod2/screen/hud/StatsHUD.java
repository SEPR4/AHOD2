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
import uk.ac.york.sepr4.ahod2.io.FileManager;
import uk.ac.york.sepr4.ahod2.object.entity.Player;

public class StatsHUD {

    @Getter
    private Stage hudStage;

    private GameInstance gameInstance;

    private Table hudTable;

    private Label turnLabel, scoreLabel,
            turnValueLabel, goldValueLabel, scoreValueLabel;

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
        scoreValueLabel.setText(player.getScore());

        hudStage.act();
        hudStage.draw();
    }

    private void createTable() {

        hudTable = new Table();
        hudTable.top();
        hudTable.setFillParent(true);

        //all labels follow this basic layout
        turnLabel = new Label("Turn", new Label.LabelStyle(new BitmapFont(), Color.RED));
        turnValueLabel = new Label("0", new Label.LabelStyle(new BitmapFont(), Color.RED));

        Image goldImage = new Image(FileManager.hudGold);
        goldValueLabel = new Label("0", new Label.LabelStyle(new BitmapFont(), Color.GOLD));

        scoreLabel = new Label("Score", new Label.LabelStyle(new BitmapFont(), Color.MAGENTA));
        scoreValueLabel = new Label("0", new Label.LabelStyle(new BitmapFont(), Color.MAGENTA));


        hudTable.add(turnLabel).expandX().padTop(5);
        hudTable.add(goldImage).expandX().padTop(5);
        hudTable.add(scoreLabel).expandX().padTop(5);
        hudTable.row();
        hudTable.add(turnValueLabel).expandX();
        hudTable.add(goldValueLabel).expandX();
        hudTable.add(scoreValueLabel).expandX();
        hudTable.row();

        hudStage.addActor(hudTable);

    }

}
