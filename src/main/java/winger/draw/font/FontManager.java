package com.winger.draw.font;

import java.util.HashMap;
import java.util.Map;

import com.winger.log.HTMLLogger;
import com.winger.log.LogGroup;

/**
 * Keeps handles on any added fonts and allows for lookup by name
 *
 * @author Michael P. Wingfield
 * @copyright Nov 4, 2014
 *
 */
public class FontManager
{
    protected static HTMLLogger log = HTMLLogger.getLogger(FontManager.class, LogGroup.Graphics, LogGroup.UI, LogGroup.Util, LogGroup.Framework);
    private static FontManager instance;
    
    
    public static FontManager instance()
    {
        if (instance == null)
        {
            instance = new FontManager();
        }
        return instance;
    }
    
    
    private FontManager()
    {
        fonts.put("default", new CFont("default"));
    }
    
    
    private Map<String, CFont> fonts = new HashMap<String, CFont>();
    
    
    public CFont getDefaultFont()
    {
        return getFont("default");
    }
    
    
    public void addFont(String name, CFont font)
    {
        fonts.put(name, font);
    }
    
    
    public CFont getFont(String name)
    {
        if (fonts.containsKey(name))
        {
            return fonts.get(name);
        }
        return null;
    }
    
    
    public boolean removeFont(String name)
    {
        if (fonts.containsKey(name))
        {
            fonts.remove(name);
            return true;
        }
        return false;
    }
}
