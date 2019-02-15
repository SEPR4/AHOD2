package uk.ac.york.sepr4.ahod2.object.building;

import lombok.Data;


@Data
public class Department {

    private Integer id, repairCost, minigamePower;
    private String name;

    public Department() {
        //json
    }

}
