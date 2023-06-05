package org.eteriaEngine.rendering;
import org.eteriaEngine.rendering.material.Material;
import org.eteriaEngine.rendering.mesh.Mesh;

public class RenderBatch {

    //---- VERTEX POSITIONS ----
    int positionsSize = 3;
    int colorSize = 4;
    int uvSize = 2;

    public final int VERTEX_SIZE = (positionsSize + colorSize + uvSize) * Float.BYTES;

    //---- POINTERS ----
    public int getPositionPointer() {
        return 0;
    }

    public int getColorPointer() {
        return positionsSize * Float.BYTES;
    }

    public int getUvPointer() {
        return (colorSize + positionsSize) * Float.BYTES;
    }

    private Mesh mesh;
    private Material material;
    private Renderer[] renderers;
    private float[] vertices;
    private int maxBatchSize;

    public RenderBatch(Material material, Renderer[] renderers, int maxBatchSize) {
        this.material = material;
        this.renderers = renderers;
        this.maxBatchSize = maxBatchSize;

        mesh = new Mesh();
        mesh.markDynamic(true);
    }

    /*
    public void updateMesh(){
        mesh.setVertices(generateVertices());
        mesh.setElements(generateIndices());
    }
    private float[] generateVertices(){
        float[] vertices = new float[chunkTileSize * chunkTileSize * VERTEX_SIZE * 4];
        for (int x = 0; x < chunkTileSize; x++){
            for (int y = 0; y < chunkTileSize; y++){
                Tile tile = tiles[x][y];
                int index = x * chunkTileSize + y;
                loadVertexProperties(vertices, index, tile);
            }
        }
        return vertices;
    }
    private void loadVertexProperties(float[] vertices, int index, Tile tile){
        int offset = index * VERTEX_SIZE * 4;

        for(int i = 0; i < 4; i++){

            Vector4f pos = new Vector4f(0.0f, 0.0f, 0.0f, 1.0f);
            Vector2f uv = new Vector2f();

            //Assigns Vertex coordinates
            //(-0.5, 0.5)  * --- * (0.5, 0.5)
            //             |     |
            //(-0.5, -0.5) * --- * (0.5, -0.5)
            //Assigns UV coordinates
            //(0,0)  * --- * (1, 0)
            //       |     |
            //(0, 1) * --- * (1, 1)
            if (i == 0){
                pos.x = 0.5f;
                pos.y = -0.5f;

                uv.x = 1.0f;
                uv.y = 1.0f;
            } else if (i == 1) {
                pos.x = -0.5f;
                pos.y = -0.5f;

                uv.x = 1.0f;
                uv.y = 0.0f;
            } else if (i == 2) {
                pos.x = -0.5f;
                pos.y = 0.5f;

                uv.x = 0.0f;
                uv.y = 0.0f;
            } else {
                pos.x = 0.5f;
                pos.y = 0.5f;

                uv.x = 0.0f;
                uv.y = 1.0f;
            }

            pos.mul(tile.getTransform());
            pos.mul(getTransform());

            vertices[offset] = pos.x;
            vertices[offset + 1] = pos.y;

            vertices[offset + 3] = tile.getTextureOffset().x / 16f;
            vertices[offset + 4] = tile.getTextureOffset().y / 16f;
            vertices[offset + 5] = tile.getPosition().z;

            vertices[offset + 7] = uv.x;
            vertices[offset + 8] = uv.y;


            offset += VERTEX_SIZE;
        }
    }
    private int[] generateIndices(){
        int[] elements = new int[chunkTileSize * chunkTileSize * 6];
        for(int i = 0; i < elements.length / 6; i++){
            loadElementIndices(elements, i);
        }
        return elements;
    }
    private void loadElementIndices(int[] elements, int index){
        int offsetArrayIndex = 6 * index;
        int offset = 4 * index;

        elements[offsetArrayIndex] = offset + 3;
        elements[offsetArrayIndex + 1] = offset + 2;
        elements[offsetArrayIndex + 2] = offset;

        elements[offsetArrayIndex + 3] = offset;
        elements[offsetArrayIndex + 4] = offset + 2;
        elements[offsetArrayIndex + 5] = offset + 1;
    }
    */
}
