package com.winger.struct;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.winger.draw.texture.CSpriteBatch;
import com.winger.libgdx.main.physics.TestPhysics;
import com.winger.log.HTMLLogger;
import com.winger.log.LogGroup;
import com.winger.ui.Page;

import java.util.ArrayList;
import java.util.List;

public class TestCRectangle3
{
    @SuppressWarnings("unused")
    private static final HTMLLogger log = HTMLLogger.getLogger(TestPhysics.class, LogGroup.Framework, LogGroup.Assert);
    private static final float SCALE = 10;
    CSpriteBatch sb = new CSpriteBatch();
    Page ui;
    //
    List<RectPair> pairs = new ArrayList<RectPair>();
    
    
    public TestCRectangle3(Page ui)
    {
        this.ui = ui;
        //
        pairs.add(new RectPair(new CRectangle(0, 0, 50, 50), new CRectangle(-45, -45, 20, 20), v(25, 50)));
        pairs.add(new RectPair(new CRectangle(0, 0, 50, 50), new CRectangle(40, -10, 20, 20), v(50, 50)));
        pairs.add(new RectPair(new CRectangle(0, 0, 50, 50), new CRectangle(10, 10, 20, 20), v(75, 50)));
        pairs.add(new RectPair(new CRectangle(0, 0, 50, 50), new CRectangle(20, -10, 20, 20), v(25, 25)));
        pairs.add(new RectPair(new CRectangle(0, 0, 50, 50), new CRectangle(20, -5, 20, 20, 45), v(50, 25)).setBOriginToCenter());
        pairs.add(new RectPair(new CRectangle(0, 0, 50, 50), new CRectangle(20, 12, 20, 20, 45), v(75, 25)).setBOriginToCenter());
        pairs.add(new RectPair(new CRectangle(0, 0, 50, 50), new CRectangle(50, 00, 20, 40, 45), v(75, 5)).setBOriginToCenter());
    }

    private static Vector2 v(float x, float y) {
        return new Vector2(x, y);
    }
    
    public void draw()
    {
        if (ui.isVisible)
        {
            sb.begin();
            for (RectPair pair : pairs)
            {
                pair.draw(sb);
            }
            sb.end();
        }
    }
    
    public class RectPair
    {
        public CRectangle a = null;
        public CRectangle b = null;


        public RectPair(CRectangle a, CRectangle b, Vector2 offset)
        {
            this.a = a;
            this.b = b;
            a.x += offset.x * SCALE;
            a.y += offset.y * SCALE;
            b.x += offset.x * SCALE;
            b.y += offset.y * SCALE;
        }


        public RectPair setBOriginToCenter()
        {
            b.setOriginAtCenter();
            return this;
        }


        public void draw(CSpriteBatch sb)
        {
            if (a.intersects(b))
            {
                sb.drawRectangle(ShapeType.Line, a, Color.GREEN);
            } else
            {
                sb.drawRectangle(ShapeType.Line, a, Color.RED);
            }

            if (b.intersects(a))
            {
                sb.drawRectangle(ShapeType.Line, b, Color.GREEN);
            } else
            {
                sb.drawRectangle(ShapeType.Line, b, Color.RED);
            }
        }
    }
}
