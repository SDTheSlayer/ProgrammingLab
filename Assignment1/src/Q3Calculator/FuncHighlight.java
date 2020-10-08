package Q3Calculator;

import javax.swing.*;
import java.awt.*;


/**
 * Runnable Thread for Coloring Function buttons for the calculator
 */
public class FuncHighlight extends Thread {

    /**
     * Array to store buttons
     */
    private JButton[] funcButtons;
    private boolean exit;

    public FuncHighlight(JButton[] funcButtons) {
        this.funcButtons = funcButtons;
        this.exit = false;
    }

    @Override
    public void run() {
        // Number stores current button colored
        int number = 0;

        // Optimise is used to break a 2sec sleeping period into 200ms chucks for better reactivity of the form
        int optimize = 10;
        while (!exit) {

            if (Main.padColor == 0) {
                synchronized (Main.funcLock) {
                    funcButtons[number].setBackground(new Color(165, 110, 165));
                    try {
                        Main.funcLock.wait();
                    } catch (InterruptedException e) {
                        System.out.println("Function Highlighter Interrupted!!" + e.toString());
                    }
                    number = 0;
                    optimize = 10;
                }
            }
            //Highlighting  a button
            if (optimize == 10) {
                number = number % 6;
                funcButtons[number].setBackground(new Color(190, 70, 70));
            }

            //Sleep for 2sec total between highlighting and removing the highlight for a block
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                System.out.println("Function Highlighter Interrupted!!" + e.toString());
            }

            //Un-Highlighting the button
            if (optimize == 1) {
                funcButtons[number].setBackground(new Color(165, 110, 165));
                number++;
            }
            optimize--;
            if (optimize == 0) {
                optimize = 10;
            }
        }
    }

    public void shutdown() {
        this.exit = true;
    }
}
