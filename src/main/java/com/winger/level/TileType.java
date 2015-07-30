package com.winger.level;

public interface TileType<T>
{
    Class<? extends Tileable> clazz();


    T[] tileTypes();
}
