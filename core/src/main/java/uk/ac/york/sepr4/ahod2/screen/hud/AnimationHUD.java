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

/***
 * Class used to load and update on-screen animation elements such as damage and heal splats.
 */
public class AnimationHUD {
    @Getter
    private Stage animationsStage;
    private GameInstance gameInstance;
    private List<Animation> animations = new ArrayList<>();

    public AnimationHUD(GameInstance gameInstance) {
        this.gameInstance = gameInstance;

        // Local widths and heights.
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        animationsStage = new Stage(new FitViewport(w, h, new OrthographicCamera()));
    }

    public void addDamageAnimation(Vector2 coords, Integer value, Float time){
        Label dmgLabel = new Label("-"+value, StyleManager.generateLabelStyle(45, Color.RED));
        dmgLabel.setPosition(coords.x, coords.y);
        animations.add(new Animation(dmgLabel, time));
    }
    public void addHealAnimation(Vector2 coords, Integer value, Float time){
        Label healLabel = new Label("+"+value, StyleManager.generateLabelStyle(45, Color.GREEN));
        healLabel.setPosition(coords.x, coords.y);
        animations.add(new Animation(healLabel, time));
    }

    public void update(float delta) {

        List<Animation> toRemove = new ArrayList<>();
        for(Animation animation : animations) {
            //if animation time is over, add to remove list
            if(animation.getTime() <= delta) {
                toRemove.add(animation);
                animationsStage.getActors().removeValue(animation.getActor(), false);
                continue;
            }
            if(!animationsStage.getActors().contains(animation.getActor(), false)) {
                animationsStage.addActor(animation.getActor());
            }
            animation.setTime(animation.getTime()-delta);
        }

        //remove finished animations
        animations.removeAll(toRemove);

        animationsStage.act();
        animationsStage.draw();
    }
}
