package uk.ac.york.sepr4.ahod2.screen.hud;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import lombok.Getter;
import uk.ac.york.sepr4.ahod2.GameInstance;
import uk.ac.york.sepr4.ahod2.io.FileManager;
import uk.ac.york.sepr4.ahod2.io.StyleManager;
import uk.ac.york.sepr4.ahod2.screen.AHODScreen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AnimationHUD {
    @Getter
    private Stage animationsStage;
    private GameInstance gameInstance;
    private Array<Vector2> damageAnimationsPoints;
    private Array<Integer> damageValues;
    private Array<Float> damagetime;

    public AnimationHUD(GameInstance gameInstance) {
        this.gameInstance = gameInstance;

        // Local widths and heights.
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        animationsStage = new Stage(new FitViewport(w, h, new OrthographicCamera()));

        this.damageAnimationsPoints = new Array<Vector2>();
        this.damageValues = new Array<Integer>();
        this.damagetime = new Array<Float>();
    }

    public void addDamageAnimation(Vector2 coords, Integer value, Float time){
        this.damageAnimationsPoints.add(coords);
        this.damageValues.add(value);
        this.damagetime.add(time);
    }

    public void update(float delta) {
        Array<Vector2> damageAnimationsPointsCopy = new Array<Vector2>();
        Array<Integer> damageValuesCopy = new Array<Integer>();
        Array<Float> damagetimeCopy = new Array<Float>();

        for(int i = 0; i<damageAnimationsPoints.size; i++){
            this.damagetime.set(i, this.damagetime.get(i) - delta);
            if(this.damagetime.get(i) >= 0){
                damageAnimationsPointsCopy.add(this.damageAnimationsPoints.get(i));
                damageValuesCopy.add(this.damageValues.get(i));
                damagetimeCopy.add(this.damagetime.get(i));
            }
        }

        this.damageAnimationsPoints = new Array<Vector2>();
        this.damageValues = new Array<Integer>();
        this.damagetime = new Array<Float>();
        animationsStage.getActors().removeAll(animationsStage.getActors(), false);

        if(damageAnimationsPointsCopy.size != 0) {
            for (int i = 0; i < damageAnimationsPointsCopy.size; i++) {
                Label label = new Label(""+damageValuesCopy.get(i)+"", StyleManager.generateLabelStyle(30, Color.RED));
                if(!animationsStage.getActors().contains(label, false)){
                    animationsStage.addActor(label);
                }
                label.setPosition(damageAnimationsPointsCopy.get(i).x, damageAnimationsPointsCopy.get(i).y);
                this.damageAnimationsPoints.add(damageAnimationsPointsCopy.get(i).add(new Vector2(0, 5)));
                this.damageValues.add(damageValuesCopy.get(i));
                this.damagetime.add(damagetimeCopy.get(i));
            }
        }

        animationsStage.act();
        animationsStage.draw();
    }
}
