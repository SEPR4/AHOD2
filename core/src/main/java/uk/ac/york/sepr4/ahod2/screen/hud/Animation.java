package uk.ac.york.sepr4.ahod2.screen.hud;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import lombok.Data;

@Data
public class Animation {
    private Type animationType;
    private Vector2 startingPoint;
    private Vector2 currentPoint;
    private Vector2 endPoint;
    private String text;
    private Float time;
    private Texture texture;

     public Animation(Type animationType, Vector2 startingPoint, Vector2 endPoint, Float time, Texture texture){
        this.animationType = animationType;
        this.startingPoint = startingPoint;
        this.currentPoint = startingPoint;
        this.endPoint = endPoint;
        this.time = time;
        this.texture = texture;

    }

    public Animation(Type animationType, Vector2 startingPoint, String text, Float time){
        this.animationType = animationType;
        this.startingPoint = startingPoint;
        this.currentPoint = startingPoint;
        this.text = text;
        this.time = time;
    }


}

enum Type{
    DAMAGE, HEAL, EFFECT
        }