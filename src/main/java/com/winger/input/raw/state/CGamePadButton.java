package com.winger.input.raw.state;

/**
 * Enum for game pad button events
 *
 * @author Michael P. Wingfield
 * @copyright Nov 4, 2014
 *
 */
public enum CGamePadButton
{
    UP(0),
    DOWN(1),
    LEFT(2),
    RIGHT(3),
    START(4),
    BACK(5),
    LS(6),
    RS(7),
    LB(8),
    RB(9),
    XBOX(10),
    A(11),
    B(12),
    X(13),
    Y(14),
    LT(0),
    RT(1),
    LS_RIGHT(2),
    LS_UP(3),
    LS_DOWN(3),
    RS_LEFT(4),
    RS_RIGHT(4),
    RS_UP(5),
    RS_DOWN(5),
    LS_LEFT(2), ;
    
    public int val;


    CGamePadButton(int val)
    {
        this.val = val;
    }
}
