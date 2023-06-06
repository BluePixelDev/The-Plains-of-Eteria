package org.eteriaEngine;

import org.joml.Vector3f;

/**
 * A library that converts built in java math methods to use floats.
 */
public class Mathf {
    private Mathf(){

    }

    public final static float radToDeg = 57.2957795f;
    public final static float degToRad = 0.0174532925f;

    //<editor-fold desc="LERP">
    /**
     * Linearly interpolates vector value from a to b in given time.
     * @return Interpolated Vector3 value.
     */
    public static Vector3f lerp(Vector3f a, Vector3f b, float t){
        t = clamp(t, 0, 1);
        Vector3f vecA = new Vector3f(a);
        vecA.mul(1 - t);

        Vector3f vecB = new Vector3f(b);
        vecB.mul(t);

        return vecA.add(vecB);
    }
    /**
     * Linearly interpolates float value from a to b in given time.
     * @return Interpolated float value.
     */
    public static float lerp(float a, float b, float t){
        t = clamp(t, 0, 1);
        return (1 - t) * a + t * b;
    }
    //</editor-fold>

    //<editor-fold desc="CLAMP">
    /**
     * Clamps value between min and max value.
     */
    public static float clamp(float value, float min, float max){
        return value < min? min : Math.min(value, max);
    }
    /**
     * Clamps value between min and max value..
     */
    public static int clamp(int value, int min, int max){
        return value < min? min : Math.min(value, max);
    }
    /**
     * Clamps value between min and max value.
     */
    public static double clamp(double value, double min, double max){
        return value < min? min : Math.min(value, max);
    }
    //</editor-fold>

    /**
     * Returns the smallest (closest to negative infinity) float value that is greater than or equal
     * to the argument and is equal to a mathematical integer.
     */
    public static float ceil(float value){
        return (float) Math.ceil(value);
    }
    /**
     * Returns the largest (closest to negative infinity) float value that is less than or equal
     * to the argument and is equal to a mathematical integer.
     */
    public static float floor(float value){
        return (float) Math.floor(value);
    }
    /**
     * Returns absolute value of a float.<br>
     * Example: <br>
     * <b>abs(-5) = 5</b>
     */
    public static float abs(float value){
        return Math.abs(value);
    }
}
