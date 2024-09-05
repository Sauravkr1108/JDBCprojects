import java.sql.*;
import java.util.Scanner;

public class HotelReservation {
    private static final String url = "jdbc:mysql://localhost:3306/hotel_reservation_db";
    private static final String username = "root";
    private static final String password = "saurav";

    public static void main(String[] args) throws ClassNotFoundException, SQLException, InterruptedException {

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection(url, username, password);
        Scanner scanner = new Scanner(System.in);

        while(true) {
            System.out.println("\n *** HOTEL RESERVATION SYSTEM ***");
            System.out.println("\n 1. Reserve Room\n"
                            +   " 2. View Reservations\n"
                            +   " 3. Get Room Number\n"
                            +   " 4. Update Reservation\n"
                            +   " 5. Delete Reservation\n"
                            +   " 6. Exit");
            System.out.print("\n Choose an option: ");
            int choice = scanner.nextInt();

            switch(choice){
                case 1: reserveRoom(connection,scanner);
                        break;
                case 2: viewReservations(connection,scanner);
                        break;
                case 3: getRoomNumber(connection,scanner);
                        break;
                case 4: updateReservation(connection,scanner);
                        break;
                case 5: deleteReservation(connection,scanner);
                        break;
                case 6:
                    exit();
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid Choice!! Choose again.");
            }
        }
    }

    private static void reserveRoom (Connection connection, Scanner scanner) throws SQLException {
        System.out.print(" Enter Guest Name: ");
        String name = scanner.next();

        System.out.print(" Enter room number: ");
        int roomNumber = scanner.nextInt();
        System.out.println(" Enter mobile number: ");
        String mobileNo = scanner.next();

        String query = "INSERT INTO reservations (guest_name, room_number, contact_number) VALUES (?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1,name);
        preparedStatement.setInt(2, roomNumber);
        preparedStatement.setString(3, mobileNo);
        int rowsAffected = preparedStatement.executeUpdate();

        if(rowsAffected > 0){
            System.out.println(" Reservation Successful!!");
        } else
            System.out.println(" Reservation Failed");
    }

    private static void viewReservations (Connection connection, Scanner scanner) throws SQLException {
        System.out.println(" RESERVATIONS: - ");
        System.out.println("+----------------+----------------+----------------+----------------+-----------------+");
        System.out.println("| Reservation ID | Guest          | Room Number    | Contact Number | Reservation Date|");
        System.out.println("+----------------+----------------+----------------+----------------+-----------------+");

        String query = "SELECT * FROM reservations";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while(resultSet.next()) {
            int reservation_id = resultSet.getInt(1);
            String guestName = resultSet.getString(2);
            int roomNumber = resultSet.getInt(3);
            String mob = resultSet.getString(4);
            String date = resultSet.getTimestamp(5).toString();

            System.out.printf("| %-14d | %-15s | %-13d | %-28s | %-19s |\n", reservation_id, guestName, roomNumber, mob, date);
        }
        System.out.println("+----------------+----------------+----------------+----------------+-----------------+");
    }

    private static void getRoomNumber (Connection connection, Scanner scanner) throws SQLException {

        System.out.print(" Enter Reservation ID: ");
        int reservationID = scanner.nextInt();

        String query = "SELECT room_number, guest_name FROM reservations WHERE reservation_id = " + reservationID;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        if(resultSet.next()) {
            int roomNo = resultSet.getInt("room_number");
            String name = resultSet.getString("guest_name");
            System.out.println(" Reservation ID: "+ reservationID + "\n Guest Name: "+ name +"\n Room Number: "+ roomNo);
        }
        else
            System.out.println(" Reservation not found for given ID !!");

    }

    private static void updateReservation (Connection connection, Scanner scanner) throws SQLException {
        System.out.print(" Enter the Reservation ID to update: ");
        int reservationID = scanner.nextInt();

        if(reservationExist(connection, reservationID)){
            System.out.print(" Update Guest Name: ");
            String name = scanner.next();
            System.out.print(" Update Room Number: ");
            int roomNo = scanner.nextInt();
            System.out.println(" Update Contact Number: ");
            String mob = scanner.next();

            String query = "UPDATE reservations SET guest_name = ?, room_number = ?, contact_number = ? WHERE reservation_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, roomNo);
            preparedStatement.setString(3, mob);
            preparedStatement.setInt(4, reservationID);
            int rowsAffected = preparedStatement.executeUpdate();
            if(rowsAffected > 0)
                System.out.println(" Reservation update unsuccessful");
            else
                System.out.println(" Reservation update unsuccessful");
        } else {
            System.out.println(" Reservation ID not found !!");
            return;
        }

    }

    private static void deleteReservation (Connection connection, Scanner scanner) throws SQLException {
        System.out.print(" Enter reservation ID to remove: ");
        int reservationID = scanner.nextInt();
        if(!reservationExist(connection,reservationID)){
            System.out.println(" Reservation ID not found !!");
            return;
        }
        String query = "DELETE FROM reservations WHERE reservation_id = "+ reservationID;
        Statement statement = connection.createStatement();
        int rowsAffected = statement.executeUpdate(query);
        if(rowsAffected>0)
            System.out.println(" Reservation Removed !!");
        else
            System.out.println(" Reservation deletion failed !!");
    }

    private static boolean reservationExist(Connection connection, int reservationID) throws SQLException {
        String sql = "SELECT reservation_id FROM reservations WHERE reservation_id = " + reservationID;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        return resultSet.next();
    }

    private static void exit() throws InterruptedException {
        System.out.print(" Exiting System ");
        int i=5;
        while(i!=0){
            System.out.print(".");
            Thread.sleep(200);
            i--;
        }
        System.out.println("\n THANK YOU !! ");
    }

}