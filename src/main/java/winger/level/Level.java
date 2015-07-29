package com.winger.level;

import java.util.ArrayList;
import java.util.List;

import com.winger.draw.texture.CSpriteBatch;
import com.winger.game.AbstractGameObject;

public class Level<T extends TileType<T>>
{
    private String name;
    private List<AbstractGameObject> gameObjects;
    
    
    public Level()
    {
        name = null;
        gameObjects = new ArrayList<AbstractGameObject>();
    }
    
    
    public void addGameObject(AbstractGameObject gameObject)
    {
        gameObjects.add(gameObject);
    }
    
    
    public void update(float delta)
    {
        for (AbstractGameObject obj : gameObjects)
        {
            obj.update(delta);
        }
    }
    
    
    public void draw(CSpriteBatch sb)
    {
        for (AbstractGameObject obj : gameObjects)
        {
            obj.draw(sb);
        }
    }
    
    
    public String getName()
    {
        return name;
    }
    
    
    public Level<T> setName(String name)
    {
        this.name = name;
        return this;
    }
}
