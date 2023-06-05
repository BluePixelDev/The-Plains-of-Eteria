package org.eteriaEngine.rendering.material;

import org.eteriaEngine.assets.AssetDatabase;
import org.eteriaEngine.rendering.enums.BlendingMode;
import org.eteriaEngine.rendering.enums.CullingMode;
import org.eteriaEngine.rendering.enums.DepthFunc;
import org.eteriaEngine.rendering.Shader;
import org.eteriaEngine.rendering.Texture2D;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUseProgram;

/**
 * Specifies information sent to the shader.
 */
public class Material {
    private final Shader shader;

    private CullingMode culling = CullingMode.BACK;
    private DepthFunc depthFunc = DepthFunc.LESS_EQUAL;
    private BlendingMode blendingMode = BlendingMode.ADDITIVE;

    private final HashMap<String, Integer> uniformCache = new HashMap<>();
    private final HashMap<String, MaterialCommand<?>> commands = new HashMap<>();
    private int textureUnit = 0;

    public Material(String shaderName){
        this.shader = (Shader) AssetDatabase.loadAssetAtPath(shaderName);
    }
    public Material(Shader shader){
        this.shader = shader;
    }

    //CULLING
    public CullingMode getCulling() {
        return culling;
    }
    public void setCulling(CullingMode culling) {
        this.culling = culling;
    }

    //DEPTH FUNCTION
    public DepthFunc getDepthFunc() {
        return depthFunc;
    }
    public void setDepthFunc(DepthFunc depthFunc) {
        this.depthFunc = depthFunc;
    }

    public Shader getShader() {
        return shader;
    }
    int getFreeTextureUnit(){
        return textureUnit;
    }

    /**
     * Binds this material for current rendering.
     */
    public void bind(){
        glUseProgram(shader.getShaderID());
    }
    /**
     * Applies all commands queued inside the material.
     */
    public void apply(){
        for (MaterialCommand<?> command : commands.values()) {
            command.applyCommand();
        }
        applyCulling();
        applyDepth();
        applyBlending();
        clearCommands();
    }
    private void applyCulling(){
        glEnable(GL_CULL_FACE);
        switch (culling){
            case BACK -> glCullFace(GL_BACK);
            case FRONT -> glCullFace(GL_FRONT);
            case BOTH -> glDisable(GL_CULL_FACE);
        }
    }
    private void applyDepth(){
        glEnable(GL_DEPTH_TEST);
        switch (depthFunc){
            case NEVER -> glDepthFunc(GL_NEVER);
            case LESS -> glDepthFunc(GL_LESS);
            case EQUAL -> glDepthFunc(GL_EQUAL);
            case LESS_EQUAL -> glDepthFunc(GL_LEQUAL);
            case GREATER -> glDepthFunc(GL_GREATER);
            case NOT_EQUAL -> glDepthFunc(GL_NOTEQUAL);
            case GREATER_EQUAL -> glDepthFunc(GL_GEQUAL);
            case ALWAYS -> glDepthFunc(GL_ALWAYS);
        }
    }
    private void applyBlending(){
        glEnable(GL_BLEND);

        if(blendingMode == BlendingMode.ADDITIVE){
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        }
    }
    /**
     * Unbinds this material for current rendering.
     */
    public void unbind(){
        glUseProgram(0);
    }
    /**
     * Clears all commands stored inside this material.
     */
    public void clearCommands(){
        commands.clear();
        textureUnit = 0;
    }

    //---- PROPERTY COMMANDS ----
    public void setInt(String propertyName, int value){
        CreateCommand(propertyName, PropertyType.INT, value);
    }
    public void setFloat(String propertyName, float value){
        CreateCommand(propertyName, PropertyType.FLOAT, value);
    }
    public void setVector2f(String propertyName, Vector2f value){
        CreateCommand(propertyName, PropertyType.VECTOR2F, value);
    }
    public void setVector3f(String propertyName, Vector3f value){
        CreateCommand(propertyName, PropertyType.VECTOR3F, value);
    }
    public void setVector4f(String propertyName, Vector4f value){
        CreateCommand(propertyName, PropertyType.VECTOR4, value);
    }
    public void setMatrix4f(String propertyName, Matrix4f value){
        CreateCommand(propertyName, PropertyType.MATRIX4F, value);
    }
    public void setTexture(String propertyName, Texture2D value){
        CreateCommand(propertyName, PropertyType.TEXTURE_2D, value);
    }

    //Creates a new MaterialCommand
    private <T> void CreateCommand(String propertyName, PropertyType propertyType, T value){
        int varLocation = getPropertyLocation(propertyName, shader);
        if(varLocation != -1){

            MaterialCommand<T> propertyCommand;
            if(propertyType == PropertyType.TEXTURE_2D){
                propertyCommand = new MaterialCommand<>(this, varLocation, propertyType, value, textureUnit);
            }
            else {
                propertyCommand = new MaterialCommand<>(this, varLocation, propertyType, value);
            }

            if(commands.containsKey(propertyName)){
                commands.replace(propertyName, propertyCommand);
            }
            else{
                commands.put(propertyName, propertyCommand);
                if(propertyType == PropertyType.TEXTURE_2D){
                    textureUnit++;
                }
            }
        }
    }
    private int getPropertyLocation(String propertyName, Shader shader){
        if(!uniformCache.containsKey(propertyName)){
            int shaderProgramID = shader.getShaderID();
            uniformCache.put(propertyName, shaderProgramID);
        }

        return glGetUniformLocation(uniformCache.get(propertyName), propertyName);
    }
}
