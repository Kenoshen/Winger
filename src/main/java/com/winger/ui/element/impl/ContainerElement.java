package com.winger.ui.element.impl;

import com.winger.ui.Element;
import com.winger.ui.ElementRecord;
import com.winger.ui.Page;

@SuppressWarnings("unchecked")
public abstract class ContainerElement<T> extends Element<T>
{
    protected float paddingLeft;
    protected float paddingRight;
    protected float paddingTop;
    protected float paddingBottom;
    
    
    public ContainerElement(Page page, ElementRecord record, Element<?> parent)
    {
        super(page, record, parent);
    }
    
    
    @Override
    public T initialize()
    {
        super.initialize();
        
        if (record.containsKey("padding") && record.get("padding") != null)
        {
            Object tempPadding = record.get("padding");
            if (record.get("paddingLeft") == null)
                record.put("paddingLeft", tempPadding);
            if (record.get("paddingRight") == null)
                record.put("paddingRight", tempPadding);
            if (record.get("paddingTop") == null)
                record.put("paddingTop", tempPadding);
            if (record.get("paddingBottom") == null)
                record.put("paddingBottom", tempPadding);
        }
        paddingLeft = parsePercentage(record.get("paddingLeft"), width());
        paddingRight = parsePercentage(record.get("paddingRight"), width());
        paddingTop = parsePercentage(record.get("paddingTop"), height());
        paddingBottom = parsePercentage(record.get("paddingBottom"), height());
        
        return (T) this;
    }
    
    
    // ////////////////////////////////////////////////////////
    // Getters and Setters
    // ////////////////////////////////////////////////////////
    public float paddingLeft()
    {
        return paddingLeft;
    }
    
    
    public T paddingLeft(float value)
    {
        paddingLeft = value;
        return (T) this;
    }
    
    
    public float paddingRight()
    {
        return paddingRight;
    }
    
    
    public T paddingRight(float value)
    {
        paddingRight = value;
        return (T) this;
    }
    
    
    public float paddingTop()
    {
        return paddingTop;
    }
    
    
    public T paddingTop(float value)
    {
        paddingTop = value;
        return (T) this;
    }
    
    
    public float paddingBottom()
    {
        return paddingBottom;
    }
    
    
    public T paddingBottom(float value)
    {
        paddingBottom = value;
        return (T) this;
    }
    
}
