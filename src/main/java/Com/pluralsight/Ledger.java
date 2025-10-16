package Com.pluralsight;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;


public class Ledger {
    private static Scanner scanner = new Scanner(System.in);
    private static final String FILE_PATH = "src/main/resources/transactions.csv";
    public static void main (String[] args) {
        System.out.println("================================");
        System.out.println("       Welcome to Ledger App     ");
        System.out.println("=================================");
        run();
    }

    public static void run() {
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

        switch (option) {
            case "D":
                System.out.println("===========================");
                System.out.println("\nPlease add your deposit: ");
                addDeposit();
                break;
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
                System.exit(0);
                break;
            default:
                System.out.println("Incorrect option entered, try again");
        }
        System.out.println("\nPress ENTER to continue...");
        scanner.nextLine();
    }
    public static void addDeposit() {
        LocalDate date;
        LocalTime time;

        System.out.print("Use current date and time? (Y/N): ");
        String choice = scanner.nextLine().trim().toLowerCase();

        if (choice.equals("n")) {
            while (true) {
                System.out.print("Enter date (MM-dd-yyyy) or press ENTER to skip: ");
                String dateInput = scanner.nextLine().trim();

            }

