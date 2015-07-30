package com.winger.struct.deserialize;


public class JSONDeserializeSubObj
{
    public String str1;
    private String private1;
    
    
    public String getName()
    {
        return getClass().getName();
    }
    
    
    public String getPrivate1()
    {
        return private1;
    }
    
    
    public void setPrivate1(String private1)
    {
        this.private1 = private1;
    }
    
}
