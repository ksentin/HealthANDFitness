package com.example.healthfitness;

public class Dish {
    private String name;
    private int description;

    public Dish(String name, int description) {
        this.name = name;
        this.description = description;
    }

    public Dish() {
    }

    public String getName() {
        return name;
    }

    public int getDescription() {
        return description;
    }
}
