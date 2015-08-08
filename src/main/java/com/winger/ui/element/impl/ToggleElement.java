package com.winger.ui.element.impl;

import com.badlogic.gdx.graphics.Color;
import com.winger.draw.texture.CTexture;
import com.winger.ui.Element;
import com.winger.ui.ElementRecord;
import com.winger.ui.Page;
import com.winger.ui.delegate.Toggled;

/**
 * An element that can be turned off and on with a click
 *
 * @author Michael P. Wingfield
 * @copyright Nov 4, 2014
 *
 */
public class ToggleElement extends Element<ToggleElement>
{
    public Toggled toggledEventHandler;
    protected CTexture onIcon;
    protected CTexture offIcon;
    protected CTexture onTexture;
    protected CTexture offTexture;
    protected CTexture onTextureHover;
    protected CTexture offTextureHover;
    protected CTexture onTextureSelect;
    protected CTexture offTextureSelect;
    protected Color onColor;
    protected Color offColor;
    protected Color onColorHover;
    protected Color offColorHover;
    protected Color onColorSelect;
    protected Color offColorSelect;
    protected boolean isToggled;
    
    
    // ////////////////////////////////////////////////////////
    // Initialize methods
    // ////////////////////////////////////////////////////////
    public ToggleElement(Page page, ElementRecord record, Element<?> parent)
    {
        super(page, record, parent);
    }
    
    
    @Override
    public ToggleElement initialize()
    {
        super.initialize();
        //
        // get booleans
        isToggled = parseBoolean((Boolean) get("isToggled"), false);
        
        //
        // get colors
        onColor = parseColor((String) get("onColor"), color);
        onColorHover = parseColor((String) get("onColorHover"), colorHover);
        onColorSelect = parseColor((String) get("onColorSelect"), colorSelect);
        //
        // get textures
        onIcon = parseTexture((String) get("onIcon"));
        if (onIcon == null)
            onIcon = icon;
        onTexture = parseTexture((String) get("onTexture"));
        if (onTexture == null)
            onTexture = texture;
        onTextureHover = parseTexture((String) get("onTextureHover"));
        if (onTextureHover == null)
            onTextureHover = textureHover;
        onTextureSelect = parseTexture((String) get("onTextureSelect"));
        if (onTextureSelect == null)
            onTextureSelect = textureSelect;
        
        //
        // get colors
        offColor = parseColor((String) get("offColor"), color);
        offColorHover = parseColor((String) get("offColorHover"), colorHover);
        offColorSelect = parseColor((String) get("offColorSelect"), colorSelect);
        //
        // get textures
        offIcon = parseTexture((String) get("offIcon"));
        if (offIcon == null)
            offIcon = icon;
        offTexture = parseTexture((String) get("offTexture"));
        if (offTexture == null)
            offTexture = texture;
        offTextureHover = parseTexture((String) get("offTextureHover"));
        if (offTextureHover == null)
            offTextureHover = textureHover;
        offTextureSelect = parseTexture((String) get("offTextureSelect"));
        if (offTextureSelect == null)
            offTextureSelect = textureSelect;
        
        toggle(isToggled);
        
        return this;
    }
    
    
    // ////////////////////////////////////////////////////////
    // Event methods
    // ////////////////////////////////////////////////////////
    @Override
    public ToggleElement onSelectStartEvent()
    {
        super.onSelectStartEvent();
        toggle();
        return this;
    }
    
    
    public void toggle(boolean toggle)
    {
        isToggled = toggle;
        //
        if (isToggled)
        {
            icon = onIcon;
            texture = (onTexture == null ? texture : onTexture);
            textureHover = (onTextureHover == null ? textureHover : onTextureHover);
            textureSelect = (onTextureSelect == null ? textureSelect : onTextureSelect);
            color = (onColor == null ? color : onColor);
            colorHover = (onColorHover == null ? colorHover : onColorHover);
            colorSelect = (onColorSelect == null ? colorSelect : onColorSelect);
        } else
        {
            icon = offIcon;
            texture = (offTexture == null ? texture : offTexture);
            textureHover = (offTextureHover == null ? textureHover : offTextureHover);
            textureSelect = (offTextureSelect == null ? textureSelect : offTextureSelect);
            color = (offColor == null ? color : offColor);
            colorHover = (offColorHover == null ? colorHover : offColorHover);
            colorSelect = (offColorSelect == null ? colorSelect : offColorSelect);
        }
        
        if (toggledEventHandler != null)
        {
            toggledEventHandler.toggled(this);
        }
    }
    
    
    public void toggle()
    {
        toggle(!isToggled);
    }
    
    
    // ////////////////////////////////////////////////////////
    // Getters and Setters
    // ////////////////////////////////////////////////////////
    public CTexture onIcon()
    {
        return onIcon;
    }
    
    
    public ToggleElement onIcon(CTexture onIcon)
    {
        this.onIcon = onIcon;
        return this;
    }
    
    
    public CTexture onTexture()
    {
        return onTexture;
    }
    
    
    public ToggleElement onTexture(CTexture onTexture)
    {
        this.onTexture = onTexture;
        return this;
    }
    
    
    public CTexture onTextureHover()
    {
        return onTextureHover;
    }
    
    
    public ToggleElement onTextureHover(CTexture onTextureHover)
    {
        this.onTextureHover = onTextureHover;
        return this;
    }
    
    
    public CTexture onTextureSelect()
    {
        return onTextureSelect;
    }
    
    
    public ToggleElement onTextureSelect(CTexture onTextureSelect)
    {
        this.onTextureSelect = onTextureSelect;
        return this;
    }
    
    
    public CTexture offIcon()
    {
        return offIcon;
    }
    
    
    public ToggleElement offIcon(CTexture offIcon)
    {
        this.offIcon = offIcon;
        return this;
    }
    
    
    public CTexture offTexture()
    {
        return offTexture;
    }
    
    
    public ToggleElement offTexture(CTexture offTexture)
    {
        this.offTexture = offTexture;
        return this;
    }
    
    
    public CTexture offTextureHover()
    {
        return offTextureHover;
    }
    
    
    public ToggleElement offColorHover(CTexture offTextureHover)
    {
        this.offTextureHover = offTextureHover;
        return this;
    }
    
    
    public CTexture offTextureSelect()
    {
        return offTextureSelect;
    }
    
    
    public ToggleElement offTextureSelect(CTexture offTextureSelect)
    {
        this.offTextureSelect = offTextureSelect;
        return this;
    }
    
    
    public Color onColor()
    {
        return onColor;
    }
    
    
    public ToggleElement onColor(Color onColor)
    {
        this.onColor = onColor;
        return this;
    }
    
    
    public Color onColorHover()
    {
        return onColorHover;
    }
    
    
    public ToggleElement onColorHover(Color onColorHover)
    {
        this.onColorHover = onColorHover;
        return this;
    }
    
    
    public Color onColorSelect()
    {
        return onColorSelect;
    }
    
    
    public ToggleElement onColorSelect(Color onColorSelect)
    {
        this.onColorSelect = onColorSelect;
        return this;
    }
    
    
    public Color offColor()
    {
        return offColor;
    }
    
    
    public ToggleElement offColor(Color offColor)
    {
        this.offColor = offColor;
        return this;
    }
    
    
    public Color offColorHover()
    {
        return offColorHover;
    }
    
    
    public ToggleElement offColorHover(Color offColorHover)
    {
        this.offColorHover = offColorHover;
        return this;
    }
    
    
    public Color offColorSelect()
    {
        return offColorSelect;
    }
    
    
    public ToggleElement offColorSelect(Color offColorSelect)
    {
        this.offColorSelect = offColorSelect;
        return this;
    }
    
    
    public boolean isToggled()
    {
        return isToggled;
    }
}
