package org.eteriaEngine.core;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11C.glClearColor;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public final class Window{

    // The window handle
    private volatile long window;

    public Window(){
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        // Set up an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE); // the window will be resizable

        // Create the window
        window = glfwCreateWindow(Screen.width(), Screen.height(), "", NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        // Get the thread stack and push a new frame
        try (MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            assert vidmode != null;
            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically
    }

    /**
     * Returns ID of this window.
     */
    public long getWindow() {
        return window;
    }
    public boolean shouldWindowClose(){
        return glfwWindowShouldClose(window);
    }

    //---- CONTROL API ----
    /**
     * Closes currently open window.
     */
    public void closeWindow() {
        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }
    /**
     * Changes interval for vertical sync.
     */
    public void setAsync(int interval){
       glfwSwapInterval(interval);
    }
    /**
     * Sets resolution of the window.
     */
    public void setResolution(Resolution resolution){
       glfwSetWindowSize(window, resolution.width(), resolution.height());
       GL11.glViewport(0,0,  resolution.width(), resolution.height());
       GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
       assert vidmode != null;
       glfwSetWindowPos(window, vidmode.width() / 2 - resolution.width() / 2, vidmode.height() / 2 - resolution.height() / 2);
    }
    public void showWindow(){
        // Make the window visible
        glfwShowWindow(window);
    }
    public void hideWindow(){
        // Make the window visible
        glfwHideWindow(window);
    }
    /**
     * Makes context required for rendering.
     */
    public void makeContext(){
        glfwMakeContextCurrent(getWindow());
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();
        // Set the clear color
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        // Enable v-sync
        glfwSwapInterval(1);
    }
    /**
     * Sets current window title.
     */
    public void setWindowTitle(String title){
        glfwSetWindowTitle(window, title);
    }
    public void setWindowIcon(BufferedImage image){
        ByteBuffer imageBuffer = EngineUtility.convertToByteBuffer(image);
        glfwSetWindowIcon(window, new GLFWImage.Buffer(imageBuffer));
    }
    /**
     * Polls events. In other words, listens to keyboard input.
     */
    public void pollEvents(){
        while (!shouldWindowClose()){
            glfwPollEvents();
        }
    }
    /**
     * This method is important to call when starting rendering
     * in order to clear the window.
     */
    public void startRendering(){
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
    }
    /**
     * This method is important to call when ending rendering
     * in order to swap buffers.
     */
    public void stopRendering(){
        glfwSwapBuffers(window); // swap the color buffers
    }
}