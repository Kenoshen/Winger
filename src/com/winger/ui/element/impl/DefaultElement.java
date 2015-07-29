package com.winger.ui.element.impl;

import com.winger.ui.Element;
import com.winger.ui.ElementRecord;
import com.winger.ui.Page;

/**
 * A default implementation of the Element class. Used when a type is unknown to the ElementFactory or when a type is not given.
 *
 * @author Michael P. Wingfield
 * @copyright Nov 4, 2014
 *
 */
public class DefaultElement extends Element<DefaultElement>
{
    
    public DefaultElement(Page page, ElementRecord record, Element<?> parent)
    {
        super(page, record, parent);
    }
}
