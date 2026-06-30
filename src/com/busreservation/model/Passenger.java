package com.busreservation.model;

public class Passenger extends Person {

    private int passengerId;

    public Passenger() {
    }

    public Passenger(int passengerId, String name, int age, String gender, String phone) {
        super(name, age, gender, phone);
        this.passengerId = passengerId;
    }

    public int getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(int passengerId) {
        this.passengerId = passengerId;
    }
}