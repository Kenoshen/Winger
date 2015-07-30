package com.winger.input.raw.state;

public enum KeyboardKey
{
    A("a", 29),
    B("b", 30),
    C("c", 31),
    D("d", 32),
    E("e", 33),
    F("f", 34),
    G("g", 35),
    H("h", 36),
    I("i", 37),
    J("j", 38),
    K("k", 39),
    L("l", 40),
    M("m", 41),
    N("n", 42),
    O("o", 43),
    P("p", 44),
    Q("q", 45),
    R("r", 46),
    S("s", 47),
    T("t", 48),
    U("u", 49),
    V("v", 50),
    W("w", 51),
    X("x", 52),
    Y("y", 53),
    Z("z", 54),
    TILDE("`", "~", 68),
    ONE("1", "!", 8),
    TWO("2", "@", 9),
    THREE("3", "#", 10),
    FOUR("4", "$", 11),
    FIVE("5", "%", 12),
    SIX("6", "^", 13),
    SEVEN("7", "&", 14),
    EIGHT("8", "*", 15),
    NINE("9", "(", 16),
    ZERO("0", ")", 7),
    DASH("-", "_", 69),
    EQUALS("=", "+", 70),
    OPEN_BRACKET("[", "{", 71),
    CLOSE_BRACKET("]", "}", 72),
    BACK_SLASH("\\", "|", 73),
    SEMI_COLON(";", ":", 74),
    SINGLE_QUOTE("'", "\"", 75),
    ENTER("\n", 66),
    TAB("\t", 61),
    BACKSPACE("\b", 67),
    DELETE("\r", 112),
    COMMA(",", "<", 55),
    PERIOD(".", ">", 56),
    SLASH("/", "?", 76),
    SPACE(" ", 62),
    LEFT_SHIFT(59),
    RIGHT_SHIFT(60),
    LEFT_CONTROL(129),
    RIGHT_CONTROL(130),
    LEFT_ALT(57),
    RIGHT_ALT(58),
    INSERT(133),
    HOME(3),
    END(132),
    PAGE_UP(92),
    PAGE_DOWN(93),
    LEFT(21),
    RIGHT(22),
    UP(19),
    DOWN(20),
    ESCAPE(131),
    NUMPAD_0("0", 144),
    NUMPAD_1("1", 145),
    NUMPAD_2("2", 146),
    NUMPAD_3("3", 147),
    NUMPAD_4("4", 148),
    NUMPAD_5("5", 149),
    NUMPAD_6("6", 150),
    NUMPAD_7("7", 151),
    NUMPAD_8("8", 152),
    NUMPAD_9("9", 153),
    F1(244),
    F2(245),
    F3(246),
    F4(247),
    F5(248),
    F6(249),
    F7(250),
    F8(251),
    F9(252),
    F10(253),
    F11(254),
    F12(255),
    GRAHV(TILDE),
    GRAVE(TILDE),
    BACK_QUOTE(TILDE),
    EXCLAMATION(ONE),
    BANG(ONE),
    AT(TWO),
    HASH(THREE),
    HASH_TAG(THREE),
    POUND(THREE),
    DOLLAR(FOUR),
    PERCENT(FIVE),
    CARROT(SIX),
    AND(SEVEN),
    STAR(EIGHT),
    OPEN_PARENTHESIS(NINE),
    CLOSE_PARENTHESIS(ZERO),
    UNDERSCORE(DASH),
    PLUS(EQUALS),
    MINUS(DASH),
    OPEN_CURLY(OPEN_BRACKET),
    CLOSE_CURLY(CLOSE_BRACKET),
    PIPE(BACK_SLASH),
    COLON(SEMI_COLON),
    DOUBLE_QUOTE(SINGLE_QUOTE),
    APOSTROPHE(SINGLE_QUOTE),
    LESS_THAN(COMMA),
    GREATER_THAN(PERIOD),
    QUESTION(SLASH), ;
    private String character;
    private String shiftCharacter;
    private boolean isModifier;
    private KeyboardKey alternate = null;
    private Integer index = null;


    KeyboardKey(String character, String shiftCharacter, boolean isModifier, Integer index)
    {
        this.character = character;
        this.shiftCharacter = shiftCharacter;
        this.isModifier = isModifier;
        this.index = index;
    }


    KeyboardKey(String character, String shiftCharacter, Integer index)
    {
        this(character, shiftCharacter, false, index);
    }


    KeyboardKey(String character, Integer index)
    {
        this(character, character.toUpperCase(), false, index);
    }


    KeyboardKey(Integer index)
    {
        this("", "", true, index);
    }


    KeyboardKey(KeyboardKey alternate)
    {
        this("", "", false, null);
        this.alternate = alternate;
    }

    public static KeyboardKey fromCharacter(String character) {
        if (character == null) {
            return null;
        }
        for (KeyboardKey key : KeyboardKey.values()) {
            if (character.equalsIgnoreCase(key.character()) || character.equalsIgnoreCase(key.shiftCharacter())) {
                return key;
            }
        }
        return null;
    }

    public static KeyboardKey fromIndex(int index) {
        for (KeyboardKey key : KeyboardKey.values()) {
            if (key.index == index) {
                return key;
            }
        }
        return null;
    }
    
    public String character()
    {
        if (alternate != null)
        {
            return alternate.character;
        } else
        {
            return character;
        }
    }
    
    public String shiftCharacter()
    {
        if (alternate != null)
        {
            return alternate.shiftCharacter;
        } else
        {
            return shiftCharacter;
        }
    }
    
    public boolean isModifier()
    {
        if (alternate != null)
        {
            return alternate.isModifier;
        } else
        {
            return isModifier;
        }
    }
    
    public boolean isAlternate()
    {
        return (alternate != null);
    }
    
    public Integer index()
    {
        if (alternate != null)
        {
            return alternate.index;
        } else
        {
            return index;
        }
    }
}
