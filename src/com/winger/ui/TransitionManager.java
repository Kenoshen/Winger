package com.winger.ui;

import java.util.HashMap;
import java.util.Map;

import com.winger.ui.transition.impl.DefaultTransition;

/**
 * Manages transitions between UI pages using the Transition classes
 *
 * @author Michael P. Wingfield
 * @copyright Nov 4, 2014
 *
 */
public class TransitionManager
{
    private static TransitionManager instance;
    
    
    private TransitionManager()
    {
        setDefaultTransition(new DefaultTransition());
    }
    
    
    public static TransitionManager instance()
    {
        if (instance == null)
        {
            instance = new TransitionManager();
        }
        return instance;
    }
    
    
    private Map<String, Transition> transitions = new HashMap<String, Transition>();
    
    
    public Transition getDefaultTransition()
    {
        return getTransition("default");
    }
    
    
    public void setDefaultTransition(Transition transition)
    {
        putTransition("default", transition);
    }
    
    
    public void putTransition(String name, Transition transition)
    {
        transitions.put(name, transition);
    }
    
    
    public Transition getTransition(String name)
    {
        if (transitions.containsKey(name))
        {
            return transitions.get(name);
        }
        return null;
    }
    
    
    public boolean removeTransition(String name)
    {
        if (transitions.containsKey(name))
        {
            transitions.remove(name);
            return true;
        }
        return false;
    }
}
