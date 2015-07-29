package com.winger.libgdx.main.sprite;

import java.util.HashMap;
import java.util.Map;

import com.winger.draw.texture.CSprite;
import com.winger.draw.texture.CSpriteBatch;
import com.winger.libgdx.main.physics.TestPhysics;
import com.winger.log.HTMLLogger;
import com.winger.log.LogGroup;
import com.winger.ui.Page;

public class TestSortedRendering
{
    @SuppressWarnings("unused")
    private static final HTMLLogger log = HTMLLogger.getLogger(TestPhysics.class, LogGroup.Framework, LogGroup.Assert);
    Page ui;
    Map<String, CSprite> sprites = new HashMap<String, CSprite>();
    CSpriteBatch sb = new CSpriteBatch();
    
    
    public TestSortedRendering(Page ui)
    {
        this.ui = ui;
        //
        // sprites.put("blue", new CSprite("blue.stand", ui.getElementById("blue").getAbsoluteBoundingBox(), false));
        // sprites.put("green", new CSprite("green.stand", ui.getElementById("green").getAbsoluteBoundingBox(), false));
        // sprites.put("red", new CSprite("red.stand", ui.getElementById("red").getAbsoluteBoundingBox(), false));
        // sprites.put("yellow", new CSprite("yellow.stand", ui.getElementById("yellow").getAbsoluteBoundingBox(), false));
    }
    
    
    public void draw()
    {
        if (ui.isVisible)
        {
            sb.begin();
            for (String key : sprites.keySet())
            {
                sprites.get(key).update();
                sprites.get(key).draw(sb);
            }
            sb.end();
        }
    }
}
