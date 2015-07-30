package com.winger.stats;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.winger.draw.font.CFont;
import com.winger.draw.font.FontManager;
import com.winger.draw.texture.CSpriteBatch;

import java.util.ArrayList;
import java.util.List;

/**
 * Helpful for tracking FPS in a given application
 *
 * @author Michael P. Wingfield
 * @copyright Nov 4, 2014
 *
 */
public class FPSCalculator
{
    private CFont font;
    private Color color;
    private List<Long> frames;
    
    
    public FPSCalculator()
    {
        init(30);
    }
    
    
    public FPSCalculator(int frameCount)
    {
        init(frameCount);
    }
    
    
    private void init(int frameCount)
    {
        framesToTrack(frameCount);
        color(Color.BLACK.cpy());
        font = FontManager.instance().getDefaultFont();
    }
    
    
    /**
     * Sets the number of frames to use when determining the FPS. Using a lower number is more jittery, using a higher number is smoother. Defaults to
     * 30.
     * 
     * @param frameCount
     */
    public void framesToTrack(int frameCount)
    {
        if (frameCount < 5)
        {
            frameCount = 5;
        }
        frames = new ArrayList<Long>();
        for (int i = 0; i < frameCount; i++)
        {
            frames.add(0L);
        }
    }
    
    
    /**
     * Gets the color of the font
     * 
     * @return
     */
    public Color color()
    {
        return color;
    }
    
    
    /**
     * Sets the color of the font
     * 
     * @param color
     */
    public void color(Color color)
    {
        this.color = color;
    }
    
    
    /**
     * Call this at the end of your drawing cycle to calculate fps
     */
    public void update()
    {
        frames.add(0, System.currentTimeMillis());
        frames.remove(frames.size() - 1);
    }
    
    
    /**
     * Gets the current fps calculation
     * 
     * @return
     */
    public float fps()
    {
        int mpf = msecPerFrame();
        if (mpf != 0)
        {
            return 1000 / mpf;
        }
        return 0;
    }
    
    
    /**
     * Gets the milliseconds / frame calculation
     * 
     * @return
     */
    public int msecPerFrame()
    {
        long sum = 0;
        for (int i = 0; i + 1 < frames.size(); i++)
        {
            sum += frames.get(i) - frames.get(i + 1);
        }
        return (int) (sum / (frames.size() - 1));
    }
    
    
    /**
     * Convenience method that prints the fps calculation to System.out
     */
    public void printFps()
    {
        System.out.println(fps());
    }
    
    
    /**
     * Convenience method that prints the fps calculation to System.out with the given prefix
     * 
     * @param prefix
     */
    public void printFps(String prefix)
    {
        System.out.println(prefix + fps());
    }
    
    
    /**
     * Displays the fps counter at (0, 0)
     * 
     * @param sb
     */
    public void displayFps(CSpriteBatch sb)
    {
        displayFps(sb, 0, 0);
    }
    
    
    /**
     * Displays the fps counter at the given position
     * 
     * @param sb
     * @param pos
     */
    public void displayFps(CSpriteBatch sb, Vector2 pos)
    {
        displayFps(sb, pos.x, pos.y);
    }
    
    
    /**
     * Displays the fps counter at the given position
     * 
     * @param sb
     * @param x
     * @param y
     */
    public void displayFps(CSpriteBatch sb, float x, float y)
    {
        sb.drawText(font, "" + fps(), x, y, color);
    }
}
