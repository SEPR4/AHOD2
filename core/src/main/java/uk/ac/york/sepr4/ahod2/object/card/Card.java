package uk.ac.york.sepr4.ahod2.object.card;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import lombok.Data;

@Data
public class Card {

    private Integer id, manaCost, shopCost, damage, damageSelf, moveTime, heal, power;
    private String name, desc, textureStr;
    private boolean _default;

    private Texture texture;

    public Card() {
        //load from json
    }

}
