package com.winger.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * String parsing methods for reading hex values, decoding colors, and breaking down vector strings
 *
 * @author Michael P. Wingfield
 * @copyright Nov 4, 2014
 *
 */
public class ParseUtils
{
    public static final String[] HexChars = new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F" };
    
    
    /**
     * Parses a hex string into it's integer value
     * 
     * @param hex
     * @return
     */
    public static int hexValue(String hex)
    {
        if (hex == null)
            return -1;
        
        hex = hex.replace("#", "").toUpperCase();
        
        if (hex.length() % 2 != 0)
            hex = "0" + hex;
        
        if (hex.length() > 8)
            return -1;
        
        int val = 0;
        int power = 0;
        for (int i = hex.length() - 1; i >= 0; i--)
        {
            for (int k = 0; k < HexChars.length; k++)
            {
                if (hex.charAt(i) == HexChars[k].charAt(0))
                {
                    val += k * (int) Math.pow(16, power);
                }
            }
            power++;
        }
        return val;
    }
    
    
    /**
     * Parses a color string into it's Color object value<br /> Ex: blue, lightred, #001122, #00112233, #012, #0123, rgb(0, 1, 0.5), rgba(0, 1, 0.5,
     * 1)
     * 
     * @param colorStr
     * @return
     */
    public static Color decodeColor(String colorStr)
    {
        return decodeColor(colorStr, Color.WHITE.cpy());
    }
    
    
    /**
     * Parses a color string into it's Color object value<br /> Ex: blue, lightred, #001122, #00112233, #012, #0123, rgb(0, 1, 0.5), rgba(0, 1, 0.5,
     * 1)
     * 
     * @param colorStr
     * @param defaultColor
     * @return
     */
    public static Color decodeColor(String colorStr, Color defaultColor)
    {
        if (colorStr == null)
            return defaultColor;
        
        colorStr = colorStr.toLowerCase();
        
        float red = -1;
        float green = -1;
        float blue = -1;
        float alpha = 1;
        if (colorStr.contains("#"))
        {
            colorStr = colorStr.substring(1);
            String redHex = "";
            String greenHex = "";
            String blueHex = "";
            String alphaHex = "";
            if (colorStr.length() == 3 || colorStr.length() == 4)
            {
                redHex = colorStr.substring(0, 0 + 1);
                redHex += redHex;
                greenHex = colorStr.substring(1, 1 + 1);
                greenHex += greenHex;
                blueHex = colorStr.substring(2, 2 + 1);
                blueHex += blueHex;
                if (colorStr.length() == 4)
                {
                    alphaHex = colorStr.substring(3, 3 + 1);
                    alphaHex += alphaHex;
                    alpha = ((float) hexValue(alphaHex)) / 255f;
                }
            } else if (colorStr.length() == 6 || colorStr.length() == 8)
            {
                redHex = colorStr.substring(0, 0 + 2);
                greenHex = colorStr.substring(2, 2 + 2);
                blueHex = colorStr.substring(4, 4 + 2);
                if (colorStr.length() == 8)
                {
                    alphaHex = colorStr.substring(6, 6 + 2);
                    alpha = ((float) hexValue(alphaHex)) / 255f;
                }
            } else
            {
                throw new IllegalArgumentException("Unsupported format for color: " + colorStr);
            }
            red = ((float) hexValue(redHex)) / 255f;
            green = ((float) hexValue(greenHex)) / 255f;
            blue = ((float) hexValue(blueHex)) / 255f;
        } else if (colorStr.contains("rgb"))
        {
            colorStr = colorStr.substring(4, 4 + 5);
            String[] colorArray = colorStr.split(",");
            red = Float.parseFloat(colorArray[0]);
            green = Float.parseFloat(colorArray[1]);
            blue = Float.parseFloat(colorArray[2]);
            if (colorArray.length == 4)
                alpha = Float.parseFloat(colorArray[3]);
        } else
        {
            if ("red".equals(colorStr))
            {
                red = 1;
            } else if ("darkred".equals(colorStr))
            {
                red = 0.3f;
            } else if ("orange".equals(colorStr))
            {
                red = 250f / 255f;
                green = 120f / 255f;
            } else if ("yellow".equals(colorStr))
            {
                red = 250f / 255f;
                green = 240f / 255f;
            } else if ("limegreen".equals(colorStr))
            {
                red = 140f / 255f;
                green = 240f / 255f;
            } else if ("yellowgreen".equals(colorStr))
            {
                red = 140f / 255f;
                green = 240f / 255f;
            } else if ("lightgreen".equals(colorStr))
            {
                red = 140f / 255f;
                green = 240f / 255f;
            } else if ("green".equals(colorStr))
            {
                green = 1;
            } else if ("darkgreen".equals(colorStr))
            {
                green = 0.3f;
            } else if ("indigo".equals(colorStr))
            {
                green = 240f / 255f;
                blue = 240f / 255f;
            } else if ("teal".equals(colorStr))
            {
                green = 240f / 255f;
                blue = 240f / 255f;
            } else if ("lightblue".equals(colorStr))
            {
                green = 220f / 255f;
                blue = 240f / 255f;
            } else if ("blue".equals(colorStr))
            {
                blue = 1;
            } else if ("darkblue".equals(colorStr))
            {
                blue = 0.3f;
            } else if ("pink".equals(colorStr))
            {
                red = 220f / 255f;
                blue = 240f / 255f;
            } else if ("purple".equals(colorStr))
            {
                red = 120f / 255f;
                green = 20f / 255f;
                blue = 160f / 255f;
            } else if ("black".equals(colorStr))
            {
                red = 0;
                green = 0;
                blue = 0;
            } else if ("white".equals(colorStr))
            {
                red = 1;
                green = 1;
                blue = 1;
            }
        }
        
        if (red == -1 && green == -1 && blue == -1)
            return defaultColor;
        else
        {
            if (red == -1)
                red = 0;
            if (green == -1)
                green = 0;
            if (blue == -1)
                blue = 0;
        }
        
        return new Color(red, green, blue, alpha);
    }
    
    
    /**
     * Parses a vector2 from a string<br /> Ex: (0.12, 3.5)
     * 
     * @param vectorStr
     * @return
     */
    public static Vector2 vector2FromString(String vectorStr)
    {
        float[] vals = vectorFromString(vectorStr);
        if (vals == null)
            return new Vector2(0, 0);
        return new Vector2(vals[0], vals[1]);
    }
    
    
    /**
     * Parses a vector2 from a string<br /> Ex: (0.12, 3.5, 5)
     * 
     * @param vectorStr
     * @return
     */
    public static Vector3 vector3FromString(String vectorStr)
    {
        float[] vals = vectorFromString(vectorStr);
        if (vals == null)
            return Vector3.Zero;
        return new Vector3(vals[0], vals[1], vals[2]);
    }
    
    
    /**
     * Parses a vector2 from a string<br /> Ex: (0.12, 3.5, 5, 3.2, 8, 4.2, ...)
     * 
     * @param vectorStr
     * @return
     */
    public static float[] vectorFromString(String vectorStr)
    {
        if (vectorStr == null)
            return null;
        
        vectorStr = vectorStr.replace("(", "").replace(")", "");
        String[] vals = vectorStr.split(",|;|/|\\|");
        float[] values = new float[] { 0, 0, 0, 0 };
        for (int i = 0; i < vals.length && i < 4; i++)
        {
            try
            {
                values[i] = Float.parseFloat(vals[i]);
            } catch (Exception e)
            {}
        }
        return values;
    }
}
