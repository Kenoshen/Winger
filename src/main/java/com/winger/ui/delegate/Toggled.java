package com.winger.ui.delegate;

import com.winger.ui.element.impl.ToggleElement;

/**
 * For notifying that a toggle was performed
 *
 * @author Michael P. Wingfield
 * @copyright Nov 4, 2014
 *
 */
public interface Toggled
{
    boolean toggled(ToggleElement element);
}
