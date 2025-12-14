package com.secj3303.model;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "hhutm_training_session")
public class TrainingSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "trainer_id")
    private Person trainer;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Person member;


    @Column(name = "sessionDate")
    private LocalDate sessionDate;

    @Column(name = "sessionTime")
    private LocalTime sessionTime;

    @Column(length = 300)
    private String notes;

    public TrainingSession() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Person getTrainer() { return trainer; }
    public void setTrainer(Person trainer) { this.trainer = trainer; }

    public Person getMember() { return member; }
    public void setMember(Person member) { this.member = member; }

    public LocalDate getSessionDate() { return sessionDate; }
    public void setSessionDate(LocalDate sessionDate) { this.sessionDate = sessionDate; }

    public LocalTime getSessionTime() { return sessionTime; }
    public void setSessionTime(LocalTime sessionTime) { this.sessionTime = sessionTime; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
