package uk.ac.york.sepr4.ahod2.object.building;

import lombok.Data;
import uk.ac.york.sepr4.ahod2.object.card.Card;

import java.util.ArrayList;
import java.util.List;

@Data
public class Department {

    private Integer id, repairCost, minigamePower;
    private String name;
    private List<Integer> stockArr;

    private List<Card> stock = new ArrayList<>();

    public Department() {
        //json
    }

    public void addCard(Card card) {
        stock.add(card);
    }
}
