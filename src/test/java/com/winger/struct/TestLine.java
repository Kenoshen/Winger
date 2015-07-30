package com.winger.struct;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.winger.log.HTMLLogger;
import com.winger.log.LogGroup;
import org.junit.Test;

@SuppressWarnings("unused")
public class TestLine
{
    private static final HTMLLogger log = HTMLLogger.getLogger(TestLine.class, LogGroup.Framework, LogGroup.Assert);
    
    
    @Test
    public void testLineLength()
    {
        Line a = new Line(10, 0);
        eq(a.length(), 10f, "horizontal line failed");
        
        a = new Line(0, 10);
        eq(a.length(), 10f, "vertical line failed");
        
        a = new Line(10, 10);
        tr(a.length() > 10f && a.length() < 15f, "angled line failed");
        
        a = new Line(0, 10, 10, 10);
        eq(a.length(), 10f, "floating horizontal line failed");
        
        a = new Line(10, 0, 10, 10);
        eq(a.length(), 10f, "floating vertical line failed");
        
        a = new Line(0, 10, 10, 0);
        tr(a.length() > 10f && a.length() < 15f, "floating angled line failed");
    }
    
    
    @Test
    public void testLineIntersect()
    {
        Line a = new Line(5, 5, 10, 10);
        Line b = new Line(5, 10, 10, 5);
        tr(a.intersects(b), a + " and " + b);
        tr(b.intersects(a), b + " and " + a);
        
        a = new Line(10, 10);
        b = new Line(-10, -10);
        tr(a.intersects(b), a + " and " + b);
        tr(b.intersects(a), b + " and " + a);
        
        a = new Line(0, 10);
        b = new Line(-5, -5, 5, 5);
        tr(a.intersects(b), a + " and " + b);
        tr(b.intersects(a), b + " and " + a);
        
        a = new Line(0, 10);
        b = new Line(-4, -4, 6, 6);
        tr(a.intersects(b), a + " and " + b);
        tr(b.intersects(a), b + " and " + a);
        
        a = new Line(0, 10);
        b = new Line(0, 5, 5, 5);
        tr(a.intersects(b), a + " and " + b);
        tr(b.intersects(a), b + " and " + a);
        
        a = new Line(0, 10);
        b = new Line(20, 10, 10, 20);
        fa(a.intersects(b), a + " and " + b);
        fa(b.intersects(a), b + " and " + a);
    }
    
    
    @Test
    public void testLineIntersection()
    {
        Line a = new Line(10, 10);
        Line b = new Line(10, 0, 0, 10);
        eq(a.intersection(b), new Vector3(5, 5, 0), a + " and " + b);
        eq(b.intersection(a), new Vector3(5, 5, 0), b + " and " + a);
        
        a = new Line(0, 10, 12, 10);
        b = new Line(10, 0, 10, 12);
        eq(a.intersection(b), new Vector3(10, 10, 0), a + " and " + b);
        eq(b.intersection(a), new Vector3(10, 10, 0), b + " and " + a);
    }
    
    
    @Test
    public void testLineCollinear()
    {
        // ||-----|---|
        Line a = new Line(10, 10);
        Line b = new Line(5, 5);
        tr(a.collinear(b), a + " and " + b);
        tr(b.collinear(a), b + " and " + a);
        
        // |---|----|---|
        a = new Line(0, 0, 10, 0);
        b = new Line(2, 0, 8, 0);
        tr(a.collinear(b), a + " and " + b);
        tr(b.collinear(a), b + " and " + a);
        
        // |----||----|
        a = new Line(0, 0, 10, 0);
        b = new Line(10, 0, 20, 0);
        tr(a.collinear(b), a + " and " + b);
        tr(b.collinear(a), b + " and " + a);
        
        // |----| |----|
        a = new Line(0, 10, 10, 10);
        b = new Line(20, 10, 30, 10);
        tr(a.collinear(b), a + " and " + b);
        tr(b.collinear(a), b + " and " + a);
        
        // |----*----|
        a = new Line(5, 10, 5, 10);
        b = new Line(0, 10, 20, 10);
        tr(a.collinear(b), a + " and " + b);
        tr(b.collinear(a), b + " and " + a);
        
        // |*----|
        a = new Line(0, 10, 0, 10);
        b = new Line(0, 10, 20, 10);
        fa(a.collinear(b), a + " and " + b);
        fa(b.collinear(a), b + " and " + a);
        
        // |-------|
        // |-------|
        a = new Line(0, 0, 10, 0);
        b = new Line(0, 5, 10, 5);
        fa(a.collinear(b), a + " and " + b);
        fa(b.collinear(a), b + " and " + a);
        
        // \/
        // /\
        a = new Line(0, 0, 10, 10);
        b = new Line(0, 10, 10, 0);
        fa(a.collinear(b), a + " and " + b);
        fa(b.collinear(a), b + " and " + a);
        
        // |----|
        // |
        // |
        a = new Line(0, 10, 10, 10);
        b = new Line(0, 0, 0, 10);
        fa(a.collinear(b), a + " and " + b);
        fa(b.collinear(a), b + " and " + a);
        
        // |----|
        // |
        // |
        a = new Line(0, 10, 10, 10);
        b = new Line(0, 10, 0, 0);
        fa(a.collinear(b), a + " and " + b);
        fa(b.collinear(a), b + " and " + a);
    }
    
    
    @Test
    public void testLineParallel()
    {
        // ||-----|---|
        Line a = new Line(10, 10);
        Line b = new Line(5, 5);
        tr(a.parallel(b), a + " and " + b);
        tr(b.parallel(a), b + " and " + a);
        
        // |---|----|---|
        a = new Line(0, 0, 10, 0);
        b = new Line(2, 0, 8, 0);
        tr(a.parallel(b), a + " and " + b);
        tr(b.parallel(a), b + " and " + a);
        
        // |----||----|
        a = new Line(0, 0, 10, 0);
        b = new Line(10, 0, 20, 0);
        tr(a.parallel(b), a + " and " + b);
        tr(b.parallel(a), b + " and " + a);
        
        // |----| |----|
        a = new Line(0, 10, 10, 10);
        b = new Line(20, 10, 30, 10);
        tr(a.parallel(b), a + " and " + b);
        tr(b.parallel(a), b + " and " + a);
        
        // |-------|
        // |-------|
        a = new Line(0, 0, 10, 0);
        b = new Line(0, 5, 10, 5);
        tr(a.parallel(b), a + " and " + b);
        tr(b.parallel(a), b + " and " + a);
        
        // |\ /|
        // \/
        // /\
        // |/ \|
        a = new Line(0, 0, 10, 10);
        b = new Line(0, 10, 10, 0);
        fa(a.parallel(b), a + " and " + b);
        fa(b.parallel(a), b + " and " + a);
        
        // |----|
        // |
        // |
        a = new Line(0, 10, 10, 10);
        b = new Line(0, 0, 0, 10);
        fa(a.parallel(b), a + " and " + b);
        fa(b.parallel(a), b + " and " + a);
    }
    
    
    @Test
    public void testLineContains()
    {
        Line a = new Line(10, 10);
        Vector2 b = new Vector2(5, 5);
        tr(a.contains(b), a + " and " + b);
        
        a = new Line(10, 10);
        b = new Vector2(0, 0);
        tr(a.contains(b), a + " and " + b);
        
        a = new Line(0, 10, 10, 10);
        b = new Vector2(10, 10);
        tr(a.contains(b), a + " and " + b);
        
        a = new Line(0, 10, 10, 10);
        b = new Vector2(0, 10);
        tr(a.contains(b), a + " and " + b);
        
        a = new Line(0, 10);
        b = new Vector2(0, 5);
        tr(a.contains(b), a + " and " + b);
        
        a = new Line(10, 0);
        b = new Vector2(5, 0);
        tr(a.contains(b), a + " and " + b);
        
        a = new Line(0, 10, 10, 10);
        b = new Vector2(5, 10);
        tr(a.contains(b), a + " and " + b);
        
        a = new Line(10, 10);
        b = new Vector2(5, 0);
        fa(a.contains(b), a + " and " + b);
        
        a = new Line(10, 10);
        b = new Vector2(-5, -5);
        fa(a.contains(b), a + " and " + b);
    }
    
    
    private void tr(boolean b, String message)
    {
        if (!b)
            throw new RuntimeException(b + " != " + true + "  " + message);
    }
    
    
    private void tr(boolean b)
    {
        tr(b, "");
    }
    
    
    private void fa(boolean b, String message)
    {
        if (b)
            throw new RuntimeException(b + " != " + false + "  " + message);
    }
    
    
    private void fa(boolean b)
    {
        fa(b, "");
    }
    
    
    private void eq(Object a, Object b, String message)
    {
        if (a == null && b == null)
        {
            return;
        } else if (a == null)
        {
            throw new RuntimeException(a + " != " + b + "  " + message);
        } else if (b == null)
        {
            throw new RuntimeException(a + " != " + b + "  " + message);
        } else if (!(a.equals(b)))
        {
            throw new RuntimeException(a + " != " + b + "  " + message);
        }
    }
    
    
    private void eq(Object a, Object b)
    {
        eq(a, b, "");
    }
}
