package com.winger.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.winger.Winger;
import com.winger.draw.font.CFont;
import com.winger.draw.texture.CSpriteBatch;
import com.winger.input.delegate.CKeyboardEventHandler;
import com.winger.input.delegate.CMouseEventHandler;
import com.winger.input.raw.CKeyboard;
import com.winger.input.raw.CMouse;
import com.winger.input.raw.state.ButtonState;
import com.winger.input.raw.state.CMouseButton;
import com.winger.input.raw.state.CMouseDrag;
import com.winger.input.raw.state.KeyboardKey;
import com.winger.log.HTMLLogger;
import com.winger.log.LogGroup;
import com.winger.math.VectorMath;
import com.winger.struct.CRectangle;
import com.winger.struct.JSON;
import com.winger.struct.Tups.Tup2;
import com.winger.ui.Element;
import com.winger.ui.Page;
import com.winger.ui.PageManager;
import com.winger.ui.delegate.PageEventHandler;
import com.winger.ui.delegate.TextEntered;
import com.winger.ui.element.impl.TextBoxElement;
import com.winger.ui.element.impl.ToggleElement;
import com.winger.utils.ReflectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LevelEditor<T extends TileType<T>> implements PageEventHandler, CMouseEventHandler, CKeyboardEventHandler, TextEntered
{
    private static final HTMLLogger log = HTMLLogger.getLogger(LevelEditor.class, LogGroup.Framework, LogGroup.Util);
    
    private static final float UI_W = 800f;
    private static final float UI_H = 600f;
    private static final float UI_OPTIONS_WIDTH = 60f;
    private static final float UI_OPTIONS_HEIGHT = 60f;
    
    private static final float UI_TILES_MAIN_WIDTH = 650f / UI_W * 100f;
    // private static final float UI_TILES_WIDTH = 75f / UI_W * 100f;
    private static final float UI_TILES_HEIGHT = 75f / UI_H * 100f;
    private static final float UI_TILES_PADDING_W = 5f / UI_W * 100f;
    private static final float UI_TILES_PADDING_H = 5f / UI_H * 100f;
    
    private static final float UI_STATUS_WIDTH = 150f / UI_W * 100f;
    private static final float UI_STATUS_PADDING_W = 2f / UI_W * 100f;
    private static final float UI_STATUS_PADDING_H = 2f / UI_H * 100f;
    private static final float UI_STATUS_KEY = 55f / 200f * 100f;
    private static final float UI_STATUS_VALUE = 125f / 200f * 100f;
    // private static final float UI_STATUS_TEXT_0 = 10f / UI_H * 100f;
    private static final float UI_STATUS_TEXT_1 = 20f / UI_H * 100f;
    private static final float UI_STATUS_TEXT_2 = 30f / UI_H * 100f;
    // private static final float UI_STATUS_TEXT_3 = 40f / UI_H * 100f;
    
    private static final String UI_BACKGROUND_COLOR = "#aaaaaa";
    
    private static final String PAGE_NAME = "leveleditor";
    
    private static final float CAMERA_MOVE_SPEED = 15;
    
    private static final Color SELECTED_TILE_COLOR = Color.YELLOW.cpy();
    private static final Color SENSOR_TILE_COLOR = new Color(1, 0, 1, 1);
    private static final Color PARENT_TILE_COLOR = Color.CYAN.cpy();
    private static final Color DYNAMIC_TILE_COLOR = new Color(0, 1, 0, 1);
    private static final Color UNKOWN_TILE_COLOR = new Color(1, 1, 1, 1);
    private static final Color GRID_LINE_COLOR = new Color(0.1f, 0.1f, 0.1f, 1);
    //
    private Page ui;
    private boolean uiHasFocus = false;
    private String selectedOptionToggle = null;
    private String lastSelectedOptionToggle = null;
    private String[] optionsToggles = new String[] { "select", "add", "move", "stretch", "delete" };
    private Element<?> mouseUIElement = null;
    //
    private int snapAmount = 32;
    //
    private String levelName = "default";
    private Map<String, Tileable> tileTypes = new HashMap<String, Tileable>();
    private List<Tile> tiles = new ArrayList<Tile>();
    private String currentPaintTile = null;
    private Tile currentTile = null;
    private boolean creatingTile = false;
    private boolean movingTile = false;
    private boolean stretchingTile = false;
    private Vector2 originalStretchingPosition = null;
    private Vector2 stretchingOrientation = new Vector2(0, 0);
    private boolean showGrid = true;
    private boolean showSprites = true;
    private boolean showIds = true;
    private int tileIdIndex = 0;
    //
    private Camera camera = new OrthographicCamera();
    private CSpriteBatch sb = new CSpriteBatch();
    
    
    public LevelEditor(TileType<T> defaultTile)
    {
        T[] tileTypes = defaultTile.tileTypes();
        for (T tileType : tileTypes)
        {
            this.tileTypes.put(tileType.toString().toLowerCase(), ReflectionUtils.create(tileType.clazz()));
        }
        createUI();
        subscribeToEvents();
        
        ui.keyboard.subscribeToAllKeyboardEvents(this, ButtonState.DOWN);
        ui.keyboard.subscribeToAllKeyboardEvents(this, ButtonState.UP);
        ui.mouse.subscribeToAllMouseEvents(this);
        
        mouseUIElement = ui.getElementById("page_mouse");
        
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(camera.viewportWidth / 4f, camera.viewportHeight / 4f, 0);
        camera.update();
        
        sb.setCamera(camera);
    }
    
    
    public void printTileTypes()
    {
        log.info("Level Editor Tile Types: ");
        for (String tileName : tileTypes.keySet())
        {
            Tileable tile = tileTypes.get(tileName);
            log.info(tileName + ": " + tile.getClass().getName());
        }
    }
    
    
    // /////////////////////////////////////////////
    // Main Methods
    // /////////////////////////////////////////////
    public void update(float delta)
    {
        if (ui.isEnabled)
        {
            camera.update();
            if (creatingTile || movingTile || stretchingTile)
                updateUIStatus();
            
            if (mouseUIElement != null)
            {
                Vector2 mouseWorldPos = VectorMath.toVector2(camera.unproject(VectorMath.toVector3(ui.mouse.x(),
                    Gdx.graphics.getHeight() - ui.mouse.y())));
                mouseUIElement.text((int) mouseWorldPos.x + "\n" + (int) mouseWorldPos.y);
            }
            
            if (!uiHasFocus)
            {
                if (ui.keyboard.isKeyBeingPressed(KeyboardKey.LEFT) || ui.keyboard.isKeyBeingPressed(KeyboardKey.A))
                {
                    camera.translate(-CAMERA_MOVE_SPEED, 0, 0);
                } else if (ui.keyboard.isKeyBeingPressed(KeyboardKey.RIGHT) || ui.keyboard.isKeyBeingPressed(KeyboardKey.D))
                {
                    camera.translate(CAMERA_MOVE_SPEED, 0, 0);
                }
                if (ui.keyboard.isKeyBeingPressed(KeyboardKey.UP) || ui.keyboard.isKeyBeingPressed(KeyboardKey.W))
                {
                    camera.translate(0, CAMERA_MOVE_SPEED, 0);
                } else if (ui.keyboard.isKeyBeingPressed(KeyboardKey.DOWN) || ui.keyboard.isKeyBeingPressed(KeyboardKey.S))
                {
                    camera.translate(0, -CAMERA_MOVE_SPEED, 0);
                }
            }
            
            for (String tileName : tileTypes.keySet())
            {
                Tileable t = tileTypes.get(tileName);
                if (t.getTileSprite() != null)
                {
                    t.getTileSprite().update(delta);
                }
            }
        }
    }
    
    
    public void draw()
    {
        if (ui.isVisible)
        {
            sb.setProjectionMatrix(camera.combined);
            sb.begin();
            
            if (showGrid)
                drawGrid();
            for (Tile tile : tiles)
            {
                boolean lightColored = true;
                if (tile.ref != null)
                {
                    Color tileColor = tile.ref.getTileColor();
                    Vector3 colorV = new Vector3(tileColor.r, tileColor.g, tileColor.b);
                    if (colorV.len() <= 0.707)
                        lightColored = false;
                    if (tile.ref.isSensor())
                        lightColored = false;
                    if (showSprites && tile.ref.getTileSprite() != null)
                    {
                        tile.ref.getTileSprite().setRect(tile.rect);
                        tile.ref.getTileSprite().setZ(tile.zIndex);
                        // tile.ref.getTileSprite().setIsTiled(tile.tiled);
                        // tile.ref.getTileSprite().setTileSize(tile.tiledWidth, tile.tiledHeight);
                        tile.ref.getTileSprite().draw(sb);
                    }
                    if (tile.ref.isSensor())
                    {
                        sb.drawRectangle(ShapeType.Line, tile.rect, SENSOR_TILE_COLOR, tile.zIndex);
                    } else if (!showSprites || tile.ref.getTileSprite() == null)
                    {
                        sb.drawRectangle(ShapeType.Filled, tile.rect, tileColor, tile.zIndex);
                    }
                    if (!tile.ref.isStatic())
                    {
                        sb.drawRectangle(ShapeType.Line, tile.rect, DYNAMIC_TILE_COLOR, tile.zIndex);
                    }
                    
                } else
                {
                    sb.drawRectangle(ShapeType.Line, tile.rect, UNKOWN_TILE_COLOR, tile.zIndex);
                }
                if (tile == currentTile)
                {
                    sb.drawRectangle(ShapeType.Line, tile.rect, SELECTED_TILE_COLOR, 1000);
                }
                if (showIds && tile.id != null && !tile.id.isEmpty())
                {
                    CFont font = Winger.font.getDefaultFont();
                    if (font != null)
                    {
                        sb.drawText(font, tile.id, tile.rect.x, tile.rect.y + tile.rect.height, (lightColored ? Color.BLACK : Color.WHITE), 2000);
                    }
                }
            }
            drawTriggers();
            drawParentConnectors();
            sb.end();
        }
    }
    
    
    private void drawGrid()
    {
        Vector2 bottomLeft = VectorMath.toVector2(camera.unproject(VectorMath.toVector3(0, Gdx.graphics.getHeight())));
        bottomLeft.x = snapToFloor(bottomLeft.x, snapAmount);
        bottomLeft.y = snapToFloor(bottomLeft.y, snapAmount);
        Vector2 topRight = VectorMath.toVector2(camera.unproject(VectorMath.toVector3(Gdx.graphics.getWidth(), 0)));
        topRight.x = snapToFloor(topRight.x, snapAmount) + snapAmount;
        topRight.y = snapToFloor(topRight.y, snapAmount) + snapAmount;
        //
        Color c = GRID_LINE_COLOR;
        int zIndex = -1000;
        for (float x = bottomLeft.x; x < topRight.x; x += snapAmount)
        {
            if (x == 0)
            {
                c = Color.GREEN;
                zIndex = 1000;
            } else
            {
                c = GRID_LINE_COLOR;
                zIndex = -1000;
            }
            sb.drawLine(new Vector2(x, bottomLeft.y), new Vector2(x, topRight.y), c, zIndex);
        }
        for (float y = bottomLeft.y; y < topRight.y; y += snapAmount)
        {
            if (y == 0)
            {
                c = Color.RED;
                zIndex = 1000;
            } else
            {
                c = GRID_LINE_COLOR;
                zIndex = -1000;
            }
            sb.drawLine(new Vector2(bottomLeft.x, y), new Vector2(topRight.x, y), c, zIndex);
        }
        sb.drawCircle(ShapeType.Filled, new Vector2(0, 0), snapAmount / 4, GRID_LINE_COLOR, 1000);
    }
    
    
    private void drawTriggers()
    {
        for (Tile tile : tiles)
        {
            if (tile.triggerName != null && !tile.triggerName.isEmpty() && tile.triggerId != null && !tile.triggerId.isEmpty())
            {
                Tile subTile = getTileById(tile.triggerId);
                if (subTile != null)
                {
                    Vector2 p1 = tile.rect.center().cpy();
                    p1.y += 5;
                    Vector2 p2 = subTile.rect.center().cpy();
                    p2.x += 5;
                    sb.drawArrow(ShapeType.Line, p1, p2, 10, SENSOR_TILE_COLOR, 1000);
                }
            }
        }
    }
    
    
    private void drawParentConnectors()
    {
        for (Tile tile : tiles)
        {
            if (tile.parent != null && !tile.parent.isEmpty())
            {
                Tile subTile = getTileById(tile.parent);
                if (subTile != null)
                {
                    Vector2 p1 = tile.rect.center().cpy();
                    p1.y -= 5;
                    Vector2 p2 = subTile.rect.center().cpy();
                    p2.x -= 5;
                    sb.drawArrow(ShapeType.Line, p1, p2, 10, PARENT_TILE_COLOR, 1000);
                }
            }
        }
    }
    
    
    private int snapToFloor(float value, int snap)
    {
        float i = (value) / ((float) snap);
        if (i < 0)
            i--;
        return ((int) i) * snap;
    }
    
    
    private CRectangle getSnappedRectangle(Vector2 a, Vector2 b, int snap)
    {
        //
        float minX = Math.min(a.x, b.x);
        float minY = Math.min(a.y, b.y);
        float maxX = Math.max(a.x, b.x);
        float maxY = Math.max(a.y, b.y);
        //
        minX = snapToFloor(minX, snap);
        minY = snapToFloor(minY, snap);
        maxX = snapToFloor(maxX, snap) + snap;
        maxY = snapToFloor(maxY, snap) + snap;
        //
        return new CRectangle(minX, minY, maxX - minX, maxY - minY);
    }
    
    
    private Vector2 getSnappedPosition(Vector2 pos, int snap)
    {
        return new Vector2(snapToFloor(pos.x, snap), snapToFloor(pos.y, snap));
    }
    
    
    private void getStretchingOrientation(CRectangle rect, Vector2 pos)
    {
        Vector2 diff = pos.cpy().sub(rect.center());
        if (diff.x > 0)
            stretchingOrientation.x = 0;
        else
            stretchingOrientation.x = 1;
        if (diff.y > 0)
            stretchingOrientation.y = 0;
        else
            stretchingOrientation.y = 1;
    }
    
    
    // /////////////////////////////////////////////
    // Tile Methods
    // /////////////////////////////////////////////
    private Tile createTile(Vector2 position)
    {
        Tile t = new Tile();
        t.id = "" + tileIdIndex;
        tileIdIndex++;
        t.rect.x = position.x;
        t.rect.y = position.y;
        t.rect.width = snapAmount;
        t.rect.height = snapAmount;
        t.ref = tileTypes.get(currentPaintTile);
        t.tiled = tileTypes.get(currentPaintTile).isTiled();
        t.tiledWidth = snapAmount;
        t.tiledHeight = snapAmount;
        t.type = currentPaintTile;
        tiles.add(t);
        return t;
    }
    
    
    private Tile deleteTile(Tile t)
    {
        log.info("Remove tile");
        tiles.remove(t);
        if (t == currentTile)
            setCurrentTile(null);
        return t;
    }
    
    
    private void setCurrentTile(Tile tile)
    {
        currentTile = tile;
        updateUIStatus();
    }
    
    
    private Tile selectTile(Vector2 selectionPosition)
    {
        for (int i = tiles.size() - 1; i >= 0; i--)
        {
            Tile t = tiles.get(i);
            if (t != currentTile && t.rect.contains(selectionPosition))
            {
                return t;
            }
        }
        return null;
    }
    
    
    private void updateCurrentTile()
    {
        if (currentTile != null)
        {
            String originalParent = currentTile.parent;
            currentTile.parent = ui.getElementById("element_parent").text();
            if (!checkForLoopingParentalStructure(currentTile))
                currentTile.parent = originalParent;
            if (currentTile.parent != null && currentTile.parent.equals(currentTile.id))
                currentTile.parent = originalParent;
            
            String newId = ui.getElementById("element_id").text();
            if (getTileById(newId) == null)
                currentTile.id = newId;
            if (currentTile.parent != null && currentTile.parent.equals(currentTile.id))
                currentTile.parent = null;
            
            boolean needToUpdateChildren = false;
            float diffX = 0;
            float diffY = 0;
            try
            {
                float newX = Float.parseFloat(ui.getElementById("element_x").text());
                if (newX != currentTile.rect.x)
                {
                    needToUpdateChildren = true;
                    diffX = newX - currentTile.rect.x;
                }
                currentTile.rect.x = newX;
            } catch (Exception e)
            {}
            try
            {
                float newY = Float.parseFloat(ui.getElementById("element_y").text());
                if (newY != currentTile.rect.y)
                {
                    needToUpdateChildren = true;
                    diffY = newY - currentTile.rect.y;
                }
                currentTile.rect.y = newY;
            } catch (Exception e)
            {}
            if (needToUpdateChildren)
            {
                updateChildren(currentTile, diffX, diffY);
            }
            try
            {
                currentTile.zIndex = Float.parseFloat(ui.getElementById("element_z").text());
            } catch (Exception e)
            {}
            try
            {
                currentTile.rect.width = Float.parseFloat(ui.getElementById("element_width").text());
            } catch (Exception e)
            {}
            try
            {
                currentTile.rect.height = Float.parseFloat(ui.getElementById("element_height").text());
            } catch (Exception e)
            {}
            try
            {
                currentTile.tiledWidth = Float.parseFloat(ui.getElementById("element_tiledwidth").text());
            } catch (Exception e)
            {}
            try
            {
                currentTile.tiledHeight = Float.parseFloat(ui.getElementById("element_tiledheight").text());
            } catch (Exception e)
            {}
            currentTile.triggerName = ui.getElementById("trigger_name").text();
            currentTile.triggerId = ui.getElementById("trigger_id").text();
        }
        // update page
        levelName = ui.getElementById("page_name").text();
        if (levelName == null || levelName.isEmpty())
            levelName = "default";
        try
        {
            snapAmount = Integer.parseInt(ui.getElementById("page_snap").text());
            if (snapAmount == 0)
                snapAmount = 32;
            else if (snapAmount < 0)
                snapAmount *= -1;
        } catch (Exception e)
        {}
    }
    
    
    private Tile getTileById(String id)
    {
        if (id != null && !id.isEmpty())
        {
            for (Tile tile : tiles)
            {
                if (id.equals(tile.id))
                {
                    return tile;
                }
            }
        }
        return null;
    }
    
    
    private List<Tile> getChildren(Tile t)
    {
        List<Tile> children = new ArrayList<Tile>();
        if (t != null)
        {
            for (Tile tile : tiles)
            {
                if (tile != t && tile.parent != null && tile.parent.equals(t.id))
                {
                    children.add(tile);
                }
            }
        }
        return children;
    }
    
    
    private void updateChildren(Tile t, float diffX, float diffY)
    {
        List<Tile> children = getChildren(t);
        for (Tile child : children)
        {
            child.rect.x += diffX;
            child.rect.y += diffY;
            updateChildren(child, diffX, diffY);
        }
    }
    
    
    private boolean checkForLoopingParentalStructure(Tile t)
    {
        try
        {
            for (Tile child : getChildren(t))
            {
                boolean b = checkForLoopingParentalStructure(child);
                if (!b)
                    return false;
            }
            return true;
        } catch (StackOverflowError e)
        {
            return false;
        }
    }
    
    
    // /////////////////////////////////////////////
    // UI Methods
    // /////////////////////////////////////////////
    private void createUI()
    {
        float W = Gdx.graphics.getWidth() / 100f;
        float H = Gdx.graphics.getHeight() / 100f;
        JSON root = JSON.emptyObject();
        root.put("elements", JSON.emptyArray());
        //
        JSON tileJson = JSON.emptyObject();
        tileJson.put("id", "tileScroll");
        tileJson.put("isEnabled", false);
        tileJson.put("type", "scroll");
        tileJson.put("color", UI_BACKGROUND_COLOR);
        tileJson.put("alignment", "bottom-left");
        tileJson.put("width", UI_TILES_MAIN_WIDTH + "%");
        tileJson.put("height", (UI_TILES_HEIGHT + UI_TILES_PADDING_H * 2) + "%");
        tileJson.put("bar", JSON.emptyObject());
        tileJson.put("bar.verticalScroll", false);
        tileJson.put("bar.horizontalFlip", true);
        tileJson.put("children", JSON.emptyArray());
        JSON tileGrid = JSON.emptyObject();
        tileGrid.put("type", "grid");
        tileGrid.put("isEnabled", false);
        tileGrid.put("alignment", "bottom-left");
        tileGrid.put("paddingLeft", UI_TILES_PADDING_W * W);
        tileGrid.put("paddingRight", UI_TILES_PADDING_W * W);
        tileGrid.put("paddingTop", UI_TILES_PADDING_H * H);
        tileGrid.put("paddingBottom", UI_TILES_PADDING_H * H);
        tileGrid.put("rows", 1);
        tileGrid.put("columns", -1);
        tileGrid.put("children", JSON.emptyArray());
        for (String tileName : tileTypes.keySet())
        {
            JSON tile = JSON.emptyObject();
            tile.put("id", "tile_" + tileName);
            tile.put("type", "toggle");
            tile.put("text", tileName);
            tile.put("width", UI_TILES_HEIGHT * H);
            tile.put("height", UI_TILES_HEIGHT * H);
            tile.put("color", "#" + tileTypes.get(tileName).getTileColor().toString());
            tile.put("colorHover", "#" + tileTypes.get(tileName).getTileColor().toString());
            tile.put("onColor", "red");
            tile.put("onColorHover", "red");
            tile.put("onSelectStart", "tile_" + tileName + "_selectStart");
            tileGrid.put("children.#", tile);
        }
        tileJson.put("children.#", tileGrid);
        //
        JSON optionsJson = JSON.emptyObject();
        optionsJson.put("id", "options");
        optionsJson.put("isEnabled", false);
        optionsJson.put("type", "grid");
        optionsJson.put("color", UI_BACKGROUND_COLOR);
        optionsJson.put("alignment", "top-left");
        optionsJson.put("y", Gdx.graphics.getHeight());
        optionsJson.put("paddingLeft", UI_TILES_PADDING_W * W);
        optionsJson.put("paddingRight", UI_TILES_PADDING_W * W);
        optionsJson.put("paddingTop", UI_TILES_PADDING_H * H);
        optionsJson.put("paddingBottom", UI_TILES_PADDING_H * H);
        optionsJson.put("rows", 1);
        optionsJson.put("columns", -1);
        optionsJson.put("children", JSON.emptyArray());
        String[] optionButtons = new String[] { "open", "save", "save_as", "play" };
        for (String optionButton : optionButtons)
        {
            JSON optionJson = JSON.emptyObject();
            optionJson.put("id", "option_" + optionButton);
            optionJson.put("text", optionButton);
            optionJson.put("width", UI_OPTIONS_WIDTH);
            optionJson.put("height", UI_OPTIONS_HEIGHT);
            optionJson.put("groups", JSON.emptyArray());
            optionJson.put("groups.#", "selectable");
            optionsJson.put("children.#", optionJson);
        }
        for (String optionButton : optionsToggles)
        {
            JSON optionJson = JSON.emptyObject();
            optionJson.put("id", "option_" + optionButton);
            optionJson.put("text", optionButton);
            optionJson.put("type", "toggle");
            optionJson.put("width", UI_OPTIONS_WIDTH);
            optionJson.put("height", UI_OPTIONS_HEIGHT);
            optionJson.put("onColor", "red");
            optionJson.put("onColorHover", "red");
            optionJson.put("onSelectStart", "option_" + optionButton + "_selectStart");
            optionsJson.put("children.#", optionJson);
        }
        //
        JSON statusWrapper = JSON.emptyObject();
        statusWrapper.put("id", "statusWrapper");
        statusWrapper.put("isEnabled", false);
        statusWrapper.put("color", UI_BACKGROUND_COLOR);
        statusWrapper.put("alignment", "bottom-right");
        statusWrapper.put("x", Gdx.graphics.getWidth());
        statusWrapper.put("width", UI_STATUS_WIDTH + "%");
        statusWrapper.put("height", "100%");
        statusWrapper.put("children", JSON.emptyArray());
        JSON statusJson = JSON.emptyObject();
        statusJson.put("id", "status");
        statusJson.put("isEnabled", false);
        statusJson.put("type", "grid");
        statusJson.put("color", UI_BACKGROUND_COLOR);
        statusJson.put("alignment", "top-left");
        statusJson.put("x", "-100%");
        statusJson.put("y", "100%");
        statusJson.put("paddingLeft", UI_STATUS_PADDING_W * W);
        statusJson.put("paddingRight", UI_STATUS_PADDING_W * W);
        statusJson.put("paddingTop", UI_STATUS_PADDING_H * H);
        statusJson.put("paddingBottom", UI_STATUS_PADDING_H * H);
        statusJson.put("row", -1);
        statusJson.put("column", 2);
        JSON statusChildren = JSON.emptyArray();
        statusJson.put("children", statusChildren);
        
        addTextField(statusChildren, "Page", UI_STATUS_TEXT_2 + "%", false, true, "");
        addTextField(statusChildren, "Name", UI_STATUS_TEXT_1 + "%", false, false, "page_");
        addTextField(statusChildren, "Mouse", UI_STATUS_TEXT_1 + "%", false, false, "page_");
        addTextField(statusChildren, "Grid", UI_STATUS_TEXT_1 + "%", false, false, "page_", true);
        addTextField(statusChildren, "Sprites", UI_STATUS_TEXT_1 + "%", false, false, "page_", true);
        addTextField(statusChildren, "Ids", UI_STATUS_TEXT_1 + "%", false, false, "page_", true);
        addTextField(statusChildren, "Snap", UI_STATUS_TEXT_1 + "%", true, false, "page_");
        // addTextField(statusChildren, "", UI_STATUS_TEXT_0 + "%", false, true, "");
        addTextField(statusChildren, "Element", UI_STATUS_TEXT_2 + "%", false, true, "");
        addTextField(statusChildren, "Parent", UI_STATUS_TEXT_1 + "%", false, false, "element_");
        addTextField(statusChildren, "Id", UI_STATUS_TEXT_1 + "%", false, false, "element_");
        addTextField(statusChildren, "Type", UI_STATUS_TEXT_1 + "%", false, false, "element_");
        addTextField(statusChildren, "X", UI_STATUS_TEXT_1 + "%", true, false, "element_");
        addTextField(statusChildren, "Y", UI_STATUS_TEXT_1 + "%", true, false, "element_");
        addTextField(statusChildren, "Z", UI_STATUS_TEXT_1 + "%", true, false, "element_");
        addTextField(statusChildren, "Width", UI_STATUS_TEXT_1 + "%", true, false, "element_");
        addTextField(statusChildren, "Height", UI_STATUS_TEXT_1 + "%", true, false, "element_");
        addTextField(statusChildren, "Tiled", UI_STATUS_TEXT_1 + "%", false, false, "element_", true);
        addTextField(statusChildren, "TiledWidth", UI_STATUS_TEXT_1 + "%", true, false, "element_");
        addTextField(statusChildren, "TiledHeight", UI_STATUS_TEXT_1 + "%", true, false, "element_");
        // addTextField(statusChildren, "", UI_STATUS_TEXT_0 + "%", false, true, "");
        addTextField(statusChildren, "Trigger", UI_STATUS_TEXT_2 + "%", false, true, "");
        addTextField(statusChildren, "Name", UI_STATUS_TEXT_1 + "%", true, false, "trigger_");
        addTextField(statusChildren, "Id", UI_STATUS_TEXT_1 + "%", true, false, "trigger_");
        
        statusWrapper.put("children.#", statusJson);
        //
        root.put("elements.#", tileJson);
        root.put("elements.#", optionsJson);
        root.put("elements.#", statusWrapper);
        //
        root.put("global", JSON.emptyObject());
        root.put("global.color", UI_BACKGROUND_COLOR);
        root.put("global.font", "default");
        root.put("global.texture", "white");
        root.put("groups", JSON.emptyObject());
        root.put("groups.selectable", JSON.emptyObject());
        root.put("groups.selectable.colorHover", "yellow");
        root.put("groups.selectable.colorSelect", "red");
        root.put("groups.tinted", JSON.emptyObject());
        root.put("groups.tinted.color", "#555");
        root.put("useGamePadForNavigation", false);
        root.put("useKeyboardForNavigation", false);
        root.put("transition", JSON.emptyObject());
        root.put("transition.time", 1);
        root.put("transition.name", "default");
        log.debug("LevelEditor:" + root.toString(3));
        try
        {
            ui = root.deserializeTo(Page.class);
            ui.init(PAGE_NAME);
            PageManager.instance().putPage(ui.name, new Tup2<Page, JSON>(ui, root));
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    
    private void addTextField(JSON children, String fieldName, String height, boolean isNumber, boolean isTitle, String prefix, boolean isToggle)
    {
        JSON key = JSON.emptyObject();
        key.put("id", "key_" + fieldName.toLowerCase());
        key.put("isEnabled", false);
        key.put("width", UI_STATUS_KEY + "%");
        key.put("height", height);
        key.put("text", fieldName);
        if (!isTitle)
            key.put("textAlignment", "right");
        
        children.put(key);
        
        JSON value = JSON.emptyObject();
        value.put("id", prefix + fieldName.toLowerCase());
        if (!isTitle && !isToggle)
            value.put("type", "textbox");
        else if (isToggle)
            value.put("type", "toggle");
        
        value.put("textAlignment", "top-left");
        if (!isToggle)
        {
            value.put("width", UI_STATUS_VALUE + "%");
            value.put("height", height);
        } else
        {
            // value.put("height", (UI_STATUS_VALUE * 0.25f) + "%");
            value.put("width", 30);
            value.put("height", 30);
            value.put("onColor", "red");
            value.put("onIcon", "check");
        }
        if (isNumber)
            value.put("allowed", "1234567890");
        if (!isTitle)
        {
            value.put("groups", JSON.emptyArray());
            value.put("groups.#", "selectable");
            value.put("groups.#", "tinted");
            value.put("onSelectStart", prefix + fieldName.toLowerCase() + "_selectStart");
        } else
        {
            value.put("isEnabled", false);
        }
        
        children.put(value);
    }
    
    
    private void addTextField(JSON children, String fieldName, String height, boolean isNumber, boolean isTitle, String prefix)
    {
        addTextField(children, fieldName, height, isNumber, isTitle, prefix, false);
    }
    
    
    private void subscribeToEvents()
    {
        for (String tileName : tileTypes.keySet())
        {
            PageManager.instance().subscribeToEvent(this, PAGE_NAME, "tile_" + tileName + "_selectStart");
        }
        
        ((ToggleElement) ui.getElementById("page_grid")).toggle(true);
        ((ToggleElement) ui.getElementById("page_sprites")).toggle(true);
        toggleBetweenSpritesAndColors();
        ((ToggleElement) ui.getElementById("page_ids")).toggle(true);
        ((TextBoxElement) ui.getElementById("page_snap")).textEntered = this;
        String[] pageEvent = new String[] { "grid", "sprites", "ids" };
        for (String pageEventName : pageEvent)
        {
            PageManager.instance().subscribeToEvent(this, PAGE_NAME, "page_" + pageEventName + "_selectStart");
        }
        pageEvent = new String[] { "parent", "id", "x", "y", "z", "width", "height", "tiledwidth", "tiledheight" };
        for (String statusEventName : pageEvent)
        {
            ((TextBoxElement) ui.getElementById("element_" + statusEventName)).textEntered = this;
        }
        pageEvent = new String[] { "tiled" };
        for (String pageEventName : pageEvent)
        {
            PageManager.instance().subscribeToEvent(this, PAGE_NAME, "element_" + pageEventName + "_selectStart");
        }
        pageEvent = new String[] { "name", "id" };
        for (String statusEventName : pageEvent)
        {
            ((TextBoxElement) ui.getElementById("trigger_" + statusEventName)).textEntered = this;
        }
        pageEvent = new String[] { "open", "save", "save_as", "play" };
        for (String optionsName : pageEvent)
        {
            Winger.ui.subscribeToEvent(this, PAGE_NAME, "option_" + optionsName + "_selectStart");
        }
        ((ToggleElement) ui.getElementById("option_select")).toggle(true);
        selectedOptionToggle = "select";
        for (String optionsName : optionsToggles)
        {
            Winger.ui.subscribeToEvent(this, PAGE_NAME, "option_" + optionsName + "_selectStart");
        }
    }
    
    
    private void toggleBetweenSpritesAndColors()
    {
        for (String tileName : tileTypes.keySet())
        {
            ToggleElement e = (ToggleElement) ui.getElementById("tile_" + tileName);
            Tileable t = tileTypes.get(tileName);
            if (showSprites && t.getTileSprite() != null)
            {
                e.offTexture(t.getTileSprite().getTexture(0));
                e.onTexture(t.getTileSprite().getTexture(0));
                e.offColor(Color.WHITE);
                e.toggle(e.isToggled());
            } else
            {
                e.offTexture(Winger.texture.getTexture("white"));
                e.onTexture(Winger.texture.getTexture("white"));
                e.offColor(tileTypes.get(tileName).getTileColor());
                e.toggle(e.isToggled());
            }
            
        }
    }
    
    
    public void updateUIStatus()
    {
        if (currentTile == null)
        {
            ui.getElementById("element_parent").text("");
            ui.getElementById("element_id").text("");
            ui.getElementById("element_type").text("");
            ui.getElementById("element_x").text("");
            ui.getElementById("element_y").text("");
            ui.getElementById("element_z").text("");
            ui.getElementById("element_width").text("");
            ui.getElementById("element_height").text("");
            ((ToggleElement) ui.getElementById("element_tiled")).toggle(false);
            ui.getElementById("element_tiledwidth").text("");
            ui.getElementById("element_tiledheight").text("");
            ui.getElementById("trigger_name").text("");
            ui.getElementById("trigger_id").text("");
        } else
        {
            ui.getElementById("element_parent").text(currentTile.parent);
            ui.getElementById("element_id").text(currentTile.id);
            ui.getElementById("element_type").text(currentTile.type);
            ui.getElementById("element_x").text("" + currentTile.rect.x);
            ui.getElementById("element_y").text("" + currentTile.rect.y);
            ui.getElementById("element_z").text("" + currentTile.zIndex);
            ui.getElementById("element_width").text("" + currentTile.rect.width);
            ui.getElementById("element_height").text("" + currentTile.rect.height);
            ((ToggleElement) ui.getElementById("element_tiled")).toggle(currentTile.tiled);
            ui.getElementById("element_tiledwidth").text("" + currentTile.tiledWidth);
            ui.getElementById("element_tiledheight").text("" + currentTile.tiledHeight);
            ui.getElementById("trigger_name").text(currentTile.triggerName);
            ui.getElementById("trigger_id").text(currentTile.triggerId);
        }
    }
    
    
    public boolean inUIZone(Vector2 position)
    {
        return ui.getElementById("tileScroll").getAbsoluteBoundingBox().contains(position)
            || ui.getElementById("options").getAbsoluteBoundingBox().contains(position)
            || ui.getElementById("statusWrapper").getAbsoluteBoundingBox().contains(position);
    }
    
    
    // /////////////////////////////////////////////
    // Event Handler Methods
    // /////////////////////////////////////////////
    @Override
    public void handleEvent(Object sender, String pageName, String eventName)
    {
        if (eventName.startsWith("tile_") && eventName.endsWith("_selectStart"))
        {
            for (String tileName : tileTypes.keySet())
            {
                if (eventName.equalsIgnoreCase("tile_" + tileName + "_selectStart"))
                {
                    if (((ToggleElement) ui.getElementById("tile_" + tileName)).isToggled())
                    {
                        currentPaintTile = tileName;
                    } else
                    {
                        currentPaintTile = null;
                    }
                } else
                {
                    ((ToggleElement) ui.getElementById("tile_" + tileName)).toggle(false);
                }
            }
        } else if (eventName.startsWith("option_") && eventName.endsWith("_selectStart"))
        {
            for (String optionToggle : optionsToggles)
            {
                if (eventName.equalsIgnoreCase("option_" + optionToggle + "_selectStart"))
                {
                    if (((ToggleElement) ui.getElementById("option_" + optionToggle)).isToggled())
                    {
                        selectedOptionToggle = optionToggle;
                    } else
                    {
                        selectedOptionToggle = null;
                    }
                } else
                {
                    ((ToggleElement) ui.getElementById("option_" + optionToggle)).toggle(false);
                }
            }
        } else if (eventName.equalsIgnoreCase("page_grid_selectStart"))
        {
            ToggleElement gridElement = ((ToggleElement) ui.getElementById("page_grid"));
            showGrid = gridElement.isToggled();
        } else if (eventName.equalsIgnoreCase("page_sprites_selectStart"))
        {
            ToggleElement spritesElement = ((ToggleElement) ui.getElementById("page_sprites"));
            showSprites = spritesElement.isToggled();
            toggleBetweenSpritesAndColors();
        } else if (eventName.equalsIgnoreCase("page_ids_selectStart"))
        {
            ToggleElement idsElement = ((ToggleElement) ui.getElementById("page_ids"));
            showIds = idsElement.isToggled();
        } else if (eventName.equalsIgnoreCase("element_tiled_selectStart"))
        {
            if (currentTile != null)
            {
                ToggleElement tiledElement = ((ToggleElement) ui.getElementById("element_tiled"));
                currentTile.tiled = tiledElement.isToggled();
                updateCurrentTile();
            }
        }
    }
    
    
    @Override
    public void handleClickEvent(CMouse mouse, CMouseButton button, ButtonState state)
    {
        if (ui.isEnabled && !inUIZone(mouse.position()))
        {
            uiHasFocus = false;
            Vector2 mouseWorldPos = VectorMath.toVector2(camera.unproject(VectorMath.toVector3(mouse.x(), Gdx.graphics.getHeight() - mouse.y())));
            if (state == ButtonState.DOWN
                && ("select".equals(selectedOptionToggle) || "move".equals(selectedOptionToggle) || "stretch".equals(selectedOptionToggle)))
            {
                setCurrentTile(selectTile(mouseWorldPos));
            } else if (state == ButtonState.UP && "delete".equals(selectedOptionToggle))
            {
                Tile selectedTile = null;
                if (currentTile != null && currentTile.rect.contains(mouseWorldPos))
                {
                    selectedTile = currentTile;
                }
                if (selectedTile == null)
                {
                    selectedTile = selectTile(mouseWorldPos);
                }
                if (selectedTile != null)
                {
                    deleteTile(selectedTile);
                }
            }
        } else
        {
            uiHasFocus = true;
        }
    }
    
    
    @Override
    public void handleScrollEvent(CMouse mouse, float difference)
    {
        if (ui.isEnabled && !inUIZone(mouse.position()))
        {   
            
        }
    }
    
    
    @Override
    public void handleMoveEvent(CMouse mouse, Vector2 position)
    {
        // TODO Auto-generated method stub
        
    }
    
    
    @Override
    public void handleDragEvent(CMouse mouse, CMouseButton button, CMouseDrag state)
    {
        if (ui.isEnabled && !inUIZone(mouse.getLastDownPosition(button)))
        {
            Vector2 startScreenPos = mouse.getLastDownPosition(button);
            Vector2 startWorldPos = VectorMath.toVector2(camera.unproject(VectorMath.toVector3(startScreenPos.x, Gdx.graphics.getHeight()
                - startScreenPos.y)));
            Vector2 endScreenPos = mouse.position();
            Vector2 endWorldPos = VectorMath.toVector2(camera.unproject(VectorMath.toVector3(endScreenPos.x, Gdx.graphics.getHeight()
                - endScreenPos.y)));
            if (state == CMouseDrag.BEGIN && "add".equals(selectedOptionToggle) && currentPaintTile != null)
            {
                setCurrentTile(createTile(startWorldPos));
                creatingTile = true;
                log.debug("DRAG start " + mouse.getLastDownPosition(button));
            } else if (state == CMouseDrag.MOVE && creatingTile)
            {
                if (currentTile != null)
                {
                    currentTile.rect = getSnappedRectangle(startWorldPos, endWorldPos, snapAmount);
                }
            } else if (state == CMouseDrag.END && creatingTile)
            {
                log.debug("DRAG end " + mouse.position());
                creatingTile = false;
            }
            
            if (state == CMouseDrag.BEGIN && "move".equals(selectedOptionToggle))
            {
                Tile selectedTile = null;
                if (currentTile != null && currentTile.rect.contains(startWorldPos))
                {
                    selectedTile = currentTile;
                }
                if (selectedTile == null)
                {
                    selectedTile = selectTile(startWorldPos);
                }
                if (selectedTile != null)
                {
                    currentTile = selectedTile;
                    movingTile = true;
                }
            } else if (state == CMouseDrag.MOVE && movingTile)
            {
                if (currentTile != null)
                {
                    Vector2 pos = getSnappedPosition(endWorldPos, snapAmount);
                    Vector2 diff = new Vector2(pos.x - currentTile.rect.x, pos.y - currentTile.rect.y);
                    currentTile.rect.x = pos.x;
                    currentTile.rect.y = pos.y;
                    updateChildren(currentTile, diff.x, diff.y);
                }
            } else if (state == CMouseDrag.END && movingTile)
            {
                movingTile = false;
            }
            
            if (state == CMouseDrag.BEGIN && "stretch".equals(selectedOptionToggle))
            {
                Tile selectedTile = null;
                if (currentTile != null && currentTile.rect.contains(startWorldPos))
                {
                    selectedTile = currentTile;
                }
                if (selectedTile == null)
                {
                    selectedTile = selectTile(startWorldPos);
                }
                if (selectedTile != null)
                {
                    currentTile = selectedTile;
                    stretchingTile = true;
                    getStretchingOrientation(currentTile.rect, startWorldPos);
                    originalStretchingPosition = new Vector2(currentTile.rect.x, currentTile.rect.y).add(new Vector2(currentTile.rect.width - 1,
                        currentTile.rect.height - 1).scl(stretchingOrientation));
                }
            } else if (state == CMouseDrag.MOVE && stretchingTile)
            {
                if (currentTile != null)
                {
                    currentTile.rect = getSnappedRectangle(originalStretchingPosition, endWorldPos, snapAmount);
                }
            } else if (state == CMouseDrag.END && stretchingTile)
            {
                stretchingTile = false;
            }
        }
    }
    
    
    @Override
    public void handleKeyEvent(CKeyboard keyboard, KeyboardKey key, ButtonState state)
    {
        if ((key == KeyboardKey.BACKSPACE || key == KeyboardKey.DELETE) && state == ButtonState.DOWN && currentTile != null && !uiHasFocus)
        {
            deleteTile(currentTile);
        } else if ((key == KeyboardKey.LEFT_CONTROL || key == KeyboardKey.RIGHT_CONTROL) && state == ButtonState.DOWN)
        {
            lastSelectedOptionToggle = selectedOptionToggle;
            ((ToggleElement) ui.getElementById("option_add")).toggle(true);
            handleEvent(this, ui.name, "option_add_selectStart");
        } else if ((key == KeyboardKey.LEFT_CONTROL || key == KeyboardKey.RIGHT_CONTROL) && state == ButtonState.UP)
        {
            if (lastSelectedOptionToggle != null)
            {
                ((ToggleElement) ui.getElementById("option_" + lastSelectedOptionToggle)).toggle(true);
                handleEvent(this, ui.name, "option_" + lastSelectedOptionToggle + "_selectStart");
            } else
            {
                ((ToggleElement) ui.getElementById("option_" + selectedOptionToggle)).toggle(true);
            }
        } else if ((key == KeyboardKey.LEFT_SHIFT || key == KeyboardKey.RIGHT_SHIFT) && state == ButtonState.DOWN)
        {
            lastSelectedOptionToggle = selectedOptionToggle;
            ((ToggleElement) ui.getElementById("option_stretch")).toggle(true);
            handleEvent(this, ui.name, "option_stretch_selectStart");
        } else if ((key == KeyboardKey.LEFT_SHIFT || key == KeyboardKey.RIGHT_SHIFT) && state == ButtonState.UP)
        {
            if (lastSelectedOptionToggle != null)
            {
                ((ToggleElement) ui.getElementById("option_" + lastSelectedOptionToggle)).toggle(true);
                handleEvent(this, ui.name, "option_" + lastSelectedOptionToggle + "_selectStart");
            } else
            {
                ((ToggleElement) ui.getElementById("option_" + selectedOptionToggle)).toggle(true);
            }
        } else if ((key == KeyboardKey.LEFT_ALT || key == KeyboardKey.RIGHT_ALT) && state == ButtonState.DOWN)
        {
            lastSelectedOptionToggle = selectedOptionToggle;
            ((ToggleElement) ui.getElementById("option_move")).toggle(true);
            handleEvent(this, ui.name, "option_move_selectStart");
        } else if ((key == KeyboardKey.LEFT_ALT || key == KeyboardKey.RIGHT_ALT) && state == ButtonState.UP)
        {
            if (lastSelectedOptionToggle != null)
            {
                ((ToggleElement) ui.getElementById("option_" + lastSelectedOptionToggle)).toggle(true);
                handleEvent(this, ui.name, "option_" + lastSelectedOptionToggle + "_selectStart");
            } else
            {
                ((ToggleElement) ui.getElementById("option_" + selectedOptionToggle)).toggle(true);
            }
        } else if (key == KeyboardKey.X && state == ButtonState.DOWN)
        {
            lastSelectedOptionToggle = selectedOptionToggle;
            ((ToggleElement) ui.getElementById("option_delete")).toggle(true);
            handleEvent(this, ui.name, "option_delete_selectStart");
        } else if (key == KeyboardKey.X && state == ButtonState.UP)
        {
            if (lastSelectedOptionToggle != null)
            {
                ((ToggleElement) ui.getElementById("option_" + lastSelectedOptionToggle)).toggle(true);
                handleEvent(this, ui.name, "option_" + lastSelectedOptionToggle + "_selectStart");
            } else
            {
                ((ToggleElement) ui.getElementById("option_" + selectedOptionToggle)).toggle(true);
            }
        } else if (key == KeyboardKey.Z && state == ButtonState.DOWN)
        {
            lastSelectedOptionToggle = selectedOptionToggle;
            ((ToggleElement) ui.getElementById("option_select")).toggle(true);
            handleEvent(this, ui.name, "option_select_selectStart");
        } else if (key == KeyboardKey.Z && state == ButtonState.UP)
        {
            if (lastSelectedOptionToggle != null)
            {
                ((ToggleElement) ui.getElementById("option_" + lastSelectedOptionToggle)).toggle(true);
                handleEvent(this, ui.name, "option_" + lastSelectedOptionToggle + "_selectStart");
            } else
            {
                ((ToggleElement) ui.getElementById("option_" + selectedOptionToggle)).toggle(true);
            }
        }
    }
    
    
    @Override
    public void textEntered(String oldText, String newText)
    {
        updateCurrentTile();
    }
}
