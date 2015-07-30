package com.winger.input.raw.state;

import com.badlogic.gdx.math.Vector2;

/**
 * Gamepad state access class
 *
 * @author Michael P. Wingfield
 * @copyright Nov 4, 2014
 *
 */
public class GamePadState
{
    public PlayerIndex playerIndex = PlayerIndex.ONE;
    public ButtonState a = ButtonState.UP;
    public ButtonState b = ButtonState.UP;
    public ButtonState x = ButtonState.UP;
    public ButtonState y = ButtonState.UP;
    public ButtonState start = ButtonState.UP;
    public ButtonState back = ButtonState.UP;
    public ButtonState leftBumper = ButtonState.UP;
    public ButtonState rightBumper = ButtonState.UP;
    public ButtonState leftStickClick = ButtonState.UP;
    public ButtonState leftStickLeft = ButtonState.UP;
    public ButtonState leftStickRight = ButtonState.UP;
    public ButtonState leftStickUp = ButtonState.UP;
    public ButtonState leftStickDown = ButtonState.UP;
    public ButtonState rightStickClick = ButtonState.UP;
    public ButtonState rightStickLeft = ButtonState.UP;
    public ButtonState rightStickRight = ButtonState.UP;
    public ButtonState rightStickUp = ButtonState.UP;
    public ButtonState rightStickDown = ButtonState.UP;
    public ButtonState xbox = ButtonState.UP;
    public ButtonState left = ButtonState.UP;
    public ButtonState right = ButtonState.UP;
    public ButtonState up = ButtonState.UP;
    public ButtonState down = ButtonState.UP;
    public ButtonState leftTriggerClick = ButtonState.UP;
    public ButtonState rightTriggerClick = ButtonState.UP;
    public float leftTrigger = 0;
    public float rightTrigger = 0;
    public Vector2 leftStick = new Vector2();
    public Vector2 rightStick = new Vector2();
    
    
    public ButtonState statusForGamePadButton(CGamePadButton button)
    {
        switch (button)
        {
            case A:
                return a;
            case B:
                return b;
            case BACK:
                return back;
            case DOWN:
                return down;
            case LB:
                return leftBumper;
            case LEFT:
                return left;
            case LS:
                return leftStickClick;
            case LS_DOWN:
                return leftStickDown;
            case LS_LEFT:
                return leftStickLeft;
            case LS_RIGHT:
                return leftStickRight;
            case LS_UP:
                return leftStickUp;
            case LT:
                return leftTriggerClick;
            case RB:
                return rightBumper;
            case RIGHT:
                return right;
            case RS:
                return rightStickClick;
            case RS_DOWN:
                return rightStickDown;
            case RS_LEFT:
                return rightStickLeft;
            case RS_RIGHT:
                return rightStickRight;
            case RS_UP:
                return rightStickUp;
            case RT:
                return rightTriggerClick;
            case START:
                return start;
            case UP:
                return up;
            case X:
                return x;
            case XBOX:
                return xbox;
            case Y:
                return y;
            default:
                return ButtonState.UP;
                
        }
    }
    
}
