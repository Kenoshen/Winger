package com.winger.input.raw.state;

/**
 * Basically a struct for MouseState objects
 *
 * @author Michael P. Wingfield
 * @copyright Nov 4, 2014
 *
 */
public class MouseState
{
    public float x = 0;
    public float y = 0;
    public float scroll = 0;
    public ButtonState left = ButtonState.UP;
    public ButtonState right = ButtonState.UP;
    public ButtonState middle = ButtonState.UP;
}
