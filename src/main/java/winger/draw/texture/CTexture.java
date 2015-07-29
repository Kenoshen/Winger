package com.winger.draw.texture;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.winger.struct.CRectangle;

/**
 * Wrapper class for texture, uses CRectangle for drawing and Rectangle for source. Also allows for texture manager lookup.
 *
 * @author Michael P. Wingfield
 * @copyright Nov 4, 2014
 *
 */
public class CTexture
{
    
    public String name;
    public Rectangle location;
    public Texture texture;
    
    
    public CTexture()
    {
        name = "";
        location = new Rectangle(0, 0, 0, 0);
        texture = null;
    }
    
    
    public CTexture(Texture texture)
    {
        name = "";
        location = new Rectangle(0, 0, texture.getWidth(), texture.getHeight());
        this.texture = texture;
    }
    
    
    public CTexture(TextureRegion texRegion)
    {
        name = "";
        location = new Rectangle(texRegion.getRegionX(), texRegion.getRegionY(), texRegion.getRegionWidth(), texRegion.getRegionHeight());
        this.texture = texRegion.getTexture();
    }
    
    
    public CTexture(String name, Texture texture)
    {
        this.name = name;
        location = new Rectangle(0, 0, texture.getWidth(), texture.getHeight());
        this.texture = texture;
    }
    
    
    public CTexture(Texture texture, Rectangle location)
    {
        name = "";
        this.location = new Rectangle(location.x, location.y, location.width, location.height);
        this.texture = texture;
    }
    
    
    public CTexture(String name, Texture texture, Rectangle location)
    {
        this.name = name;
        this.location = new Rectangle(location.x, location.y, location.width, location.height);
        this.texture = texture;
    }
    
    
    public void draw(CSpriteBatch sb, CRectangle rect)
    {
        drawCorrectly(sb, rect.x, rect.y, rect.width, rect.height, rect.rotation, rect.origin.x, rect.origin.y, (int) location.x, (int) location.y,
            (int) location.width, (int) location.height, false, false, null, 0);
    }
    
    
    public void draw(CSpriteBatch sb, CRectangle rect, Rectangle srcRect, boolean flipX, boolean flipY)
    {
        drawCorrectly(sb, rect.x, rect.y, rect.width, rect.height, rect.rotation, rect.origin.x, rect.origin.y, (int) srcRect.x, (int) srcRect.y,
            (int) srcRect.width, (int) srcRect.height, flipX, flipY, null, 0);
    }
    
    
    public void draw(CSpriteBatch sb, CRectangle rect, Vector2 textureOrigin, Rectangle srcRect, boolean flipX, boolean flipY)
    {
        drawCorrectly(sb, rect.x, rect.y, rect.width, rect.height, rect.rotation, textureOrigin.x, textureOrigin.y, (int) srcRect.x, (int) srcRect.y,
            (int) srcRect.width, (int) srcRect.height, flipX, flipY, null, 0);
    }
    
    
    public void draw(CSpriteBatch sb, Rectangle rect)
    {
        drawCorrectly(sb, rect.x, rect.y, rect.width, rect.height, 0, 0, 0, (int) location.x, (int) location.y, (int) location.width,
            (int) location.height, false, false, null, 0);
    }
    
    
    public void draw(CSpriteBatch sb, Rectangle rect, Vector2 textureOrigin, Rectangle srcRect, boolean flipX, boolean flipY)
    {
        drawCorrectly(sb, rect.x, rect.y, rect.width, rect.height, 0, textureOrigin.x, textureOrigin.y, (int) srcRect.x, (int) srcRect.y,
            (int) srcRect.width, (int) srcRect.height, flipX, flipY, null, 0);
    }
    
    
    public void draw(CSpriteBatch sb, Vector2 pos)
    {
        drawCorrectly(sb, pos.x, pos.y, location.width, location.height, 0, 0, 0, (int) location.x, (int) location.y, (int) location.width,
            (int) location.height, false, false, null, 0);
    }
    
    
    public void draw(CSpriteBatch sb, Vector2 pos, float width, float height)
    {
        drawCorrectly(sb, pos.x, pos.y, width, height, 0, 0, 0, (int) location.x, (int) location.y, (int) location.width, (int) location.height,
            false, false, null, 0);
    }
    
    
    public void draw(CSpriteBatch sb, float x, float y)
    {
        drawCorrectly(sb, x, y, location.width, location.height, 0, 0, 0, (int) location.x, (int) location.y, (int) location.width,
            (int) location.height, false, false, null, 0);
    }
    
    
    public void draw(CSpriteBatch sb, float x, float y, float width, float height)
    {
        drawCorrectly(sb, x, y, width, height, 0, 0, 0, (int) location.x, (int) location.y, (int) location.width, (int) location.height, false,
            false, null, 0);
    }
    
    
    public void draw(CSpriteBatch sb, float x, float y, float width, float height, float rotation)
    {
        drawCorrectly(sb, x, y, width, height, rotation, 0, 0, (int) location.x, (int) location.y, (int) location.width, (int) location.height,
            false, false, null, 0);
    }
    
    
    public void draw(CSpriteBatch sb, float x, float y, float width, float height, float rotation, float originx, float originy)
    {
        drawCorrectly(sb, x, y, width, height, rotation, originx, originy, (int) location.x, (int) location.y, (int) location.width,
            (int) location.height, false, false, null, 0);
    }
    
    
    public void draw(CSpriteBatch sb, float x, float y, float width, float height, float rotation, float originx, float originy, int srcX, int srcY,
        int srcWidth, int srcHeight, boolean flipX, boolean flipY)
    {
        drawCorrectly(sb, x, y, width, height, rotation, originx, originy, srcX, srcY, srcWidth, srcHeight, flipX, flipY, null, 0);
    }
    
    
    public void draw(CSpriteBatch sb, CRectangle rect, Color color)
    {
        drawCorrectly(sb, rect.x, rect.y, rect.width, rect.height, rect.rotation, rect.origin.x, rect.origin.y, (int) location.x, (int) location.y,
            (int) location.width, (int) location.height, false, false, color, 0);
    }
    
    
    public void draw(CSpriteBatch sb, CRectangle rect, Rectangle srcRect, boolean flipX, boolean flipY, Color color)
    {
        drawCorrectly(sb, rect.x, rect.y, rect.width, rect.height, rect.rotation, rect.origin.x, rect.origin.y, (int) srcRect.x, (int) srcRect.y,
            (int) srcRect.width, (int) srcRect.height, flipX, flipY, color, 0);
    }
    
    
    public void draw(CSpriteBatch sb, CRectangle rect, Vector2 textureOrigin, Rectangle srcRect, boolean flipX, boolean flipY, Color color)
    {
        drawCorrectly(sb, rect.x, rect.y, rect.width, rect.height, rect.rotation, textureOrigin.x, textureOrigin.y, (int) srcRect.x, (int) srcRect.y,
            (int) srcRect.width, (int) srcRect.height, flipX, flipY, color, 0);
    }
    
    
    public void draw(CSpriteBatch sb, Rectangle rect, Color color)
    {
        drawCorrectly(sb, rect.x, rect.y, rect.width, rect.height, 0, 0, 0, (int) location.x, (int) location.y, (int) location.width,
            (int) location.height, false, false, color, 0);
    }
    
    
    public void draw(CSpriteBatch sb, Rectangle rect, Vector2 textureOrigin, Rectangle srcRect, boolean flipX, boolean flipY, Color color)
    {
        drawCorrectly(sb, rect.x, rect.y, rect.width, rect.height, 0, textureOrigin.x, textureOrigin.y, (int) srcRect.x, (int) srcRect.y,
            (int) srcRect.width, (int) srcRect.height, flipX, flipY, color, 0);
    }
    
    
    public void draw(CSpriteBatch sb, Vector2 pos, Color color)
    {
        drawCorrectly(sb, pos.x, pos.y, location.width, location.height, 0, 0, 0, (int) location.x, (int) location.y, (int) location.width,
            (int) location.height, false, false, color, 0);
    }
    
    
    public void draw(CSpriteBatch sb, Vector2 pos, float width, float height, Color color)
    {
        drawCorrectly(sb, pos.x, pos.y, width, height, 0, 0, 0, (int) location.x, (int) location.y, (int) location.width, (int) location.height,
            false, false, color, 0);
    }
    
    
    public void draw(CSpriteBatch sb, float x, float y, Color color)
    {
        drawCorrectly(sb, x, y, location.width, location.height, 0, 0, 0, (int) location.x, (int) location.y, (int) location.width,
            (int) location.height, false, false, color, 0);
    }
    
    
    public void draw(CSpriteBatch sb, float x, float y, float width, float height, Color color)
    {
        drawCorrectly(sb, x, y, width, height, 0, 0, 0, (int) location.x, (int) location.y, (int) location.width, (int) location.height, false,
            false, color, 0);
    }
    
    
    public void draw(CSpriteBatch sb, float x, float y, float width, float height, float rotation, Color color)
    {
        drawCorrectly(sb, x, y, width, height, rotation, 0, 0, (int) location.x, (int) location.y, (int) location.width, (int) location.height,
            false, false, color, 0);
    }
    
    
    public void draw(CSpriteBatch sb, float x, float y, float width, float height, float rotation, float originx, float originy, Color color)
    {
        drawCorrectly(sb, x, y, width, height, rotation, originx, originy, (int) location.x, (int) location.y, (int) location.width,
            (int) location.height, false, false, color, 0);
    }
    
    
    public void draw(CSpriteBatch sb, float x, float y, float width, float height, float rotation, float originx, float originy, int srcX, int srcY,
        int srcWidth, int srcHeight, boolean flipX, boolean flipY, Color color)
    {
        drawCorrectly(sb, x, y, width, height, rotation, originx, originy, srcX, srcY, srcWidth, srcHeight, flipX, flipY, color, 0);
    }
    
    
    public void draw(CSpriteBatch sb, CRectangle rect, float zIndex)
    {
        drawCorrectly(sb, rect.x, rect.y, rect.width, rect.height, rect.rotation, rect.origin.x, rect.origin.y, (int) location.x, (int) location.y,
            (int) location.width, (int) location.height, false, false, null, zIndex);
    }
    
    
    public void draw(CSpriteBatch sb, CRectangle rect, Rectangle srcRect, boolean flipX, boolean flipY, float zIndex)
    {
        drawCorrectly(sb, rect.x, rect.y, rect.width, rect.height, rect.rotation, rect.origin.x, rect.origin.y, (int) srcRect.x, (int) srcRect.y,
            (int) srcRect.width, (int) srcRect.height, flipX, flipY, null, zIndex);
    }
    
    
    public void draw(CSpriteBatch sb, CRectangle rect, Vector2 textureOrigin, Rectangle srcRect, boolean flipX, boolean flipY, float zIndex)
    {
        drawCorrectly(sb, rect.x, rect.y, rect.width, rect.height, rect.rotation, textureOrigin.x, textureOrigin.y, (int) srcRect.x, (int) srcRect.y,
            (int) srcRect.width, (int) srcRect.height, flipX, flipY, null, zIndex);
    }
    
    
    public void draw(CSpriteBatch sb, Rectangle rect, float zIndex)
    {
        drawCorrectly(sb, rect.x, rect.y, rect.width, rect.height, 0, 0, 0, (int) location.x, (int) location.y, (int) location.width,
            (int) location.height, false, false, null, zIndex);
    }
    
    
    public void draw(CSpriteBatch sb, Rectangle rect, Vector2 textureOrigin, Rectangle srcRect, boolean flipX, boolean flipY, float zIndex)
    {
        drawCorrectly(sb, rect.x, rect.y, rect.width, rect.height, 0, textureOrigin.x, textureOrigin.y, (int) srcRect.x, (int) srcRect.y,
            (int) srcRect.width, (int) srcRect.height, flipX, flipY, null, zIndex);
    }
    
    
    public void draw(CSpriteBatch sb, Vector2 pos, float zIndex)
    {
        drawCorrectly(sb, pos.x, pos.y, location.width, location.height, 0, 0, 0, (int) location.x, (int) location.y, (int) location.width,
            (int) location.height, false, false, null, zIndex);
    }
    
    
    public void draw(CSpriteBatch sb, Vector2 pos, float width, float height, float zIndex)
    {
        drawCorrectly(sb, pos.x, pos.y, width, height, 0, 0, 0, (int) location.x, (int) location.y, (int) location.width, (int) location.height,
            false, false, null, zIndex);
    }
    
    
    public void draw(CSpriteBatch sb, float x, float y, float zIndex)
    {
        drawCorrectly(sb, x, y, location.width, location.height, 0, 0, 0, (int) location.x, (int) location.y, (int) location.width,
            (int) location.height, false, false, null, zIndex);
    }
    
    
    public void draw(CSpriteBatch sb, float x, float y, float width, float height, float rotation, float zIndex)
    {
        drawCorrectly(sb, x, y, width, height, rotation, 0, 0, (int) location.x, (int) location.y, (int) location.width, (int) location.height,
            false, false, null, zIndex);
    }
    
    
    public void draw(CSpriteBatch sb, float x, float y, float width, float height, float rotation, float originx, float originy, float zIndex)
    {
        drawCorrectly(sb, x, y, width, height, rotation, originx, originy, (int) location.x, (int) location.y, (int) location.width,
            (int) location.height, false, false, null, zIndex);
    }
    
    
    public void draw(CSpriteBatch sb, float x, float y, float width, float height, float rotation, float originx, float originy, int srcX, int srcY,
        int srcWidth, int srcHeight, boolean flipX, boolean flipY, float zIndex)
    {
        drawCorrectly(sb, x, y, width, height, rotation, originx, originy, srcX, srcY, srcWidth, srcHeight, flipX, flipY, null, zIndex);
    }
    
    
    public void draw(CSpriteBatch sb, CRectangle rect, Color color, float zIndex)
    {
        drawCorrectly(sb, rect.x, rect.y, rect.width, rect.height, rect.rotation, rect.origin.x, rect.origin.y, (int) location.x, (int) location.y,
            (int) location.width, (int) location.height, false, false, color, zIndex);
    }
    
    
    public void draw(CSpriteBatch sb, CRectangle rect, Rectangle srcRect, boolean flipX, boolean flipY, Color color, float zIndex)
    {
        drawCorrectly(sb, rect.x, rect.y, rect.width, rect.height, rect.rotation, rect.origin.x, rect.origin.y, (int) srcRect.x, (int) srcRect.y,
            (int) srcRect.width, (int) srcRect.height, flipX, flipY, color, zIndex);
    }
    
    
    public void draw(CSpriteBatch sb, CRectangle rect, Vector2 textureOrigin, Rectangle srcRect, boolean flipX, boolean flipY, Color color,
        float zIndex)
    {
        drawCorrectly(sb, rect.x, rect.y, rect.width, rect.height, rect.rotation, textureOrigin.x, textureOrigin.y, (int) srcRect.x, (int) srcRect.y,
            (int) srcRect.width, (int) srcRect.height, flipX, flipY, color, zIndex);
    }
    
    
    public void draw(CSpriteBatch sb, Rectangle rect, Color color, float zIndex)
    {
        drawCorrectly(sb, rect.x, rect.y, rect.width, rect.height, 0, 0, 0, (int) location.x, (int) location.y, (int) location.width,
            (int) location.height, false, false, color, zIndex);
    }
    
    
    public void draw(CSpriteBatch sb, Rectangle rect, Vector2 textureOrigin, Rectangle srcRect, boolean flipX, boolean flipY, Color color,
        float zIndex)
    {
        drawCorrectly(sb, rect.x, rect.y, rect.width, rect.height, 0, textureOrigin.x, textureOrigin.y, (int) srcRect.x, (int) srcRect.y,
            (int) srcRect.width, (int) srcRect.height, flipX, flipY, color, zIndex);
    }
    
    
    public void draw(CSpriteBatch sb, Vector2 pos, Color color, float zIndex)
    {
        drawCorrectly(sb, pos.x, pos.y, location.width, location.height, 0, 0, 0, (int) location.x, (int) location.y, (int) location.width,
            (int) location.height, false, false, color, zIndex);
    }
    
    
    public void draw(CSpriteBatch sb, Vector2 pos, float width, float height, Color color, float zIndex)
    {
        drawCorrectly(sb, pos.x, pos.y, width, height, 0, 0, 0, (int) location.x, (int) location.y, (int) location.width, (int) location.height,
            false, false, color, zIndex);
    }
    
    
    public void draw(CSpriteBatch sb, float x, float y, Color color, float zIndex)
    {
        drawCorrectly(sb, x, y, location.width, location.height, 0, 0, 0, (int) location.x, (int) location.y, (int) location.width,
            (int) location.height, false, false, color, zIndex);
    }
    
    
    public void draw(CSpriteBatch sb, float x, float y, float width, float height, Color color, float zIndex)
    {
        drawCorrectly(sb, x, y, width, height, 0, 0, 0, (int) location.x, (int) location.y, (int) location.width, (int) location.height, false,
            false, color, zIndex);
    }
    
    
    public void draw(CSpriteBatch sb, float x, float y, float width, float height, float rotation, Color color, float zIndex)
    {
        drawCorrectly(sb, x, y, width, height, rotation, 0, 0, (int) location.x, (int) location.y, (int) location.width, (int) location.height,
            false, false, color, zIndex);
    }
    
    
    public void draw(CSpriteBatch sb, float x, float y, float width, float height, float rotation, float originx, float originy, Color color,
        float zIndex)
    {
        drawCorrectly(sb, x, y, width, height, rotation, originx, originy, (int) location.x, (int) location.y, (int) location.width,
            (int) location.height, false, false, color, zIndex);
    }
    
    
    public void draw(CSpriteBatch sb, float x, float y, float width, float height, float rotation, float originx, float originy, int srcX, int srcY,
        int srcWidth, int srcHeight, boolean flipX, boolean flipY, Color color, float zIndex)
    {
        drawCorrectly(sb, x, y, width, height, rotation, originx, originy, srcX, srcY, srcWidth, srcHeight, flipX, flipY, color, zIndex);
    }
    
    
    private void drawCorrectly(CSpriteBatch sb, float x, float y, float width, float height, float rotation, float originx, float originy, int srcX,
        int srcY, int srcWidth, int srcHeight, boolean flipX, boolean flipY, Color color, float zIndex)
    {
        sb.draw(texture, color, x - originx, y - originy, originx, originy, width, height, 1, 1, rotation, srcX, srcY, srcWidth, srcHeight, flipX,
            flipY, zIndex);
    }
    
    
    @Override
    public String toString()
    {
        return "{\"name\":\"" + name + "\",\"x\":" + location.x + ",\"y\":" + location.y + ",\"width\":" + location.width + ",\"height\":"
            + location.height + "}";
    }
}
