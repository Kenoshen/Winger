package com.winger.input.delegate;

import com.badlogic.gdx.math.Vector2;
import com.winger.input.raw.CMouse;
import com.winger.input.raw.state.ButtonState;
import com.winger.input.raw.state.CMouseButton;
import com.winger.input.raw.state.CMouseDrag;

/**
 * Used for Mouse events
 *
 * @author Michael P. Wingfield
 * @copyright Nov 4, 2014
 *
 */
public interface CMouseEventHandler
{
    void handleClickEvent(CMouse mouse, CMouseButton button, ButtonState state);


    void handleScrollEvent(CMouse mouse, float difference);


    void handleMoveEvent(CMouse mouse, Vector2 position);


    void handleDragEvent(CMouse mouse, CMouseButton button, CMouseDrag state);
}
