package com.winger.struct;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.winger.draw.texture.CSpriteBatch;
import com.winger.libgdx.main.physics.TestPhysics;
import com.winger.log.HTMLLogger;
import com.winger.log.LogGroup;
import com.winger.ui.Page;

public class TestCRectangle2
{
    @SuppressWarnings("unused")
    private static final HTMLLogger log = HTMLLogger.getLogger(TestPhysics.class, LogGroup.Framework, LogGroup.Assert);
    CSpriteBatch sb = new CSpriteBatch();
    Page ui;
    //
    CRectangle grow;
    CRectangle stationary;
    //
    float growX = 2;
    float growY = 4;
    
    
    public TestCRectangle2(Page ui)
    {
        this.ui = ui;
        //
        stationary = new CRectangle(800, 450, 400, 400);
        stationary.setOriginAtCenter();
        //
        grow = stationary.cpy();
    }
    
    
    public void draw()
    {
        if (ui.isVisible)
        {
            grow.width += growX;
            grow.height += growY;
            
            if (grow.width > 700 || grow.width < 100)
                growX *= -1;
            if (grow.height > 700 || grow.height < 100)
                growY *= -1;
            
            grow.setOriginAtCenter();
            
            sb.begin();
            drawRect(grow, stationary);
            drawRect(stationary, grow);
            sb.end();
        }
    }
    
    
    private void drawRect(CRectangle rect, CRectangle... others)
    {
        boolean drawn = false;
        for (CRectangle other : others)
        {
            if (other.intersects(rect))
            {
                sb.drawRectangle(ShapeType.Filled, rect, Color.BLUE);
                drawn = true;
            }
        }
        if (!drawn)
        {
            sb.drawRectangle(ShapeType.Line, rect, Color.WHITE);
        }
    }
}
