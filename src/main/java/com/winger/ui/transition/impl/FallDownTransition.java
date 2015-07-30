package com.winger.ui.transition.impl;

import com.winger.math.tween.Tween;
import com.winger.ui.Element;
import com.winger.ui.Page;
import com.winger.ui.Transition;

import java.util.Map;

/**
 * Transition between pages by elements falling downwards
 *
 * @author Michael P. Wingfield
 * @copyright Nov 4, 2014
 *
 */
public class FallDownTransition extends Transition
{
    public FallDownTransition()
    {
        millisecondsToLive = 1000;
    }
    
    
    public void update()
    {
        switch (transitionState)
        {
            case 1: // transition on
                float complOn = getPercentageComplete();
                for (Element<?> element : page.elements())
                {
                    float y = element.y();
                    float prevY = 0;
                    Map<String, Object> record = null;
                    if (!records.containsKey(element.hashCode()))
                    {
                        record = addNewRecord(element);
                        record.put("y", element.y());
                    } else
                    {
                        record = records.get(element.hashCode());
                    }
                    prevY = (Float) record.get("y");
                    
                    y = Tween.sinusoidalInOut(complOn, prevY + 500, -500, 1);
                    element.y(y);
                }
                if (complOn == 1)
                {
                    onTransitionCompleted();
                }
                break;
            
            case -1: // transition off
                float complOff = getPercentageComplete();
                for (Element<?> element : page.elements())
                {
                    Map<String, Object> record = records.get(element.hashCode());
                    float y = element.y();
                    float prevY = (Float) record.get("y");
                    
                    y = Tween.sinusoidalInOut(complOff, prevY, +500, 1);
                    element.y(y);
                }
                if (complOff == 1)
                {
                    offTransitionCompleted();
                }
                break;
        }
    }
    
    
    public void transitionOff()
    {
        for (Element<?> element : page.elements())
        {
            Map<String, Object> record = addNewRecord(element);
            record.put("y", element.y());
        }
        offTransitionStarted();
    }
    
    
    public void transitionOn()
    {
        onTransitionStarted();
    }
    
    
    public Transition clone(Page page)
    {
        FallDownTransition clone = new FallDownTransition();
        clone.millisecondsToLive = this.millisecondsToLive;
        clone.page = page;
        return clone;
    }
}
