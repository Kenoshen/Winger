package com.winger.utils;

import com.winger.input.raw.CKeyboard;
import com.winger.input.raw.state.KeyboardKey;
import com.winger.struct.Tups.Tup2;

/**
 * Not really sure what this was going to be...
 *
 * @author Michael P. Wingfield
 * @copyright Nov 4, 2014
 *
 */
public class TypeUtils
{
    public static Tup2<String, Integer> getTypedText(CKeyboard keyboard, String currentText, int index)
    {
        boolean shift = false;
        boolean control = false;
        boolean alt = false;
        for (KeyboardKey key : keyboard.currentKeysDown().keysDown)
        {
            if (key.isModifier())
            {
                if (key == KeyboardKey.LEFT_SHIFT || key == KeyboardKey.RIGHT_SHIFT)
                {
                    shift = true;
                } else if (key == KeyboardKey.LEFT_CONTROL || key == KeyboardKey.RIGHT_CONTROL)
                {
                    control = true;
                } else if (key == KeyboardKey.LEFT_ALT || key == KeyboardKey.RIGHT_ALT)
                {
                    alt = true;
                }
            }
        }
        String t = "";
        for (KeyboardKey key : keyboard.keysJustDown())
        {
            if (key == KeyboardKey.BACKSPACE)
            {
                if (index > 0 && currentText.length() > 0)
                {
                    currentText = StringUtils.remove(currentText, index - 1, index);
                    index--;
                }
            } else if (key == KeyboardKey.DELETE)
            {
                if (index >= 0 && index < currentText.length())
                    currentText = StringUtils.remove(currentText, index, index + 1);
            } else if (key == KeyboardKey.LEFT)
            {
                if (index > 0)
                    index--;
            } else if (key == KeyboardKey.RIGHT)
            {
                if (index < currentText.length())
                    index++;
            } else if (key == KeyboardKey.HOME)
            {
                index = 0;
            } else if (key == KeyboardKey.END)
            {
                index = currentText.length();
            } else if (control)
            {
                if (key == KeyboardKey.C)
                    GlobalClipboard.instance().setContents(currentText);
                if (key == KeyboardKey.V)
                    t += GlobalClipboard.instance().getContents();
            } else if (!key.isModifier())
            {
                if (alt || control)
                {
                    // nothing
                } else if (shift)
                {
                    t += key.shiftCharacter();
                } else
                {
                    t += key.character();
                }
            }
        }
        
        currentText = StringUtils.insert(currentText, index, t);
        index += t.length();
        
        return new Tup2<String, Integer>(currentText, index);
    }
}
