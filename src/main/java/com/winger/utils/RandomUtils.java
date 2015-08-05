package com.winger.utils;

import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 * Common random number generator utilities
 *
 * @author Michael P. Wingfield
 * @copyright Nov 4, 2014
 *
 */
public class RandomUtils
{
    public static Random random = new Random();
    
    
    /**
     * Gets a random float between 0 and 1
     * 
     * @return
     */
    public static float rand()
    {
        return random.nextFloat();
    }
    
    
    /**
     * Gets a random int, either 0 or 1
     * 
     * @return
     */
    public static int randInt()
    {
        return randInt(0, 1);
    }
    
    
    /**
     * Gets a random long, either 0 or 1
     * 
     * @return
     */
    public static long randLong()
    {
        return randLong(0L, 1L);
    }
    
    
    /**
     * Gets a random float from 0(inclusive) to max(inclusive)
     * 
     * @param max
     * @return
     */
    public static float rand(float max)
    {
        return rand(0, max);
    }
    
    
    /**
     * Gets a random int from 0(inclusive) to max(inclusive)
     * 
     * @param max
     * @return
     */
    public static int randInt(int max)
    {
        return randInt(0, max);
    }
    
    
    /**
     * Gets a random long from 0(inclusive) to max(inclusive)
     * 
     * @param max
     * @return
     */
    public static long randLong(long max)
    {
        return randLong(0L, max);
    }
    
    
    /**
     * Gets a random float from min(inclusive) to max(inclusive)
     * 
     * @param min
     * @param max
     * @return
     */
    public static float rand(float min, float max)
    {
        if (min >= max)
        {
            return min;
        }
        return min + rand() * (max - min);
    }
    
    
    /**
     * Gets a random int from min(inclusive) to max(inclusive)
     * 
     * @param min
     * @param max
     * @return
     */
    public static int randInt(int min, int max)
    {
        if (min >= max)
        {
            return min;
        }
        return min + random.nextInt(max - min + 1);
    }
    
    
    /**
     * Gets a random long from min(inclusive) to max(inclusive)
     * 
     * @param min
     * @param max
     * @return
     */
    public static long randLong(long min, long max)
    {
        if (min >= max)
        {
            return min;
        }
        return min + (long) random.nextInt((int) max - (int) min + 1);
    }
    
    
    /**
     * Returns a random element from this collection
     *
     * @param collection
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T fromCollection(Collection<T> collection)
    {
        if (collection == null)
        {
            return null;
        }
        if (collection.size() < 2)
        {
            return (T) collection.toArray()[0];
        }
        return (T) fromArray(collection.toArray());
    }
    
    
    /**
     * Returns a random element from this list
     * 
     * @param list
     * @return
     */
    public static <T> T fromList(List<T> list)
    {
        return fromCollection(list);
    }
    
    
    /**
     * Returns a random element from this array
     *
     * @param array
     * @return
     */
    public static <T> T fromArray(T[] array)
    {
        if (array == null)
        {
            return null;
        }
        if (array.length < 2)
        {
            return array[0];
        }
        return array[randInt(array.length - 1)];
    }
    
    
    /**
     * Returns the a new list with the elements in a random order
     * 
     * @param originalList
     * @return
     */
    public static <T> List<T> randomizeList(List<T> originalList)
    {
        if (originalList == null)
        {
            return null;
        }
        if (originalList.size() < 2)
        {
            return originalList;
        }
        List<T> newList = new ArrayList<T>();
        List<T> tempList = new ArrayList<T>();
        for (T o : originalList)
        {
            tempList.add(o);
        }
        int size = originalList.size();
        for (int i = 0; i < size; i++)
        {
            T temp = fromList(tempList);
            tempList.remove(temp);
            newList.add(temp);
            
        }
        return newList;
    }
    
    
    /**
     * Returns the same list with the elements in a random order.<br> (Does the operations on the given list)
     * 
     * @param originalList
     * @return
     */
    public static <T> void randomizeWithinList(List<T> originalList)
    {
        if (originalList == null)
        {
            return;
        }
        if (originalList.size() < 2)
        {
            return;
        }
        List<T> tempList = new ArrayList<T>();
        int size = originalList.size();
        for (int i = 0; i < size; i++)
        {
            T temp = fromList(originalList);
            originalList.remove(temp);
            tempList.add(temp);
            
        }
        for (T o : tempList)
        {
            originalList.add(o);
        }
    }

    /**
     * Returns a random color
     *
     * @return
     */
    public static Color randomColor(){
        return new Color(rand(), rand(), rand(), 1);
    }
}
