package com.winger.ui;

import com.winger.log.HTMLLogger;
import com.winger.log.LogGroup;

import java.util.HashMap;
import java.util.Map;

/**
 * Used for UI page transitions where a UI page holds a list of elements that need to be moved or modified on transition to or from another page
 *
 * @author Michael P. Wingfield
 * @copyright Nov 4, 2014
 *
 */
public abstract class Transition
{
    protected static final HTMLLogger log = HTMLLogger.getLogger(Transition.class, LogGroup.GUI, LogGroup.Framework);
    public Page page;
    public long millisecondsToLive;
    
    // / <summary>
    // / -1 means it is transitioning off
    // / 0 means it is not transitioning
    // / 1 means it is transitioning on
    // / </summary>
    protected int transitionState = 0;
    protected long startTime = 0;
    protected long expectedEndTime = 0;
    
    protected Map<Integer, Map<String, Object>> records = new HashMap<Integer, Map<String, Object>>();
    
    
    protected void initializeDefaults()
    {
        millisecondsToLive = 1000;
        setStartTimeAsNow();
    }
    
    
    protected void setStartTimeAsNow()
    {
        startTime = getCurrentTime();
        expectedEndTime = startTime + millisecondsToLive;
    }
    
    
    protected long getCurrentTime()
    {
        return System.currentTimeMillis();
    }
    
    
    protected boolean isCurrentTimeExceededTimeToLive()
    {
        return (getCurrentTime() > expectedEndTime);
    }
    
    
    protected float getPercentageComplete()
    {
        if (millisecondsToLive != 0)
        {
            float percentage = (float) (expectedEndTime - getCurrentTime()) / (float) millisecondsToLive;
            if (percentage > 1f)
            {
                percentage = 1f;
            } else if (percentage < 0f)
            {
                percentage = 0f;
            }
            return (1f - percentage);
        }
        return 0;
    }
    
    
    protected long getMillisecondsLeft()
    {
        long left = expectedEndTime - getCurrentTime();
        if (left < 0)
        {
            return 0;
        }
        return left;
    }
    
    
    protected Map<String, Object> addNewRecord(Element<?> element)
    {
        Map<String, Object> dic = new HashMap<String, Object>();
        records.put(element.hashCode(), dic);
        return dic;
    }
    
    
    protected void offTransitionStarted()
    {
        page.isEnabled = false;
        page.isVisible = true;
        page.isTransitioning = true;
        setStartTimeAsNow();
        transitionState = -1;
        if (page.onTransitionOffStart != null)
        {
            notify(page.onTransitionOffStart);
        }
    }
    
    
    protected void offTransitionCompleted()
    {
        page.isEnabled = false;
        page.isVisible = false;
        page.isTransitioning = false;
        transitionState = 0;
        if (page.onTransitionOffEnd != null)
        {
            notify(page.onTransitionOffEnd);
        }
    }
    
    
    protected void onTransitionStarted()
    {
        page.isEnabled = false;
        page.isVisible = true;
        page.isTransitioning = true;
        setStartTimeAsNow();
        transitionState = 1;
        if (page.onTransitionOnStart != null)
        {
            notify(page.onTransitionOnStart);
        }
    }
    
    
    protected void onTransitionCompleted()
    {
        page.isEnabled = true;
        page.isVisible = true;
        page.isTransitioning = false;
        transitionState = 0;
        if (page.onTransitionOnEnd != null)
        {
            notify(page.onTransitionOnEnd);
        }
    }
    
    
    protected void notify(String eventStr)
    {
        PageManager.instance().notifySubscribers(page, page.name, eventStr);
    }
    
    
    public abstract void update();
    
    
    public abstract void transitionOff();
    
    
    public abstract void transitionOn();
    
    
    public abstract Transition clone(Page page);
}
