package com.winger.ui.element.impl;

import com.winger.draw.texture.CSpriteBatch;
import com.winger.input.raw.CKeyboard;
import com.winger.struct.Tups.Tup2;
import com.winger.ui.Element;
import com.winger.ui.ElementRecord;
import com.winger.ui.Page;
import com.winger.ui.delegate.TextEntered;
import com.winger.utils.StringUtils;
import com.winger.utils.TypeUtils;

/**
 * A text input element, not currently working
 *
 * @author Michael P. Wingfield
 * @copyright Nov 4, 2014
 *
 */
public class TextBoxElement extends Element<TextBoxElement>
{
    protected String allowed;
    protected String notAllowed;
    protected int maxChar;
    protected int index;
    
    protected long frameCounter = 0;
    protected boolean blinkOn = false;
    
    public TextEntered textEntered;
    
    
    public TextBoxElement(Page page, ElementRecord record, Element<?> parent)
    {
        super(page, record, parent);
    }
    
    
    @Override
    public TextBoxElement initialize()
    {
        super.initialize();
        allowed = get("allowed");
        notAllowed = get("notAllowed");
        
        maxChar = parseNumber((Number) get("maxChar"), -1);
        if (text() == null)
        {
            text("");
        }
        index = parseNumber((Number) get("index"), text().length());
        
        return this;
    }
    
    
    @Override
    public void draw(CSpriteBatch spriteBatch)
    {
        frameCounter++;
        String tempText = text();
        if (frameCounter % 20 == 0)
        {
            blinkOn = !blinkOn;
        }
        if (hasFocus())
        {
            if (blinkOn)
            {
                text(StringUtils.insert(tempText, index(), "|"));
            } else
            {
                text(StringUtils.insert(tempText, index(), " "));
            }
        }
        super.draw(spriteBatch);
        text(tempText);
    }
    
    
    @Override
    public TextBoxElement sendKeyboardInfoToThisElement(CKeyboard keyboard)
    {
        // TODO: need to figure out how to set the index in the right place
        if (text() == null)
        {
            text("");
        }
        String previousText = text();
        Tup2<String, Integer> typedText = TypeUtils.getTypedText(keyboard, previousText, index());
        index(typedText.i2());
        text(typedText.i1());
        if (textEntered != null)
        {
            textEntered.textEntered(previousText, text());
        }
        return this;
    }
    
    
    // ////////////////////////////////////////////////////////
    // Getters and Setters
    // ////////////////////////////////////////////////////////
    public String allowed()
    {
        return allowed;
    }
    
    
    public TextBoxElement allowed(String value)
    {
        allowed = value;
        return this;
    }
    
    
    public String notAllowed()
    {
        return notAllowed;
    }
    
    
    public TextBoxElement notAllowed(String value)
    {
        notAllowed = value;
        return this;
    }
    
    
    public int maxChar()
    {
        return maxChar;
    }
    
    
    public TextBoxElement maxChar(int value)
    {
        maxChar = value;
        return this;
    }
    
    
    @Override
    public TextBoxElement text(String text)
    {
        if (text == null || text.isEmpty())
            index(0);
        else if (text.length() < index())
            index(text.length());
        
        return super.text(text);
    }
    
    
    public int index()
    {
        return index;
    }
    
    
    public TextBoxElement index(int value)
    {
        index = value;
        return this;
    }
    
}
