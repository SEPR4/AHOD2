package uk.ac.york.sepr4.ahod2.object.entity;

import lombok.Data;
import uk.ac.york.sepr4.ahod2.node.Node;

import java.util.Optional;

@Data
public class Player {

    private Ship ship;
    private Optional<Node> location = Optional.empty();
    private Integer supplies = 0, gold = 0, score = 0;

    public Player() {
        ship = new Ship();
    }

    public void addGold(Integer gold) {
        this.gold+=gold;
    }
    public void addSupplies(Integer supplies) {
        this.supplies+=supplies;
    }

}
