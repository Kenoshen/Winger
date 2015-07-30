package com.winger.input.raw.state;

/**
 * Enum for button events
 *
 * @author Michael P. Wingfield
 * @copyright Nov 4, 2014
 *
 */
public enum ButtonState
{
    UP, DOWN, ;
    
    public static ButtonState isDown(boolean isDown)
    {
        if (isDown)
        {
            return DOWN;
        } else
        {
            return UP;
        }
    }
}
