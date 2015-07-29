package com.winger.libgdx.main.level;

import com.badlogic.gdx.graphics.Color;
import com.winger.draw.texture.CSprite;
import com.winger.level.Tileable;

public class WallTile implements Tileable
{
    
    @Override
    public Color getTileColor()
    {
        return Color.YELLOW;
    }
    
    
    @Override
    public CSprite getTileSprite()
    {
        return null;
    }
    
    
    @Override
    public boolean isSensor()
    {
        return false;
    }
    
    
    @Override
    public boolean isTiled()
    {
        return true;
    }
    
    
    @Override
    public boolean isStatic()
    {
        return true;
    }
    
}
