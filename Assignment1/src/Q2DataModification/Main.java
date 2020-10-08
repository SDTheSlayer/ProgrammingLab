package Q2DataModification;

import java.util.Scanner;

/**
 * Main Class for taking initial input and running the program
 */
public class Main {
    private final static Scanner userScanner = new Scanner(System.in);

    public static void main(String[] args) {
        ModificationSystem modificationSystem = new ModificationSystem();



        boolean exit = false;
        while (!exit) {
            int choice;
            System.out.println("Choose an operation to perform: \n" +
                    "       1] Schedule an Update\n" +
                    "       2] Execute all pending updates\n" +
                    "       3] Exit");
            choice = userScanner.nextInt();
            switch (choice) {
                case 1:
                    modificationSystem.scheduleUpdate();
                    break;
                case 2:
                    // Take the initial data from file
                    modificationSystem.readDataFromFile();
                    modificationSystem.executePending();
                    break;
                case 3:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid Option!");
                    break;
            }
        }
    }
}
