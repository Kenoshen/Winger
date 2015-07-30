package com.winger.ui.transition.impl;

import com.winger.math.tween.Tween;
import com.winger.ui.Element;
import com.winger.ui.Page;
import com.winger.ui.Transition;

import java.util.Map;

/**
 * Transition between pages by elements falling to the right
 *
 * @author Michael P. Wingfield
 * @copyright Nov 4, 2014
 *
 */
public class FallRightTransition extends Transition
{
    public FallRightTransition()
    {
        millisecondsToLive = 1000;
    }
    
    
    public void update()
    {
        switch (transitionState)
        {
            case 1: // transition on
                float complon = getPercentageComplete();
                for (Element<?> element : page.elements())
                {
                    float x = element.x();
                    float prevX = 0;
                    Map<String, Object> record = null;
                    if (!records.containsKey(element.hashCode()))
                    {
                        record = addNewRecord(element);
                        record.put("x", element.x());
                    } else
                    {
                        record = records.get(element.hashCode());
                    }
                    prevX = (Float) record.get("x");
                    
                    x = Tween.sinusoidalInOut(complon, prevX + 800, -800, 1);
                    element.x(x);
                }
                if (complon == 1)
                {
                    onTransitionCompleted();
                }
                break;
            
            case -1: // transition off
                float comploff = getPercentageComplete();
                for (Element<?> element : page.elements())
                {
                    Map<String, Object> record = records.get(element.hashCode());
                    float x = element.x();
                    float prevX = (Float) record.get("x");
                    
                    x = Tween.sinusoidalInOut(comploff, prevX, +800, 1);
                    element.x(x);
                }
                if (comploff == 1)
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
            record.put("x", element.x());
        }
        offTransitionStarted();
    }
    
    
    public void transitionOn()
    {
        onTransitionStarted();
    }
    
    
    public Transition clone(Page page)
    {
        FallRightTransition clone = new FallRightTransition();
        clone.millisecondsToLive = this.millisecondsToLive;
        clone.page = page;
        return clone;
    }
}
