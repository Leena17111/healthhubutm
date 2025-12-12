package com.secj3303.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "HHUTM_BMI_RECORD")
public class BmiRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Foreign key to Person
    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    @Column(nullable = false)
    private Double height; // in meters

    @Column(nullable = false)
    private Double weight; // in kg

    @Column(nullable = false)
    private Double bmiValue; // calculated BMI

    @Column(nullable = false)
    private String category;  // BMI category (Normal, Overweight, etc.)

    @Column(name = "record_date")
    private LocalDate date;

    public BmiRecord() {
        this.date = LocalDate.now();
    }

    public BmiRecord(Person person, Double height, Double weight, Double bmiValue, String category) {
        this.person = person;
        this.height = height;
        this.weight = weight;
        this.bmiValue = bmiValue;
        this.category = category;
        this.date = LocalDate.now();
    }

    // ===================
    // Getters & Setters
    // ===================

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getBmiValue() {
        return bmiValue;
    }

    public void setBmiValue(Double bmiValue) {
        this.bmiValue = bmiValue;
    }

    public String getCategory() {            
        return category;
    }

    public void setCategory(String category) { 
        this.category = category;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
