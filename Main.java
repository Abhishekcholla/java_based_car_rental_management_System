
// here we are going to build a new experimental car rental java based application using oops and java concepts
//date:14-03-2026
//name: simple java based car rental management system

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Car {
    private String carId;
    private String brand;
    private String model;
    private double basePricePerDay;
    private boolean isAvailable;

    public Car(String carId, String brand, String model, double basePricePerDay) {
        this.carId = carId;
        this.brand = brand;
        this.model = model;
        this.basePricePerDay = basePricePerDay;
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
        return basePricePerDay * rentalDays;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void rent() {
        isAvailable = false;
    }

    public void returnCar() {
        isAvailable = true;
    }

    @Override
    public String toString() {
        return carId + " - " + brand + " " + model;
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

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public void rentCar(Car car, Customer customer, int days) {
        if (car.isAvailable()) {
            car.rent();
            rentals.add(new Rental(car, customer, days));
        } else {
            System.out.println("Car is not available for rent.");
        }
    }

    public void returnCar(Car car) {
        car.returnCar();

        Rental rentalToRemove = null;

        for (Rental rental : rentals) {
            if (rental.getCar().getCarId().equals(car.getCarId())) {
                rentalToRemove = rental;
                break;
            }
        }

        if (rentalToRemove != null) {
            rentals.remove(rentalToRemove);
        } else {
            System.out.println("Car was not rented.");
        }
    }

    public void menu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== Car Rental System =====");
            System.out.println("1. Rent a Car");
            System.out.println("2. Return a Car");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input! Please enter a number.");
                scanner.nextLine();
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1:
                    System.out.println("\n== Rent a Car ==");

                    System.out.print("Enter your name: ");
                    String customerName = scanner.nextLine();

                    System.out.println("\nAvailable Cars:");

                    boolean available = false;

                    for (Car car : cars) {
                        if (car.isAvailable()) {
                            System.out.println(car);
                            available = true;
                        }
                    }

                    if (!available) {
                        System.out.println("No cars are currently available.");
                        break;
                    }

                    System.out.print("\nEnter Car ID: ");
                    String carId = scanner.nextLine();

                    System.out.print("Enter rental days: ");

                    if (!scanner.hasNextInt()) {
                        System.out.println("Invalid number of days.");
                        scanner.nextLine();
                        break;
                    }

                    int rentalDays = scanner.nextInt();
                    scanner.nextLine();

                    if (rentalDays <= 0) {
                        System.out.println("Rental days must be greater than 0.");
                        break;
                    }

                    Car selectedCar = null;

                    for (Car car : cars) {
                        if (car.getCarId().equalsIgnoreCase(carId) && car.isAvailable()) {
                            selectedCar = car;
                            break;
                        }
                    }

                    if (selectedCar == null) {
                        System.out.println("Invalid Car ID or car is unavailable.");
                        break;
                    }

                    Customer customer = new Customer(
                            "CUS" + (customers.size() + 1),
                            customerName);

                    addCustomer(customer);

                    double totalPrice = selectedCar.calculatePrice(rentalDays);

                    System.out.println("\n===== Rental Summary =====");
                    System.out.println("Customer ID : " + customer.getCustomerId());
                    System.out.println("Customer    : " + customer.getName());
                    System.out.println("Car         : " + selectedCar.getBrand() + " " + selectedCar.getModel());
                    System.out.println("Days        : " + rentalDays);
                    System.out.printf("Total Price : rs.%.2f%n", totalPrice);

                    System.out.print("\nConfirm Rental (Y/N): ");
                    String confirm = scanner.nextLine();

                    if (confirm.equalsIgnoreCase("Y")) {
                        rentCar(selectedCar, customer, rentalDays);
                        System.out.println("Car rented successfully!");
                    } else {
                        System.out.println("Rental cancelled.");
                    }

                    break;

                case 2:

                    System.out.println("\n== Return a Car ==");

                    System.out.print("Enter Car ID: ");
                    String returnId = scanner.nextLine();

                    Car carToReturn = null;

                    for (Car car : cars) {
                        if (car.getCarId().equalsIgnoreCase(returnId) && !car.isAvailable()) {
                            carToReturn = car;
                            break;
                        }
                    }

                    if (carToReturn == null) {
                        System.out.println("Invalid Car ID or car is not rented.");
                        break;
                    }

                    Customer rentedCustomer = null;

                    for (Rental rental : rentals) {
                        if (rental.getCar().getCarId().equals(carToReturn.getCarId())) {
                            rentedCustomer = rental.getCustomer();
                            break;
                        }
                    }

                    returnCar(carToReturn);

                    if (rentedCustomer != null) {
                        System.out.println("Car returned successfully by " + rentedCustomer.getName());
                    } else {
                        System.out.println("Car returned successfully.");
                    }

                    break;

                case 3:
                    System.out.println("\nThank you for using the Car Rental System!");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }
}

public class Main {

    public static void main(String[] args) {

        CarRentalSystem rentalSystem = new CarRentalSystem();

        rentalSystem.addCar(new Car("C001", "Toyota", "Innova_Crysta", 2600.0));
       // rentalSystem.addCar(new Car("C002", "Honda", "City", 970.0));
       // rentalSystem.addCar(new Car("C003", "Mahindra", "Thar", 550.0));
        rentalSystem.addCar(new Car("C004", "Hyundai", "Creta", 1900.0));
        rentalSystem.addCar(new Car("C005", "Tata", "Nexon", 980.0));
        rentalSystem.addCar(new Car("C006", "Tata", "Hexa", 1900.0));
        rentalSystem.addCar(new Car("C007", "Maruti_suzuki", "Ertiga", 1580.0));
        rentalSystem.addCar(new Car("C008", "Maruti_suzuki", "Baleno", 780.0));

        rentalSystem.menu();
    }
}
