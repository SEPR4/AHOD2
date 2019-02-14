package uk.ac.york.sepr4.ahod2.util;

import com.badlogic.gdx.Gdx;
import uk.ac.york.sepr4.ahod2.object.card.Card;
import uk.ac.york.sepr4.ahod2.object.card.CardManager;
import uk.ac.york.sepr4.ahod2.object.entity.Ship;
import uk.ac.york.sepr4.ahod2.screen.BattleScreen;

import java.util.Comparator;
import java.util.List;

public class BattleAI {

    public static void chooseMove(BattleScreen battleScreen) {
        //choose the best damage+self heal/mana ratio card
        //if no cards, then draw
        //if cards but not enough mana for best option then end turn unless another card that kills is available
        CardManager cardManager = battleScreen.getGameInstance().getCardManager();
        Gdx.app.debug("BattleAI", "Enemy Move!");
        Ship enemy = battleScreen.getEnemy();
        List<Card> hand = enemy.getHand();
        if(hand.size() > 0) {
            if(enemy.getHealth() > enemy.getMaxHealth()/2) {
                //more than half health - attack
                hand.sort(new SortByDamageRatio());
            } else {
                //heal
                hand.sort(new SortByHealRatio());
            }
            for(Card card : hand) {
                if(enemy.getMana() >= card.getManaCost()
                && !(enemy.getHealth() +card.getHeal() >= enemy.getMaxHealth())) {
                    battleScreen.useCard(enemy, battleScreen.getPlayer().getShip(), card);
                    return;
                }
            }
        } else if(hand.size() >= battleScreen.getHandSize()) {
            battleScreen.endTurn();
            return;
        }
        //no cards or not enough mana
        if(!battleScreen.drawCard(enemy)) {
            battleScreen.endTurn();
        }
        return;
    }

}

class SortByDamageRatio implements Comparator<Card> {
    public int compare(Card card1, Card card2) {
        return ((card1.getDamage()+card1.getDamage()*card1.getMoveTime())/card1.getManaCost())
                - ((card2.getDamage()+card2.getDamage()*card2.getMoveTime())/card2.getManaCost());
    }
}

class SortByHealRatio implements Comparator<Card> {
    public int compare(Card card1, Card card2) {
        return ((card1.getHeal()+card1.getHeal()*card1.getMoveTime())/card1.getManaCost())
                - ((card2.getHeal()+card2.getHeal()*card2.getMoveTime())/card2.getManaCost());
    }
}
