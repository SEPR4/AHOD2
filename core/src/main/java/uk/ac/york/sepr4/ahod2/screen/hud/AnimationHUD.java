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
    private List<Animation> animations = new ArrayList<Animation>();

    public AnimationHUD(GameInstance gameInstance) {
        this.gameInstance = gameInstance;

        // Local widths and heights.
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        animationsStage = new Stage(new FitViewport(w, h, new OrthographicCamera()));
    }

    public void addDamageAnimation(Vector2 coords, Integer value, Float time){
        this.animations.add(new Animation(Type.DAMAGE, coords, ""+value, time));
    }
    public void addHealAnimation(Vector2 coords, Integer value, Float time){
        this.animations.add(new Animation(Type.HEAL, coords, ""+value, time));
    }

    public void update(float delta) {
        List<Animation> animationsCopy = new ArrayList<Animation>();

        for(int i = 0; i<this.animations.size(); i++){
            this.animations.get(i).setTime(this.animations.get(i).getTime() - delta);
            if (this.animations.get(i).getTime() >= 0){
                animationsCopy.add(this.animations.get(i));
            }
        }

        this.animations = new ArrayList<Animation>();
        animationsStage.getActors().clear();

        if(animationsCopy.size() != 0) {
            for (int i = 0; i < animationsCopy.size(); i++) {
                if (animationsCopy.get(i).getAnimationType() == Type.DAMAGE){
                    Label label = new Label(animationsCopy.get(i).getText(), StyleManager.generateLabelStyle(30, Color.RED));
                    if(!animationsStage.getActors().contains(label, false)){
                        animationsStage.addActor(label);
                    }
                    label.setPosition(animationsCopy.get(i).getCurrentPoint().x, animationsCopy.get(i).getCurrentPoint().y);
                    animationsCopy.get(i).setCurrentPoint(animationsCopy.get(i).getCurrentPoint().add(new Vector2(0, 5)));//change
                    this.animations.add(animationsCopy.get(i));
                }
            }
        }

        animationsStage.act();
        animationsStage.draw();
    }
}
