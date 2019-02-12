package uk.ac.york.sepr4.ahod2.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import uk.ac.york.sepr4.ahod2.GameInstance;
import uk.ac.york.sepr4.ahod2.io.FileManager;
import uk.ac.york.sepr4.ahod2.io.StyleManager;
import uk.ac.york.sepr4.ahod2.object.ShipFactory;
import uk.ac.york.sepr4.ahod2.object.card.Card;
import uk.ac.york.sepr4.ahod2.object.entity.Player;
import uk.ac.york.sepr4.ahod2.object.entity.Ship;
import uk.ac.york.sepr4.ahod2.util.BattleAI;

import java.util.List;
import java.util.*;

public class BattleScreen extends AHODScreen {

    private GameInstance gameInstance;
    private Integer gold;
    private Ship enemy;

    private Player player;

    private Image playerShipImage, enemyShipImage;
    private Label playerHealthLabel, enemyHealthLabel, playerManaLabel, enemyManaLabel;
    private List<Cell> buttonCells = new ArrayList<>();
    private Table table, cardTable;

    private BattleTurn turn;
    private boolean animating = false;

    private static final Integer handSize = 4;

    /***
     * Used by generic battle encounters
     * @param gameInstance
     */
    public BattleScreen(GameInstance gameInstance) {
        this(gameInstance,
                ShipFactory.generateEnemyShip(gameInstance.getCurrentLevel().getDifficulty()),
                gameInstance.getCurrentLevel().getBattleGold());
    }

    /***
     * Used when battle initiated through encounter (by choice)
     * @param gameInstance
     * @param enemy
     * @param gold
     */
    public BattleScreen(GameInstance gameInstance, Ship enemy, Integer gold) {
        super(new Stage(new ScreenViewport(), new SpriteBatch()), FileManager.battleScreenBG, false);
        this.gameInstance = gameInstance;
        this.enemy = enemy;
        this.gold = gold;

        player = gameInstance.getPlayer();

        loadBattleElements();
        loadCardElements();

        turn = BattleTurn.PLAYER;

        setMessageHUD(gameInstance);

    }

    @Override
    public void renderInner(float delta) {
        updateBattleElements();

        if(animating) {
            //do animations

            //switch turns
            if(turn == BattleTurn.PLAYER) {
                player.getShip().applyDelayedHeal();
                if(player.getShip().applyDelayedDamage()) {
                    hasDied(player.getShip());
                }

                player.getShip().incMana(1);
                turn = BattleTurn.ENEMY;
            } else if(turn == BattleTurn.ENEMY) {
                enemy.applyDelayedHeal();
                if(enemy.applyDelayedDamage()) {
                    //sink animation
                    hasDied(enemy);
                } else {
                    //damage animation
                }

                enemy.incMana(1);
                turn = BattleTurn.PLAYER;
            }

            animating = false;
        } else {
            if(turn == BattleTurn.ENEMY) {
                //do enemy turn
                BattleAI.chooseMove(this);
            }
        }
    }


    private boolean playerTurnCheck() {
        if(turn == BattleTurn.PLAYER && !animating) {
            return true;
        } else {
            gameInstance.getMessageHUD().addStatusMessage("Not your turn!", 2f);
            return false;
        }
    }


    private void useCard(Ship source, Ship target, Card card) {
        if(source.getMana() < card.getManaCost()) {
            //not enough mana to cast
            gameInstance.getMessageHUD().addStatusMessage("Not enough mana!", 3f);
        }
        source.useCard(card);
        source.deductMana(card.getManaCost());
        if(card.getDamageTime() > 0) {
            for(int i=0;i<card.getDamageTime();i++) {
                source.addDamage(card.getDamageSelf(), i);
                target.addDamage(card.getDamage(), i);
            }
        } else {
            source.addDamage(card.getDamageSelf(), 0);
            target.addDamage(card.getDamage(), 0);
        }
        //could add delayed healing
        source.addHeal(card.getHeal(), 0);

        animating = true;
    }

    private void endTurn() {
        //skip turn

        animating = true;
    }


    private void drawCard(Ship source) {
        if(gameInstance.getCardManager().drawRandomCard(source)) {
            animating = true;
        }
    }


    private void hasDied(Ship ship) {
        if(ship == enemy) {
            //player wins (reset mana and cards)
            player.getShip().battleOver();
            player.addGold(gold);
            gameInstance.getMessageHUD().addGoldMessage(gold);
            if(enemy.isBoss()) {
                //screen is switched in this method
                gameInstance.advanceLevel();
            } else {
                gameInstance.fadeSwitchScreen(gameInstance.getSailScreen());
            }
        } else {
            gameInstance.switchScreen(new EndScreen(gameInstance, false));
        }
        dispose();
    }

    private void loadCardElements() {
        //bottom 1/3 (deck display, hold 4 cards at once)
        cardTable = new Table();
        cardTable.setFillParent(true);
        cardTable.bottom();
        cardTable.debug();

        for(int i=0;i<handSize;i++) {
            buttonCells.add(
                    cardTable.add()
                            .expandX()
                            .height(Value.percentHeight(0.27f, cardTable))
                            .width(Value.percentWidth(1f/(handSize), cardTable))
                            .padBottom(Value.percentHeight(0.02f, cardTable)));
        }

        getStage().addActor(table);
        getStage().addActor(cardTable);
    }

    private void loadBattleElements() {

        //create ship images and set scaling (so should scale if screen size changes)
        playerShipImage = new Image(player.getShip().getImage());
        playerShipImage.setScaling(Scaling.fillY);

        //flip enemy image
        TextureRegion txR = new TextureRegion(enemy.getImage());
        txR.flip(true, false);
        enemyShipImage = new Image(txR);
        enemyShipImage.setScaling(Scaling.fillY);

        //health labels
        Label.LabelStyle style = StyleManager.generateLabelStyle(30, Color.RED);
        playerHealthLabel = new Label(player.getShip().getHealth() + "/" + player.getShip().getMaxHealth(), style);
        enemyHealthLabel = new Label(enemy.getHealth() + "/" + enemy.getMaxHealth(), style);

        //mana labels
        Label.LabelStyle style2 = StyleManager.generateLabelStyle(30, Color.PURPLE);
        playerManaLabel = new Label(player.getShip().getMana() + "/" + player.getShip().getMaxMana(), style2);
        enemyManaLabel = new Label(enemy.getMana() + "/" + enemy.getMaxMana(), style2);

        //buttons
        TextButton drawButton = new TextButton("Draw Card", StyleManager.generateTBStyle(30, Color.CORAL, Color.GRAY));
        drawButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ev, float x, float y) {
                if(playerTurnCheck()) {
                    drawCard(player.getShip());
                }
            }
        });
        TextButton endTurnButton = new TextButton("End Turn", StyleManager.generateTBStyle(30, Color.ORANGE, Color.GRAY));
        endTurnButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ev, float x, float y) {
                if(playerTurnCheck()) {
                    endTurn();
                }
            }
        });

        table = new Table();
        table.setFillParent(true);
        table.top();
        table.debug();

        //top 2/3 (ship + stat display)
        table.add(playerShipImage).expandX()
                .height(Value.percentHeight(0.52f, table))
                .width(Value.percentWidth(0.5f, table))
                .padTop(Value.percentHeight(0.02f, table));
        table.add(enemyShipImage).expandX()
                .height(Value.percentHeight(0.52f, table))
                .width(Value.percentWidth(0.5f, table))
                .padTop(Value.percentHeight(0.02f, table));
        table.row();

        table.add(playerHealthLabel).expandX()
                .height(Value.percentHeight(0.05f, table));
        table.add(enemyHealthLabel).expandX()
                .height(Value.percentHeight(0.05f, table));
        table.row();

        table.add(playerManaLabel).expandX()
                .height(Value.percentHeight(0.05f, table))
                .padBottom(Value.percentHeight(0.02f, table));
        table.add(enemyManaLabel).expandX()
                .height(Value.percentHeight(0.05f, table))
                .padBottom(Value.percentHeight(0.02f, table));
        table.row();

        //start of bottom 1/3
        table.add(drawButton).expandX()
                .height(Value.percentHeight(0.04f, table))
                .padBottom(Value.percentHeight(0.02f, table));
        table.add(endTurnButton).expandX()
                .height(Value.percentHeight(0.04f, table))
                .padBottom(Value.percentHeight(0.02f, table));

    }

    private ImageButton getIBForCard(Card card) {
        ImageButton iB = new ImageButton(card.getTexture());
        iB.setName(""+card.getId());
        iB.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ev, float x, float y) {
                if(playerTurnCheck()) {
                    useCard(player.getShip(), enemy, card);
                }
            }
        });
        return iB;
    }

    private void updateBattleElements() {
        playerHealthLabel.setText(player.getShip().getHealth() + "/" + player.getShip().getMaxHealth());
        enemyHealthLabel.setText(enemy.getHealth() + "/" + enemy.getMaxHealth());

        playerManaLabel.setText(player.getShip().getMana() + "/" + player.getShip().getMaxMana());
        enemyManaLabel.setText(enemy.getMana() + "/" + enemy.getMaxMana());


        //Removes discarded cards
        List<Cell> emptyCells = new ArrayList<>();
        for(Cell cells : buttonCells) {
            if(!(cells.getActor() instanceof ImageButton)) {
                emptyCells.add(cells);
            }
        }

        //adds drawn cards
        for(Card cards : player.getShip().getHand()) {
            boolean isPlaced = false;
            for(Cell cells : buttonCells) {
                Actor act = cells.getActor();
                if(act != null && act.getName() != null && act.getName().equalsIgnoreCase(""+cards.getId())) {
                    isPlaced = true;
                }
            }
            if(!isPlaced) {
                emptyCells.get(0).setActor(getIBForCard(cards));
                emptyCells.remove(0);
            }
        }
    }

}

enum BattleMove {
    DRAW, WAIT, CARD
}

enum BattleTurn {
    PLAYER, ENEMY
}
