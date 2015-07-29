package com.winger.ui.delegate;

/**
 * For getting page events
 *
 * @author Michael P. Wingfield
 * @copyright Nov 4, 2014
 *
 */
public interface PageEventHandler
{
    public void handleEvent(Object sender, String pageName, String eventName);
}
