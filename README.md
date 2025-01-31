# Bar Management System

## Overview
This Bar Management System is a JavaFX-based desktop application designed for managing various operations in a medium-sized bar. The system integrates with MySQL for efficient data management.

## Features
- **Inventory Management**: Track stock levels and manage inventory efficiently.
- **Supplier Management**: Maintain supplier records and manage procurement.
- **Sales Management**: Process customer orders and track daily sales.
- **Billing System**: Generate and print invoices for customers.
- **Staff Management**: Manage staff records and access permissions.
- **Reports & Analytics**: View sales reports and business insights.

## Technologies Used
- **Frontend**: JavaFX
- **Backend**: Java
- **Database**: MySQL

## Installation & Setup
1. Clone the repository:
   ```sh
   git clone <your-repo-url>
   ```
2. Install MySQL and create a database:
   ```sql
   CREATE DATABASE bar_management;
   ```
3. Configure database connection in `application.properties` (or similar config file).
4. Run the application:
   ```sh
   mvn clean install
   java -jar target/bar-management.jar
   ```
## Future Improvements
- Improve reporting features

## Author
Pasindu Pabasara

## License
This project is licensed under the MIT License.
