package BankManagement;

import java.sql.*;
import java.util.Scanner;

public class AccountManager {

    private Connection connection;
    private  Scanner scanner;
    public AccountManager(Connection connection, Scanner scanner) {
        this.scanner = scanner;
        this.connection = connection;
    }

    public void credit_money(long account_number) throws SQLException {
        scanner.nextLine();
        System.out.print("\n Enter Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print(" Enter Security PIN: ");
        String pin = scanner.nextLine();
        connection.setAutoCommit(false);
        if(account_number!=0) {
            String query = "SELECT * FROM accounts WHERE account_number = ? AND security_pin = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, account_number);
            preparedStatement.setString(2, pin);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String credit_query = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
                PreparedStatement preparedStatement1 = connection.prepareStatement(credit_query);
                preparedStatement1.setDouble(1, amount);
                preparedStatement1.setLong(2, account_number);
                int rowsAffected = preparedStatement1.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Rs." + amount + " Credited successfully");
                    connection.commit();
                    connection.setAutoCommit(true);
                    return; //Remove it once
                } else {
                    System.out.println(" Transaction Failed");
                    connection.rollback();
                    connection.setAutoCommit(true);
                }
            } else
                System.out.println(" Invalid PIN !!");
        }
        connection.setAutoCommit(true);
    }

    public void debit_money(long account_number) throws SQLException {
        scanner.nextLine();
        System.out.print("\n Enter Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print(" Enter Security PIN: ");
        String pin = scanner.nextLine();
        connection.setAutoCommit(false);
        if(account_number!=0) {
            String query = "SELECT * FROM accounts WHERE account_number = ? AND security_pin = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, account_number);
            preparedStatement.setString(2, pin);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                double current_balance = resultSet.getDouble("balance");
                if(amount<=current_balance) {
                    String debit_query = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
                    PreparedStatement preparedStatement1 = connection.prepareStatement(debit_query);
                    preparedStatement1.setDouble(1,amount);
                    preparedStatement1.setLong(2,account_number);
                    int rowsAffected = preparedStatement1.executeUpdate();

                    if(rowsAffected>0) {
                        System.out.println("Rs." + amount + " Debited successfully");
                        connection.commit();
                        connection.setAutoCommit(true);
                        return;
                    }
                    else {
                        System.out.println(" Transaction Failed");
                        connection.rollback();
                        connection.setAutoCommit(true);
                    }
                }
                else
                    System.out.println(" Insufficient Balance !!");
            }
            else
                System.out.println(" Invalid PIN !!");
        }
        connection.setAutoCommit(true);
    }

    public void transfer_money(long sender_account_number) throws SQLException {
        scanner.nextLine();
        System.out.print("\n Enter the Receiver Account Number: ");
        long receiver_account_number = scanner.nextLong();
        System.out.print(" Enter Amount to transfer: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print(" Enter PIN: ");
        String pin = scanner.nextLine();
        connection.setAutoCommit(false);
        if(sender_account_number!=0 && receiver_account_number!=0) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM accounts WHERE account_number = ? AND security_pin = ?");
            preparedStatement.setLong(1, sender_account_number);
            preparedStatement.setString(2, pin);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                double current_balance = resultSet.getDouble("balance");

                if(current_balance >= amount) {
                    String debit_query = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
                    String credit_query = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
                    PreparedStatement debitPreparedStatement = connection.prepareStatement(debit_query);
                    debitPreparedStatement.setDouble(1, amount);
                    debitPreparedStatement.setLong(2, sender_account_number);
                    PreparedStatement creditPreparedStatement = connection.prepareStatement(credit_query);
                    creditPreparedStatement.setDouble(1, amount);
                    creditPreparedStatement.setLong(2, receiver_account_number);

                    int rowsAffected1 = debitPreparedStatement.executeUpdate();
                    int rowsAffected2 = creditPreparedStatement.executeUpdate();
                    if(rowsAffected1 > 0 && rowsAffected2 > 0) {
                        System.out.println(" Transaction Successfull");
                        System.out.println(" Rs."+ amount + " Transferred from " + sender_account_number +" to "+receiver_account_number);
                        connection.commit();
                        connection.setAutoCommit(true);
                    }
                    else {
                        System.out.println(" Transaction Failed");
                        connection.rollback();
                        connection.setAutoCommit(true);
                    }
                }
                else
                    System.out.println(" Insufficient Balance!! ");
            }
            else
                System.out.println(" Invalid PIN !!");
        }
        else
            System.out.println("Invalid Account Numbers!!");
        connection.setAutoCommit(true);
    }

    public void get_balance(long account_number) throws SQLException {
        scanner.nextLine();
        System.out.print("\n Security PIN: ");
        String pin = scanner.nextLine();
        String query = "SELECT balance FROM accounts WHERE account_number = ? AND security_pin = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setDouble(1, account_number);
        preparedStatement.setString(2, pin);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()){
            double balance = resultSet.getDouble("balance");
            System.out.println(" Balance: " + balance);
        }
        else
            System.out.println(" Invalid PIN");
    }
}
