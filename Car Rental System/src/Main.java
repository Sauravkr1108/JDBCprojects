import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Car {
    private String carId;
    private String brand;
    private String model;
    private double basePrice;
    private boolean isAvailable;

    public Car(String carId, String brand, String model, double basePrice) {
        this.carId = carId;
        this.brand = brand;
        this.model = model;
        this.basePrice = basePrice;
        this.isAvailable = true;
    }

    public String getCarId() {
        return carId;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public double calculatePrice(int rentalDays) {
        return basePrice * rentalDays;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void rent(){
        isAvailable = false;
    }
    public void returnCar(){
        isAvailable = true;
    }
}

class Customer {
    private String customerId;
    private String name;

    public Customer(String customerId, String name) {
        this.customerId = customerId;
        this.name = name;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }
}

class Rental {
    private Car car;
    private Customer customer;
    private int days;

    public Rental(Car car, Customer customer, int days) {
        this.car = car;
        this.customer = customer;
        this.days = days;
    }

    public Car getCar() {
        return car;
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getDays() {
        return days;
    }
}

class CarRentalSystem {
    private List<Car> cars;
    private List<Customer> customers;
    private List<Rental> rentals;

    public CarRentalSystem() {
        cars = new ArrayList<>();
        customers = new ArrayList<>();
        rentals = new ArrayList<>();
    }

    public void addCar(Car car) {
        cars.add(car);
    }

    public void addCustomer(Customer customer){
        customers.add(customer);
    }

    public void rentCar(Car car, Customer customer, int days) {
        if(car.isAvailable()){
            car.rent();
            rentals.add(new Rental(car, customer, days));
        }
        else
            System.out.println("Car is not available for rent");
    }

    public void returnCar(Car car) {
        Rental rentalRemove = null;
        for(Rental rental : rentals) {
            if(rental.getCar() == car){
                rentalRemove = rental;
                car.returnCar();
                rentals.remove(rentalRemove);
                break;
            }
        }
        if(rentalRemove==null)
            System.out.println("Car was not rented");
    }

    public void menu() {
        Scanner sc = new Scanner(System.in);
        while(true) {
            System.out.println("=== Car Rental System ===");
            System.out.println("Enter choice: \n" +
                    " 1. Rent a Car\n" +
                    " 2. Return a Car\n" +
                    " 3. Exit\n");
            int choice = sc.nextInt();
            sc.nextLine();
            switch (choice) {
                case 1:
                    System.out.println("=== Rent a car ===");
                    System.out.print(" Enter your name: ");
                    String name = sc.nextLine();
                    System.out.println(" Available cars: ");
                    for (Car car : cars) {
                        if (car.isAvailable())
                            System.out.println(car.getCarId() + " - " + car.getModel() + " - " + car.getBrand());
                    }
                    System.out.println(" Enter the Car Id to rent: ");
                    String carId = sc.nextLine();
                    System.out.println(" Enter number of days to rent: ");
                    int days = sc.nextInt();
                    sc.nextLine();
                    Customer newCustomer = new Customer("CUS" + (customers.size() + 1), name);
                    addCustomer(newCustomer);

                    Car selectedCar = null;
                    for (Car car : cars) {
                        if (car.getCarId().equalsIgnoreCase(carId) && car.isAvailable()) {
                            selectedCar = car;
                            break;
                        }
                    }
                    if (selectedCar != null) {
                        double totalPrice = selectedCar.calculatePrice(days);
                        System.out.println("=== Rental information ===");
                        System.out.println("Customer ID: " + newCustomer.getCustomerId());
                        System.out.println("Customer name: " + newCustomer.getName());
                        System.out.println("Car: " + selectedCar.getBrand() + " " + selectedCar.getModel());
                        System.out.println("Rental Days: " + days);
                        System.out.println("Total Price: " + totalPrice);

                        System.out.println("Confirm Rental Y/N: ");
                        String confirm = sc.nextLine();

                        if (confirm.equalsIgnoreCase("Y")) {
                            rentCar(selectedCar, newCustomer, days);
                            System.out.println(" Car rented successfully");
                        } else
                            System.out.println(" Rental Cancelled");
                    } else
                        System.out.println(" Invalid car selection or car not available to rent");
                    break;

                case 2:
                    System.out.println("===Return a car===");
                    System.out.println("Enter the Car ID to return: ");
                    String carId_return = sc.nextLine();

                    Car carToReturn = null;
                    for (Car car : cars) {
                        if (car.getCarId().equalsIgnoreCase(carId_return) && !car.isAvailable()) {
                            carToReturn = car;
                            break;
                        }
                    }
                    if (carToReturn != null) {
                        Customer customer = null;
                        for (Rental rental : rentals) {
                            if (rental.getCar() == carToReturn) {
                                customer = rental.getCustomer();
                                break;
                            }
                        }
                        if (customer != null) {
                            returnCar(carToReturn);
                            System.out.println("Car returned successfully");
                        } else
                            System.out.println("Car was not rented or rental information missing");
                    } else
                        System.out.println("Invalid Car Id or car was not rented");
                    break;
                case 3:
                    System.out.println(" *** Thank You ***");
                    return;
                default:
                    System.out.println("Enter valid choice");
            }
        }
    }
}

public class Main {
    public static void main(String[] args) {
        CarRentalSystem rentalSystem = new CarRentalSystem();

        Car car1 = new Car("C001", "Toyota", "Camry", 60.0); // Different base price per day for each car
        Car car2 = new Car("C002", "Honda", "Accord", 70.0);
        Car car3 = new Car("C003", "Mahindra", "Thar", 150.0);
        rentalSystem.addCar(car1);
        rentalSystem.addCar(car2);
        rentalSystem.addCar(car3);

        rentalSystem.menu();
    }
}