package com.winger.ui.transition.impl;

import com.winger.ui.Page;
import com.winger.ui.Transition;

/**
 * A default implementation of the Transition class. Just snaps pages on and off.
 *
 * @author Michael P. Wingfield
 * @copyright Nov 4, 2014
 *
 */
public class DefaultTransition extends Transition
{
    public DefaultTransition()
    {
        millisecondsToLive = 1;
    }
    
    
    public void update()
    {   
        
    }
    
    
    public void transitionOff()
    {
        offTransitionCompleted();
    }
    
    
    public void transitionOn()
    {
        onTransitionCompleted();
    }
    
    
    public Transition clone(Page page)
    {
        DefaultTransition clone = new DefaultTransition();
        clone.millisecondsToLive = this.millisecondsToLive;
        clone.page = page;
        return clone;
    }
}
