package com.winger.physics.body;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.winger.physics.CBody;

public class BoxBody extends CBody<BoxBody>
{
    private float width;
    private float height;
    
    private PolygonShape shape;
    
    
    // ////////////////////////////////////////////
    // Initialization
    // ////////////////////////////////////////////
    public BoxBody(float width, float height)
    {
        super();
        this.width = width;
        this.height = height;
    }
    
    
    public BoxBody init(FixtureDef fixtureDef, BodyDef bodyDef)
    {
        super.init(fixtureDef, bodyDef);
        
        shape = new PolygonShape();
        shape.setAsBox(width, height);
        addShape(shape);
        
        return this;
    }
    
    
    public BoxBody setWidth()
    {
        shape.setAsBox(width, height);
        return this;
    }
    
    
    public BoxBody setHeight()
    {
        shape.setAsBox(width, height);
        return this;
    }
    
    
    // ////////////////////////////////////////////
    // Getters and Setters
    // ////////////////////////////////////////////
    public float getWidth()
    {
        return width;
    }
    
    
    public float getHeight()
    {
        return height;
    }
    
}
