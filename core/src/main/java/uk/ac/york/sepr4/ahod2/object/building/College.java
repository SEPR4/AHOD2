package ahod2.object.building;

import lombok.Data;
import ahod2.object.card.Card;

@Data
public class College {

    private String name;
    private Integer id, cardId, bossDifficulty;

    //set by buildingmanager on load
    private Card card;


    public College() {
        //empty constructor for JSON
    }
}
