package Q1SockMatching;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Main Class for Reading the input from the file and starting all the differnet Machines
 */
public class Main {

    public static final String INPUT_TXT = "./resources/Q1SockMatching/input.txt";

    public static void main(String[] args) {

        // Variable Declaration for the various Machines
        SockPile sockPile = new SockPile();
        ShelfManager shelfManager = new ShelfManager();
        MatchingMachine matchingMachine = new MatchingMachine(shelfManager);
        List<RobotArm> robotArmList = new ArrayList<RobotArm>();
        int numRobotArms = 0;

        // Read input from File
        File file = new File(INPUT_TXT);
        try {
            Scanner scanner = new Scanner(file);
            if (scanner.hasNextInt()) {
                numRobotArms = scanner.nextInt();
            }
            // Automatically assigns an incremental id to the socks recieved to display the pairings done by the matching machine
            Integer id = 1;
            while (scanner.hasNextInt()) {
                Sock sock = new Sock(id, scanner.nextInt());
                if (sock.getColor() < 1 || sock.getColor() > 4) {
                    System.out.println("Invalid Sock Color Detected " + sock.getColor() + " Skipping..... ");

                } else {
                    sockPile.addSock(sock);
                    id++;
                }

            }
        } catch (FileNotFoundException e) {
            System.out.println("Input File not found");
        }
        if (numRobotArms <= 0) {
            System.out.println("No Robots Created!!");
            return;
        }

        //Create the specified number of Robots
        for (int i = 0; i < numRobotArms; i++) {
            RobotArm robotArm = new RobotArm(sockPile, matchingMachine, String.valueOf(i));
            robotArmList.add(robotArm);
        }

        // Start the Matching machine and all robot arms
        matchingMachine.start();
        for (RobotArm robotArm : robotArmList) {
            robotArm.start();
        }

        // Gracefully Shutdown all Threads
        try {
            for (RobotArm robotArm : robotArmList) {
                robotArm.join();
            }
            matchingMachine.shutdown();
            matchingMachine.join();
        } catch (InterruptedException e) {
            System.out.println("Robot Arms interrupted !!! " + e.toString());
        }
        shelfManager.displayShelf();
    }
}
