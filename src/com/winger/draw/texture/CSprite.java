package com.winger.draw.texture;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.winger.struct.CRectangle;
import com.winger.utils.RandomUtils;

public class CSprite
{
    private CRectangle rect;
    private float zIndex = 0;
    private Color color = Color.WHITE.cpy();
    private List<CTexture> textures;
    private int index = 0;
    private boolean isAnimation = false;
    private boolean isAnimating = true;
    private boolean isReverse = false;
    private boolean isRandomAnimation = false;
    private long mSecPerFrame = 1;
    private long rndMinMSecPerFrame = 1;
    private long rndMaxMSecPerFrame = 500;
    private float timer = 0;
    private long currentTimeStep = 0;
    private long lastTimeStep = System.currentTimeMillis();
    private boolean shouldLoop = false;
    private boolean isFlipped = false;
    private boolean isFlopped = false;
    private boolean isFinished = false;
    // private boolean isTiled = false;
    // private Vector2 tileSize = new Vector2(0, 0);
    // private List<Tup3<CRectangle, Rectangle, CTexture>> tiles = null;
    private boolean isDebug = false;
    
    
    public CSprite(CTexture texture, CRectangle rect)
    {
        textures = new ArrayList<CTexture>();
        textures.add(texture);
        this.rect = rect;
    }
    
    
    public CSprite(List<CTexture> textures, CRectangle rect)
    {
        this.textures = textures;
        this.rect = rect;
    }
    
    
    public CSprite(String textureName, CRectangle rect, boolean isAnimation)
    {
        this.isAnimation = isAnimation;
        if (isAnimation)
        {
            textures = TextureManager.instance().getTextureGroup(textureName);
        } else
        {
            textures = new ArrayList<CTexture>();
            CTexture tex = TextureManager.instance().getTexture(textureName);
            if (tex != null)
            {
                textures.add(tex);
            }
        }
        this.rect = rect;
    }
    
    
    public void resetAnimation()
    {
        resetTimer();
        refreshCurrentTimeStep();
        lastTimeStep = System.currentTimeMillis();
        if (isReverse)
            index = textures.size() - 1;
        else
            index = 0;
        isAnimating = true;
        isFinished = false;
    }
    
    
    public void resetTimer()
    {
        timer = 0;
    }
    
    
    public void refreshCurrentTimeStep()
    {
        currentTimeStep = System.currentTimeMillis();
    }
    
    
    public void update(float delta)
    {
        if (isAnimation && isAnimating)
        {
            timer += delta;
            if ((long) (timer * 1000) > mSecPerFrame)
            {
                timer = 0;
                if (isRandomAnimation)
                {
                    mSecPerFrame = RandomUtils.randLong(rndMinMSecPerFrame, rndMaxMSecPerFrame);
                }
                if (isReverse)
                {
                    index--;
                    if (index < 0)
                    {
                        if (shouldLoop)
                        {
                            index = textures.size() - 1;
                        } else
                        {
                            index = 0;
                            isAnimating = false;
                            isFinished = true;
                        }
                    }
                } else
                {
                    index++;
                    if (index >= textures.size())
                    {
                        if (shouldLoop)
                        {
                            index = 0;
                        } else
                        {
                            index = textures.size() - 1;
                            isAnimating = false;
                            isFinished = true;
                        }
                    }
                }
                
            }
        }
    }
    
    
    public void update()
    {
        if (isAnimation && isAnimating)
        {
            refreshCurrentTimeStep();
            float delta = (float) (currentTimeStep - lastTimeStep) / 1000f;
            update(delta);
            lastTimeStep = currentTimeStep;
        }
    }
    
    
    public void draw(CSpriteBatch sb)
    {
        if (textures != null && textures.size() > 0)
        {
            // if (isTiled() && tileSize.x > 0 && tileSize.y > 0)
            // {
            // for (Tup3<CRectangle, Rectangle, CTexture> tile : tiles)
            // {
            // tile.i3().draw(sb, tile.i1(), tile.i2(), isFlipped(), isFlopped(), color, zIndex);
            // if (isDebug)
            // {
            // sb.drawRectangle(ShapeType.Line, tile.i1(), Color.CYAN);
            // sb.drawCircle(ShapeType.Filled, tile.i1().position(), 5, Color.CYAN);
            // }
            // }
            // } else
            // {
            CTexture tex = textures.get(index);
            tex.draw(sb, rect, tex.location, isFlipped(), isFlopped(), color, zIndex);
            if (isDebug)
            {
                sb.drawRectangle(ShapeType.Line, rect, Color.CYAN);
                sb.drawCircle(ShapeType.Filled, rect.position(), 5, Color.CYAN);
            }
            // }
        }
    }
    
    
    // private void updateTiles()
    // {
    // for (Tup3<CRectangle, Rectangle, CTexture> tile : tiles)
    // {
    // tile.i1().x = rect.x;
    // tile.i1().y = rect.y;
    // tile.i1().rotation = rect.rotation;
    // }
    // }
    
    // private void tilesAreNowDirty()
    // {
    // tiles = new ArrayList<Tup3<CRectangle, Rectangle, CTexture>>();
    // CTexture tex = textures.get(index);
    // Vector2 origin = rect.originPercentage();
    // CRectangle tileRect = new CRectangle(rect.x, rect.y, tileSize.x, tileSize.y);
    // calculateTiles(new Vector2(1, 1), origin, tileRect, tex);
    // calculateTiles(new Vector2(1, -1), origin, tileRect, tex);
    // calculateTiles(new Vector2(-1, -1), origin, tileRect, tex);
    // calculateTiles(new Vector2(-1, 1), origin, tileRect, tex);
    // updateTiles();
    // }
    
    // private void calculateTiles(Vector2 axis, Vector2 origin, CRectangle tileRect, CTexture tex)
    // {
    // if (tileSize.x <= 0 || tileSize.y <= 0)
    // {
    // return;
    // }
    // if (axis.x > 0 && origin.x > 0.5f)
    // {
    // return;
    // } else if (axis.x < 0 && origin.x < 0.5f)
    // {
    // return;
    // } else if (axis.y > 0 && origin.y > 0.5f)
    // {
    // return;
    // } else if (axis.y < 0 && origin.y < 0.5f)
    // {
    // return;
    // }
    //
    // float spaceWidth = (origin.x > 0 && origin.x < 1 ? rect.width * 0.5f : rect.width);
    // int fullTimesWidth = (int) (spaceWidth / tileRect.width);
    // float remainderWidth = spaceWidth - (fullTimesWidth * tileRect.width);
    // float spaceHeight = (origin.y > 0 && origin.y < 1 ? rect.height * 0.5f : rect.height);
    // int fullTimesHeight = (int) (spaceHeight / tileRect.height);
    // float remainderHeight = spaceHeight - (fullTimesHeight * tileRect.height);
    //
    // CRectangle remainderRect = null;
    // Rectangle remainderSource = null;
    // float widthConversion = 0;
    // float heightConversion = 0;
    // if (remainderWidth > 0 || remainderHeight > 0)
    // {
    // remainderRect = tileRect.clone();
    // remainderSource = new Rectangle(0, 0, tileRect.width, tileRect.height);
    // widthConversion = tex.location.width / tileRect.width;
    // heightConversion = tex.location.height / tileRect.height;
    // }
    //
    // for (int x = 0; x > -fullTimesWidth; x--)
    // {
    // for (int y = 0; y > -fullTimesHeight; y--)
    // {
    // tileRect.origin.x = x * tileRect.width;
    // if (axis.x < 0)
    // {
    // tileRect.origin.x *= -1;
    // tileRect.origin.x += tileRect.width;
    // }
    // tileRect.origin.y = y * tileRect.height;
    // if (axis.y < 0)
    // {
    // tileRect.origin.y *= -1;
    // tileRect.origin.y += tileRect.height;
    // }
    // tiles.add(Tups.tup3(tileRect.clone(), new Rectangle(tex.location), tex));
    //
    // if (remainderWidth > 0)
    // {
    // remainderRect.width = remainderWidth;
    // remainderRect.height = tileRect.height;
    // remainderSource.x = 0;
    // remainderSource.y = 0;
    // remainderSource.width = remainderRect.width;
    // remainderSource.height = remainderRect.height;
    // //
    // remainderRect.origin.x = -(fullTimesWidth) * tileRect.width;
    // if (axis.x < 0)
    // {
    // remainderRect.origin.x *= -1;
    // remainderRect.origin.x += remainderWidth;
    // remainderSource.x = tileRect.width - remainderRect.width;
    // }
    // //
    // remainderRect.origin.y = y * tileRect.height;
    // remainderSource.y = tileRect.height - remainderRect.height;
    // if (axis.y < 0)
    // {
    // remainderRect.origin.y *= -1;
    // remainderRect.origin.y += tileRect.height;
    // remainderSource.y = 0;
    // }
    // //
    // remainderSource.x *= widthConversion;
    // remainderSource.x += tex.location.x;
    // remainderSource.y *= heightConversion;
    // remainderSource.y += tex.location.y;
    // remainderSource.width *= widthConversion;
    // remainderSource.height *= heightConversion;
    // //
    // tiles.add(Tups.tup3(remainderRect.clone(), new Rectangle(remainderSource), tex));
    // }
    // }
    //
    // if (remainderHeight > 0)
    // {
    // remainderRect.width = tileRect.width;
    // remainderRect.height = remainderHeight;
    // remainderSource.x = 0;
    // remainderSource.y = 0;
    // remainderSource.width = remainderRect.width;
    // remainderSource.height = remainderRect.height;
    // //
    // remainderRect.origin.x = x * tileRect.width;
    // if (axis.x < 0)
    // {
    // remainderRect.origin.x *= -1;
    // remainderRect.origin.x += tileRect.width;
    // remainderSource.x = tileRect.width - remainderRect.width;
    // }
    // //
    // remainderRect.origin.y = -(fullTimesHeight) * tileRect.height;
    // remainderSource.y = tileRect.height - remainderRect.height;
    // if (axis.y < 0)
    // {
    // remainderRect.origin.y *= -1;
    // remainderRect.origin.y += remainderHeight;
    // remainderSource.y = 0;
    // }
    // //
    // remainderSource.x *= widthConversion;
    // remainderSource.x += tex.location.x;
    // remainderSource.y *= heightConversion;
    // remainderSource.y += tex.location.y;
    // remainderSource.width *= widthConversion;
    // remainderSource.height *= heightConversion;
    // //
    // tiles.add(Tups.tup3(remainderRect.clone(), new Rectangle(remainderSource), tex));
    // }
    // }
    // if (remainderHeight > 0 && remainderWidth > 0)
    // {
    // remainderRect.width = remainderWidth;
    // remainderRect.height = remainderHeight;
    // remainderSource.x = 0;
    // remainderSource.y = 0;
    // remainderSource.width = remainderRect.width;
    // remainderSource.height = remainderRect.height;
    // //
    // remainderRect.origin.x = -(fullTimesWidth) * tileRect.width;
    // if (axis.x < 0)
    // {
    // remainderRect.origin.x *= -1;
    // remainderRect.origin.x += remainderWidth;
    // remainderSource.x = tileRect.width - remainderRect.width;
    // }
    // //
    // remainderRect.origin.y = -(fullTimesHeight) * tileRect.height;
    // remainderSource.y = tileRect.height - remainderRect.height;
    // if (axis.y < 0)
    // {
    // remainderRect.origin.y *= -1;
    // remainderRect.origin.y += remainderHeight;
    // remainderSource.y = 0;
    // }
    // //
    // remainderSource.x *= widthConversion;
    // remainderSource.x += tex.location.x;
    // remainderSource.y *= heightConversion;
    // remainderSource.y += tex.location.y;
    // remainderSource.width *= widthConversion;
    // remainderSource.height *= heightConversion;
    // //
    // tiles.add(Tups.tup3(remainderRect.clone(), new Rectangle(remainderSource), tex));
    // }
    // }
    
    // /////////////////////////////////////////////
    // Getters and Setters
    // /////////////////////////////////////////////
    public CSprite addTexture(CTexture texture)
    {
        textures.add(texture);
        return this;
    }
    
    
    public CSprite addTextures(List<CTexture> textures)
    {
        this.textures.addAll(textures);
        return this;
    }
    
    
    public CTexture getTexture(int index)
    {
        if (index < 0 || index >= textures.size())
            return null;
        return textures.get(index);
    }
    
    
    public CRectangle getRect()
    {
        return rect.clone();
    }
    
    
    public CSprite setRect(CRectangle rect)
    {
        if (rect != null)
        {
            this.rect = rect.clone();
            // tilesAreNowDirty();
        }
        return this;
    }
    
    
    public float getX()
    {
        return rect.x;
    }
    
    
    public CSprite setX(float x)
    {
        rect.x = x;
        // if (isTiled)
        // {
        // updateTiles();
        // }
        return this;
    }
    
    
    public float getY()
    {
        return rect.y;
    }
    
    
    public CSprite setY(float y)
    {
        rect.y = y;
        // if (isTiled)
        // {
        // updateTiles();
        // }
        return this;
    }
    
    
    public Vector2 getPosition()
    {
        return new Vector2(rect.x, rect.y);
    }
    
    
    public CSprite setPosition(Vector2 pos)
    {
        setX(pos.x);
        setY(pos.y);
        return this;
    }
    
    
    public float getWidth()
    {
        return rect.width;
    }
    
    
    public CSprite setWidth(float width)
    {
        rect.width = width;
        // if (isTiled)
        // {
        // tilesAreNowDirty();
        // }
        return this;
    }
    
    
    public float getHeight()
    {
        return rect.height;
    }
    
    
    public CSprite setHeight(float height)
    {
        rect.height = height;
        // if (isTiled)
        // {
        // tilesAreNowDirty();
        // }
        return this;
    }
    
    
    public float getRotation()
    {
        return rect.rotation;
    }
    
    
    public CSprite setRotation(float rotation)
    {
        rect.rotation = rotation;
        // if (isTiled)
        // {
        // updateTiles();
        // }
        return this;
    }
    
    
    public float getOriginX()
    {
        return rect.origin.x;
    }
    
    
    public CSprite setOriginX(float originX)
    {
        rect.origin.x = originX;
        // tilesAreNowDirty();
        return this;
    }
    
    
    public float getOriginY()
    {
        return rect.origin.y;
    }
    
    
    public CSprite setOriginY(float originY)
    {
        rect.origin.y = originY;
        // tilesAreNowDirty();
        return this;
    }
    
    
    public Vector2 getOrigin()
    {
        return rect.origin.cpy();
    }
    
    
    public CSprite setOrigin(Vector2 origin)
    {
        setOriginX(origin.x);
        setOriginY(origin.y);
        return this;
    }
    
    
    public float getZ()
    {
        return zIndex;
    }
    
    
    public CSprite setZ(float z)
    {
        this.zIndex = z;
        return this;
    }
    
    
    public Color getColor()
    {
        return color;
    }
    
    
    public CSprite setColor(Color color)
    {
        this.color = color.cpy();
        return this;
    }
    
    
    public int getIndex()
    {
        return index;
    }
    
    
    public CSprite setIndex(int index)
    {
        this.index = index;
        return this;
    }
    
    
    public boolean isAnimation()
    {
        return isAnimation;
    }
    
    
    public CSprite setAnimation(boolean isAnimation)
    {
        this.isAnimation = isAnimation;
        return this;
    }
    
    
    public boolean isAnimating()
    {
        return isAnimating;
    }
    
    
    public CSprite setAnimating(boolean isAnimating)
    {
        this.isAnimating = isAnimating;
        return this;
    }
    
    
    public boolean isReverse()
    {
        return isReverse;
    }
    
    
    public CSprite setReverse(boolean isReverse)
    {
        this.isReverse = isReverse;
        return this;
    }
    
    
    public boolean isRandomAnimation()
    {
        return isRandomAnimation;
    }
    
    
    public CSprite setRandomAnimation(boolean isRandomAnimation, long minMSecPerFrame, long maxMSecPerFrame)
    {
        this.isRandomAnimation = isRandomAnimation;
        this.rndMinMSecPerFrame = minMSecPerFrame;
        this.rndMaxMSecPerFrame = maxMSecPerFrame;
        return this;
    }
    
    
    public boolean shouldLoop()
    {
        return shouldLoop;
    }
    
    
    public CSprite setShouldLoop(boolean shouldLoop)
    {
        this.shouldLoop = shouldLoop;
        return this;
    }
    
    
    public long getMillisecondsPerFrame()
    {
        return mSecPerFrame;
    }
    
    
    public CSprite setMillisecondsPerFrame(long mSecPerFrame)
    {
        this.mSecPerFrame = mSecPerFrame;
        return this;
    }
    
    
    public boolean isFlipped()
    {
        return isFlipped;
    }
    
    
    public CSprite setFlipped(boolean isFlipped)
    {
        this.isFlipped = isFlipped;
        return this;
    }
    
    
    public boolean isFlopped()
    {
        return isFlopped;
    }
    
    
    public CSprite setFlopped(boolean isFlopped)
    {
        this.isFlopped = isFlopped;
        return this;
    }
    
    
    public boolean isFinished()
    {
        return isFinished;
    }
    
    
    public CSprite setTotalAnimationTime(long totalMilliseconds)
    {
        if (textures.size() > 0)
        {
            mSecPerFrame = totalMilliseconds / (long) textures.size();
        }
        return this;
    }
    
    // public boolean isTiled()
    // {
    // return isTiled;
    // }
    //
    //
    // public CSprite setIsTiled(boolean isTiled)
    // {
    // this.isTiled = isTiled;
    // tilesAreNowDirty();
    // return this;
    // }
    //
    //
    // public Vector2 getTileSize()
    // {
    // return tileSize.cpy();
    // }
    //
    //
    // public CSprite setTileSize(Vector2 tileSize)
    // {
    // setTileSize(tileSize.x, tileSize.y);
    // return this;
    // }
    //
    //
    // public CSprite setTileSize(float width, float height)
    // {
    // tileSize.x = width;
    // tileSize.y = height;
    // tilesAreNowDirty();
    // return this;
    // }
}
