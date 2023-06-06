package org.eteriaEngine.rendering.commands;

import org.eteriaEngine.interfaces.ICommand;
import org.eteriaEngine.rendering.RTHandle;
import org.eteriaEngine.rendering.material.Material;
import org.eteriaEngine.rendering.mesh.Mesh;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.Collections;

import static org.lwjgl.opengl.GL11.*;

public class CommandBuffer implements ICommand{

    private final ArrayList<ICommand> commands = new ArrayList<>();
    Material screenMaterial = new Material("engine/shaders/defaultScreen.glsl");
    RenderContext renderContext = null;

    public CommandBuffer(){
        glEnable(GL_CULL_FACE);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
    }

    public void blit(RTHandle source){
        commands.add(new BlitCommand(source, this));
    }
    public void setRenderTarget(RTHandle target){
        commands.add(new SetRenderTargetCommand(target));
    }
    public void drawMesh(Mesh mesh, Matrix4f matrix, Material material){
        commands.add(new DrawMeshCommand(mesh, matrix, material, this));
    }

    public void setRenderContext(RenderContext renderContext) {
        this.renderContext = renderContext;
    }
    public RenderContext getRenderContext() {
        return renderContext;
    }

    public void execute(){
        setRenderTarget(null);
        Collections.reverse(commands);
        for(int i = 0; i < commands.size(); i++){
            commands.get(i).execute();
        }
        commands.clear();
    }
}
