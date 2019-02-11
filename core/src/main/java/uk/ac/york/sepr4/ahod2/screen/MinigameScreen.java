package uk.ac.york.sepr4.ahod2.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import uk.ac.york.sepr4.ahod2.GameInstance;
import uk.ac.york.sepr4.ahod2.io.FileManager;
import uk.ac.york.sepr4.ahod2.io.StyleManager;
import uk.ac.york.sepr4.ahod2.object.card.Card;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class MinigameScreen extends AHODScreen {

    private GameInstance gameInstance;
    private DepartmentScreen departmentScreen;

    private final Integer minigamePower;

    private List<Integer> cardLocs = new ArrayList<>(), selectedLocs = new ArrayList<>();
    private Card toWin;

    private boolean gameOver = false, exiting = false;
    private final Integer attempts = 4;

    private Table introTable, playTable;

    public MinigameScreen(GameInstance gameInstance, DepartmentScreen departmentScreen) {
        super(new Stage(new ScreenViewport()), FileManager.menuScreenBG);
        this.gameInstance = gameInstance;
        this.departmentScreen = departmentScreen;

        minigamePower = departmentScreen.getDepartment().getMinigamePower();

        createIntroTable();
        setHUD(gameInstance);
    }

    private void update() {
        if (gameOver && !exiting) {
            if (selectedLocs.containsAll(cardLocs)) {
                //won
                gameInstance.getPlayer().getShip().addCard(toWin);
                gameInstance.getMessageHUD().addStatusMessage("You Won!", 5f);
            } else {
                //lost
                gameInstance.getMessageHUD().addStatusMessage("You Lost!", 5f);
            }
            exiting = true;
            departmentScreen.resetMinigame();
            gameInstance.fadeSwitchScreen(departmentScreen, true);
        }

    }

    private void playGame() {
        createPlayTable();
    }

    private void cardClick(int index) {
        if (!gameOver && !exiting) {
            selectedLocs.add(index);
            Cell cell = playTable.getCells().get(index);
            if (cardLocs.contains(index)) {
                Image image = new Image(toWin.getTexture());
                image.setScaling(Scaling.fit);
                cell.setActor(image);
            } else {
                Image image = new Image(FileManager.minigameDrawable);
                image.setScaling(Scaling.fit);
                cell.setActor(image);
            }
            if (selectedLocs.size() == attempts || selectedLocs.containsAll(cardLocs)) {
                gameOver = true;
            }
        }
    }

    private void createPlayTable() {
        getStage().clear();
        playTable = new Table();
        playTable.setFillParent(true);
        playTable.top();
        playTable.debug();

        Optional<Card> toWinOpt = gameInstance.getCardManager().randomCard(minigamePower);
        if (toWinOpt.isPresent()) {
            toWin = toWinOpt.get();
            Drawable drawable = FileManager.minigameBackDrawable;
            Random random = new Random();
            while (cardLocs.size() < 2) {
                int rand = random.nextInt(8);
                if (!cardLocs.contains(rand)) {
                    cardLocs.add(rand);
                }
            }
            for (int i = 0; i < 8; i++) {
                ImageButton iB = new ImageButton(drawable);
                int finalI = i;
                iB.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent ev, float x, float y) {
                        cardClick(finalI);
                    }
                });
                playTable.add(iB).expandX()
                        .padLeft(Value.percentWidth(0.05f, playTable))
                        .width(Value.percentWidth(0.2f, playTable))
                        .padTop(Value.percentHeight(0.05f, playTable));

                if (i == 3) {
                    playTable.row();
                }
            }
            getStage().addActor(playTable);
        } else {
            Gdx.app.error("MinigameScreen", "Card with minigame power couldn't be generated!");
            gameInstance.getPlayer().addGold(getPlayCost());
            createIntroTable();
        }
    }

    private Integer getPlayCost() {
        return minigamePower * 50;
    }

    private void createIntroTable() {
        getStage().clear();
        introTable = new Table();
        introTable.top();
        introTable.setFillParent(true);

        Label introLabel = new Label(
                "Welcome! This game costs " + getPlayCost() + " to play!\n" +
                        "The aim is to pick 2 identical cards from a pile of 8.\n" +
                        "You have only " + attempts + " attempts! If you win, you get to keep the card!",
                StyleManager.generateLabelStyle(30, Color.WHITE));

        TextButton playButton = new TextButton("Play!", StyleManager.generateTBStyle(30, Color.GREEN, Color.GRAY));
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ev, float x, float y) {
                if (gameInstance.getPlayer().getGold() >= getPlayCost()) {
                    gameInstance.getPlayer().takeGold(getPlayCost());
                    playGame();
                } else {
                    gameInstance.getMessageHUD().addStatusMessage("You do not have enough gold", 5f);
                }
            }
        });

        TextButton exitButton = new TextButton("Exit", StyleManager.generateTBStyle(30, Color.RED, Color.GRAY));
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ev, float x, float y) {
                gameInstance.fadeSwitchScreen(departmentScreen);
            }
        });

        introTable.add(introLabel);
        introTable.row();
        introTable.add(playButton);
        introTable.row();
        introTable.add(exitButton);
        getStage().addActor(introTable);
    }

    @Override
    public void renderInner(float delta) {
        update();
    }
}
