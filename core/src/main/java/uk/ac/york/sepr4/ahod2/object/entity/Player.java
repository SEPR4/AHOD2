package uk.ac.york.sepr4.ahod2.object.entity;

import lombok.Data;
import uk.ac.york.sepr4.ahod2.node.Node;
import uk.ac.york.sepr4.ahod2.object.GameLevel;

import java.util.Optional;

@Data
public class Player {

    private Ship ship;
    private Optional<Node> location = Optional.empty();
    private Integer gold = 100;
    private GameLevel level;

    public Player(GameLevel gameLevel) {
        ship = new Ship();
        this.level = gameLevel;
        ship.setMaxHealth(10);
        ship.setHealth(10);
    }

    public Integer getScore(){
        return 0;
    }

    public void takeGold(Integer gold) {
        this.gold-=gold;
    }
    public void addGold(Integer gold) {
        this.gold+=gold;
    }

}
