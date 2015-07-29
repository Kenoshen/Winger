package com.winger.libgdx.main.sprite;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.winger.draw.texture.CSprite;
import com.winger.draw.texture.CSpriteBatch;
import com.winger.input.delegate.CKeyboardEventHandler;
import com.winger.input.raw.CKeyboard;
import com.winger.input.raw.CMouse;
import com.winger.input.raw.state.ButtonState;
import com.winger.input.raw.state.KeyboardKey;
import com.winger.libgdx.main.physics.TestPhysics;
import com.winger.log.HTMLLogger;
import com.winger.log.LogGroup;
import com.winger.ui.Page;
import com.winger.ui.PageManager;
import com.winger.ui.delegate.PageEventHandler;

public class TestSprites implements CKeyboardEventHandler, PageEventHandler
{
    @SuppressWarnings("unused")
    private static final HTMLLogger log = HTMLLogger.getLogger(TestPhysics.class, LogGroup.Framework, LogGroup.Assert);
    CMouse mouse;
    CKeyboard keyboard;
    OrthographicCamera camera;
    Page ui;
    Map<String, CSprite> sprites = new HashMap<String, CSprite>();
    boolean allAnimationsToggle = false;
    CSpriteBatch sb = new CSpriteBatch();
    
    
    public TestSprites(Page ui)
    {
        this.ui = ui;
        //
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom = 0.1f;
        //
        keyboard = new CKeyboard();
        keyboard.subscribeToAllKeyboardEvents(this, ButtonState.DOWN);
        keyboard.subscribeToAllKeyboardEvents(this, ButtonState.UP);
        //
        sprites.put("blue", new CSprite("blue.stand", ui.getElementById("blue").getAbsoluteBoundingBox(), false));
        sprites.put("green", new CSprite("green.stand", ui.getElementById("green").getAbsoluteBoundingBox(), false));
        sprites.put("red", new CSprite("red.stand", ui.getElementById("red").getAbsoluteBoundingBox(), false));
        sprites.put("yellow", new CSprite("yellow.stand", ui.getElementById("yellow").getAbsoluteBoundingBox(), false));
        sprites.put("blueRun", new CSprite("blue.stand", ui.getElementById("blueRun").getAbsoluteBoundingBox(), true).setMillisecondsPerFrame(125)
            .setShouldLoop(true));
        sprites.put("greenRun", new CSprite("green.run", ui.getElementById("greenRun").getAbsoluteBoundingBox(), true).setMillisecondsPerFrame(50)
            .setShouldLoop(true));
        sprites.put("redRun", new CSprite("red.run", ui.getElementById("redRun").getAbsoluteBoundingBox(), true).setMillisecondsPerFrame(75)
            .setShouldLoop(true));
        sprites.put("yellowRun", new CSprite("yellow.run", ui.getElementById("yellowRun").getAbsoluteBoundingBox(), true)
            .setMillisecondsPerFrame(100).setShouldLoop(true));
        sprites.put("door",
            new CSprite("various.archDoor", ui.getElementById("door").getAbsoluteBoundingBox(), true).setRandomAnimation(true, 1, 1000)
                .setShouldLoop(true));
        sprites.put("door2",
            new CSprite("various.archDoor", ui.getElementById("door2").getAbsoluteBoundingBox(), true).setRandomAnimation(true, 1, 1000)
                .setShouldLoop(true));
        sprites.put("door3",
            new CSprite("various.archDoor", ui.getElementById("door3").getAbsoluteBoundingBox(), true).setRandomAnimation(true, 1, 1000)
                .setShouldLoop(true));
        sprites.put("door4",
            new CSprite("various.archDoor", ui.getElementById("door4").getAbsoluteBoundingBox(), true).setRandomAnimation(true, 1, 1000)
                .setShouldLoop(true));
        sprites.put("blueRun2", new CSprite("blue.run", ui.getElementById("blueRun2").getAbsoluteBoundingBox(), true).setMillisecondsPerFrame(125)
            .setShouldLoop(true).setReverse(true));
        sprites.put("greenRun2", new CSprite("green.run", ui.getElementById("greenRun2").getAbsoluteBoundingBox(), true).setMillisecondsPerFrame(50)
            .setShouldLoop(true).setReverse(true));
        sprites.put("redRun2", new CSprite("red.run", ui.getElementById("redRun2").getAbsoluteBoundingBox(), true).setMillisecondsPerFrame(75)
            .setShouldLoop(true).setReverse(true));
        sprites.put("yellowRun2",
            new CSprite("yellow.run", ui.getElementById("yellowRun2").getAbsoluteBoundingBox(), true).setMillisecondsPerFrame(100)
                .setShouldLoop(true).setReverse(true));
        sprites.put("rnd1", new CSprite("test.square, various.tribute, various.upFanOn1", ui.getElementById("rnd1").getAbsoluteBoundingBox(), true)
            .setRandomAnimation(true, 1, 1000).setShouldLoop(true));
        sprites.put("rnd2", new CSprite("various.teleporter1, various.scale3, various.rainbow", ui.getElementById("rnd2").getAbsoluteBoundingBox(),
            true).setRandomAnimation(true, 1, 1000).setShouldLoop(true));
        sprites.put("rnd3", new CSprite("various.devil1, various.buttonPress, various.lever1", ui.getElementById("rnd3").getAbsoluteBoundingBox(),
            true).setRandomAnimation(true, 1, 1000).setShouldLoop(true));
        sprites.put("rnd4", new CSprite("various.skull, weight switch.scale4, various.grass", ui.getElementById("rnd4").getAbsoluteBoundingBox(),
            true).setRandomAnimation(true, 1, 1000).setShouldLoop(true));
        //
        for (String key : sprites.keySet())
        {
            PageManager.instance().subscribeToEvent(this, ui.name, key);
        }
    }
    
    
    public void draw()
    {
        if (ui.isVisible)
        {
            keyboard.update();
            sb.begin();
            for (String key : sprites.keySet())
            {
                sprites.get(key).update();
                sprites.get(key).draw(sb);
            }
            sb.end();
        }
    }
    
    
    public void allAnimations()
    {
        for (String key : sprites.keySet())
        {
            sprites.get(key).setAnimating(allAnimationsToggle);
        }
        allAnimationsToggle = !allAnimationsToggle;
    }
    
    
    public void faceRight(boolean faceRight)
    {
        for (String key : sprites.keySet())
        {
            sprites.get(key).setFlipped(!faceRight);
        }
    }
    
    
    public void faceUp(boolean faceUp)
    {
        for (String key : sprites.keySet())
        {
            sprites.get(key).setFlopped(faceUp);
        }
    }
    
    
    @Override
    public void handleKeyEvent(CKeyboard keyboard, KeyboardKey key, ButtonState state)
    {
        if (key == KeyboardKey.SPACE && state == ButtonState.DOWN)
        {
            allAnimations();
        } else if (key == KeyboardKey.LEFT && state == ButtonState.DOWN)
        {
            faceRight(false);
        } else if (key == KeyboardKey.RIGHT && state == ButtonState.DOWN)
        {
            faceRight(true);
        } else if (key == KeyboardKey.UP && state == ButtonState.DOWN)
        {
            faceUp(true);
        } else if (key == KeyboardKey.DOWN && state == ButtonState.DOWN)
        {
            faceUp(false);
        }
    }
    
    
    @Override
    public void handleEvent(Object sender, String pageName, String eventName)
    {
        try
        {
            sprites.get(eventName).setAnimating(!sprites.get(eventName).isAnimating());
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
