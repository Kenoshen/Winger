package com.winger.level;

import com.winger.struct.CRectangle;

public class Tile
{
    public String id;
    public String parent;
    public String type;
    public CRectangle rect;
    public float zIndex;
    public boolean tiled;
    public float tiledWidth;
    public float tiledHeight;
    public String triggerName;
    public String triggerId;
    public Tileable ref;
    
    
    public Tile()
    {
        id = null;
        parent = null;
        type = null;
        rect = new CRectangle(0, 0, 1, 1);
        zIndex = 0;
        tiled = false;
        tiledWidth = 32;
        tiledHeight = 32;
        ref = null;
    }
}
