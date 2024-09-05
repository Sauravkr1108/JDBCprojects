package BankManagement;

import java.sql.*;
import java.util.Scanner;

public class Accounts {

    private Connection connection;
    private Scanner scanner;
    public Accounts(Connection connection, Scanner scanner) {
        this.scanner = scanner;
        this.connection = connection;
    }

    public long open_account(String email) throws SQLException {
        int rowsAffected = 0;
        long account_no = 0;
        if(!account_exist(email)) {
            scanner.nextLine();
            System.out.print("\n Full Name: ");
            String name = scanner.nextLine();
            System.out.print(" Enter Initial Amount: ");
            double balance = scanner.nextDouble();
            scanner.nextLine();
            System.out.print(" Security PIN: ");
            String pin = scanner.nextLine();

            String query = "INSERT INTO accounts (account_number, full_name, email, balance, security_pin) VALUES (?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            account_no = generateAccount_number();
            preparedStatement.setLong(1, account_no);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, email);
            preparedStatement.setDouble(4, balance);
            preparedStatement.setString(5, pin);
            rowsAffected = preparedStatement.executeUpdate();
        }
        if(rowsAffected>0)
            return account_no;
        else throw new RuntimeException("Account Creation Failed");
    }

    public long getAccount_number(String email) throws SQLException {
        String query = "SELECT * FROM accounts WHERE email = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, email);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next())
            return resultSet.getLong("account_number");
        else throw new RuntimeException("Account Number doesn't exist");
    }

    public long generateAccount_number() throws SQLException {
        String query = "SELECT account_number FROM accounts ORDER BY account_number DESC LIMIT 1";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        if(resultSet.next()) {
            long account_no = resultSet.getLong(1);
            return (account_no + 1);
        }
        else
            return 10000100;
    }

    public boolean account_exist(String email) throws SQLException {
        String query = "SELECT * FROM accounts WHERE email = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, email);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.next();
    }
}
