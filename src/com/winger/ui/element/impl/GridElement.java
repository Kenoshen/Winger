package com.winger.ui.element.impl;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.winger.math.VectorMath;
import com.winger.struct.CRectangle;
import com.winger.ui.Alignment;
import com.winger.ui.Element;
import com.winger.ui.ElementRecord;
import com.winger.ui.Page;

/**
 * An element that arranges its children in a grid layout
 *
 * @author Michael P. Wingfield
 * @copyright Nov 4, 2014
 *
 */
public class GridElement extends ContainerElement<GridElement>
{
    protected int row;
    protected int column;
    
    
    public GridElement(Page page, ElementRecord record, Element<?> parent)
    {
        super(page, record, parent);
    }
    
    
    @Override
    public GridElement initialize()
    {
        super.initialize();
        
        row = parseNumber((Number) get("row"), -1);
        column = parseNumber((Number) get("column"), -1);
        
        return this;
    }
    
    
    @Override
    public GridElement afterChildrenInitialize()
    {
        arrangeChildren();
        return this;
    }
    
    
    public void arrangeChildren()
    {
        int rowCount = row();
        int colCount = column();
        if (rowCount < 0 && colCount < 0)
        {
            rowCount = 1;
            colCount = 1000000;
        } else if (rowCount < 0)
        {
            rowCount = 1000000;
        } else if (colCount < 0)
        {
            colCount = 1000000;
        }
        float padL = paddingLeft();
        float padR = paddingRight();
        float padT = paddingTop();
        float padB = paddingBottom();
        
        List<Float> rowHeights = new ArrayList<Float>();
        for (int r = 0; r < rowCount; r++)
        {
            rowHeights.add(0f);
            for (int c = 0; c < colCount; c++)
            {
                if (r * colCount + c < children.size())
                {
                    Element<?> child = children.get(r * colCount + c);
                    if (child.height() > rowHeights.get(r))
                    {
                        rowHeights.set(r, child.height());
                    }
                } else
                {
                    r = rowCount;
                    c = colCount;
                    break;
                }
            }
        }
        List<Float> colWidths = new ArrayList<Float>();
        int childCount = 0;
        for (int c = 0; c < colCount; c++)
        {
            colWidths.add(0f);
            for (int r = 0; r < rowCount; r++)
            {
                if (r * colCount + c < children.size())
                {
                    Element<?> child = children.get(r * colCount + c);
                    if (child.width() > colWidths.get(c))
                    {
                        colWidths.set(c, child.width());
                    }
                    childCount++;
                } else
                {
                    break;
                }
            }
            if (childCount >= children.size())
            {
                break;
            }
        }
        
        Vector2 aV = VectorMath.multiply(VectorMath.multiply(alignment().alignmentVector(), 2).sub(1, 1), -1);
        Vector2 aVSingle = alignment().alignmentVector();
        if (aV.x == 0)
        {
            aV.x = 1;
            aVSingle.x = 0;
        }
        if (aV.y == 0)
        {
            aV.y = -1;
            aVSingle.y = 1;
        }
        float x = 0;
        float y = 0;
        float totalWidth = 0;
        for (int r = 0; r < rowCount; r++)
        {
            y += padT * aV.y;
            x = 0;
            boolean didBreak = false;
            for (int c = 0; c < colCount; c++)
            {
                if (r * colCount + c < children.size())
                {
                    x += padL * aV.x;
                    
                    Element<?> child = children.get(r * colCount + c);
                    Vector2 childAlignment = child.alignment().alignmentVector().sub(aVSingle);
                    CRectangle cell = new CRectangle(x, y, colWidths.get(c), rowHeights.get(r));
                    
                    child.x(cell.x + (childAlignment.x * cell.width));
                    child.y(cell.y + (childAlignment.y * cell.height));
                    
                    x += cell.width * aV.x;
                    x += padR * aV.x;
                } else
                {
                    didBreak = true;
                    break;
                }
            }
            y += (rowHeights.get(r) + padB) * aV.y;
            
            if (Math.abs(x) > Math.abs(totalWidth))
            {
                totalWidth = x;
            }
            if (didBreak)
            {
                break;
            }
        }
        width(Math.abs(totalWidth));
        height(Math.abs(y));
        
        /*
         * Vector2 aV = VectorMath.multiply(VectorMath.multiply((alignment() != Alignment.CENTER ? alignment().alignmentVector() :
         * Alignment.BOTTOM_LEFT.alignmentVector()), 2).sub(1, 1), -1); if (aV.x == 0) { aV.x = 1; } if (aV.y == 0) { aV.y = -1; }
         * 
         * float w = 0; float h = 0; float rowHeight = 0; float maxWidth = 0; float firstRowHeight = 0;
         * 
         * int i = 0; List<Element> children = children(); for (int r = 0; r < rowCount; r++) { h += padT * aV.y; w = 0; rowHeight = 0; for (int c =
         * 0; c < colCount; c++) { if (i < children.size()) { Element child = children.get(i); if (child.height() > rowHeight) { rowHeight =
         * child.height(); }
         * 
         * Vector2 origin = VectorMath.multiply(child.alignment().alignmentVector(), new Vector2(child.width(), child.height())); Vector2 widHei = new
         * Vector2(child.width(), child.height()).sub(origin);
         * 
         * w += (padL + origin.x) * aV.x; child.x(w); child.y(h + origin.y); w += (widHei.x + padR) * aV.x;
         * 
         * if (Math.abs(w) > Math.abs(maxWidth)) { maxWidth = w; } i++; } else { break; } } if (r == 0) { firstRowHeight = rowHeight; } h +=
         * (rowHeight + padB) * aV.y; if (i >= children.size()) { break; } } for (;i < children.size(); i++) { children.get(i).isEnabled(false);
         * children.get(i).isVisible(false); }
         * 
         * 
         * 
         * width(Math.abs(maxWidth)); height(Math.abs(h));
         */
        if (alignment() == Alignment.TOP)
        {
            for (Element<?> child : children)
            {
                child.x(child.x() - (width() / 2));
            }
        } else if (alignment() == Alignment.BOTTOM)
        {
            for (Element<?> child : children)
            {
                child.x(child.x() - (width() / 2));
            }
        } else if (alignment() == Alignment.LEFT)
        {
            for (Element<?> child : children)
            {
                child.y(child.y() + (height() / 2));
            }
        } else if (alignment() == Alignment.RIGHT)
        {
            for (Element<?> child : children)
            {
                child.y(child.y() + (height() / 2));
            }
        } else if (alignment() == Alignment.CENTER)
        {
            for (Element<?> child : children)
            {
                child.x(child.x() - (width() / 2));
                child.y(child.y() + (height() / 2));
            }
        }
    }
    
    
    // ////////////////////////////////////////////////////////
    // Getters and Setters
    // ////////////////////////////////////////////////////////
    public int row()
    {
        return row;
    }
    
    
    public GridElement row(int value)
    {
        row = value;
        return this;
    }
    
    
    public int column()
    {
        return column;
    }
    
    
    public GridElement column(int value)
    {
        column = value;
        return this;
    }
}
