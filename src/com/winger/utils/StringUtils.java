package com.winger.utils;

/**
 * Common string modifiers
 *
 * @author Michael P. Wingfield
 * @copyright Nov 4, 2014
 *
 */
public class StringUtils
{
    /**
     * Removes the characters from the original string that fall between the startIndex(inclusive) and the endIndex(exclusive)
     * 
     * @param originalStr
     * @param startIndex
     * @param endIndex
     * @return
     */
    public static String remove(String originalStr, int startIndex, int endIndex)
    {
        return originalStr.substring(0, startIndex) + originalStr.substring(endIndex, originalStr.length());
    }
    
    
    /**
     * Inserts the strToInsert into the originalStr at the position index
     * 
     * @param originalStr
     * @param index
     * @param strToInsert
     * @return
     */
    public static String insert(String originalStr, int index, String strToInsert)
    {
        if (originalStr == null)
            originalStr = "";
        return originalStr.substring(0, index) + strToInsert + originalStr.substring(index, originalStr.length());
    }
}
