package uk.ac.york.sepr4.ahod2.io;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class FontManager {

    public static final FreeTypeFontGenerator generator =
            new FreeTypeFontGenerator(Gdx.files.internal("font/PirataOne-Regular.ttf"));

    public static BitmapFont generatePirateFont(Integer size, Color color) {
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        parameter.color = color;

        return generator.generateFont(parameter);
    }

}
