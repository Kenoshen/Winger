package com.winger.utils;

import com.badlogic.gdx.files.FileHandle;
import com.winger.log.HTMLLogger;
import com.winger.log.LogGroup;
import com.winger.struct.JSON;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Will parse files with .json or .config extensions and put them into a simple JSON object
 *
 * Created by mwingfield on 8/4/15.
 */
public class Config {
    private final static String DEFAULT_CONFIG_PATH = "./project.config";
    private final static HTMLLogger log = HTMLLogger.getLogger(Config.class, LogGroup.System, LogGroup.Framework, LogGroup.Util);

    private FileHandle fileHandle;
    private Map<String, Object> data;

    public Config(){
        this(DEFAULT_CONFIG_PATH, false);
    }

    public Config(boolean watch){
        this(DEFAULT_CONFIG_PATH, watch);
    }

    public Config(String fileLocation, boolean watch){
        this(new FileHandle(new File(fileLocation)), watch);
    }

    public Config(FileHandle fileHandle, boolean watch){
        if (fileHandle == null){
            throw new RuntimeException("FileHandle cannot be null");
        } else if(! fileHandle.exists()){
            throw new RuntimeException("File does not exist: " + fileHandle.file().getAbsolutePath());
        } else if (fileHandle.isDirectory() || (!"json".equals(fileHandle.extension()) && !"config".equals(fileHandle.extension()))){
            throw new RuntimeException("Config file at " + fileHandle.path() + " must be either .config or .json");
        }
        this.fileHandle = fileHandle;
        parse();
        if (watch){
            watch();
        }
    }

    private void parse(){
        parse(fileHandle);
    }

    public void parse(FileHandle fileHandle){
        if (data == null){
            data = new HashMap<>();
        }
        if (fileHandle.exists() && !fileHandle.isDirectory()){
            if ("json".equals(fileHandle.extension())){
                JSON fileData = JSON.parse(fileHandle.readString());
                Map<String, Object> flat = fileData.flatten();
                data.putAll(flat);
            } else if ("config".equals(fileHandle.extension())){
                String fileData = fileHandle.readString();
                String[] lines = fileData.split("\n");
                for (String line : lines){
                    String[] pair = line.split("=");
                    String key = null;
                    String val = null;
                    if (pair.length > 1){
                        key = pair[0];
                        val = pair[1];
                    }
                    if (key != null && val != null){
                        key = key.trim();
                        val = val.trim();
                        try {
                            data.put(key, Double.parseDouble(val));
                        } catch (Exception e){
                            if ("true".equalsIgnoreCase(val) || "false".equalsIgnoreCase(val)){
                                data.put(key, Boolean.parseBoolean(val));
                            } else {
                                data.put(key, val);
                            }
                        }
                    }
                }
            }
        }

        log.debug("Config: " + data);
    }

    public <T> T get(String path){
        if (data.containsKey(path)){
            Object o = data.get(path);
            return (T) o;
        } else {
            throw new RuntimeException("Config value does not exist: " + path);
        }
    }

    public <T> T get(String path, Class<T> type){
        if (data.containsKey(path)){
            Object o = data.get(path);
            return type.cast(o);
        } else {
            throw new RuntimeException("Config value does not exist: " + path);
        }
    }

    private void watch(){
        WatchUtils.watchForModify(fileHandle, new WatchUtils.WatchFunc() {
            @Override
            public void trigger() {
                log.info("Parse Config File again");
                parse();
            }
        });
    }
}
