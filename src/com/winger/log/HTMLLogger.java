package com.winger.log;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Logging class that outputs to html as well as normal console output. Can be configured with log levels and group tags that can color log statements
 * in a browser
 *
 * @author Michael P. Wingfield
 * @copyright Nov 4, 2014
 *
 */
@SuppressWarnings("rawtypes")
public class HTMLLogger
{
    // //////////////////////////////////////////////////////////////////////////////
    // Static class
    // //////////////////////////////////////////////////////////////////////////////
    protected static List<HTMLLogger> loggers = new ArrayList<HTMLLogger>();
    protected static LogLevel globalLogLevel = LogLevel.Info;
    protected static OutputStream globalOut = System.out;
    protected static OutputStream globalHTMLOut = null;
    protected static String htmlLogHead = "<html><head><style>body{background-color: black;}msg{display: block;border-radius: 3px;font-family: monospace;padding-top: 5px;padding-bottom: 5px;}msg *{padding-right: 10px;}millis{display: none;}groups{display: none;}group{display: inline;padding-left: 10px;padding-right: 10px;}stack{display: none;padding-left: 20px;}ste{display: block;}.info{background-color: #ccf;color: #007;}.info:hover{background-color: #77f;cursor: pointer;}.error{background-color: #fcc;color: #700;}.error:hover{background-color: #f77;cursor: pointer;}.warn{background-color: #ffc;color: #770;}.warn:hover{background-color: #ff7;cursor: pointer;}.debug{background-color: #cfc;color: #070;}.debug:hover{background-color: #7f7;cursor: pointer;}</style><script>window.onload = function(){var msgElems = document.getElementsByTagName(\"msg\");for (var i = 0; i < msgElems.length; i++){msgElems[i].onclick = function(){var elem = this.getElementsByTagName(\"groups\")[0];if (elem !== undefined){if (elem.style.display !== 'none' && elem.style.display !== ''){elem.style.display = 'none';}else{elem.style.display = 'block';}}elem = this.getElementsByTagName(\"stack\")[0];if (elem !== undefined){if (elem.style.display !== 'none' && elem.style.display !== ''){elem.style.display = 'none';}else{elem.style.display = 'block';}}};}};</script></head><body>";
    
    
    // //////////////////////////////////////
    // Factory methods
    // //////////////////////////////////////
    /**
     * Builds a new logger class
     * 
     * @param clazz the class containing the logger
     * @return
     */
    public static HTMLLogger getLogger(Class clazz)
    {
        HTMLLogger log = new HTMLLogger(clazz);
        loggers.add(log);
        log.setOutputStream(globalOut);
        log.setHTMLOutputStream(globalHTMLOut);
        return log;
    }
    
    
    /**
     * Builds a new logger class
     * 
     * @param clazz the class containing the logger
     * @param logLevel the log level of the logger (defaults to global log level)
     * @return
     */
    public static HTMLLogger getLogger(Class clazz, LogLevel logLevel)
    {
        HTMLLogger log = getLogger(clazz);
        log.setLogLevel(logLevel);
        return log;
    }
    
    
    /**
     * Builds a new logger class
     * 
     * @param clazz the class containing the logger
     * @param logLevel the log level of the logger (defaults to global log level)
     * @param groups grouping tags for the html logging
     * @return
     */
    public static HTMLLogger getLogger(Class clazz, LogLevel logLevel, LogGroup... groups)
    {
        HTMLLogger log = getLogger(clazz, logLevel);
        log.addTags(groups);
        return log;
    }
    
    
    /**
     * Builds a new logger class
     * 
     * @param clazz the class containing the logger
     * @param groups grouping tags for the html logging
     * @return
     */
    public static HTMLLogger getLogger(Class clazz, LogGroup... groups)
    {
        HTMLLogger log = getLogger(clazz);
        log.addTags(groups);
        return log;
    }
    
    
    // //////////////////////////////////////
    // Getters and Setters
    // //////////////////////////////////////
    /**
     * Gets the global log level which trickles down to any loggers with log levels set to Default
     */
    public static LogLevel getGlobalLogLevel()
    {
        return globalLogLevel;
    }
    
    
    /**
     * Sets the global log level which will trickle down to any loggers with log levels set to Default
     * 
     * @param logLevel
     */
    public static void setGlobalLogLevel(LogLevel logLevel)
    {
        if (logLevel == LogLevel.Default)
        {
            logLevel = LogLevel.Info;
        }
        globalLogLevel = logLevel;
    }
    
    
    /**
     * Sets the global output stream object, changes the output stream for all loggers. Default is System.out
     * 
     * @param os
     */
    public static void setGlobalOutputStream(OutputStream os)
    {
        globalOut = os;
        for (HTMLLogger logger : loggers)
        {
            logger.setOutputStream(globalOut);
        }
    }
    
    
    /**
     * Sets the global html output stream object, changes the html output stream for all loggers
     * 
     * @param os
     */
    public static void setGlobalHTMLOutputStream(OutputStream os)
    {
        globalHTMLOut = os;
        if (globalHTMLOut != null)
        {
            try
            {
                globalHTMLOut.write(htmlLogHead.getBytes());
            } catch (IOException e)
            {}
        }
        for (HTMLLogger logger : loggers)
        {
            logger.setHTMLOutputStream(globalHTMLOut);
        }
    }
    
    
    // //////////////////////////////////////////////////////////////////////////////
    // Instanced class
    // //////////////////////////////////////////////////////////////////////////////
    protected Class clazz;
    protected OutputStream out;
    protected OutputStream htmlOut;
    //
    protected LogLevel logLevel = LogLevel.Default;
    protected Set<LogGroup> groups = new HashSet<LogGroup>();
    //
    protected StringBuilder sb = null;
    protected StringBuilder buffer = new StringBuilder();
    //
    protected SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    
    
    protected HTMLLogger(Class clazz)
    {
        this.clazz = clazz;
    }
    
    
    // //////////////////////////////////////
    // Getters and Setters
    // //////////////////////////////////////
    /**
     * Gets the log level of the logger
     * 
     * @return
     */
    public LogLevel getLogLevel()
    {
        return logLevel;
    }
    
    
    /**
     * Sets the log level of the logger
     * 
     * @param logLevel
     * @return
     */
    public HTMLLogger setLogLevel(LogLevel logLevel)
    {
        this.logLevel = logLevel;
        return this;
    }
    
    
    /**
     * Adds a group to the logger
     * 
     * @param group
     * @return
     */
    public HTMLLogger addGroup(LogGroup group)
    {
        groups.add(group);
        return this;
    }
    
    
    /**
     * Adds the groups to the logger
     * 
     * @param groupArray
     * @return
     */
    public HTMLLogger addTags(LogGroup... groupArray)
    {
        if (groupArray != null && groupArray.length > 0)
        {
            for (LogGroup group : groupArray)
            {
                addGroup(group);
            }
        }
        return this;
    }
    
    
    /**
     * Removes a group from the logger
     * 
     * @param group
     * @return
     */
    public HTMLLogger removeGroup(LogGroup group)
    {
        if (groups.contains(group))
        {
            groups.remove(group);
        }
        return this;
    }
    
    
    /**
     * Gets a copy of the list of groups, adding or removing from this list does not affect the logger
     * 
     * @return
     */
    public List<LogGroup> getGroups()
    {
        List<LogGroup> ts = new ArrayList<LogGroup>();
        ts.addAll(groups);
        return ts;
    }
    
    
    /**
     * Sets the plain text output stream object that this logger writes to
     * 
     * @param os
     * @return
     */
    protected HTMLLogger setOutputStream(OutputStream os)
    {
        out = os;
        return this;
    }
    
    
    /**
     * Sets the html output stream object that this logger writes to
     * 
     * @param os
     * @return
     */
    protected HTMLLogger setHTMLOutputStream(OutputStream os)
    {
        htmlOut = os;
        return this;
    }
    
    
    // //////////////////////////////////////
    // Logging helper methods
    // //////////////////////////////////////
    /**
     * Gets the StackTraceElement that is responsible for the log call
     * 
     * @return
     */
    protected StackTraceElement getSTE()
    {
        StackTraceElement[] stackTraces = Thread.currentThread().getStackTrace();
        for (int i = 1; i < stackTraces.length; i++)
        {
            if (stackTraces[i].getClassName() != this.getClass().getName())
            {
                return stackTraces[i];
            }
        }
        return null;
    }
    
    
    /**
     * Gets a list of StackTraceElements leading up to the call to the log
     * 
     * @return
     */
    protected List<StackTraceElement> getStackTrace()
    {
        StackTraceElement[] stackTraces = Thread.currentThread().getStackTrace();
        List<StackTraceElement> elems = new ArrayList<StackTraceElement>();
        for (int i = 1; i < stackTraces.length; i++)
        {
            if (stackTraces[i].getClassName() != this.getClass().getName())
            {
                elems.add(stackTraces[i]);
            }
        }
        return elems;
    }
    
    
    /**
     * Gets a time string
     * 
     * @return
     */
    protected String getTime()
    {
        return sdf.format(new Date(System.currentTimeMillis()));
    }
    
    
    /**
     * Logs the actual message to the OutputStreams
     * 
     * @param msg
     * @param type
     */
    protected void log(String msg, LogLevel level)
    {
        LogLevel tempLL = logLevel;
        if (tempLL == LogLevel.Default)
        {
            tempLL = HTMLLogger.getGlobalLogLevel();
        }
        if (tempLL.shouldLog(level))
        {
            try
            {
                if (out != null)
                {
                    out.write(("[" + getTime() + "][" + level + (level == LogLevel.Info || level == LogLevel.Warn ? " " : "") + "]["
                        + clazz.getName() + "](" + getSTE().getLineNumber() + "): " + msg + "\n").getBytes());
                    if (level == LogLevel.Error)
                    {
                        List<StackTraceElement> st = getStackTrace();
                        for (StackTraceElement ste : st)
                        {
                            out.write(("\t" + ste + "\n").getBytes());
                        }
                    }
                }
                if (htmlOut != null)
                {
                    sb = new StringBuilder();
                    sb.append("<msg class='").append(level).append(" ").append(clazz.getName()).append(" ");
                    for (LogGroup group : groups)
                    {
                        sb.append(group).append(" ");
                    }
                    sb.append("'>");
                    sb.append("<time>").append(getTime()).append("</time>");
                    sb.append("<level>").append(level + (level == LogLevel.Info || level == LogLevel.Warn ? " " : "")).append("</level>");
                    sb.append("<clazz>").append(clazz.getName()).append("</clazz>");
                    sb.append("<line>").append(getSTE().getLineNumber()).append("</line>");
                    sb.append("<text>").append(msg.replace("\n", "<br />")).append("</text>");
                    sb.append("<millis>").append(System.currentTimeMillis()).append("</millis>");
                    sb.append("<groups>");
                    for (LogGroup group : groups)
                    {
                        sb.append("<group>").append(group).append("</group>");
                    }
                    sb.append("</groups>");
                    if (level == LogLevel.Error)
                    {
                        sb.append("<stack>");
                        List<StackTraceElement> st = getStackTrace();
                        for (StackTraceElement ste : st)
                        {
                            sb.append("<ste>").append(ste).append("</ste>");
                        }
                        sb.append("</stack>");
                    }
                    sb.append("</msg>\n");
                    htmlOut.write(sb.toString().getBytes());
                }
            } catch (IOException e)
            {}
        }
    }
    
    
    /**
     * Logs the actual message to the OutputStreams
     * 
     * @param msg
     * @param type
     */
    protected void log(String msg, LogLevel level, Exception e)
    {
        LogLevel tempLL = logLevel;
        if (tempLL == LogLevel.Default)
        {
            tempLL = HTMLLogger.getGlobalLogLevel();
        }
        if (tempLL.shouldLog(level))
        {
            try
            {
                if (out != null)
                {
                    out.write(("[" + getTime() + "][" + level + (level == LogLevel.Info || level == LogLevel.Warn ? " " : "") + "]["
                        + clazz.getName() + "](" + getSTE().getLineNumber() + "): " + msg + "\n").getBytes());
                    out.write((e + "\n").getBytes());
                    List<StackTraceElement> st = getStackTrace();
                    for (StackTraceElement ste : st)
                    {
                        out.write(("\t" + ste + "\n").getBytes());
                    }
                }
                if (htmlOut != null)
                {
                    sb = new StringBuilder();
                    sb.append("<msg class='").append(level).append(" ").append(clazz.getName()).append(" ");
                    for (LogGroup group : groups)
                    {
                        sb.append(group).append(" ");
                    }
                    sb.append("'>");
                    sb.append("<time>").append(getTime()).append("</time>");
                    sb.append("<level>").append(level + (level == LogLevel.Info || level == LogLevel.Warn ? " " : "")).append("</level>");
                    sb.append("<clazz>").append(clazz.getName()).append("</clazz>");
                    sb.append("<line>").append(getSTE().getLineNumber()).append("</line>");
                    sb.append("<text>").append(msg.replace("\n", "<br />")).append("</text>");
                    sb.append("<millis>").append(System.currentTimeMillis()).append("</millis>");
                    sb.append("<groups>");
                    for (LogGroup group : groups)
                    {
                        sb.append("<group>").append(group).append("</group>");
                    }
                    sb.append("</groups>");
                    if (level == LogLevel.Error)
                    {
                        sb.append("<stack>");
                        List<StackTraceElement> st = getStackTrace();
                        for (StackTraceElement ste : st)
                        {
                            sb.append("<ste>").append(ste).append("</ste>");
                        }
                        sb.append("</stack>");
                    }
                    sb.append("</msg>\n");
                    htmlOut.write(sb.toString().getBytes());
                }
            } catch (IOException ioe)
            {}
        }
    }
    
    
    // //////////////////////////////////////
    // Public logging methods
    // //////////////////////////////////////
    /**
     * Appends the string to the buffer. Basically like using string builder to build a string. Use flush____() to log the message.
     * 
     * @param s
     * @return
     */
    public HTMLLogger buffer(String s)
    {
        if (s != null)
        {
            buffer.append(s);
        } else
        {
            buffer.append("null");
        }
        return this;
    }
    
    
    /**
     * Appends the object to the buffer. Basically like using string builder to build a string. Use flush____() to log the message.
     * 
     * @param o
     * @return
     */
    public HTMLLogger buffer(Object o)
    {
        if (o != null)
        {
            buffer.append(o.toString());
        } else
        {
            buffer.append("null");
        }
        return this;
    }
    
    
    /**
     * Logs message with Info tag
     * 
     * @param s
     */
    public HTMLLogger info(String s)
    {
        if (s != null)
        {
            log(s, LogLevel.Info);
        } else
        {
            log("null", LogLevel.Info);
        }
        return this;
    }
    
    
    /**
     * Logs message with Info tag
     * 
     * @param o
     */
    public HTMLLogger info(Object o)
    {
        if (o == null)
        {
            log("null", LogLevel.Info);
        } else
        {
            log(o.toString(), LogLevel.Info);
        }
        return this;
    }
    
    
    /**
     * Logs message with Info tag
     * 
     * @param s
     */
    public HTMLLogger info(String s, Exception e)
    {
        if (s != null)
        {
            log(s, LogLevel.Info, e);
        } else
        {
            log("null", LogLevel.Info, e);
        }
        return this;
    }
    
    
    /**
     * Logs message with Info tag
     * 
     * @param o
     */
    public HTMLLogger info(Object o, Exception e)
    {
        if (o == null)
        {
            log("null", LogLevel.Info, e);
        } else
        {
            log(o.toString(), LogLevel.Info, e);
        }
        return this;
    }
    
    
    /**
     * Flushes the buffer as an info log message
     * 
     * @return
     */
    public HTMLLogger flushInfo()
    {
        info(buffer);
        buffer = new StringBuilder();
        return this;
    }
    
    
    /**
     * Logs message with Debug tag
     * 
     * @param s
     */
    public HTMLLogger debug(String s)
    {
        if (s != null)
        {
            log(s, LogLevel.Debug);
        } else
        {
            log("null", LogLevel.Debug);
        }
        return this;
    }
    
    
    /**
     * Logs message with Debug tag
     * 
     * @param o
     */
    public HTMLLogger debug(Object o)
    {
        if (o == null)
        {
            log("null", LogLevel.Debug);
        } else
        {
            log(o.toString(), LogLevel.Debug);
        }
        return this;
    }
    
    
    /**
     * Logs message with Debug tag
     * 
     * @param s
     */
    public HTMLLogger debug(String s, Exception e)
    {
        if (s != null)
        {
            log(s, LogLevel.Debug, e);
        } else
        {
            log("null", LogLevel.Debug, e);
        }
        return this;
    }
    
    
    /**
     * Logs message with Debug tag
     * 
     * @param o
     */
    public HTMLLogger debug(Object o, Exception e)
    {
        if (o == null)
        {
            log("null", LogLevel.Debug, e);
        } else
        {
            log(o.toString(), LogLevel.Debug, e);
        }
        return this;
    }
    
    
    /**
     * Flushes the buffer as a debug log message
     * 
     * @return
     */
    public HTMLLogger flushDebug()
    {
        debug(buffer);
        buffer = new StringBuilder();
        return this;
    }
    
    
    /**
     * Logs message with Warn tag
     * 
     * @param s
     */
    public HTMLLogger warn(String s)
    {
        if (s != null)
        {
            log(s, LogLevel.Warn);
        } else
        {
            log("null", LogLevel.Warn);
        }
        return this;
    }
    
    
    /**
     * Logs message with Warn tag
     * 
     * @param o
     */
    public HTMLLogger warn(Object o)
    {
        if (o == null)
        {
            log("null", LogLevel.Warn);
        } else
        {
            log(o.toString(), LogLevel.Warn);
        }
        return this;
    }
    
    
    /**
     * Logs message with Warn tag
     * 
     * @param s
     */
    public HTMLLogger warn(String s, Exception e)
    {
        if (s != null)
        {
            log(s, LogLevel.Warn, e);
        } else
        {
            log("null", LogLevel.Warn, e);
        }
        return this;
    }
    
    
    /**
     * Logs message with Warn tag
     * 
     * @param o
     */
    public HTMLLogger warn(Object o, Exception e)
    {
        if (o == null)
        {
            log("null", LogLevel.Warn, e);
        } else
        {
            log(o.toString(), LogLevel.Warn, e);
        }
        return this;
    }
    
    
    /**
     * Flushes the buffer as a warn log message
     * 
     * @return
     */
    public HTMLLogger flushWarn()
    {
        warn(buffer);
        buffer = new StringBuilder();
        return this;
    }
    
    
    /**
     * Logs message with Error tag
     * 
     * @param s
     */
    public HTMLLogger error(String s)
    {
        if (s != null)
        {
            log(s, LogLevel.Error);
        } else
        {
            log("null", LogLevel.Error);
        }
        return this;
    }
    
    
    /**
     * Logs message with Error tag
     * 
     * @param o
     */
    public HTMLLogger error(Object o)
    {
        if (o == null)
        {
            log("null", LogLevel.Error);
        } else
        {
            log(o.toString(), LogLevel.Error);
        }
        return this;
    }
    
    
    /**
     * Logs message with Error tag
     * 
     * @param s
     */
    public HTMLLogger error(String s, Exception e)
    {
        if (s != null)
        {
            log(s, LogLevel.Error, e);
        } else
        {
            log("null", LogLevel.Error, e);
        }
        return this;
    }
    
    
    /**
     * Logs message with Error tag
     * 
     * @param o
     */
    public HTMLLogger error(Object o, Exception e)
    {
        if (o == null)
        {
            log("null", LogLevel.Error, e);
        } else
        {
            log(o.toString(), LogLevel.Error, e);
        }
        return this;
    }
    
    
    /**
     * Flushes the buffer as an error log message
     * 
     * @return
     */
    public HTMLLogger flushError()
    {
        error(buffer);
        buffer = new StringBuilder();
        return this;
    }
}
