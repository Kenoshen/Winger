package com.winger.physics.body;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.winger.physics.CBody;

import java.util.ArrayList;
import java.util.List;

public class ChainBody extends CBody<ChainBody>
{
    private List<Vector2> points;
    private ChainShape shape;
    
    
    public ChainBody(Vector2... points)
    {
        super();
        
        this.points = new ArrayList<Vector2>();
        for (Vector2 point : points)
        {
            this.points.add(point.cpy());
        }
    }
    
    
    public ChainBody(float... points)
    {
        if (points.length % 2 != 0)
        {
            throw new RuntimeException("Must supply an even number of points to the ChainBody constructor");
        }
        this.points = new ArrayList<Vector2>();
        for (int i = 0; i < points.length; i += 2)
        {
            this.points.add(new Vector2(points[i], points[i + 1]));
        }
    }
    
    
    @Override
    public ChainBody init(FixtureDef fixtureDef, BodyDef bodyDef)
    {
        super.init(fixtureDef, bodyDef);
        
        shape = new ChainShape();
        shape.createChain(points.toArray(new Vector2[points.size()]));
        
        addShape(shape);
        
        return this;
    }
    
    
    public Vector2 getPoint(int index)
    {
        if (index < points.size())
        {
            return points.get(index).cpy();
        }
        return null;
    }
    
    
    public int size()
    {
        return points.size();
    }
}
