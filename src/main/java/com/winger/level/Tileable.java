package com.winger.level;

import com.badlogic.gdx.graphics.Color;
import com.winger.draw.texture.CSprite;

public interface Tileable
{
    CSprite getTileSprite();


    Color getTileColor();


    boolean isSensor();


    boolean isTiled();


    boolean isStatic();
}
