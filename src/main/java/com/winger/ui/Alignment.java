package com.winger.ui;

import com.badlogic.gdx.math.Vector2;

/**
 * Enum for UI placement of origins
 *
 * @author Michael P. Wingfield
 * @copyright Nov 4, 2014
 *
 */
public enum Alignment
{
    TOP_LEFT(0f, 1f),
    TOP(0.5f, 1f),
    TOP_RIGHT(1f, 1f),
    RIGHT(1f, 0.5f),
    BOTTOM_RIGHT(1f, 0f),
    BOTTOM(0.5f, 0f),
    BOTTOM_LEFT(0f, 0f),
    LEFT(0f, 0.5f),
    CENTER(0.5f, 0.5f);
    
    private Vector2 aV;


    Alignment(float vx, float vy)
    {
        aV = new Vector2(vx, vy);
    }
    
    
    public static Alignment fromStr(String alignment)
    {
        for (Alignment a : Alignment.values())
        {
            if (a.toString().equals(alignment))
            {
                return a;
            }
        }
        return null;
    }
    
    
    public boolean equalsStr(String alignmentStr)
    {
        return toString().equals(alignmentStr);
    }
    
    
    public Vector2 alignmentVector()
    {
        return new Vector2(aV);
    }
    
    
    @Override
    public String toString()
    {
        return name().toLowerCase().replace("_", "-");
    }
}
