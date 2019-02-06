package uk.ac.york.sepr4.ahod2.object.entity;

import lombok.Data;
import uk.ac.york.sepr4.ahod2.node.Node;
import uk.ac.york.sepr4.ahod2.object.GameLevel;

import java.util.Optional;

@Data
public class Player {

    private Ship ship;
    private Optional<Node> location = Optional.empty();
    private Integer supplies = 10, gold = 100, score = 0;
    private GameLevel level;

    public Player(GameLevel gameLevel) {
        ship = new Ship();
        this.level = gameLevel;
    }

    public void addGold(Integer gold) {
        this.gold+=gold;
    }
    public void addSupplies(Integer supplies) {
        this.supplies+=supplies;
    }

}
