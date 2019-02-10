package ahod2.object.building;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import lombok.Data;
import ahod2.GameInstance;
import ahod2.object.card.Card;

import java.util.Optional;

@Data
public class BuildingManager {

    private GameInstance gameInstance;

    private Array<College> colleges;
    private Array<Department> departments;

    public BuildingManager(GameInstance gameInstance) {
        this.gameInstance = gameInstance;

        Json json = new Json();
        loadColleges(json.fromJson(Array.class, College.class, Gdx.files.internal("data/colleges.json")));

        loadDepartments(json.fromJson(Array.class, Department.class, Gdx.files.internal("data/departments.json")));

        Gdx.app.log("BuildingManager", "Loaded "+colleges.size+" colleges!");
    }

    public Optional<College> getCollegeByID(Integer id) {
        for(College college: colleges) {
            if(college.getId()==id){
                return Optional.of(college);
            }
        }
        return Optional.empty();
    }

    public Optional<Department> getDepartmentByID(Integer id) {
        for(Department department: departments) {
            if(department.getId()==id){
                return Optional.of(department);
            }
        }
        return Optional.empty();
    }

    private void loadColleges(Array<College> colleges) {
        Array<College> finalColleges = new Array<>();
        for(College college : colleges) {
            Optional<Card> cardOptional = gameInstance.getCardManager().getCardByID(college.getCardId());
            if(cardOptional.isPresent()) {
                college.setCard(cardOptional.get());
                finalColleges.add(college);

            } else {
                Gdx.app.error("BuildingManager", "Card for "+college.getName()+" not found!");
            }
        }
        this.colleges = finalColleges;
    }

    private void loadDepartments(Array<Department> departments) {
        //TODO: check other params?
        for(Department department: departments) {
            for(Integer cardId : department.getStockArr()) {
                Optional<Card> cardOptional = gameInstance.getCardManager().getCardByID(cardId);
                if(cardOptional.isPresent()) {
                    department.addCard(cardOptional.get());
                } else {
                    Gdx.app.error("BuildingManager", "Card "+cardId +" not found for Dept: "+department.getId());
                }

            }
        }
        this.departments = departments;
    }



}