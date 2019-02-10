package ahod2.object.entity;

import lombok.Data;
import ahod2.node.Node;
import ahod2.object.GameLevel;

import java.util.Optional;

@Data
public class Player {

    private Ship ship;
    private Optional<Node> location = Optional.empty();
    private Integer gold = 100;
    private GameLevel level;

    public Player(GameLevel gameLevel) {
        ship = new Ship();
        ship.setMana(1000);
        ship.setMaxMana(1000);
        this.level = gameLevel;
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