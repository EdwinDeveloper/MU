Restaurant Booking Application Setup Guide

    Overview

        * Goal: A Java-based booking system for customers to book tables and owners to view bookings.
        * Tech Stack: Java, Mu Server, PostgreSQL, Docker.
    
    Setup Options

        * Docker: Run using docker-compose.
        * IntelliJ: Run locally with a PostgreSQL instance.

    Prerequisites
        * Java 21+
        * Docker and Docker Compose (for containerized setup)
        * IntelliJ IDEA (for local setup)
        * PostgreSQL (for local setup)
    
    Running with Docker
    
        * Build and Run:
        * docker-compose up --build
        * Starts containers for both app and PostgreSQL.
    
    Access:
        
        * Open http://localhost:8086

    Running Locally in IntelliJ
    
        * Start PostgreSQL:
            * Set up a PostgreSQL database with .env credentials.
        * Configure IntelliJ:
            * In application.properties:
            
            db.url=jdbc:postgresql://localhost:5432/your_database_name
            db.username=your_database_user
            db.password=your_database_password
        * Run:
            * In IntelliJ, right-click MuApplication and select Run 'MuApplication.main()'.
        * Access:
            *Visit http://localhost:8082
    

    Import the Postman Collection
    
        Download the provided Postman collection file located in "postman" folder HSBC.postman_collection.json
        Open Postman, go to File > Import, and select the file.
    
    API Endpoints

        * POST /bookings: Create a booking.

            {
                "customerName":"Name of the customer",
                "tableSize": 9,
                "tableNumber": 3,
                "dateTime": "2024-11-11 23:30:00"
            }

        * GET /bookings:  View bookings for a specific date.

            {
                "startDate": "2024-11-08 16:30:00",
                "endDate":"2024-11-20 16:30:00"
            }