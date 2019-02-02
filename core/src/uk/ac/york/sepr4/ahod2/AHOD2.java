package uk.ac.york.sepr4.ahod2;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import uk.ac.york.sepr4.ahod2.screen.MenuScreen;

public class AHOD2 extends Game {

	public static final boolean DEBUG = true;

	@Override
	public void create() {
		if(DEBUG) {
			Gdx.app.setLogLevel(Application.LOG_DEBUG);
		}
		setScreen(new MenuScreen(this));
	}
}
