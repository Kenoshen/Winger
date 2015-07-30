package com.winger.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.winger.draw.font.CFont;
import com.winger.draw.font.FontManager;
import com.winger.draw.texture.CSpriteBatch;
import com.winger.draw.texture.CTexture;
import com.winger.draw.texture.TextureManager;
import com.winger.input.raw.CKeyboard;
import com.winger.log.HTMLLogger;
import com.winger.log.LogGroup;
import com.winger.math.VectorMath;
import com.winger.struct.CRectangle;
import com.winger.ui.element.impl.DragDropElement;
import com.winger.ui.element.impl.GridElement;
import com.winger.ui.element.impl.ScrollElement;
import com.winger.ui.element.impl.TextBoxElement;
import com.winger.utils.ParseUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("unchecked")
/**
 * ELement plays a key role in the Winger.UI framework. It is the equivalent of an HTML element. But whereas an HTML element has an underlying data
 * structure of HTML, this element is made up of a JSON object. It has position, size, color, text, image, children, and many more tings. It handles
 * UI events and knows where and when to draw itself.
 *
 * @author Michael P. Wingfield
 * @copyright Nov 4, 2014
 *
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @Type(value = DragDropElement.class, name = "dragdrop"), @Type(value = GridElement.class, name = "grid"),
    @Type(value = ScrollElement.class, name = "scroll"), @Type(value = TextBoxElement.class, name = "textbox") })
public abstract class Element<T> implements Iterable<Element<?>>
{
    protected final HTMLLogger log;
    //
    protected ElementRecord record;
    //
    protected String id;
    protected String type;
    protected float x;
    protected float y;
    protected float z;
    protected float width;
    protected float height;
    protected float rotation;
    protected String text;
    protected Alignment alignment;
    protected Alignment textAlignment;
    protected Color color;
    protected Color colorHover;
    protected Color colorSelect;
    protected Color textColor;
    protected CTexture icon;
    protected CTexture texture;
    protected CTexture textureHover;
    protected CTexture textureSelect;
    protected CFont font;
    protected boolean isEnabled;
    protected boolean isVisible;
    protected boolean hasFocus;
    protected String onHoverStart;
    protected String onHoverEnd;
    protected String onSelectStart;
    protected String onSelectEnd;
    protected String onDragStart;
    protected String onDragEnd;
    protected String transitionOnSelect;
    //
    protected Element<?> parent;
    protected Page page;
    protected List<Element<?>> children;
    protected Rectangle sourceRectangle;
    //
    protected int enumeratorPosition = 0;
    protected List<Element<?>> enumeratorList = new ArrayList<Element<?>>();
    //
    protected CTexture tex = null;
    protected Color col = Color.WHITE.cpy();
    protected boolean isHover = false;
    protected boolean isSelected = false;
    protected boolean isDragging = false;
    //
    protected float parentWidth = 0;
    protected float parentHeight = 0;
    protected float parentZ = 0;
    
    
    // ////////////////////////////////////////////////////////
    // Initialization
    // ////////////////////////////////////////////////////////
    public Element(Page page, ElementRecord record, Element<?> parent)
    {
        log = HTMLLogger.getLogger(this.getClass(), LogGroup.Framework, LogGroup.GUI);
        this.page = page;
        this.children = new ArrayList<Element<?>>();
        init(record, parent);
    }

    /**
     * Gets the text offset using the alignment and size of the object
     *
     * @param alignment
     * @param textAlignment
     * @param textAreaWidth
     * @param textAreaHeight
     * @param textSizeWidth
     * @param textSizeHeight
     * @return
     */
    public static Vector2 getTextOffset(Alignment alignment, Alignment textAlignment, float textAreaWidth, float textAreaHeight, float textSizeWidth,
                                        float textSizeHeight) {
        Vector2 offset = textAlignment.alignmentVector().sub(alignment.alignmentVector());
        offset.x = (offset.x * textAreaWidth);
        offset.y = (offset.y * textAreaHeight) + (1 - textAlignment.alignmentVector().y) * textSizeHeight;

        return offset;
    }

    private static void getAncestorChainRecurse(List<Element<?>> chain, Element<?> current) {
        if (current != null && current.parent != null) {
            chain.add(current.parent);
            getAncestorChainRecurse(chain, current.parent);
        }
    }

    protected static int getBitmapFontAlignment(Alignment align) {
        int a = Align.center;
        switch (align) {
            case BOTTOM:
                break;
            case BOTTOM_LEFT:
                a = Align.left;
                break;
            case BOTTOM_RIGHT:
                a = Align.right;
                break;
            case CENTER:
                break;
            case LEFT:
                a = Align.left;
                break;
            case RIGHT:
                a = Align.right;
                break;
            case TOP:
                break;
            case TOP_LEFT:
                a = Align.left;
                break;
            case TOP_RIGHT:
                a = Align.right;
                break;
            default:
                break;
        }
        return a;
    }

    protected static void getElementTreeAsListRecurse(int elementPosition, List<Element<?>> elements) {
        if (elementPosition >= elements.size()) {
            return;
        }
        Element<?> curElement = elements.get(elementPosition);
        for (Element<?> child : curElement.children) {
            elements.add(child);
        }
        getElementTreeAsListRecurse(elementPosition + 1, elements);
    }
    
    protected T init(ElementRecord record, Element<?> parent)
    {
        this.parent = parent;
        this.record = record;
        //
        Element<?> curParent = parent;
        while (parentWidth == 0f)
        {
            if (curParent != null)
            {
                parentWidth = curParent.width;
                parentHeight = curParent.height;
                parentZ = curParent.z;
                curParent = curParent.parent;
            } else
            {
                parentWidth = Gdx.graphics.getWidth();
                parentHeight = Gdx.graphics.getHeight();
                parentZ = 0;
            }
        }
        //
        return (T) this;
    }
    
    /**
     * Must be called on element creation
     */
    public T initialize()
    {
        //
        // get strings
        id = get("id");
        type = get("type");
        text = get("text");
        onHoverStart = get("onHoverStart");
        onHoverEnd = get("onHoverEnd");
        onSelectStart = get("onSelectStart");
        onSelectEnd = get("onSelectEnd");
        onDragStart = get("onDragStart");
        onDragEnd = get("onDragEnd");
        transitionOnSelect = get("transitionOnSelect");
        //
        // get booleans
        isEnabled = parseBoolean(get("isEnabled"), true);
        isVisible = parseBoolean(get("isVisible"), true);
        hasFocus = parseBoolean(get("hasFocus"), false);
        //
        // calculate percentages
        if (parent != null)
        {
            rotation = parsePercentage(get("rotation"), parent.rotation);
        } else
        {
            rotation = 0;
        }
        x = parsePercentage(get("x"), parentWidth);
        y = parsePercentage(get("y"), parentHeight);
        z = parsePercentage(get("z"), parentZ);
        width = parsePercentage(get("width"), parentWidth);
        height = parsePercentage(get("height"), parentHeight);
        //
        // get alignments
        alignment = parseAlignment(get("alignment"));
        textAlignment = parseAlignment(get("textAlignment"));
        //
        // get colors
        color = parseColor(get("color"), Color.WHITE);
        colorHover = parseColor(get("colorHover"), Color.WHITE);
        colorSelect = parseColor(get("colorSelect"), Color.WHITE);
        textColor = parseColor(get("textColor"), Color.BLACK);
        //
        // get textures
        icon = parseTexture(get("icon"));
        texture = parseTexture(get("texture"));
        textureHover = parseTexture(get("textureHover"));
        textureSelect = parseTexture(get("textureSelect"));
        //
        // get fonts
        font = parseFont(get("font"));
        return (T) this;
    }
    
    /**
     * Gets called after this element's children are initialized
     *
     * @return
     */
    public T afterChildrenInitialize()
    {
        return (T) this;
    }
    
    // ////////////////////////////////////////////////////////
    // Parsing Helpers
    // ////////////////////////////////////////////////////////
    protected <K extends Number> K parseNumber(Number num, K defaultNum)
    {
        if (num != null)
        {
            if (defaultNum instanceof Long)
                return (K) new Long(num.longValue());
            else if (defaultNum instanceof Integer)
                return (K) new Integer(num.intValue());
            else if (defaultNum instanceof Short)
                return (K) new Short(num.shortValue());
            else if (defaultNum instanceof Byte)
                return (K) new Byte(num.byteValue());
            else if (defaultNum instanceof Double)
                return (K) new Double(num.doubleValue());
            else if (defaultNum instanceof Float)
                return (K) new Float(num.floatValue());
        }
        return defaultNum;
    }
    
    protected boolean parseBoolean(Boolean b, boolean defaultValue)
    {
        if (b == null)
        {
            return defaultValue;
        }
        return b;
    }
    
    protected float parsePercentage(Object sub, float parent)
    {
        if (sub == null)
        {
            return 0;
        }
        if (sub instanceof String)
        {
            String valStr = (String) sub;
            float valDub = 0;
            if (valStr.trim().endsWith("%"))
            {
                try
                {
                    valDub = Float.parseFloat(valStr.replace("%", "").trim());
                    valDub *= 0.01f;
                    valDub *= parent;
                } catch (Exception e)
                {}
            }
            return valDub;
        } else
        {
            return ((Number) sub).floatValue();
        }
    }


    // ////////////////////////////////////////////////////////
    // Draw Methods
    // ////////////////////////////////////////////////////////

    protected Alignment parseAlignment(String alignmentStr)
    {
        if (alignmentStr == null)
        {
            return Alignment.CENTER;
        }
        return Alignment.fromStr(alignmentStr);
    }
    
    protected Color parseColor(String colorStr, Color defaultColor)
    {
        if (colorStr == null)
        {
            return defaultColor.cpy();
        }
        return ParseUtils.decodeColor(colorStr);
    }
    
    protected CTexture parseTexture(String textureStr)
    {
        if (textureStr == null)
        {
            return null;
        }
        return TextureManager.instance().getTexture(textureStr);
    }
    
    protected CFont parseFont(String fontStr)
    {
        return FontManager.instance().getFont(fontStr);
    }
    
    /**
     * Draws its texture, text, icon where applicable
     *
     * @param spriteBatch
     */
    public void draw(CSpriteBatch spriteBatch)
    {
        if (isVisible)
        {
            tex = null;

            if (isNeutral())
            {
                tex = texture;
                col = color;
            } else if (isHover())
            {
                tex = textureHover;
                col = colorHover;
            } else if (isSelected())
            {
                tex = textureSelect;
                col = colorSelect;
            }

            if (isHover() && textureHover == null)
            {
                tex = texture;
            } else if (isSelected() && textureSelect == null)
            {
                tex = texture;
            }

            CRectangle absoluteRect = getAbsoluteBoundingBox();
            CRectangle drawsourceRect = calculateDrawsourceRectangle(absoluteRect);
            drawsourceRect.origin = getSpriteOrigin(absoluteRect.width, absoluteRect.height);
            if (tex != null)
            {
                Rectangle textureSourceRect = calculateTexturesourceRectangle(absoluteRect, tex);
                tex.draw(spriteBatch, drawsourceRect, textureSourceRect, false, false, col, z());

            }

            if (icon != null)
            {
                Rectangle textureSourceRect = calculateTexturesourceRectangle(absoluteRect, icon);
                icon.draw(spriteBatch, drawsourceRect, textureSourceRect, false, false, col, z());
            }

            // LIB GDX 1.5.3
            // if (font != null && text != null && !"".equals(text) && sourceRectangle == null)
            // {
            // TextBounds bounds = font.getMultiLineBounds(text);
            // Vector2 offset = getTextOffset(alignment, textAlignment, drawsourceRect.width, drawsourceRect.height, bounds.width, bounds.height);
            // font.drawMultiLine(spriteBatch, text, drawsourceRect.x + offset.x, drawsourceRect.y + offset.y, 0,
            // getBitmapFontAlignment(textAlignment), textColor, z());
            // }

            // LIB GDX 1.5.6
            if (font != null && text != null && !"".equals(text) && sourceRectangle == null)
            {
                GlyphLayout bounds = new GlyphLayout(font, text);
                Vector2 offset = getTextOffset(alignment, textAlignment, drawsourceRect.width, drawsourceRect.height, bounds.width, bounds.height);
                font.draw(spriteBatch, text, drawsourceRect.x + offset.x, drawsourceRect.y + offset.y, 0, getBitmapFontAlignment(textAlignment),
                    textColor, z());
            }
        }
    }
    
    public void debugDraw(CSpriteBatch spriteBatch)
    {
        spriteBatch.drawRectangle(ShapeType.Line, getAbsoluteBoundingBox(), Color.MAGENTA, 1000);
    }
    
    /**
     * Using the Alignment property, gets the origin of the element
     *
     * @param width
     * @param height
     * @return
     */
    public Vector2 getSpriteOrigin(float width, float height)
    {
        Vector2 org = alignment.alignmentVector();
        org.x *= width;
        org.y *= height;
        return org;
    }
    
    /**
     * Gets the source rectangle for this texture using its absolute rectangle
     *
     * @param absoluteRect
     * @param texture
     * @return
     */
    protected Rectangle calculateTexturesourceRectangle(CRectangle absoluteRect, CTexture texture)
    {
        if (sourceRectangle == null)
        {
            return new Rectangle(texture.location);
        }
        Rectangle sr = new Rectangle(0, 0, 0, 0);
        float ratioX = sourceRectangle.x / absoluteRect.width;
        float ratioY = sourceRectangle.y / absoluteRect.height;
        float ratioW = sourceRectangle.width / absoluteRect.width;
        float ratioH = sourceRectangle.height / absoluteRect.height;
        sr.x = (int) (texture.location.width * ratioX);
        sr.y = (int) (texture.location.height * ratioY);
        sr.width = (int) (texture.location.width * ratioW);
        sr.height = (int) (texture.location.height * ratioH);
        return sr;
    }
    
    /**
     * Gets the draw source rectangle for this object using the absolute rectangle
     *
     * @param absoluteRect
     * @return
     */
    protected CRectangle calculateDrawsourceRectangle(CRectangle absoluteRect)
    {
        if (sourceRectangle == null)
        {
            return absoluteRect.clone();
        } else
        {
            CRectangle drawRect = new CRectangle((int) absoluteRect.x, (int) absoluteRect.y, sourceRectangle.width, sourceRectangle.height,
                absoluteRect.rotation);
            if (sourceRectangle.x > 0)
            {
                drawRect.x += (int) (absoluteRect.width - sourceRectangle.width);
            }
            if (sourceRectangle.y > 0)
            {
                drawRect.y += (int) (absoluteRect.height - sourceRectangle.height);
            }
            return drawRect;
        }
    }
    
    /**
     * Follows the parent property to find a list of ancestors
     *
     * @return
     */
    public List<Element<?>> getAncestorChain()
    {
        List<Element<?>> chain = new ArrayList<Element<?>>();
        getAncestorChainRecurse(chain, this);
        return chain;
    }
    
    /**
     * Gets the absolute bounding box for a given element taking into account this element's inherited position, this absolute box is in screen
     * coordinates
     *
     * @return
     */
    public CRectangle getAbsoluteBoundingBox()
    {
        CRectangle rect = new CRectangle(x, y, width, height, rotation);
        rect.origin = getOrigin();

        List<Element<?>> ancestors = getAncestorChain();
        for (Element<?> ancestor : ancestors)
        {
            Vector2 ancestorPoint = new Vector2(ancestor.x, ancestor.y);
            Vector2 curPoint = new Vector2(rect.x, rect.y);
            curPoint = curPoint.add(ancestorPoint);
            if (ancestor.rotation != 0)
            {
                curPoint = VectorMath.rotatePointByDegreesAroundZero(curPoint.sub(ancestorPoint), ancestor.rotation).add(ancestorPoint);
                rect.rotation += ancestor.rotation;
            }
            rect.x = curPoint.x;
            rect.y = curPoint.y;
        }

        return rect;
    }
    
    /**
     * Gets the drawing bounding box for this element. Basically the source rectangle but at the position of the absolute rectangle.
     *
     * @return
     */
    public CRectangle getDrawBoundingBox()
    {
        CRectangle rect = getAbsoluteBoundingBox();
        if (sourceRectangle != null)
        {
            CRectangle drawBoundingBox = new CRectangle(sourceRectangle);
            drawBoundingBox.x += rect.x;
            drawBoundingBox.y += rect.y;
            return drawBoundingBox;
        }
        return rect;
    }
    
    protected Vector2 getOrigin()
    {
        Vector2 org = alignment.alignmentVector();
        org.x *= width;
        org.y *= height;
        return org;
    }
    
    /**
     * Not sure what this was supposed to do yet...
     *
     * @param keyboard
     */
    public T sendKeyboardInfoToThisElement(CKeyboard keyboard)
    {
        return (T) this;
    }
    
    /**
     * Gets this element and its children in a list using depth-first recursion
     *
     * @return
     */
    public List<Element<?>> getElementTreeAsList()
    {
        List<Element<?>> elements = new ArrayList<Element<?>>();
        elements.add(this);
        getElementTreeAsListRecurse(0, elements);
        return elements;
    }
    
    // ////////////////////////////////////////////////////////
    // Events
    // ////////////////////////////////////////////////////////
    public boolean isNeutral()
    {
        return !(isHover || isSelected);
    }
    
    
    public T onHoverStartEvent()
    {
        isHover(true);
        isSelected(false);
        return (T) this;
    }
    
    
    public T onHoverEndEvent()
    {
        isHover(false);
        return (T) this;
    }
    
    
    public T onSelectStartEvent()
    {
        isSelected(true);
        isHover(false);
        hasFocus(true);
        return (T) this;
    }
    
    
    public T onSelectEndEvent()
    {
        isSelected(false);
        return (T) this;
    }
    
    
    public T onDragStartEvent()
    {
        isDragging(true);
        return (T) this;
    }
    
    
    public T onDragEndEvent()
    {
        isDragging(false);
        return (T) this;
    }
    
    
    public T neutralizeEvent()
    {
        isHover = false;
        isSelected = false;
        hasFocus(false);
        return (T) this;
    }
    
    
    // ////////////////////////////////////////////////////////
    // Generic
    // ////////////////////////////////////////////////////////
    /**
     * Gets this element and its children in a list using depth-first recursion
     */
    @Override
    public Iterator<Element<?>> iterator()
    {
        return getElementTreeAsList().iterator();
    }
    
    
    public String toString()
    {
        // try
        // {
        // return JSON.serialize(this);
        // } catch (JsonProcessingException e)
        // {
        // e.printStackTrace();
        // }
        
        return "{" + id() + (type() != null ? "@" + type() : "") + " " + this.getAbsoluteBoundingBox() + " }";
    }
    
    
    protected final <K> K get(String key)
    {
        if (record != null && record.containsKey(key))
            return (K) record.get(key);
        return null;
    }
    
    
    protected final T set(String key, Object obj)
    {
        if (record != null)
            record.put(key, obj);
        return (T) this;
    }
    
    
    // ////////////////////////////////////////////////////////
    // Getters and Setters
    // ////////////////////////////////////////////////////////
    public String id()
    {
        return id;
    }
    
    
    public T id(String id)
    {
        this.id = id;
        return (T) this;
    }
    
    
    public String type()
    {
        return type;
    }
    
    
    public T type(String type)
    {
        this.type = type;
        return (T) this;
    }
    
    
    public float x()
    {
        return x;
    }
    
    
    public T x(float x)
    {
        this.x = x;
        return (T) this;
    }
    
    
    public float y()
    {
        return y;
    }
    
    
    public T y(float y)
    {
        this.y = y;
        return (T) this;
    }
    
    
    public float z()
    {
        return z;
    }
    
    
    public T z(float z)
    {
        this.z = z;
        return (T) this;
    }
    
    
    public float width()
    {
        return width;
    }
    
    
    public T width(float width)
    {
        this.width = width;
        return (T) this;
    }
    
    
    public float height()
    {
        return height;
    }
    
    
    public T height(float height)
    {
        this.height = height;
        return (T) this;
    }
    
    
    public float rotation()
    {
        return rotation;
    }
    
    
    public T rotation(float rotation)
    {
        this.rotation = rotation;
        return (T) this;
    }
    
    
    public String text()
    {
        return text;
    }
    
    
    public T text(String text)
    {
        this.text = text;
        return (T) this;
    }
    
    
    public Alignment alignment()
    {
        return alignment;
    }
    
    
    public T alignment(Alignment alignment)
    {
        this.alignment = alignment;
        return (T) this;
    }
    
    
    public Alignment textAlignment()
    {
        return textAlignment;
    }
    
    
    public T textAlignment(Alignment textAlignment)
    {
        this.textAlignment = textAlignment;
        return (T) this;
    }
    
    
    public Color color()
    {
        return color;
    }
    
    
    public T color(Color color)
    {
        this.color = color;
        return (T) this;
    }
    
    
    public Color colorHover()
    {
        return colorHover;
    }
    
    
    public T colorHover(Color colorHover)
    {
        this.colorHover = colorHover;
        return (T) this;
    }
    
    
    public Color colorSelect()
    {
        return colorSelect;
    }
    
    
    public T colorSelect(Color colorSelect)
    {
        this.colorSelect = colorSelect;
        return (T) this;
    }
    
    
    public Color textColor()
    {
        return textColor;
    }
    
    
    public T textColor(Color textColor)
    {
        this.textColor = textColor;
        return (T) this;
    }
    
    
    public CTexture icon()
    {
        return icon;
    }
    
    
    public T icon(CTexture icon)
    {
        this.icon = icon;
        return (T) this;
    }
    
    
    public CTexture texture()
    {
        return texture;
    }
    
    
    public T texture(CTexture texture)
    {
        this.texture = texture;
        return (T) this;
    }
    
    
    public CTexture textureHover()
    {
        return textureHover;
    }
    
    
    public T textureHover(CTexture textureHover)
    {
        this.textureHover = textureHover;
        return (T) this;
    }
    
    
    public CTexture textureSelect()
    {
        return textureSelect;
    }
    
    
    public T textureSelect(CTexture textureSelect)
    {
        this.textureSelect = textureSelect;
        return (T) this;
    }
    
    
    public CFont font()
    {
        return font;
    }
    
    
    public T font(CFont font)
    {
        this.font = font;
        return (T) this;
    }
    
    
    public boolean isEnabled()
    {
        return isEnabled;
    }
    
    
    public T isEnabled(boolean isEnabled)
    {
        this.isEnabled = isEnabled;
        return (T) this;
    }
    
    
    public boolean isVisible()
    {
        return isVisible;
    }
    
    
    public T isVisible(boolean isVisible)
    {
        this.isVisible = isVisible;
        return (T) this;
    }
    
    
    public boolean hasFocus()
    {
        return hasFocus;
    }
    
    
    public T hasFocus(boolean hasFocus)
    {
        this.hasFocus = hasFocus;
        return (T) this;
    }
    
    
    public String transitionOnSelect()
    {
        return transitionOnSelect;
    }
    
    
    public T transitionOnSelect(String transitionOnSelect)
    {
        this.transitionOnSelect = transitionOnSelect;
        return (T) this;
    }
    
    
    @JsonIgnore
    public Element<?> parent()
    {
        return parent;
    }
    
    
    public T parent(Element<?> parent)
    {
        this.parent = parent;
        return (T) this;
    }
    
    
    @JsonIgnore
    public Page page()
    {
        return page;
    }
    
    
    public boolean isHover()
    {
        return isHover;
    }
    
    
    public T isHover(boolean isHover)
    {
        this.isHover = isHover;
        return (T) this;
    }
    
    
    public boolean isSelected()
    {
        return isSelected;
    }
    
    
    public T isSelected(boolean isSelected)
    {
        this.isSelected = isSelected;
        return (T) this;
    }
    
    
    public boolean isDragging()
    {
        return isDragging;
    }
    
    
    public T isDragging(boolean isDragging)
    {
        this.isDragging = isDragging;
        return (T) this;
    }
    
    
    public String onHoverStart()
    {
        return onHoverStart;
    }
    
    
    public T onHoverStart(String value)
    {
        onHoverStart = value;
        return (T) this;
    }
    
    
    public String onHoverEnd()
    {
        return onHoverEnd;
    }
    
    
    public T onHoverEnd(String value)
    {
        onHoverEnd = value;
        return (T) this;
    }
    
    
    public String onSelectStart()
    {
        return onSelectStart;
    }
    
    
    public T onSelectStart(String value)
    {
        onSelectStart = value;
        return (T) this;
    }
    
    
    public String onSelectEnd()
    {
        return onSelectEnd;
    }
    
    
    public T onSelectEnd(String value)
    {
        onSelectEnd = value;
        return (T) this;
    }
    
    
    public String onDragStart()
    {
        return onDragStart;
    }
    
    
    public T onDragStart(String value)
    {
        onDragStart = value;
        return (T) this;
    }
    
    
    public String onDragEnd()
    {
        return onDragEnd;
    }
    
    
    public T onDragEnd(String value)
    {
        onDragEnd = value;
        return (T) this;
    }
    
    
    public Rectangle sourceRectangle()
    {
        return sourceRectangle;
    }
    
    
    public T sourceRectangle(Rectangle value)
    {
        sourceRectangle = value;
        return (T) this;
    }
    
    
    public List<Element<?>> children()
    {
        return children;
    }
}
