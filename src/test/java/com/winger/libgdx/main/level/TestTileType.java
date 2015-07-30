package com.winger.libgdx.main.level;

import com.winger.level.TileType;
import com.winger.level.Tileable;

public enum TestTileType implements TileType<TestTileType>
{
    GROUND(GroundTile.class),
    WALL(WallTile.class),
    SENSOR(SensorTile.class),
    DYNAMIC(DynamicTile.class),
    GROUND0(GroundTile.class),
    WALL0(WallTile.class),
    GROUND1(GroundTile.class),
    WALL1(WallTile.class),
    GROUND2(GroundTile.class),
    WALL2(WallTile.class),
    GROUND3(GroundTile.class),
    WALL3(WallTile.class),
    GROUND4(GroundTile.class),
    WALL4(WallTile.class), ;
    
    private Class<? extends Tileable> clazz;


    TestTileType(Class<? extends Tileable> clazz)
    {
        this.clazz = clazz;
    }
    
    
    @Override
    public Class<? extends Tileable> clazz()
    {
        return clazz;
    }
    
    
    @Override
    public TestTileType[] tileTypes()
    {
        return values();
    }
    
}
