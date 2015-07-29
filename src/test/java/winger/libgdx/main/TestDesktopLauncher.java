package com.winger.libgdx.main;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.winger.utils.GlobalClipboard;

public class TestDesktopLauncher
{
    public static void main(String[] arg)
    {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 1600;
        config.height = 900;
        System.setProperty("org.lwjgl.opengl.Display.allowSoftwareOpenGL", "true");
        TestGdxGame game = new TestGdxGame();
        Application app = new LwjglApplication(game, config);
        GlobalClipboard.instance().setClipboard(app.getClipboard());
    }
}
