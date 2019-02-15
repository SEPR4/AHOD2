package uk.ac.york.sepr4.ahod2.object.entity;

import com.badlogic.gdx.graphics.Texture;
import lombok.Data;
import lombok.Getter;
import uk.ac.york.sepr4.ahod2.io.FileManager;
import uk.ac.york.sepr4.ahod2.object.card.Card;

import java.util.*;

@Data
public class Ship {

    private String name;
    private boolean boss;
    private Integer health, maxHealth = 10, mana = 1,maxMana = 1;
    private Texture image;
    private List<Card> hand = new ArrayList<>(), deck = new ArrayList<>();

    private Deque<Card> playDeck = new ArrayDeque<>();

    @Getter
    private List<Integer> delayedDamage = new ArrayList<>(), delayedHeal = new ArrayList<>();

    public Ship() {
        this.health = maxHealth;

        try {
            Class.forName("uk.ac.york.sepr4.ahod2.io.FileManager");
            image = FileManager.defaultShipTexture;
            image.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        } catch (Exception ex) {
            //either not found or initializer exception (no gdx - for tests)
        } catch (Error error) {}
    }


    /***
     * Returns true if health drops to 0 or below
     * @param damage
     * @return
     */
    private boolean damage(Integer damage) {
        if(damage >= health){
            health = 0;
            return true;
        } else {
            health-=damage;
            return false;
        }

    }

    private void heal(Integer val) {
        if(health+val >= maxHealth) {
            health = maxHealth;
        } else {
            health+=val;
        }

    }

    public void battleStart(List<Card> defaultCards) {
        resetBattleParams();
        shuffleReset(defaultCards);
    }

    public void shuffleReset(List<Card> defaultCards) {
        Collections.shuffle(defaultCards);
        Collections.shuffle(deck);
        defaultCards.forEach(card -> playDeck.add(card));
        deck.forEach(card -> playDeck.add(card));
    }

    public void setMaxMana(Integer lMana) {
        if(lMana > 10) {
            maxMana = 10;
        } else {
            maxMana = lMana;
        }
    }

    public void resetBattleParams() {
        hand = new ArrayList<>();
        playDeck = new ArrayDeque<>();
        delayedDamage = new ArrayList<>();
        maxMana = 1;
        mana = 1;
    }

    public boolean applyDelayedDamage() {
        if(delayedDamage.size()>0) {
            if(damage(delayedDamage.get(0))) {
                return true;
            }
            delayedDamage.remove(0);
        }
        return false;
    }


    public void applyDelayedHeal() {
        if(delayedHeal.size()>0) {
            heal(delayedHeal.get(0));
            delayedHeal.remove(0);
        }
    }

    public void addHeal(Integer val, Integer turn) {
        if(delayedHeal.size() > turn) {
            delayedHeal.set(turn, delayedHeal.get(turn) + val);
        } else {
            delayedHeal.add(turn, val);
        }
    }

    public void addDamage(Integer val, Integer turn) {
        if(delayedDamage.size() > turn) {
            delayedDamage.set(turn, delayedDamage.get(turn) + val);
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
    }

    public void addCardToHand(Card card) {
        hand.add(card);
    }

    public void addCard(Card card) {
        deck.add(card);
    }
}
