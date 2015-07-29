package com.winger.utils;

import com.badlogic.gdx.utils.Clipboard;

public class GlobalClipboard
{
    private static GlobalClipboard instance;
    
    
    public static GlobalClipboard instance()
    {
        if (instance == null)
        {
            instance = new GlobalClipboard();
        }
        return instance;
    }
    
    
    private String contents;
    private Clipboard clipboard;
    
    
    private GlobalClipboard()
    {
        contents = "";
    }
    
    
    public void setClipboard(Clipboard clipboard)
    {
        this.clipboard = clipboard;
    }
    
    
    public String getContents()
    {
        if (clipboard != null)
        {
            contents = clipboard.getContents();
        }
        return contents;
    }
    
    
    public void setContents(String contents)
    {
        if (contents == null)
        {
            contents = "";
        }
        this.contents = contents;
        if (clipboard != null)
        {
            clipboard.setContents(contents);
        }
    }
}
