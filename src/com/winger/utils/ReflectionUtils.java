package com.winger.utils;

public class ReflectionUtils
{
    public static <T> T create(Class<T> clazz, boolean debug)
    {
        try
        {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e)
        {
            if (debug)
                e.printStackTrace();
            return null;
        }
    }
    
    
    public static <T> T create(Class<T> clazz)
    {
        return create(clazz, false);
    }
}
