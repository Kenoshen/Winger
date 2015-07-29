package com.winger.struct;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.winger.draw.texture.CSpriteBatch;
import com.winger.math.VectorMath;

public class Line
{
    public final Vector3 start = new Vector3(0, 0, 0);
    public final Vector3 end = new Vector3(0, 0, 0);
    
    
    public Line(float startX, float startY, float startZ, float endX, float endY, float endZ)
    {
        start.x = startX;
        start.y = startY;
        start.z = startZ;
        end.x = endX;
        end.y = endY;
        end.z = endZ;
    }
    
    
    public Line(float startX, float startY, float endX, float endY)
    {
        this(startX, startY, 0, endX, endY, 0);
    }
    
    
    public Line(Vector3 start, Vector3 end)
    {
        this(start.x, start.y, start.z, end.x, end.y, end.z);
    }
    
    
    public Line(Vector2 start, Vector2 end)
    {
        this(start.x, start.y, 0, end.x, end.y, 0);
    }
    
    
    public Line(float endX, float endY, float endZ)
    {
        this(0, 0, 0, endX, endY, endZ);
    }
    
    
    public Line(float endX, float endY)
    {
        this(0, 0, 0, endX, endY, 0);
    }
    
    
    public Line(Vector3 end)
    {
        this(0, 0, 0, end.x, end.y, end.z);
    }
    
    
    public Line(Vector2 end)
    {
        this(0, 0, 0, end.x, end.y, 0);
    }
    
    
    public Line()
    {
        this(0, 0, 0, 0, 0, 0);
    }
    
    
    public Vector3 fromOrigin()
    {
        return end.cpy().sub(start);
    }
    
    
    public float length()
    {
        return fromOrigin().len();
    }
    
    
    public boolean isZero()
    {
        return (length() == 0);
    }
    
    
    public Vector3 direction()
    {
        return fromOrigin().nor();
    }
    
    
    /**
     * <b>NOTE:</b> only checks in 2D and only on line segment
     * 
     * @param other
     * @return
     */
    public boolean intersects(Line other)
    {
        return (intersection(other) != null);
    }
    
    
    /**
     * <b>NOTE:</b> only checks in 2D and only on line segment
     * 
     * @param other
     * @return
     */
    public Vector3 intersection(Line other)
    {
        
        boolean containsStart = contains(other.start);
        boolean containsEnd = contains(other.end);
        if ((containsStart && containsEnd) || (other.contains(start) && other.contains(end)))
        {
            return null;
        } else if (containsStart)
        {
            return other.start.cpy();
        } else if (containsEnd)
        {
            return other.end.cpy();
        } else
        {
            Vector2 result = new Vector2();
            if (Intersector.intersectSegments(toV2(start), toV2(end), toV2(other.start), toV2(other.end), result))
            {
                return VectorMath.toVector3(result);
            }
        }
        return null;
    }
    
    
    public boolean contains(Vector2 point)
    {
        return contains(new Vector3(point.x, point.y, 0));
    }
    
    
    public boolean contains(Vector3 point)
    {
        Vector3 d1 = fromOrigin();
        Vector3 d2 = point.cpy().sub(start);
        return d1.isZero() || d2.isZero() || (d2.isCollinear(d1) && d1.len() >= d2.len());
    }
    
    
    public boolean collinear(Line other)
    {
        Vector3 d1 = fromOrigin();
        Vector3 d2 = other.end.cpy().sub(start);
        if (!d1.isZero() && !d2.isZero() && d2.isOnLine(d1))
        {
            return true;
        } else
        {
            d2 = other.fromOrigin();
            d1 = end.cpy().sub(other.start);
            return (!d1.isZero() && !d2.isZero() && d1.isOnLine(d2));
        }
    }
    
    
    public boolean parallel(Line other)
    {
        Vector3 d1 = fromOrigin();
        Vector3 d2 = other.fromOrigin();
        return (d1.isCollinear(d2) || d1.isCollinearOpposite(d2));
    }
    
    
    public Line clone()
    {
        return new Line(start.x, start.y, start.z, end.x, end.y, end.z);
    }
    
    
    public Line cpy()
    {
        return clone();
    }
    
    
    public void draw(CSpriteBatch sb, Color color, float zIndex)
    {
        sb.drawLine(new Vector2(start.x, start.y), new Vector2(end.x, end.y), color, zIndex);
    }
    
    
    public void draw(CSpriteBatch sb, Color color)
    {
        sb.drawLine(new Vector2(start.x, start.y), new Vector2(end.x, end.y), color);
    }
    
    
    @Override
    public String toString()
    {
        return "{" + start + "->" + end + "}";
    }
    
    
    private static Vector2 toV2(Vector3 v3)
    {
        return VectorMath.toVector2(v3);
    }
}
