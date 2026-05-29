package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "student")
public class Student {

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"controlNumber\"")
    private int controlNumber;
    private String name;
    @Column(name = "lastname")
    private String lastname;

    public int getControlNumber() {
        return controlNumber;
    }
    public void setControlNumber(int controlNumber) {
        this.controlNumber = controlNumber;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getLastname() {
        return lastname;
    }
    public void setLastname(String lastName) {
        this.lastname = lastName;
    }

    @Override
    public String toString(){
        return controlNumber + " :: " + name + " :: " + lastname;
    }
}
