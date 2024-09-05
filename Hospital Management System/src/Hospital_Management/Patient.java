package Hospital_Management;

import java.sql.*;
import java.util.Scanner;

public class Patient  {
    private Connection connection;
    private Scanner scanner;

    public Patient(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void addPatient() throws SQLException {
        System.out.print(" Patient Name: ");
        String name = scanner.next();
        System.out.print(" Patient Age: ");
        int age = scanner.nextInt();
        System.out.print(" Patient Gender: ");
        String gender = scanner.next();

        String query = "INSERT INTO patients (name, age, gender) VALUES (?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, name);
        preparedStatement.setInt(2, age);
        preparedStatement.setString(3, gender);

        int rowsAffected = preparedStatement.executeUpdate();
        if(rowsAffected>0)
            System.out.println(" Patient added successfully");
        else
            System.out.println(" Failed to add patient");
    }

    public void viewPatient() throws SQLException {
        String query = "SELECT * FROM patients";
        PreparedStatement preparedStatement  = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        System.out.println(" Patients ");
        System.out.println("+------------+--------------------+----------+------------+");
        System.out.println("| Patient Id | Name               | Age      | Gender     |");
        System.out.println("+------------+--------------------+----------+------------+");
        while (resultSet.next()) {
            int id = resultSet.getInt(1);
            String name = resultSet.getString(2);
            int age = resultSet.getInt(3);
            String gender = resultSet.getString(4);
            System.out.printf("| %-10s | %-18s | %-8s | %-10s |\n", id, name, age, gender);
            System.out.println("+------------+--------------------+----------+------------+");
        }
    }

    public boolean checkPatient(int id) throws SQLException {
        String query = "SELECT * FROM patients WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.next();
    }
}
