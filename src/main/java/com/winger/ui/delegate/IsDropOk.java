package com.winger.ui.delegate;

import com.badlogic.gdx.math.Vector2;
import com.winger.ui.element.impl.DragDropElement;

/**
 * For telling the DragDropElement if a drop area is ok
 *
 * @author Michael P. Wingfield
 * @copyright Nov 4, 2014
 *
 */
public interface IsDropOk
{
    boolean isDropOk(DragDropElement element, Vector2 initialDragPosition);
}
