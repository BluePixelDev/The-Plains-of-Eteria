package org.eteriaEngine.rendering.postProcessing;
import org.eteriaEngine.rendering.RTHandle;
import org.eteriaEngine.rendering.camera.Camera;
import org.eteriaEngine.rendering.command.CommandBuffer;

public abstract class PostProcessingEffect {
    protected boolean isActive;
    public abstract boolean isActive();

    public void Setup(){}
    public void render(Camera camera, CommandBuffer commandBuffer, RTHandle rtHandle){}
    public void Cleanup(Camera camera){}

    public final void blit(){

    }
}
