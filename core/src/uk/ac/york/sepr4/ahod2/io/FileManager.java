package uk.ac.york.sepr4.ahod2.io;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class FileManager {

    public static Texture defaultShipTexture = new Texture(Gdx.files.internal("ships/default.png"));
    public static Texture sailScreenBG = new Texture(Gdx.files.internal("images/bg.png"));
    public static Texture encounterScreenBG = new Texture(Gdx.files.internal("images/encounter.png"));

    public static Texture nodeIcon = new Texture(Gdx.files.internal("images/node/default.png"));
    public static Texture battleNodeIcon = new Texture(Gdx.files.internal("images/node/battle.png"));
    public static Texture departmentNodeIcon = new Texture(Gdx.files.internal("images/node/department.png"));


    public static Texture hudGold = new Texture(Gdx.files.internal("images/hud/gold.png"));
    public static Texture hudSupplies = new Texture(Gdx.files.internal("images/hud/supplies.png"));



}
