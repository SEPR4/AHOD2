package uk.ac.york.sepr4.ahod2.io;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import uk.ac.york.sepr4.ahod2.screen.sail.SailScreen;

public class SailInputProcessor implements InputProcessor {

    private SailScreen sailScreen;

    private boolean up, down;
    private float scrollAmount = 10f;

    public SailInputProcessor(SailScreen sailScreen) {
        this.sailScreen = sailScreen;
    }

    //TODO: Dynamic heights
    public void scrollCamera() {
        float cameraY = (sailScreen.getCameraLowerBound().y);
        if(up) {
            if(cameraY + scrollAmount + sailScreen.getStage().getHeight() <
                    sailScreen.getGameInstance().getPlayer().getLevel().getNodeView().getHeight() + 250f) {
                sailScreen.getOrthographicCamera().translate(0, scrollAmount);
            }
        }
        if(down) {
            if(cameraY - scrollAmount > 0) {
                sailScreen.getOrthographicCamera().translate(0, -scrollAmount);
            }
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.DOWN) {
            up = false;
            down = true;
        }
        if(keycode == Input.Keys.UP) {
            down = false;
            up = true;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(keycode == Input.Keys.DOWN) {
            down = false;
        }
        if(keycode == Input.Keys.UP) {
            up = false;
        }
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
