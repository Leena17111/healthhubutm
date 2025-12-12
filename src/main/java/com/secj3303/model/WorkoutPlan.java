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
@Table(name="HHUTM_WORKOUT_PLAN")
public class WorkoutPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="trainer_id")
    private Person trainer;

    @ManyToOne
    @JoinColumn(name="member_id")
    private Person member;

    @Column(length=500)
    private String description;

    private Integer weekNumber;
    private LocalDate createdAt;

    public WorkoutPlan() {}

    // Getters and setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Person getTrainer() { return trainer; }
    public void setTrainer(Person trainer) { this.trainer = trainer; }

    public Person getMember() { return member; }
    public void setMember(Person member) { this.member = member; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getWeekNumber() { return weekNumber; }
    public void setWeekNumber(Integer weekNumber) { this.weekNumber = weekNumber; }

    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }
}
