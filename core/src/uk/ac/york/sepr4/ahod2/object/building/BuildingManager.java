package uk.ac.york.sepr4.ahod2.object.building;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import lombok.Data;
import uk.ac.york.sepr4.ahod2.AHOD2;
import uk.ac.york.sepr4.ahod2.GameInstance;
import uk.ac.york.sepr4.ahod2.object.Card;

import java.util.Optional;

@Data
public class BuildingManager {

    private GameInstance gameInstance;

    private Array<College> colleges;

    public BuildingManager(GameInstance gameInstance) {
        this.gameInstance = gameInstance;

        Json json = new Json();
        loadColleges(json.fromJson(Array.class, College.class, Gdx.files.internal("data/colleges.json")));

        Gdx.app.log("BuildingManager", "Loaded "+colleges.size+" colleges!");
    }

    private void loadColleges(Array<College> colleges) {
        Array<College> finalColleges = new Array<>();
        for(College college : colleges) {
            Optional<Card> cardOptional = gameInstance.getCardManager().getCardByID(college.getCardID());
            if(cardOptional.isPresent()) {
                college.setCard(cardOptional.get());
                finalColleges.add(college);

            } else {
                Gdx.app.error("BuildingManager", "Card for "+college.getName()+" not found!");
            }
        }
        this.colleges = finalColleges;
    }



}
