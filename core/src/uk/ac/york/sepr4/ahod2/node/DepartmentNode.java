package uk.ac.york.sepr4.ahod2.node;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import lombok.Data;
import uk.ac.york.sepr4.ahod2.io.FileManager;
import uk.ac.york.sepr4.ahod2.object.building.Department;

@Data
public class DepartmentNode extends Node {

    private Department department;

    public DepartmentNode(Node node, Department department) {
        super(node.getId(), node.getRow(), node.getCol());
        this.department = department;

        setConnected(node.getConnected());
        this.setTexture(new TextureRegionDrawable(new TextureRegion(FileManager.departmentNodeIcon)));
    }
}
