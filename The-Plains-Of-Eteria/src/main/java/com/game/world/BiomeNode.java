package com.game.world;

public class BiomeNode {
    private BiomeType biomeType;
    private double humidity;
    private double temperature;

    public BiomeNode(BiomeType biomeType, double humidity, double temperature) {
        this.biomeType = biomeType;
        this.humidity = humidity;
        this.temperature = temperature;
    }

    public BiomeType getBiomeType() {
        return biomeType;
    }
    public double getHumidity() {
        return humidity;
    }
    public double getTemperature() {
        return temperature;
    }
}
