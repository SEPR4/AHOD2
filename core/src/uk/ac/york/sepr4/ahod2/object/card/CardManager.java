package uk.ac.york.sepr4.ahod2.object.card;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import uk.ac.york.sepr4.ahod2.object.entity.Ship;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class CardManager {

    private List<Card> cards = new ArrayList<>();
    private List<Card> defaultCards = new ArrayList<>();

    public CardManager() {
        Json json = new Json();
        loadCards(json.fromJson(Array.class, Card.class, Gdx.files.internal("data/cards.json")));

        for(Card card: cards) {
            if(card.is_default()) {
                defaultCards.add(card);
            }
        }

        Gdx.app.log("CardManager", "Loaded "+cards.size()+" cards!");
    }

    private void loadCards(Array<Card> cards){
        for(Card card: cards) {
            FileHandle fileHandle = Gdx.files.internal("images/card/"+card.getTextureStr()+".png");
            if(!fileHandle.exists()) {
                Gdx.app.error("CardManager", "Texture not found for card: "+card.getId());
                continue;
            }
            if(!card.is_default() && card.getShopCost() == null) {
                Gdx.app.error("CardManager", "Not default but no shop cost for card: "+card.getId());
                continue;
            }
            card.setTexture(new TextureRegionDrawable(new TextureRegion((new Texture(fileHandle)))));
            this.cards.add(card);
        }
    }

    /***
     * Gets deck including default cards.
     * @param ship
     * @return
     */
    public List<Card> getFullDeck(Ship ship) {
        List<Card> deck = new ArrayList<>(ship.getDeck());
        deck.addAll(defaultCards);
        return deck;
    }

    public Optional<Card> randomCard(Integer power) {
        Random random = new Random();
        Card card = cards.get(random.nextInt(cards.size()-1));
        if(!card.is_default()&& card.getPower() <= power) {
            return Optional.of(card);
        }
        return Optional.empty();
    }

    public Optional<Card> getCardByID(Integer id) {
        for(Card card: cards) {
            if(card.getId().equals(id)) {
                return Optional.of(card);
            }
        }
        return Optional.empty();
    }

}
