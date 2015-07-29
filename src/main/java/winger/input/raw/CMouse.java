package com.winger.input.raw;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.math.Vector2;
import com.winger.input.delegate.CMouseEventHandler;
import com.winger.input.raw.state.ButtonState;
import com.winger.input.raw.state.CMouseButton;
import com.winger.input.raw.state.CMouseDrag;
import com.winger.input.raw.state.CMousePosition;
import com.winger.input.raw.state.CMouseScroll;
import com.winger.input.raw.state.MouseState;
import com.winger.utils.Notifiers;
import com.winger.utils.SubscriptionRecords;

/**
 * Wrapper class for mouse input, allows for event subscription and notification as well as state checking
 *
 * @author Michael P. Wingfield
 * @copyright Nov 4, 2014
 *
 */
public class CMouse
{
    public static final CMouse instance = new CMouse();
    //
    private MouseState _state = null;
    
    
    public MouseState state()
    {
        return _state;
    }
    
    
    private MouseState _lastState = null;
    
    
    public MouseState lastState()
    {
        return _lastState;
    }
    
    
    private Notifiers.Notifier3<CMouseEventHandler, CMouseButton, ButtonState> _clickNotifier;
    
    
    public Notifiers.Notifier3<CMouseEventHandler, CMouseButton, ButtonState> clickNotifier()
    {
        return _clickNotifier;
    }
    
    
    private Notifiers.Notifier2<CMouseEventHandler, CMouseScroll> _scrollNotifier;
    
    
    public Notifiers.Notifier2<CMouseEventHandler, CMouseScroll> scrollNotifier()
    {
        return _scrollNotifier;
    }
    
    
    private Notifiers.Notifier2<CMouseEventHandler, CMousePosition> _moveNotifier;
    
    
    public Notifiers.Notifier2<CMouseEventHandler, CMousePosition> moveNotifier()
    {
        return _moveNotifier;
    }
    
    
    private Notifiers.Notifier3<CMouseEventHandler, CMouseButton, CMouseDrag> _dragNotifier;
    
    
    public Notifiers.Notifier3<CMouseEventHandler, CMouseButton, CMouseDrag> dragNotifier()
    {
        return _dragNotifier;
    }
    
    
    private static CMouseButton[] buttonsEnum = CMouseButton.values();
    private Map<CMouseButton, Vector2> downPos = new HashMap<CMouseButton, Vector2>();
    private Map<CMouseButton, Vector2> upPos = new HashMap<CMouseButton, Vector2>();
    private Map<CMouseButton, Vector2> lastPos = new HashMap<CMouseButton, Vector2>();
    private Map<CMouseButton, Boolean> heldNotDragged = new HashMap<CMouseButton, Boolean>();
    private Map<CMouseButton, Boolean> dragged = new HashMap<CMouseButton, Boolean>();
    
    private float dragBreakDist = 5;
    
    
    public Vector2 position()
    {
        return new Vector2(_state.x, _state.y);
    }
    
    
    public void position(Vector2 value)
    {
        _state.x = value.x;
        _state.y = value.y;
        Gdx.input.setCursorPosition((int) value.x, (int) value.y);
    }
    
    
    public int x()
    {
        return (int) _state.x;
    }
    
    
    public void x(int value)
    {
        _state.x = value;
        Gdx.input.setCursorPosition(value, (int) _state.y);
    }
    
    
    public int y()
    {
        return (int) _state.y;
    }
    
    
    public void y(int value)
    {
        _state.y = value;
        Gdx.input.setCursorPosition((int) _state.x, value);
    }
    
    
    public CMouse()
    {
        _state = getCurrentState();
        
        _clickNotifier = Notifiers.notifier3();
        _scrollNotifier = Notifiers.notifier2();
        _moveNotifier = Notifiers.notifier2();
        _dragNotifier = Notifiers.notifier3();
        
        downPos.put(CMouseButton.LEFT, new Vector2(0, 0));
        downPos.put(CMouseButton.RIGHT, new Vector2(0, 0));
        downPos.put(CMouseButton.MIDDLE, new Vector2(0, 0));
        upPos.put(CMouseButton.LEFT, new Vector2(0, 0));
        upPos.put(CMouseButton.RIGHT, new Vector2(0, 0));
        upPos.put(CMouseButton.MIDDLE, new Vector2(0, 0));
        heldNotDragged.put(CMouseButton.LEFT, false);
        heldNotDragged.put(CMouseButton.RIGHT, false);
        heldNotDragged.put(CMouseButton.MIDDLE, false);
        dragged.put(CMouseButton.LEFT, false);
        dragged.put(CMouseButton.RIGHT, false);
        dragged.put(CMouseButton.MIDDLE, false);
    }
    
    
    public void update()
    {
        _lastState = _state;
        _state = getCurrentState();
        
        if (!clickNotifier().isEmpty())
        {
            checkClickStatesAndDoEvents();
        }
        if (!scrollNotifier().isEmpty())
        {
            checkScrollStateAndDoEvent();
        }
        if (!moveNotifier().isEmpty())
        {
            checkMoveStateAndDoEvent();
        }
        
        Vector2 pos = position();
        for (CMouseButton button : buttonsEnum)
        {
            if (justPressed(button))
            {
                downPos.put(button, pos);
                heldNotDragged.put(button, true);
            }
            if (held(button))
            {
                if (heldNotDragged.get(button))
                {
                    if (pos.dst(downPos.get(button)) < dragBreakDist)
                    {
                        // still holding but not moved past the drag break dist
                    } else
                    {
                        heldNotDragged.put(button, false);
                        dragged.put(button, true);
                        // drag begin event
                        notifyDragEventHandlers(button, CMouseDrag.BEGIN);
                    }
                } else if (!pos.equals(lastPos.get(button)))
                {
                    lastPos.put(button, pos);
                    notifyDragEventHandlers(button, CMouseDrag.MOVE);
                }
            }
            if (justReleased(button))
            {
                upPos.put(button, pos);
                heldNotDragged.put(button, false);
                if (dragged.get(button))
                {
                    dragged.put(button, false);
                    // drag end event
                    notifyDragEventHandlers(button, CMouseDrag.END);
                }
            }
        }
    }
    
    
    public boolean justPressed(CMouseButton button)
    {
        switch (button)
        {
            case LEFT:
                return (_state.left == ButtonState.DOWN && _lastState.left == ButtonState.UP);
                
            case RIGHT:
                return (_state.right == ButtonState.DOWN && _lastState.right == ButtonState.UP);
                
            case MIDDLE:
                return (_state.middle == ButtonState.DOWN && _lastState.middle == ButtonState.UP);
        }
        return false;
    }
    
    
    public boolean justReleased(CMouseButton button)
    {
        switch (button)
        {
            case LEFT:
                return (_state.left == ButtonState.UP && _lastState.left == ButtonState.DOWN);
                
            case RIGHT:
                return (_state.right == ButtonState.UP && _lastState.right == ButtonState.DOWN);
                
            case MIDDLE:
                return (_state.middle == ButtonState.UP && _lastState.middle == ButtonState.DOWN);
        }
        return false;
    }
    
    
    public boolean held(CMouseButton button)
    {
        switch (button)
        {
            case LEFT:
                return (_state.left == ButtonState.DOWN && _lastState.left == ButtonState.DOWN);
                
            case RIGHT:
                return (_state.right == ButtonState.DOWN && _lastState.right == ButtonState.DOWN);
                
            case MIDDLE:
                return (_state.middle == ButtonState.DOWN && _lastState.middle == ButtonState.DOWN);
        }
        return false;
    }
    
    
    public float getScrollDifference()
    {
        return _state.scroll - _lastState.scroll;
    }
    
    
    public Vector2 getPositionDifference()
    {
        return new Vector2(_state.x - _lastState.x, _state.y - _lastState.y);
    }
    
    
    public Vector2 getLastDownPosition(CMouseButton button)
    {
        return downPos.get(button);
    }
    
    
    public Vector2 getLastUpPosition(CMouseButton button)
    {
        return upPos.get(button);
    }
    
    
    private void checkClickStatesAndDoEvents()
    {
        if (_lastState.left != _state.left)
        {
            notifyClickEventHandlers(CMouseButton.LEFT, _state.left);
        }
        if (_lastState.right != _state.right)
        {
            notifyClickEventHandlers(CMouseButton.RIGHT, _state.right);
        }
        if (_lastState.middle != _state.middle)
        {
            notifyClickEventHandlers(CMouseButton.MIDDLE, _state.middle);
        }
    }
    
    
    private void checkScrollStateAndDoEvent()
    {
        if (_lastState.scroll != _state.scroll)
        {
            notifyScrollEventHandlers(CMouseScroll.CHANGED);
        }
    }
    
    
    private void checkMoveStateAndDoEvent()
    {
        if (_lastState.x != _state.x || _lastState.y != _state.y)
        {
            notifyMoveEventHandlers(CMousePosition.CHANGED);
        }
    }
    
    
    /**
     * Sends an event to the click event handlers
     * 
     * @param button the button
     * @param state the state of the button
     */
    public void notifyClickEventHandlers(CMouseButton button, ButtonState state)
    {
        List<SubscriptionRecords.SubscriptionRecord3<CMouseEventHandler, CMouseButton, ButtonState>> subsToNotify = clickNotifier()
            .getSubscribersToNotify(button, state);
        for (SubscriptionRecords.SubscriptionRecord3<CMouseEventHandler, CMouseButton, ButtonState> record : subsToNotify)
        {
            record.handler.handleClickEvent(this, button, state);
        }
    }
    
    
    /**
     * Sends an event to the scroll event handlers
     * 
     * @param state the state of the scroll wheel
     */
    public void notifyScrollEventHandlers(CMouseScroll state)
    {
        List<SubscriptionRecords.SubscriptionRecord2<CMouseEventHandler, CMouseScroll>> subsToNotify = scrollNotifier().getSubscribersToNotify(state);
        float diff = getScrollDifference();
        for (SubscriptionRecords.SubscriptionRecord2<CMouseEventHandler, CMouseScroll> record : subsToNotify)
        {
            record.handler.handleScrollEvent(this, diff);
        }
    }
    
    
    /**
     * Sends an event to the move event handlers
     * 
     * @param state the state of the mouse position
     */
    public void notifyMoveEventHandlers(CMousePosition state)
    {
        List<SubscriptionRecords.SubscriptionRecord2<CMouseEventHandler, CMousePosition>> subsToNotify = moveNotifier().getSubscribersToNotify(state);
        Vector2 pos = position();
        for (SubscriptionRecords.SubscriptionRecord2<CMouseEventHandler, CMousePosition> record : subsToNotify)
        {
            record.handler.handleMoveEvent(this, pos);
        }
    }
    
    
    /**
     * Sends an event to the drag event handlers
     * 
     * @param button the button
     * @param state the state of the drag
     */
    public void notifyDragEventHandlers(CMouseButton button, CMouseDrag state)
    {
        List<SubscriptionRecords.SubscriptionRecord3<CMouseEventHandler, CMouseButton, CMouseDrag>> subsToNotify = dragNotifier()
            .getSubscribersToNotify(button, state);
        for (SubscriptionRecords.SubscriptionRecord3<CMouseEventHandler, CMouseButton, CMouseDrag> record : subsToNotify)
        {
            record.handler.handleDragEvent(this, button, state);
        }
    }
    
    
    private MouseState getCurrentState()
    {
        MouseState s = new MouseState();
        s = new MouseState();
        s.x = Gdx.input.getX();
        s.y = (Gdx.input.getY() * -1) + Gdx.graphics.getHeight();
        // Gdx.input.getInputProcessor().sc
        s.left = (Gdx.input.isButtonPressed(Buttons.LEFT) ? ButtonState.DOWN : ButtonState.UP);
        s.right = (Gdx.input.isButtonPressed(Buttons.RIGHT) ? ButtonState.DOWN : ButtonState.UP);
        s.middle = (Gdx.input.isButtonPressed(Buttons.MIDDLE) ? ButtonState.DOWN : ButtonState.UP);
        return s;
    }
    
    
    public void subscribeToAllMouseEvents(CMouseEventHandler handler)
    {
        subscribeToAllMouseClickEvents(handler);
        subscribeToAllMouseDragEvents(handler);
        subscribeToAllMouseMoveEvents(handler);
        subscribeToAllMouseScrollEvents(handler);
    }
    
    
    public void subscribeToAllMouseClickEvents(CMouseEventHandler handler)
    {
        clickNotifier().subscribeToEvent(handler, CMouseButton.LEFT, ButtonState.UP);
        clickNotifier().subscribeToEvent(handler, CMouseButton.LEFT, ButtonState.DOWN);
        clickNotifier().subscribeToEvent(handler, CMouseButton.RIGHT, ButtonState.UP);
        clickNotifier().subscribeToEvent(handler, CMouseButton.RIGHT, ButtonState.DOWN);
    }
    
    
    public void subscribeToAllMouseDragEvents(CMouseEventHandler handler)
    {
        dragNotifier().subscribeToEvent(handler, CMouseButton.LEFT, CMouseDrag.BEGIN);
        dragNotifier().subscribeToEvent(handler, CMouseButton.LEFT, CMouseDrag.MOVE);
        dragNotifier().subscribeToEvent(handler, CMouseButton.LEFT, CMouseDrag.END);
        dragNotifier().subscribeToEvent(handler, CMouseButton.RIGHT, CMouseDrag.BEGIN);
        dragNotifier().subscribeToEvent(handler, CMouseButton.RIGHT, CMouseDrag.MOVE);
        dragNotifier().subscribeToEvent(handler, CMouseButton.RIGHT, CMouseDrag.END);
        dragNotifier().subscribeToEvent(handler, CMouseButton.MIDDLE, CMouseDrag.BEGIN);
        dragNotifier().subscribeToEvent(handler, CMouseButton.MIDDLE, CMouseDrag.MOVE);
        dragNotifier().subscribeToEvent(handler, CMouseButton.MIDDLE, CMouseDrag.END);
    }
    
    
    public void subscribeToAllMouseMoveEvents(CMouseEventHandler handler)
    {
        moveNotifier().subscribeToEvent(handler, CMousePosition.CHANGED);
    }
    
    
    public void subscribeToAllMouseScrollEvents(CMouseEventHandler handler)
    {
        scrollNotifier().subscribeToEvent(handler, CMouseScroll.CHANGED);
    }
}
