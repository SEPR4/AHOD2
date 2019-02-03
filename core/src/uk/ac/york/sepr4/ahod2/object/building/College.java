package uk.ac.york.sepr4.ahod2.object.building;

import lombok.Data;
import uk.ac.york.sepr4.ahod2.object.card.Card;

@Data
public class College {

    private String name;
    private Integer id, cardId;

    //set by buildingmanager on load
    private Card card;


    public College() {
        //empty constructor for JSON
    }
}
