package com.winger.struct;

import com.winger.log.HTMLLogger;
import com.winger.log.LogGroup;
import com.winger.struct.deserialize.JSONDeserializeRoot;
import org.junit.Test;

import java.util.List;

public class TestJSON
{
    private static final HTMLLogger log = HTMLLogger.getLogger(TestJSON.class, LogGroup.Framework, LogGroup.Assert);
    
    
    private JSON setUpJSON()
    {
        JSON j = JSON.emptyObject();
        j.put("string", "hello");
        j.put("bool", true);
        j.put("long", 10000000000L);
        j.put("int", 1000000);
        j.put("short", (short) 1000);
        j.put("byte", (byte) 1);
        j.put("double", 0.1);
        j.put("float", 0.5f);
        j.put("obj", JSON.emptyObject().put("key", "value"));
        j.put("objArray", JSON.emptyArray().put(JSON.emptyObject().put("k1", "v1")));
        j.put("strArray", JSON.emptyArray().put("str1"));
        j.put("numArray", JSON.emptyArray().put(0).put(1000000000000L).put(0.5f));
        j.put("etcArray", JSON.emptyArray().put(0).put("hello").put(JSON.emptyObject().put("k", "v")));
        return j;
    }
    
    
    private JSON setUpDeserializeJSON()
    {
        JSON j = JSON.emptyObject();
        j.put("str1", "hello");
        j.put("str2", "world");
        j.put("int1", 4.3);
        j.put("float1", 4.4f);
        j.put("private1", "goodbye");
        j.put("private2", 5.5f);
        j.put("obj1", JSON.emptyObject());
        j.put("obj1.str1", "I'm still here");
        j.put("obj1.private1", "Can you see me?");
        j.put("list1", JSON.emptyArray());
        j.put("list1.#", JSON.emptyObject());
        j.put("list1.0.str1", "yippy");
        j.put("list1.0.private1", "kiyack");
        j.put("list1.#", JSON.emptyObject());
        j.put("list1.1.str1", "yippy2");
        j.put("list1.1.private1", "kiyack2");
        j.put("list2", JSON.emptyArray());
        j.put("list2.#", "str1");
        j.put("list2.#", "str2");
        return j;
    }
    
    
    @Test
    public void testJSONGetStringConversion()
    {
        JSON j = setUpJSON();
        //
        String s = j.get("string");
        log.info(s);
        s = j.get(String.class, "string");
        log.info(s);
    }
    
    
    @Test
    public void testJSONGetNumberConversion()
    {
        JSON j = setUpJSON();
        //
        long l = j.get("long");
        log.info(l);
        l = j.get(Long.class, "long");
        log.info(l);
        //
        int i = j.get("int");
        log.info(i);
        i = j.get(Integer.class, "int");
        log.info(i);
        //
        short s = j.get("short");
        log.info(s);
        s = j.get(Short.class, "short");
        log.info(s);
        //
        byte b = j.get("byte");
        log.info(b);
        b = j.get(Byte.class, "byte");
        log.info(b);
        //
        double d = j.get("double");
        log.info(d);
        d = j.get(Double.class, "double");
        log.info(d);
        //
        float f = j.get("float");
        log.info(f);
        f = j.get(Float.class, "float");
        log.info(f);
    }
    
    
    @Test
    public void testJSONGetBooleanConversion()
    {
        JSON j = setUpJSON();
        //
        boolean b = j.get("bool");
        log.info(b);
        b = j.get(Boolean.class, "bool");
        log.info(b);
    }
    
    
    @Test
    public void testJSONGetJSONConversion()
    {
        JSON j = setUpJSON();
        //
        JSON o = j.get("obj");
        log.info(o);
        o = j.get(JSON.class, "obj");
        log.info(o);
        //
        JSON a = j.get("objArray");
        log.info(a);
        a = j.get(JSON.class, "objArray");
        log.info(a);
    }
    
    
    @Test
    public void testJSONGetArrayConversion()
    {
        JSON j = setUpJSON();
        //
        List<JSON> o = j.get("objArray.#");
        log.info(o);
        o.get(0).length();
        //
        List<String> s = j.get("strArray.#");
        log.info(s);
        s.get(0).trim();
        //
        List<Number> n = j.get("numArray.#");
        log.info(n);
        n.get(0).doubleValue();
        //
        List<Object> e = j.get("etcArray.#");
        log.info(e);
        e.get(0).hashCode();
    }
    
    
    @Test
    public void testJSONGetNumberArrayConversion()
    {
        JSON j = setUpJSON();
        //
        List<Number> n = j.get("numArray.#");
        log.info(n);
        n.get(0).floatValue();
    }
    
    
    @Test
    public void testJSONGetWrongNumberConverstion()
    {
        JSON j = setUpJSON();
        //
        long l = j.get(Long.class, "float");
        log.info(l);
        int i = j.get(Integer.class, "float");
        log.info(i);
        short s = j.get(Short.class, "float");
        log.info(s);
        byte b = j.get(Byte.class, "float");
        log.info(b);
        //
        l = j.get(Long.class, "double");
        log.info(l);
        i = j.get(Integer.class, "double");
        log.info(i);
        s = j.get(Short.class, "double");
        log.info(s);
        b = j.get(Byte.class, "double");
        log.info(b);
        //
        l = j.get(Long.class, "byte");
        log.info(l);
        i = j.get(Integer.class, "byte");
        log.info(i);
        s = j.get(Short.class, "byte");
        log.info(s);
        //
        l = j.get(Long.class, "short");
        log.info(l);
        i = j.get(Integer.class, "short");
        log.info(i);
        b = j.get(Byte.class, "short");
        log.info(b);
        //
        l = j.get(Long.class, "int");
        log.info(l);
        s = j.get(Short.class, "int");
        log.info(s);
        b = j.get(Byte.class, "int");
        log.info(b);
        //
        i = j.get(Integer.class, "long");
        log.info(i);
        s = j.get(Short.class, "long");
        log.info(s);
        b = j.get(Byte.class, "long");
        log.info(b);
        //
        double d = j.get(Double.class, "long");
        log.info(d);
        float f = j.get(Float.class, "long");
        log.info(f);
        //
        d = j.get(Double.class, "int");
        log.info(d);
        f = j.get(Float.class, "int");
        log.info(f);
        //
        d = j.get(Double.class, "short");
        log.info(d);
        f = j.get(Float.class, "short");
        log.info(f);
        //
        d = j.get(Double.class, "byte");
        log.info(d);
        f = j.get(Float.class, "byte");
        log.info(f);
        //
        d = j.get(Double.class, "float");
        log.info(d);
        //
        f = j.get(Float.class, "double");
        log.info(f);
    }
    
    
    @Test
    public void testJSONDeserialize()
    {
        JSON j = setUpDeserializeJSON();
        String original = j.toString(3);
        log.info("Original: " + original);
        try
        {
            JSONDeserializeRoot root = j.deserializeTo(JSONDeserializeRoot.class);
            
            log.info("AWESOME!!!");
            
            JSON serialized = JSON.serializeToJSON(root);
            String now = serialized.toString(3);
            log.info(now);
            
        } catch (Exception e)
        {
            log.error("Damn...");
            e.printStackTrace();
        }
    }
    
}
