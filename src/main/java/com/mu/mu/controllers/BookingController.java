package com.mu.mu.controllers;

import com.google.gson.Gson;
import com.mu.mu.config.DatabaseUtil;
import com.mu.mu.models.Booking;
import io.muserver.MuRequest;
import io.muserver.MuResponse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookingController {
    private static List<Booking> bookings = new ArrayList<>();

    public boolean handleBookingRequest(MuRequest request, MuResponse response) {
        response.headers().set("Content-Type", "application/json");
        try {
            String requestBody = request.readBodyAsString();
            Booking booking = new Gson().fromJson(requestBody, Booking.class);

            saveBookingToDatabase(booking);

            response.write("{\"message\": \"Booking created successfully\"}");
            return true;
        } catch (Exception e) {
            response.status(500);
            response.write("{\"message\": \"Error processing booking: " + e.getMessage() + "\"}");
            return false;
        }
    }

    public boolean handleGetBookings(MuRequest request, MuResponse response) {
        try {
            String date = request.query().get("date");
            List<Booking> bookingsForDate = getBookingsForDate(date);

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

    private List<Booking> getBookingsForDate(String date) {
        List<Booking> result = new ArrayList<>();
        for (Booking booking : bookings) {
            if (booking.getDateTime().equals(date)) {
                result.add(booking);
            }
        }
        return result;
    }

    public void initializeDatabase() {
        try (Connection connection = DatabaseUtil.getConnection()) {
            String createTableQuery = "CREATE TABLE IF NOT EXISTS bookings (id SERIAL PRIMARY KEY, customer_name VARCHAR(255), table_size INT, booking_date VARCHAR(255))";
            connection.createStatement().execute(createTableQuery);
            System.out.println("Database initialized successfully.");
        } catch (SQLException e) {
            System.out.println("Error initializing the database: " + e.getMessage());
        }
    }

    private void saveBookingToDatabase(Booking booking) throws SQLException {
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "INSERT INTO bookings (customer_name, table_size, booking_date) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, booking.getCustomerName());
                preparedStatement.setInt(2, booking.getTableSize());
                preparedStatement.setObject(3, booking.getDateTime()); // Store LocalDateTime as a timestamp

                preparedStatement.executeUpdate();
            }
        }
    }
}
