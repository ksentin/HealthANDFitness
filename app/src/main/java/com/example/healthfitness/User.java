package com.example.healthfitness;

public class User {
    private String email, password, name;
    private int age, weight, height;

    public User(String name, int age, int weight, int height, String email, String password) {
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public int getWeight() {
        return weight;
    }

    public int getHeight() {
        return height;
    }
}
