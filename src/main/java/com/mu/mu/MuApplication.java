package com.mu.mu;

import com.mu.mu.Repositories.BookRepository;
import com.mu.mu.config.ApplicationConfig;
import com.mu.mu.controllers.BookingController;
import io.muserver.Method;
import io.muserver.MuServer;
import io.muserver.MuServerBuilder;

public class MuApplication {

	public static void main(String[] args) {
		BookingController bookingController = new BookingController();

		BookRepository.initializeDatabase();

		int serverPort = ApplicationConfig.getServerPort();

		MuServer server = MuServerBuilder.httpServer()
				.withHttpPort(serverPort)
				.addHandler(Method.POST, "/book", (request, response, pathParams) -> {
					bookingController.handleBookingRequest(request, response);
				})
				.addHandler(Method.GET, "/bookings", (request, response, pathParams) -> {
					bookingController.handleGetBookings(request, response);
				})
				.addHandler(Method.GET, "/health", (request, response, pathParams) -> {
					bookingController.handleHealth(request, response);
				})
				.start();

		System.out.println("Server started at " + server.uri());
	}

}
