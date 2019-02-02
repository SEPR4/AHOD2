package uk.ac.york.sepr4.ahod2.io;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class FileManager {

    public static Texture defaultShipTexture = new Texture(Gdx.files.internal("ships/default.png"));
    public static Texture sailScreenBG = new Texture(Gdx.files.internal("images/bg.png"));
    public static Texture nodeIcon = new Texture(Gdx.files.internal("images/node_icon.png"));

    public static Texture hudGold = new Texture(Gdx.files.internal("images/hud/gold.png"));
    public static Texture hudSupplies = new Texture(Gdx.files.internal("images/hud/supplies.png"));



}
