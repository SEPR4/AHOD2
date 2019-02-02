package uk.ac.york.sepr4.ahod2.object;

import com.badlogic.gdx.Gdx;
import uk.ac.york.sepr4.ahod2.GameInstance;
import uk.ac.york.sepr4.ahod2.object.entity.Ship;

import java.util.Optional;
import java.util.Random;

public class ShipFactory {

    //difficulty 0-100
    public static Ship generateEnemyShip(Integer difficulty) {
        Random random = new Random();
        Ship ship = new Ship();

        ship.setMaxHealth(10+random.nextInt(difficulty));
        ship.setHealth(ship.getMaxHealth());

        Integer noCards = random.nextInt(difficulty/10);
        while(noCards > 0) {
            Optional<Card> optional = GameInstance.INSTANCE.getCardManager().randomCard(difficulty);
            if(optional.isPresent()) {
                ship.addCard(optional.get());
                noCards--;
            } else {
                Gdx.app.debug("ShipFactory", "Couldn't get valid card!");
            }
        }
        return ship;
    }

   // public static Ship generateCollegeBoss(College college, Integer difficulty) { }

}
