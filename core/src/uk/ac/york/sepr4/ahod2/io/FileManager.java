package uk.ac.york.sepr4.ahod2.io;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class FileManager {

    public static Texture defaultShipTexture = new Texture(Gdx.files.internal("ships/default.png"));

    public static Texture sailScreenBG = new Texture(Gdx.files.internal("images/screen/sail.png"));
    public static Texture menuScreenBG = new Texture(Gdx.files.internal("images/screen/menu.png"));
    public static Texture battleScreenBG = new Texture(Gdx.files.internal("images/screen/battle.png"));

    public static Texture nodeIcon = new Texture(Gdx.files.internal("images/node/default.png"));
    public static Texture battleNodeIcon = new Texture(Gdx.files.internal("images/node/battle.png"));
    public static Texture departmentNodeIcon = new Texture(Gdx.files.internal("images/node/department.png"));


    public static Texture hudGold = new Texture(Gdx.files.internal("images/hud/gold.png"));
    public static Texture hudShipView = new Texture(Gdx.files.internal("images/hud/shipview.png"));



}
