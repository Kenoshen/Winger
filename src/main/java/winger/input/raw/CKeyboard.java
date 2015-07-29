package com.winger.input.raw;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.winger.input.delegate.CKeyboardEventHandler;
import com.winger.input.raw.state.ButtonState;
import com.winger.input.raw.state.KeyboardKey;
import com.winger.input.raw.state.KeyboardState;
import com.winger.utils.Notifiers;
import com.winger.utils.SubscriptionRecords;

/**
 * Wrapper class for keyboard, allows for event subscription and notification as well as state checking
 *
 * @author Michael P. Wingfield
 * @copyright Nov 4, 2014
 *
 */
public class CKeyboard
{
    private KeyboardState _state;
    
    
    public KeyboardState state()
    {
        return _state;
    }
    
    
    private Notifiers.Notifier3<CKeyboardEventHandler, KeyboardKey, ButtonState> _keyNotifier;
    
    
    public Notifiers.Notifier3<CKeyboardEventHandler, KeyboardKey, ButtonState> keyNotifier()
    {
        return _keyNotifier;
    }
    
    
    private static final int STATES_TO_KEEP = 30;
    private static final int FRAMES_BETWEEN_PRESS = 5;
    private int frameCounter = 0;
    private List<KeyboardState> heldKeys = new ArrayList<KeyboardState>();
    
    private KeyboardState lastKeysDown;
    private KeyboardState curKeysDown;
    
    
    public CKeyboard()
    {
        _keyNotifier = Notifiers.notifier3();
        _state = getCurrentState();
        lastKeysDown = getCurrentState();
        curKeysDown = getCurrentState();
    }
    
    
    public void update()
    {
        _state = getCurrentState();
        addLastKeysToHeld(lastKeysDown);
        lastKeysDown = curKeysDown;
        curKeysDown = _state;
        if (!keyNotifier().isEmpty())
        {
            doKeyEvents();
        }
        frameCounter++;
        if (frameCounter > FRAMES_BETWEEN_PRESS)
        {
            frameCounter = 0;
            if (heldKeys.get(heldKeys.size() - 1).keysDown.size() > 0)
            {
                // TODO: this is where the repeating keys happen
                // callHeldKeysJustDown();
            }
        }
    }
    
    
    public boolean isKeyBeingPressed(KeyboardKey key)
    {
        return contains(curKeysDown, key);
    }
    
    
    public boolean isKeyJustPressed(KeyboardKey key)
    {
        if (contains(curKeysDown, key) && !contains(lastKeysDown, key))
            return true;
        else
            return false;
    }
    
    
    public boolean isKeyJustReleased(KeyboardKey key)
    {
        if (!contains(curKeysDown, key) && contains(lastKeysDown, key))
            return true;
        else
            return false;
    }
    
    
    public boolean isKeyBeingHeld(KeyboardKey key)
    {
        if (contains(curKeysDown, key) && contains(lastKeysDown, key))
            return true;
        else
            return false;
    }
    
    
    public KeyboardState currentKeysDown()
    {
        return curKeysDown;
    }
    
    
    public KeyboardState lastKeysDown()
    {
        return lastKeysDown;
    }
    
    
    public List<KeyboardKey> keysJustDown()
    {
        List<KeyboardKey> keysJustDown = new ArrayList<KeyboardKey>();
        for (int i = 0; i < curKeysDown.keysDown.size(); i++)
        {
            if (!contains(lastKeysDown, curKeysDown.keysDown.get(i)))
            {
                keysJustDown.add(curKeysDown.keysDown.get(i));
            }
        }
        return keysJustDown;
    }
    
    
    public List<KeyboardKey> keysJustUp()
    {
        List<KeyboardKey> keysJustUp = new ArrayList<KeyboardKey>();
        for (int i = 0; i < lastKeysDown.keysDown.size(); i++)
        {
            if (!contains(curKeysDown, lastKeysDown.keysDown.get(i)))
            {
                keysJustUp.add(lastKeysDown.keysDown.get(i));
            }
        }
        return keysJustUp;
    }
    
    
    private boolean contains(KeyboardState keys, KeyboardKey key)
    {
        for (int i = 0; i < keys.keysDown.size(); i++)
            if (keys.keysDown.get(i) == key)
                return true;
        return false;
    }
    
    
    private void doKeyEvents()
    {
        for (KeyboardKey lastkey : lastKeysDown.keysDown)
        {
            if (!contains(curKeysDown, lastkey))
            {
                notifyButtonEventHandlers(lastkey, ButtonState.UP);
            }
        }
        
        for (KeyboardKey curkey : curKeysDown.keysDown)
        {
            if (!contains(lastKeysDown, curkey))
            {
                notifyButtonEventHandlers(curkey, ButtonState.DOWN);
            }
        }
    }
    
    
    /**
     * Sends an event to the key event handlers
     * 
     * @param key the key
     * @param state the state of the button
     */
    public void notifyButtonEventHandlers(KeyboardKey key, ButtonState state)
    {
        List<SubscriptionRecords.SubscriptionRecord3<CKeyboardEventHandler, KeyboardKey, ButtonState>> subsToNotify = keyNotifier()
            .getSubscribersToNotify(key, state);
        for (SubscriptionRecords.SubscriptionRecord3<CKeyboardEventHandler, KeyboardKey, ButtonState> record : subsToNotify)
        {
            record.handler.handleKeyEvent(this, key, state);
        }
    }
    
    
    /**
     * Subscribes to all keyboard event types with a given state
     * 
     * @param handler the handler to register
     * @param state the button state to register for
     */
    public void subscribeToAllKeyboardEvents(CKeyboardEventHandler handler, ButtonState state)
    {
        for (KeyboardKey key : KeyboardKey.values())
        {
            if (!key.isAlternate())
            {
                keyNotifier().subscribeToEvent(handler, key, state);
            }
        }
    }
    
    
    private KeyboardState getCurrentState()
    {
        KeyboardState k = new KeyboardState();
        List<KeyboardKey> tempList = new ArrayList<KeyboardKey>();
        for (KeyboardKey key : KeyboardKey.values())
        {
            if (!key.isAlternate() && Gdx.input.isKeyPressed(key.index()))
            {
                tempList.add(key);
            }
        }
        k.keysDown = tempList;
        return k;
    }
    
    
    private void addLastKeysToHeld(KeyboardState state)
    {
        List<KeyboardKey> markForDelete = new ArrayList<KeyboardKey>();
        for (KeyboardState other : heldKeys)
        {
            for (KeyboardKey key : markForDelete)
            {
                other.keysDown.remove(key);
            }
            for (KeyboardKey key : other.keysDown)
            {
                if (!contains(state, key))
                {
                    markForDelete.add(key);
                }
            }
        }
        heldKeys.add(0, state);
        if (heldKeys.size() > STATES_TO_KEEP)
        {
            heldKeys.remove(heldKeys.size() - 1);
        }
    }
    
    
    private void callHeldKeysJustDown()
    {
        for (KeyboardKey key : heldKeys.get(heldKeys.size() - 1).keysDown)
        {
            notifyButtonEventHandlers(key, ButtonState.DOWN);
        }
    }
}
