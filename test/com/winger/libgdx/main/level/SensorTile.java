package com.winger.libgdx.main.level;

import com.badlogic.gdx.graphics.Color;
import com.winger.draw.texture.CSprite;
import com.winger.level.Tileable;

public class SensorTile implements Tileable
{
    
    @Override
    public Color getTileColor()
    {
        return Color.BLUE;
    }
    
    
    @Override
    public CSprite getTileSprite()
    {
        return null;
    }
    
    
    @Override
    public boolean isSensor()
    {
        return true;
    }
    
    
    @Override
    public boolean isTiled()
    {
        return false;
    }
    
    
    @Override
    public boolean isStatic()
    {
        return true;
    }
    
}
