package com.winger.libgdx.main.level;

import com.badlogic.gdx.graphics.Color;
import com.winger.Winger;
import com.winger.draw.texture.CSprite;
import com.winger.level.Tileable;
import com.winger.struct.CRectangle;

public class DynamicTile implements Tileable
{
    private CSprite sprite;
    
    
    public DynamicTile()
    {
        sprite = new CSprite(Winger.texture.getTexture("various.box"), CRectangle.empty());
    }
    
    
    @Override
    public Color getTileColor()
    {
        return Color.OLIVE;
    }
    
    
    @Override
    public CSprite getTileSprite()
    {
        return sprite;
    }
    
    
    @Override
    public boolean isSensor()
    {
        return false;
    }
    
    
    @Override
    public boolean isTiled()
    {
        return false;
    }
    
    
    @Override
    public boolean isStatic()
    {
        return false;
    }
    
}
