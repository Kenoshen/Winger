package com.winger.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.winger.game.AbstractGameObject;
import com.winger.struct.JSON;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public abstract class CBody<T>
{
    public Body body;
    public CBody<?> parent;
    protected AbstractGameObject gameObject;
    private BodyDef bodyDef;
    private FixtureDef fixtureDef;
    private List<Shape> shapes;
    
    
    // ////////////////////////////////////////////
    // Initialization
    // ////////////////////////////////////////////
    public CBody()
    {
        shapes = new ArrayList<Shape>();
    }
    
    
    public T init(FixtureDef fixtureDef, BodyDef bodyDef)
    {
        if (fixtureDef != null)
        {
            this.fixtureDef = fixtureDef;
        } else
        {
            this.fixtureDef = defaultFixtureDef();
        }
        if (bodyDef != null)
        {
            this.bodyDef = bodyDef;
        } else
        {
            this.bodyDef = defaultBodyDef();
        }
        return (T) this;
    }
    
    
    public final T init(FixtureDef fixtureDef)
    {
        return init(fixtureDef, null);
    }
    
    
    public final T init(BodyDef bodyDef)
    {
        return init(null, bodyDef);
    }
    
    
    public final T init(Vector2 position)
    {
        BodyDef b = defaultBodyDef();
        b.position.x = position.x;
        b.position.y = position.y;
        return init(b);
    }
    
    
    public final T init()
    {
        return init(null, null);
    }
    
    
    protected FixtureDef defaultFixtureDef()
    {
        FixtureDef f = new FixtureDef();
        f.density = 1;
        return f;
    }
    
    
    protected BodyDef defaultBodyDef()
    {
        BodyDef b = new BodyDef();
        b.type = BodyType.DynamicBody;
        b.allowSleep = true;
        return b;
    }
    
    
    // ////////////////////////////////////////////
    // Body Creation
    // ////////////////////////////////////////////
    public T addShape(Shape shape)
    {
        shapes.add(shape);
        return (T) this;
    }
    
    
    public T addToWorld(World world)
    {
        body = world.createBody(bodyDef);
        body.setUserData(this);
        for (Shape shape : shapes)
        {
            fixtureDef.shape = shape;
            body.createFixture(fixtureDef);
        }
        return (T) this;
    }
    
    
    public T addToWorld(CWorld world)
    {
        return addToWorld(world._world);
    }
    
    
    public T addToWorld()
    {
        return addToWorld(CWorld.world);
    }
    
    
    public T removeFromWorld(World world)
    {
        world.destroyBody(body);
        return (T) this;
    }
    
    
    public T removeFromWorld()
    {
        return removeFromWorld(CWorld.world._world);
    }
    
    
    // ////////////////////////////////////////////
    // Helpers
    // ////////////////////////////////////////////
    @Override
    public String toString()
    {
        try
        {
            return JSON.serializeToJSON(this).toString(3);
        } catch (JsonProcessingException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "{}";
    }
    
    
    // ////////////////////////////////////////////
    // Getters and Setters
    // ////////////////////////////////////////////
    public float getDensity()
    {
        if (body.getFixtureList() != null && body.getFixtureList().size > 0)
            return body.getFixtureList().get(0).getDensity();
        return 0;
    }
    
    
    /**
     * 
     * @default 0.0
     * @param density
     * @return
     */
    public T setDensity(float density)
    {
        for (Fixture fixture : body.getFixtureList())
        {
            fixture.setDensity(density);
        }
        body.resetMassData();
        return (T) this;
    }
    
    
    public float getFriction()
    {
        if (body.getFixtureList() != null && body.getFixtureList().size > 0)
            return body.getFixtureList().get(0).getFriction();
        return 0;
    }
    
    
    /**
     * 
     * @default 0.0
     * @param friction
     * @return
     */
    public T setFriction(float friction)
    {
        for (Fixture fixture : body.getFixtureList())
        {
            fixture.setFriction(friction);
        }
        return (T) this;
    }
    
    
    public boolean getIsSensor()
    {
        if (body.getFixtureList() != null && body.getFixtureList().size > 0)
            return body.getFixtureList().get(0).isSensor();
        return false;
    }
    
    
    /**
     * 
     * @default false
     * @param isSensor
     * @return
     */
    public T setIsSensor(boolean isSensor)
    {
        for (Fixture fixture : body.getFixtureList())
        {
            fixture.setSensor(isSensor);
        }
        return (T) this;
    }
    
    
    public float getRestitution()
    {
        if (body.getFixtureList() != null && body.getFixtureList().size > 0)
            return body.getFixtureList().get(0).getRestitution();
        return 0;
    }
    
    
    /**
     * 
     * @default 0.0
     * @param restitution
     * @return
     */
    public T setRestitution(float restitution)
    {
        for (Fixture fixture : body.getFixtureList())
        {
            fixture.setRestitution(restitution);
        }
        return (T) this;
    }
    
    
    public float getAngle()
    {
        return body.getAngle();
    }
    
    
    /**
     * 
     * @default 0.0
     * @param angle
     * @return
     */
    public T setAngle(float angle)
    {
        body.setTransform(getPosition(), angle);
        return (T) this;
    }
    
    
    public float getAngularDamping()
    {
        return body.getAngularDamping();
    }
    
    
    /**
     * 
     * @default 0.0
     * @param angularDamping
     * @return
     */
    public T setAngularDamping(float angularDamping)
    {
        body.setAngularDamping(angularDamping);
        return (T) this;
    }
    
    
    public float getAngularVelocity()
    {
        return body.getAngularVelocity();
    }
    
    
    /**
     * 
     * @default 0.0
     * @param angularVelocity
     * @return
     */
    public T setAngularVelocity(float angularVelocity)
    {
        body.setAngularVelocity(angularVelocity);
        return (T) this;
    }
    
    
    public float getGravityScale()
    {
        return body.getGravityScale();
    }
    
    
    /**
     * 
     * @default 0.0
     * @param gravityScale
     * @return
     */
    public T setGravityScale(float gravityScale)
    {
        body.setGravityScale(gravityScale);
        return (T) this;
    }
    
    
    public float getLinearDamping()
    {
        return body.getLinearDamping();
    }
    
    
    /**
     * 
     * @default 0.0
     * @param linearDamping
     * @return
     */
    public T setLinearDamping(float linearDamping)
    {
        body.setLinearDamping(linearDamping);
        return (T) this;
    }
    
    
    public Vector2 getLinearVelocity()
    {
        return body.getLinearVelocity();
    }
    
    
    /**
     * 
     * @default (0.0, 0.0)
     * @param linearVelocity
     * @return
     */
    public T setLinearVelocity(Vector2 linearVelocity)
    {
        body.setLinearVelocity(linearVelocity);
        return (T) this;
    }
    
    
    /**
     * 
     * @default (0.0, 0.0)
     * @param x
     * @param y
     * @return
     */
    public T setLinearVelocity(float x, float y)
    {
        return setLinearVelocity(new Vector2(x, y));
    }
    
    
    public Vector2 getPosition()
    {
        return body.getPosition();
    }
    
    
    /**
     * 
     * @default (0.0, 0.0)
     * @param position
     * @return
     */
    public T setPosition(Vector2 position)
    {
        body.setTransform(position, getAngle());
        return (T) this;
    }
    
    
    /**
     * 
     * @default (0.0, 0.0)
     * @param x
     * @param y
     * @return
     */
    public T setPosition(float x, float y)
    {
        return setPosition(new Vector2(x, y));
    }
    
    
    public boolean isActive()
    {
        return body.isActive();
    }
    
    
    /**
     * 
     * @default false
     * @param active
     * @return
     */
    public T setActive(boolean active)
    {
        body.setActive(active);
        return (T) this;
    }
    
    
    public boolean isAllowSleep()
    {
        return body.isSleepingAllowed();
    }
    
    
    /**
     * 
     * @default false
     * @param allowSleep
     * @return
     */
    public T setAllowSleep(boolean allowSleep)
    {
        body.setSleepingAllowed(allowSleep);
        return (T) this;
    }
    
    
    public boolean isAwake()
    {
        return body.isAwake();
    }
    
    
    /**
     * 
     * @default false
     * @param awake
     * @return
     */
    public T setAwake(boolean awake)
    {
        body.setAwake(awake);
        return (T) this;
    }
    
    
    public boolean isBullet()
    {
        return body.isBullet();
    }
    
    
    /**
     * 
     * @default false
     * @param bullet
     * @return
     */
    public T setBullet(boolean bullet)
    {
        body.setBullet(bullet);
        return (T) this;
    }
    
    
    public boolean isFixedRotation()
    {
        return body.isFixedRotation();
    }
    
    
    /**
     * 
     * @default false
     * @param fixedRotation
     * @return
     */
    public T setFixedRotation(boolean fixedRotation)
    {
        body.setFixedRotation(fixedRotation);
        return (T) this;
    }
    
    
    public BodyType getType()
    {
        return body.getType();
    }
    
    
    public T setType(BodyType type)
    {
        body.setType(type);
        return (T) this;
    }
    
    
    public AbstractGameObject getGameObject()
    {
        return gameObject;
    }
    
    
    public T setGameObject(AbstractGameObject gameObject)
    {
        this.gameObject = gameObject;
        return (T) this;
    }
}
