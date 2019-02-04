package uk.ac.york.sepr4.ahod2.screen.sail;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import lombok.Getter;
import uk.ac.york.sepr4.ahod2.io.FileManager;
import uk.ac.york.sepr4.ahod2.node.Node;
import uk.ac.york.sepr4.ahod2.object.GameLevel;
import uk.ac.york.sepr4.ahod2.object.entity.Player;
import uk.ac.york.sepr4.ahod2.util.NodeUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class NodeView {

    private SailScreen sailScreen;

    private static final Texture nodeIcon = FileManager.nodeIcon;
    @Getter
    private static final float vertSpacing = 250f;

    private HashMap<Integer, ImageButton> nodeButtons = new HashMap<>();

    private ShapeRenderer shapeRenderer = new ShapeRenderer();

    private GameLevel gameLevel;

    private List<Node> nodeMap;

    @Getter
    private float height = 0;

    public NodeView(SailScreen sailScreen, GameLevel gameLevel) {
        this.sailScreen = sailScreen;
        this.gameLevel = gameLevel;

        createNodeMap();
    }

    public void update() {
        Gdx.gl.glLineWidth(2);
        shapeRenderer.setProjectionMatrix(sailScreen.getBatch().getProjectionMatrix());
        drawConnections();
        drawPlayerLocation();
    }

    //adds actors to the stage - add once
    private void createNodeMap() {
        nodeMap = NodeUtil.generateRandomNodeMap(gameLevel);
        for(int i=0; i<nodeMap.size(); i++) {
            List<Node> row = getRow(nodeMap, i);
            float spacing = getNodeSpacings(row.size());
            for(Node node : row) {
                ImageButton btn = new ImageButton(node.getTexture());
                //TODO: Works but cleanup
                Vector2 pos = new Vector2(((node.getCol()+1)*spacing+(nodeIcon.getWidth()*node.getCol())),((node.getRow()+1)*vertSpacing));
                btn.setPosition(pos.x, pos.y);
                if(pos.y > height) {
                    height = pos.y;
                }
                btn.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        sailScreen.nodeClick(node);
                    }
                });
                nodeButtons.put(node.getId(), btn);
            }
        }

        nodeButtons.values().forEach(btn -> {
            sailScreen.getStage().addActor(btn);
        });
    }

    private void drawPlayerLocation() {

        Player player = sailScreen.getGameInstance().getPlayer();
        Optional<Node> node = player.getLocation();
        if(node.isPresent()) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            ImageButton imageButton = nodeButtons.get(node.get().getId());
            Vector2 loc = getNodeButtonCenter(imageButton);
            shapeRenderer.circle(loc.x, loc.y, imageButton.getWidth() / 2);
            shapeRenderer.end();
        }
    }

    //draw lines with shape renderer - must be done on render
    private void drawConnections() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        nodeButtons.keySet().forEach(nodeId -> {
            Node node = nodeMap.get(nodeId);
            node.getConnected().forEach(connection -> {
                ImageButton source = nodeButtons.get(nodeId);
                ImageButton target = nodeButtons.get(connection.getId());
                shapeRenderer.rectLine(getNodeButtonCenter(source), getNodeButtonCenter(target), 3);
            });
        });
        shapeRenderer.end();
    }

    private Vector2 getNodeButtonCenter(ImageButton imageButton) {
        float x = imageButton.getX(), y = imageButton.getY();
        x+=imageButton.getWidth()/2;
        y+=imageButton.getHeight()/2;
        return new Vector2(x,y);
    }


    //TODO: Not efficient
    private List<Node> getRow(List<Node> nodes, Integer id) {
        List<Node> row = new ArrayList<>();
        for(Node node:nodes){
            if(node.getRow() == id){
                row.add(node);
            }
        }
        return row;
    }

    private float getNodeSpacings(Integer no) {
        float w = Gdx.graphics.getWidth();
        return (w-(no*nodeIcon.getWidth()))/(no+1);
    }

}
