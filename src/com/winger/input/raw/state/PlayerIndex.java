package com.winger.input.raw.state;

/**
 * Enum for player index
 *
 * @author Michael P. Wingfield
 * @copyright Nov 4, 2014
 *
 */
public enum PlayerIndex
{
    ONE(0), TWO(1), THREE(2), FOUR(3), ;
    
    public int index = 0;
    
    
    private PlayerIndex(int index)
    {
        this.index = index;
    }
}
