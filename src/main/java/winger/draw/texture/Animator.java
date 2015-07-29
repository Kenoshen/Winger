package com.winger.draw.texture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Animator
{
    private Map<String, AnimatorRecord> records;
    private String currentAnimation;
    private boolean isFlipped = false;
    private boolean isFlopped = false;
    private boolean isPaused = false;
    private List<AnimatorNotificationSubscriber> listeners;
    
    
    public Animator()
    {
        records = new HashMap<String, AnimatorRecord>();
        listeners = new ArrayList<AnimatorNotificationSubscriber>();
        currentAnimation = null;
    }
    
    
    public void update(float delta)
    {
        if (currentAnimation != null && !isPaused)
        {
            AnimatorRecord r = records.get(currentAnimation);
            r.sprite.update(delta);
            if (r.sprite.isFinished() && r.type == AnimatorType.NO_LOOP_NEXT)
            {
                if (r.next != null)
                    goToAnimation(r.next);
                else
                    goToAnimation(r.out);
            }
        }
    }
    
    
    public void draw(CSpriteBatch sb)
    {
        if (currentAnimation != null)
        {
            records.get(currentAnimation).sprite.draw(sb);
        }
    }
    
    
    public Animator addAnimation(String name, CSprite sprite, AnimatorType type, String next, String out)
    {
        if (currentAnimation == null)
            currentAnimation = name;
        switch (type)
        {
            case LOOP_WAIT:
                sprite.setShouldLoop(true);
                break;
            case NO_LOOP_NEXT:
                sprite.setShouldLoop(false);
                break;
            case NO_LOOP_WAIT:
                sprite.setShouldLoop(false);
                break;
            default:
                break;
        
        }
        records.put(name, new AnimatorRecord(sprite, type, next, out));
        return this;
    }
    
    
    public Animator addAnimation(String name, CSprite sprite, AnimatorType type)
    {
        return addAnimation(name, sprite, type, null, null);
    }
    
    
    public Animator addLoopAnimation(String name, CSprite sprite)
    {
        return addAnimation(name, sprite, AnimatorType.LOOP_WAIT, null, null);
    }
    
    
    public Animator addNoLoopWaitAnimation(String name, CSprite sprite, String next, String out)
    {
        return addAnimation(name, sprite, AnimatorType.NO_LOOP_WAIT, next, out);
    }
    
    
    public Animator addNoLoopNextAnimation(String name, CSprite sprite, String next, String out)
    {
        return addAnimation(name, sprite, AnimatorType.NO_LOOP_NEXT, next, out);
    }
    
    
    public Animator goToAnimation(String name)
    {
        for (AnimatorNotificationSubscriber listener : listeners)
        {
            if (listener != null)
                listener.animationNotification(currentAnimation, name);
        }
        currentAnimation = name;
        if (name != null)
            records.get(name).sprite.resetAnimation();
        return this;
    }
    
    
    public Animator goToNextAnimation()
    {
        if (currentAnimation != null)
        {
            AnimatorRecord r = records.get(currentAnimation);
            if (r.next != null)
                goToAnimation(r.next);
            else
                goToAnimation(r.out);
        }
        return this;
    }
    
    
    public Animator goToOutAnimation()
    {
        if (currentAnimation != null)
        {
            AnimatorRecord r = records.get(currentAnimation);
            goToAnimation(r.out);
        }
        return this;
    }
    
    
    public Animator addListener(AnimatorNotificationSubscriber listener)
    {
        listeners.add(listener);
        return this;
    }
    
    
    public String getCurrentAnimation()
    {
        return currentAnimation;
    }
    
    
    public CSprite getSprite(String name)
    {
        return records.get(name).sprite;
    }
    
    
    public Animator setSprite(String name, CSprite sprite)
    {
        records.get(name).sprite = sprite;
        return this;
    }
    
    
    public Animator setType(String name, AnimatorType type)
    {
        records.get(name).type = type;
        return this;
    }
    
    
    public Animator setNext(String name, String next)
    {
        records.get(name).next = next;
        return this;
    }
    
    
    public Animator setOut(String name, String out)
    {
        records.get(name).out = out;
        return this;
    }
    
    
    public boolean getIsFlipped()
    {
        return isFlipped;
    }
    
    
    public Animator setFlipped(boolean isFlipped)
    {
        this.isFlipped = isFlipped;
        for (String name : records.keySet())
        {
            records.get(name).sprite.setFlipped(isFlipped);
        }
        return this;
    }
    
    
    public boolean getIsFlopped()
    {
        return isFlopped;
    }
    
    
    public Animator setFlopped(boolean isFlopped)
    {
        this.isFlopped = isFlopped;
        for (String name : records.keySet())
        {
            records.get(name).sprite.setFlopped(isFlopped);
        }
        return this;
    }
    
    
    public boolean getIsPaused()
    {
        return isPaused;
    }
    
    
    public Animator setPaused(boolean isPaused)
    {
        this.isPaused = isPaused;
        return this;
    }
    
    
    private class AnimatorRecord
    {
        public CSprite sprite;
        public AnimatorType type;
        public String next;
        public String out;
        
        
        public AnimatorRecord(CSprite sprite, AnimatorType type, String next, String out)
        {
            this.sprite = sprite;
            this.type = type;
            this.next = next;
            this.out = out;
        }
    }
    
    
    public enum AnimatorType
    {
        LOOP_WAIT, NO_LOOP_WAIT, NO_LOOP_NEXT
    }
    
    
    public interface AnimatorNotificationSubscriber
    {
        public void animationNotification(String oldAnimation, String newAnimation);
    }
}
