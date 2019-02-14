package uk.ac.york.sepr4.ahod2.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import lombok.Getter;
import uk.ac.york.sepr4.ahod2.GameInstance;
import uk.ac.york.sepr4.ahod2.io.FileManager;
import uk.ac.york.sepr4.ahod2.io.StyleManager;
import uk.ac.york.sepr4.ahod2.object.building.Department;
import uk.ac.york.sepr4.ahod2.object.card.Card;
import uk.ac.york.sepr4.ahod2.object.entity.Player;
import uk.ac.york.sepr4.ahod2.object.entity.Ship;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DepartmentScreen extends AHODScreen {

    private GameInstance gameInstance;
    @Getter
    private Department department;

    private MinigameScreen minigameScreen;

    private Table topTable = new Table(), storeTable = new Table();

    private TextButton repairButton;
    private Label currentCardCost;

    private HashMap<Card, ImageButton> cardButtons = new HashMap<>();

    public DepartmentScreen(GameInstance gameInstance, Department department) {
        super(new Stage(new ScreenViewport()), FileManager.menuScreenBG, false);

        this.gameInstance = gameInstance;
        this.department = department;

        this.minigameScreen = new MinigameScreen(gameInstance, this);

        setupTopTable();
        setupStoreTable();
        setMessageHUD(gameInstance);

    }

    private void setupTopTable() {
        topTable.setFillParent(true);
        topTable.top();

        Label label = new Label("Welcome to "+department.getName(), StyleManager.generateLabelStyle(50, Color.RED));
        repairButton = new TextButton("Click to repair!\nCost: "+getRepairCost(),
                StyleManager.generateTBStyle(40, Color.GREEN, Color.GRAY));
        repairButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ev, float x, float y) {
                repair();
            }
        });
        TextButton gambleButton = new TextButton("Visit Tavern (Minigame)",
                StyleManager.generateTBStyle(30, Color.GOLD, Color.GRAY));
        gambleButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ev, float x, float y) {
                switchMinigame();
            }
        });
        TextButton exitButton = new TextButton("Exit Department",
                StyleManager.generateTBStyle(30, Color.RED, Color.GRAY));
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ev, float x, float y) {
                exitScreen();
            }
        });

        topTable.add(label)
                .expandX()
                .padTop(Value.percentHeight(0.02f, topTable))
                .height(Value.percentHeight(0.1f, topTable));
        topTable.row();
        topTable.add(repairButton)
                .expandX()
                .padTop(Value.percentHeight(0.02f, topTable))
                .height(Value.percentHeight(0.1f, topTable));
        topTable.row();
        topTable.add(gambleButton)
                .expandX()
                .padTop(Value.percentHeight(0.02f, topTable))
                .height(Value.percentHeight(0.1f, topTable));
        topTable.row();
        topTable.add(exitButton)
                .expandX()
                .padTop(Value.percentHeight(0.02f, topTable))
                .height(Value.percentHeight(0.1f, topTable));

        getStage().addActor(topTable);
    }

    private boolean repair() {
        Player player = gameInstance.getPlayer();
        if(getRepairCost() < player.getGold()) {
            return false;
        }
        player.takeGold(getRepairCost());
        player.getShip().setHealth(player.getShip().getMaxHealth());
        return true;
    }

    public void resetMinigame() {
        this.minigameScreen = new MinigameScreen(gameInstance, this);
    }

    private void switchMinigame() {
        gameInstance.fadeSwitchScreen(minigameScreen);
    }

    private void exitScreen() {
        gameInstance.fadeSwitchScreen(gameInstance.getSailScreen());
    }

    private boolean buyCard(Card card) {
        Player player = gameInstance.getPlayer();
        if(card.getShopCost() > player.getGold()) {
            return false;
        }
        player.takeGold(card.getShopCost());
        player.getShip().addCard(card);
        return true;
    }

    private Integer getRepairCost() {
        Player player = gameInstance.getPlayer();
        Integer toHeal = (player.getShip().getMaxHealth()-player.getShip().getHealth());
        return toHeal*department.getRepairCost();
    }

    private void setupStoreTable() {
        storeTable.setFillParent(true);
        storeTable.bottom();

        currentCardCost = new Label("Cost: ", StyleManager.generateLabelStyle(30, Color.GOLD));
        storeTable.add(currentCardCost)
                .expandX()
                .padBottom(Value.percentHeight(0.02f, storeTable));
        storeTable.row();

        for(Card card : department.getStock()) {
            ImageButton iB = new ImageButton(new TextureRegionDrawable(card.getTexture()));
            iB.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent ev, float x, float y) {
                    buyCard(card);
                }
                @Override
                public void enter(InputEvent ev, float x, float y, int pointer, Actor fromActor) {
                    currentCardCost.setText("Cost: "+card.getShopCost());
                }
                @Override
                public void exit(InputEvent ev, float x, float y, int pointer, Actor toActor) {
                    currentCardCost.setText("");
                }
            });
            cardButtons.put(card, iB);
            storeTable.add(iB)
                    .expandX()
                    .height(Value.percentHeight(0.30f, storeTable))
                    .padBottom(Value.percentHeight(0.02f, storeTable));
        }

        getStage().addActor(storeTable);
    }

    private void updateTables() {
        repairButton.setText("Click to repair!\nCost: "+getRepairCost());

        Ship player = gameInstance.getPlayer().getShip();
        List<Card> toRemove = new ArrayList<>();
        for(Card card: cardButtons.keySet()) {
            if(player.getDeck().contains(card)) {
                storeTable.removeActor(cardButtons.get(card));
                toRemove.add(card);
            }
        }
        toRemove.forEach(card -> cardButtons.remove(card));
    }

    @Override
    public void renderInner(float delta) {
        updateTables();
    }
}
