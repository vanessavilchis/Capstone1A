package Com.pluralsight;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;


public class Ledger {
    private static Scanner scanner = new Scanner(System.in);

    //Stores the location of my CSV file
    private static final String FILE_PATH = "src/main/resources/transactions.csv";

    public static void main(String[] args) {
        System.out.println("================================");
        System.out.println("       Welcome to Ledger App     ");
        System.out.println("=================================");

        //method to start the application loop
        run();
    }

    //main application loop
    public static void run() {
        //Infinite loop  keeps running forever until user prompt stopped (with System.exit(0))
        while (true) {
            menu();
            menuSelector();
        }
    }

    public static void menu() {

        System.out.println("What would you like to do?     ");
        System.out.println("  [D] Add Deposit");
        System.out.println("  [P] Make Payment (Debit)");
        System.out.println("  [L] Display Ledger");
        System.out.println("  [X] Exit the Application");
        System.out.println("=================================");
    }

    public static void menuSelector() {
        System.out.print("Enter your selection: ");
        String option = scanner.nextLine().trim().toUpperCase();

//Starts a switch statement - checks what letter the user entered.
        switch (option) {
            case "D":
                System.out.println("===========================");
                System.out.println("\nPlease add your deposit: ");
                addDeposit();
                break; //  exits the switch
            case "P":
                System.out.println("====================");
                System.out.println("\nMake a payment ");
                makePayment();
                break;
            case "L":
                System.out.println("====================");
                System.out.println("\n Display ledger ");
                ledgerScreen();
                break;
            case "X":
                System.out.println("==================================");
                System.out.println("   Thank you for using our app!   ");
                System.out.println("           Goodbye!                ");
                System.out.println("==================================");
                System.exit(0); //exit program status 0 means successfully closed
                break;
            default:
                System.out.println("Invalid option, try again");
        }
        System.out.println("\nPress ENTER to continue...");
        scanner.nextLine();
    }

    public static void addDeposit() {
        //Declares two variables to store the date and time.
        LocalDate date;
        LocalTime time;

        System.out.print("Use current date and time? (Y/N): ");
        String choice = scanner.nextLine().trim().toLowerCase();

        if (choice.equals("n")) {
            //Infinite loop for date entry - keeps asking until valid date entered.
            while (true) {
                System.out.print("Enter date (MM-dd-yyyy) or press ENTER to skip: ");
                String dateInput = scanner.nextLine().trim();
                if (dateInput.isEmpty()) {
                    date = LocalDate.now();
                    break;
                }
                try {
                    //parse converts user's text into a LocalDate
                    // dateTimeFormatter defines expected format
                    date = LocalDate.parse(dateInput, DateTimeFormatter.ofPattern("MM-dd-yyyy"));
                    break;

                    // If invalid format, catch the error.
                } catch (Exception e) {
                    System.out.println("Invalid date format! Please use MM-dd-yyyy (e.g., 10-14-2025).");
                }
            }
            //Loops until valid time entered or Enter to use current time
            while (true) {
                System.out.print("Enter time (HH:mm:ss) or press ENTER to skip: ");
                String timeInput = scanner.nextLine().trim();

                if (timeInput.isEmpty()) {
                    time = LocalTime.now();
                    break;
                }
                try {
                    time = LocalTime.parse(timeInput, DateTimeFormatter.ofPattern("HH:mm:ss"));
                    break;
                } catch (Exception e) {
                    System.out.println("Invalid time format! Please use HH:mm:ss (e.g., 14:30:00).");
                }
            }
        } else { // If user typed "y" yes, use current date/time
            date = LocalDate.now();
            time = LocalTime.now();
        }
        System.out.print("Enter description: ");
        String description = scanner.nextLine();

        System.out.print("Enter vendor: ");
        String vendor = scanner.nextLine();

        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        // Always make deposits positive(removes negative sign) because we are ADDING deposit
        //returns the absolute value
        amount = Math.abs(amount);

        //Creates a new Transaction object with all the information
        Transaction deposit = new Transaction(date, time, description, vendor, amount);

        //This method writes the transaction to the CSV file
        saveTransaction(deposit);
        System.out.println("Deposit saved successfully!");
    }
//method to record a payment - the money going out
    public static void makePayment() {
        //Declares variables to store the payment's date and time.
        LocalDate date;
        LocalTime time;

        System.out.print("Use current date and time? (Y/N): ");
        //Reads the user's answer, trims, and converts to lowercase
        String choice = scanner.nextLine().trim().toLowerCase();

        //If user typed "n" (no), they want to enter a specific date/time.
        if (choice.equals("n")) {
            while (true) {
                System.out.print("Enter date (MM-dd-yyyy) or press ENTER to skip: ");
                String dateInput = scanner.nextLine().trim();

                if (dateInput.isEmpty()) {
                    date = LocalDate.now();
                    break;
                }
                try {
                    //expects format like "MM-dd-yyyy"
                    //If input is "October 16" or / instead of -, it will fail
                    date = LocalDate.parse(dateInput, DateTimeFormatter.ofPattern("MM-dd-yyyy"));
                    break;
                } catch (Exception e) {
                    System.out.println("Invalid date format! Please use MM-dd-yyyy (e.g., 10-14-2025).");
                }
            }
            while (true) {
                System.out.print("Enter time (HH:mm:ss) or press ENTER to skip: ");
                String timeInput = scanner.nextLine().trim();

                if (timeInput.isEmpty()) {
                    time = LocalTime.now(); //gets the current time from  computer
                    break;
                }
                try {
                    time = LocalTime.parse(timeInput, DateTimeFormatter.ofPattern("HH:mm:ss"));
                    break;
                } catch (Exception e) {
                    System.out.println("Invalid time format! Please use HH:mm:ss (e.g., 14:30:00).");
                }
            }
        } else {
            //If user typed "y"
            date = LocalDate.now();
            time = LocalTime.now();
        }
            System.out.print("Enter description: ");
            String description = scanner.nextLine();

            System.out.print("Enter vendor: ");
            String vendor = scanner.nextLine();

            System.out.print("Enter amount: ");
            double amount = scanner.nextDouble();
            scanner.nextLine();

            // Always make payments negative bc we are making a payment so -
            amount = -Math.abs(amount);


            //Creates a new Transaction object by collecting data and storing it in variable "payment"
            Transaction payment = new Transaction(date, time, description, vendor, amount);

            //This method writes the transaction to the CSV file
            saveTransaction(payment);
            System.out.println("Payment saved successfully!");
        }
        //method that displays the ledger menu
    public static void ledgerScreen() {
       //keep showing menu until close
        boolean keepRunning = true;

        while (keepRunning) {
            System.out.println("==================================");
            System.out.println("             Ledger               ");
            System.out.println("             Screen               ");
            System.out.println("==================================");
            System.out.println(" A - All - Display all entries");
            System.out.println(" D - Deposits - Display only deposits");
            System.out.println(" P - Payments - Show only payments");
            System.out.println(" R - Reports - Run reports");
            System.out.println(" H - Home - Go back to home");
            System.out.print("\nEnter your choice: ");
            System.out.println("==================================");

            String choice = scanner.nextLine().trim().toUpperCase();

//checks what letter user entered and executes matching case.
            switch (choice) {
                case "A":
                    System.out.println("\n -----ALL TRANSACTIONS---Newest First)-----");
                    displayAll();
                    break;
                case "D":
                    System.out.println("\n-----DEPOSITS ONLY---Newest First)------");
                    displayDeposits();
                    break;
                case "P":
                    System.out.println("\n-----PAYMENTS ONLY---Newest First----");
                    displayPayments();
                    break;
                case "R":
                    reportsScreen();
                    break;
                case "H":
                    keepRunning = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter A, D, P, R, or H.");
            }
            // pause after displaying transactions
            // in ledger menu AND user chose to see all transactions OR chose deposits or Payments
            if (keepRunning && (choice.equals("A") || choice.equals("D") || choice.equals("P"))) {
                System.out.println("\nPress ENTER to continue...");
                scanner.nextLine();
            }
        }
    }
    //method to display all transactions both deposits and payments
    public static void displayAll() {
        //Loads all transactions from CSV file and returns an ArrayList
        //Stores all transactions in transactions variable
        ArrayList<Transaction> transactions = loadTransactions();

        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
            //exits the method
            return;
        }

        //Prints formatted column headers
        System.out.printf("%-12s | %-10s | %-25s | %-20s | %12s\n",
                "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("-".repeat(90));

        // Show newest first (reverse order)
        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction t = transactions.get(i);
            System.out.printf("%-12s | %-10s | %-25s | %-20s | $%,11.2f\n",
                    t.getDate().format(DateTimeFormatter.ofPattern("MM-dd-yyyy")),
                    t.getTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                    t.getDescription(),
                    t.getVendor(),
                    t.getAmount());
        }
    }


        }

    }
}

