package uk.ac.york.sepr4.ahod2;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import uk.ac.york.sepr4.ahod2.screen.MenuScreen;

public class AHOD2 extends Game {

	public static final boolean DEBUG = false;

	@Override
	public void create() {
		if(DEBUG) {
			//if debug, enable lower logging level and launch into game
			Gdx.app.setLogLevel(Application.LOG_DEBUG);
			GameInstance gameInstance = new GameInstance(this);
			gameInstance.start();
		} else {
			setScreen(new MenuScreen(this));
		}
	}
}
