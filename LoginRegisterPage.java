package loginregister.page;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class LoginRegisterPage {

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";

    // Validate if the password meets criteria
    private static boolean isValidPassword(String password) {
        String passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
        return Pattern.matches(passwordPattern, password);
    }

    // Check if username already exists
    private static boolean isUsernameTaken(String username) {
        try (Scanner fileScanner = new Scanner(new File("users.csv"))) {
            while (fileScanner.hasNextLine()) {
                String[] credentials = fileScanner.nextLine().split(",");
                if (credentials.length >= 7 && credentials[6].equals(username)) {
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("User database not found.");
        }
        return false;
    }

    // Registration method
    public static void registerUser() {
        Scanner scanner = new Scanner(System.in);
        String firstName, lastName, icOrPassport, username, password, jobTitle, department, contactInfo;

        System.out.println("=== Registration Page ===");

        System.out.print("Enter your First Name: ");
        firstName = scanner.nextLine();

        System.out.print("Enter your Last Name: ");
        lastName = scanner.nextLine();

        System.out.print("Enter your IC/Passport Number: ");
        icOrPassport = scanner.nextLine();

        System.out.print("Enter your Job Title: ");
        jobTitle = scanner.nextLine();

        System.out.print("Enter your Department: ");
        department = scanner.nextLine();

        System.out.print("Enter your Contact Info: ");
        contactInfo = scanner.nextLine();

        // Check for unique username
        while (true) {
            System.out.print("Enter a Username: ");
            username = scanner.nextLine();
            if (isUsernameTaken(username)) {
                System.out.println("Username already taken. Please try a different one.");
            } else {
                break;
            }
        }

        while (true) {
            System.out.print("Enter a Password (minimum 8 characters, must contain letters and numbers): ");
            password = scanner.nextLine();
            if (isValidPassword(password)) {
                break;
            } else {
                System.out.println("Invalid password. Please try again.");
            }
        }

        // Store user details in a file
        try (FileWriter writer = new FileWriter("users.txt", true)) {
            writer.write(firstName + "," + lastName + "," + icOrPassport + "," + jobTitle + "," + department + "," + contactInfo + "," + username + "," + password + "\n");
            System.out.println("Registration successful!");
        } catch (IOException e) {
            System.out.println("An error occurred while saving the data.");
            e.printStackTrace();
        }
    }

    // Login method that reads from the file
    public static void login() {
        Scanner scanner = new Scanner(System.in);
        String inputUsername, inputPassword;

        System.out.println("=== Login Page ===");

        System.out.print("Enter Username: ");
        inputUsername = scanner.nextLine();

        System.out.print("Enter Password: ");
        inputPassword = scanner.nextLine();

        if (inputUsername.equals(ADMIN_USERNAME) && inputPassword.equals(ADMIN_PASSWORD)) {
            Admin admin = new Admin();
            admin.adminMenu();
        } else {
            userLogin(inputUsername, inputPassword);
        }
    }

    // User login method
    private static void userLogin(String inputUsername, String inputPassword) {
        boolean loginSuccessful = false;

        try (Scanner fileScanner = new Scanner(new File("users.txt"))) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] credentials = line.split(",");

                if (credentials.length == 8) {
                    String storedUsername = credentials[6];
                    String storedPassword = credentials[7];

                    if (storedUsername.equals(inputUsername) && storedPassword.equals(inputPassword)) {
                        loginSuccessful = true;
                        System.out.println("Login successful! Welcome, " + credentials[0] + " " + credentials[1] + "!");
                        System.out.println("Job Title: " + credentials[3]);
                        System.out.println("Department: " + credentials[4]);
                        System.out.println("Contact Info: " + credentials[5]);
                        break;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("User database not found.");
        }

        if (!loginSuccessful) {
            System.out.println("Invalid username or password. Please try again.");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n=== Welcome to the CLI Registration System ===");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    registerUser();
                    break;
                case 2:
                    login();
                    break;
                case 3:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 3);
    }

    // Admin class with management functionality
    static class Admin {

        private void adminMenu() {
            Scanner scanner = new Scanner(System.in);
            int option;
            do {
                System.out.println("\n=== Admin Menu ===");
                System.out.println("1. View Users");
                System.out.println("2. Edit User");
                System.out.println("3. Add User");
                System.out.println("4. Delete User");
                System.out.println("5. Exit");
                System.out.print("Choose an option: ");
                option = scanner.nextInt();
                scanner.nextLine();

                switch (option) {
    case 1:
        viewUsers();
        break;
    case 2:
        editUser();
        break;
    case 3:
        registerUser();
        break;
    case 4:
        deleteUser();
        break;
    case 5:
        System.out.println("Exiting admin menu...");
        break;
    default:
        System.out.println("Invalid option, please try again.");
        break;
}
            } while (option != 5);
        }

        private void viewUsers() {
            try (Scanner fileScanner = new Scanner(new File("users.txt"))) {
                while (fileScanner.hasNextLine()) {
                    System.out.println(fileScanner.nextLine());
                }
            } catch (FileNotFoundException e) {
                System.out.println("User database not found.");
            }
        }

        private void editUser() {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter username of the user to edit: ");
            String username = scanner.nextLine();
            List<String> users = new ArrayList<>();
            boolean found = false;

            try (Scanner fileScanner = new Scanner(new File("users.txt"))) {
                while (fileScanner.hasNextLine()) {
                    String line = fileScanner.nextLine();
                    String[] details = line.split(",");
                    if (details[6].equals(username)) {
                        System.out.print("Enter new first name: ");
                        details[0] = scanner.nextLine();
                        System.out.print("Enter new last name: ");
                        details[1] = scanner.nextLine();
                        System.out.print("Enter new IC/Passport: ");
                        details[2] = scanner.nextLine();
                        System.out.print("Enter new job title: ");
                        details[3] = scanner.nextLine();
                        System.out.print("Enter new department: ");
                        details[4] = scanner.nextLine();
                        System.out.print("Enter new contact info: ");
                        details[5] = scanner.nextLine();
                        System.out.print("Enter new password: ");
                        details[7] = scanner.nextLine();
                        found = true;
                    }
                    users.add(String.join(",", details));
                }
            } catch (FileNotFoundException e) {
                System.out.println("User database not found.");
            }

            if (found) {
                updateDatabase(users);
                System.out.println("User updated successfully.");
            } else {
                System.out.println("User not found.");
            }
        }

        private void deleteUser() {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter username of the user to delete: ");
            String username = scanner.nextLine();
            List<String> users = new ArrayList<>();
            boolean found = false;

            try (Scanner fileScanner = new Scanner(new File("users.csv"))) {
                while (fileScanner.hasNextLine()) {
                    String line = fileScanner.nextLine();
                    String[] details = line.split(",");
                    if (!details[6].equals(username)) {
                        users.add(line);
                    } else {
                        found = true;
                    }
                }
            } catch (FileNotFoundException e) {
                System.out.println("User database not found.");
            }

            if (found) {
                updateDatabase(users);
                System.out.println("User deleted successfully.");
            } else {
                System.out.println("User not found.");
            }
        }

        private void updateDatabase(List<String> users) {
            try (FileWriter writer = new FileWriter("users.txt")) {
                for (String user : users) {
                    writer.write(user + "\n");
                }
            } catch (IOException e) {
                System.out.println("Error updating user database.");
                e.printStackTrace();
            }
        }
    }
}
