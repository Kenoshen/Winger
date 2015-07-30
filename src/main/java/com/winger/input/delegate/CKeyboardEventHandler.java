package com.winger.input.delegate;

import com.winger.input.raw.CKeyboard;
import com.winger.input.raw.state.ButtonState;
import com.winger.input.raw.state.KeyboardKey;

/**
 * Used for Keyboard events
 *
 * @author Michael P. Wingfield
 * @copyright Nov 4, 2014
 *
 */
public interface CKeyboardEventHandler
{
    void handleKeyEvent(CKeyboard keyboard, KeyboardKey key, ButtonState state);
}
