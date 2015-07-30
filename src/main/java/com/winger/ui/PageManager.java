package com.winger.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.winger.draw.texture.CSpriteBatch;
import com.winger.log.HTMLLogger;
import com.winger.log.LogGroup;
import com.winger.struct.JSON;
import com.winger.struct.Tups.Tup2;
import com.winger.ui.delegate.PageEventHandler;
import com.winger.utils.Notifiers;
import com.winger.utils.SubscriptionRecords;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages all of the UI pages loaded from config files. Can be used to manually transition between pages, get nested page elements, update, draw, and
 * control sprite batches
 *
 * @author Michael P. Wingfield
 * @copyright Nov 4, 2014
 *
 */
public class PageManager extends Notifiers.Notifier3<PageEventHandler, String, String>
{
    private static final HTMLLogger log = HTMLLogger.getLogger(PageManager.class, LogGroup.Framework, LogGroup.GUI, LogGroup.UI);
    private static final String pageFileExtension = "ui";
    private static PageManager instance;
    private Map<String, Tup2<Page, JSON>> pages = new HashMap<String, Tup2<Page, JSON>>();
    private CSpriteBatch uiSpriteBatch = new CSpriteBatch();
    
    
    private PageManager()
    {}

    public static PageManager instance()
    {
        if (instance == null)
        {
            instance = new PageManager();
        }
        return instance;
    }
    
    /**
     * Searches in a directory for files with a .ui extension and tries to parse them into Page objects
     * 
     * @param directory
     */
    public void addPagesInDirectory(FileHandle directory)
    {
        if (directory.isDirectory())
        {
            log.debug("Adding pages in directory: " + directory.path());
            FileHandle[] fileList = directory.list();
            List<FileHandle> directories = new ArrayList<FileHandle>();
            for (FileHandle fh : fileList)
            {
                if (fh.isDirectory())
                {
                    directories.add(fh);
                } else if (pageFileExtension.equals(fh.extension()))
                {
                    log.debug("Adding page: " + fh.name());
                    Page pageObj;
                    try
                    {
                        JSON pageJSON = JSON.parse(fh.readString());
                        pageObj = pageJSON.deserializeTo(Page.class);
                        pageObj.init(fh.nameWithoutExtension());
                        putPage(fh.nameWithoutExtension(), new Tup2<Page, JSON>(pageObj, pageJSON));
                    } catch (IOException e)
                    {
                        log.debug("Failed on " + fh.path());
                        e.printStackTrace();
                    }
                }
            }
            
            // add subdirectories
            for (FileHandle fh : directories)
            {
                addPagesInDirectory(fh);
            }
        }
    }
    
    
    /**
     * Searches in a directory for files with a .ui extension and tries to parse them into Page objects
     * 
     * @param pathToDirectory
     */
    public void addPagesInDirectory(String pathToDirectory)
    {
        addPagesInDirectory(Gdx.files.internal(pathToDirectory));
    }
    
    
    /**
     * Manually puts a page under a given page name
     * 
     * @param pageName
     * @param page
     */
    public void putPage(String pageName, Tup2<Page, JSON> page)
    {
        pages.put(pageName, page);
        page.i1().name = pageName;
    }
    
    
    /**
     * Re-initializes the given page
     * 
     * @param pageName
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonParseException
     */
    public void restartpage(String pageName) throws IOException
    {
        if (pages.containsKey(pageName))
        {
            JSON pageJSON = pages.get(pageName).i2();
            Page restartedPage = pageJSON.deserializeTo(Page.class);
            restartedPage.name = pageName;
            pages.put(pageName, new Tup2<Page, JSON>(restartedPage, pageJSON));
        }
    }
    
    
    /**
     * Gets a page under the given page name
     * 
     * @param pageName
     * @return
     */
    public Page getPage(String pageName)
    {
        if (pages.containsKey(pageName))
        {
            return pages.get(pageName).i1();
        }
        return null;
    }
    
    
    /**
     * Removes a page with the given page name
     * 
     * @param pageName
     * @return
     */
    public boolean removePage(String pageName)
    {
        if (pages.containsKey(pageName))
        {
            pages.remove(pageName);
            return true;
        }
        return false;
    }
    
    
    /**
     * Calls transitionOff and transitionOn for the current page and the given page respectively
     * 
     * @param page
     */
    public void transitionToPage(Page page)
    {
        if (page == null)
        {
            return;
        }
        log.debug("Transition to page: " + page.name);
        
        for (String key : pages.keySet())
        {
            Page curPage = getPage(key);
            if (curPage.isEnabled || curPage.isTransitioning)
            {
                // set that page to transition off
                if (curPage.getTransitionObj() != null)
                {
                    curPage.getTransitionObj().transitionOff();
                }
            }
        }
        
        // tell the page to transition on
        if (page.getTransitionObj() != null)
        {
            page.getTransitionObj().transitionOn();
        }
    }
    
    
    /**
     * Calls transitionOff and transitionOn for the current page and the given page respectively
     * 
     * @param pageName
     */
    public void transitionToPage(String pageName)
    {
        transitionToPage(getPage(pageName));
    }
    
    
    /**
     * Gets an element by id under a given page
     * 
     * @param pageName
     * @param elementId
     * @return
     */
    public Element<?> getElement(String pageName, String elementId)
    {
        Element<?> e = null;
        Page p = getPage(pageName);
        if (p != null)
        {
            e = p.getElementById(elementId);
        }
        return e;
    }
    
    
    /**
     * Manually calls an event on a page with the given page name and event name
     * 
     * @param sender
     * @param pageName
     * @param eventName
     */
    public void notifySubscribers(Object sender, String pageName, String eventName)
    {
        List<SubscriptionRecords.SubscriptionRecord3<PageEventHandler, String, String>> subsToNotify = getSubscribersToNotify(pageName, eventName);
        for (SubscriptionRecords.SubscriptionRecord3<PageEventHandler, String, String> sub : subsToNotify)
        {
            sub.handler.handleEvent(sender, pageName, eventName);
        }
    }
    
    
    /**
     * Calls update on all pages
     */
    public void update()
    {
        for (String key : pages.keySet())
        {
            getPage(key).update();
        }
    }
    
    
    /**
     * Gets the sprite batch used by the pages
     * 
     * @return
     */
    public CSpriteBatch uiSpriteBatch()
    {
        return uiSpriteBatch;
    }
    
    
    /**
     * Sets the sprite batch used by the pages
     * 
     * @param spriteBatch
     */
    public void setUISpriteBatch(CSpriteBatch spriteBatch)
    {
        uiSpriteBatch = spriteBatch;
    }
    
    
    /**
     * Calls draw on all pages
     */
    public void draw()
    {
        for (String key : pages.keySet())
        {
            getPage(key).draw(uiSpriteBatch);
        }
    }
}
