package org.eteriaEngine.core;

public final class Time {
    private static boolean hasStarted;
    private static float timeScale = 1;
    private static long startTimeNano;
    private static long lastTimeNano;
    private static long deltaTimeNano;

    static void update(){
        if(!hasStarted){
            startTimeNano = System.nanoTime();
            hasStarted = true;
        }
        deltaTimeNano = System.nanoTime() - lastTimeNano;
        lastTimeNano = System.nanoTime();
    }

    public static float getTimeScale() {
        return timeScale;
    }
    public static void setTimeScale(float timeScale) {
        Time.timeScale = Math.abs(timeScale);
    }

    //<editor-fold desc="API">
    /**
     * @return Current frame per seconds value.
     */
    public static double getFrameRateHertz() {
        float frameRate = 1f / (System.nanoTime() - lastTimeNano);
        return frameRate * 1e9;
    }
    /**
     * @return Scaled delta time. Time in seconds since the last frame.
     */
    public static float deltaTime(){
        return (deltaTimeNano / 1000000000f) * timeScale;
    }
    /**
     * @return Unscaled delta time. Time in seconds since the last frame.
     */
    public static float unscaledDeltaTime(){
        return (deltaTimeNano / 1000000000f);
    }
    /**
     * @return Time in seconds since the start of the Application.
     */
    public static float timeSinceStart() {
        return (System.nanoTime() - startTimeNano) / 1000000000f;
    }
    //</editor-fold>
}
