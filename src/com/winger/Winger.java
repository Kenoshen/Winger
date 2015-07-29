package com.winger;

import com.winger.draw.font.FontManager;
import com.winger.draw.texture.TextureManager;
import com.winger.log.HTMLLogger;
import com.winger.log.LogGroup;
import com.winger.log.LogLevel;
import com.winger.math.NumberMath;
import com.winger.math.VectorMath;
import com.winger.physics.CWorld;
import com.winger.stats.FPSCalculator;
import com.winger.ui.PageManager;
import com.winger.ui.TransitionManager;
import com.winger.utils.GlobalClipboard;
import com.winger.utils.ParseUtils;
import com.winger.utils.RandomUtils;
import com.winger.utils.ReflectionUtils;
import com.winger.utils.StringUtils;
import com.winger.utils.TypeUtils;

public class Winger
{
    private Winger()
    {};
    
    
    public static final FontManager font = FontManager.instance();
    public static final TextureManager texture = TextureManager.instance();
    public static final NumberMath math = new NumberMath();
    public static final VectorMath vector = new VectorMath();
    public static final CWorld world = CWorld.world;
    public static final HTMLLogger log = HTMLLogger.getLogger(Winger.class, LogLevel.Debug, LogGroup.Framework);
    public static final FPSCalculator fps = new FPSCalculator();
    public static final PageManager ui = PageManager.instance();
    public static final TransitionManager transition = TransitionManager.instance();
    public static final GlobalClipboard clipboard = GlobalClipboard.instance();
    public static final ParseUtils parse = new ParseUtils();
    public static final RandomUtils random = new RandomUtils();
    public static final ReflectionUtils reflection = new ReflectionUtils();
    public static final StringUtils string = new StringUtils();
    public static final TypeUtils type = new TypeUtils();
}
