package com.winger.level;

public interface TileType<T>
{
    public Class<? extends Tileable> clazz();
    
    
    public T[] tileTypes();
}
