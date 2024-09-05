package BankManagement;

import java.io.IOException;
import java.sql.*;
import java.util.Scanner;

public class BankingApp {

    //instance variables
    private static final String url = "jdbc:mysql://localhost:3306/bank_management";
    private static final String username = "root";
    private static final String password = "saurav";

    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {

        // Load JDBC drivers
        Class.forName("com.mysql.cj.jdbc.Driver");
        // Establish Connection with SQL DB
        Connection connection = DriverManager.getConnection(url, username, password);

        Scanner scanner = new Scanner(System.in);
        // Parameterized Constructor for other Class and pass connection and scanner
        User user = new User(connection, scanner);
        Accounts accounts = new Accounts(connection, scanner);
        AccountManager accountManager = new AccountManager(connection, scanner);

        //Driver code
        String email;
        long account_number;

        while (true) {

            System.out.println("\n *** WELCOME TO BANKING SYSTEM ***\n"
                            +   " 1. Register\n"
                            +   " 2. Login\n"
                            +   " 3. Exit\n");
            System.out.print(" Enter your choice: ");
            int choice1 = scanner.nextInt();

            switch (choice1) {
                case 1:
                    user.register();
                    break;
                case 2:
                    email = user.login();
                    if(email!=null) {
                        System.out.println(" User logged in ! ");
                        System.out.println(" Press ENTER to continue...");
                        System.in.read();

                        if(!accounts.account_exist(email)) {
                            System.out.println(" 1. Open new Bank account \n"
                                            +  " 2. Exit\n");
                            int ch = scanner.nextInt();
                            if(ch == 1) {
                                account_number = accounts.open_account(email);
                                System.out.println(" Account created successfully");
                                System.out.println(" Account Number - " + account_number);
                            }
                            else break;
                        }
                        account_number = accounts.getAccount_number(email);
                        int choice2 = 0;
                        while(choice2 != 5) {
                            System.out.println("\n 1. Debit Money \n"
                                            +  " 2. Credit Money \n"
                                            +  " 3. Transfer Money \n"
                                            +  " 4. Check Balance \n"
                                            +  " 5. Log Out \n");
                            System.out.print(" Enter your choice: ");
                            choice2 = scanner.nextInt();
                            switch (choice2) {
                                case 1:
                                    accountManager.debit_money(account_number);
                                    break;
                                case 2:
                                    accountManager.credit_money(account_number);
                                    break;
                                case 3:
                                    accountManager.transfer_money(account_number);
                                    break;
                                case 4:
                                    accountManager.get_balance(account_number);
                                    break;
                                case 5:
                                    break;
                                default:
                                    System.out.println(" Enter valid choice!");
                                    break;
                            }
                        }
                    }
                    else break;
                case 3:
                    System.out.println("\n    THANK YOU !!! ");
                    System.out.println(" *** Exiting System ***");
                    return;
                default:
                    System.out.println(" Enter Valid Choice");
                    break;
            }
        }
    }
}
