package uk.ac.york.sepr4.ahod2.io;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class StyleManager {

    private static final float baseScreenHeight=1080, baseScreenWidth=1920;

    public static final FreeTypeFontGenerator generator =
            new FreeTypeFontGenerator(Gdx.files.internal("font/PirataOne-Regular.ttf"));

    public static BitmapFont generatePirateFont(Integer size, Color color) {
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        parameter.color = color;

        return generator.generateFont(parameter);
    }

    public static Label.LabelStyle generateLabelStyle(Integer size, Color color) {
        return new Label.LabelStyle(generatePirateFont(size, color), color);
    }

    public static TextButton.TextButtonStyle generateTBStyle(Integer size, Color colorUp, Color colorDown) {
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = generatePirateFont(size, colorUp);
        style.fontColor = colorUp;
        style.downFontColor = colorDown;

        return style;
    }

    /***
     * Gets scale factor for texture/image depending on current viewport (screen) size
     * @return scaling factor
     */
    public static float getScalingFactor(){
        return (Gdx.graphics.getHeight())/baseScreenHeight;
    }

}
