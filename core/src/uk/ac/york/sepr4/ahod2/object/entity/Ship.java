package uk.ac.york.sepr4.ahod2.object.entity;

import com.badlogic.gdx.graphics.Texture;
import lombok.Data;
import uk.ac.york.sepr4.ahod2.io.FileManager;
import uk.ac.york.sepr4.ahod2.object.Card;
import uk.ac.york.sepr4.ahod2.object.building.College;

import java.util.ArrayList;
import java.util.List;

@Data
public class Ship {

    private Integer health = 100, maxHealth = 100;
    private College college;
    private Texture image = FileManager.defaultShipTexture;
    private List<Card> deck = new ArrayList<>();

    public Ship() {

    }

    public void addCard(Card card) {
        deck.add(card);
    }
}
