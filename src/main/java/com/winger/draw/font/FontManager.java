package com.winger.draw.font;

import com.winger.log.HTMLLogger;
import com.winger.log.LogGroup;

import java.util.HashMap;
import java.util.Map;

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
    private Map<String, CFont> fonts = new HashMap<String, CFont>();


    private FontManager() {
        fonts.put("default", new CFont("default"));
    }
    
    public static FontManager instance()
    {
        if (instance == null)
        {
            instance = new FontManager();
        }
        return instance;
    }
    
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
