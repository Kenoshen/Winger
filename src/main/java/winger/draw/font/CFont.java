package com.winger.draw.font;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.winger.draw.texture.CSpriteBatch;

/**
 * Wrapper class for fonts to allow access by name
 *
 * @author Michael P. Wingfield
 * @copyright Nov 4, 2014
 *
 */
public class CFont extends BitmapFont
{
    public String name = "";
    
    
    public CFont(String name)
    {
        super();
        this.name = name;
    }
    
    
    public CFont(String name, BitmapFontData data, Array<TextureRegion> regions, boolean integer)
    {
        super(data, regions, integer);
        this.name = name;
    }
    
    
    public CFont(String name, BitmapFontData data, TextureRegion region, boolean integer)
    {
        super(data, region, integer);
        this.name = name;
    }
    
    
    public CFont(String name, boolean flip)
    {
        super(flip);
        this.name = name;
    }
    
    
    public CFont(String name, FileHandle fontFile)
    {
        super(fontFile);
        this.name = name;
    }
    
    
    public CFont(String name, FileHandle fontFile, boolean flip)
    {
        super(fontFile, flip);
        this.name = name;
    }
    
    
    public CFont(String name, FileHandle fontFile, FileHandle imageFile, boolean flip)
    {
        super(fontFile, imageFile, flip);
        this.name = name;
    }
    
    
    public CFont(String name, FileHandle fontFile, FileHandle imageFile, boolean flip, boolean integer)
    {
        super(fontFile, imageFile, flip, integer);
        this.name = name;
    }
    
    
    public CFont(String name, FileHandle fontFile, TextureRegion region)
    {
        super(fontFile, region);
        this.name = name;
    }
    
    
    public CFont(String name, FileHandle fontFile, TextureRegion region, boolean flip)
    {
        super(fontFile, region, flip);
        this.name = name;
    }
    
    
    public void draw(CSpriteBatch batch, CharSequence str, float x, float y, Color color)
    {
        batch.drawText(this, str, x, y, color);
    }
    
    
    public void draw(CSpriteBatch batch, CharSequence str, float x, float y, int start, int end, Color color)
    {
        batch.drawText(this, str, x, y, start, end, color);
    }
    
    
    public void drawMultiLine(CSpriteBatch batch, CharSequence str, float x, float y, Color color)
    {
        batch.drawTextMultiLine(this, str, x, y, color);
    }
    
    
    public void drawMultiLine(CSpriteBatch batch, CharSequence str, float x, float y, float alignmentWidth, int alignment, Color color)
    {
        batch.drawTextMultiLine(this, str, x, y, alignmentWidth, alignment, color);
    }
    
    
    public void drawWrapped(CSpriteBatch batch, CharSequence str, float x, float y, float wrapWidth, Color color)
    {
        batch.drawTextWrapped(this, str, x, y, wrapWidth, color);
    }
    
    
    public void drawWrapped(CSpriteBatch batch, CharSequence str, float x, float y, float wrapWidth, int alignment, Color color)
    {
        batch.drawTextWrapped(this, str, x, y, wrapWidth, alignment, color);
    }
    
    
    public void draw(CSpriteBatch batch, CharSequence str, float x, float y, Color color, float zIndex)
    {
        batch.drawText(this, str, x, y, color, zIndex);
    }
    
    
    public void draw(CSpriteBatch batch, CharSequence str, float x, float y, int start, int end, Color color, float zIndex)
    {
        batch.drawText(this, str, x, y, start, end, color, zIndex);
    }
    
    
    public void drawMultiLine(CSpriteBatch batch, CharSequence str, float x, float y, Color color, float zIndex)
    {
        batch.drawTextMultiLine(this, str, x, y, color, zIndex);
    }
    
    
    public void drawMultiLine(CSpriteBatch batch, CharSequence str, float x, float y, float alignmentWidth, int alignment, Color color, float zIndex)
    {
        batch.drawTextMultiLine(this, str, x, y, alignmentWidth, alignment, color, zIndex);
    }
    
    
    public void drawWrapped(CSpriteBatch batch, CharSequence str, float x, float y, float wrapWidth, Color color, float zIndex)
    {
        batch.drawTextWrapped(this, str, x, y, wrapWidth, color, zIndex);
    }
    
    
    public void drawWrapped(CSpriteBatch batch, CharSequence str, float x, float y, float wrapWidth, int alignment, Color color, float zIndex)
    {
        batch.drawTextWrapped(this, str, x, y, wrapWidth, alignment, color, zIndex);
    }
}
