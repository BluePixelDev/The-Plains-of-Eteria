package com.game.world.tiles;
import com.game.world.BiomeType;
import com.game.world.Tile;
import com.game.world.TileType;
import org.joml.Vector2f;

public class GrassTile extends Tile {
    public GrassTile(BiomeType biomeType){
        super(TileType.GRASS, biomeType);
        setTextureOffset(new Vector2f(0.0f, 1.0f));
    }
}
