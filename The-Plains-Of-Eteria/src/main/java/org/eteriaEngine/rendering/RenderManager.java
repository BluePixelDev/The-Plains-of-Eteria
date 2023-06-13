package org.eteriaEngine.rendering;

import org.eteriaEngine.core.Screen;
import org.eteriaEngine.core.Window;
import org.eteriaEngine.rendering.commandBuffer.CommandBuffer;
import org.eteriaEngine.rendering.camera.Camera;
import org.eteriaEngine.rendering.camera.CameraManager;
import org.eteriaEngine.rendering.enums.FilterMode;
import org.eteriaEngine.rendering.enums.GraphicsFormat;
import org.eteriaEngine.rendering.enums.TextureWrapMode;
import org.eteriaEngine.rendering.postProcessing.PostProcessing;

import java.util.HashSet;

public class RenderManager {
    private static volatile RenderManager instance;
    private static final Object mutex = new Object();

    private final RTHandle sceneRT;
    private final RTHandle postRT;

    private final PostProcessing postProcessing = new PostProcessing();
    private final HashSet<Renderer> renderers = new HashSet<>();

    public static RenderManager get(){
        RenderManager result = instance;

        if(instance == null){
            synchronized (mutex){
                result = instance;

                if(result == null){
                    instance = result = new RenderManager();
                }
            }
        }
        return result;
    }

    public RenderManager(){
        sceneRT = RTHandle.Alloc(
                Screen.width(),
                Screen.height(),
                FilterMode.POINT,
                TextureWrapMode.REPEAT,
                GraphicsFormat.RGB16F
        );
        postRT = RTHandle.Alloc(
                Screen.width(),
                Screen.height(),
                FilterMode.POINT,
                TextureWrapMode.REPEAT,
                GraphicsFormat.RGB16F
        );
    }
    public void addRenderer(Renderer renderer){
        renderers.add(renderer);
    }
    public void removeRenderer(Renderer renderer){
        if(!renderers.contains(renderer)){
            renderers.add(renderer);
        }
    }

    public void render(Window window){
        window.startRendering();
        Camera camera = CameraManager.getCurrentCamera();

        CommandBuffer sceneBuffer = new CommandBuffer();
        sceneBuffer.setRenderTarget(sceneRT);
        if(camera != null){
            for (Renderer renderer : renderers) {
                if(renderer.isEnabled() && renderer.gameObject().isActive()){
                    renderer.render(camera, sceneBuffer);
                }
            }
        }
        sceneBuffer.execute();

        //---- POST PROCESSING ----
        CommandBuffer postBuffer = new CommandBuffer();
        postProcessing.render(camera, postBuffer, sceneRT, postRT);
        postBuffer.execute();

        //---- FINAL RENDERING ----
        CommandBuffer screenBuffer = new CommandBuffer();
        screenBuffer.setRenderTarget(null);
        screenBuffer.blit(postRT);
        screenBuffer.execute();
        window.stopRendering();
    }
}
