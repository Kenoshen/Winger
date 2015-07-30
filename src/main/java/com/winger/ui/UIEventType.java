package com.winger.ui;

/**
 * Enum for ui event types
 *
 * @author Michael P. Wingfield
 * @copyright Nov 4, 2014
 *
 */
public enum UIEventType
{
    ON_SELECT_START, ON_SELECT_END, ON_HOVER_START, ON_HOVER_END, ON_DRAG_START, ON_DRAG_END;
    
    public boolean equalsStr(String uiEventTypeStr)
    {
        return asString().equals(uiEventTypeStr);
    }
    
    
    public String asString()
    {
        return name().toLowerCase().replace("_", "-");
    }
}
