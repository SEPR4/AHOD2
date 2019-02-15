package uk.ac.york.sepr4.ahod2.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import lombok.Getter;
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
    private Integer difficulty;
    @Getter
    private Ship enemy;

    @Getter
    private Player player;

    private Image playerShipImage, enemyShipImage;
    private Label playerHealthLabel, enemyHealthLabel, playerManaLabel, enemyManaLabel;
    private TextButton drawButton;

    private Table table;

    private BattleTurn turn;
    private boolean animating = false;

    private Integer turnNo = 1;

    @Getter
    private static final Integer handSize = 5;

    /***
     * Used by generic battle encounters
     * @param gameInstance
     */
    public BattleScreen(GameInstance gameInstance) {
        this(gameInstance,
                ShipFactory.generateEnemyShip(gameInstance.getCurrentLevel().getDifficulty()),
                gameInstance.getCurrentLevel().getDifficulty(),
                gameInstance.getCurrentLevel().getBattleGold());
    }

    /***
     * Used when battle initiated through encounter (by choice)
     * @param gameInstance
     * @param enemy
     * @param gold
     */
    public BattleScreen(GameInstance gameInstance, Ship enemy, Integer difficulty, Integer gold) {
        super(new Stage(new ScreenViewport(), new SpriteBatch()), FileManager.battleScreenBG);
        this.gameInstance = gameInstance;
        this.enemy = enemy;
        this.difficulty = difficulty;
        this.gold = gold;

        player = gameInstance.getPlayer();

        loadBattleElements();

        turn = BattleTurn.PLAYER;

        setMessageHUD(gameInstance);
        setAnimationsHUD(gameInstance);

        enemy.battleStart(gameInstance.getCardManager().getDefaultCards());
        player.getShip().battleStart(gameInstance.getCardManager().getDefaultCards());

        for (int i = 0; i<3; i++) {
            gameInstance.getAnimationHUD().addDamageAnimation(new Vector2(1000, 100), 10, 5f);
        }
    }

    @Override
    public void renderInner(float delta) {
        updateBattleElements();

        if(animating) {
            //do animations

            //switch turns
            if(turn == BattleTurn.PLAYER) {
                //Gdx.app.debug("BattleScreen", "Applying Enemy Damage!");
                enemy.applyDelayedHeal();
                if(enemy.applyDelayedDamage()) {
                    //sink Animation
                    hasDied(enemy);
                } else {
                    //damage Animation
                }


                turn = BattleTurn.ENEMY;
            } else if(turn == BattleTurn.ENEMY) {
                //Gdx.app.debug("BattleScreen", "Applying Player Damage!");
                player.getShip().applyDelayedHeal();
                if(player.getShip().applyDelayedDamage()) {
                    hasDied(player.getShip());
                }

                turnNo++;
                //set to turn number (max at 10)
                player.getShip().setMaxMana(turnNo);
                player.getShip().setMana(player.getShip().getMaxMana());
                enemy.setMaxMana(turnNo);
                enemy.setMana(enemy.getMaxMana());

                turn = BattleTurn.PLAYER;
                Gdx.app.debug("BattleScreen", "Player Move!");
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


    public void useCard(Ship source, Ship target, Card card) {
        if(source.getMana() < card.getManaCost()) {
            //not enough mana to cast
            gameInstance.getMessageHUD().addStatusMessage("Not enough mana!", 3f);
            return;
        }

        source.useCard(card);
        source.deductMana(card.getManaCost());
        for(int i=0;i<=card.getMoveTime();i++) {
            source.addDamage(card.getDamageSelf(), i);
            target.addDamage(card.getDamage(), i);
            source.addHeal(card.getHeal(), i);
        }
        //could add delayed healing
        Gdx.app.debug("BattleScreen","Using card: "+card.getName());

    }

    public void endTurn() {
        //skip turn
        Gdx.app.debug("BattleScreen","Ending turn!");
        animating = true;
    }

    private Integer getCardDrawCost() {
        if(turnNo > 10) {
            return 5;
        } else if(turnNo == 1) {
            return 1;
        }else {
            return (int)Math.floor((double)turnNo/2);
        }
    }


    public boolean drawCard(Ship source) {
        if(source.getMana() >=getCardDrawCost()) {
            if (source.getHand().size() < handSize) {
                if (gameInstance.getCardManager().drawRandomCard(source)) {
                    Gdx.app.debug("BattleScreen", "Drawing Card!");
                    source.deductMana(getCardDrawCost());
                    return true;
                } else {
                    if (turn == BattleTurn.PLAYER) {
                        gameInstance.getMessageHUD().addStatusMessage("Can't draw card!", 3f);
                    }
                }
            } else {
                if (turn == BattleTurn.PLAYER) {
                    gameInstance.getMessageHUD().addStatusMessage("Max hand size!", 3f);
                }
            }
        } else {
            if (turn == BattleTurn.PLAYER) {
                gameInstance.getMessageHUD().addStatusMessage("Not enough mana!", 3f);
            }
        }
        return false;
    }


    private void hasDied(Ship ship) {
        if(ship == enemy) {
            //player wins (reset mana and cards)
            player.addGold(gold);
            gameInstance.getMessageHUD().addGoldMessage(gold);
            if(enemy.isBoss()) {
                //screen is switched in this method
                gameInstance.advanceLevel();
            } else {
                gameInstance.fadeSwitchScreen(new CardSelectionScreen(gameInstance, gameInstance.getCardManager().getRandomSelection(difficulty)), true);
            }
        } else {
            gameInstance.fadeSwitchScreen(new EndScreen(gameInstance, false), true);
        }
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
        drawButton = new TextButton("Draw Card", StyleManager.generateTBStyle(30, Color.CORAL, Color.GRAY));
        drawButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ev, float x, float y) {
                Gdx.app.debug("BattleScreen","Pressed draw card button!");

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
                .height(Value.percentHeight(0.40f, table))
                .width(Value.percentWidth(0.5f, table))
                .padTop(Value.percentHeight(0.02f, table));
        table.add(enemyShipImage).expandX()
                .height(Value.percentHeight(0.40f, table))
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

        getStage().addActor(table);
    }

    private ImageButton getIBForCard(Card card, Integer index) {
        Integer currentHandSize = player.getShip().getHand().size();
        Texture tex = card.getTexture();
        float height = table.getHeight()/2; //1/3 of screen
        float width = tex.getWidth()*(height/(tex.getHeight()));
        ImageButton iB = new ImageButton(new TextureRegionDrawable(tex));
        iB.setSize(width, height);
        iB.setName(""+card.getId());
        iB.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ev, float x, float y) {
                if(playerTurnCheck()) {
                    useCard(player.getShip(), enemy, card);
                }
            }
        });
        float w = Gdx.graphics.getWidth();

        float pos = (w-(currentHandSize*width))/(currentHandSize+1);
        iB.setPosition((index+1)*pos+(index*width), -10-height/8);

        return iB;
    }

    private void updateBattleElements() {
        drawButton.setText("Draw Card ("+getCardDrawCost()+")");

        playerHealthLabel.setText(player.getShip().getHealth() + "/" + player.getShip().getMaxHealth());
        enemyHealthLabel.setText(enemy.getHealth() + "/" + enemy.getMaxHealth());

        playerManaLabel.setText(player.getShip().getMana() + "/" + player.getShip().getMaxMana());
        enemyManaLabel.setText(enemy.getMana() + "/" + enemy.getMaxMana());

        Array<Actor> toRemove = new Array<>();
        for(Actor actor : getStage().getActors()) {
            if(actor instanceof ImageButton) {
                toRemove.add(actor);
            }
        }
        getStage().getActors().removeAll(toRemove, false);

        Integer index = 0;
        for(Card card:player.getShip().getHand()) {
            getStage().addActor(getIBForCard(card, index));
            index++;
        }

    }

}

enum BattleTurn {
    PLAYER, ENEMY
}
