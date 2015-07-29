package com.winger.struct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.math.Vector2;

/**
 * A free form matrix/table data structure
 *
 * @author Michael P. Wingfield
 * @copyright Nov 4, 2014
 *
 * @param <T>
 */
public class ObjectMatrix<T> implements Iterable<T>
{
    protected Map<Vector2, T> map;
    
    
    public ObjectMatrix()
    {
        map = new HashMap<Vector2, T>();
    }
    
    
    /**
     * Get the object at the given coordinates
     * 
     * @param coord
     * @return
     */
    public T get(Vector2 coord)
    {
        if (map.containsKey(coord))
        {
            return map.get(coord);
        }
        return null;
    }
    
    
    /**
     * Get the object at the given coordinates
     * 
     * @param x
     * @param y
     * @return
     */
    public T get(float x, float y)
    {
        return get(new Vector2(x, y));
    }
    
    
    /**
     * Put the object at the given coordinates
     * 
     * @param coord
     * @param value
     */
    public void put(Vector2 coord, T value)
    {
        map.put(coord, value);
    }
    
    
    /**
     * Put the object at the given coordinates
     * 
     * @param x
     * @param y
     * @param value
     */
    public void put(float x, float y, T value)
    {
        put(new Vector2(x, y), value);
    }
    
    
    /**
     * Get all the coordinates that have been added to this matrix
     * 
     * @return
     */
    public List<Vector2> getKeys()
    {
        List<Vector2> tempKeys = new ArrayList<Vector2>();
        for (Vector2 key : map.keySet())
        {
            tempKeys.add(key);
        }
        return tempKeys;
    }
    
    
    /**
     * Get all the values that have been added to this matrix
     * 
     * @return
     */
    public List<T> getValues()
    {
        List<T> retList = new ArrayList<T>();
        for (Vector2 key : map.keySet())
        {
            retList.add(map.get(key));
        }
        return retList;
    }
    
    
    /**
     * Get the number of values that have been added to this matrix
     * 
     * @return
     */
    public int count()
    {
        return map.size();
    }
    
    
    /**
     * Get the number of values that have been added to this matrix
     * 
     * @return
     */
    public int size()
    {
        return map.size();
    }
    
    
    @Override
    public Iterator<T> iterator()
    {
        return getValues().iterator();
    }
}
