package com.winger.libgdx.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.winger.Winger;
import com.winger.draw.texture.CSpriteBatch;
import com.winger.input.raw.CGamePad;
import com.winger.libgdx.main.input.TestCGamePad;
import com.winger.libgdx.main.input.TestFPSCalculator;
import com.winger.libgdx.main.input.TestKeyboardInput;
import com.winger.libgdx.main.level.TestLevelEditor;
import com.winger.libgdx.main.physics.TestPhysics;
import com.winger.libgdx.main.sprite.*;
import com.winger.log.HTMLLogger;
import com.winger.log.LogGroup;
import com.winger.log.LogLevel;
import com.winger.physics.CWorld;
import com.winger.stats.FPSCalculator;
import com.winger.struct.TestCRectangle;
import com.winger.struct.TestCRectangle2;
import com.winger.struct.TestCRectangle3;
import com.winger.ui.PageManager;
import com.winger.ui.delegate.PageEventHandler;

public class TestGdxGame extends ApplicationAdapter implements PageEventHandler
{
    @SuppressWarnings("unused")
    private static final HTMLLogger log = HTMLLogger.getLogger(CGamePad.class, LogGroup.Framework, LogGroup.System);
    //
    CSpriteBatch batch;
    OrthographicCamera camera;
    FPSCalculator fps;
    //
    TestCGamePad testGamePad;
    TestPhysics testPhysics;
    TestLevelEditor testLevelEditor;
    TestSprites testSprites;
    TestAnimator testAnimator;
    TestShapeRenderer testShapeRenderer;
    TestKeyboardInput testKeyboardInput;
    TestTiledSprites testTiledSprites;
    TestTiledSprites2 testTiledSprites2;
    TestFPSCalculator testFPSCalculator;
    TestCRectangle testCRectangle;
    TestCRectangle2 testCRectangle2;
    TestCRectangle3 testCRectangle3;
    
    
    @Override
    public void create()
    {
        super.create();
        HTMLLogger.setGlobalLogLevel(LogLevel.Info);
        //
        batch = new CSpriteBatch();
        //
        testGamePad = new TestCGamePad();
        testGamePad.initGamePad();
        // testGamePad.subscribeToGamePadButtons(ButtonState.DOWN);
        // testGamePad.subscribeToGamePadSticks();
        // testGamePad.subscribeToGamePadTriggers();
        //
        Winger.texture.loadTexturesInDirectory("src/test/resources/img");
        Winger.texture.loadTextureAtlas("src/test/resources/atlas", "testAtlas");
        Winger.texture.loadTextureAtlas("src/test/resources/atlas", "buttons");
        Winger.texture.loadTextureAtlas("src/test/resources/atlas/libGdx-atlas", "images");
        PageManager.instance().addPagesInDirectory("src/test/resources/test");
        PageManager.instance().transitionToPage("main");
        PageManager.instance().setUISpriteBatch(batch);
        //
        testPhysics = new TestPhysics(PageManager.instance().getPage("test-physics"));
        //
        testSprites = new TestSprites(PageManager.instance().getPage("test-sprites"));
        //
        testAnimator = new TestAnimator(PageManager.instance().getPage("test-animator"));
        //
        testShapeRenderer = new TestShapeRenderer(PageManager.instance().getPage("test-shape-renderer"));
        //
        testKeyboardInput = new TestKeyboardInput(Winger.ui.getPage("test-keyboard"));
        //
        testTiledSprites = new TestTiledSprites(Winger.ui.getPage("test-tiles"));
        testTiledSprites2 = new TestTiledSprites2(Winger.ui.getPage("test-tiles2"));
        //
        testFPSCalculator = new TestFPSCalculator(Winger.ui.getPage("test-fps"));
        //
        testCRectangle = new TestCRectangle(Winger.ui.getPage("test-rect"));
        testCRectangle2 = new TestCRectangle2(Winger.ui.getPage("test-rect2"));
        testCRectangle3 = new TestCRectangle3(Winger.ui.getPage("test-rect3"));
        //
        testLevelEditor = new TestLevelEditor();
        //
        fps = new FPSCalculator();
        fps.framesToTrack(120);
        fps.color(Color.WHITE.cpy());
    }
    
    
    @Override
    public void render()
    {
        super.render();
        fps.update();
        PageManager.instance().update();
        testGamePad.update();
        testPhysics.update(1f / 60f);
        testLevelEditor.update(1 / 60f);
        testAnimator.update(1 / 60f);
        testKeyboardInput.update();
        testFPSCalculator.update();
        
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        testPhysics.draw();
        testLevelEditor.draw();
        testSprites.draw();
        testAnimator.draw();
        testShapeRenderer.draw();
        testTiledSprites.draw();
        testTiledSprites2.draw();
        testFPSCalculator.draw();
        testCRectangle.draw();
        testCRectangle2.draw();
        testCRectangle3.draw();
        
        // draw the UI
        batch.begin();
        PageManager.instance().draw();
        fps.displayFps(batch, 20, Gdx.graphics.getHeight() - 20);
        batch.end();
    }
    
    
    @Override
    public void pause()
    {
        super.pause();
    }
    
    
    @Override
    public void resume()
    {
        super.resume();
    }
    
    
    @Override
    public void resize(int width, int height)
    {
        super.resize(width, height);
    }
    
    
    @Override
    public void dispose()
    {
        CWorld.world._world.dispose();
        super.dispose();
    }
    
    
    @Override
    public void handleEvent(Object sender, String pageName, String eventName)
    {
        if ("main".equals(pageName) && "refresh".equals(eventName))
        {
            // try
            // {
            // PageManager.instance().restartpage("test-drag");
            // } catch (IOException e)
            // {
            // e.printStackTrace();
            // }
        } else if ("test-physics".equals(pageName) && "startPhysics".equals(eventName))
        {   
            
        }
    }
}
