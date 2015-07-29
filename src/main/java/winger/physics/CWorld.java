package com.winger.physics;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.winger.draw.texture.CSpriteBatch;
import com.winger.game.AbstractGameObject;

public class CWorld
{
    public static CWorld world;
    
    private static int VELOCITY_ITERATIONS = 6;
    private static int POSITION_ITERATIONS = 2;
    
    public World _world;
    private Box2DDebugRenderer debugger;
    public Camera camera;
    private CSpriteBatch spriteBatch;
    private boolean isDebugging;
    private float pixelsToUnits;
    //
    private List<AbstractGameObject> gameObjects;
    private List<AbstractGameObject> gameObjectsToAdd;
    private List<AbstractGameObject> gameObjectsToDelete;
    
    
    public CWorld(Camera camera)
    {
        spriteBatch = new CSpriteBatch();
        debugger = new Box2DDebugRenderer();
        this.camera = camera;
        debug(false);
        init(new Vector2(0, -10), true);
    }
    
    
    public CWorld init(Vector2 gravity, boolean shouldObjectsSleep)
    {
        gameObjectsToAdd = new ArrayList<AbstractGameObject>();
        gameObjectsToDelete = new ArrayList<AbstractGameObject>();
        if (_world != null)
        {
            if (gameObjects != null)
            {
                gameObjectsToDelete.addAll(gameObjects);
                actuallyRemoveGameObjects();
            }
            _world.dispose();
        }
        _world = new World(gravity, shouldObjectsSleep);
        gameObjects = new ArrayList<AbstractGameObject>();
        world = this;
        return this;
    }
    
    
    public CWorld addGameObject(AbstractGameObject gameObject)
    {
        gameObjectsToAdd.add(gameObject);
        return this;
    }
    
    
    private void actuallyAddGameObjects()
    {
        for (AbstractGameObject gameObject : gameObjectsToAdd)
        {
            gameObject.body.addToWorld(_world);
            gameObject.body.setGameObject(gameObject);
            gameObjects.add(gameObject);
        }
        gameObjectsToAdd = new ArrayList<AbstractGameObject>();
    }
    
    
    public CWorld removeGameObject(AbstractGameObject gameObject)
    {
        gameObjectsToDelete.add(gameObject);
        return this;
    }
    
    
    private void actuallyRemoveGameObjects()
    {
        for (AbstractGameObject gameObject : gameObjectsToDelete)
        {
            gameObjects.remove(gameObject);
            gameObject.body.removeFromWorld(_world);
        }
    }
    
    
    public CWorld update(float delta)
    {
        camera.update();
        _world.step(delta, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
        for (AbstractGameObject obj : gameObjects)
        {
            obj.update(delta);
        }
        actuallyAddGameObjects();
        actuallyRemoveGameObjects();
        return this;
    }
    
    
    public CWorld draw()
    {
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        for (AbstractGameObject obj : gameObjects)
        {
            obj.draw(spriteBatch);
        }
        spriteBatch.end();
        if (debug())
        {
            debugger.render(_world, camera.combined);
        }
        return this;
    }
    
    
    // ////////////////////////////////////////
    // Getters and Setters
    // ////////////////////////////////////////
    public boolean debug()
    {
        return isDebugging;
    }
    
    
    public CWorld debug(boolean value)
    {
        isDebugging = value;
        return this;
    }
    
    
    public float pixelsToUnits()
    {
        return pixelsToUnits;
    }
    
    
    public CWorld pixelsToUnits(float value)
    {
        pixelsToUnits = value;
        return this;
    }
}
