package com.winger.struct;

import org.junit.Test;

import com.winger.log.HTMLLogger;
import com.winger.log.LogGroup;

@SuppressWarnings("unused")
public class TestCRectangleUnit
{
    private static final HTMLLogger log = HTMLLogger.getLogger(TestCRectangleUnit.class, LogGroup.Framework, LogGroup.Assert);
    
    CRectangle a;
    CRectangle b;
    
    
    @Test
    public void testZeroAndZero()
    {
        a = new CRectangle(0, 0, 10, 10);
        b = new CRectangle(20, 20, 10, 10);
        
        fa(a.overlaps(b), "a overlaps b");
        fa(b.overlaps(a), "b overlaps a");
        
        fa(a.intersects(b), "a intersects b");
        fa(b.intersects(a), "b intersects a");
        
        fa(a.contains(b), "a contains b");
        fa(b.contains(a), "b contains a");
    }
    
    
    @Test
    public void testOneAndOne()
    {
        a = new CRectangle(0, 0, 10, 10);
        b = new CRectangle(5, 5, 10, 10);
        
        tr(a.overlaps(b), "a overlaps b");
        tr(b.overlaps(a), "b overlaps a");
        
        tr(a.intersects(b), "a intersects b");
        tr(b.intersects(a), "b intersects a");
        
        fa(a.contains(b), "a contains b");
        fa(b.contains(a), "b contains a");
    }
    
    
    @Test
    public void testFourAndZero()
    {
        a = new CRectangle(0, 0, 10, 10);
        b = new CRectangle(5, 5, 2, 2);
        
        tr(a.overlaps(b), "a overlaps b");
        tr(b.overlaps(a), "b overlaps a");
        
        fa(a.intersects(b), "a intersects b");
        fa(b.intersects(a), "b intersects a");
        
        tr(a.contains(b), "a contains b");
        fa(b.contains(a), "b contains a");
    }
    
    
    @Test
    public void testTwoAndZero()
    {
        a = new CRectangle(10, 10, 10, 10);
        b = new CRectangle(11, 9, 5, 5);
        
        tr(a.overlaps(b), "a overlaps b");
        tr(b.overlaps(a), "b overlaps a");
        
        tr(a.intersects(b), "a intersects b");
        tr(b.intersects(a), "b intersects a");
        
        fa(a.contains(b), "a contains b");
        fa(b.contains(a), "b contains a");
    }
    
    
    @Test
    public void testOneAndZero()
    {
        a = new CRectangle(10, 10, 10, 10);
        b = new CRectangle(15, 9, 5, 5, (float) Math.toRadians(45));
        b.setOriginAtCenter();
        
        tr(a.overlaps(b), "a overlaps b");
        tr(b.overlaps(a), "b overlaps a");
        
        tr(a.intersects(b), "a intersects b");
        tr(b.intersects(a), "b intersects a");
        
        fa(a.contains(b), "a contains b");
        fa(b.contains(a), "b contains a");
    }
    
    
    @Test
    public void testThreeAndZero()
    {
        a = new CRectangle(10, 10, 10, 10);
        b = new CRectangle(15, 11, 5, 5, (float) Math.toRadians(45));
        b.setOriginAtCenter();
        
        tr(a.overlaps(b), "a overlaps b");
        tr(b.overlaps(a), "b overlaps a");
        
        tr(a.intersects(b), "a intersects b");
        tr(b.intersects(a), "b intersects a");
        
        fa(a.contains(b), "a contains b");
        fa(b.contains(a), "b contains a");
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
