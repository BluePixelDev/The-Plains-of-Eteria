package org.eteriaEngine.rendering.commandBuffer;

import org.eteriaEngine.interfaces.ICommand;
import org.eteriaEngine.rendering.RTHandle;
import org.eteriaEngine.rendering.commandBuffer.commands.BlitCMD;
import org.eteriaEngine.rendering.commandBuffer.commands.DrawMeshCMD;
import org.eteriaEngine.rendering.commandBuffer.commands.SetRenderTargetCMD;
import org.eteriaEngine.rendering.enums.BlendingMode;
import org.eteriaEngine.rendering.enums.CullingMode;
import org.eteriaEngine.rendering.enums.DepthFunc;
import org.eteriaEngine.rendering.material.Material;
import org.eteriaEngine.rendering.mesh.Mesh;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;

public class CommandBuffer implements ICommand{

    private final HashMap<RTHandle, ArrayList<ICommand>> commands = new HashMap<>();

    RTHandle currentRT = null;
    Material screenMaterial = new Material("engine/shaders/defaultScreen.glsl");
    RenderContext renderContext = null;

    public CommandBuffer(){
        commands.put(null, new ArrayList<>());
        glEnable(GL_CULL_FACE);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
    }

    public void setRenderTarget(RTHandle target){
        currentRT = target;
        if(!commands.containsKey(target)){
            commands.put(target, new ArrayList<>());
        }
        commands.get(target).add(new SetRenderTargetCMD(target));
    }
    public void blit(RTHandle source){
        commands.get(currentRT).add(new BlitCMD(source, screenMaterial));
    }
    public void drawMesh(Mesh mesh, Matrix4f matrix, Material material){
        commands.get(currentRT).add(new DrawMeshCMD(mesh, matrix, material, this));
    }

    public void setRenderContext(RenderContext renderContext) {
        this.renderContext = renderContext;
    }
    public RenderContext getRenderContext() {
        return renderContext;
    }

    public void execute(){
        for (ArrayList<ICommand> commandList : commands.values()) {
            for(ICommand command : commandList){
                command.execute();
            }
        }
        commands.clear();
    }

    public record RenderContext(CullingMode cullingMode, DepthFunc depthFunc, BlendingMode blendingMode) {

        @Override
        public String toString() {
            return "MaterialRenderContext{" +
                    "cullingMode=" + cullingMode +
                    ", depthFunc=" + depthFunc +
                    ", blendingMode=" + blendingMode +
                    '}';
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            RenderContext that = (RenderContext) o;
            return cullingMode == that.cullingMode && depthFunc == that.depthFunc && blendingMode == that.blendingMode;
        }
    }
}
