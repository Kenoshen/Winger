package com.winger.log;

/**
 * Enum for log level
 *
 * @author Michael P. Wingfield
 * @copyright Nov 4, 2014
 *
 */
public enum LogLevel
{
    Error(4), Warn(3), Info(2), Debug(1), Default(0);
    
    private int value = 0;


    LogLevel(int value)
    {
        this.value = value;
    }
    
    
    /**
     * The relative level of the LogLevel object. A level of 0 means everything, the higher the number the more exclusions.
     * 
     * @return
     */
    public int level()
    {
        return value;
    }
    
    
    /**
     * Compares the LogLevel to a LogLevel of a message to determine if the message should be logged
     * 
     * @param logMessageLevel
     * @return
     */
    public boolean shouldLog(LogLevel logMessageLevel)
    {
        return (this.level() <= logMessageLevel.level());
    }
}
