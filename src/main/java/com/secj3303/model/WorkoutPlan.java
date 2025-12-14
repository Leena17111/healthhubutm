package com.secj3303.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "HHUTM_WORKOUT_PLAN")
public class WorkoutPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "program_id")
    private Program program; // plans are linked to program

    private Integer weekNumber; // weekly plan number

    @Column(length = 500)
    private String description;

    public WorkoutPlan() {}

    // Getters + Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Program getProgram() { return program; }
    public void setProgram(Program program) { this.program = program; }

    public Integer getWeekNumber() { return weekNumber; }
    public void setWeekNumber(Integer weekNumber) { this.weekNumber = weekNumber; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
