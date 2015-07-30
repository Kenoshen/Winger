package com.winger.struct;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.winger.draw.texture.CSpriteBatch;
import com.winger.libgdx.main.physics.TestPhysics;
import com.winger.log.HTMLLogger;
import com.winger.log.LogGroup;
import com.winger.ui.Page;

public class TestCRectangle
{
    @SuppressWarnings("unused")
    private static final HTMLLogger log = HTMLLogger.getLogger(TestPhysics.class, LogGroup.Framework, LogGroup.Assert);
    CSpriteBatch sb = new CSpriteBatch();
    Page ui;
    //
    CRectangle spin1;
    CRectangle spin2;
    CRectangle move1;
    CRectangle stationary;
    //
    float rotation1 = 0.5f;
    float rotation2 = 1f;
    float speed1 = 3;
    
    
    public TestCRectangle(Page ui)
    {
        this.ui = ui;
        //
        spin1 = new CRectangle(550, 450, 300, 600);
        spin1.setOriginAtCenter();
        
        spin2 = new CRectangle(1050, 450, 600, 300);
        spin2.setOriginAtCenter();
        
        move1 = new CRectangle(100, 450, 100, 100);
        move1.setOriginAtCenter();
        
        stationary = new CRectangle(0, 0, 100, 100);
        stationary.origin.x = -400;
        stationary.origin.y = -600;
        
    }
    
    
    public void draw()
    {
        if (ui.isVisible)
        {
            spin1.rotation += rotation1;
            spin2.rotation += rotation2;
            move1.x += speed1;
            
            if (move1.x > 1600 || move1.x < 0)
                speed1 *= -1;
            
            sb.begin();
            drawRect(spin1, spin2);
            drawRect(spin2, spin1);
            drawRect(move1, spin2, spin1);
            if (spin1.overlaps(stationary))
            {
                sb.drawRectangle(ShapeType.Filled, stationary, Color.PURPLE);
            } else
            {
                sb.drawRectangle(ShapeType.Line, stationary, Color.WHITE);
            }
            sb.end();
        }
    }
    
    
    private void drawRect(CRectangle rect, CRectangle... others)
    {
        boolean drawn = false;
        for (CRectangle other : others)
        {
            if (other.contains(rect))
            {
                sb.drawRectangle(ShapeType.Filled, rect, Color.BLUE);
                drawn = true;
                break;
            } else if (rect.intersects(other))
            {
                sb.drawRectangle(ShapeType.Line, rect, Color.RED);
                drawn = true;
                break;
            }
        }
        if (!drawn)
        {
            sb.drawRectangle(ShapeType.Line, rect, Color.WHITE);
        }
    }
}
