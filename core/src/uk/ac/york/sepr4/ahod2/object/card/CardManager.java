package uk.ac.york.sepr4.ahod2.object.card;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class CardManager {

    private Array<Card> cards;
    private List<Card> defaultCards = new ArrayList<>();

    public CardManager() {
        Json json = new Json();
        cards = json.fromJson(Array.class, Card.class, Gdx.files.internal("data/cards.json"));

        for(Card card: cards) {
            if(card.is_default()) {
                defaultCards.add(card);
            }
        }

        Gdx.app.log("CardManager", "Loaded "+cards.size+" cards!");
    }

    public Optional<Card> randomCard(Integer power) {
        Random random = new Random();
        Card card = cards.get(random.nextInt(cards.size-1));
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
