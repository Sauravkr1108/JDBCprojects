import java.util.ArrayList;
import java.util.Scanner;

abstract class Employee {
    private String name;
    private  int id;
    private String empType;

    public Employee(String name, int id, String empType) {
        this.name = name;
        this.id = id;
        this.empType = empType;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getEmpType() {
        return  empType;
    }

    public abstract double calcSalary();

    @Override
    public String toString() {
        return " Employee - Name: " + name + ", Id: " + id + ", Salary: " + calcSalary() + " [" + empType +"]";
    }
}

class FullTimeEmployee extends Employee {
    private double monthlySalary;

    public FullTimeEmployee(String name, int id, String empType ,double monthlySalary) {
        super(name, id, empType);
        this.monthlySalary = monthlySalary;
    }

    @Override
    public double calcSalary(){
        return monthlySalary;
    }
}

class PartTimeEmployee extends Employee{
    private double hourlySalary;
    private int hours;

    public PartTimeEmployee(String name, int id, String empType ,double hourlySalary, int hours){
        super(name, id, empType);
        this.hourlySalary = hourlySalary;
        this.hours = hours;
    }

    @Override
    public double calcSalary(){
        return hours * hourlySalary;
    }
}

class PayrollSystem {
    private ArrayList<Employee> employees;

    public PayrollSystem(){
        employees = new ArrayList<>();
    }

    public void addEmployee(Employee employee){
        employees.add(employee);
        System.out.println(" Employee "+ employee.getName() + " added. "+ employee.getEmpType());
    }

    public void removeEmployee(int id){
        Employee employeeRemove = null;
        for(Employee employee:employees){
            if(employee.getId() == id){
                employeeRemove = employee;
                break;
            }
        }
        if(employeeRemove != null){
            employees.remove(employeeRemove);
            System.out.println(" Employee: "+employeeRemove.getName()+" removed.");
        }
        else System.out.println("Employee not found!!");
    }

    public void displayEmployee(){
        for(Employee employee:employees){
            System.out.println(employee);
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        PayrollSystem payrollSystem = new PayrollSystem();
        FullTimeEmployee emp1 = new FullTimeEmployee("Saurav",1,"Full-Time", 10000);
        payrollSystem.addEmployee(emp1);
        PartTimeEmployee emp2 = new PartTimeEmployee("Sneha", 2, "Part-Time", 2000,6);
        payrollSystem.addEmployee(emp2);

        while (true) {
            System.out.println("\n=== Employee Payroll System ===\n");
            System.out.println(" 1. Add Full-Time Employee");
            System.out.println(" 2. Add Part-Time Employee ");
            System.out.println(" 3. Display Employee");
            System.out.println(" 4. Remove Employee");
            System.out.println(" 5. Exit\n");

            int ch = sc.nextInt();
            switch(ch) {
                case 1:
                    sc.nextLine();
                    System.out.print(" Name: ");
                    String name1 = sc.nextLine();
                    System.out.print(" Id: ");
                    int id1 = sc.nextInt();
                    sc.nextLine();
                    System.out.print(" Monthly Salary: ");
                    double salary = sc.nextDouble();
                    sc.nextLine();
                    emp1 = new FullTimeEmployee(name1, id1, "Full-Time", salary);
                    payrollSystem.addEmployee(emp1);
                    break;
                case 2:
                    sc.nextLine();
                    System.out.print(" Name: ");
                    String name2 = sc.nextLine();
                    System.out.print(" Id: ");
                    int id2 = sc.nextInt();
                    sc.nextLine();
                    System.out.print(" Hourly Salary: ");
                    double hourlySalary = sc.nextDouble();
                    sc.nextLine();
                    System.out.print(" Working Hours: ");
                    int hours = sc.nextInt();
                    sc.nextLine();
                    emp2 = new PartTimeEmployee(name2, id2, "Part-Time", hourlySalary, hours);
                    payrollSystem.addEmployee(emp2);
                    break;
                case 3:
                    payrollSystem.displayEmployee();
                    break;
                case 4:
                    System.out.print(" Enter Id of employee to remove: ");
                    int id = sc.nextInt();
                    sc.nextLine();
                    payrollSystem.removeEmployee(id);
                    break;
                case 5:
                    System.out.println(" *** Thank You *** ");
                    return;
                default:
                    System.out.println(" Enter valid choice!!");
            }
        }
    }
}