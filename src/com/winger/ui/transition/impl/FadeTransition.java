package com.winger.ui.transition.impl;

import java.util.Map;

import com.badlogic.gdx.graphics.Color;
import com.winger.ui.Element;
import com.winger.ui.Page;
import com.winger.ui.Transition;

/**
 * Transition between pages by elements fading in and out of visibility
 *
 * @author Michael P. Wingfield
 * @copyright Nov 4, 2014
 *
 */
public class FadeTransition extends Transition
{
    public FadeTransition()
    {
        millisecondsToLive = 200;
    }
    
    
    public void update()
    {
        switch (transitionState)
        {
            case 1: // transition on
                float complOn = getPercentageComplete();
                for (Element<?> element : page.elements())
                {
                    Map<String, Object> record = null;
                    if (!records.containsKey(element.hashCode()))
                    {
                        record = addNewRecord(element);
                        record.put("color", new Color(element.color()));
                        record.put("color-hover", new Color(element.colorHover()));
                        record.put("color-select", new Color(element.colorSelect()));
                        record.put("color-text", new Color(element.textColor()));
                    } else
                    {
                        record = records.get(element.hashCode());
                    }
                    element.color(calculatePrevColor(element.color(), (Color) record.get("color"), complOn));
                    element.colorHover(calculatePrevColor(element.colorHover(), (Color) record.get("color-hover"), complOn));
                    element.colorSelect(calculatePrevColor(element.colorSelect(), (Color) record.get("color-select"), complOn));
                    element.textColor(calculatePrevColor(element.textColor(), (Color) record.get("color-text"), complOn));
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
                    element.color(calculateNewColor(element.color(), (Color) record.get("color"), complOff));
                    element.colorHover(calculateNewColor(element.colorHover(), (Color) record.get("color-hover"), complOff));
                    element.colorSelect(calculateNewColor(element.colorSelect(), (Color) record.get("color-select"), complOff));
                    element.textColor(calculateNewColor(element.textColor(), (Color) record.get("color-text"), complOff));
                }
                if (complOff == 1)
                {
                    offTransitionCompleted();
                }
                break;
        }
    }
    
    
    private Color calculateNewColor(Color col, Color prevColor, float percentComplete)
    {
        col.a = (1f - percentComplete) * prevColor.a;
        return col;
    }
    
    
    private Color calculatePrevColor(Color col, Color prevColor, float percentageComplete)
    {
        col.a = percentageComplete * prevColor.a;
        return col;
    }
    
    
    public void transitionOff()
    {
        for (Element<?> element : page.elements())
        {
            Map<String, Object> record = addNewRecord(element);
            record.put("color", new Color(element.color()));
            record.put("color-hover", new Color(element.colorHover()));
            record.put("color-select", new Color(element.colorSelect()));
            record.put("color-text", new Color(element.textColor()));
        }
        offTransitionStarted();
    }
    
    
    public void transitionOn()
    {
        onTransitionStarted();
    }
    
    
    public Transition clone(Page page)
    {
        FadeTransition clone = new FadeTransition();
        clone.millisecondsToLive = this.millisecondsToLive;
        clone.page = page;
        return clone;
    }
}
