package uk.ac.york.sepr4.ahod2.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import uk.ac.york.sepr4.ahod2.GameInstance;
import uk.ac.york.sepr4.ahod2.io.FileManager;
import uk.ac.york.sepr4.ahod2.io.StyleManager;
import uk.ac.york.sepr4.ahod2.object.building.Department;
import uk.ac.york.sepr4.ahod2.object.entity.Player;

public class DepartmentScreen extends AHODScreen {

    private GameInstance gameInstance;
    private Department department;

    private Table topTable = new Table(), storeTable = new Table();

    private TextButton repairButton;

    public DepartmentScreen(GameInstance gameInstance, Department department) {
        super(new Stage(new ScreenViewport()), FileManager.menuScreenBG);

        this.gameInstance = gameInstance;
        this.department = department;

        setupTopTable();
        setupStoreTable();
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
                .padTop(Value.percentHeight(0.05f, topTable))
                .height(Value.percentHeight(0.1f, topTable));

        topTable.row();
        topTable.add(repairButton)
                .expandX()
                .padTop(Value.percentHeight(0.05f, topTable))
                .height(Value.percentHeight(0.2f, topTable));
        topTable.add(exitButton)
                .expandX()
                .padTop(Value.percentHeight(0.05f, topTable))
                .height(Value.percentHeight(0.2f, topTable));



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

    private void exitScreen() {
        gameInstance.fadeSwitchScreen(gameInstance.getSailScreen());
    }

    private Integer getRepairCost() {
        Player player = gameInstance.getPlayer();
        Integer toHeal = (player.getShip().getMaxHealth()-player.getShip().getHealth());
        return toHeal*department.getRepairCost();
    }

    private void setupStoreTable() {
        storeTable.setFillParent(true);
        storeTable.bottom();



        getStage().addActor(storeTable);
    }

    private void updateTables() {

    }

    @Override
    public void renderInner(float delta) {
        updateTables();
    }
}
