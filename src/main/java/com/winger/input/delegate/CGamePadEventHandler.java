package com.winger.input.delegate;

import com.badlogic.gdx.math.Vector2;
import com.winger.input.raw.CGamePad;
import com.winger.input.raw.state.ButtonState;
import com.winger.input.raw.state.CGamePadButton;
import com.winger.input.raw.state.CGamePadStick;
import com.winger.input.raw.state.CGamePadTrigger;

/**
 * Used for GamePad events
 *
 * @author Michael P. Wingfield
 * @copyright Nov 4, 2014
 *
 */
public interface CGamePadEventHandler
{
    void handleButtonEvent(CGamePad gamePad, CGamePadButton button, ButtonState state);


    void handleTriggerEvent(CGamePad gamePad, CGamePadTrigger trigger, float state);


    void handleStickEvent(CGamePad gamePad, CGamePadStick stick, Vector2 state);
}
