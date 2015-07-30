package com.winger.draw.texture;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.winger.draw.font.CFont;
import com.winger.math.VectorMath;
import com.winger.struct.CRectangle;

import java.util.ArrayList;
import java.util.List;

public class CSpriteBatch
{
    public boolean shouldSort = true;
    private List<SortedTextureRecord> sortedRendering = new ArrayList<SortedTextureRecord>();
    private boolean actualShouldSort = true;
    private SpriteBatch sb;
    private ShapeRenderer shapes = new ShapeRenderer();
    private Camera camera;
    // public CRectangle window;
    
    private boolean shapeDrawMode = false;
    
    
    public CSpriteBatch(SpriteBatch sb)
    {
        this.sb = sb;
        shapes.setAutoShapeType(true);
    }
    
    
    public CSpriteBatch()
    {
        this(new SpriteBatch());
    }

    public Camera getCamera() {
        return camera;
    }
    
    public void setCamera(Camera camera)
    {
        this.camera = camera;
    }

    public Matrix4 getProjectionMatrix()
    {
        return sb.getProjectionMatrix();
    }
    
    public void setProjectionMatrix(Matrix4 projectionMatrix)
    {
        sb.setProjectionMatrix(projectionMatrix);
        shapes.setProjectionMatrix(projectionMatrix);
    }
    
    public void begin()
    {
        actualShouldSort = shouldSort;
        if (camera != null)
        {
            // float v = 0;
            // window = new CRectangle(0, 0, 0, 0);
            // Vector2 topLeft = VectorMath.toVector2(camera.unproject(new Vector3(0, 0, 0)));
            // Vector2 bottomRight = VectorMath.toVector2(camera.unproject(new Vector3(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0)));
            // window.x = topLeft.x + v;
            // window.y = bottomRight.y + v;
            // window.width = bottomRight.x - topLeft.x - v * 2;
            // window.height = topLeft.y - bottomRight.y - v * 2;
        }
    }
    
    
    private void sort(SortedTextureRecord record)
    {
        int index = -1;
        if (actualShouldSort)
        {
            for (int i = 0; i < sortedRendering.size(); i++)
            {
                if (sortedRendering.get(i).zIndex > record.zIndex)
                {
                    index = i;
                    break;
                }
            }
        }
        if (index == -1 || !actualShouldSort)
            index = sortedRendering.size();
        sortedRendering.add(index, record);
    }
    
    
    // private boolean isOnScreen(SortedTextureRecord record)
    // {
    // if (camera == null)
    // {
    // return true;
    // } else
    // {
    // CRectangle r = new CRectangle(record.x, record.y, record.width, record.height, record.rotation);
    // r.origin.x = record.originx;
    // r.origin.y = record.originy;
    // boolean b = (window.overlaps(r));
    // return b;
    // }
    // }
    
    public void draw(Texture texture, Color color, float x, float y, float originx, float originy, float width, float height, float u, float v,
        float rotation, int srcX, int srcY, int srcWidth, int srcHeight, boolean flipX, boolean flipY, float zIndex)
    {
        sort(new SortedTextureRecord(texture, color, x, y, originx, originy, width, height, u, v, rotation, srcX, srcY, srcWidth, srcHeight, flipX,
            flipY, zIndex));
    }
    
    
    public void drawText(CFont font, CharSequence str, float x, float y, Color color)
    {
        drawText(font, str, x, y, color, 0);
    }
    
    
    public void drawText(CFont font, CharSequence str, float x, float y, int start, int end, Color color)
    {
        drawText(font, str, x, y, start, end, color, 0);
    }
    
    
    public void drawTextMultiLine(CFont font, CharSequence str, float x, float y, Color color)
    {
        drawTextMultiLine(font, str, x, y, color, 0);
    }
    
    
    public void drawTextMultiLine(CFont font, CharSequence str, float x, float y, float alignmentWidth, int alignment, Color color)
    {
        drawTextMultiLine(font, str, x, y, alignmentWidth, alignment, color, 0);
    }
    
    
    public void drawTextWrapped(CFont font, CharSequence str, float x, float y, float wrapWidth, Color color)
    {
        drawTextWrapped(font, str, x, y, wrapWidth, color, 0);
    }
    
    
    public void drawTextWrapped(CFont font, CharSequence str, float x, float y, float wrapWidth, int alignment, Color color)
    {
        drawTextWrapped(font, str, x, y, wrapWidth, alignment, color, 0);
    }
    
    
    public void drawText(CFont font, CharSequence str, float x, float y, Color color, float zIndex)
    {
        sort(new SortedTextureRecord(FontDrawType.DRAW_TEXT_0, font, str, x, y, color, zIndex));
    }
    
    
    public void drawText(CFont font, CharSequence str, float x, float y, int start, int end, Color color, float zIndex)
    {
        sort(new SortedTextureRecord(font, str, x, y, start, end, color, zIndex));
    }
    
    
    public void drawTextMultiLine(CFont font, CharSequence str, float x, float y, Color color, float zIndex)
    {
        sort(new SortedTextureRecord(FontDrawType.DRAW_TEXT_MULTILINE_0, font, str, x, y, color, zIndex));
    }
    
    
    public void drawTextMultiLine(CFont font, CharSequence str, float x, float y, float alignmentWidth, int alignment, Color color, float zIndex)
    {
        sort(new SortedTextureRecord(FontDrawType.DRAW_TEXT_MULTILINE_1, font, str, x, y, alignmentWidth, alignment, color, zIndex));
    }
    
    
    public void drawTextWrapped(CFont font, CharSequence str, float x, float y, float wrapWidth, Color color, float zIndex)
    {
        
        sort(new SortedTextureRecord(FontDrawType.DRAW_TEXT_WRAPPED_0, font, str, x, y, wrapWidth, color, zIndex));
    }
    
    
    public void drawTextWrapped(CFont font, CharSequence str, float x, float y, float wrapWidth, int alignment, Color color, float zIndex)
    {
        
        sort(new SortedTextureRecord(FontDrawType.DRAW_TEXT_WRAPPED_1, font, str, x, y, wrapWidth, alignment, color, zIndex));
    }
    
    
    public void drawRectangle(ShapeType type, CRectangle rect, Color color, float zIndex)
    {
        sort(new SortedTextureRecord(type, rect.x - rect.origin.x, rect.y - rect.origin.y, rect.origin.x, rect.origin.y, rect.width, rect.height,
            rect.rotation, color, zIndex));
    }
    
    
    public void drawRectangle(ShapeType type, CRectangle rect, Color color)
    {
        drawRectangle(type, rect, color, 0);
    }
    
    
    public void drawCircle(ShapeType type, Vector2 position, float radius, Color color, float zIndex)
    {
        sort(new SortedTextureRecord(type, position.x, position.y, radius, color, zIndex));
    }
    
    
    public void drawCircle(ShapeType type, Vector2 position, float radius, Color color)
    {
        drawCircle(type, position, radius, color, 0);
    }
    
    
    public void drawArc(ShapeType type, Vector2 position, float radius, float arcRadiansStart, float arcRadians, Color color, float zIndex)
    {
        sort(new SortedTextureRecord(type, position.x, position.y, radius, arcRadiansStart, arcRadians, color, zIndex));
    }
    
    
    public void drawArc(ShapeType type, Vector2 position, float radius, float arcRadiansStart, float arcRadians, Color color)
    {
        drawArc(type, position, radius, arcRadiansStart, arcRadians, color, 0);
    }
    
    
    public void drawTriangle(ShapeType type, Vector2 point1, Vector2 point2, Vector2 point3, Color color, float zIndex)
    {
        sort(new SortedTextureRecord(type, point1.x, point1.y, point2.x, point2.y, point3.x, point3.y, color, zIndex));
    }
    
    
    public void drawTriangle(ShapeType type, Vector2 point1, Vector2 point2, Vector2 point3, Color color)
    {
        drawTriangle(type, point1, point2, point3, color, 0);
    }
    
    
    public void drawLine(Vector2 point1, Vector2 point2, Color color, float zIndex)
    {
        sort(new SortedTextureRecord(ShapeType.Line, point1.x, point1.y, point2.x, point2.y, color, zIndex));
    }
    
    
    public void drawLine(Vector2 point1, Vector2 point2, Color color)
    {
        drawLine(point1, point2, color, 0);
    }
    
    
    public void drawPoint(Vector2 point, Color color, float zIndex)
    {
        sort(new SortedTextureRecord(ShapeType.Line, point.x, point.y, color, zIndex));
    }
    
    
    public void drawPoint(Vector2 point, Color color)
    {
        drawPoint(point, color, 0);
    }
    
    
    public void drawArrow(ShapeType type, Vector2 pointStart, Vector2 pointEnd, float arrowSize, Color color, float zIndex)
    {
        Vector2 diff = pointEnd.cpy().sub(pointStart);
        float length = diff.len();
        length -= arrowSize;
        diff = diff.nor();
        //
        Vector2 perp = VectorMath.rotatePointByDegreesAroundZero(diff.cpy(), 90).scl(arrowSize / 2f);
        Vector2 perp2 = perp.cpy().scl(-1);
        diff = diff.scl(length);
        Vector2 shortEnd = pointStart.cpy().add(diff);
        perp = perp.add(shortEnd);
        perp2 = perp2.add(shortEnd);
        //
        drawLine(pointStart, shortEnd, color, zIndex);
        drawTriangle(type, pointEnd, perp, perp2, color, zIndex);
    }
    
    
    public void drawArrow(ShapeType type, Vector2 pointStart, Vector2 pointEnd, float arrowSize, Color color)
    {
        drawArrow(type, pointStart, pointEnd, arrowSize, color, 0);
    }
    
    
    public void end()
    {
        actuallyDrawTextures();
    }
    
    
    private void actuallyDrawTextures()
    {
        // if (window != null)
        // this.drawRectangle(ShapeType.Line, window, Color.GREEN, -100000);
        if (sortedRendering != null && sortedRendering.size() > 0)
        {
            sb.begin();
            shapeDrawMode = false;
            //
            Color original = sb.getColor();
            for (SortedTextureRecord record : sortedRendering)
            {
                if (record.font != null)
                {
                    if (shapeDrawMode == true)
                    {
                        shapes.end();
                        sb.begin();
                    }
                    shapeDrawMode = false;
                    //
                    Color originalFontColor = null;
                    if (record.color != null)
                    {
                        originalFontColor = record.font.getColor();
                        record.font.setColor(record.color);
                    }
                    switch (record.fontDrawType)
                    {
                        case DRAW_TEXT_0:
                            record.font.draw(sb, record.str, record.x, record.y, record.width, 0, true);
                            break;
                        case DRAW_TEXT_1:
                            record.font.draw(sb, record.str, record.x, record.y, record.width, 0, true);
                            break;
                        case DRAW_TEXT_MULTILINE_0:
                            record.font.draw(sb, record.str, record.x, record.y, record.width, 0, true);
                            break;
                        case DRAW_TEXT_MULTILINE_1:
                            record.font.draw(sb, record.str, record.x, record.y, record.width, 0, true);
                            break;
                        case DRAW_TEXT_WRAPPED_0:
                            record.font.draw(sb, record.str, record.x, record.y, record.width, 0, true);
                            break;
                        case DRAW_TEXT_WRAPPED_1:
                            record.font.draw(sb, record.str, record.x, record.y, record.width, 0, true);
                            break;
                        default:
                            break;
                    }
                    if (record.color != null)
                    {
                        record.font.setColor(originalFontColor);
                    }
                } else if (record.isShape)
                {
                    if (shapeDrawMode == false)
                    {
                        sb.end();
                        shapes.begin();
                    }
                    shapeDrawMode = true;
                    //
                    Color originalShapeColor = null;
                    if (record.color != null)
                    {
                        originalShapeColor = shapes.getColor();
                        shapes.setColor(record.color);
                    }
                    shapes.set(record.shapeType);
                    switch (record.shapeDrawType)
                    {
                        case ARC:
                            shapes.arc(record.x, record.y, record.radius, (float) Math.toDegrees((double) record.arcRadiansStart),
                                (float) Math.toDegrees((double) record.arcRadians));
                            break;
                        case CIRCLE:
                            shapes.circle(record.x, record.y, record.radius);
                            break;
                        case LINE:
                            shapes.line(record.x, record.y, record.x2, record.y2);
                            break;
                        case POINT:
                            shapes.point(record.x, record.y, 0);
                            break;
                        case RECTANGLE:
                            shapes.rect(record.x, record.y, record.originx, record.originy, record.width, record.height, 1, 1, record.rotation);
                            break;
                        case TRIANGLE:
                            shapes.triangle(record.x, record.y, record.x2, record.y2, record.x3, record.y3);
                            break;
                        default:
                            break;
                    
                    }
                    if (record.color != null)
                    {
                        shapes.setColor(originalShapeColor);
                    }
                } else
                {
                    if (shapeDrawMode == true)
                    {
                        shapes.end();
                        sb.begin();
                    }
                    shapeDrawMode = false;
                    //
                    if (record.color != null)
                        sb.setColor(record.color);
                    // if (isOnScreen(record))
                    // {
                    sb.draw(record.texture, record.x, record.y, record.originx, record.originy, record.width, record.height, record.u, record.v,
                        record.rotation, record.srcX, record.srcY, record.srcWidth, record.srcHeight, record.flipX, record.flipY);
                    // }
                    if (record.color != null)
                        sb.setColor(original);
                }
            }
            if (shapeDrawMode == true)
            {
                shapes.end();
            } else
            {
                sb.end();
            }
        }
        //
        sortedRendering = new ArrayList<SortedTextureRecord>();
    }


    private enum FontDrawType {
        DRAW_TEXT_0,
        DRAW_TEXT_1,
        DRAW_TEXT_MULTILINE_0,
        DRAW_TEXT_MULTILINE_1,
        DRAW_TEXT_WRAPPED_0,
        DRAW_TEXT_WRAPPED_1
    }


    private enum ShapeDrawType {
        RECTANGLE,
        CIRCLE,
        ARC,
        TRIANGLE,
        LINE,
        POINT,
    }
    
    private class SortedTextureRecord
    {
        public Texture texture;
        public Color color;
        public float x;
        public float y;
        public float originx;
        public float originy;
        public float width;
        public float height;
        public float u;
        public float v;
        public float rotation;
        public int srcX;
        public int srcY;
        public int srcWidth;
        public int srcHeight;
        public boolean flipX;
        public boolean flipY;
        public float zIndex;

        public CFont font;
        public FontDrawType fontDrawType;
        public CharSequence str;
        public int start;
        public int end;
        public int alignment;

        public boolean isShape = false;
        public ShapeType shapeType;
        public ShapeDrawType shapeDrawType;
        public float radius;
        public float x2;
        public float y2;
        public float x3;
        public float y3;
        public float arcRadiansStart;
        public float arcRadians;


        public SortedTextureRecord(Texture texture, Color color, float x, float y, float originx, float originy, float width, float height, float u,
            float v, float rotation, int srcX, int srcY, int srcWidth, int srcHeight, boolean flipX, boolean flipY, float zIndex)
        {
            this.texture = texture;
            this.color = color;
            this.x = x;
            this.y = y;
            this.originx = originx;
            this.originy = originy;
            this.width = width;
            this.height = height;
            this.u = u;
            this.v = v;
            this.rotation = rotation;
            this.srcX = srcX;
            this.srcY = srcY;
            this.srcWidth = srcWidth;
            this.srcHeight = srcHeight;
            this.flipX = flipX;
            this.flipY = flipY;
            this.zIndex = zIndex;
        }


        public SortedTextureRecord(CFont font, CharSequence str, float x, float y, int start, int end, Color color, float zIndex)
        {
            this.fontDrawType = FontDrawType.DRAW_TEXT_0;
            this.font = font;
            this.str = str;
            this.x = x;
            this.y = y;
            this.start = start;
            this.end = end;
            this.color = color;
            this.zIndex = zIndex;
        }


        public SortedTextureRecord(FontDrawType fontDrawType, CFont font, CharSequence str, float x, float y, Color color, float zIndex)
        {
            this.fontDrawType = fontDrawType;
            this.font = font;
            this.str = str;
            this.x = x;
            this.y = y;
            this.color = color;
            this.zIndex = zIndex;
        }


        public SortedTextureRecord(FontDrawType fontDrawType, CFont font, CharSequence str, float x, float y, float width, Color color, float zIndex)
        {
            this.fontDrawType = FontDrawType.DRAW_TEXT_WRAPPED_0;
            this.font = font;
            this.str = str;
            this.x = x;
            this.y = y;
            this.width = width;
            this.color = color;
            this.zIndex = zIndex;
        }


        public SortedTextureRecord(FontDrawType fontDrawType, CFont font, CharSequence str, float x, float y, float width, int alignment,
            Color color, float zIndex)
        {
            this.fontDrawType = fontDrawType;
            this.font = font;
            this.str = str;
            this.x = x;
            this.y = y;
            this.width = width;
            this.alignment = alignment;
            this.color = color;
            this.zIndex = zIndex;
        }


        public SortedTextureRecord(ShapeType shapeType, float x, float y, float originx, float originy, float width, float height, float rotation,
            Color color, float zIndex)
        {
            isShape = true;
            this.shapeDrawType = ShapeDrawType.RECTANGLE;
            this.shapeType = shapeType;
            this.x = x;
            this.y = y;
            this.originx = originx;
            this.originy = originy;
            this.width = width;
            this.height = height;
            this.rotation = rotation;
            this.color = color;
            this.zIndex = zIndex;
        }


        public SortedTextureRecord(ShapeType shapeType, float x, float y, float radius, Color color, float zIndex)
        {
            isShape = true;
            this.shapeDrawType = ShapeDrawType.CIRCLE;
            this.shapeType = shapeType;
            this.x = x;
            this.y = y;
            this.radius = radius;
            this.color = color;
            this.zIndex = zIndex;
        }


        public SortedTextureRecord(ShapeType shapeType, float x, float y, float radius, float arcRadiansStart, float arcRadians, Color color,
            float zIndex)
        {
            isShape = true;
            this.shapeDrawType = ShapeDrawType.ARC;
            this.shapeType = shapeType;
            this.x = x;
            this.y = y;
            this.radius = radius;
            this.arcRadiansStart = arcRadiansStart;
            this.arcRadians = arcRadians;
            this.color = color;
            this.zIndex = zIndex;
        }


        public SortedTextureRecord(ShapeType shapeType, float x, float y, float x2, float y2, float x3, float y3, Color color, float zIndex)
        {
            isShape = true;
            this.shapeDrawType = ShapeDrawType.TRIANGLE;
            this.shapeType = shapeType;
            this.x = x;
            this.y = y;
            this.x2 = x2;
            this.y2 = y2;
            this.x3 = x3;
            this.y3 = y3;
            this.color = color;
            this.zIndex = zIndex;
        }


        public SortedTextureRecord(ShapeType shapeType, float x, float y, float x2, float y2, Color color, float zIndex)
        {
            isShape = true;
            this.shapeDrawType = ShapeDrawType.LINE;
            this.shapeType = shapeType;
            this.x = x;
            this.y = y;
            this.x2 = x2;
            this.y2 = y2;
            this.color = color;
            this.zIndex = zIndex;
        }


        public SortedTextureRecord(ShapeType shapeType, float x, float y, Color color, float zIndex)
        {
            isShape = true;
            this.shapeDrawType = ShapeDrawType.POINT;
            this.shapeType = shapeType;
            this.x = x;
            this.y = y;
            this.color = color;
            this.zIndex = zIndex;
        }
    }
}
