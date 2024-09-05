package Hospital_Management;

import java.sql.*;
import java.util.Scanner;

public class Hospital_Management {
    private static final String url = "jdbc:mysql://localhost:3306/hospital_management";
    private static final String username = "root";
    private static final String password = "saurav";

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection(url, username, password);
        Scanner scanner = new Scanner(System.in);

        Patient patient = new Patient(connection, scanner);
        Doctor doctor = new Doctor(connection);

        while(true){
            System.out.println(" === HOSPITAL MANAGEMENT SYSTEM ===");
            System.out.println("1. Add Patient");
            System.out.println("2. View Patients");
            System.out.println("3. View Doctors");
            System.out.println("4. Book Appointment");
            System.out.println("5. Exit");
            System.out.println("Enter your choice: ");
            int choice = scanner.nextInt();
            switch(choice) {
                case 1:
                    patient.addPatient();
                    break;
                case 2:
                    patient.viewPatient();
                    break;
                case 3:
                    doctor.viewDoctor();
                    break;
                case 4:
                    bookAppointment(connection, scanner, patient, doctor);
                    break;
                case 5:
                    System.out.println(" Thank You...");
                    return;
                default:
                    System.out.println(" Enter valid choice");
            }
        }
    }

    public static void bookAppointment(Connection connection, Scanner scanner, Patient patient, Doctor doctor) throws SQLException {
        System.out.print(" Enter Patient Id: ");
        int patientId = scanner.nextInt();
        System.out.print(" Enter Doctor Id: ");
        int doctorId = scanner.nextInt();
        System.out.print(" Enter appointment date YYYY-MM-DD: ");
        String appointmentDate = scanner.next();

        if(patient.checkPatient(patientId) && doctor.checkDoctor(doctorId)) {
            if(checkAvailability(doctorId, appointmentDate, connection)) {
                String query = "INSERT INTO appointments (patient_id, doctor_id, appointment_date) VALUES(?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, patientId);
                preparedStatement.setInt(2, doctorId);
                preparedStatement.setString(3, appointmentDate);
                int rowsAffected = preparedStatement.executeUpdate();
                if(rowsAffected>0)
                    System.out.println("Appointment Booked!");
                else
                    System.out.println("Failed to Book Appointment!");
            } else
                System.out.println(" Doctor not available on date "+appointmentDate);
        } else
            System.out.println(" Doctor/Patient doesnot exist!!");
    }

    private static boolean checkAvailability(int doctorId, String appointmentDate,Connection connection) throws SQLException {
        String query = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND appointment_date = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, doctorId);
        preparedStatement.setString(2, appointmentDate);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()){
            int count = resultSet.getInt(1);
            return count == 0;
        } else return false;
    }
}
