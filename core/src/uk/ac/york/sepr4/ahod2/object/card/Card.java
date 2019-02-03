package uk.ac.york.sepr4.ahod2.object;

import lombok.Data;

@Data
public class Card {

    private Integer id, manaCost, shopCost, damage, heal, power;
    private String name, desc;
    private boolean _default;

    public Card() {
        //load from json
    }

}
