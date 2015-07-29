package com.winger.libgdx.main.sprite;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.winger.draw.texture.Animator;
import com.winger.draw.texture.Animator.AnimatorNotificationSubscriber;
import com.winger.draw.texture.CSprite;
import com.winger.draw.texture.CSpriteBatch;
import com.winger.input.delegate.CKeyboardEventHandler;
import com.winger.input.raw.CKeyboard;
import com.winger.input.raw.CMouse;
import com.winger.input.raw.state.ButtonState;
import com.winger.input.raw.state.KeyboardKey;
import com.winger.libgdx.main.physics.TestPhysics;
import com.winger.log.HTMLLogger;
import com.winger.log.LogGroup;
import com.winger.struct.CRectangle;
import com.winger.ui.Page;

public class TestAnimator implements CKeyboardEventHandler, AnimatorNotificationSubscriber
{
    @SuppressWarnings("unused")
    private static final HTMLLogger log = HTMLLogger.getLogger(TestPhysics.class, LogGroup.Framework, LogGroup.Assert);
    CMouse mouse;
    CKeyboard keyboard;
    OrthographicCamera camera;
    Page ui;
    CSpriteBatch sb = new CSpriteBatch();
    Animator animator = new Animator();
    
    
    public TestAnimator(Page ui)
    {
        this.ui = ui;
        CRectangle rect = ui.getElementById("animator").getAbsoluteBoundingBox();
        
        animator.addLoopAnimation("idle", new CSprite("various.devil", rect, true).setTotalAnimationTime(500));
        animator.addLoopAnimation("run", new CSprite("green.run", rect, true).setTotalAnimationTime(500));
        animator.addNoLoopNextAnimation("jumpUp", new CSprite("green.jumpUp1,", rect, true).setTotalAnimationTime(500), "jumpDown", "idle");
        animator.addNoLoopNextAnimation("jumpDown", new CSprite("green.jumpDown1,", rect, true).setTotalAnimationTime(500), null, "idle");
        animator.addListener(this);
        
        keyboard = new CKeyboard();
        keyboard.subscribeToAllKeyboardEvents(this, ButtonState.DOWN);
    }
    
    
    public void update(float delta)
    {
        keyboard.update();
        if (ui.isEnabled)
            animator.update(delta);
    }
    
    
    public void draw()
    {
        if (ui.isVisible)
        {
            sb.begin();
            animator.draw(sb);
            sb.end();
        }
    }
    
    
    @Override
    public void handleKeyEvent(CKeyboard keyboard, KeyboardKey key, ButtonState state)
    {
        if (key == KeyboardKey.SPACE)
        {
            animator.goToAnimation("jumpUp");
        } else if (key == KeyboardKey.LEFT)
        {
            animator.setFlipped(true);
            animator.goToAnimation("run");
        } else if (key == KeyboardKey.RIGHT)
        {
            animator.setFlipped(false);
            animator.goToAnimation("run");
        }
    }
    
    
    @Override
    public void animationNotification(String oldAnimation, String newAnimation)
    {
        ui.getElementById("currentAnimation").text(newAnimation);
    }
}
