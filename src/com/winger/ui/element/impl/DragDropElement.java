package com.winger.ui.element.impl;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;
import com.winger.draw.texture.CSpriteBatch;
import com.winger.input.raw.CMouse;
import com.winger.input.raw.state.CMouseButton;
import com.winger.math.tween.Tween;
import com.winger.math.tween.TweenType;
import com.winger.math.tween.delegate.TweenIsFinished;
import com.winger.struct.CRectangle;
import com.winger.ui.Element;
import com.winger.ui.ElementRecord;
import com.winger.ui.Page;
import com.winger.ui.delegate.IsDropOk;

/**
 * An element that can be moved by using drag and drop with the mouse
 *
 * @author Michael P. Wingfield
 * @copyright Nov 4, 2014
 *
 */
public class DragDropElement extends Element<DragDropElement> implements TweenIsFinished
{
    protected Color dragColor;
    protected boolean useIcon;
    protected boolean useBackground;
    protected boolean useText;
    protected boolean centerOnMouse;
    protected boolean moveOnDrag;
    protected boolean snapToX;
    protected boolean snapToY;
    protected boolean hasBounds;
    protected boolean defaultIsDropOk;
    protected boolean resetDragRoot;
    protected float right;
    protected float left;
    protected float top;
    protected float bottom;
    
    protected Vector2 dragRoot;
    public Vector2 draggingPosition;
    
    public IsDropOk isDropOk;
    
    protected boolean isReturningFromBadDrop = false;
    protected Vector2 badDropPos = new Vector2(0, 0);
    protected Tween tween;
    
    
    // ////////////////////////////////////////////////////////
    // Initialize methods
    // ////////////////////////////////////////////////////////
    public DragDropElement(Page page, ElementRecord record, Element<?> parent)
    {
        super(page, record, parent);
    }
    
    
    @Override
    public DragDropElement initialize()
    {
        tween = new Tween();
        tween.tweenTypeX = TweenType.SINUSOIDAL_OUT;
        tween.tweenTypeY = TweenType.SINUSOIDAL_OUT;
        tween.duration = 750;
        tween.tweenIsFinished = this;
        
        super.initialize();
        
        dragColor = parseColor((String) get("dragColor"), Color.WHITE);
        useIcon = parseBoolean((Boolean) get("useIcon"), true);
        useBackground = parseBoolean((Boolean) get("useBackground"), true);
        useText = parseBoolean((Boolean) get("useText"), true);
        centerOnMouse = parseBoolean((Boolean) get("centerOnMouse"), false);
        moveOnDrag = parseBoolean((Boolean) get("moveOnDrag"), false);
        snapToX = parseBoolean((Boolean) get("snapToX"), false);
        snapToY = parseBoolean((Boolean) get("snapToY"), false);
        hasBounds = parseBoolean((Boolean) get("hasBounds"), false);
        defaultIsDropOk = parseBoolean((Boolean) get("defaultIsDropOk"), true);
        resetDragRoot = parseBoolean((Boolean) get("resetDragRoot"), false);
        right = parsePercentage(get("right"), parentWidth);
        left = parsePercentage(get("left"), parentWidth);
        top = parsePercentage(get("top"), parentHeight);
        bottom = parsePercentage(get("bottom"), parentHeight);
        
        resetDragRoot();
        
        return this;
    }
    
    
    @Override
    public void draw(CSpriteBatch spriteBatch)
    {
        if (!moveOnDrag() || (!isDragging && !isReturningFromBadDrop))
        {
            super.draw(spriteBatch);
        }
        
        if (isDragging)
        {
            CMouse mouse = page.mouse;
            Vector2 mousePos = mouse.position();
            Vector2 lastDown = mouse.getLastDownPosition(CMouseButton.LEFT);
            CRectangle absolute = getAbsoluteBoundingBox();
            CRectangle abRect = new CRectangle(mousePos.x, mousePos.y, width(), height(), absolute.origin);
            if (centerOnMouse())
            {} else
            {
                abRect.x = abRect.x - (lastDown.x - absolute.x);
                abRect.y = abRect.y - (lastDown.y - absolute.y);
            }
            if (snapToX())
            {
                abRect.y = absolute.y;
            }
            if (snapToY())
            {
                abRect.x = absolute.x;
            }
            if (hasBounds)
            {
                if (!snapToY() && left() <= right())
                {
                    float absMinX = dragRoot.x + left() + absolute.origin.x;
                    float absMaxX = dragRoot.x + right() + absolute.origin.x - width();
                    if (abRect.x < absMinX)
                    {
                        abRect.x = absMinX;
                    }
                    if (abRect.x > absMaxX)
                    {
                        abRect.x = absMaxX;
                    }
                }
                if (!snapToX() && bottom() <= top())
                {
                    float absMinY = dragRoot.y + bottom() + absolute.origin.y;
                    float absMaxY = dragRoot.y + top() + absolute.origin.y - height();
                    if (abRect.y < absMinY)
                    {
                        abRect.y = absMinY;
                    }
                    if (abRect.y > absMaxY)
                    {
                        abRect.y = absMaxY;
                    }
                }
            }
            draggingPosition = abRect.position().cpy();
            
            if (useBackground())
            {
                if (texture() != null)
                {
                    texture().draw(spriteBatch, abRect, dragColor());
                }
            }
            if (useIcon())
            {
                if (icon() != null)
                {
                    icon().draw(spriteBatch, abRect, dragColor());
                }
            }
            if (useText())
            {
                if (font() != null && text() != null && !"".equals(text()))
                {
                    GlyphLayout bounds = new GlyphLayout(font(), text());
                    Vector2 offset = getTextOffset(alignment(), textAlignment(), abRect.width, abRect.height, bounds.width, bounds.height);
                    font().draw(spriteBatch, text(), abRect.x + offset.x, abRect.y + offset.y, 0, getBitmapFontAlignment(textAlignment()),
                        textColor());
                }
            }
        }
        if (isReturningFromBadDrop)
        {
            if (tween.isTweening())
            {
                tween.update(50);
                CRectangle r = new CRectangle(tween.position.x, tween.position.y, width(), height());
                // r.origin.x = width() * alignment().alignmentVector().x, height() * alignment().alignmentVector();
                r.origin.x = width() * alignment().alignmentVector().x;
                r.origin.y = height() * alignment().alignmentVector().y;
                if (useBackground())
                {
                    if (texture() != null)
                    {
                        texture().draw(spriteBatch, r, dragColor());
                        // spriteBatch.draw(texture(), r.rect(), dragColor());
                    }
                }
                if (useIcon())
                {
                    if (icon() != null)
                    {
                        texture().draw(spriteBatch, r, dragColor());
                        // spriteBatch.draw(icon(), r.rect(), dragColor());
                    }
                }
                if (useText())
                {
                    if (font() != null && text() != null && !"".equals(text()))
                    {
                        GlyphLayout bounds = new GlyphLayout(font(), text());
                        Vector2 offset = getTextOffset(alignment(), textAlignment(), r.width, r.height, bounds.width, bounds.height);
                        font().draw(spriteBatch, text(), r.x + offset.x, r.y + offset.y, 0, getBitmapFontAlignment(textAlignment()), textColor());
                    }
                }
            }
        }
    }
    
    
    // ////////////////////////////////////////////////////////
    // Event methods
    // ////////////////////////////////////////////////////////
    @Override
    public DragDropElement onDragStartEvent()
    {
        super.onDragStartEvent();
        draggingPosition = getAbsoluteBoundingBox().position().cpy();
        return this;
    }
    
    
    @Override
    public DragDropElement onDragEndEvent()
    {
        super.onDragEndEvent();
        
        boolean dropIsOk = defaultIsDropOk;
        if (isDropOk != null)
        {
            dropIsOk = isDropOk.isDropOk(this, draggingPosition);
        }
        if (dropIsOk)
        {
            if (moveOnDrag())
            {
                Vector2 relMovePos = draggingPosition.sub(getAbsoluteBoundingBox().position());
                x(x() + relMovePos.x);
                y(y() + relMovePos.y);
                if (resetDragRoot)
                {
                    resetDragRoot();
                }
            }
        } else
        {
            isReturningFromBadDrop = true;
            badDropPos = draggingPosition.cpy();
            tween.origin = badDropPos;
            tween.goal = getAbsoluteBoundingBox().position();
            tween.start();
        }
        return this;
    }
    
    
    public void tweenIsFinished(Tween sender)
    {
        isReturningFromBadDrop = false;
    }
    
    
    // ////////////////////////////////////////////////////////
    // Getters and Setters
    // ////////////////////////////////////////////////////////
    public Color dragColor()
    {
        return dragColor;
    }
    
    
    public DragDropElement dragColor(Color value)
    {
        dragColor = value;
        return this;
    }
    
    
    public boolean useIcon()
    {
        return useIcon;
    }
    
    
    public DragDropElement useIcon(boolean value)
    {
        useIcon = value;
        return this;
    }
    
    
    public boolean useBackground()
    {
        return useBackground;
    }
    
    
    public DragDropElement useBackground(boolean value)
    {
        useBackground = value;
        return this;
    }
    
    
    public boolean useText()
    {
        return useText;
    }
    
    
    public DragDropElement useText(boolean value)
    {
        useText = value;
        return this;
    }
    
    
    public boolean centerOnMouse()
    {
        return centerOnMouse;
    }
    
    
    public DragDropElement centerOnMouse(boolean value)
    {
        centerOnMouse = value;
        return this;
    }
    
    
    public boolean snapToX()
    {
        return snapToX;
    }
    
    
    public DragDropElement snapToX(boolean value)
    {
        snapToX = value;
        return this;
    }
    
    
    public boolean snapToY()
    {
        return snapToY;
    }
    
    
    public DragDropElement snapToY(boolean value)
    {
        snapToY = value;
        return this;
    }
    
    
    public boolean hasBounds()
    {
        return hasBounds;
    }
    
    
    public DragDropElement hasBounds(boolean value)
    {
        hasBounds = value;
        return this;
    }
    
    
    public float left()
    {
        return left;
    }
    
    
    public DragDropElement left(float value)
    {
        left = value;
        return this;
    }
    
    
    public float right()
    {
        return right;
    }
    
    
    public DragDropElement right(float value)
    {
        right = value;
        return this;
    }
    
    
    public float bottom()
    {
        return bottom;
    }
    
    
    public DragDropElement bottom(float value)
    {
        bottom = value;
        return this;
    }
    
    
    public float top()
    {
        return top;
    }
    
    
    public DragDropElement top(float value)
    {
        top = value;
        return this;
    }
    
    
    public boolean moveOnDrag()
    {
        return moveOnDrag;
    }
    
    
    public DragDropElement moveOnDrag(boolean value)
    {
        moveOnDrag = value;
        return this;
    }
    
    
    public DragDropElement resetDragRoot()
    {
        dragRoot = getAbsoluteBoundingBox().position();
        return this;
    }
    
}
