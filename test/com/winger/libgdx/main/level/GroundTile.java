package com.winger.libgdx.main.level;

import com.badlogic.gdx.graphics.Color;
import com.winger.Winger;
import com.winger.draw.texture.CSprite;
import com.winger.level.Tileable;
import com.winger.struct.CRectangle;
import com.winger.utils.ParseUtils;

public class GroundTile implements Tileable
{
    private CSprite sprite;
    private Color color;
    
    
    public GroundTile()
    {
        sprite = new CSprite(Winger.texture.getTexture("various.grass"), CRectangle.empty());
        color = ParseUtils.decodeColor("#87421F");
    }
    
    
    @Override
    public Color getTileColor()
    {
        return color;
    }
    
    
    @Override
    public CSprite getTileSprite()
    {
        return sprite;
    }
    
    
    @Override
    public boolean isTiled()
    {
        return true;
    }
    
    
    @Override
    public boolean isSensor()
    {
        return false;
    }
    
    
    @Override
    public boolean isStatic()
    {
        return true;
    }
    
}
