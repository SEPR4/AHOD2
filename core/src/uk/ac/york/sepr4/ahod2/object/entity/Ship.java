package uk.ac.york.sepr4.ahod2.object.entity;

import com.badlogic.gdx.graphics.Texture;
import lombok.Data;
import uk.ac.york.sepr4.ahod2.io.FileManager;
import uk.ac.york.sepr4.ahod2.object.card.Card;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
public class Ship {

    private String name;
    private boolean boss;
    private Integer health, maxHealth = 50, mana, maxMana = 10;
    private Texture image = FileManager.defaultShipTexture;
    private List<Card> deck = new ArrayList<>(), hand = new ArrayList<>(), discarded = new ArrayList<>();

    private List<Integer> delayedDamage = new ArrayList<>();

    public Ship() {
        image.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        this.mana = maxMana;
        this.health = maxHealth;
    }


    /***
     * Returns true if health drops to 0 or below
     * @param damage
     * @return
     */
    public boolean damage(Integer damage) {
        if(damage >= health){
            health = 0;
            return true;
        } else {
            health-=damage;
            return false;
        }

    }

    public void heal(Integer val) {
        if(health+val >= maxHealth) {
            health = maxHealth;
        } else {
            health+=val;
        }

    }

    public void battleOver() {
        hand = new ArrayList<>();
        discarded = new ArrayList<>();
        delayedDamage = new ArrayList<>();
        mana = maxMana;
    }

    public boolean applyDelayedDamage() {
        for(Integer dmgVal : delayedDamage) {
            if(damage(dmgVal)) {
                 return true;
            }
        }
        if(delayedDamage.size()>0) {
            delayedDamage.remove(0);
        }
        return false;
    }

    public void addDelayedDamage(Integer val, Integer turn) {
        if(delayedDamage.get(turn) != null) {
            delayedDamage.add(val, delayedDamage.get(turn) + val);
        } else {
            delayedDamage.add(turn, val);
        }
    }

    public void deductMana(Integer val) {
        mana -= val;
    }

    public void incMana(Integer val) {
        if(mana+val >= maxMana) {
            mana = maxMana;
        } else {
            mana += val;
        }
    }

    public void useCard(Card card) {
        hand.remove(card);
        discarded.add(card);
    }

    public void addCardToHand(Card card) {
        hand.add(card);
    }

    public void addCard(Card card) {
        deck.add(card);
    }
}
