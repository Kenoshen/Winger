package com.winger.physics.body;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.winger.physics.CBody;

public class CircleBody extends CBody<CircleBody>
{
    private float radius;
    private CircleShape shape;
    
    
    // ////////////////////////////////////////////
    // Initialization
    // ////////////////////////////////////////////
    public CircleBody(float radius)
    {
        super();
        this.radius = radius;
    }
    
    
    @Override
    public CircleBody init(FixtureDef fixtureDef, BodyDef bodyDef)
    {
        super.init(fixtureDef, bodyDef);
        
        shape = new CircleShape();
        setRadius();
        addShape(shape);
        
        return this;
    }
    
    
    private CircleBody setRadius()
    {
        shape.setRadius(radius);
        return this;
    }
    
    
    // ////////////////////////////////////////////
    // Getters and Setters
    // ////////////////////////////////////////////
    public float getRadius()
    {
        return radius;
    }
    
}
