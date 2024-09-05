package Hospital_Management;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Doctor {
    private Connection connection;

    public Doctor(Connection connection) {
        this.connection = connection;
    }

    public void viewDoctor() throws SQLException {
        String query = "SELECT * FROM doctors";
        PreparedStatement preparedStatement  = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        System.out.println(" Doctors ");
        System.out.println("+------------+--------------------+------------------+");
        System.out.println("| Doctor Id  | Name               | Specialization   |");
        System.out.println("+------------+--------------------+------------------+");
        while(resultSet.next()){
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String specialization = resultSet.getString("specialization");
            System.out.printf("| %-10s | %-18s | %-16s |\n", id, name, specialization);
            System.out.println("+------------+--------------------+------------------+");
        }
    }

    public boolean checkDoctor(int id) throws SQLException {
        String query = "SELECT * FROM doctors WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.next();
    }
}
