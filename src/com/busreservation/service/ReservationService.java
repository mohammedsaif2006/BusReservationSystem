package com.busreservation.service;

public class ReservationService implements BookingOperations {

    @Override
    public void bookTicket() {
        System.out.println("Book Ticket Module");
    }

    @Override
    public void cancelTicket() {
        System.out.println("Cancel Ticket Module");
    }

    @Override
    public void searchBooking() {
        System.out.println("Search Booking Module");
    }

    @Override
    public void viewBookings() {
        System.out.println("View Bookings Module");
    }
}