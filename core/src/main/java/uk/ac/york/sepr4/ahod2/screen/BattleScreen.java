package uk.ac.york.sepr4.ahod2.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
import uk.ac.york.sepr4.ahod2.object.card.CardManager;
import uk.ac.york.sepr4.ahod2.object.entity.Player;
import uk.ac.york.sepr4.ahod2.object.entity.Ship;
import java.util.List;
import java.util.*;

public class BattleScreen extends AHODScreen {

    private GameInstance gameInstance;
    private Integer gold;
    private Ship enemy;

    private Player player;
    private CardManager cardManager;

    private Image playerShipImage, enemyShipImage;
    private Label playerHealthLabel, enemyHealthLabel, playerManaLabel, enemyManaLabel;
    private HashMap<Card, ImageButton> cardButtons = new HashMap<>();
    private List<Cell> buttonCells = new ArrayList<>();
    private Table table, cardTable;

    private BattleTurn turn;

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
        super(new Stage(new ScreenViewport(), new SpriteBatch()), FileManager.battleScreenBG);
        this.gameInstance = gameInstance;
        this.enemy = enemy;
        this.gold = gold;

        player = gameInstance.getPlayer();
        cardManager = gameInstance.getCardManager();

        loadBattleElements();
        loadCardElements();

        turn = BattleTurn.PLAYER;
    }

    @Override
    public void renderInner(float delta) {
        updateBattleElements();
    }


    private void enemyTurn() {
        //do enemy move

        applyDelayedDamage();
        incMana();
    }

    private void applyDelayedDamage() {
        for(Ship ship:Arrays.asList(enemy, player.getShip())) {
            if(ship.applyDelayedDamage()) {
                hasDied(ship);
            }
        }
    }

    private void incMana() {
        for(Ship ship:Arrays.asList(enemy, player.getShip())) {
            ship.incMana(1);
        }
    }

    private boolean clickCard(Ship ship, Card card) {
        if (ship.getMana() < card.getManaCost()) {
            return false;
        }
        ship.deductMana(card.getManaCost());

        Ship source, target;
        if (ship == enemy) {
            source = enemy;
            target = player.getShip();
        } else {
            source = player.getShip();
            target = enemy;
        }
        if(source.damage(card.getDamageSelf())) {
            hasDied(source);
        }

        if (target.damage(card.getDamage())) {
            //dead
            hasDied(target);
        }

        if(card.getDamageTime() > 0) {
            for(int i=0; i<card.getDamageTime()-2;i++){
                //queue damage for n-1 more turns
                source.addDelayedDamage(card.getDamageSelf(), i);
                target.addDelayedDamage(card.getDamage(), i);
            }
        }

        source.heal(card.getHeal());

        ship.useCard(card);
        enemyTurn();
        return true;
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
        TextButton endTurnButton = new TextButton("End Turn", StyleManager.generateTBStyle(30, Color.ORANGE, Color.GRAY));

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

    private ImageButton getIBForCard(Ship ship, Card card) {
        ImageButton iB = new ImageButton(card.getTexture());
        iB.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ev, float x, float y) {
                clickCard(ship,card);
            }
        });
        return iB;
    }

    private void updateBattleElements() {
        playerHealthLabel.setText(player.getShip().getHealth() + "/" + player.getShip().getMaxHealth());
        enemyHealthLabel.setText(enemy.getHealth() + "/" + enemy.getMaxHealth());

        playerManaLabel.setText(player.getShip().getMana() + "/" + player.getShip().getMaxMana());
        enemyManaLabel.setText(enemy.getMana() + "/" + enemy.getMaxMana());


    }

}

enum BattleMove {
    DRAW, WAIT, CARD
}

enum BattleTurn {
    PLAYER, ENEMY, ANIMATION
}
