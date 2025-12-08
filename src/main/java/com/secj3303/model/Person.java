package com.secj3303.model;

public class Person {

    private int id;         // primary key
    private String name;
    private int yob;
    private int age;
    private double weight;
    private double height;
    private double bmi;
    private String category;

    public Person() {}

    public Person(int id, String name, int yob, int age, double weight, double height, double bmi, String category) {
        this.id = id;
        this.name = name;
        this.yob = yob;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.bmi = bmi;
        this.category = category;
    }

    public Person(String name, int yob, double weight, double height, String[] something) {
    this.name = name;
    this.yob = yob;
    this.weight = weight;
    this.height = height;
}


    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getYob() { return yob; }
    public void setYob(int yob) { this.yob = yob; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public double getHeight() { return height; }
    public void setHeight(double height) { this.height = height; }

    public double getBmi() { return bmi; }
    public void setBmi(double bmi) { this.bmi = bmi; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}
