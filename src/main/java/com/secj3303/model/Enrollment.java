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
@Table(name = "HHUTM_ENROLLMENT")
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Person member;

    @ManyToOne
    @JoinColumn(name = "program_id", nullable = false)
    private Program program;

   @Column(name = "enrollment_date")
private LocalDate enrollmentDate = LocalDate.now();


    public Enrollment() {}

   public Enrollment(Person member, Program program) {
    this.member = member;
    this.program = program;
    this.enrollmentDate = LocalDate.now();
}


    // GETTERS & SETTERS
    
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Person getMember() { return member; }
    public void setMember(Person member) { this.member = member; }

    public Program getProgram() { return program; }
    public void setProgram(Program program) { this.program = program; }

    public LocalDate getEnrollmentDate() { return enrollmentDate; }
    public void setEnrollmentDate(LocalDate enrollmentDate) { this.enrollmentDate = enrollmentDate; }
}
