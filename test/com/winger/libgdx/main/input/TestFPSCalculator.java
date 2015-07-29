package com.winger.libgdx.main.input;

import com.winger.draw.texture.CSprite;
import com.winger.draw.texture.CSpriteBatch;
import com.winger.input.raw.CKeyboard;
import com.winger.ui.Element;
import com.winger.ui.Page;

public class TestFPSCalculator
{
    static final long maxIterations = 20000000L;
    long add = 100000L;
    long iterations = 0L;
    CKeyboard keyboard;
    Page ui;
    Element<?> elem;
    CSprite sprite;
    CSpriteBatch sb = new CSpriteBatch();
    
    
    public TestFPSCalculator(Page ui)
    {
        this.ui = ui;
        keyboard = ui.keyboard;
        elem = ui.getElementById("display");
        
        sprite = new CSprite("green.run", ui.getElementById("anim").getAbsoluteBoundingBox(), true);
        sprite.setMillisecondsPerFrame(1000).setShouldLoop(true);
    }
    
    
    public void update()
    {
        if (ui.isEnabled)
        {
            @SuppressWarnings("unused")
            float x = 0;
            for (long i = -iterations; i < iterations; i++)
            {
                x *= i;
            }
            
            iterations += add;
            
            if (iterations <= 0 || iterations >= maxIterations)
            {
                add *= -1;
            }
            
            elem.text("Iterations: " + iterations);
            
            sprite.update(1);
        }
    }
    
    
    public void draw()
    {
        if (ui.isVisible)
        {
            sb.begin();
            sprite.draw(sb);
            sb.end();
        }
    }
}
