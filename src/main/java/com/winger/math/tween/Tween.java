package com.winger.math.tween;

import com.badlogic.gdx.math.Vector2;
import com.winger.math.tween.delegate.TweenIsFinished;

/**
 * Similar to Flash's idea of a motion tween, where an object is set at a position in one frame, and given a different position in a following frame,
 * and then told to morph from the starting position to the ending position in a given mathamatical style. Same concept, just using durations instead
 * of frame counts.
 *
 * @author Michael P. Wingfield
 * @copyright Nov 4, 2014
 *
 */
public class Tween
{
    public TweenIsFinished tweenIsFinished;
    
    public Vector2 origin = new Vector2(0, 0);
    public Vector2 position = new Vector2(0, 0);
    public Vector2 goal = new Vector2(0, 0);
    public boolean stopOnGoal = true;
    public float currentTime;
    public float duration;
    public TweenType tweenTypeX;
    public TweenType tweenTypeY;
    private boolean pause = true;
    private boolean snappedToGoal = false;
    
    /**
     * Get the tween value given all the needed parameters
     *
     * @param type
     * @param currentTime
     * @param startValue
     * @param deltaValue
     * @param duration
     * @return
     */
    public static float getTweenValue(TweenType type, float currentTime, float startValue, float deltaValue, float duration)
    {
        switch (type)
        {
            case LINEAR:
                return linear(currentTime, startValue, deltaValue, duration);

            case QUADRATIC_IN:
                return quadraticIn(currentTime, startValue, deltaValue, duration);

            case QUADRATIC_OUT:
                return quadraticOut(currentTime, startValue, deltaValue, duration);

            case QUADRATIC_INOUT:
                return quadraticInOut(currentTime, startValue, deltaValue, duration);

            case CUBIC_IN:
                return cubicIn(currentTime, startValue, deltaValue, duration);

            case CUBIC_OUT:
                return cubicOut(currentTime, startValue, deltaValue, duration);

            case CUBIC_INOUT:
                return cubicInOut(currentTime, startValue, deltaValue, duration);

            case QUARTIC_IN:
                return quarticIn(currentTime, startValue, deltaValue, duration);

            case QUARTIC_OUT:
                return quarticOut(currentTime, startValue, deltaValue, duration);

            case QUARTIC_INOUT:
                return quarticInOut(currentTime, startValue, deltaValue, duration);

            case QUINTIC_IN:
                return quinticIn(currentTime, startValue, deltaValue, duration);

            case QUINTIC_OUT:
                return quinticOut(currentTime, startValue, deltaValue, duration);

            case QUINTIC_INOUT:
                return quinticInOut(currentTime, startValue, deltaValue, duration);

            case SINUSOIDAL_IN:
                return sinusoidalIn(currentTime, startValue, deltaValue, duration);

            case SINUSOIDAL_OUT:
                return sinusoidalOut(currentTime, startValue, deltaValue, duration);

            case SINUSOIDAL_INOUT:
                return sinusoidalInOut(currentTime, startValue, deltaValue, duration);

            case EXPONENTIAL_IN:
                return exponentialIn(currentTime, startValue, deltaValue, duration);

            case EXPONENTIAL_OUT:
                return exponentialOut(currentTime, startValue, deltaValue, duration);

            case EXPONENTIAL_INOUT:
                return exponentialInOut(currentTime, startValue, deltaValue, duration);

            case CIRCULAR_IN:
                return circularIn(currentTime, startValue, deltaValue, duration);

            case CIRCULAR_OUT:
                return circularOut(currentTime, startValue, deltaValue, duration);

            case CIRCULAR_INOUT:
                return circularInOut(currentTime, startValue, deltaValue, duration);

            default:
                return linear(currentTime, startValue, deltaValue, duration);
        }
    }
    
    /**
     * Gets the tween type from a string
     *
     * @param s
     * @return
     */
    public static TweenType tweenTypeFromString(String s)
    {
        s = s.toUpperCase().replace(" ", "").replace("_", "");
        if ("0".equals(s) || "LINEAR".equals(s))
            return TweenType.LINEAR;
        else if ("1".equals(s) || "QUADRATICIN".equals(s))
            return TweenType.QUADRATIC_IN;
        else if ("2".equals(s) || "QUADRATICOUT".equals(s))
            return TweenType.QUADRATIC_OUT;
        else if ("3".equals(s) || "QUADRATICINOUT".equals(s))
            return TweenType.QUADRATIC_INOUT;
        else if ("4".equals(s) || "CUBICIN".equals(s))
            return TweenType.CUBIC_IN;
        else if ("5".equals(s) || "CUBICOUT".equals(s))
            return TweenType.CUBIC_OUT;
        else if ("6".equals(s) || "CUBICINOUT".equals(s))
            return TweenType.CUBIC_INOUT;
        else if ("7".equals(s) || "QUARTICIN".equals(s))
            return TweenType.QUARTIC_IN;
        else if ("8".equals(s) || "QUARTICOUT".equals(s))
            return TweenType.QUARTIC_OUT;
        else if ("9".equals(s) || "QUARTICINOUT".equals(s))
            return TweenType.QUARTIC_INOUT;
        else if ("10".equals(s) || "QUINTICIN".equals(s))
            return TweenType.QUINTIC_IN;
        else if ("11".equals(s) || "QUINTICOUT".equals(s))
            return TweenType.QUINTIC_OUT;
        else if ("12".equals(s) || "QUINTICINOUT".equals(s))
            return TweenType.QUINTIC_INOUT;
        else if ("13".equals(s) || "SINUSOIDALIN".equals(s))
            return TweenType.SINUSOIDAL_IN;
        else if ("14".equals(s) || "SINUSOIDALOUT".equals(s))
            return TweenType.SINUSOIDAL_OUT;
        else if ("15".equals(s) || "SINUSOIDALINOUT".equals(s))
            return TweenType.SINUSOIDAL_INOUT;
        else if ("16".equals(s) || "EXPONENTIALIN".equals(s))
            return TweenType.EXPONENTIAL_IN;
        else if ("17".equals(s) || "EXPONENTIALOUT".equals(s))
            return TweenType.EXPONENTIAL_OUT;
        else if ("18".equals(s) || "EXPONENTIALINOUT".equals(s))
            return TweenType.EXPONENTIAL_INOUT;
        else if ("19".equals(s) || "CIRCULARIN".equals(s))
            return TweenType.CIRCULAR_IN;
        else if ("20".equals(s) || "CIRCULAROUT".equals(s))
            return TweenType.CIRCULAR_OUT;
        else if ("21".equals(s) || "CIRCULARINOUT".equals(s))
            return TweenType.CIRCULAR_INOUT;
        else
            return TweenType.LINEAR;
    }
    
    /**
     * No acceleration
     *
     * @param t currentTime
     * @param b startValue
     * @param c deltaValue
     * @param d duration
     * @return
     */
    public static float linear(float t, float b, float c, float d)
    {
        return c * t / d + b;
    }
    
    /**
     * Accelerate from zero
     *
     * @param t currentTime
     * @param b startValue
     * @param c deltaValue
     * @param d duration
     * @return
     */
    public static float quadraticIn(float t, float b, float c, float d)
    {
        t /= d;
        return c * t * t + b;
    }
    
    /**
     * Deccelerate to zero
     *
     * @param t currentTime
     * @param b startValue
     * @param c deltaValue
     * @param d duration
     * @return
     */
    public static float quadraticOut(float t, float b, float c, float d)
    {
        t /= d;
        return -c * t * (t - 2) + b;
    }
    
    /**
     * Accelerate half way then deccelerate the other half
     *
     * @param t currentTime
     * @param b startValue
     * @param c deltaValue
     * @param d duration
     * @return
     */
    public static float quadraticInOut(float t, float b, float c, float d)
    {
        t /= d / 2;
        if (t < 1)
            return c / 2 * t * t + b;
        t--;
        return -c / 2 * (t * (t - 2) - 1) + b;
    }
    
    /**
     * Accelerate from zero
     *
     * @param t currentTime
     * @param b startValue
     * @param c deltaValue
     * @param d duration
     * @return
     */
    public static float cubicIn(float t, float b, float c, float d)
    {
        t /= d;
        return c * t * t * t + b;
    }
    
    /**
     * Deccelerate to zero
     *
     * @param t currentTime
     * @param b startValue
     * @param c deltaValue
     * @param d duration
     * @return
     */
    public static float cubicOut(float t, float b, float c, float d)
    {
        t /= d;
        t--;
        return c * (t * t * t + 1) + b;
    }
    
    /**
     * Accelerate half way then deccelerate the other half
     *
     * @param t currentTime
     * @param b startValue
     * @param c deltaValue
     * @param d duration
     * @return
     */
    public static float cubicInOut(float t, float b, float c, float d)
    {
        t /= d / 2;
        if (t < 1)
            return c / 2 * t * t * t + b;
        t -= 2;
        return c / 2 * (t * t * t + 2) + b;
    }
    
    /**
     * Accelerate from zero
     *
     * @param t currentTime
     * @param b startValue
     * @param c deltaValue
     * @param d duration
     * @return
     */
    public static float quarticIn(float t, float b, float c, float d)
    {
        t /= d;
        return c * t * t * t * t + b;
    }
    
    /**
     * Deccelerate to zero
     *
     * @param t currentTime
     * @param b startValue
     * @param c deltaValue
     * @param d duration
     * @return
     */
    public static float quarticOut(float t, float b, float c, float d)
    {
        t /= d;
        t--;
        return -c * (t * t * t * t - 1) + b;
    }
    
    /**
     * Accelerate half way then deccelerate the other half
     *
     * @param t currentTime
     * @param b startValue
     * @param c deltaValue
     * @param d duration
     * @return
     */
    public static float quarticInOut(float t, float b, float c, float d)
    {
        t /= d / 2;
        if (t < 1)
            return c / 2 * t * t * t * t + b;
        t -= 2;
        return -c / 2 * (t * t * t * t - 2) + b;
    }
    
    /**
     * Accelerate from zero
     *
     * @param t currentTime
     * @param b startValue
     * @param c deltaValue
     * @param d duration
     * @return
     */
    public static float quinticIn(float t, float b, float c, float d)
    {
        t /= d;
        return c * t * t * t * t * t + b;
    }
    
    /**
     * Deccelerate to zero
     *
     * @param t currentTime
     * @param b startValue
     * @param c deltaValue
     * @param d duration
     * @return
     */
    public static float quinticOut(float t, float b, float c, float d)
    {
        t /= d;
        t--;
        return c * (t * t * t * t * t - 1) + b;
    }
    
    /**
     * Accelerate half way then deccelerate the other half
     *
     * @param t currentTime
     * @param b startValue
     * @param c deltaValue
     * @param d duration
     * @return
     */
    public static float quinticInOut(float t, float b, float c, float d)
    {
        t /= d / 2;
        if (t < 1)
            return c / 2 * t * t * t * t * t + b;
        t -= 2;
        return c / 2 * (t * t * t * t * t + 2) + b;
    }
    
    /**
     * Accelerate from zero
     *
     * @param t currentTime
     * @param b startValue
     * @param c deltaValue
     * @param d duration
     * @return
     */
    public static float sinusoidalIn(float t, float b, float c, float d)
    {
        return -c * (float) Math.cos(t / d * (Math.PI / 2)) + c + b;
    }
    
    /**
     * Deccelerate to zero
     *
     * @param t currentTime
     * @param b startValue
     * @param c deltaValue
     * @param d duration
     * @return
     */
    public static float sinusoidalOut(float t, float b, float c, float d)
    {
        return c * (float) Math.sin(t / d * (Math.PI / 2)) + b;
    }
    
    /**
     * Accelerate half way then deccelerate the other half
     *
     * @param t currentTime
     * @param b startValue
     * @param c deltaValue
     * @param d duration
     * @return
     */
    public static float sinusoidalInOut(float t, float b, float c, float d)
    {
        return -c / 2 * ((float) Math.cos(Math.PI * t / d) - 1) + b;
    }
    
    /**
     * Accelerate from zero
     *
     * @param t currentTime
     * @param b startValue
     * @param c deltaValue
     * @param d duration
     * @return
     */
    public static float exponentialIn(float t, float b, float c, float d)
    {
        return c * (float) Math.pow(2, 10 * (t / d - 1)) + b;
    }
    
    /**
     * Deccelerate to zero
     *
     * @param t currentTime
     * @param b startValue
     * @param c deltaValue
     * @param d duration
     * @return
     */
    public static float exponentialOut(float t, float b, float c, float d)
    {
        return c * (-(float) Math.pow(2, -10 * t / d) + 1) + b;
    }
    
    /**
     * Accelerate half way then deccelerate the other half
     *
     * @param t currentTime
     * @param b startValue
     * @param c deltaValue
     * @param d duration
     * @return
     */
    public static float exponentialInOut(float t, float b, float c, float d)
    {
        t /= d / 2;
        if (t < 1)
            return c / 2 * (float) Math.pow(2, 10 * (t - 1)) + b;
        t--;
        return c / 2 * (-(float) Math.pow(2, -10 * t) + 2) + b;
    }
    
    /**
     * Accelerate from zero
     *
     * @param t currentTime
     * @param b startValue
     * @param c deltaValue
     * @param d duration
     * @return
     */
    public static float circularIn(float t, float b, float c, float d)
    {
        t /= d;
        return -c * ((float) Math.sqrt(1 - t * t) - 1) + b;
    }
    
    /**
     * Deccelerate to zero
     *
     * @param t currentTime
     * @param b startValue
     * @param c deltaValue
     * @param d duration
     * @return
     */
    public static float circularOut(float t, float b, float c, float d)
    {
        t /= d;
        t--;
        return c * (float) Math.sqrt(1 - t * t) + b;
    }
    
    /**
     * Accelerate half way then deccelerate the other half
     *
     * @param t currentTime
     * @param b startValue
     * @param c deltaValue
     * @param d duration
     * @return
     */
    public static float circularInOut(float t, float b, float c, float d)
    {
        t /= d / 2;
        if (t < 1)
            return -c / 2 * ((float) Math.sqrt(1 - t * t) - 1) + b;
        t -= 2;
        return c / 2 * ((float) Math.sqrt(1 - t * t) + 1) + b;
    }

    /**
     * Starts the tween
     */
    public void start() {
        currentTime = 0;
        position = origin;
        snappedToGoal = false;
        unPause();
    }

    /**
     * Checks if the tween is paused
     *
     * @return
     */
    public boolean isPaused() {
        return pause;
    }

    /**
     * Pauses the tween
     */
    public void pause() {
        pause = true;
    }

    /**
     * Unpauses the tween
     */
    public void unPause() {
        pause = false;
    }

    /**
     * Checks if the tween is tweening
     *
     * @return
     */
    public boolean isTweening() {
        return (currentTime < duration);
    }

    /**
     * Updates the tween
     *
     * @param timeChange
     */
    public void update(float timeChange) {
        if (!pause) {
            currentTime = currentTime + timeChange;
            if (!stopOnGoal || currentTime < duration) {
                position.x = getTweenValue(tweenTypeX, currentTime, origin.x, goal.x - origin.x, duration);
                position.y = getTweenValue(tweenTypeY, currentTime, origin.y, goal.y - origin.y, duration);
            } else if (!snappedToGoal) {
                snappedToGoal = true;
                position = goal;
                pause();
                if (tweenIsFinished != null) {
                    tweenIsFinished.tweenIsFinished(this);
                }
            }
        }
    }
}
