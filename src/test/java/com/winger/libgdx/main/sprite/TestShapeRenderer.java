package com.winger.libgdx.main.sprite;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.winger.draw.texture.CSprite;
import com.winger.draw.texture.CSpriteBatch;
import com.winger.struct.CRectangle;
import com.winger.ui.Page;

public class TestShapeRenderer
{
    private CSpriteBatch sb = new CSpriteBatch();
    private Page ui;
    
    private CSprite testSprite;
    
    
    public TestShapeRenderer(Page ui)
    {
        this.ui = ui;
        
        testSprite = new CSprite("green.stand", new CRectangle(700, 100, 200, 200), false);
    }
    
    
    public void draw()
    {
        if (ui.isVisible)
        {
            sb.begin();
            testSprite.draw(sb);
            drawArcs();
            drawCircles();
            drawRectangles();
            drawTriangles();
            drawLines();
            drawPoint();
            drawArrows();
            testSprite.draw(sb);
            sb.end();
        }
    }
    
    
    public void drawArcs()
    {
        sb.drawArc(ShapeType.Filled, new Vector2(100, 600), 50, 1, 2, Color.WHITE);
        sb.drawArc(ShapeType.Line, new Vector2(100, 500), 50, 1, 2, Color.WHITE);
    }
    
    
    public void drawCircles()
    {
        sb.drawCircle(ShapeType.Filled, new Vector2(200, 600), 50, Color.WHITE);
        sb.drawCircle(ShapeType.Line, new Vector2(200, 500), 50, Color.WHITE);
    }
    
    
    public void drawRectangles()
    {
        sb.drawRectangle(ShapeType.Filled, new CRectangle(300, 600, 50, 50), Color.WHITE);
        sb.drawRectangle(ShapeType.Line, new CRectangle(300, 500, 50, 50), Color.WHITE);
        sb.drawRectangle(ShapeType.Filled, new CRectangle(300, 400, 50, 50, 45), Color.WHITE);
        sb.drawRectangle(ShapeType.Line, new CRectangle(300, 300, 50, 50, 45), Color.WHITE);
    }
    
    
    public void drawTriangles()
    {
        sb.drawTriangle(ShapeType.Filled, new Vector2(400, 600), new Vector2(350, 500), new Vector2(450, 500), Color.WHITE);
        sb.drawTriangle(ShapeType.Line, new Vector2(400, 400), new Vector2(350, 300), new Vector2(450, 300), Color.WHITE);
    }
    
    
    public void drawLines()
    {
        sb.drawLine(new Vector2(500, 600), new Vector2(510, 500), Color.WHITE);
    }
    
    
    public void drawPoint()
    {
        sb.drawPoint(new Vector2(600, 600), Color.WHITE);
    }
    
    
    public void drawArrows()
    {
        sb.drawArrow(ShapeType.Line, new Vector2(200, 200), new Vector2(300, 150), 30, Color.WHITE);
        sb.drawArrow(ShapeType.Filled, new Vector2(200, 200), new Vector2(100, 300), 30, Color.WHITE);
    }
}
