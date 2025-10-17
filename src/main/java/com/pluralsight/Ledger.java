package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;


public class Ledger {
    private static final Scanner scanner = new Scanner(System.in);

    //Stores the location of my CSV file
    private static final String FILE_PATH = "src/main/resources/transactions.csv";

    public static void main(String[] args) {
        createFileIfNotExists();
        System.out.println("=================================");
        System.out.println("       Welcome to Ledger App     ");
        System.out.println("=================================");

        //method to start the application loop
        run();
    }
    // NEW METHOD: Create file if it doesn't exist
    private static void createFileIfNotExists() {
        File file = new File(FILE_PATH);
        File parentDir = file.getParentFile();

        try {
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
                System.out.println("Created new transactions file.");
            }
        } catch (IOException e) {
            System.out.println("Error creating file: " + e.getMessage());
        }
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
                System.out.println("=================================");
                System.out.println("\nPlease add your deposit information below: ");
                addDeposit();
                break; //  exits the switch
            case "P":
                System.out.println("=================================");
                System.out.println("\nEnter payment information ");
                makePayment();
                break;
            case "L":
                System.out.println("=================================");
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
        System.out.println("Payment saved!");
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
            System.out.println(" P - Payments - Display only payments");
            System.out.println(" R - Reports - Run reports");
            System.out.println(" H - Home - Go back to home");
            System.out.println("==================================");
            System.out.print("Enter your choice:  ");
            String choice = scanner.nextLine().trim().toUpperCase();

//checks what letter user entered and executes matching case.
            switch (choice) {
                case "A":
                    System.out.println("\n -----ALL TRANSACTIONS---Newest First-----");
                    displayAll();
                    break;
                case "D":
                    System.out.println("\n-----DEPOSITS ONLY---Newest First------");
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

    public static void reportsScreen() {
        boolean keepRunning = true;

        while (keepRunning) {
            System.out.println("\n==================================");
            System.out.println("             Reports              ");
            System.out.println("==================================");
            System.out.println(" 1) Month To Date");
            System.out.println(" 2) Previous Month");
            System.out.println(" 3) Year To Date");
            System.out.println(" 4) Previous Year");
            System.out.println(" 5) Search by Vendor");
            System.out.println(" 0) Back - Return to Ledger");
            System.out.println("==================================");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    System.out.println("\n-----MONTH TO DATE-----");
                    displayMonthToDate();
                    break;
                case "2":
                    System.out.println("\n-----PREVIOUS MONTH-----");
                    displayPreviousMonth();
                    break;
                case "3":
                    System.out.println("\n-----YEAR TO DATE-----");
                    displayYearToDate();
                    break;
                case "4":
                    System.out.println("\n-----PREVIOUS YEAR-----");
                    displayPreviousYear();
                    break;
                case "5":
                    System.out.println("\n-----SEARCH BY VENDOR-----");
                    searchByVendor();
                    break;
                case "0":
                    keepRunning = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }

            if (keepRunning && !choice.equals("0")) {
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
        // % placeholder (-) aligned to the left, # is space for characters wide, string and Pipe
        System.out.printf("%-12s | %-10s | %-25s | %-20s | %12s\n", // Number with commas, 11 characters, 2 decimals format
                "Date", "Time", "Description", "Vendor", "Amount");

        //separator line printing the - dash 90 times across to separate this is a way to format and separate
        // easier to read the header and data
        // 80 characters wide + 10 for extra space and account for pipe
        System.out.println("-".repeat(90));

        // Show newest first so in reverse order
        //Start at the LAST index (newest transaction)
        //Continue until we reach index 0
        //Decrease by 1 each time (moving backwards)
        // for loop (int index = the number of transactions -1); // index greater than or equal to 0
        for (int i = transactions.size() - 1; i >= 0; i--) {

            // gets one transaction from the Array list
            // transactions.get(i) = gets the transaction at index
            // stores in the variable
            Transaction t = transactions.get(i);
            System.out.printf("%-12s | %-10s | %-25s | %-20s | $%,11.2f\n",
                    //Gets the LocalDate/time object from transaction and formats it
                    //t = variable transaction
                    t.getDate().format(DateTimeFormatter.ofPattern("MM-dd-yyyy")),
                    t.getTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                    //Gets description, vendor, and amount but does not need to be formatted
                    t.getDescription(),
                    t.getVendor(),
                    t.getAmount());
        }
    }
// method does not give anything back like no return value
    //display deposit method
    // closed parenthesis means it does not take any input
    public static void displayDeposits() {
       //A list that can hold transaction objects > variable = method that reads the CSV file
        // so pretty much it opens the CSV file and reads every line, converts to each line into a transaction object
        // and gives back a list.
        ArrayList<Transaction> transactions = loadTransactions();

        System.out.printf("%-12s | %-10s | %-25s | %-20s | %12s\n",
                "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("-".repeat(90));

        // true or false does it have deposits ?
        boolean hasDeposits = false;
        // for loop (int index = # of transactions minus 1 from the index stops at -1;
        // index greater or equal to 0 (start of index);
        for (int i = transactions.size() - 1; i >= 0; i--) {
            //datatype variable = get index from the transaction list and store in variable t
            Transaction t = transactions.get(i);

            // Only show if amount is POSITIVE (money added)
            // command if amount is greater than 0 (positive) then print
            if (t.getAmount() > 0) {
                System.out.printf("%-12s | %-10s | %-25s | %-20s | $%,11.2f\n",
                        t.getDate().format(DateTimeFormatter.ofPattern("MM-dd-yyyy")),
                        t.getTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                        t.getDescription(),
                        t.getVendor(),
                        t.getAmount());
                //deposit is there
                hasDeposits = true;

            }
        }
        // if statement or conditional, no transaction found = print no deposit found
        // if it does not have deposit (no deposit found) print message
        if (!hasDeposits) {
            System.out.println("No deposits found.");
        }
    }

    public static void displayPayments() {
        ArrayList<Transaction> transactions = loadTransactions();

        System.out.printf("%-12s | %-10s | %-25s | %-20s | %12s\n",
                "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("-".repeat(90));

        boolean hasPayments = false;
        // i -- = index decrease by 1
        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction t = transactions.get(i);

            // Only show if amount is NEGATIVE (money paid)
            if (t.getAmount() < 0) {
                System.out.printf("%-12s | %-10s | %-25s | %-20s | $%,11.2f\n",
                        t.getDate().format(DateTimeFormatter.ofPattern("MM-dd-yyyy")),
                        t.getTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                        t.getDescription(),
                        t.getVendor(),
                        t.getAmount());
                hasPayments = true;
            }
        }
        if (!hasPayments) {
            System.out.println("No payments found.");
        }
    }
// Method that reads the csv file and gives back a list of all transactions
    public static ArrayList<Transaction> loadTransactions() {
       //A list that holds transaction objects variable (where we store data) = new empty list (transaction)
        ArrayList<Transaction> transactions = new ArrayList<>();
        try {
            // file reader opens the file buffered reader reads the file line by line
            BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH));

            // type of data variable
            String line;
            // while loop keeps reading every line one by one until the end of the file
            while ((line = reader.readLine()) != null) {
                // data type list parts, cut the line into pieces where pipe symbol is
                // in other words separates date time description vendor and amount
                String[] parts = line.split("\\|");
                LocalDate date = LocalDate.parse(parts[0], DateTimeFormatter.ofPattern("MM-dd-yyyy"));
                LocalTime time = LocalTime.parse(parts[1], DateTimeFormatter.ofPattern("HH:mm:ss"));
                String description = parts[2];
                String vendor = parts[3];
                // turns text amount to math amount
                double amount = Double.parseDouble(parts[4]);
                // packages all the data together into one trans object
                Transaction t = new Transaction(date, time, description, vendor, amount);
                transactions.add(t); // add array list
            }
            reader.close();
            //handle situations instead of crashing
            // in this case can not read file print message
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        // this returns the result
        // returning list w. all transactions or error or empty but returns value
        return transactions;
    }
// Declares method to takes one trans and saves it to the file
    public static void saveTransaction(Transaction record) {
        try { // try catch block
       // Buff Writer is going to write line by line
            // this opens the file to get ready to write inside the csv
            // append = true means adds to the end and does not erase anything
            // if append were false it would delete everything

            BufferedWriter writer = new BufferedWriter(
                    new FileWriter("src/main/resources/transactions.csv", true));
            writer.write(record.toCSV()); // converts transaction to CSV format and write it to the file
            writer.newLine(); // so transaction goes on its own line instead of all trans on one line
            writer.close(); // done writing close file and save changes
            System.out.println("Transaction saved!");
// damage control if something goes wrong in this case saving the trans
        } catch (IOException e) {
            System.out.println("Error saving transaction: " + e.getMessage());
        }
    }
    public static void displayMonthToDate() {
        ArrayList<Transaction> allTransactions = loadTransactions();
        LocalDate now = LocalDate.now();

        System.out.println("\n=== MONTH TO DATE ===");
        System.out.printf("%-12s | %-10s | %-25s | %-20s | %12s\n",
                "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("-".repeat(90));

        boolean found = false;
        for (Transaction t : allTransactions) {
            if (t.getDate().getMonth() == now.getMonth() &&
                    t.getDate().getYear() == now.getYear()) {
                System.out.printf("%-12s | %-10s | %-25s | %-20s | $%,11.2f\n",
                        t.getDate().format(DateTimeFormatter.ofPattern("MM-dd-yyyy")),
                        t.getTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                        t.getDescription(),
                        t.getVendor(),
                        t.getAmount());
                found = true;
            }
        }
        if (!found) {
            System.out.println("No transactions found for this month.");
        }
    }

    public static void displayPreviousMonth() {
        ArrayList<Transaction> allTransactions = loadTransactions();
        LocalDate lastMonth = LocalDate.now().minusMonths(1);

        System.out.println("\n=== PREVIOUS MONTH ===");
        System.out.printf("%-12s | %-10s | %-25s | %-20s | %12s\n",
                "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("-".repeat(90));

        boolean found = false;
        for (Transaction t : allTransactions) {
            if (t.getDate().getMonth() == lastMonth.getMonth() &&
                    t.getDate().getYear() == lastMonth.getYear()) {
                System.out.printf("%-12s | %-10s | %-25s | %-20s | $%,11.2f\n",
                        t.getDate().format(DateTimeFormatter.ofPattern("MM-dd-yyyy")),
                        t.getTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                        t.getDescription(),
                        t.getVendor(),
                        t.getAmount());
                found = true;
            }
        }
        if (!found) {
            System.out.println("No transactions found for previous month.");
        }
    }

    public static void displayYearToDate() {
        ArrayList<Transaction> allTransactions = loadTransactions();
        int currentYear = LocalDate.now().getYear();

        System.out.println("\n=== YEAR TO DATE ===");
        System.out.printf("%-12s | %-10s | %-25s | %-20s | %12s\n",
                "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("-".repeat(90));

        boolean found = false;
        for (Transaction t : allTransactions) {
            if (t.getDate().getYear() == currentYear) {
                System.out.printf("%-12s | %-10s | %-25s | %-20s | $%,11.2f\n",
                        t.getDate().format(DateTimeFormatter.ofPattern("MM-dd-yyyy")),
                        t.getTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                        t.getDescription(),
                        t.getVendor(),
                        t.getAmount());
                found = true;
            }
        }
        if (!found) {
            System.out.println("No transactions found for this year.");
        }
    }

    public static void displayPreviousYear() {
        ArrayList<Transaction> allTransactions = loadTransactions();
        int previousYear = LocalDate.now().getYear() - 1;

        System.out.println("\n=== PREVIOUS YEAR ===");
        System.out.printf("%-12s | %-10s | %-25s | %-20s | %12s\n",
                "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("-".repeat(90));

        boolean found = false;
        for (Transaction t : allTransactions) {
            if (t.getDate().getYear() == previousYear) {
                System.out.printf("%-12s | %-10s | %-25s | %-20s | $%,11.2f\n",
                        t.getDate().format(DateTimeFormatter.ofPattern("MM-dd-yyyy")),
                        t.getTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                        t.getDescription(),
                        t.getVendor(),
                        t.getAmount());
                found = true;
            }
        }
        if (!found) {
            System.out.println("No transactions found for previous year.");
        }
    }

    public static void searchByVendor() {
        ArrayList<Transaction> allTransactions = loadTransactions();
        System.out.print("Enter vendor name: ");
        String vendorSearch = scanner.nextLine();

        System.out.println("\n=== SEARCH BY VENDOR: " + vendorSearch + " ===");
        System.out.printf("%-12s | %-10s | %-25s | %-20s | %12s\n",
                "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("-".repeat(90));

        boolean found = false;
        for (Transaction t : allTransactions) {
            if (t.getVendor().toLowerCase().contains(vendorSearch.toLowerCase())) {
                System.out.printf("%-12s | %-10s | %-25s | %-20s | $%,11.2f\n",
                        t.getDate().format(DateTimeFormatter.ofPattern("MM-dd-yyyy")),
                        t.getTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                        t.getDescription(),
                        t.getVendor(),
                        t.getAmount());
                found = true;
            }
        }
        if (!found) {
            System.out.println("No transactions found for vendor: " + vendorSearch);
        }
    }
}
