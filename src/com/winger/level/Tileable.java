package com.winger.level;

import com.badlogic.gdx.graphics.Color;
import com.winger.draw.texture.CSprite;

public interface Tileable
{
    public CSprite getTileSprite();
    
    
    public Color getTileColor();
    
    
    public boolean isSensor();
    
    
    public boolean isTiled();
    
    
    public boolean isStatic();
}
