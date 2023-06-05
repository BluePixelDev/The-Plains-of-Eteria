package com.game.world;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Tile {
    private Vector3f position = new Vector3f();
    private Vector2f textureOffset = new Vector2f();

    private final TileType tileType;
    private final BiomeType biomeType;

    public Tile(TileType tileType, BiomeType biomeType) {
        this.tileType = tileType;
        this.biomeType = biomeType;
    }

    //---- POSITION ----
    public Vector3f getPosition() {
        return new Vector3f(position);
    }
    public void setPosition(Vector3f position){
        this.position = new Vector3f(position);
    }

    //---- TEXTURE OFFSET ----
    public Vector2f getTextureOffset() {
        return new Vector2f(textureOffset);
    }
    public void setTextureOffset(Vector2f textureOffset) {
        this.textureOffset = textureOffset;
    }

    ///--- TILE AND BIOME TYPE ----
    public TileType getTileType() {
        return tileType;
    }
    public BiomeType getBiomeType() {
        return biomeType;
    }

    //---- LOCAL TRANSFORM ----
    public Matrix4f getTransform(){
        Matrix4f mat4 = new Matrix4f();
        mat4.identity();
        mat4.translate(position);
        return mat4;
    }
}
