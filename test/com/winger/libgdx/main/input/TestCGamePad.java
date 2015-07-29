package com.winger.libgdx.main.input;

import com.badlogic.gdx.math.Vector2;
import com.winger.input.delegate.CGamePadEventHandler;
import com.winger.input.raw.CGamePad;
import com.winger.input.raw.state.ButtonState;
import com.winger.input.raw.state.CGamePadButton;
import com.winger.input.raw.state.CGamePadStick;
import com.winger.input.raw.state.CGamePadTrigger;
import com.winger.log.HTMLLogger;
import com.winger.log.LogGroup;

public class TestCGamePad implements CGamePadEventHandler
{
    private static final HTMLLogger log = HTMLLogger.getLogger(TestCGamePad.class, LogGroup.Framework, LogGroup.Assert);
    CGamePad gamePad;
    
    
    public void update()
    {
        gamePad.update();
    }
    
    
    public void initGamePad()
    {
        gamePad = new CGamePad();
    }
    
    
    public void subscribeToGamePadButtons(ButtonState state)
    {
        for (CGamePadButton button : CGamePadButton.values())
        {
            gamePad.buttonNotifier().subscribeToEvent(this, button, state);
        }
    }
    
    
    public void subscribeToGamePadSticks()
    {
        gamePad.thumbStickNotifier().subscribeToEvent(this, CGamePadStick.LEFT);
        gamePad.thumbStickNotifier().subscribeToEvent(this, CGamePadStick.RIGHT);
    }
    
    
    public void subscribeToGamePadTriggers()
    {
        gamePad.triggerNotifier().subscribeToEvent(this, CGamePadTrigger.LEFT);
        gamePad.triggerNotifier().subscribeToEvent(this, CGamePadTrigger.RIGHT);
    }
    
    
    @Override
    public void handleButtonEvent(CGamePad gamePad, CGamePadButton button, ButtonState state)
    {
        log.info(gamePad.playerNum().name() + " " + button.name() + " " + state.name());
    }
    
    
    @Override
    public void handleTriggerEvent(CGamePad gamePad, CGamePadTrigger trigger, float state)
    {
        log.info(gamePad.playerNum().name() + " " + trigger.name() + "_TRIGGER " + state);
    }
    
    
    @Override
    public void handleStickEvent(CGamePad gamePad, CGamePadStick stick, Vector2 state)
    {
        log.info(gamePad.playerNum().name() + " " + stick.name() + "_STICK " + state);
    }
}
