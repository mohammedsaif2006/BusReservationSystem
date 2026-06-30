package com.busreservation.model;

public class Bus {

    private int busId;
    private String busName;
    private String source;
    private String destination;
    private double fare;
    private int totalSeats;
    private int availableSeats;

    public Bus() {
    }

    public Bus(int busId, String busName, String source,
               String destination, double fare,
               int totalSeats, int availableSeats) {

        this.busId = busId;
        this.busName = busName;
        this.source = source;
        this.destination = destination;
        this.fare = fare;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
    }

    public int getBusId() {
        return busId;
    }

    public void setBusId(int busId) {
        this.busId = busId;
    }

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public double getFare() {
        return fare;
    }

    public void setFare(double fare) {
        this.fare = fare;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }
}