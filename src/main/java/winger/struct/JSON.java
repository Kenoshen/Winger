package com.winger.struct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.winger.log.HTMLLogger;
import com.winger.log.LogGroup;

/**
 * Parses and allows access to JSON-formatted data structures. At a low level, JSON is basically a HashMap/List of HashMaps. Very useful data type for
 * network communication, configuration, and serializing complex objects
 *
 * @author Michael P. Wingfield
 * @copyright Nov 4, 2014
 *
 */
public class JSON implements Iterable<Object>
{
    private static final HTMLLogger log = HTMLLogger.getLogger(JSON.class, LogGroup.Framework);
    protected boolean isArray = false;
    protected LinkedHashMap<String, Object> map;
    protected List<Object> array;
    //
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final ObjectWriter writer;
    static
    {
        mapper.setSerializationInclusion(Include.NON_NULL);
        mapper.setSerializationInclusion(Include.NON_EMPTY);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, true);
        writer = mapper.writer();
    }
    
    
    /**
     * Initialize a JSON object with a json-formatted string
     * 
     * @param jsonString
     */
    public JSON(String jsonString)
    {
        try
        {
            parseString(jsonString, 0);
        } catch (Exception e)
        {
            log.debug("Error in parsing JSON: ", e);
            throw new IllegalArgumentException(e);
        }
    }
    
    
    /**
     * Gets an object at a given xpath.<br>
     * 
     * Ex:<br> ("books") // gets the object under the property name "books"<br> ("books.0.price") // gets the price of the first book<br> ("books.#")
     * // gets a list of book objects<br> ("books.#.price") // gets a list of price objects
     * 
     * @param xpath xpath (not necessarily fully implemented)
     * @return the object that matches the path, or null if nothing matches
     */
    public <T> T get(String xpath)
    {
        String[] parts = xpath.split("\\.");
        return get(parts);
    }
    
    
    /**
     * Gets an object at a given xpath.<br>
     * 
     * Ex:<br> ("books") // gets the object under the property name "books"<br> ("books.0.price") // gets the price of the first book<br> ("books.#")
     * // gets a list of book objects<br> ("books.#.price") // gets a list of price objects
     * 
     * @param <T>
     * 
     * @param xpath xpath (not necessarily fully implemented)
     * @return the object that matches the path, or null if nothing matches
     */
    public <T> T get(Class<T> clazz, String xpath)
    {
        String[] parts = xpath.split("\\.");
        return get(clazz, parts);
    }
    
    
    /**
     * Gets an object at a given path.<br>
     * 
     * Ex:<br> ("books") // gets the object under the property name "books"<br> ("books", 0, "price") // gets the price of the first book<br>
     * ("books", "#") // gets a list of book objects<br> ("books", "#", "price") // gets a list of price objects
     * 
     * @param parts the path parts
     * @return the object that matches the path, or null if nothing matches
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String... parts)
    {
        return (T) get(parts, 0);
    }
    
    
    /**
     * Gets an object at a given path.<br>
     * 
     * Ex:<br> ("books") // gets the object under the property name "books"<br> ("books", 0, "price") // gets the price of the first book<br>
     * ("books", "#") // gets a list of book objects<br> ("books", "#", "price") // gets a list of price objects
     * 
     * @param parts the path parts
     * @return the object that matches the path, or null if nothing matches
     */
    public <T> T get(Class<T> clazz, String... parts)
    {
        return convert(clazz, get(parts, 0));
    }
    
    
    protected Object get(String[] pathParts, int depth)
    {
        String curPath = pathParts[depth];
        int n = 0;
        if (isArray)
        {
            if (curPath.equals("#"))
            {
                if (depth + 1 >= pathParts.length)
                {
                    return array;
                } else
                {
                    List<Object> objs = new ArrayList<Object>();
                    for (Object o : array)
                    {
                        if (o instanceof JSON)
                        {
                            objs.add(((JSON) o).get(pathParts, depth + 1));
                        }
                    }
                    return objs;
                }
            } else
            {
                try
                {
                    n = Integer.parseInt(curPath);
                    Object o = array.get(n);
                    if (depth + 1 >= pathParts.length)
                    {
                        return o;
                    } else if (o instanceof JSON)
                    {
                        return ((JSON) array.get(n)).get(pathParts, depth + 1);
                    }
                } catch (Exception e)
                {   
                    
                }
            }
        } else
        {
            if (map.containsKey(pathParts[depth]))
            {
                Object o = map.get(pathParts[depth]);
                if (depth + 1 >= pathParts.length)
                {
                    return o;
                } else if (o instanceof JSON)
                {
                    return ((JSON) o).get(pathParts, depth + 1);
                }
            }
        }
        return null;
    }
    
    
    @SuppressWarnings("unchecked")
    private <T> T convert(Class<T> clazz, Object o)
    {
        String name = clazz.getName();
        if (name.equals(Long.class.getName()))
        {
            return (T) new Long(((Number) o).longValue());
        } else if (name.equals(Integer.class.getName()))
        {
            return (T) new Integer(((Number) o).intValue());
        } else if (name.equals(Short.class.getName()))
        {
            return (T) new Short(((Number) o).shortValue());
        } else if (name.equals(Byte.class.getName()))
        {
            return (T) new Byte(((Number) o).byteValue());
        } else if (name.equals(Double.class.getName()))
        {
            return (T) new Double(((Number) o).doubleValue());
        } else if (name.equals(Float.class.getName()))
        {
            return (T) new Float(((Number) o).floatValue());
        }
        return clazz.cast(o);
    }
    
    
    /**
     * Puts an object at a given xpath.<br>
     * 
     * Ex:<br> ("books") // sets the object under the property name "books"<br> ("books.0") // sets the first book<br> ("books.0.price") // sets the
     * price of the first book<br> ("books.#") // adds an object to the list of books
     * 
     * @param xpath xpath (not necessarily fully implemented)
     * @return the object that matches the path, or null if nothing matches
     */
    public JSON put(String xpath, Object val)
    {
        if (xpath == null || xpath.length() == 0)
        {
            log.debug("Did not put because xpath was null or empty");
            return this;
        }
        String key = null;
        Object parent = null;
        String[] parts = xpath.split("\\.");
        if (parts.length > 1)
        {
            String[] newParts = new String[parts.length - 1];
            for (int i = 0; i < newParts.length; i++)
            {
                newParts[i] = parts[i];
            }
            key = parts[parts.length - 1];
            if (newParts.length == 0)
            {
                parent = this;
            } else
            {
                parent = get(newParts, 0);
            }
        } else
        {
            key = parts[0];
            parent = this;
        }
        
        if (parent != null && parent instanceof JSON)
        {
            JSON jParent = (JSON) parent;
            if (jParent.isArray)
            {
                int index = 0;
                if ("#".equals(key))
                {
                    jParent.array.add(val);
                    return this;
                } else
                {
                    try
                    {
                        index = Integer.parseInt(key);
                        jParent.array.set(index, val);
                        return this;
                    } catch (Exception e)
                    {   
                        
                    }
                }
            } else
            {
                jParent.map.put(key, val);
                return this;
            }
        }
        log.debug("Did not put because xpath did not lead anywhere: " + xpath);
        return this;
    }
    
    
    /**
     * Puts an object at the end of the array (only works for array json objects).
     */
    public JSON put(Object val)
    {
        if (isArray)
        {
            array.add(val);
        }
        return this;
    }
    
    
    /**
     * Removes the key at the given xpath
     * 
     * @param xpath
     * @return
     */
    public Object remove(String xpath)
    {
        if (xpath == null || xpath.length() == 0)
        {
            log.debug("Did not put because xpath was null or empty");
            return false;
        }
        String key = null;
        Object parent = null;
        String[] parts = xpath.split("\\.");
        if (parts.length > 1)
        {
            String[] newParts = new String[parts.length - 1];
            for (int i = 0; i < newParts.length; i++)
            {
                newParts[i] = parts[i];
            }
            key = parts[parts.length - 1];
            if (newParts.length == 0)
            {
                parent = this;
            } else
            {
                parent = get(newParts, 0);
            }
        } else
        {
            key = parts[0];
            parent = this;
        }
        
        if (parent != null && parent instanceof JSON)
        {
            JSON jParent = (JSON) parent;
            if (jParent.isArray)
            {
                int index = 0;
                if ("#".equals(key))
                {
                    jParent.array.remove(jParent.array.size() - 1);
                    return true;
                } else
                {
                    try
                    {
                        index = Integer.parseInt(key);
                        jParent.array.remove(index);
                        return true;
                    } catch (Exception e)
                    {   
                        
                    }
                }
            } else
            {
                jParent.map.put(key, null);
                return true;
            }
        }
        log.debug("Did not remove because xpath did not lead anywhere: " + xpath);
        return false;
    }
    
    
    /**
     * Gets the keys/indexes within this object
     * 
     * @return a list of properties
     */
    public List<String> properties()
    {
        if (!isArray)
        {
            List<String> propList = new ArrayList<String>();
            propList.addAll(map.keySet());
            return propList;
        } else
        {
            List<String> indexes = new ArrayList<String>();
            for (int i = 0; i < array.size(); i++)
            {
                indexes.add("" + i);
            }
            return indexes;
        }
    }
    
    
    /**
     * Checks if a property exists
     * 
     * @param xpath the xpath to the property
     * @return true if the property exists
     */
    public boolean hasProperty(String xpath)
    {
        if (xpath == null || xpath.equals(""))
        {
            return false;
        }
        String[] pathParts = xpath.split("\\.");
        return hasProperty(pathParts, 0, this);
    }
    
    
    protected boolean hasProperty(String[] pathParts, int depth, Object cur)
    {
        if (depth >= pathParts.length)
        {
            return true;
        }
        if (cur == null || !(cur instanceof JSON))
        {
            return false;
        }
        JSON curJ = (JSON) cur;
        String property = pathParts[depth];
        if (curJ.properties().contains(property))
        {
            return hasProperty(pathParts, depth + 1, curJ.get(property));
        } else
        {
            return false;
        }
    }
    
    
    /**
     * Gets the count of the number of properties
     * 
     * @return the count of the number of properties
     */
    public int length()
    {
        return properties().size();
    }
    
    
    /**
     * Parses a json string to a new json object
     * 
     * @param jsonString
     * @return
     */
    public static JSON parse(String jsonString)
    {
        return new JSON(jsonString);
    }
    
    
    protected JSON()
    {   
        
    }
    
    
    protected int parseString(String jsonString, int index) throws Exception
    {
        if (jsonString == null)
        {
            throw new Exception("Cannot parse null JSON string.");
        }
        if ("".equals(jsonString))
        {
            throw new Exception("Cannot parse empty JSON string.");
        }
        index = streamToNotWhiteSpace(jsonString, index);
        if (jsonString.charAt(index) == '{')
        {
            isArray = false;
            map = new LinkedHashMap<String, Object>();
            
            int i = index + 1;
            int k = 0;
            while (true)
            {
                i = streamToNotWhiteSpace(jsonString, i);
                char startOfFind = jsonString.charAt(i);
                if (startOfFind == '"')
                {
                    // this is a key
                    i++;
                    k = streamToChar(jsonString, '"', i, false, true);
                    String key = jsonString.substring(i, k);
                    // finding :
                    k++;
                    i = streamToChar(jsonString, ':', k, true, false);
                    i++;
                    // finding obj/str/num
                    i = streamToNotWhiteSpace(jsonString, i);
                    char startOfObj = jsonString.charAt(i);
                    if (startOfObj == '{' || startOfObj == '[')
                    {
                        // its an object/array (basically another JSON object)
                        JSON j = new JSON();
                        i = j.parseString(jsonString, i);
                        map.put(key, j);
                        i = streamToNotWhiteSpace(jsonString, i);
                        char maybeEndChar = jsonString.charAt(i);
                        i++;
                        if (maybeEndChar == ',')
                        {
                            // this is means there is another key:value
                        } else if (maybeEndChar == '}')
                        {
                            // this is the end of the object
                            return i;
                        } else
                        {
                            throw new Exception("Couldn't find end ',' or '}'. index:" + i + "  " + getHelperString(jsonString, i, 100));
                        }
                    } else if (startOfObj == '"')
                    {
                        // its a string
                        i++;
                        k = streamToChar(jsonString, '"', i, true, true);
                        String val = jsonString.substring(i, k);
                        map.put(key, val);
                        k++;
                        i = streamToNotWhiteSpace(jsonString, k);
                        char maybeEndChar = jsonString.charAt(i);
                        i++;
                        if (maybeEndChar == ',')
                        {
                            // this is means there is another key:value
                        } else if (maybeEndChar == '}')
                        {
                            // this is the end of the object
                            return i;
                        } else
                        {
                            throw new Exception("Couldn't find end ',' or '}'. index:" + i + "  " + getHelperString(jsonString, i, 100));
                        }
                    } else if (startOfObj == ',')
                    {
                        // its null
                        map.put(key, null);
                        i++;
                    } else if (startOfObj == '}')
                    {
                        // its null and its the end of the obj
                        map.put(key, null);
                        i++;
                        return i;
                    } else if (startOfObj == 't' || startOfObj == 'f' || startOfObj == 'T' || startOfObj == 'F')
                    {
                        // its a boolean
                        if (jsonString.substring(i, i + 4).toLowerCase().equals("true"))
                        {
                            i += 4;
                            map.put(key, true);
                        } else if (jsonString.substring(i, i + 5).toLowerCase().equals("false"))
                        {
                            i += 5;
                            map.put(key, false);
                        } else
                        {
                            throw new Exception("Unrecognized characters at index: " + i + "  " + getHelperString(jsonString, i, 100));
                        }
                        
                        i = streamToNotWhiteSpace(jsonString, i);
                        char maybeEndChar = jsonString.charAt(i);
                        i++;
                        if (maybeEndChar == ',')
                        {
                            // this is means there is another key:value
                        } else if (maybeEndChar == '}')
                        {
                            // this is the end of the object
                            return i;
                        } else
                        {
                            throw new Exception("Couldn't find end ',' or '}'. index:" + i + "  " + getHelperString(jsonString, i, 100));
                        }
                    } else if (startOfObj == 'n' || startOfObj == 'N')
                    {
                        // its a null
                        if (jsonString.substring(i, i + 4).toLowerCase().equals("null"))
                        {
                            i += 4;
                            map.put(key, null);
                        } else
                        {
                            throw new Exception("Unrecognized characters at index: " + i + "  " + getHelperString(jsonString, i, 100));
                        }
                        
                        i = streamToNotWhiteSpace(jsonString, i);
                        char maybeEndChar = jsonString.charAt(i);
                        i++;
                        if (maybeEndChar == ',')
                        {
                            // this is means there is another key:value
                        } else if (maybeEndChar == '}')
                        {
                            // this is the end of the object
                            return i;
                        } else
                        {
                            throw new Exception("Couldn't find end ',' or '}'. index:" + i + "  " + getHelperString(jsonString, i, 100));
                        }
                    } else
                    {
                        boolean isNegative = false;
                        if (startOfObj == '-')
                        {
                            i++;
                            startOfObj = jsonString.charAt(i);
                            isNegative = true;
                        }
                        try
                        {
                            @SuppressWarnings("unused")
                            double d = Double.parseDouble("" + startOfObj);
                            
                            // its a number
                            k = streamToChars(jsonString, i, new char[] { ' ', '\t', 'n', ',', '}' });
                            String valStr = jsonString.substring(i, k);
                            double val = Double.parseDouble(valStr);
                            if (isNegative)
                            {
                                val *= -1;
                            }
                            map.put(key, val);
                            i = streamToNotWhiteSpace(jsonString, k);
                            char maybeEndChar = jsonString.charAt(i);
                            i++;
                            if (maybeEndChar == ',')
                            {
                                // this is means there is another key:value
                            } else if (maybeEndChar == '}')
                            {
                                // this is the end of the object
                                return i;
                            } else
                            {
                                throw new Exception("Couldn't find end ',' or '}'. index:" + i + "  " + getHelperString(jsonString, i, 100));
                            }
                        } catch (Exception e)
                        {
                            // its an error
                            throw new Exception("Could not determine the type at index:" + i + "  " + getHelperString(jsonString, i, 100));
                        }
                    }
                } else if (startOfFind == '}')
                {
                    // this is the end
                    i++;
                    return i;
                } else
                {
                    throw new Exception("Syntax error, expecting '\"' or '}'. index:" + i + "  " + getHelperString(jsonString, i, 100));
                }
            }
        } else if (jsonString.charAt(index) == '[')
        {
            isArray = true;
            array = new ArrayList<Object>();
            
            int i = index + 1;
            int k = 0;
            while (true)
            {
                // finding obj/str/num
                i = streamToNotWhiteSpace(jsonString, i);
                char startOfObj = jsonString.charAt(i);
                if (startOfObj == '{' || startOfObj == '[')
                {
                    // its an object/array (basically another JSON object)
                    JSON j = new JSON();
                    i = j.parseString(jsonString, i);
                    array.add(j);
                    i = streamToNotWhiteSpace(jsonString, i);
                    char maybeEndChar = jsonString.charAt(i);
                    i++;
                    if (maybeEndChar == ',')
                    {
                        // this is means there is another key:value
                    } else if (maybeEndChar == ']')
                    {
                        // this is the end of the object
                        return i;
                    } else
                    {
                        throw new Exception("Couldn't find end ',' or ']'. index:" + i + "  " + getHelperString(jsonString, i, 100));
                    }
                } else if (startOfObj == '"')
                {
                    // its a string
                    i++;
                    k = streamToChar(jsonString, '"', i, true, true);
                    String val = jsonString.substring(i, k);
                    array.add(val);
                    k++;
                    i = streamToNotWhiteSpace(jsonString, k);
                    char maybeEndChar = jsonString.charAt(i);
                    i++;
                    if (maybeEndChar == ',')
                    {
                        // this is means there is another key:value
                    } else if (maybeEndChar == ']')
                    {
                        // this is the end of the object
                        return i;
                    } else
                    {
                        throw new Exception("Couldn't find end ',' or ']'. index:" + i + "  " + getHelperString(jsonString, i, 100));
                    }
                } else if (startOfObj == ',')
                {
                    // its null
                    array.add(null);
                    i++;
                } else if (startOfObj == ']')
                {
                    // its null and its the end of the obj
                    i++;
                    return i;
                } else
                {
                    try
                    {
                        @SuppressWarnings("unused")
                        double d = Double.parseDouble("" + startOfObj);
                        
                        // its a number
                        k = streamToChars(jsonString, i, new char[] { ' ', '\t', 'n', ',', ']' });
                        String valStr = jsonString.substring(i, k);
                        double val = Double.parseDouble(valStr);
                        array.add(val);
                        i = streamToNotWhiteSpace(jsonString, k);
                        char maybeEndChar = jsonString.charAt(i);
                        i++;
                        if (maybeEndChar == ',')
                        {
                            // this is means there is another key:value
                        } else if (maybeEndChar == ']')
                        {
                            // this is the end of the object
                            return i;
                        } else
                        {
                            throw new Exception("Couldn't find end ',' or ']'. index:" + i + "  " + getHelperString(jsonString, i, 100));
                        }
                    } catch (Exception e)
                    {
                        // its an error
                        throw new Exception("Could not determine the type at index:" + i + "  " + getHelperString(jsonString, i, 100));
                    }
                }
            }
        }
        throw new Exception("Expected '{' or '[' to start string=" + jsonString.substring(0, Math.min(jsonString.length(), 25)));
    }
    
    
    protected static int streamToChar(String s, char c, int i, boolean allowWhiteSpace, boolean allowOtherChar) throws Exception
    {
        if (i >= s.length())
        {
            throw new Exception("Starting index was greater or equal to String length. index:" + i);
        }
        while (i < s.length())
        {
            char n = s.charAt(i);
            if (n == c)
            {
                return i;
            } else if (n == ' ' || n == '\n' || n == '\t')
            {
                if (!allowWhiteSpace)
                {
                    throw new Exception("No whitespace allowed. index:" + i);
                }
            } else
            {
                if (!allowOtherChar)
                {
                    throw new Exception("Expecting a '" + c + "' but got a '" + n + "'. index:" + i);
                }
            }
            i++;
        }
        throw new Exception("Char did not exist in string");
    }
    
    
    protected static int streamToNotWhiteSpace(String s, int i) throws Exception
    {
        if (i >= s.length())
        {
            throw new Exception("Starting index was greater or equal to String length. index:" + i);
        }
        while (i < s.length())
        {
            char n = s.charAt(i);
            if (n != ' ' && n != '\n' && n != '\t' && n != '\r')
            {
                return i;
            }
            i++;
        }
        throw new Exception("Found the end of the String before a non-whitespace char");
    }
    
    
    protected static int streamToChars(String s, int i, char[] chars) throws Exception
    {
        if (i >= s.length())
        {
            throw new Exception("Starting index was greater or equal to String length. index:" + i);
        }
        while (i < s.length())
        {
            char n = s.charAt(i);
            for (char c : chars)
            {
                if (n == c)
                {
                    return i;
                }
            }
            i++;
        }
        throw new Exception("Found the end of the String before any of the given chars");
    }
    
    
    protected static String getHelperString(String s, int i, int len)
    {
        int endHelperStr = Math.min(i + len, s.length() - 1);
        String helperString = "--[" + s.substring(Math.max(i - (len / 2), 0), endHelperStr) + "]--";
        return helperString.replace("\n", "").replace("\t", "");
    }
    
    
    @Override
    public String toString()
    {
        return toString(0);
    }
    
    
    /**
     * Formats the return string more JSON looking using spacing and new lines
     * 
     * @param tab
     * @return
     */
    public String toString(int tab)
    {
        String t = "";
        for (int i = 0; i < tab; i++)
        {
            t += " ";
        }
        String n = "";
        if (tab > 0)
            n = "\n";
        String s = " ";
        return toString(t, n, s, 0);
    }
    
    
    protected String toString(String t, String n, String s, int depth)
    {
        String tab = tab(t, depth + 1);
        if (isArray)
        {
            if (array.size() == 0)
            {
                return "[]";
            }
            String str = "[" + n;
            for (int i = 0; i < array.size(); i++)
            {
                str += tab;
                Object o = array.get(i);
                if (o == null)
                {
                    str += "null";
                } else if (o instanceof JSON)
                {
                    str += ((JSON) o).toString(t, n, s, depth + 1);
                } else if (o instanceof String)
                {
                    str += "\"" + o.toString() + "\"";
                } else
                {
                    str += o.toString();
                }
                
                if (i + 1 < array.size())
                {
                    str += "," + s + n;
                }
            }
            str += n + tab(t, depth) + "]";
            return str;
        } else
        {
            String[] keys = map.keySet().toArray(new String[map.keySet().size()]);
            if (keys.length == 0)
            {
                return "{}";
            }
            String str = "{" + n;
            for (int i = 0; i < keys.length; i++)
            {
                str += tab + "\"" + keys[i] + "\"" + s + ":" + s;
                Object o = map.get(keys[i]);
                if (o == null)
                {
                    str += "null";
                } else if (o instanceof JSON)
                {
                    str += ((JSON) o).toString(t, n, s, depth + 1);
                } else if (o instanceof String)
                {
                    str += "\"" + o.toString() + "\"";
                } else
                {
                    str += o.toString();
                }
                
                if (i + 1 < keys.length)
                {
                    str += "," + s + n;
                }
            }
            str += n + tab(t, depth) + "}";
            return str;
        }
    }
    
    
    protected String tab(String t, int depth)
    {
        String tab = "";
        for (int i = 0; i < depth; i++)
        {
            tab += t;
        }
        return tab;
    }
    
    
    /**
     * Gets the json object as a list of objects (used for iterator)
     * 
     * @return
     */
    private List<Object> getJsonAsList()
    {
        if (isArray)
        {
            return array;
        } else
        {
            List<Object> objs = new ArrayList<Object>();
            for (String key : map.keySet())
            {
                objs.add(map.get(key));
            }
            return objs;
        }
    }
    
    
    /**
     * Inits a JSON object as {}
     * 
     * @return
     */
    public static JSON emptyObject()
    {
        return new JSON("{}");
    }
    
    
    /**
     * Inits a JSON object as []
     * 
     * @return
     */
    public static JSON emptyArray()
    {
        return new JSON("[]");
    }
    
    
    public <T> T deserializeTo(Class<T> clazz) throws JsonParseException, JsonMappingException, IOException
    {
        return mapper.readValue(toString(3), clazz);
    }
    
    
    public static String serialize(Object obj) throws JsonProcessingException
    {
        return writer.writeValueAsString(obj);
    }
    
    
    public static JSON serializeToJSON(Object obj) throws JsonProcessingException
    {
        return JSON.parse(JSON.serialize(obj));
    }
    
    
    @Override
    public Iterator<Object> iterator()
    {
        return getJsonAsList().iterator();
    }
    
}
