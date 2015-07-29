package com.winger.struct.deserialize;

import java.util.List;

public class JSONDeserializeRoot
{
    public String getPrivate1()
    {
        return private1;
    }
    
    
    public void setPrivate1(String private1)
    {
        this.private1 = private1;
    }
    
    
    public float getPrivate2()
    {
        return private2;
    }
    
    
    public void setPrivate2(float private2)
    {
        this.private2 = private2;
    }
    
    
    public String getName()
    {
        return getClass().getName();
    }
    
    
    public String str1;
    public String str2;
    public int int1;
    public float float1;
    private String private1;
    private float private2;
    public JSONDeserializeSubObj obj1;
    public JSONDeserializeSubList list1;
    public List<String> list2;
    
}
