package com.winger.draw.texture;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.winger.log.HTMLLogger;
import com.winger.log.LogGroup;
import com.winger.struct.JSON;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages textures using the name field. Allows for atlas loading.
 *
 * @author Michael P. Wingfield
 * @copyright Nov 4, 2014
 *
 */
public class TextureManager
{
    private static final String[] supportedImageFormats = new String[]{"png", "jpeg", "gif"};
    private static final String atlasMetadataExtension = "atlasmd";
    private static HTMLLogger log = HTMLLogger.getLogger(TextureManager.class, LogGroup.Graphics, LogGroup.Util, LogGroup.Framework);
    private static TextureManager instance;
    private Map<String, CTexture> textures = new HashMap<String, CTexture>();

    private TextureManager() {

    }

    public static TextureManager instance()
    {
        if (instance == null)
        {
            instance = new TextureManager();
        }
        return instance;
    }
    
    public void loadTextureAtlas(String atlasLocation, String atlasName)
    {
        log.debug("AtlasLocation: " + atlasLocation + " atlasName: " + atlasName);
        FileHandle rootFolder = Gdx.files.internal(atlasLocation);
        log.debug("Finding files at: " + rootFolder.path());
        FileHandle[] fileList = rootFolder.list();
        FileHandle atlasTexture = null;
        FileHandle atlasData = null;
        TextureAtlas ta = null;
        boolean isLibGdxTextureAtlas = false;
        for (FileHandle fh : fileList)
        {
            log.debug("FileHandle: " + fh.path());
            if (!fh.isDirectory() && fh.nameWithoutExtension().equals(atlasName))
            {
                String ext = fh.extension();
                if (ext.equals(atlasMetadataExtension))
                {
                    atlasData = fh;
                    log.debug("Found atlasData: " + fh);
                } else if (isImageFileTypeSupported(ext))
                {
                    atlasTexture = fh;
                    log.debug("Found atlasTexture: " + fh);
                } else if (ext.equalsIgnoreCase("atlas"))
                {
                    ta = new TextureAtlas(fh);
                    isLibGdxTextureAtlas = true;
                    break;
                }
                if (atlasTexture != null && atlasData != null)
                {
                    break;
                }
            }
        }
        if (!isLibGdxTextureAtlas)
        {
            if (atlasTexture == null || atlasData == null)
            {
                log.error("One or both file handles could not be found");
                throw new IllegalArgumentException("Atlas texture and metadata files did not exist at the given location: " + atlasLocation + " -> "
                    + atlasName);
            }
            try
            {
                Texture tex = new Texture(atlasTexture);
                JSON data = JSON.parse(atlasData.readString());
                log.debug("Metadata: " + data.toString(3));
                for (Object o : (JSON) data.get("metadata"))
                {
                    JSON d = (JSON) o;
                    CTexture ct = new CTexture();
                    ct.texture = tex;
                    ct.name = d.get("name");
                    ct.location = new Rectangle(((Double) d.get("x")).intValue(), ((Double) d.get("y")).intValue(),
                        ((Double) d.get("width")).intValue(), ((Double) d.get("height")).intValue());
                    textures.put(ct.name, ct);
                    log.debug("Create CTexture: " + ct);
                }
            } catch (Exception e)
            {
                log.error("Error in creating atlas texture: " + e);
            }
        } else
        {
            Array<AtlasRegion> ars = ta.getRegions();
            for (AtlasRegion ar : ars)
            {
                Texture tex = ar.getTexture();
                CTexture ct = new CTexture();
                ct.texture = tex;
                ct.name = ar.name;
                ct.location = new Rectangle(ar.getRegionX(), ar.getRegionY(), ar.packedWidth, ar.packedHeight);
                textures.put(ct.name, ct);
                log.debug("Create CTexture: " + ct);
            }
        }
    }
    
    
    public void loadTexturesInDirectory(FileHandle directory)
    {
        if (directory.isDirectory())
        {
            log.debug("Loading images in directory: " + directory.path());
            FileHandle[] fileList = directory.list();
            List<FileHandle> directories = new ArrayList<FileHandle>();
            for (FileHandle fh : fileList)
            {
                if (fh.isDirectory())
                {
                    directories.add(fh);
                } else if (isImageFileTypeSupported(fh.extension()))
                {
                    try
                    {
                        Texture tex = new Texture(fh);
                        CTexture ct = new CTexture();
                        ct.texture = tex;
                        ct.name = fh.nameWithoutExtension();
                        ct.location = new Rectangle(0, 0, tex.getWidth(), tex.getHeight());
                        textures.put(ct.name, ct);
                        log.debug("Create CTexture: " + ct);
                    } catch (Exception e)
                    {
                        log.error("Error in creating texture: " + fh.path());
                    }
                }
            }
            
            // load images in sub directories
            for (FileHandle fh : directories)
            {
                loadTexturesInDirectory(fh);
            }
        }
    }
    
    
    public void loadTexturesInDirectory(String pathToDirectory)
    {
        loadTexturesInDirectory(Gdx.files.internal(pathToDirectory));
    }
    
    
    public CTexture getTexture(String name)
    {
        if (textures.containsKey(name))
        {
            return textures.get(name);
        } else
        {
            return null;
        }
    }
    
    
    public List<CTexture> getTextureGroup(String name)
    {
        List<CTexture> texGroup = new ArrayList<CTexture>();
        if (name.contains(","))
        {
            String[] names = name.split(",");
            for (String n : names)
            {
                CTexture tex = getTexture(n.trim());
                if (tex != null)
                    texGroup.add(tex);
            }
        } else
        {
            int index = 1;
            CTexture tex = getTexture(name + index);
            while (tex != null)
            {
                texGroup.add(tex);
                index++;
                tex = getTexture(name + index);
            }
        }
        return texGroup;
    }
    
    
    public boolean isImageFileTypeSupported(String extension)
    {
        for (int i = 0; i < supportedImageFormats.length; i++)
        {
            if (supportedImageFormats[i].equals(extension))
            {
                return true;
            }
        }
        return false;
    }
}
