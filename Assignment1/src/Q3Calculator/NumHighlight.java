package Q3Calculator;

import javax.swing.*;
import java.awt.*;

/**
 * Runnable Thread for Coloring NUM buttons for the calculator
 */
public class NumHighlight extends Thread {

    /**
     * Array to store buttons
     */
    private JButton[] numButtons;
    private boolean exit;

    public NumHighlight(JButton[] numButtons) {
        this.numButtons = numButtons;
        this.exit = false;
    }


    @Override
    public void run() {
        // Number stores current button colored
        int number = 0;

        // Optimise is used to break a 2sec sleeping period into 200ms chucks for better reactivity of the form
        int optimize = 10;
        while (!exit) {

            // Pad Color and numLock control when the thread should be active
            if (Main.padColor == 1) {
                synchronized (Main.numLock) {
                    numButtons[number].setBackground(new Color(100, 150, 160));
                    try {
                        Main.numLock.wait();
                    } catch (InterruptedException e) {
                        System.out.println("Number Highlighter Interrupted!!" + e.toString());
                    }
                    number = 0;
                    optimize = 10;
                }
            }

            //Highlighting  a button
            if (optimize == 10) {
                number = number % 10;
                numButtons[number].setBackground(new Color(0, 90, 160));
            }

            //Sleep for 2sec total between highlighting and removing the highlight for a block
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                System.out.println("Number Highlighter Interrupted!!" + e.toString());
            }

            //Un-Highlighting the button
            if (optimize == 1) {
                numButtons[number].setBackground(new Color(100, 150, 160));
                number++;
            }
            optimize--;
            if (optimize == 0) {
                optimize = 10;
            }
        }
    }

    /**
     * Function for graceful Exiting of thread
     */
    public void shutdown() {
        this.exit = true;
    }
}
