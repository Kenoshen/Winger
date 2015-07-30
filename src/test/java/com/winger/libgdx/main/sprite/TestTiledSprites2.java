package com.winger.libgdx.main.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.winger.draw.texture.CSprite;
import com.winger.draw.texture.CSpriteBatch;
import com.winger.input.raw.CKeyboard;
import com.winger.input.raw.state.KeyboardKey;
import com.winger.libgdx.main.physics.TestPhysics;
import com.winger.log.HTMLLogger;
import com.winger.log.LogGroup;
import com.winger.struct.CRectangle;
import com.winger.ui.Page;

public class TestTiledSprites2
{
    @SuppressWarnings("unused")
    private static final HTMLLogger log = HTMLLogger.getLogger(TestPhysics.class, LogGroup.Framework, LogGroup.Assert);
    OrthographicCamera camera;
    Page ui;
    CKeyboard keyboard;
    CSpriteBatch sb = new CSpriteBatch();
    String textureStr = "green.run1";
    CSprite sprite;
    float camMoveSpeed = 10;
    
    
    public TestTiledSprites2(Page ui)
    {
        this.ui = ui;
        keyboard = ui.keyboard;
        //
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        
        camera.translate(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, 0);
        camera.update();
        sb.setCamera(camera);
        
        sprite = new CSprite(textureStr, new CRectangle(800, 450, 500, 500), false);
        sprite.setOriginX(250);
        sprite.setOriginY(250);
        // sprite.setTileSize(110, 110);
        // sprite.setIsTiled(true);
        
        printPoints(sprite.getRect());
    }
    
    
    private void printPoints(CRectangle rect)
    {
        Vector2[] points = rect.points();
        for (int i = 0; i < points.length; i++)
        {
            System.out.println("( " + points[i].x + ", " + points[i].y + " )");
        }
    }
    
    
    public void draw()
    {
        if (ui.isVisible)
        {
            Vector3 moveCam = new Vector3(0, 0, 0);
            if (keyboard.isKeyBeingPressed(KeyboardKey.UP))
            {
                moveCam.y = 1 * camMoveSpeed;
            }
            if (keyboard.isKeyBeingPressed(KeyboardKey.DOWN))
            {
                moveCam.y = -1 * camMoveSpeed;
            }
            if (keyboard.isKeyBeingPressed(KeyboardKey.LEFT))
            {
                moveCam.x = -1 * camMoveSpeed;
            }
            if (keyboard.isKeyBeingPressed(KeyboardKey.RIGHT))
            {
                moveCam.x = 1 * camMoveSpeed;
            }
            camera.translate(moveCam);
            camera.update();
            sb.setProjectionMatrix(camera.combined);
            sb.begin();
            // printPoints(sb.window);
            sprite.draw(sb);
            sb.end();
        }
    }
    
    /*
     * 
     * [800.0:-50.0] [1300.0:-50.0] [1300.0:450.0] [800.0:450.0]
     * 
     * [300.0:299.00003] [1300.0:299.00003] [1300.0:599.0] [300.0:599.0]
     */
}
