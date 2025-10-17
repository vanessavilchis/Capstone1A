package com.pluralsight;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    // Instance variables
    // what every trans will have
    // only code inside this class can directly access these
    private final LocalDate date;
    private final LocalTime time;
    private String description;
    private String vendor;
    private double amount;


    // my constructors
    // my 5 parameters that are needed to create a transaction
    // list of variables in method
    // the instance variable belongs to (date, time, description) "this" trans object
    public Transaction(LocalDate date, LocalTime time, String description, String vendor, double amount) {
        this.date = date;
        this.time = time;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;

        // Getters and Setters
        //Getter is a method that returns the value of a private variable
        // Getter lets someone see
        // return will give back the value stored in each variable
        //Setter is a method that sets/changes the valur of a private variable
        //Setter lets someone change it
        //encapsulation - bundling data w the methods that control access to it
    }
    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    // Format for CSV file - matches the format
    public String toCSV() {
        return String.format("%s|%s|%s|%s|%.2f",
                date.format(DateTimeFormatter.ofPattern("MM-dd-yyyy")),
                time.format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                description,
                vendor,
                amount);
    }
    // replacing the default toString method
    // declare method converting object to text
    // when print a trans it shows the CSV format
    @Override
    public String toString() {
        return toCSV(); // print
    }
}