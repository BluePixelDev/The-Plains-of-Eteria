package org.eteriaEngine.rendering.command;

import org.eteriaEngine.interfaces.ICommand;
import org.eteriaEngine.rendering.RTHandle;
import org.eteriaEngine.rendering.material.Material;
import org.eteriaEngine.rendering.mesh.Mesh;
import org.joml.Matrix4f;
import java.util.ArrayList;
import java.util.Collections;

public class CommandBuffer implements ICommand{

    private ArrayList<ICommand> commands = new ArrayList<>();
    Material screenMaterial = new Material("engine/shaders/defaultScreen.glsl");

    public void blit(RTHandle source){
        commands.add(new BlitCommand(source, this));
    }
    public void setRenderTarget(RTHandle target){
        commands.add(new SetRenderTargetCommand(target));
    }
    public void drawMesh(Mesh mesh, Matrix4f matrix, Material material){
        commands.add(new DrawMeshCommand(mesh, matrix, material, this));
    }

    public void execute(){
        Collections.reverse(commands);
        for(int i = 0; i < commands.size(); i++){
            commands.get(i).execute();
        }
        commands.clear();
    }
}
