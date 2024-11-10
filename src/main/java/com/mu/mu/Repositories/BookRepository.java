package com.mu.mu.Repositories;

import com.mu.mu.config.DatabaseUtil;
import com.mu.mu.models.Booking;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookRepository {

    public static void saveBookingToDatabase(Booking booking) throws SQLException {
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "INSERT INTO bookings (customer_name, table_size, booking_date) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, booking.getCustomerName());
                preparedStatement.setInt(2, booking.getTableSize());
                preparedStatement.setObject(3, booking.getDateTime());

                preparedStatement.executeUpdate();
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
            String createTableQuery = "CREATE TABLE IF NOT EXISTS bookings (id SERIAL PRIMARY KEY, customer_name VARCHAR(255), table_size INT, booking_date VARCHAR(255))";
            connection.createStatement().execute(createTableQuery);
            System.out.println("Database initialized successfully.");
        } catch (SQLException e) {
            System.out.println("Error initializing the database: " + e.getMessage());
        }
    }

}
