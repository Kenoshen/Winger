package com.winger.libgdx.main.input;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.winger.input.raw.CKeyboard;
import com.winger.input.raw.CMouse;
import com.winger.input.raw.state.CMouseButton;
import com.winger.input.raw.state.KeyboardState;
import com.winger.log.HTMLLogger;
import com.winger.log.LogGroup;
import com.winger.ui.Element;
import com.winger.ui.Page;

public class TestKeyboardInput
{
    private static final HTMLLogger log = HTMLLogger.getLogger(TestKeyboardInput.class, LogGroup.Framework, LogGroup.Assert);
    Element<?> display;
    CKeyboard keyboard;
    CMouse mouse;
    
    
    public TestKeyboardInput(Page ui)
    {
        display = ui.getElementById("display");
        keyboard = ui.keyboard;
        mouse = ui.mouse;
    }
    
    
    public void update()
    {
        if (display != null)
        {
            List<Integer> keysBeingPressed = new ArrayList<Integer>();
            for (int i = 0; i < 1000; i++)
            {
                if (Gdx.input.isKeyPressed(i))
                {
                    keysBeingPressed.add(i);
                }
            }
            
            KeyboardState state = keyboard.currentKeysDown();
            
            display.text(keysBeingPressed + "\n" + state.keysDown + "\n" + (mouse.held(CMouseButton.LEFT) ? "LEFT" : "") + "\n"
                + (mouse.held(CMouseButton.RIGHT) ? "RIGHT" : "") + "\n" + (mouse.held(CMouseButton.MIDDLE) ? "MIDDLE" : ""));
        }
    }
}
