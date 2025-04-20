package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
  private static Connection connection;

  // Establish connection with MySQL database
  public static Connection getConnection() throws SQLException {
    if (connection == null || connection.isClosed()) {
      connection = DriverManager.getConnection(
          "jdbc:mysql://localhost:3306/college_events",
          "root",
          "Password@123");
    }
    return connection;
  }
}

