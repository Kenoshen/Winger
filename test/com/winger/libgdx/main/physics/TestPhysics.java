package com.winger.libgdx.main.physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.winger.input.delegate.CKeyboardEventHandler;
import com.winger.input.delegate.CMouseEventHandler;
import com.winger.input.raw.CKeyboard;
import com.winger.input.raw.CMouse;
import com.winger.input.raw.state.ButtonState;
import com.winger.input.raw.state.CMouseButton;
import com.winger.input.raw.state.CMouseDrag;
import com.winger.input.raw.state.KeyboardKey;
import com.winger.log.HTMLLogger;
import com.winger.log.LogGroup;
import com.winger.math.VectorMath;
import com.winger.physics.CBody;
import com.winger.physics.CWorld;
import com.winger.physics.body.BoxBody;
import com.winger.physics.body.ChainBody;
import com.winger.physics.body.CircleBody;
import com.winger.physics.body.PillBody;
import com.winger.physics.body.PlayerBody;
import com.winger.ui.Page;
import com.winger.utils.RandomUtils;

public class TestPhysics implements CMouseEventHandler, CKeyboardEventHandler
{
    @SuppressWarnings("unused")
    private static final HTMLLogger log = HTMLLogger.getLogger(TestPhysics.class, LogGroup.Framework, LogGroup.Assert);
    CWorld world;
    CMouse mouse;
    CKeyboard keyboard;
    OrthographicCamera camera;
    Page physicsUI;
    
    PlayerBody player;
    
    
    public TestPhysics(Page ui)
    {
        physicsUI = ui;
        //
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom = 0.1f;
        world = new CWorld(camera);
        world.init(new Vector2(0, -30), true);
        world.debug(true);
        //
        ChainBody chain = new ChainBody(-35, 0, 35, 0, 35, 40);
        BodyDef bD = new BodyDef();
        bD.type = BodyType.StaticBody;
        bD.position.y = -20;
        chain.init(bD);
        chain.addToWorld(world);
        //
        mouse = CMouse.instance;
        mouse.clickNotifier().subscribeToEvent(this, CMouseButton.LEFT, ButtonState.DOWN);
        keyboard = new CKeyboard();
        keyboard.subscribeToAllKeyboardEvents(this, ButtonState.DOWN);
        keyboard.subscribeToAllKeyboardEvents(this, ButtonState.UP);
    }
    
    
    public void update(float delta)
    {
        mouse.update();
        keyboard.update();
        if (physicsUI.isEnabled)
            world.update(delta);
        
        if (player != null)
        {
            if (keyboard.isKeyBeingPressed(KeyboardKey.LEFT))
            {
                player.run(-20);
            } else if (keyboard.isKeyBeingPressed(KeyboardKey.RIGHT))
            {
                player.run(20);
            } else
            {
                player.run(0);
            }
            if (keyboard.isKeyJustPressed(KeyboardKey.UP) || keyboard.isKeyJustPressed(KeyboardKey.W) || keyboard.isKeyJustPressed(KeyboardKey.SPACE))
            {
                player.jump(25);
            }
        }
    }
    
    
    public void draw()
    {
        if (physicsUI.isVisible)
            world.draw();
    }
    
    
    public void createNewRandomBody()
    {
        CBody<?> body;
        int rand = RandomUtils.randInt(0, 3);
        if (rand == 0)
        {
            body = new CircleBody(RandomUtils.rand(1, 2));
        } else if (rand == 1)
        {
            body = new PillBody(RandomUtils.rand(2, 3), RandomUtils.rand(2, 4));
        } else if (rand == 2)
        {
            player = new PlayerBody(2, 4);
            body = player;
        } else
        {
            body = new BoxBody(RandomUtils.rand(1, 2), RandomUtils.rand(1, 2));
        }
        body.init(VectorMath.multiply(VectorMath.toVector2(camera.unproject(VectorMath.toVector3(mouse.position()))), 1, -1));
        body.addToWorld(world._world);
        body.setRestitution(RandomUtils.rand());
        body.setAngularVelocity(RandomUtils.rand(-2, 2));
    }
    
    
    @Override
    public void handleClickEvent(CMouse mouse, CMouseButton button, ButtonState state)
    {
        if (physicsUI.isEnabled)
            createNewRandomBody();
    }
    
    
    @Override
    public void handleScrollEvent(CMouse mouse, float difference)
    {
        // TODO Auto-generated method stub
        
    }
    
    
    @Override
    public void handleMoveEvent(CMouse mouse, Vector2 position)
    {
        // TODO Auto-generated method stub
        
    }
    
    
    @Override
    public void handleDragEvent(CMouse mouse, CMouseButton button, CMouseDrag state)
    {
        // TODO Auto-generated method stub
        
    }
    
    
    @Override
    public void handleKeyEvent(CKeyboard keyboard, KeyboardKey key, ButtonState state)
    {
        // if (player != null)
        // {
        // if (key == KeyboardKey.LEFT || key == KeyboardKey.RIGHT)
        // {
        // if (state == ButtonState.DOWN)
        // {
        // if (key == KeyboardKey.LEFT)
        // {
        // player.walk(-10);
        // } else
        // {
        // player.walk(10);
        // }
        // } else
        // {
        // player.walk(0);
        // }
        // } else if (key == KeyboardKey.UP && state == ButtonState.DOWN)
        // {
        // player.jump(25);
        // }
        // }
    }
}
