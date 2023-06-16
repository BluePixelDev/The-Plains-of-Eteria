package org.eteriaEngine.rendering.postProcessing;

import org.eteriaEngine.rendering.RTHandle;
import org.eteriaEngine.rendering.camera.Camera;
import org.eteriaEngine.rendering.commandBuffer.CommandBuffer;
import org.eteriaEngine.rendering.material.Material;

public class TestPostProcessEffect extends PostProcessingEffect{
    Material effectMaterial = new Material("assets/shaders/blur.glsl");
    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void render(Camera camera, CommandBuffer commandBuffer, RTHandle rtHandle) {
        effectMaterial.setTexture("_mainTex", rtHandle.getTexture());
        commandBuffer.drawFullscreen(effectMaterial);
    }
}
