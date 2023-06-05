package org.eteriaEngine.rendering;

import org.eteriaEngine.core.Screen;
import org.eteriaEngine.core.Window;
import org.eteriaEngine.rendering.camera.Camera;
import org.eteriaEngine.rendering.camera.CameraManager;
import org.eteriaEngine.rendering.command.CommandBuffer;
import org.eteriaEngine.rendering.enums.FilterMode;
import org.eteriaEngine.rendering.enums.GraphicsFormat;
import org.eteriaEngine.rendering.enums.TextureWrapMode;
import org.eteriaEngine.rendering.material.Material;
import org.eteriaEngine.rendering.mesh.Quad;
import org.eteriaEngine.rendering.postProcessing.PostProcessing;
import java.util.HashSet;

public class RenderManager {
    private static volatile RenderManager instance;
    private static final Object mutex = new Object();

    private final RTHandle afterRenderRT;
    private final RTHandle afterPostRT;

    private final Quad screenQuad;
    private final Material screenMaterial;

    private PostProcessing postProcessing = new PostProcessing();
    private HashSet<Renderer> renderers = new HashSet<>();

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

        screenQuad = new Quad(2, 2);
        screenMaterial = new Material("assets/shaders/screen.glsl");
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
        CommandBuffer commandBuffer = new CommandBuffer();

        commandBuffer.setRenderTarget(afterRenderRT);
        if(camera != null){
            for (Renderer renderer: renderers) {
                if(renderer.isEnabled() && renderer.gameObject().isActive()){
                    renderer.render(camera, commandBuffer);
                }
            }
        }
        commandBuffer.execute();

        //---- POST PROCESSING ----
        CommandBuffer postCommandBuffer = new CommandBuffer();
        postCommandBuffer.setRenderTarget(afterPostRT);
        postProcessing.render(camera, postCommandBuffer, afterRenderRT);
        postCommandBuffer.execute();

        commandBuffer.setRenderTarget(null);
        commandBuffer.blit(afterRenderRT);
        commandBuffer.execute();
        window.stopRendering();
    }
}
