package uk.ac.york.sepr4.ahod2.object.building;

import lombok.Data;
import uk.ac.york.sepr4.ahod2.object.card.Card;

import java.util.List;

@Data
public class Department {

    private Integer id, repairCost;
    private String name;

    private List<Card> stock;

    public Department() {
        //json
    }
}
