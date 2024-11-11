package com.mu.mu.Repositories;

import com.mu.mu.config.DatabaseUtil;
import com.mu.mu.models.Booking;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookRepository {

    public static Map<String, Object> saveBookingToDatabase(Booking booking) throws SQLException {
        Map<String, Object> responseResult = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try (Connection connection = DatabaseUtil.getConnection()) {
            String checkQuery = "SELECT booking_date FROM bookings WHERE table_number = ? ORDER BY booking_date DESC LIMIT 1";

            try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
                checkStatement.setInt(1, booking.getTableNumber());
                ResultSet resultSet = checkStatement.executeQuery();

                if (resultSet.next()) {
                    String lastBookingDateString = resultSet.getString("booking_date");

                    try {
                        LocalDateTime lastBookingDateTime = LocalDateTime.parse(lastBookingDateString, formatter);
                        LocalDateTime twoHoursAgo = LocalDateTime.parse(booking.getDateTime(), formatter).minusHours(2);

                        if (lastBookingDateTime.isAfter(twoHoursAgo)) {
                            responseResult.put("code", 400);
                            responseResult.put("message", "A booking for this table was made within the last 2 hours.");
                            return responseResult;
                        }
                    } catch (DateTimeParseException e) {
                        responseResult.put("code", 400);
                        responseResult.put("message", "Error parsing the booking_date: " + e.getMessage());
                        return responseResult;
                    }
                }
            }

            String insertQuery = "INSERT INTO bookings (table_number, customer_name, table_size, booking_date) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setInt(1, booking.getTableNumber());
                preparedStatement.setString(2, booking.getCustomerName());
                preparedStatement.setInt(3, booking.getTableSize());
                preparedStatement.setObject(4, booking.getDateTime());

                preparedStatement.executeUpdate();
                responseResult.put("code", 200);
                responseResult.put("message", "Booking saved successfully");
                return responseResult;
            }
        }
    }

    public static List<Booking> getBookingsWithinDateRange(String startDate, String endDate) {
        List<Booking> bookings = new ArrayList<>();

        String query = "SELECT * FROM bookings WHERE booking_date BETWEEN ? AND ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, startDate);
            ps.setString(2, endDate);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Booking booking = new Booking();
                    booking.setCustomerName(rs.getString("customer_name"));
                    booking.setTableSize(rs.getInt("table_size"));
                    booking.setDateTime(rs.getString("booking_date"));
                    booking.setTableNumber(rs.getInt("table_number"));
                    bookings.add(booking);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    public static void initializeDatabase() {
        try (Connection connection = DatabaseUtil.getConnection()) {
            String createTableQuery = "CREATE TABLE IF NOT EXISTS bookings (id SERIAL PRIMARY KEY, table_number INT, customer_name VARCHAR(255), table_size INT, booking_date VARCHAR(255))";
            connection.createStatement().execute(createTableQuery);
            System.out.println("Database initialized successfully.");
        } catch (SQLException e) {
            System.out.println("Error initializing the database: " + e.getMessage());
        }
    }

}
