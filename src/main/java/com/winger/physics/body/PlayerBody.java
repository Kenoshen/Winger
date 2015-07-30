package com.winger.physics.body;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.winger.physics.CBody;
import com.winger.physics.CWorld;

public class PlayerBody extends CBody<PlayerBody>
{
    private static final float LEGS_POSITION_OFFSET = 0.7f;
    private static final float LEGS_SIZE_OFFSET = 0.1f;
    
    private float width;
    private float height;
    private float runSlide = 0.07f;
    
    private CircleBody legs;
    
    private PolygonShape box;
    private CircleShape topCircle;
    private CircleShape bottomCircle;
    
    
    // ////////////////////////////////////////////
    // Initialization
    // ////////////////////////////////////////////
    public PlayerBody(float width, float height)
    {
        super();
        this.width = width;
        this.height = height;
        normalize();
    }
    
    
    public PlayerBody init(FixtureDef fixtureDef, BodyDef bodyDef)
    {
        super.init(fixtureDef, bodyDef);
        
        box = new PolygonShape();
        topCircle = new CircleShape();
        bottomCircle = new CircleShape();
        
        legs = new CircleBody(width - LEGS_SIZE_OFFSET);
        legs.parent = this;
        FixtureDef legsDef = new FixtureDef();
        legsDef.isSensor = true;
        legs.init(legsDef);
        
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
    
    
    private PlayerBody setWidth()
    {
        box.setAsBox(width, height - width);
        topCircle.setRadius(width + 0.01f);
        bottomCircle.setRadius(width + 0.01f);
        return this;
    }
    
    
    private PlayerBody setHeight()
    {
        float boxHeight = height - width;
        box.setAsBox(width, boxHeight);
        topCircle.setPosition(new Vector2(0, boxHeight));
        bottomCircle.setPosition(new Vector2(0, -boxHeight));
        return this;
    }
    
    
    @Override
    public PlayerBody addToWorld(World world)
    {
        super.addToWorld(world);
        legs.addToWorld(world);
        WeldJointDef def = new WeldJointDef();
        def.bodyA = body;
        def.bodyB = legs.body;
        def.collideConnected = false;
        def.localAnchorA.y = -((height - width) + LEGS_POSITION_OFFSET);
        CWorld.world._world.createJoint(def);
        return this;
    }
    
    
    public PlayerBody walk(float speed)
    {
        Vector2 vel = getLinearVelocity();
        float velChange = speed - vel.x;
        float impulse = body.getMass() * velChange;
        body.applyLinearImpulse(new Vector2(impulse, 0), body.getWorldCenter(), true);
        return this;
    }
    
    
    public PlayerBody run(float speed)
    {
        Vector2 vel = getLinearVelocity();
        float velChange = speed - vel.x;
        float impulse = (body.getMass() * velChange) * runSlide;
        body.applyLinearImpulse(new Vector2(impulse, 0), body.getWorldCenter(), true);
        return this;
    }
    
    
    public PlayerBody jump(float force)
    {
        float impulse = body.getMass() * force;
        body.applyLinearImpulse(new Vector2(0, impulse), body.getWorldCenter(), true);
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
    
    
    public float getRunSlide()
    {
        return runSlide;
    }
    
    
    public PlayerBody setRunSlide(float runSlide)
    {
        this.runSlide = runSlide;
        return this;
    }
}
