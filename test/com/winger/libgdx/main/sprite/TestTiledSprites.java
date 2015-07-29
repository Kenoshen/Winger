package com.winger.libgdx.main.sprite;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.winger.draw.texture.CSprite;
import com.winger.draw.texture.CSpriteBatch;
import com.winger.libgdx.main.physics.TestPhysics;
import com.winger.log.HTMLLogger;
import com.winger.log.LogGroup;
import com.winger.struct.CRectangle;
import com.winger.struct.Tups;
import com.winger.struct.Tups.Tup2;
import com.winger.ui.Page;

public class TestTiledSprites
{
    @SuppressWarnings("unused")
    private static final HTMLLogger log = HTMLLogger.getLogger(TestPhysics.class, LogGroup.Framework, LogGroup.Assert);
    OrthographicCamera camera;
    Page ui;
    Map<String, Tup2<CSprite, CRectangle>> sprites = new HashMap<String, Tup2<CSprite, CRectangle>>();
    CSpriteBatch sb = new CSpriteBatch();
    String textureStr = "green.run1";
    
    
    public TestTiledSprites(Page ui)
    {
        this.ui = ui;
        //
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        
        camera.translate(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, 0);
        camera.update();
        sb.setCamera(camera);
        //
        String[] ids = new String[] { "tl", "t", "tr", "l", "c", "r", "bl", "b", "br" };
        //
        for (String id : ids)
        {
            CRectangle rect = ui.getElementById(id).getAbsoluteBoundingBox().clone();
            CSprite sprite = new CSprite(textureStr, rect.clone(), false);
            sprites.put(id, Tups.tup2(sprite, rect));
            rect.setOriginAtCenter();
        }
        //
        for (String key : sprites.keySet())
        {
            Tup2<CSprite, CRectangle> t = sprites.get(key);
            CSprite s = t.i1();
            // s.setTileSize(70, 70);
            s.setRotation(10);
            Vector2 o = new Vector2(0, 0);
            if (key.equals("tl"))
            {
                o.x = 0;
                o.y = 1;
            } else if (key.equals("t"))
            {
                o.x = 0.5f;
                o.y = 1;
            } else if (key.equals("tr"))
            {
                o.x = 1;
                o.y = 1;
            } else if (key.equals("l"))
            {
                o.x = 0;
                o.y = 0.5f;
            } else if (key.equals("c"))
            {
                o.x = 0.5f;
                o.y = 0.5f;
            } else if (key.equals("r"))
            {
                o.x = 1;
                o.y = 0.5f;
            } else if (key.equals("bl"))
            {
                o.x = 0;
                o.y = 0;
            } else if (key.equals("b"))
            {
                o.x = 0.5f;
                o.y = 0;
            } else if (key.equals("br"))
            {
                o.x = 1;
                o.y = 0;
            }
            o.x *= s.getWidth();
            o.y *= s.getHeight();
            s.setOriginX(o.x);
            s.setOriginY(o.y);
            o = o.sub(new Vector2(0.5f * s.getWidth(), 0.5f * s.getHeight()));
            s.setX(s.getX() + o.x);
            s.setY(s.getY() + o.y);
            // s.setIsTiled(true);
        }
    }
    
    
    public void draw()
    {
        if (ui.isVisible)
        {
            camera.update();
            sb.setProjectionMatrix(camera.combined);
            sb.begin();
            for (String key : sprites.keySet())
            {
                CSprite s = sprites.get(key).i1();
                s.update();
                s.draw(sb);
                sb.drawRectangle(ShapeType.Line, s.getRect(), Color.WHITE);
            }
            sb.end();
        }
    }
}
