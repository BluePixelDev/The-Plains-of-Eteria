package com.game.world;
import org.eteriaEngine.rendering.mesh.Mesh;
import org.joml.*;

public class Chunk {
    private final Vector3f position;
    private final int chunkTileSize;
    private final WorldMap worldMap;
    private final Mesh mesh;
    private final Tile[][] tiles;

    public Chunk(Vector3f position, int chunkSize, WorldMap worldMap){
        mesh = new Mesh();
        mesh.markDynamic(true);

        this.worldMap = worldMap;
        this.chunkTileSize = chunkSize;
        this.position = new Vector3f(position);
        this.tiles = new Tile[chunkSize][chunkSize];
    }
    public void updateMesh(){
        rebuildMesh();
    }
    private void rebuildMesh(){
        Vector3f[] vertices = new Vector3f[chunkTileSize * chunkTileSize * 4];
        Vector4f[] colors = new Vector4f[chunkTileSize * chunkTileSize * 4];
        Vector2f[] UVs = new Vector2f[chunkTileSize * chunkTileSize * 4];

        Vector3i[] indices = new Vector3i[chunkTileSize * chunkTileSize * 2];

        for (int x = 0; x < chunkTileSize; x++){
            for (int y = 0; y < chunkTileSize; y++){
                Tile tile = tiles[x][y];
                int index = x * chunkTileSize + y;

                generateVertices(vertices, index, tile);
                generateColors(colors, index, tile);
                generateUVs(UVs, index);
                generateIndices(indices, index);
            }
        }

        mesh.setVertices(vertices);
        mesh.setColors(colors);
        mesh.setUVs(UVs);
        mesh.setElements(indices);
    }

    private void generateVertices(Vector3f[] vertices, int index, Tile tile){
        int offset = index * 4;
        vertices[offset]     = new Vector3f(0.5f, -0.5f, 0.0f).add(tile.getPosition()).add(position);
        vertices[offset + 1] = new Vector3f(-0.5f, -0.5f, 0.0f).add(tile.getPosition()).add(position);
        vertices[offset + 2] = new Vector3f(-0.5f, 0.5f, 0.0f).add(tile.getPosition()).add(position);
        vertices[offset + 3] = new Vector3f(0.5f, 0.5f, 0.0f).add(tile.getPosition()).add(position);
    }
    private void generateColors(Vector4f[] colors, int index, Tile tile){
        Vector4f color = new Vector4f(
                tile.getTextureOffset().x / 16f,
                tile.getTextureOffset().y / 16f,
                tile.getPosition().z,
                1.0f
        );

        int offset = index * 4;
        colors[offset] = color;
        colors[offset + 1] = color;
        colors[offset + 2] = color;
        colors[offset + 3] = color;
    }
    private void generateUVs(Vector2f[] UVs, int index){
        int offset = index * 4;
        UVs[offset]     = new Vector2f(1.0f, 1.0f);
        UVs[offset + 1] = new Vector2f(0.0f, 0.0f);
        UVs[offset + 2] = new Vector2f(1.0f, 0.0f);
        UVs[offset + 3] = new Vector2f(0.0f, 1.0f);
    }
    private void generateIndices(Vector3i[] indices, int index){
        int arrayOffset = index * 2;
        int offset = index * 4;
        indices[arrayOffset] = new Vector3i(offset + 3, offset + 2, offset);
        indices[arrayOffset + 1] = new Vector3i(offset, offset + 2, offset + 1);
    }

    //---- API ----
    public Mesh getMesh() {
        return mesh;
    }
    public Tile[][] getTiles() {
        return tiles;
    }
    /**
     * Replaces tile inside chunk with different tile.
     * Tile is automatically placed in the position of the previous tile.
     * @param x X chunk index position of the tile.
     * @param y Y chunk index position of the tile.
     * @param tile The tile we want to replace it with.
     */
    public void replaceTile(int x, int y, Tile tile){
        //Checks whether the X index position is out of bounds.
        if(x < 0){
            return;
        }
        if(x > tiles.length-1){
            return;
        }
        //Checks whether the Y index position is out of bounds.
        if(y < 0){
            return;
        }
        if(y > tiles[x].length-1){
            return;
        }
        //Sets position of the replacement tile.
        tile.setPosition(new Vector3f(tiles[x][y].getPosition()));
        tiles[x][y] = tile;
    }
    /**
     * Returns tile at index position in the chunk.
     * @param x X chunk index position of the tile.
     * @param y Y chunk index position of the tile.
     * @return Tile which is at the index position.
     */
    public Tile getTile(int x, int y){
        //Checks whether the X index position is out of bounds.
        if(x < 0){
            return null;
        }
        if(x > tiles.length-1){
            return null;
        }
        //Checks whether the Y index position is out of bounds.
        if(y < 0){
            return null;
        }
        if(y > tiles[x].length-1){
            return null;
        }
        return tiles[x][y];
    }
    /**
     * Fills out the chunk with Tiles in the right positions.
     */
    public void fill(){
        for (int x = 0; x < chunkTileSize; x++){
            for (int y = 0; y < chunkTileSize; y++){
                Tile tile = TileFactory.GetTile(TileType.WATER, BiomeType.OCEAN);
                tile.setPosition(new Vector3f(
                        (-chunkTileSize / 2f + x) * worldMap.getTileScale(),
                        (-chunkTileSize / 2f + y) * worldMap.getTileScale(),
                        0
                ));
                tiles[x][y] = tile;
            }
        }
    }
    /**
     * @return Transformation matrix of this chunk.
     */
    public Matrix4f getTransform(){
        Matrix4f mat4 = new Matrix4f();
        mat4.identity();
        mat4.translate(position);
        return mat4;
    }
}
