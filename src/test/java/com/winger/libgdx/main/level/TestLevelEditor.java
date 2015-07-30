package com.winger.libgdx.main.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.winger.draw.texture.CSpriteBatch;
import com.winger.level.LevelEditor;
import com.winger.log.HTMLLogger;
import com.winger.log.LogGroup;

public class TestLevelEditor
{
    @SuppressWarnings("unused")
    private static final HTMLLogger log = HTMLLogger.getLogger(TestLevelEditor.class, LogGroup.Framework, LogGroup.Assert);
    
    public OrthographicCamera camera;
    public LevelEditor<TestTileType> levelEditor;
    public CSpriteBatch sb = new CSpriteBatch();
    
    
    public TestLevelEditor()
    {
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom = 0.1f;
        //
        levelEditor = new LevelEditor<TestTileType>(TestTileType.GROUND);
        // levelEditor.printTileTypes();
        
    }
    
    
    public void update(float delta)
    {
        levelEditor.update(delta);
    }
    
    
    public void draw()
    {
        levelEditor.draw();
    }
    
}
