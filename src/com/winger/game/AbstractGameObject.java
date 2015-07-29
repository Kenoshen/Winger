package com.winger.game;

import com.winger.draw.texture.CSprite;
import com.winger.draw.texture.CSpriteBatch;
import com.winger.physics.CBody;
import com.winger.struct.JSON;

public abstract class AbstractGameObject
{
    public CSprite sprite;
    public CBody<?> body;
    
    
    public abstract void update(float delta);
    
    
    public abstract void draw(CSpriteBatch spriteBatch);
    
    
    public abstract JSON toJSON();
    // ////////////////////////////////////////
    // Getters and Setters
    // ////////////////////////////////////////
}
