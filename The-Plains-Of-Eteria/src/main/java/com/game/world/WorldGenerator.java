package com.game.world;

import org.eteriaEngine.Mathf;
import org.eteriaEngine.core.EngineUtility;
import com.library.OpenSimplexNoise;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Random;

public class WorldGenerator {
    private final WorldMap worldMap;
    private final long seed;
    private final Random random;


    //Biome Generation.
    private final ArrayList<BiomeNode> landBiomeNodes = new ArrayList<>();
    private final ArrayList<BiomeNode> waterBiomeNodes = new ArrayList<>();
    public float regionChunkSize = 1;
    private final OpenSimplexNoise humidityNoise;
    private final OpenSimplexNoise temperatureNoise;
    private final float humidityMapDensity = 4;
    private final float temperatureMapDensity = 5;

    //Land Generation.
    private final int worldHeight = 32;
    private final OpenSimplexNoise landNoise;
    private final float landMapDensity = 24;
    private final float detailHeightMapDensity = 4;

    public WorldGenerator(WorldMap tileMap) {
        this.worldMap = tileMap;
        seed = System.currentTimeMillis();
        random = new Random(seed);

        //Noise layers.
        landNoise = new OpenSimplexNoise(random.nextLong());
        humidityNoise = new OpenSimplexNoise(random.nextLong());
        temperatureNoise = new OpenSimplexNoise(random.nextLong());

        landBiomeNodes.add(new BiomeNode(BiomeType.GRASSLANDS, 0.6f, 0.5f));
        landBiomeNodes.add(new BiomeNode(BiomeType.BEACH, 0.8f, 0.6f));
        landBiomeNodes.add(new BiomeNode(BiomeType.DESERT, 0.2f, 1));
        waterBiomeNodes.add(new BiomeNode(BiomeType.OCEAN, 1, 0.5f));
    }
    public WorldGenerator(WorldMap tileMap, long seed) {
        this.worldMap = tileMap;
        this.seed = seed;
        random = new Random(seed);

        //Noise layers.
        landNoise = new OpenSimplexNoise(random.nextLong());
        humidityNoise = new OpenSimplexNoise(random.nextLong());
        temperatureNoise = new OpenSimplexNoise(random.nextLong());
    }

    /**
     * Generates chunks at specified position.
     * @param width  The half extend in chunks.
     * @param height The half extend in chunks.
     */
    public void generateArea(Vector3f position, int width, int height) {
        //Caches tile size and chunk size.
        float tileScale = worldMap.getTileScale();
        int chunkSize = worldMap.getChunkTileSize();

        for (int x = 0; x < width + 1; x++) {
            for (int y = 0; y < height + 1; y++) {
                //Calculates final position of the chunk.
                Vector3f finalPosition = new Vector3f(
                        position.x + (-width / 2f + x) * chunkSize / 2f * tileScale,
                        position.y + (-height / 2f + y) * chunkSize / 2f * tileScale,
                        0
                );
                //Generates new chunk with specified position, tile size and chunk size.
                Chunk generatedChunk = generateChunk(finalPosition);
                //Adds generated chunk to list of loaded chunks.
                worldMap.addChunk(generatedChunk);
            }
        }
    }
    /**
     * Generates single chunk at specified position with specified tile size and chunk size.
     * @param position Position in the world.
     */
    public Chunk generateChunk(Vector3f position) {
        //Caches tile size and chunk size.
        int chunkSize = worldMap.getChunkTileSize();

        Chunk chunk = new Chunk(position, chunkSize, worldMap);
        chunk.fill();

        for (int x = 0; x < chunkSize; x++) {
            for (int y = 0; y < chunkSize; y++) {
                Vector3f tilePosition = new Vector3f(position);
                tilePosition.add(chunk.getTile(x, y).getPosition());
                Tile tile = generateTile(tilePosition);
                chunk.replaceTile(x, y, tile);
            }
        }
        chunk.updateMesh();
        return chunk;
    }
    /**
     * Generates a single tile based on tile and BiomePoints.
     * @return A new generated tile.
     */
    private Tile generateTile(Vector3f position) {
        TileType targetTileType = TileType.WATER;
        BiomeType targetBiomeType = BiomeType.OCEAN;

        float height = getTileHeight(position);
        BiomeType biomeType = getTileBiome(position, height);

        if(biomeType == BiomeType.GRASSLANDS){
            targetTileType = TileType.GRASS;
        }
        if(biomeType == BiomeType.DESERT){
            targetTileType = TileType.SAND;
        }
        if(biomeType == BiomeType.BEACH){
            targetTileType = TileType.SAND;
        }

        return TileFactory.GetTile(targetTileType, targetBiomeType);
    }
    /**
     * Generates height by combining noises.
     * @return Height of the tile.
     */
    public float getTileHeight(Vector3f position){

        float baseHeightMap = (float) landNoise.eval(
                (position.x / worldMap.getTileScale()) / landMapDensity,
                (position.y / worldMap.getTileScale()) / landMapDensity, 0d
        );

        float detailHeightMap = (float) landNoise.eval(
                (position.x / worldMap.getTileScale()) / detailHeightMapDensity,
                (position.y / worldMap.getTileScale()) / detailHeightMapDensity, 0d
        );

        float heightMapDetailed = Mathf.lerp(baseHeightMap, detailHeightMap, (Math.abs(baseHeightMap) + 1) / 2 * 0.25f);
        return heightMapDetailed;
    }
    public BiomeType getTileBiome(Vector3f position, float height){

        float humidity = (float) humidityNoise.eval(
                (position.x / worldMap.chunkUnitSize()) / humidityMapDensity,
                (position.y / worldMap.chunkUnitSize()) / humidityMapDensity
        );
        float temperature = (float)temperatureNoise.eval(
                (position.x / worldMap.chunkUnitSize()) / temperatureMapDensity,
                (position.y / worldMap.chunkUnitSize()) / temperatureMapDensity
        );

        //Caches values for easier access.
        humidity = (humidity + 1) / 2;
        temperature = (temperature + 1) / 2;

        BiomeType targetBiome = BiomeType.OCEAN;
        float minCost = Float.MAX_VALUE;
        var biomeNodes = height >= 0 ? landBiomeNodes : waterBiomeNodes;

        for (BiomeNode node: biomeNodes) {
            float nodeCost = node.getHumidity() + node.getTemperature();
            float currentCost = humidity + temperature;
            float cost = Math.abs(nodeCost - currentCost);

            if(cost < minCost){
                targetBiome = node.getBiomeType();
                minCost = cost;
            }
        }

        height = Mathf.ceil(height * worldHeight);
        if(height > 0 && height < 3){
            targetBiome = BiomeType.BEACH;
        }
        return targetBiome;
    }
}