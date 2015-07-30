package com.winger.ui;

import com.winger.ui.element.impl.*;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * Factory class to create elements with a given type.
 *
 * @author Michael P. Wingfield
 * @copyright Nov 4, 2014
 *
 */
public class ElementFactory
{
    private static Map<String, Class<Element<?>>> registrations = new HashMap<String, Class<Element<?>>>();
    
    
    /**
     * Registers a custom element type to be used when createElement is called
     * 
     * @param typeName can be any string that can be put in a HashMap, used when looking up the type field on an element
     * @param element can be null, can overwrite default with 'default'
     */
    public static void registerElement(String typeName, Class<Element<?>> element)
    {
        registrations.put(typeName, element);
    }
    
    
    /**
     * Creates an element using seed data from a config file. Will attempt to create currently known elements using the type field. If type is not
     * known, then it will look under the ElementFactoryRegistrars registered with this factory. If it cannot find a specific element class, then it
     * will create a default element.
     *
     * @param page
     * @param record
     * @param parent
     * @return
     */
    public static Element<?> createElement(Page page, ElementRecord record, Element<?> parent)
    {
        String type = (String) record.get("type");
        Element<?> elem = null;
        if ("grid".equals(type))
            elem = new GridElement(page, record, parent);
        else if ("textbox".equals(type))
            elem = new TextBoxElement(page, record, parent);
        else if ("scroll".equals(type))
            elem = new ScrollElement(page, record, parent);
        else if ("dragdrop".equals(type))
            elem = new DragDropElement(page, record, parent);
        else if ("toggle".equals(type))
            elem = new ToggleElement(page, record, parent);
        else if (registrations.containsKey(type) && registrations.get(type) != null)
        {
            try
            {
                @SuppressWarnings("rawtypes")
                Constructor constructor = registrations.get(type).getConstructor(Page.class, ElementRecord.class, Element.class);
                elem = (Element<?>) constructor.newInstance(page, record, parent);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        if (elem == null)
        {
            elem = new DefaultElement(page, record, parent);
        }
        elem.initialize();
        return elem;
    }
}
