package uk.ac.york.sepr4.ahod2.screen;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import uk.ac.york.sepr4.ahod2.GameInstance;
import uk.ac.york.sepr4.ahod2.io.FileManager;
import uk.ac.york.sepr4.ahod2.io.FontManager;
import uk.ac.york.sepr4.ahod2.object.encounter.Encounter;
import uk.ac.york.sepr4.ahod2.object.encounter.EncounterOption;
import uk.ac.york.sepr4.ahod2.object.entity.Player;

public class EncounterScreen implements Screen {

    private Stage encounterStage;
    private SpriteBatch batch = new SpriteBatch();

    private Encounter encounter;
    private GameInstance gameInstance;

    public EncounterScreen(GameInstance gameInstance, Encounter encounter) {
        this.gameInstance = gameInstance;
        this.encounter = encounter;

        this.encounterStage = new Stage(new ScreenViewport(), batch);

        createEncounterInfo();

        Gdx.input.setInputProcessor(encounterStage);
    }


    @Override
    public void render(float delta) {
        // clear the screen ready for next set of images to be drawn
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        drawBackground();
        batch.end();

        encounterStage.act();
        encounterStage.draw();
    }

    private void createEncounterInfo() {
        Table table1 = new Table();
        table1.setFillParent(true);
        table1.top();

        Label.LabelStyle messageStyle = new Label.LabelStyle();
        messageStyle.font = FontManager.generatePirateFont(60, Color.BLACK);

        Label encounterText = new Label(encounter.getText(), messageStyle);

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = FontManager.generatePirateFont(30, Color.BLACK);

        table1.add(encounterText).expandX().padTop(Gdx.graphics.getHeight()/4);

        Table table2 = new Table();
        table2.setFillParent(true);
        table2.top();
        for(EncounterOption encounterOption : encounter.getOptions()) {
            TextButton tB = new TextButton(encounterOption.getText(), buttonStyle);

            tB.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent ev, float x, float y){
                    optionClick(encounterOption);
                }
            });

            table2.add(tB).expandX().padTop(Gdx.graphics.getHeight()/2);
        }

        encounterStage.addActor(table1);
        encounterStage.addActor(table2);

    }

    private void optionClick(EncounterOption encounterOption){
        Gdx.app.debug("EncounterScreen", "Option clicked!");
        if(encounterOption.isBattle()) {
            BattleScreen battleScreen = new BattleScreen(gameInstance,
                    encounterOption.getDifficulty(),
                    encounterOption.getGold(),
                    encounterOption.getSupplies());
            gameInstance.switchScreen(battleScreen);
        } else {
            Player player = gameInstance.getPlayer();
            player.setGold(player.getGold()+encounterOption.getGold());
            player.setSupplies(player.getSupplies()+encounterOption.getSupplies());
            gameInstance.switchScreen(gameInstance.getSailScreen());
        }
    }

    private void drawBackground() {
        //sets background texture
        Texture texture = FileManager.encounterScreenBG;
        TextureRegion textureRegion = new TextureRegion(texture);
        textureRegion.setRegion(0,0,texture.getWidth(),texture.getHeight());
        batch.draw(texture,0,0);
    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(encounterStage);
    }
    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }



    @Override
    public void resume() {
        Gdx.input.setInputProcessor(encounterStage);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        encounterStage.dispose();

    }
}
