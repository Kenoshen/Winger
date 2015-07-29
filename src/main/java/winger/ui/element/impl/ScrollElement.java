package com.winger.ui.element.impl;

import java.util.Map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.winger.draw.texture.CSpriteBatch;
import com.winger.struct.CRectangle;
import com.winger.struct.JSON;
import com.winger.ui.Alignment;
import com.winger.ui.Element;
import com.winger.ui.ElementRecord;
import com.winger.ui.Page;
import com.winger.ui.PageManager;
import com.winger.ui.delegate.PageEventHandler;

/**
 * An element that can scroll it's child elements
 *
 * @author Michael P. Wingfield
 * @copyright Nov 4, 2014
 *
 */
public class ScrollElement extends ContainerElement<ScrollElement> implements PageEventHandler
{
    protected static final float SCROLL_SPEED = 100;
    protected static final float SCROLL_SM_SIZE = 0.1f;
    protected static final float SCROLL_MD_SIZE = 0.25f;
    protected static final float SCROLL_LG_SIZE = 0.5f;
    
    protected boolean horizontalScroll;
    protected boolean horizontalFlip;
    protected float horizontalThickness;
    protected boolean verticalScroll;
    protected boolean verticalFlip;
    protected float verticalThickness;
    protected CRectangle content = CRectangle.empty();
    
    protected DefaultElement hBar;
    protected DefaultElement hDec;
    protected DefaultElement hInc;
    protected DragDropElement hSlider;
    
    protected DefaultElement vBar;
    protected DefaultElement vDec;
    protected DefaultElement vInc;
    protected DragDropElement vSlider;
    
    protected boolean isScrollingH = false;
    protected boolean isScrollingV = false;
    protected Vector2 lastScrollPos = new Vector2(0, 0);
    
    protected JSON barSettings;
    
    
    // ////////////////////////////////////////////////////////
    // Initialize
    // ////////////////////////////////////////////////////////
    public ScrollElement(Page page, ElementRecord record, Element<?> parent)
    {
        super(page, record, parent);
    }
    
    
    @Override
    public ScrollElement initialize()
    {
        super.initialize();
        
        try
        {
            Map<String, Object> bar = get("bar");
            barSettings = JSON.serializeToJSON(bar);
        } catch (Exception e)
        {
            barSettings = JSON.emptyObject();
        }
        
        horizontalScroll = parseBoolean((Boolean) barSettings.get("horizontalScroll"), true);
        verticalScroll = parseBoolean((Boolean) barSettings.get("verticalScroll"), true);
        horizontalFlip = parseBoolean((Boolean) barSettings.get("horizontalFlip"), false);
        verticalFlip = parseBoolean((Boolean) barSettings.get("verticalFlip"), false);
        horizontalThickness = parseNumber((Number) barSettings.get("horizontalThickness"), 15f);
        verticalThickness = parseNumber((Number) barSettings.get("verticalThickness"), 15f);
        
        Vector2 bottomLeft = new Vector2(0, 0);
        bottomLeft.x -= width() * alignment().alignmentVector().x;
        bottomLeft.y -= height() * alignment().alignmentVector().y;
        Vector2 bottomRight = new Vector2(bottomLeft.x + width, bottomLeft.y);
        
        setUpHorizontalScrollBar(bottomLeft);
        setUpVerticalScrollBar(bottomRight);
        calculateWindow();
        scroll(1, 1);
        
        if (verticalFlip())
        {
            flipVertical(verticalFlip());
        }
        if (horizontalFlip())
        {
            flipHorizontal(horizontalFlip());
        }
        verticalScroll(verticalScroll());
        horizontalScroll(horizontalScroll());
        //
        return this;
    }
    
    
    protected void setUpHorizontalScrollBar(Vector2 bottomLeft)
    {
        //
        // horizontal bar
        float ht = horizontalThickness();
        
        hBar = new DefaultElement(page(), null, this);
        hBar.id(id() + "hBar");
        hBar.x(bottomLeft.x + ht);
        hBar.y(bottomLeft.y);
        hBar.width(width() - ht - ht);
        hBar.height(ht);
        hBar.alignment(Alignment.TOP_LEFT);
        hBar.isEnabled(false);
        hBar.texture(texture());
        hBar.color(Color.CYAN.cpy());
        hBar.onSelectStart(id() + "hBar");
        PageManager.instance().subscribeToEvent(this, page.name, hBar.onSelectStart());
        
        hDec = new DefaultElement(page(), null, hBar);
        hDec.id(id() + "hDec");
        hDec.width(ht);
        hDec.height(ht);
        hDec.alignment(Alignment.TOP_RIGHT);
        hDec.texture(texture());
        hDec.color(Color.RED.cpy());
        hDec.onSelectStart(id() + "hDec");
        PageManager.instance().subscribeToEvent(this, page.name, hDec.onSelectStart());
        
        hInc = new DefaultElement(page(), null, hBar);
        hInc.id(id() + "hInc");
        hInc.x(hBar.width());
        hInc.width(ht);
        hInc.height(ht);
        hInc.alignment(Alignment.TOP_LEFT);
        hInc.texture(texture());
        hInc.color(Color.BLUE.cpy());
        hInc.onSelectStart(id() + "hInc");
        PageManager.instance().subscribeToEvent(this, page.name, hInc.onSelectStart());
        
        hSlider = new DragDropElement(page(), null, hBar);
        hSlider.id(id() + "hSlider");
        hSlider.width(100);
        hSlider.height(ht);
        hSlider.hasBounds(true);
        hSlider.left(0);
        hSlider.right(hBar.width());
        hSlider.snapToX(true);
        hSlider.centerOnMouse(false);
        hSlider.moveOnDrag(true);
        hSlider.alignment(Alignment.TOP_LEFT);
        hSlider.texture(texture());
        hSlider.color(Color.GREEN.cpy());
        hSlider.colorHover(Color.GREEN.cpy());
        hSlider.colorSelect(Color.GREEN.cpy());
        hSlider.defaultIsDropOk = true;
        hSlider.useBackground = true;
        hSlider.onSelectStart(id() + "hSlider");
        hSlider.onDragStart(id() + "hSlider-dragStart");
        hSlider.onDragEnd(id() + "hSlider-dragEnd");
        hSlider.resetDragRoot();
        PageManager.instance().subscribeToEvent(this, page.name, hSlider.onSelectStart());
        PageManager.instance().subscribeToEvent(this, page.name, hSlider.onDragStart());
        PageManager.instance().subscribeToEvent(this, page.name, hSlider.onDragEnd());
        //
        // add all elements to page
        hBar.children().add(hSlider);
        hBar.children().add(hDec);
        hBar.children().add(hInc);
        
        addElement(hBar);
        addElement(hSlider);
        addElement(hDec);
        addElement(hInc);
    }
    
    
    protected void setUpVerticalScrollBar(Vector2 bottomRight)
    {
        //
        // vertical bar
        float vt = verticalThickness();
        
        vBar = new DefaultElement(page(), null, this);
        vBar.id(id() + "vBar");
        vBar.x(bottomRight.x);
        vBar.y(bottomRight.y + vt);
        vBar.width(vt);
        vBar.height(height() - vt - vt);
        vBar.alignment(Alignment.BOTTOM_LEFT);
        vBar.isEnabled(false);
        vBar.texture(texture());
        vBar.color(Color.CYAN.cpy());
        vBar.onSelectStart(id() + "vBar");
        PageManager.instance().subscribeToEvent(this, page.name, vBar.onSelectStart());
        
        vDec = new DefaultElement(page(), null, vBar);
        vDec.id(id() + "vDec");
        vDec.width(vt);
        vDec.height(vt);
        vDec.alignment(Alignment.TOP_LEFT);
        vDec.texture(texture());
        vDec.color(Color.RED.cpy());
        vDec.onSelectStart(id() + "vDec");
        PageManager.instance().subscribeToEvent(this, page.name, vDec.onSelectStart());
        
        vInc = new DefaultElement(page(), null, vBar);
        vInc.id(id() + "vInc");
        vInc.y(vBar.height());
        vInc.width(vt);
        vInc.height(vt);
        vInc.alignment(Alignment.BOTTOM_LEFT);
        vInc.texture(texture());
        vInc.color(Color.BLUE.cpy());
        vInc.onSelectStart(id() + "vInc");
        PageManager.instance().subscribeToEvent(this, page.name, vInc.onSelectStart());
        
        vSlider = new DragDropElement(page(), null, vBar);
        vSlider.id(id() + "vSlider");
        vSlider.width(vt);
        vSlider.height(100);
        vSlider.hasBounds(true);
        vSlider.bottom(0);
        vSlider.top(vBar.height());
        vSlider.snapToY(true);
        vSlider.centerOnMouse(false);
        vSlider.moveOnDrag(true);
        vSlider.alignment(Alignment.BOTTOM_LEFT);
        vSlider.texture(texture());
        vSlider.color(Color.GREEN.cpy());
        vSlider.colorHover(Color.GREEN.cpy());
        vSlider.colorSelect(Color.GREEN.cpy());
        vSlider.defaultIsDropOk = true;
        vSlider.useBackground = true;
        vSlider.onSelectStart(id() + "vSlider");
        vSlider.onDragStart(id() + "vSlider-dragStart");
        vSlider.onDragEnd(id() + "vSlider-dragEnd");
        vSlider.resetDragRoot();
        PageManager.instance().subscribeToEvent(this, page.name, vSlider.onSelectStart());
        PageManager.instance().subscribeToEvent(this, page.name, vSlider.onDragStart());
        PageManager.instance().subscribeToEvent(this, page.name, vSlider.onDragEnd());
        
        vBar.children().add(vSlider);
        vBar.children().add(vDec);
        vBar.children().add(vInc);
        
        addElement(vBar);
        addElement(vSlider);
        addElement(vDec);
        addElement(vInc);
    }
    
    
    protected ScrollElement addElement(Element<?> e)
    {
        page.addElement(e);
        e.parent().children().add(e);
        return this;
    }
    
    
    @Override
    public ScrollElement afterChildrenInitialize()
    {
        super.afterChildrenInitialize();
        calculateWindow();
        scroll(1, 1);
        return this;
    }
    
    
    // ////////////////////////////////////////////////////////
    // Calculations for position
    // ////////////////////////////////////////////////////////
    public void calculateWindow()
    {
        Vector2 windowBottomLeftBounds = new Vector2(1000000, 1000000);
        Vector2 windowTopRightBounds = new Vector2(-1000000, -1000000);
        for (Element<?> e : children())
        {
            if (e.id() != null && (e.id().startsWith(id() + "hBar") || e.id().startsWith(id() + "vBar")))
            {
                continue;
            }
            for (Element<?> child : e)
            {
                Vector2 alignmentVector = child.alignment().alignmentVector();
                Vector2 bottomLeftBounds = new Vector2(child.x() - (alignmentVector.x * child.width()), child.y()
                    - (alignmentVector.y * child.height()));
                Vector2 topRightBounds = new Vector2(bottomLeftBounds.x + child.width(), bottomLeftBounds.y + child.height());
                // left
                if (windowBottomLeftBounds.x > bottomLeftBounds.x)
                {
                    windowBottomLeftBounds.x = bottomLeftBounds.x;
                }
                // bottom
                if (windowBottomLeftBounds.y > bottomLeftBounds.y)
                {
                    windowBottomLeftBounds.y = bottomLeftBounds.y;
                }
                // right
                if (windowTopRightBounds.x < topRightBounds.x)
                {
                    windowTopRightBounds.x = topRightBounds.x;
                }
                // top
                if (windowTopRightBounds.y < topRightBounds.y)
                {
                    windowTopRightBounds.y = topRightBounds.y;
                }
            }
        }
        
        content = new CRectangle(windowBottomLeftBounds.x, windowBottomLeftBounds.y, windowTopRightBounds.x - windowBottomLeftBounds.x,
            windowTopRightBounds.y - windowBottomLeftBounds.y);
        content.origin = new Vector2(0, 0);
        
        calculateScrollSize();
    }
    
    
    public void calculateSourceRectangles()
    {
        CRectangle windowAbsRect = getAbsoluteBoundingBox();
        Vector2 windowBottomLeftBounds = new Vector2(windowAbsRect.x, windowAbsRect.y);
        Vector2 windowTopRightBounds = new Vector2(windowBottomLeftBounds.x + width(), windowBottomLeftBounds.y + height());
        for (Element<?> e : children())
        {
            if (e.id() != null && (e.id().startsWith(id() + "hBar") || e.id().startsWith(id() + "vBar")))
            {
                continue;
            }
            for (Element<?> child : e)
            {
                Vector2 alignmentVector = child.alignment().alignmentVector();
                CRectangle childAbsRect = child.getAbsoluteBoundingBox();
                Vector2 bottomLeftBounds = new Vector2(childAbsRect.x - (alignmentVector.x * childAbsRect.width), childAbsRect.y
                    - (alignmentVector.y * childAbsRect.height));
                Vector2 topRightBounds = new Vector2(bottomLeftBounds.x + childAbsRect.width, bottomLeftBounds.y + childAbsRect.height);
                Rectangle sourceRect = new Rectangle(0, 0, childAbsRect.width, childAbsRect.height);
                boolean changed = false;
                //
                if (windowBottomLeftBounds.x > bottomLeftBounds.x)
                {
                    changed = true;
                    sourceRect.x += windowBottomLeftBounds.x - bottomLeftBounds.x;
                    sourceRect.width -= windowBottomLeftBounds.x - bottomLeftBounds.x;
                }
                if (windowBottomLeftBounds.y > bottomLeftBounds.y)
                {
                    changed = true;
                    sourceRect.y += windowBottomLeftBounds.y - bottomLeftBounds.y;
                    sourceRect.height -= windowBottomLeftBounds.y - bottomLeftBounds.y;
                }
                if (windowTopRightBounds.x < topRightBounds.x)
                {
                    changed = true;
                    sourceRect.width += windowTopRightBounds.x - topRightBounds.x;
                }
                if (windowTopRightBounds.y < topRightBounds.y)
                {
                    changed = true;
                    sourceRect.height += windowTopRightBounds.y - topRightBounds.y;
                }
                //
                if (!changed)
                {
                    child.isVisible(true);
                    child.sourceRectangle(null);
                } else if (sourceRect.width <= 0f || sourceRect.height <= 0f)
                {
                    child.isVisible(false);
                    child.sourceRectangle(null);
                } else
                {
                    child.isVisible(true);
                    child.sourceRectangle(sourceRect);
                }
            }
        }
    }
    
    
    public void calculateScrollSliderPositions()
    {
        float padL = paddingLeft();
        float padR = paddingRight();
        float padT = paddingTop();
        float padB = paddingBottom();
        //
        float totalWindowScrollDistX = content.width + -width() + padL + padR;
        float totalWindowScrollDistY = content.height + -height() + padT + padB;
        Vector2 ratio = new Vector2((totalWindowScrollDistX > 0 ? (-content.x + padL) / totalWindowScrollDistX : 0),
            (totalWindowScrollDistY > 0 ? (-content.y + padB) / totalWindowScrollDistY : 0));
        //
        float totalHorizontalScrollDist = hBar.width() - hSlider.width();
        float totalVerticalScrollDist = vBar.height() - vSlider.height();
        //
        hSlider.x(ratio.x * totalHorizontalScrollDist);
        vSlider.y(ratio.y * totalVerticalScrollDist);
    }
    
    
    public void calculateScrollFromSliderPosition()
    {
        float padL = paddingLeft();
        float padR = paddingRight();
        float padT = paddingTop();
        float padB = paddingBottom();
        //
        float totalHorizontalScrollDist = hBar.width() - hSlider.width();
        float totalVerticalScrollDist = vBar.height() - vSlider.height();
        //
        Vector2 draggingPosX = ((DragDropElement) hSlider).draggingPosition;
        if (!isScrollingH)
        {
            draggingPosX = new Vector2(hSlider.x(), hSlider.y());
        } else
        {
            draggingPosX = draggingPosX.cpy();
            CRectangle absR = hSlider.parent().getAbsoluteBoundingBox();
            draggingPosX.x -= absR.x;
            draggingPosX.y -= absR.y;
        }
        //
        Vector2 draggingPosY = ((DragDropElement) vSlider).draggingPosition;
        if (!isScrollingV)
        {
            draggingPosY = new Vector2(vSlider.x(), vSlider.y());
        } else
        {
            draggingPosY = draggingPosY.cpy();
            CRectangle absR = vSlider.parent().getAbsoluteBoundingBox();
            draggingPosY.x -= absR.x;
            draggingPosY.y -= absR.y;
        }
        Vector2 scrollRatio = new Vector2((totalHorizontalScrollDist > 0 ? draggingPosX.x / totalHorizontalScrollDist : 0),
            (totalVerticalScrollDist > 0 ? draggingPosY.y / totalVerticalScrollDist : 0));
        //
        float totalWindowScrollDistX = content.width + -width() + padL + padR;
        float totalWindowScrollDistY = content.height + -height() + padT + padB;
        Vector2 windowRatio = new Vector2((totalWindowScrollDistX > 0 ? (-content.x + padL) / totalWindowScrollDistX : 0),
            (totalWindowScrollDistY > 0 ? (-content.y + padB) / totalWindowScrollDistY : 0));
        //
        Vector2 ratio = scrollRatio.sub(windowRatio);
        //
        scroll(-ratio.x * totalWindowScrollDistX, -ratio.y * totalWindowScrollDistY);
    }
    
    
    public void calculateScrollSize()
    {
        if (contentWidth() < width())
        {
            hSlider.width(0);
        } else if (contentWidth() < width() * 2)
        {
            hSlider.width(SCROLL_LG_SIZE * width());
        } else if (contentWidth() < width() * 4)
        {
            hSlider.width(SCROLL_MD_SIZE * width());
        } else
        {
            hSlider.width(SCROLL_SM_SIZE * width());
        }
        
        if (contentHeight() < height())
        {
            vSlider.height(0);
        } else if (contentHeight() < height() * 2)
        {
            vSlider.height(SCROLL_LG_SIZE * height());
        } else if (contentHeight() < height() * 4)
        {
            vSlider.height(SCROLL_MD_SIZE * height());
        } else
        {
            vSlider.height(SCROLL_SM_SIZE * height());
        }
    }
    
    
    // ////////////////////////////////////////////////////////
    // Scroll methods
    // ////////////////////////////////////////////////////////
    public void scrollHorizontal(float amount)
    {
        scroll(amount, 0);
    }
    
    
    public void scrollVertical(float amount)
    {
        scroll(0, amount);
    }
    
    
    public void scroll(float amountX, float amountY)
    {
        float padL = paddingLeft();
        float padR = paddingRight();
        float padT = paddingTop();
        float padB = paddingBottom();
        //
        Vector2 scrollBottomLeftBound = new Vector2(padL, padB);
        Vector2 scrollTopRightBound = new Vector2(width() + -padR, height() + -padT);
        //
        Vector2 windowBottomLeftBound = new Vector2(content.x, content.y);
        Vector2 windowTopRightBound = new Vector2(windowBottomLeftBound.x + content.width, windowBottomLeftBound.y + content.height);
        //
        Vector2 testWindowBottomLeftBound = new Vector2(windowBottomLeftBound.x + amountX, windowBottomLeftBound.y + amountY);
        Vector2 testWindowTopRightBound = new Vector2(windowTopRightBound.x + amountX, windowTopRightBound.y + amountY);
        //
        float temp = 0;
        if (testWindowTopRightBound.x < scrollTopRightBound.x)
        {
            temp = scrollTopRightBound.x - testWindowTopRightBound.x;
            testWindowBottomLeftBound.x += temp;
            testWindowTopRightBound.x += temp;
        }
        if (testWindowTopRightBound.y < scrollTopRightBound.y)
        {
            temp = scrollTopRightBound.y - testWindowTopRightBound.y;
            testWindowBottomLeftBound.y += temp;
            testWindowTopRightBound.y += temp;
        }
        //
        if (testWindowBottomLeftBound.x > scrollBottomLeftBound.x)
        {
            temp = scrollBottomLeftBound.x - testWindowBottomLeftBound.x;
            testWindowBottomLeftBound.x += temp;
            testWindowTopRightBound.x += temp;
        }
        if (testWindowBottomLeftBound.y > scrollBottomLeftBound.y)
        {
            temp = scrollBottomLeftBound.y - testWindowBottomLeftBound.y;
            testWindowBottomLeftBound.y += temp;
            testWindowTopRightBound.y += temp;
        }
        //
        Vector2 trueAmount = testWindowBottomLeftBound.sub(windowBottomLeftBound);
        //
        content.x += trueAmount.x;
        content.y += trueAmount.y;
        //
        for (Element<?> e : children())
        {
            if (e.id() != null && (e.id().startsWith(id() + "hBar") || e.id().startsWith(id() + "vBar")))
            {
                continue;
            } else
            {
                e.x(e.x() + trueAmount.x);
                e.y(e.y() + trueAmount.y);
            }
        }
        if (!isScrollingH && !isScrollingV)
        {
            calculateScrollSliderPositions();
        }
        //
        calculateSourceRectangles();
        
        // /////////////////////
        // Vector2 testWindowOrigin = window.origin.sub(amount);
        // float leftW = window.x - testWindowOrigin.x;
        // float topW = window.y - testWindowOrigin.y;
        // float rightW = leftW + window.width;
        // float bottomW = topW + window.height;
        //
        // CRectangle absRect = new CRectangle(x(), y(), width(), height());
        // float leftA = absRect.x - getOrigin().x;
        // float topA = absRect.y - getOrigin().y;
        // float rightA = leftA + absRect.width;
        // float bottomA = topA + absRect.height;
        //
        // if (leftW > leftA)
        // {
        // amount.x = leftA - (window.x - window.origin.x);
        // }
        // if (rightW < rightA)
        // {
        // amount.x = -((window.x - window.origin.x) + window.width - rightA);
        // }
        // if (topW > topA)
        // {
        // amount.y = topA - (window.y - window.origin.y);
        // }
        // if (bottomW < bottomA)
        // {
        // amount.y = -((window.y - window.origin.y) + window.height - bottomA);
        // }
        // if (windowWidth() - width() < 0)
        // {
        // amount.x = 0;
        // }
        // if (windowHeight() - height() < 0)
        // {
        // amount.y = 0;
        // }
        // window.origin = window.origin.sub(amount);
        // for (Element immediateChild : children())
        // {
        // if (!immediateChild.id().contains(SCROLL_BAR_PREFIX_FINDER))
        // {
        // immediateChild.x(immediateChild.x() + amount.x);
        // immediateChild.y(immediateChild.y() + amount.y);
        // }
        // }
        //
        // if (!isScrollingH)
        // {
        // float totalWindowScrollDist = windowWidth() - width();
        // float percentage = amount.x / totalWindowScrollDist;
        // float totalScrollDist = scrollBarH.width() - scrollSliderH.width();
        // float newScrollSliderX = scrollSliderH.x() + (-totalScrollDist * percentage);
        // if (newScrollSliderX < 0)
        // {
        // scrollSliderH.x(0);
        // }
        // else if (newScrollSliderX > totalScrollDist)
        // {
        // scrollSliderH.x(totalScrollDist);
        // }
        // else
        // {
        // scrollSliderH.x(newScrollSliderX);
        // }
        // }
        // if (!isScrollingV)
        // {
        // float totalWindowScrollDist = windowHeight() - height();
        // float percentage = amount.y / totalWindowScrollDist;
        // float totalScrollDist = scrollBarV.height() - scrollSliderV.height();
        // float newScrollSliderY = scrollSliderV.y() + (-totalScrollDist * percentage);
        // if (newScrollSliderY < 0)
        // {
        // scrollSliderV.y(0);
        // }
        // else if (newScrollSliderY > totalScrollDist)
        // {
        // scrollSliderV.y(totalScrollDist);
        // }
        // else
        // {
        // scrollSliderV.y(newScrollSliderY);
        // }
        // }
        //
        // calculateSourceRectangles();
    }
    
    
    // ////////////////////////////////////////////////////////
    // Main methods
    // ////////////////////////////////////////////////////////
    @Override
    public void draw(CSpriteBatch spriteBatch)
    {
        if (isScrollingH || isScrollingV)
        {
            calculateScrollFromSliderPosition();
        }
        super.draw(spriteBatch);
    }
    
    
    @Override
    public void debugDraw(CSpriteBatch spriteBatch)
    {
        super.debugDraw(spriteBatch);
        CRectangle c = content.clone();
        c.x += x();
        c.y += y();
        spriteBatch.drawRectangle(ShapeType.Line, c, Color.ORANGE, 2000);
    }
    
    
    // ////////////////////////////////////////////////////////
    // Events
    // ////////////////////////////////////////////////////////
    public void handleEvent(Object sender, String pageName, String eventName)
    {
        if (eventName == null)
        {
            return;
        }
        if (eventName.equals(hBar.onSelectStart()))
        {   
            
        } else if (eventName.equals(hDec.onSelectStart()))
        {
            scrollHorizontal(SCROLL_SPEED);
        } else if (eventName.equals(hInc.onSelectStart()))
        {
            scrollHorizontal(-SCROLL_SPEED);
        } else if (eventName.equals(hSlider.onSelectStart()))
        {   
            
        } else if (eventName.equals(vBar.onSelectStart()))
        {   
            
        } else if (eventName.equals(vDec.onSelectStart()))
        {
            scrollVertical(SCROLL_SPEED);
        } else if (eventName.equals(vInc.onSelectStart()))
        {
            scrollVertical(-SCROLL_SPEED);
        } else if (eventName.equals(vSlider.onSelectStart()))
        {   
            
        }
        // drag
        else if (eventName.equals(hSlider.onDragStart()))
        {
            isScrollingH = true;
            lastScrollPos = hSlider.draggingPosition.cpy();
        } else if (eventName.equals(hSlider.onDragEnd()))
        {
            isScrollingH = false;
        } else if (eventName.equals(vSlider.onDragStart()))
        {
            isScrollingV = true;
            lastScrollPos = vSlider.draggingPosition.cpy();
        } else if (eventName.equals(vSlider.onDragEnd()))
        {
            isScrollingV = false;
        }
    }
    
    
    // ////////////////////////////////////////////////////////
    // Helpers
    // ////////////////////////////////////////////////////////
    protected void setElementVisAndEnabled(Element<?> elm, boolean val)
    {
        if (elm != null)
        {
            for (Element<?> c : elm)
            {
                if (!c.id().endsWith("Bar"))
                {
                    c.isEnabled(val);
                }
                c.isVisible(val);
            }
        }
    }
    
    
    protected void flipVertical(boolean val)
    {
        float width = width();
        //
        if (val)
        {
            vBar.x(-verticalThickness());
        } else
        {
            vBar.x(width);
        }
    }
    
    
    protected void flipHorizontal(boolean val)
    {
        float height = height();
        //
        if (val)
        {
            hBar.y(height + horizontalThickness());
        } else
        {
            hBar.y(0);
        }
    }
    
    
    // ////////////////////////////////////////////////////////
    // Getters and Setters
    // ////////////////////////////////////////////////////////
    public boolean horizontalScroll()
    {
        return horizontalScroll;
    }
    
    
    public ScrollElement horizontalScroll(boolean value)
    {
        horizontalScroll = value;
        setElementVisAndEnabled(hBar, value);
        return this;
    }
    
    
    public boolean horizontalFlip()
    {
        return horizontalFlip;
    }
    
    
    public ScrollElement horizontalFlip(boolean value)
    {
        if (value != horizontalFlip())
        {
            horizontalFlip = value;
            flipHorizontal(value);
        }
        return this;
    }
    
    
    public float horizontalThickness()
    {
        return horizontalThickness;
    }
    
    
    public ScrollElement horizontalThickness(float value)
    {
        horizontalThickness = value;
        hBar.height(value);
        return this;
    }
    
    
    public boolean verticalScroll()
    {
        return verticalScroll;
    }
    
    
    public ScrollElement verticalScroll(boolean value)
    {
        verticalScroll = value;
        setElementVisAndEnabled(vBar, value);
        return this;
    }
    
    
    public boolean verticalFlip()
    {
        return verticalFlip;
    }
    
    
    public ScrollElement verticalFlip(boolean value)
    {
        if (value != verticalFlip())
        {
            verticalFlip = value;
            flipVertical(value);
        }
        return this;
    }
    
    
    public float verticalThickness()
    {
        return verticalThickness;
    }
    
    
    public ScrollElement verticalThickness(float value)
    {
        verticalThickness = value;
        vBar.width(value);
        return this;
    }
    
    
    public float contentWidth()
    {
        return content.width;
    }
    
    
    public float contentHeight()
    {
        return content.height;
    }
}
