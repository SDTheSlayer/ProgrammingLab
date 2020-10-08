package Q3Calculator;

import javax.swing.*;
import java.awt.event.*;
import java.util.Scanner;

public class Main {
    /**
     * Pad Color, NumLock and FucLock are used for shifting from numbers to functions when using single select scheme
     */
    public static int padColor;
    public static final Object numLock = new Object();
    public static final Object funcLock = new Object();
    private final static Scanner userScanner = new Scanner(System.in);

    /**
     * Main fucntion for running the Calculator Program
     */
    public static void main(String[] args) {

        // Trying to use Nimbus theme for better display of GUI form
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            System.out.println("Using default look and theme...");
        }

        // Choosing a the calculator control scheme, assigning choice and padcolor variable based on this
        System.out.println("Choose the rules for highlighting: \n" +
                "       1] Single Color. Press Enter to select\n" +
                "       2] Two colors. Press Enter to select Numbers. Press Space to select operations and Fucntions.");

        int choice = userScanner.nextInt();
        switch (choice) {
            case 1:
                padColor = 0;
                break;
            case 2:
                padColor = 2;
                break;
            default:
                choice = 2;
                padColor = 2;
                System.out.println("Invalid Option! Defaulting to 2.");
                break;
        }

        // Creating the JFrame for displaying the calculator GUI and setting its initial parameters
        JFrame frame = new JFrame("Calculator");
        CalculatorGUI calculator = new CalculatorGUI(choice);
        frame.setContentPane(calculator.rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 600);
        frame.setVisible(true);

        //Starting two threads for maintaing colors for Numbers and function buttons
        NumHighlight numberHighlight = new NumHighlight(calculator.getNumbers());
        FuncHighlight funcHighlight = new FuncHighlight(calculator.getFunctions());
        funcHighlight.start();
        numberHighlight.start();

        // Enusring a graceful exit for all threads.
        WindowListener listener = new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                numberHighlight.shutdown();
                funcHighlight.shutdown();
            }
        };
        frame.addWindowListener(listener);
        try {
            funcHighlight.join();
            numberHighlight.join();
        } catch (InterruptedException e) {
            System.out.println("Program Interrupted!!" + e.toString());
        }
    }
}
