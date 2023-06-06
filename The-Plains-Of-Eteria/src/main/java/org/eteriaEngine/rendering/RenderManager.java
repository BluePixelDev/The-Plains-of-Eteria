package org.eteriaEngine.rendering;

import org.eteriaEngine.core.Screen;
import org.eteriaEngine.core.Window;
import org.eteriaEngine.rendering.camera.Camera;
import org.eteriaEngine.rendering.camera.CameraManager;
import org.eteriaEngine.rendering.commands.CommandBuffer;
import org.eteriaEngine.rendering.enums.FilterMode;
import org.eteriaEngine.rendering.enums.GraphicsFormat;
import org.eteriaEngine.rendering.enums.TextureWrapMode;
import org.eteriaEngine.rendering.postProcessing.PostProcessing;

import java.util.HashSet;

public class RenderManager {
    private static volatile RenderManager instance;
    private static final Object mutex = new Object();

    private final RTHandle afterRenderRT;
    private final RTHandle afterPostRT;

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
        afterRenderRT = RTHandle.Alloc(
                Screen.width(),
                Screen.height(),
                FilterMode.POINT,
                TextureWrapMode.REPEAT,
                GraphicsFormat.RGB16F
        );
        afterPostRT = RTHandle.Alloc(
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
        CommandBuffer postCommandBuffer = new CommandBuffer();
        CommandBuffer screenRenderBuffer = new CommandBuffer();

        sceneBuffer.setRenderTarget(afterRenderRT);
        if(camera != null){
            for (Renderer renderer: renderers) {
                if(renderer.isEnabled() && renderer.gameObject().isActive()){
                    renderer.render(camera, sceneBuffer);
                }
            }
        }
        sceneBuffer.execute();

        //---- POST PROCESSING ----
        postCommandBuffer.setRenderTarget(afterPostRT);
        postProcessing.render(camera, postCommandBuffer, afterRenderRT);
        postCommandBuffer.execute();

        //---- FINAL RENDERING ----
        screenRenderBuffer.blit(afterRenderRT);
        screenRenderBuffer.execute();
        window.stopRendering();
    }
}
