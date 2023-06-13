package org.eteriaEngine.rendering.material;

import org.eteriaEngine.assets.AssetDatabase;
import org.eteriaEngine.rendering.Shader;
import org.eteriaEngine.rendering.Texture2D;
import org.eteriaEngine.rendering.enums.BlendingMode;
import org.eteriaEngine.rendering.enums.CullingMode;
import org.eteriaEngine.rendering.enums.DepthFunc;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.HashMap;

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
    private final HashMap<String, SetPropertyCMD<?>> commands = new HashMap<>();
    private int currentTexUnit = 0;

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

    //BLENDING
    public BlendingMode getBlendingMode() {
        return blendingMode;
    }
    public void setBlendingMode(BlendingMode blendingMode) {
        this.blendingMode = blendingMode;
    }

    public Shader getShader() {
        return shader;
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
        for (SetPropertyCMD<?> command : commands.values()) {
            command.applyCommand();
        }
        clear();
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
    public void clear(){
        commands.clear();
        uniformCache.clear();
        currentTexUnit = 0;
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
        CreateCommand(propertyName, PropertyType.VECTOR4F, value);
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

            SetPropertyCMD<T> propertyCommand;
            if(propertyType == PropertyType.TEXTURE_2D){
                propertyCommand = new SetPropertyCMD<>(varLocation, propertyType, value, currentTexUnit);
            }
            else {
                propertyCommand = new SetPropertyCMD<>(varLocation, propertyType, value);
            }

            if(commands.containsKey(propertyName)){
                commands.replace(propertyName, propertyCommand);
            }
            else{
                commands.put(propertyName, propertyCommand);
                if(propertyType == PropertyType.TEXTURE_2D){
                    currentTexUnit++;
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
