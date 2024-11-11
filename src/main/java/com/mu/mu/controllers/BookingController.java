package com.mu.mu.controllers;

import com.google.gson.Gson;
import com.mu.mu.Repositories.BookRepository;
import com.mu.mu.config.DatabaseUtil;
import com.mu.mu.models.Booking;
import com.mu.mu.models.DateRequest;
import io.muserver.MuRequest;
import io.muserver.MuResponse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class BookingController {
    private static List<Booking> bookings = new ArrayList<>();

    public boolean handleHealth(MuRequest request, MuResponse response){
        response.headers().set("Content-Type", "application/json");
        response.write("{\"health\": \"ok\"}");
        response.status(200);
        return true;
    }

    public boolean handleBookingRequest(MuRequest request, MuResponse response) {
        response.headers().set("Content-Type", "application/json");
        try {
            String requestBody = request.readBodyAsString();
            Booking booking = new Gson().fromJson(requestBody, Booking.class);

            Map<String, Object> result = BookRepository.saveBookingToDatabase(booking);
            response.status((Integer) result.get("code"));
            response.write(String.format("{\"message\": \"%s\"}", result.get("message")));
            return true;
        } catch (Exception e) {
            response.status(500);
            response.write("{\"message\": \"Error processing booking: " + e.getMessage() + "\"}");
            return false;
        }
    }

    public boolean handleGetBookings(MuRequest request, MuResponse response) {
        try {

            String requestBody = request.readBodyAsString();
            DateRequest dateRequest = new Gson().fromJson(requestBody, DateRequest.class);
            List<Booking> bookingsForDate = BookRepository.getBookingsWithinDateRange(dateRequest.getStartDate(), dateRequest.getEndDate());

            response.contentType("application/json");
            response.write(new Gson().toJson(bookingsForDate));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            response.status(500);
            response.write("Error retrieving bookings");
            return false;
        }
    }
}
