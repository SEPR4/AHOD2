package uk.ac.york.sepr4.ahod2.object.building;

import lombok.Data;
import uk.ac.york.sepr4.ahod2.object.card.Card;

/***
 * Class used to represent instance of college.
 * Contains id, name and difficulty of level boss.
 */
@Data
public class College {

    private String name;
    private Integer id, bossDifficulty;

    public College() {
        //empty constructor for JSON
    }
}
