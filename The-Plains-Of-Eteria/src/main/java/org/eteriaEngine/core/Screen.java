package org.eteriaEngine.core;

import org.lwjgl.glfw.GLFWVidMode;

import static org.lwjgl.glfw.GLFW.*;

public final class Screen {
    private static Resolution currentResolution = new Resolution(1, 1, 60);
    public static boolean fullscreen;

    //<editor-fold desc="API">
    //---- FULLSCREEN ----
    /**
     * Sets window fullscreen mode.
     */
    public static void setFullscreen(boolean fullscreen) {
        Screen.fullscreen = fullscreen;
        Window window = EteriaEngine.getWindow();

        if(fullscreen){
            glfwSetWindowPos(window.getWindowID(), 0, 0);
            glfwSetWindowAttrib(window.getWindowID(), GLFW_DECORATED, GLFW_FALSE);

            Resolution[] resolutions = getResolutions();
            currentResolution = resolutions[resolutions.length - 1];
        }
        else{
            glfwSetWindowAttrib(window.getWindowID(), GLFW_DECORATED, GLFW_TRUE);
        }
    }
    /**
     * Is window in fullscreen?
     */
    public static boolean fullscreen() {
        return fullscreen;
    }

    //---- RESOLUTION ----
    /**
     * Sets screen resolution.
     */
    public static void setScreenResolution(Resolution resolution){
        currentResolution = resolution;
        Window window = EteriaEngine.getWindow();

        if(window.getWindowID() != 0){
            window.setResolution(resolution);
        }
    }
    /**
     * Sets screen resolution based on index.
     */
    public static void setScreenResolution(int index){
        Resolution resolution = getResolutions()[index];
        Window window = EteriaEngine.getWindow();
        currentResolution = resolution;

        if(window.getWindowID() != 0){
            window.setResolution(resolution);
        }
    }
    /**
     * @return All resolutions supported by primary monitor.
     */
    public static Resolution[] getResolutions(){
        GLFWVidMode.Buffer modes = glfwGetVideoModes(glfwGetPrimaryMonitor());
        assert modes != null;

        Resolution[] resolutions = new Resolution[modes.limit()];
        for(int i = 0; i < resolutions.length; i++){
            resolutions[i] = new Resolution(
                    modes.get(i).width(),
                    modes.get(i).height(),
                    modes.get(i).refreshRate()
            );
        }
        return resolutions;
    }

    /**
     * @return Current resolution width.
     */
    public static int width() {
        return currentResolution.width();
    }
    /**
     * @return Current resolution height.
     */
    public static int height() {
        return currentResolution.height();
    }
    /**
     * @return Current resolution refresh rate.
     */
    public static int refreshRate() {
        return currentResolution.refreshRate();
    }
    //</editor-fold>
}