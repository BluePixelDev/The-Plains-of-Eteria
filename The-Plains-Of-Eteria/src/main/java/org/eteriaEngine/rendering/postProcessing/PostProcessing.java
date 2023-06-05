package org.eteriaEngine.rendering.postProcessing;

import org.eteriaEngine.core.Screen;
import org.eteriaEngine.rendering.RTHandle;
import org.eteriaEngine.rendering.camera.Camera;
import org.eteriaEngine.rendering.command.CommandBuffer;
import org.eteriaEngine.rendering.enums.FilterMode;
import org.eteriaEngine.rendering.enums.GraphicsFormat;
import org.eteriaEngine.rendering.enums.TextureWrapMode;

import java.util.ArrayList;

public class PostProcessing {
    private final ArrayList<PostProcessingEffect> postEffects = new ArrayList<>();
    private final RTHandle postRTA;
    private final RTHandle postRTB;

    public PostProcessing()
    {
        postRTA = RTHandle.Alloc(
                Screen.width(),
                Screen.height(),
                FilterMode.BILINEAR,
                TextureWrapMode.REPEAT,
                GraphicsFormat.RGB16F
        );

        postRTB = RTHandle.Alloc(
                Screen.width(),
                Screen.height(),
                FilterMode.BILINEAR,
                TextureWrapMode.REPEAT,
                GraphicsFormat.RGB16F
        );
    }

    public void addEffect(PostProcessingEffect effect){
        postEffects.add(effect);
    }

    public void render(Camera camera, CommandBuffer commandBuffer, RTHandle rtHandle){
        commandBuffer.setRenderTarget(postRTA);
        commandBuffer.blit(rtHandle);

        for(int i = 0; i < postEffects.size(); i++){
            if(i%2 != 1) {
                commandBuffer.setRenderTarget(postRTB);
            }
            else{
                commandBuffer.setRenderTarget(postRTA);
            }
            postEffects.get(i).render(camera, commandBuffer, rtHandle);
        }
        commandBuffer.setRenderTarget(null);
        commandBuffer.blit( postEffects.size()%2 != 1 ? postRTA : postRTB);
    }
}
