package uk.ac.york.sepr4.ahod2.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import lombok.Getter;
import uk.ac.york.sepr4.ahod2.GameInstance;
import uk.ac.york.sepr4.ahod2.io.FileManager;
import uk.ac.york.sepr4.ahod2.io.StyleManager;
import uk.ac.york.sepr4.ahod2.object.building.Department;
import uk.ac.york.sepr4.ahod2.object.card.Card;
import uk.ac.york.sepr4.ahod2.object.entity.Player;

import java.util.Random;

public class DepartmentScreen extends AHODScreen {

    private GameInstance gameInstance;
    @Getter
    private Department department;
    private MinigameScreen minigameScreen;
    private Table topTable = new Table();
    private TextButton repairButton, upgradeButton;

    private Integer addedHealth = 0;
    private boolean purchasedUpgrade = false;

    public DepartmentScreen(GameInstance gameInstance, Department department) {
        super(new Stage(new ScreenViewport()), FileManager.menuScreenBG);

        this.gameInstance = gameInstance;
        this.department = department;

        this.minigameScreen = new MinigameScreen(gameInstance, this);

        generateRandomUpgrade();

        setupTopTable();
        setMessageHUD(gameInstance);
    }

    private void generateRandomUpgrade() {
        Random random = new Random();
        addedHealth = 5*(random.nextInt(department.getMinigamePower())+1);
    }

    private Integer getUpgradeCost() {
        return department.getMinigamePower()*50;
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
        upgradeButton = new TextButton("Click to upgrade!\n (+" +addedHealth +" health)\nCost: "+getUpgradeCost(),
                StyleManager.generateTBStyle(30, Color.PURPLE, Color.GRAY));
        upgradeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ev, float x, float y) {
                purchaseUpgrade();
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
        topTable.add(upgradeButton)
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

    private void repair() {
        Player player = gameInstance.getPlayer();
        if(getRepairCost() <= player.getGold()) {
            player.takeGold(getRepairCost());
            player.getShip().setHealth(player.getShip().getMaxHealth());
        } else {
            gameInstance.getMessageHUD().addStatusMessage("Not enough gold!", 3f);
        }
    }

    private void purchaseUpgrade() {
        Player player = gameInstance.getPlayer();
        if(!purchasedUpgrade) {
            if (player.getGold() >= getUpgradeCost()) {
                purchasedUpgrade = true;
                player.takeGold(getUpgradeCost());
                player.getShip().setMaxHealth(player.getShip().getMaxHealth() + addedHealth);
            } else {
                gameInstance.getMessageHUD().addStatusMessage("Not enough gold!", 3f);
            }
        } else {
            gameInstance.getMessageHUD().addStatusMessage("Already purchased!", 3f);
        }
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

    private Integer getRepairCost() {
        Player player = gameInstance.getPlayer();
        Integer toHeal = (player.getShip().getMaxHealth()-player.getShip().getHealth());
        return toHeal*department.getRepairCost();
    }

    private void updateTables() {
        repairButton.setText("Click to repair!\nCost: "+getRepairCost());


    }

    @Override
    public void renderInner(float delta) {
        updateTables();
    }
}
