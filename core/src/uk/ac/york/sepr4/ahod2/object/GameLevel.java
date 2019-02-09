package uk.ac.york.sepr4.ahod2.object;

import com.badlogic.gdx.Gdx;
import lombok.Data;
import uk.ac.york.sepr4.ahod2.GameInstance;
import uk.ac.york.sepr4.ahod2.object.building.BuildingManager;
import uk.ac.york.sepr4.ahod2.object.building.College;
import uk.ac.york.sepr4.ahod2.object.building.Department;
import uk.ac.york.sepr4.ahod2.screen.sail.NodeView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class GameLevel {

    private Integer id, levelGold, battleGold, collegeId, difficulty, depth;
    private List<Integer> departmentIds;

    //populated when loaded by gameinstance
    private College college;
    private List<Department> departments = new ArrayList<>();
    private NodeView nodeView;


    public GameLevel() {
        //json
    }

    public boolean load(GameInstance gameInstance) {
        BuildingManager buildingManager = gameInstance.getBuildingManager();
        Optional<College> optCollege = buildingManager.getCollegeByID(collegeId);
        if(!optCollege.isPresent()) {
            Gdx.app.error("GameLevel", "College with ID specified doesnt exist for GameLevel "+id);
            return false;
        }
        college = optCollege.get();
        for(Integer departmentId : departmentIds) {
            Optional<Department> optDept = buildingManager.getDepartmentByID(departmentId);
            if(!optDept.isPresent()) {
                Gdx.app.error("GameLevel", "Dept with ID:"+departmentId+" specified doesnt exist for GameLevel "+id);
                return false;
            }
            departments.add(optDept.get());
        }
        if(nodeView == null) {
            nodeView = new NodeView(gameInstance.getSailScreen(), this);
        }
        Gdx.app.debug("GameLevel", "Successfully loaded level "+id);
        return true;
    }



}
