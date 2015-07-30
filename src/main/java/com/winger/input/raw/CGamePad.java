package com.winger.input.raw;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.math.Vector2;
import com.winger.input.delegate.CGamePadEventHandler;
import com.winger.input.raw.state.*;
import com.winger.log.HTMLLogger;
import com.winger.log.LogGroup;
import com.winger.utils.Notifiers;
import com.winger.utils.SubscriptionRecords;

import java.util.List;

/**
 * Wrapper class for GamePad, allows for event subscription and notification as well as state checking
 *
 * @author Michael P. Wingfield
 * @copyright Nov 4, 2014
 *
 */
public class CGamePad
{
    private static final HTMLLogger log = HTMLLogger.getLogger(CGamePad.class, LogGroup.Framework, LogGroup.Input);
    public Controller controller;
    //
    public Notifiers.Notifier2<CGamePadEventHandler, CGamePadStick> _thumbStickNotifier;
    public Notifiers.Notifier2<CGamePadEventHandler, CGamePadTrigger> _triggerNotifier;
    public Notifiers.Notifier3<CGamePadEventHandler, CGamePadButton, ButtonState> _buttonNotifier;
    private GamePadState _last;
    private GamePadState _cur;
    private PlayerIndex playerIndex;


    public CGamePad()
    {
        initialize(PlayerIndex.ONE);
    }


    public CGamePad(PlayerIndex playerIndex)
    {
        initialize(playerIndex);
    }

    public GamePadState state()
    {
        return _cur;
    }

    public PlayerIndex playerNum()
    {
        return playerIndex;
    }

    public boolean isConnected()
    {
        return (controller != null);
    }

    public Notifiers.Notifier3<CGamePadEventHandler, CGamePadButton, ButtonState> buttonNotifier()
    {
        return _buttonNotifier;
    }

    public Notifiers.Notifier2<CGamePadEventHandler, CGamePadTrigger> triggerNotifier()
    {
        return _triggerNotifier;
    }

    public Notifiers.Notifier2<CGamePadEventHandler, CGamePadStick> thumbStickNotifier()
    {
        return _thumbStickNotifier;
    }
    
    private void initialize(PlayerIndex playerIndex)
    {
        try
        {
            controller = Controllers.getControllers().get(playerIndex.index);
        } catch (Exception e)
        {
            log.debug("Could not create controller with player index: " + playerIndex.name());
            controller = null;
        }
        this.playerIndex = playerIndex;
        getCurrentState();
        getCurrentState();
        _buttonNotifier = Notifiers.notifier3();
        _triggerNotifier = Notifiers.notifier2();
        _thumbStickNotifier = Notifiers.notifier2();
    }
    
    
    /**
     * Updates the GamePad state and sends events to event handlers if necessary
     */
    public void update()
    {
        getCurrentState();
        if (!_buttonNotifier.isEmpty())
        {
            doButtonEvents();
        }
        if (!_triggerNotifier.isEmpty())
        {
            doTriggerEvents();
        }
        if (!_thumbStickNotifier.isEmpty())
        {
            doStickEvents();
        }
    }
    
    
    /**
     * Checks if the current state of the button is pressed
     * 
     * @param button the button to check
     * @return true if the button is pressed
     */
    public boolean isDown(CGamePadButton button)
    {
        return _cur.statusForGamePadButton(button) == ButtonState.DOWN;
    }
    
    
    /**
     * Checks if the current state of the button is released
     * 
     * @param button the button to check
     * @return true if the button is released
     */
    public boolean isUp(CGamePadButton button)
    {
        return !isDown(button);
    }
    
    
    /**
     * Checks if the current state of the button is different then the last state
     * 
     * @param button the button to check
     * @return true if the current button state is different then the last state
     */
    public boolean isChanged(CGamePadButton button)
    {
        return (_cur.statusForGamePadButton(button) != _last.statusForGamePadButton(button));
    }
    
    
    /**
     * Checks if the current trigger state is different then the last trigger state
     *
     * @param trigger the trigger to check
     * @return true if the trigger changed
     */
    public boolean isChanged(CGamePadTrigger trigger)
    {
        if (trigger == CGamePadTrigger.LEFT)
        {
            return (_cur.leftTrigger != _last.leftTrigger);
        } else if (trigger == CGamePadTrigger.RIGHT)
        {
            return (_cur.rightTrigger != _last.rightTrigger);
        }
        return false;
    }
    
    
    /**
     * Checks if the current stick state is different then the last stick state
     * 
     * @param stick the stick to check
     * @return true if the stick changed
     */
    public boolean isChanged(CGamePadStick stick)
    {
        if (stick == CGamePadStick.LEFT)
        {
            return (_cur.leftStick.x != _last.leftStick.x || _cur.leftStick.y != _last.leftStick.y);
        } else if (stick == CGamePadStick.RIGHT)
        {
            return (_cur.rightStick.x != _last.rightStick.x || _cur.rightStick.y != _last.rightStick.y);
        }
        return false;
    }
    
    
    private void doButtonEvents()
    {
        for (CGamePadButton button : CGamePadButton.values())
        {
            if (isChanged(button))
            {
                notifyButtonEventHandlers(button, _cur.statusForGamePadButton(button));
            }
        }
    }
    
    
    /**
     * Sends an event to the button event handlers
     * 
     * @param button the button
     * @param state the state of the button
     */
    public void notifyButtonEventHandlers(CGamePadButton button, ButtonState state)
    {
        List<SubscriptionRecords.SubscriptionRecord3<CGamePadEventHandler, CGamePadButton, ButtonState>> subsToNotify = _buttonNotifier
            .getSubscribersToNotify(button, state);
        for (SubscriptionRecords.SubscriptionRecord3<CGamePadEventHandler, CGamePadButton, ButtonState> record : subsToNotify)
        {
            record.handler.handleButtonEvent(this, button, state);
        }
    }
    
    
    private void doTriggerEvents()
    {
        if (isChanged(CGamePadTrigger.LEFT))
        {
            notifyTriggerEventHandlers(CGamePadTrigger.LEFT, _cur.leftTrigger);
        }
        
        if (isChanged(CGamePadTrigger.RIGHT))
        {
            notifyTriggerEventHandlers(CGamePadTrigger.RIGHT, _cur.rightTrigger);
        }
    }
    
    
    /**
     * Sends an event to the trigger event handlers
     * 
     * @param trigger the trigger
     * @param state the state of the trigger
     */
    public void notifyTriggerEventHandlers(CGamePadTrigger trigger, float state)
    {
        List<SubscriptionRecords.SubscriptionRecord2<CGamePadEventHandler, CGamePadTrigger>> subsToNotify = _triggerNotifier
            .getSubscribersToNotify(trigger);
        for (SubscriptionRecords.SubscriptionRecord2<CGamePadEventHandler, CGamePadTrigger> record : subsToNotify)
        {
            record.handler.handleTriggerEvent(this, trigger, state);
        }
    }
    
    
    private void doStickEvents()
    {
        if (isChanged(CGamePadStick.LEFT))
        {
            notifyStickEventHandlers(CGamePadStick.LEFT, _cur.leftStick);
        }
        
        if (isChanged(CGamePadStick.RIGHT))
        {
            notifyStickEventHandlers(CGamePadStick.RIGHT, _cur.rightStick);
        }
    }
    
    
    /**
     * Sends an event to the stick event handlers
     * 
     * @param stick the stick
     * @param state the state of the stick
     */
    public void notifyStickEventHandlers(CGamePadStick stick, Vector2 state)
    {
        List<SubscriptionRecords.SubscriptionRecord2<CGamePadEventHandler, CGamePadStick>> subsToNotify = _thumbStickNotifier
            .getSubscribersToNotify(stick);
        for (SubscriptionRecords.SubscriptionRecord2<CGamePadEventHandler, CGamePadStick> record : subsToNotify)
        {
            record.handler.handleStickEvent(this, stick, state);
        }
    }
    
    
    private GamePadState getCurrentState()
    {
        GamePadState s = new GamePadState();
        if (isConnected())
        {
            s.playerIndex = playerIndex;
            s.a = isButtonDown(CGamePadButton.A.val);
            s.b = isButtonDown(CGamePadButton.B.val);
            s.x = isButtonDown(CGamePadButton.X.val);
            s.y = isButtonDown(CGamePadButton.Y.val);
            s.leftBumper = isButtonDown(CGamePadButton.LB.val);
            s.rightBumper = isButtonDown(CGamePadButton.RB.val);
            s.back = isButtonDown(CGamePadButton.BACK.val);
            s.start = isButtonDown(CGamePadButton.START.val);
            s.xbox = isButtonDown(CGamePadButton.XBOX.val);
            s.leftStickClick = isButtonDown(CGamePadButton.LS.val);
            s.rightStickClick = isButtonDown(CGamePadButton.RS.val);
            s.left = isButtonDown(CGamePadButton.LEFT.val);
            s.right = isButtonDown(CGamePadButton.RIGHT.val);
            s.up = isButtonDown(CGamePadButton.UP.val);
            s.down = isButtonDown(CGamePadButton.DOWN.val);
            s.leftStick = new Vector2(getAxisDeflection(CGamePadButton.LS_LEFT.val), -getAxisDeflection(CGamePadButton.LS_UP.val));
            s.rightStick = new Vector2(getAxisDeflection(CGamePadButton.RS_LEFT.val), -getAxisDeflection(CGamePadButton.RS_UP.val));
            s.leftTrigger = getAxisDeflection(CGamePadButton.LT.val);
            s.rightTrigger = getAxisDeflection(CGamePadButton.RT.val);
            //
            s.leftStickLeft = isFuzzyDown(-s.leftStick.x);
            s.leftStickRight = isFuzzyDown(s.leftStick.x);
            s.leftStickUp = isFuzzyDown(s.leftStick.y);
            s.leftStickDown = isFuzzyDown(-s.leftStick.y);
            s.rightStickLeft = isFuzzyDown(-s.rightStick.x);
            s.rightStickRight = isFuzzyDown(s.rightStick.x);
            s.rightStickUp = isFuzzyDown(s.rightStick.y);
            s.rightStickDown = isFuzzyDown(-s.rightStick.y);
            s.leftTriggerClick = isFuzzyDown(s.leftTrigger);
            s.rightTriggerClick = isFuzzyDown(s.rightTrigger);
        }
        _last = _cur;
        _cur = s;
        //
        return s;
    }
    
    
    private ButtonState isFuzzyDown(float fuzzy)
    {
        return ButtonState.isDown(fuzzy > 0.8f);
    }
    
    
    private ButtonState isButtonDown(int button)
    {
        return ButtonState.isDown(controller.getButton(button));
    }
    
    
    private float getAxisDeflection(int axis)
    {
        return controller.getAxis(axis);
    }
}
