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

    //inate variables
    private GameInstance gameInstance;
    private DepartmentScreen departmentScreen;
    private final Integer minigamePower;

    //game variables
    private List<Integer> cardLocs = new ArrayList<>(), selectedLocs = new ArrayList<>();
    //card to be won
    private Card toWin;

    //game progress variables
    private boolean gameOver = false, exiting = false;
    private final Integer attempts = 4;

    //display tables
    private Table introTable, playTable;

    public MinigameScreen(GameInstance gameInstance, DepartmentScreen departmentScreen) {
        super(new Stage(new ScreenViewport()), FileManager.menuScreenBG);
        this.gameInstance = gameInstance;
        this.departmentScreen = departmentScreen;

        minigamePower = departmentScreen.getDepartment().getMinigamePower();

        createIntroTable();
        setMessageHUD(gameInstance);
    }

    /***
     * Check game progress.
     * If player won, give card.
     * If game over, switch back to DeptScreen
     */
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

    /***
     * Start game (when play has been clicked).
     */
    private void playGame() {
        createPlayTable();
    }

    /***
     * Card at specified position has been clicked.
     * @param index specified position
     */
    private void cardClick(int index) {
        if (!gameOver && !exiting) {
            //add selected index to list
            selectedLocs.add(index);
            Cell cell = playTable.getCells().get(index);
            //reveal card
            if (cardLocs.contains(index)) {
                //if selected index was correct
                Image image = new Image(toWin.getTexture());
                image.setScaling(Scaling.fit);
                cell.setActor(image);
            } else {
                //if selected index was incorrect
                Image image = new Image(FileManager.minigameDrawable);
                image.setScaling(Scaling.fit);
                cell.setActor(image);
            }
            //if attempts used or both cards found, end game
            if (selectedLocs.size() == attempts || selectedLocs.containsAll(cardLocs)) {
                gameOver = true;
            }
        }
    }

    /***
     * Create on-screen elements for minigame.
     */
    private void createPlayTable() {
        //clear stage
        getStage().clear();
        //create table
        playTable = new Table();
        playTable.setFillParent(true);
        playTable.top();
        if(gameInstance.getGame().DEBUG) {
            playTable.debug();
        }

        Optional<Card> toWinOpt = gameInstance.getCardManager().randomCard(minigamePower);
        if (toWinOpt.isPresent()) {
            toWin = toWinOpt.get();
            Drawable drawable = FileManager.minigameBackDrawable;
            Random random = new Random();
            //generate random winning positions
            while (cardLocs.size() < 2) {
                int rand = random.nextInt(8);
                if (!cardLocs.contains(rand)) {
                    cardLocs.add(rand);
                }
            }
            //create card arrangement
            for (int i = 0; i < 8; i++) {
                //create card button
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
            //add card table to stage
            getStage().addActor(playTable);
        } else {
            //minigame error - reset to intro screen and refund cost
            Gdx.app.error("MinigameScreen", "Card with minigame power couldn't be generated!");
            gameInstance.getPlayer().addGold(getPlayCost());
            createIntroTable();
        }
    }

    /***
     * Get cost to play minigame (based on minigame scaling factor).
     * @return cost to play minigame (gold)
     */
    private Integer getPlayCost() {
        return minigamePower * 50;
    }

    /***
     * Create introduction table.
     * (Explains rules, etc).
     */
    private void createIntroTable() {
        getStage().clear();
        introTable = new Table();
        introTable.top();
        introTable.setFillParent(true);

        //create intro label
        Label introLabel = new Label(
                "Welcome! This game costs " + getPlayCost() + " to play!\n" +
                        "The aim is to pick 2 identical cards from a pile of 8.\n" +
                        "You have only " + attempts + " attempts! If you win, you get to keep the card!",
                StyleManager.generateLabelStyle(30, Color.WHITE));

        //create play button
        TextButton playButton = new TextButton("Play!",
                StyleManager.generateTBStyle(30, Color.GREEN, Color.GRAY));
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ev, float x, float y) {
                //check player has gold to play
                if (gameInstance.getPlayer().getGold() >= getPlayCost()) {
                    gameInstance.getPlayer().takeGold(getPlayCost());
                    playGame();
                } else {
                    gameInstance.getMessageHUD().addStatusMessage("You do not have enough gold", 5f);
                }
            }
        });

        //create exit button
        TextButton exitButton = new TextButton("Exit",
                StyleManager.generateTBStyle(30, Color.RED, Color.GRAY));
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ev, float x, float y) {
                gameInstance.fadeSwitchScreen(departmentScreen);
            }
        });

        //add elements to table
        introTable.add(introLabel);
        introTable.row();
        introTable.add(playButton);
        introTable.row();
        introTable.add(exitButton);
        //add table to screen
        getStage().addActor(introTable);
    }

    @Override
    public void renderInner(float delta) {
        update();
    }
}
