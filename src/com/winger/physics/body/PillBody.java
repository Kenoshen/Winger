package com.winger.physics.body;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.winger.physics.CBody;

public class PillBody extends CBody<PillBody>
{
    private float width;
    private float height;
    
    private PolygonShape box;
    private CircleShape topCircle;
    private CircleShape bottomCircle;
    
    
    // ////////////////////////////////////////////
    // Initialization
    // ////////////////////////////////////////////
    public PillBody(float width, float height)
    {
        super();
        this.width = width;
        this.height = height;
        normalize();
    }
    
    
    public PillBody init(FixtureDef fixtureDef, BodyDef bodyDef)
    {
        super.init(fixtureDef, bodyDef);
        
        box = new PolygonShape();
        topCircle = new CircleShape();
        bottomCircle = new CircleShape();
        
        setWidth();
        setHeight();
        
        addShape(box);
        addShape(topCircle);
        addShape(bottomCircle);
        
        return this;
    }
    
    
    protected void normalize()
    {
        if (width >= height)
        {
            height = width + 0.001f;
        }
    }
    
    
    // ////////////////////////////////////////////
    // Getters and Setters
    // ////////////////////////////////////////////
    public float getWidth()
    {
        return width;
    }
    
    
    private PillBody setWidth()
    {
        normalize();
        box.setAsBox(width, height - width);
        topCircle.setRadius(width + 0.01f);
        bottomCircle.setRadius(width + 0.01f);
        return this;
    }
    
    
    public float getHeight()
    {
        return height;
    }
    
    
    private PillBody setHeight()
    {
        normalize();
        float boxHeight = height - width;
        box.setAsBox(width, boxHeight);
        topCircle.setPosition(new Vector2(0, boxHeight));
        bottomCircle.setPosition(new Vector2(0, -boxHeight));
        return this;
    }
}
