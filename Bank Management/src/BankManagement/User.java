package BankManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {

    // Declare Interface instances and initialize with constructor
    private Connection connection;
    private Scanner scanner;

    public User(Connection connection, Scanner scanner) {
        this.scanner = scanner;
        this.connection = connection;
    }

    public void register() throws SQLException {
        scanner.nextLine();
        System.out.print("\n Full Name: ");
        String name = scanner.nextLine();
        System.out.print(" Email: ");
        String email = scanner.nextLine();
        System.out.print(" Password: ");
        String password = scanner.nextLine();

        if (user_exist(email)) {
            System.out.println(" User already exist ! ");
        } else {
            String query = "INSERT INTO User (full_name, email, password) VALUES(?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0)
                System.out.println(" Registration Successfully");
            else
                System.out.println(" Registration Failed");
        }
    }

    public String login() throws SQLException {
        scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        String query = "SELECT * FROM user WHERE email = ? AND password = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, email);
        preparedStatement.setString(2, password);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next())
            return email;
        else {
            System.out.println(" No account found !");
            return null;
        }
    }

    public boolean user_exist(String email) throws SQLException {
        String query = "SELECT * FROM user WHERE email = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, email);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.next();
    }
}


/**
String register_query = "INSERT INTO User(full_name, email, password) VALUES(?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(register_query);
            preparedStatement.setString(1, full_name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);
}

can be done by:
String query = "insert into user (full_name,email,password)"
        + "values('" + full_name + "' , '" + email + "' , '" + password + "' )";
		try {
            Statement statement = con.createStatement();
            int affected_rows = statement.executeUpdate(query);
            }
**/