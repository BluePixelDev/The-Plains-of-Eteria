package com.game.world;

import org.eteriaEngine.assets.AssetDatabase;
import org.eteriaEngine.components.GameObject;
import org.eteriaEngine.rendering.Renderer;
import org.eteriaEngine.rendering.Shader;
import org.eteriaEngine.rendering.camera.Camera;
import org.eteriaEngine.rendering.commands.CommandBuffer;
import org.eteriaEngine.rendering.material.Material;
import org.eteriaEngine.rendering.mesh.Mesh;
import org.eteriaEngine.rendering.enums.FilterMode;
import org.eteriaEngine.rendering.Texture2D;
import org.joml.Vector2f;

public class WorldMapRenderer extends Renderer {
    private WorldMap worldMap;
    private Texture2D texture;
    private float tilingFactor = 1f;
    private final int tileMapSize = 16;

    public WorldMapRenderer(GameObject gameObject) {
        super(gameObject);
    }
    public void start(){
        worldMap = getComponent(WorldMap.class);
        texture = (Texture2D) AssetDatabase.loadAssetAtPath("assets/textures/tile_texture_atlas.png");
        texture.setFilterMode(FilterMode.POINT);
        Shader shader = (Shader) AssetDatabase.loadAssetAtPath("assets/shaders/tilemap.glsl");

        tilingFactor = 1f/tileMapSize;
        tilingFactor -= tilingFactor % 2;

        Material mat = new Material(shader);
        setMaterial(mat);
    }

    @Override
    public void render(Camera camera, CommandBuffer commandBuffer) {
        Material material = getMaterial();

        if(material == null){
            return;
        }

        material.setTexture("_mainTex", texture);
        material.setVector2f("_tiling", new Vector2f(tilingFactor, tilingFactor));

        for (Chunk chunk : worldMap.getChunks()) {
            Mesh chunkMesh = chunk.getMesh();
            commandBuffer.drawMesh(chunkMesh, transform().getTransformationMatrix(), material);
        }
    }
}
