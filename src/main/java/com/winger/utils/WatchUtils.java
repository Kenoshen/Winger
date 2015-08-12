package com.winger.utils;

import com.badlogic.gdx.files.FileHandle;
import com.winger.log.HTMLLogger;
import com.winger.log.LogGroup;

import java.io.IOException;
import java.nio.file.*;

/**
 * Created by mwingfield on 8/4/15.
 */
public class WatchUtils {
    private final static HTMLLogger log = HTMLLogger.getLogger(WatchUtils.class, LogGroup.System, LogGroup.Framework, LogGroup.Util);
    private WatchUtils(){}

    public interface WatchFunc {
        void trigger();
    }


    public static void watchForModify(FileHandle fileHandle, final WatchFunc func){
        if (fileHandle == null){
            throw new RuntimeException("File handle cannot be null");
        }
        if (func == null){
            throw new RuntimeException("Watch Function cannot be null");
        }
        if (fileHandle.isDirectory()){
            throw new RuntimeException("Cannot catch modify events on a directory");
        }
        final Path path = FileSystems.getDefault().getPath(fileHandle.parent().file().getAbsolutePath());
        final String fileHandleName = fileHandle.name();
        try{
            final WatchService watchService = FileSystems.getDefault().newWatchService();
            path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
            Thread thread = new Thread() {
                @Override
                public void run() {
                    int tries = 10;
                    log.debug("Watching " + path + " for changes to " + fileHandleName);

                    while (true)
                    {
                        try {
                            WatchKey wk = watchService.take();
                            for (WatchEvent<?> event : wk.pollEvents()) {
                                //we only register "ENTRY_MODIFY" so the context is always a Path.
                                final Path changed = (Path) event.context();
                                log.debug(changed + " has been modified");
                                if (changed.endsWith(fileHandleName)) {
                                    func.trigger();
                                }
                            }
                            // reset the key
                            boolean valid = wk.reset();
                            if (!valid) {
                                log.debug("Key has been unregistered");
                            }
                        } catch(InterruptedException e){
                            e.printStackTrace();
                            tries--;
                            if (tries <= 0){
                                break;
                            }
                        } catch (ClosedWatchServiceException e1){
                            e1.printStackTrace();
                            break;
                        }
                    }
                }
            };
            thread.start();
        } catch (IOException e) {
        }
    }
}
