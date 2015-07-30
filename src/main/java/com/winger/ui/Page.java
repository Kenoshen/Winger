package com.winger.ui;

import com.badlogic.gdx.math.Vector2;
import com.winger.draw.texture.CSpriteBatch;
import com.winger.input.delegate.CGamePadEventHandler;
import com.winger.input.delegate.CKeyboardEventHandler;
import com.winger.input.delegate.CMouseEventHandler;
import com.winger.input.raw.CGamePad;
import com.winger.input.raw.CKeyboard;
import com.winger.input.raw.CMouse;
import com.winger.input.raw.state.*;
import com.winger.log.HTMLLogger;
import com.winger.log.LogGroup;
import com.winger.math.VectorMath;
import com.winger.struct.CRectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A page is a fundamental part of using the Winger.UI framework. It is modeled after HTML pages where you have an HTML document that represents the
 * Page as a whole, then under that document there are nested UI elements that are laid out on the screen by using their positional and size data as
 * well as that of their parents. Same concept, just using JSON instead of HTML/CSS and slightly different naming conventions. The PageManager is
 * almost like the internet browser in this analogy.
 *
 * @author Michael P. Wingfield
 * @copyright Nov 4, 2014
 *
 */
public class Page implements CMouseEventHandler, CGamePadEventHandler, CKeyboardEventHandler
{
    private static final HTMLLogger log = HTMLLogger.getLogger(Page.class, LogGroup.Framework, LogGroup.GUI, LogGroup.UI);
    //
    public String name;
    public boolean isEnabled;
    public boolean isVisible;
    public boolean isTransitioning;
    public boolean isDebug;
    public boolean useKeyboardForNavigation;
    public boolean useGamePadForNavigation;
    public String onTransitionOffStart;
    public String onTransitionOffEnd;
    public String onTransitionOnStart;
    public String onTransitionOnEnd;
    public TransitionRecord transition;
    public List<ElementRecord> elements;
    public Variables variables;
    public Groups groups;
    public Settings global;
    //
    public CMouse mouse = CMouse.instance;
    public CKeyboard keyboard = new CKeyboard();
    public CGamePad gamePad = new CGamePad();
    private Transition transitionObj;
    private List<Element<?>> elementObjs;
    //
    private Element<?> currentHoveredElement = null;
    @SuppressWarnings("unused")
    private Element<?> currentSelectedElement = null;
    private Element<?> currentHasFocusElement = null;
    private Element<?> currentDraggedElement = null;
    
    
    public Page()
    {
        registerForMouseEvents();
        registerForKeyboardEvents();
        registerForGamePadEvents();
        elementObjs = new ArrayList<Element<?>>();
    }
    
    
    public void init(String name)
    {
        this.name = name;
        // get transition
        if (transition != null)
        {
            Transition t = TransitionManager.instance().getTransition(transition.name);
            if (t != null)
            {
                transitionObj = t.clone(this);
                transitionObj.millisecondsToLive = transition.time;
            } else
            {
                transitionObj = TransitionManager.instance().getDefaultTransition().clone(this);
            }
        }
        // parse elements
        for (ElementRecord elementRecord : elements)
        {
            addElement(elementRecord, null);
        }
    }
    
    
    @SuppressWarnings("unchecked")
    public void addElement(ElementRecord elementRecord, Element<?> parent)
    {
        // add globals, groups, and variables
        if (groups != null)
        {
            for (String group : groups.keySet())
            {
                if (elementRecord.containsKey("groups"))
                {
                    if (((List<String>) elementRecord.get("groups")).contains(group))
                    {
                        for (String key : groups.get(group).keySet())
                        {
                            if (!elementRecord.containsKey(key))
                            {
                                elementRecord.put(key, groups.get(group).get(key));
                            }
                        }
                    }
                }
            }
        }
        if (global != null)
        {
            for (String key : global.keySet())
            {
                if (!elementRecord.containsKey(key))
                {
                    elementRecord.put(key, global.get(key));
                }
            }
        }
        if (variables != null)
        {
            for (String var : variables.keySet())
            {
                for (String key : elementRecord.keySet())
                {
                    if (elementRecord.get(key) instanceof String)
                    {
                        String val = (String) elementRecord.get(key);
                        if (val.matches("\\$!<" + var + ">"))
                        {
                            elementRecord.put(key, variables.get(var));
                        }
                    }
                }
            }
        }
        //
        Element<?> e = ElementFactory.createElement(this, elementRecord, parent);
        addElement(e);
        if (parent != null)
        {
            parent.children().add(e);
        }
        //
        if (elementRecord.containsKey("children") && elementRecord.get("children") != null)
        {
            List<Map<String, Object>> children = (List<Map<String, Object>>) elementRecord.get("children");
            for (Map<String, Object> child : children)
            {
                ElementRecord childRecord = new ElementRecord();
                childRecord.putAll(child);
                addElement(childRecord, e);
            }
        }
        e.afterChildrenInitialize();
    }
    
    
    public void addElement(Element<?> e)
    {
        elementObjs.add(e);
    }
    
    
    // ///////////////////////////////////////////
    // Render methods
    // ///////////////////////////////////////////
    /**
     * Calls update on transitions and inputs
     */
    public void update()
    {
        if (isTransitioning)
        {
            if (transitionObj != null)
            {
                transitionObj.update();
            }
        }
        if (isEnabled)
        {
            mouse.update();
            keyboard.update();
            if (gamePad.isConnected())
            {
                gamePad.update();
            }
        }
    }
    
    
    /**
     * Calls draw on all visible elements
     * 
     * @param spriteBatch
     */
    public void draw(CSpriteBatch spriteBatch)
    {
        if (isVisible)
        {
            for (Element<?> element : elementObjs)
            {
                element.draw(spriteBatch);
                if (isDebug)
                    element.debugDraw(spriteBatch);
            }
        }
    }
    
    
    // ///////////////////////////////////////////
    // Get element methods
    // ///////////////////////////////////////////
    /**
     * Gets an element with a given id using a depth-first recursive search
     * 
     * @param id
     * @return
     */
    public Element<?> getElementById(String id)
    {
        if (id == null)
        {
            return null;
        }
        for (Element<?> elem : elementObjs)
        {
            if (id.equals(elem.id()))
            {
                return elem;
            }
        }
        return null;
    }
    
    
    // ///////////////////////////////////////////
    // Mouse event methods
    // ///////////////////////////////////////////
    private void registerForMouseEvents()
    {
        for (CMouseButton button : CMouseButton.values())
        {
            mouse.clickNotifier().subscribeToEvent(this, button, ButtonState.DOWN);
            mouse.clickNotifier().subscribeToEvent(this, button, ButtonState.UP);
            mouse.dragNotifier().subscribeToEvent(this, button, CMouseDrag.BEGIN);
            mouse.dragNotifier().subscribeToEvent(this, button, CMouseDrag.END);
        }
        mouse.scrollNotifier().subscribeToEvent(this, CMouseScroll.CHANGED);
        mouse.moveNotifier().subscribeToEvent(this, CMousePosition.CHANGED);
    }
    
    
    /**
     * Determines which elements are affected by a mouse click event and initiates a UI event on those elements
     */
    public void handleClickEvent(CMouse mouse, CMouseButton button, ButtonState state)
    {
        if (!isEnabled || isTransitioning)
        {
            return;
        }
        if (button == CMouseButton.LEFT)
        {
            if (state == ButtonState.DOWN)
            {
                setElementsToNotHaveFocus();
            }
            for (int i = elementObjs.size() - 1; i >= 0; i--)
            {
                Element<?> element = elementObjs.get(i);
                if (element.isEnabled())
                {
                    if (element.isHover())
                    {
                        if (state == ButtonState.DOWN)
                        {
                            currentHasFocusElement = element;
                            currentHasFocusElement.hasFocus(true);
                            initiateUIEvent(element, UIEventType.ON_SELECT_START);
                            break;
                        }
                    } else if (element.isSelected())
                    {
                        if (state == ButtonState.UP)
                        {
                            initiateUIEvent(element, UIEventType.ON_SELECT_END);
                            initiateUIEvent(element, UIEventType.ON_HOVER_START);
                            break;
                        }
                    }
                }
            }
        }
        handleMoveEvent(mouse, mouse.position());
    }
    
    
    /**
     * Determines which elements are affected by a mouse scroll event and initiates a UI event on those elements
     */
    public void handleScrollEvent(CMouse mouse, float difference)
    {
        if (!isEnabled || isTransitioning)
        {
            return;
        }
    }
    
    
    /**
     * Determines which elements are affected by a mouse move event and initiates a UI event on those elements
     */
    public void handleMoveEvent(CMouse mouse, Vector2 position)
    {
        if (!isEnabled || isTransitioning)
        {
            return;
        }
        Vector2 pos = new Vector2(position);
        // if (camera != null)
        // {
        // Vector3 tempV3 = VectorMath.toVector3(pos);
        // camera.unproject(tempV3);
        // pos = VectorMath.toVector2(tempV3);
        // }
        for (int i = elementObjs.size() - 1; i >= 0; i--)
        {
            Element<?> element = elementObjs.get(i);
            if (element.isEnabled())
            {
                CRectangle rect = element.getDrawBoundingBox();
                if (rect.contains(pos))
                {
                    if (!element.isHover() && !element.isSelected())
                    {
                        initiateUIEvent(element, UIEventType.ON_HOVER_START);
                    }
                } else
                {
                    if (element.isHover())
                    {
                        initiateUIEvent(element, UIEventType.ON_HOVER_END);
                    }
                    if (element.isSelected())
                    {
                        element.neutralizeEvent();
                    }
                }
            }
        }
    }
    
    
    /**
     * Determines which elements are affected by a mouse drag event and initiates a UI event on those elements
     */
    public void handleDragEvent(CMouse mouse, CMouseButton button, CMouseDrag state)
    {
        if (!isEnabled || isTransitioning)
        {
            return;
        }
        Vector2 lastMouseDownPosition = mouse.getLastDownPosition(button);
        // if (camera != null)
        // {
        // Vector3 tempV3 = VectorMath.toVector3(lastMouseDownPosition);
        // camera.unproject(tempV3);
        // lastMouseDownPosition = VectorMath.toVector2(tempV3);
        // }
        if (button == CMouseButton.LEFT)
        {
            if (state == CMouseDrag.BEGIN)
            {
                for (Element<?> element : elementObjs)
                {
                    if (element.isEnabled())
                    {
                        if (element.getDrawBoundingBox().contains(lastMouseDownPosition))
                        {
                            currentDraggedElement = element;
                            initiateUIEvent(element, UIEventType.ON_DRAG_START);
                            break;
                        }
                    }
                }
            } else if (state == CMouseDrag.END)
            {
                initiateUIEvent(currentDraggedElement, UIEventType.ON_DRAG_END);
            }
        }
    }
    
    
    // ///////////////////////////////////////////
    // Keyboard event methods
    // ///////////////////////////////////////////
    private void registerForKeyboardEvents()
    {
        // register for keyboard events
        keyboard.subscribeToAllKeyboardEvents(this, ButtonState.DOWN);
    }
    
    
    /**
     * Determines which elements are affected by a keyboard event and initiates a UI event on those elements
     */
    public void handleKeyEvent(CKeyboard keyboard, KeyboardKey key, ButtonState state)
    {
        if (!isEnabled || isTransitioning)
        {
            return;
        }
        if (currentHasFocusElement != null)
        {
            currentHasFocusElement.sendKeyboardInfoToThisElement(keyboard);
        }
        if (useKeyboardForNavigation)
        {
            if (key == KeyboardKey.TAB && state == ButtonState.DOWN)
            {
                if (currentHoveredElement != null)
                {
                    Element<?> element = currentHoveredElement;
                    int i = elementObjs.indexOf(element);
                    initiateUIEvent(element, UIEventType.ON_HOVER_END);
                    element = pickNextEnabledElement(i + 1, elementObjs);
                    initiateUIEvent(element, UIEventType.ON_HOVER_START);
                } else
                {
                    if (elementObjs.size() > 0)
                    {
                        Element<?> element = pickNextEnabledElement(0, elementObjs);
                        initiateUIEvent(element, UIEventType.ON_HOVER_START);
                    }
                }
            } else if ((key == KeyboardKey.ENTER || key == KeyboardKey.SPACE) && state == ButtonState.DOWN)
            {
                setElementsToNotHaveFocus();
                if (currentHoveredElement != null)
                {
                    Element<?> element = currentHoveredElement;
                    currentHasFocusElement = element;
                    currentHasFocusElement.hasFocus(true);
                    initiateUIEvent(element, UIEventType.ON_SELECT_END);
                    initiateUIEvent(element, UIEventType.ON_HOVER_START);
                }
            } else if (key == KeyboardKey.UP || key == KeyboardKey.W || key == KeyboardKey.DOWN || key == KeyboardKey.S || key == KeyboardKey.LEFT
                || key == KeyboardKey.A || key == KeyboardKey.RIGHT || key == KeyboardKey.D)
            {
                if (currentHoveredElement != null)
                {
                    Vector2 direction = new Vector2(0, 0);
                    if (key == KeyboardKey.UP || key == KeyboardKey.W)
                        direction.y = 1;
                    else if (key == KeyboardKey.DOWN || key == KeyboardKey.S)
                        direction.y = -1;
                    else if (key == KeyboardKey.LEFT || key == KeyboardKey.A)
                        direction.x = -1;
                    else if (key == KeyboardKey.RIGHT || key == KeyboardKey.D)
                        direction.x = 1;
                    Element<?> nextElement = intersectElementWithPointAndDirection(currentHoveredElement, direction);
                    if (nextElement != null)
                    {
                        initiateUIEvent(currentHoveredElement, UIEventType.ON_HOVER_END);
                        initiateUIEvent(nextElement, UIEventType.ON_HOVER_START);
                    }
                } else
                {
                    if (elementObjs.size() > 0)
                    {
                        Element<?> element = pickNextEnabledElement(0, elementObjs);
                        initiateUIEvent(element, UIEventType.ON_HOVER_START);
                    }
                }
            }
        }
    }
    
    
    // ///////////////////////////////////////////
    // GamePad event methods
    // ///////////////////////////////////////////
    private void registerForGamePadEvents()
    {
        // register for gamepad events
        for (CGamePadButton button : CGamePadButton.values())
        {
            gamePad.buttonNotifier().subscribeToEvent(this, button, ButtonState.DOWN);
        }
    }
    
    
    /**
     * Determines which elements are affected by a gamepad button event and initiates a UI event on those elements
     */
    public void handleButtonEvent(CGamePad gamePad, CGamePadButton button, ButtonState state)
    {
        if (!isEnabled || isTransitioning || !useGamePadForNavigation)
        {
            return;
        }
        if (button == CGamePadButton.A && state == ButtonState.DOWN)
        {
            setElementsToNotHaveFocus();
            if (currentHoveredElement != null)
            {
                Element<?> element = currentHoveredElement;
                currentHasFocusElement = element;
                currentHasFocusElement.hasFocus(true);
                initiateUIEvent(element, UIEventType.ON_SELECT_END);
                initiateUIEvent(element, UIEventType.ON_HOVER_START);
            }
        } else if (button == CGamePadButton.LS_DOWN || button == CGamePadButton.RS_DOWN || button == CGamePadButton.DOWN
            || button == CGamePadButton.LS_UP || button == CGamePadButton.RS_UP || button == CGamePadButton.UP || button == CGamePadButton.LS_RIGHT
            || button == CGamePadButton.RS_RIGHT || button == CGamePadButton.RIGHT || button == CGamePadButton.LS_LEFT
            || button == CGamePadButton.RS_LEFT || button == CGamePadButton.LEFT)
        {
            if (currentHoveredElement != null)
            {
                Vector2 direction = new Vector2(0, 0);
                if (button == CGamePadButton.LS_DOWN || button == CGamePadButton.RS_DOWN || button == CGamePadButton.DOWN)
                    direction.y = -1;
                else if (button == CGamePadButton.LS_UP || button == CGamePadButton.RS_UP || button == CGamePadButton.UP)
                    direction.y = 1;
                else if (button == CGamePadButton.LS_RIGHT || button == CGamePadButton.RS_RIGHT || button == CGamePadButton.RIGHT)
                    direction.x = 1;
                else if (button == CGamePadButton.LS_LEFT || button == CGamePadButton.RS_LEFT || button == CGamePadButton.LEFT)
                    direction.x = -1;
                Element<?> nextElement = intersectElementWithPointAndDirection(currentHoveredElement, direction);
                if (nextElement != null)
                {
                    initiateUIEvent(currentHoveredElement, UIEventType.ON_HOVER_END);
                    initiateUIEvent(nextElement, UIEventType.ON_HOVER_START);
                }
            } else
            {
                if (elementObjs.size() > 0)
                {
                    Element<?> element = pickNextEnabledElement(0, elementObjs);
                    initiateUIEvent(element, UIEventType.ON_HOVER_START);
                }
            }
        }
    }
    
    
    /**
     * Determines which elements are affected by a gamepad trigger event and initiates a UI event on those elements
     */
    public void handleTriggerEvent(CGamePad gamePad, CGamePadTrigger trigger, float state)
    {
        // probably don't need to worry about trigger events
        if (!isEnabled || isTransitioning)
        {
            return;
        }
    }
    
    
    /**
     * Determines which elements are affected by a gamepad stick event and initiates a UI event on those elements
     */
    public void handleStickEvent(CGamePad gamePad, CGamePadStick stick, Vector2 state)
    {
        // will use the stick button events instead of the stick change events
        if (!isEnabled || isTransitioning)
        {
            return;
        }
    }
    
    
    // ///////////////////////////////////////////
    // Helper methods
    // ///////////////////////////////////////////
    private Element<?> intersectElementWithPointAndDirection(Element<?> originElement, Vector2 direction)
    {
        CRectangle originRect = originElement.getAbsoluteBoundingBox();
        Vector2 origin = new Vector2(originRect.x, originRect.y);
        direction = direction.nor();
        Vector2 dirMax = VectorMath.rotatePointByDegreesAroundZero(direction, 46);
        dirMax = VectorMath.multiply(dirMax, 1000000);
        dirMax.add(origin);
        Vector2 dirMin = VectorMath.rotatePointByDegreesAroundZero(direction, -46);
        dirMin = VectorMath.multiply(dirMin, 1000000);
        dirMin.add(origin);
        
        float closestDistance = 10000;
        Element<?> closestElement = null;
        for (Element<?> element : elementObjs)
        {
            if (!element.equals(originElement) && element.isEnabled())
            {
                CRectangle rect = element.getAbsoluteBoundingBox();
                Vector2 pos = new Vector2(rect.x, rect.y);
                if (VectorMath.isPointInTriangle(origin, dirMax, dirMin, pos))
                {
                    float dist = origin.dst(pos);
                    if (dist < closestDistance && dist != 0)
                    {
                        closestDistance = dist;
                        closestElement = element;
                    }
                }
            }
        }
        return closestElement;
    }
    
    
    private Element<?> pickNextEnabledElement(int startIndex, List<Element<?>> elems)
    {
        int index = startIndex;
        for (int i = 0; i < elems.size(); i++)
        {
            if (index >= elems.size())
            {
                index -= elems.size();
            } else if (index < 0)
            {
                index += elems.size();
            }
            if (elems.get(index) != null && elems.get(index).isEnabled())
            {
                return elems.get(index);
            } else
            {
                index++;
            }
        }
        return null;
    }
    
    
    private void setElementsToNotHaveFocus()
    {
        currentHasFocusElement = null;
        for (Element<?> elem : elementObjs)
        {
            elem.hasFocus(false);
        }
    }
    
    
    private void initiateUIEvent(Element<?> element, UIEventType type)
    {
        if (element != null)
        {
            log.debug("Page(" + name + ") Initiate UI Event (" + type + ") for " + element.id());
            switch (type)
            {
                case ON_HOVER_START:
                    currentHoveredElement = element;
                    element.onHoverStartEvent();
                    if (element.onHoverStart() != null)
                    {
                        // notify subscribers that OnHoverStart event happend
                        PageManager.instance().notifySubscribers(element, name, element.onHoverStart());
                    }
                    break;
                
                case ON_HOVER_END:
                    currentHoveredElement = null;
                    element.onHoverEndEvent();
                    if (element.onHoverEnd() != null)
                    {
                        // notify subscribers that OnHoverEnd event happend
                        PageManager.instance().notifySubscribers(element, name, element.onHoverEnd());
                    }
                    break;
                
                case ON_SELECT_START:
                    currentSelectedElement = element;
                    element.onSelectStartEvent();
                    if (element.onSelectStart() != null)
                    {
                        // notify subscribers that OnSelectStart event happend
                        PageManager.instance().notifySubscribers(element, name, element.onSelectStart());
                    }
                    break;
                
                case ON_SELECT_END:
                    currentSelectedElement = null;
                    element.onSelectEndEvent();
                    if (element.onSelectEnd() != null)
                    {
                        // notify subscribers that OnSelectEnd event happend
                        PageManager.instance().notifySubscribers(element, name, element.onSelectEnd());
                    }
                    
                    // transition to a different page if transition on select is
                    // set
                    if (element.transitionOnSelect() != null)
                    {
                        element.neutralizeEvent();
                        PageManager.instance().transitionToPage(element.transitionOnSelect());
                    }
                    break;
                
                case ON_DRAG_START:
                    element.onDragStartEvent();
                    if (element.onDragStart() != null)
                    {
                        // notify subscribers that OnDragStart event happend
                        PageManager.instance().notifySubscribers(element, name, element.onDragStart());
                    }
                    break;
                
                case ON_DRAG_END:
                    currentDraggedElement = null;
                    element.onDragEndEvent();
                    if (element.onDragEnd() != null)
                    {
                        // notify subscribers that OnDragEnd event happend
                        PageManager.instance().notifySubscribers(element, name, element.onDragEnd());
                    }
                    break;
            }
        }
    }
    
    
    public String toString()
    {
        String s = "{ " + name + " [ ";
        for (Element<?> e : elementObjs)
            s += e + ", ";
        s += "] }";
        return s;
    }
    
    
    // ///////////////////////////////////////////
    // Getters and Setters
    // ///////////////////////////////////////////
    public Transition getTransitionObj()
    {
        return transitionObj;
    }
    
    
    public void setTransitionObj(Transition t)
    {
        transitionObj = t;
    }
    
    
    public List<Element<?>> elements()
    {
        return elementObjs;
    }
}
