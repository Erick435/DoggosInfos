package com.erick.doggosinfos;

import com.google.gson.annotations.SerializedName;

public class DogInfo {
    @SerializedName("name")
    private String name;
    @SerializedName("weight")
    private Weight weight;
    @SerializedName("height")
    private Height height;
    @SerializedName("life_span")
    private String lifeSpan;
    @SerializedName("bred_for")
    private String bredFor;
    @SerializedName("breed_group")
    private String breedGroup;

    // Getter methods

    @SerializedName("id")
    private int id;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Weight getWeight() {
        return weight;
    }

    public Height getHeight() {
        return height;
    }

    public String getLifeSpan() {
        return lifeSpan;
    }

    public String getBredFor() {
        return bredFor;
    }

    public String getBreedGroup() {
        return breedGroup;
    }

    // Inner classes for Weight and Height

    public static class Weight {
        @SerializedName("imperial")
        private String imperial;
        @SerializedName("metric")
        private String metric;

        // Getter methods

        public String getImperial() {
            return imperial;
        }

        public String getMetric() {
            return metric;
        }
    }

    public static class Height {
        @SerializedName("imperial")
        private String imperial;
        @SerializedName("metric")
        private String metric;

        // Getter methods

        public String getImperial() {
            return imperial;
        }

        public String getMetric() {
            return metric;
        }
    }
}

