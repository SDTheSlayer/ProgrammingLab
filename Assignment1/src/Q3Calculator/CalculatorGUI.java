package Q3Calculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Swing GUI class for displaying and handling different functionalities for the calculator
 */
public class CalculatorGUI {

    /**
     * List of all buttons and objects of the GUI form
     */
    private JButton a0Button;
    private JButton a1Button;
    private JButton a2Button;
    private JButton a3Button;
    private JButton a5Button;
    private JButton a6Button;
    private JButton a7Button;
    private JButton a8Button;
    private JButton addButton;
    private JButton subtractButton;
    private JButton multiplyButton;
    private JButton divideButton;
    public JPanel rootPanel;
    private JTextArea displayText;
    private JButton calculateButton;
    private JButton a9Button;
    private JButton a4Button;
    private JButton clearButton;

    /**
     * Private arrays for easier access to the button groups
     */
    private JButton[] numButtons = new JButton[10];
    private JButton[] funcButtons = new JButton[6];

    public JButton[] getFunctions() {
        return funcButtons;
    }

    public JButton[] getNumbers() {
        return numButtons;
    }

    /**
     * Constructor for the calculator. Adds all the listeners to the different buttons
     *
     * @param choice Selecting between single or multiple color and selection scheme.
     */
    public CalculatorGUI(int choice) {
        // Adding the buttons to the arrays
        funcButtons[0] = addButton;
        funcButtons[1] = subtractButton;
        funcButtons[2] = multiplyButton;
        funcButtons[3] = divideButton;
        funcButtons[4] = clearButton;
        funcButtons[5] = calculateButton;
        numButtons[0] = a0Button;
        numButtons[1] = a1Button;
        numButtons[2] = a2Button;
        numButtons[3] = a3Button;
        numButtons[4] = a4Button;
        numButtons[5] = a5Button;
        numButtons[6] = a6Button;
        numButtons[7] = a7Button;
        numButtons[8] = a8Button;
        numButtons[9] = a9Button;

        // Add a listener to take all keyboard inputs
        displayText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();

                if (choice == 2) {
                    // IF the control scheme is multi colored

                    if (key == KeyEvent.VK_ENTER) {
                        // If Enter is pressed, find the button which was highlighted and append the number to the display
                        for (int i = 0; i < 10; i++) {
                            int red = numButtons[i].getBackground().getRed();
                            int blue = numButtons[i].getBackground().getBlue();
                            int green = numButtons[i].getBackground().getGreen();
                            if (red == 0 && green == 90 && blue == 160) {
                                displayText.append(String.valueOf(i));
                                displayText.grabFocus();
                            }
                        }
                    } else if (key == KeyEvent.VK_SPACE) {
                        // If Space is pressed, find the button which was highlighted and append the operation to the display
                        for (int i = 0; i < 4; i++) {
                            int red = funcButtons[i].getBackground().getRed();
                            int blue = funcButtons[i].getBackground().getBlue();
                            int green = funcButtons[i].getBackground().getGreen();
                            if (red == 190 && green == 70 && blue == 70) {
                                displayText.append(funcButtons[i].getText());
                                displayText.grabFocus();
                            }
                        }
                        // if the highlighted button is clear, Clear the display
                        int clearRed = funcButtons[4].getBackground().getRed();
                        int clearBlue = funcButtons[4].getBackground().getBlue();
                        int clearGreen = funcButtons[4].getBackground().getGreen();
                        if (clearRed == 190 && clearBlue == 70 && clearGreen == 70) {
                            displayText.setText("");
                            displayText.grabFocus();
                        }

                        // if the highlighted button is Calculate, Evaluate the Expression in the Display box
                        int calRed = funcButtons[5].getBackground().getRed();
                        int calBlue = funcButtons[5].getBackground().getBlue();
                        int calGreen = funcButtons[5].getBackground().getGreen();
                        if (calRed == 190 && calBlue == 70 && calGreen == 70) {
                            String input = displayText.getText();

                            if (input.length() > 0) {
                                // If the input is not empty, try to evaluate the expression.
                                String ans = EvaluationEngine.eval(input);
                                displayText.setText(ans);
                                displayText.grabFocus();
                            }
                        }
                    }
                } else {

                    // Control Scheme is Single color, Similar to Dual control scheme, except need to handle locks
                    // to activate and deactivate coloring threads

                    if (Main.padColor == 0 && key == KeyEvent.VK_ENTER) {
                        for (int i = 0; i < 10; i++) {
                            int red = numButtons[i].getBackground().getRed();
                            int blue = numButtons[i].getBackground().getBlue();
                            int green = numButtons[i].getBackground().getGreen();
                            if (red == 0 && green == 90 && blue == 160) {
                                displayText.append(String.valueOf(i));

                                // Makes the num coloring thread stop
                                Main.padColor = 1;

                                // Makes the Function coloring thread start
                                synchronized (Main.funcLock) {
                                    Main.funcLock.notify();
                                }
                                displayText.grabFocus();
                                return;
                            }

                        }

                    }
                    if (Main.padColor == 1 && key == KeyEvent.VK_ENTER) {
                        for (int i = 0; i < 4; i++) {
                            int red = funcButtons[i].getBackground().getRed();
                            int blue = funcButtons[i].getBackground().getBlue();
                            int green = funcButtons[i].getBackground().getGreen();
                            if (red == 190 && green == 70 && blue == 70) {
                                displayText.append(funcButtons[i].getText());
                            }
                        }
                        int clearRed = funcButtons[4].getBackground().getRed();
                        int clearBlue = funcButtons[4].getBackground().getBlue();
                        int clearGreen = funcButtons[4].getBackground().getGreen();
                        if (clearRed == 190 && clearBlue == 70 && clearGreen == 70) {
                            displayText.setText("");
                        }
                        int calRed = funcButtons[5].getBackground().getRed();
                        int calBlue = funcButtons[5].getBackground().getBlue();
                        int calGreen = funcButtons[5].getBackground().getGreen();
                        if (calRed == 190 && calBlue == 70 && calGreen == 70) {
                            String input = displayText.getText();
                            if (input.length() > 0) {
                                String ans = EvaluationEngine.eval(input);
                                displayText.setText(ans);
                            }
                        }
                        // Makes the Function coloring thread stop
                        Main.padColor = 0;

                        // Makes the num  coloring thread start
                        synchronized (Main.numLock) {
                            Main.numLock.notify();
                        }
                        displayText.grabFocus();
                    }
                }
                displayText.grabFocus();
            }
        });

        // The below code allows to perform all the actions of the form by directly clicking on the buttons as well
        // in addition to the Color based control scheme

        MouseAdapter listener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                JButton but = (JButton) mouseEvent.getSource();
                displayText.append(but.getText());
                displayText.grabFocus();
            }
        };

        MouseAdapter clearlistener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                displayText.setText("");
                displayText.grabFocus();
            }
        };

        MouseAdapter calcListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                String input = displayText.getText();
                if (input.length() > 0) {
                    String ans = EvaluationEngine.eval(input);
                    displayText.setText(ans);
                }
                displayText.grabFocus();
            }
        };

        for (int i = 0; i < 10; i++) {
            numButtons[i].addMouseListener(listener);
        }
        for (int i = 0; i < 4; i++) {
            funcButtons[i].addMouseListener(listener);
        }
        clearButton.addMouseListener(clearlistener);
        calculateButton.addMouseListener(calcListener);

    }
// The below code is generated by using the GUI interface of IntelliJ for creating SWING forms.

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        rootPanel = new JPanel();
        rootPanel.setLayout(new GridBagLayout());
        rootPanel.setBackground(new Color(-6513508));
        rootPanel.setEnabled(true);
        Font rootPanelFont = this.$$$getFont$$$(null, -1, 26, rootPanel.getFont());
        if (rootPanelFont != null) rootPanel.setFont(rootPanelFont);
        displayText = new JTextArea();
        displayText.setAutoscrolls(true);
        displayText.setBackground(new Color(-2039584));
        displayText.setDragEnabled(true);
        displayText.setEditable(false);
        Font displayTextFont = this.$$$getFont$$$(null, -1, 26, displayText.getFont());
        if (displayTextFont != null) displayText.setFont(displayTextFont);
        displayText.setForeground(new Color(-16777216));
        displayText.setLineWrap(false);
        displayText.setMaximumSize(new Dimension(20, 32));
        displayText.setText("");
        displayText.setWrapStyleWord(false);
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.weightx = 1.0;
        gbc.weighty = 3.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.BOTH;
        rootPanel.add(displayText, gbc);
        a0Button = new JButton();
        a0Button.setBackground(new Color(-10185056));
        Font a0ButtonFont = this.$$$getFont$$$(null, -1, 28, a0Button.getFont());
        if (a0ButtonFont != null) a0Button.setFont(a0ButtonFont);
        a0Button.setForeground(new Color(-263681));
        a0Button.setText("0");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        rootPanel.add(a0Button, gbc);
        a1Button = new JButton();
        a1Button.setBackground(new Color(-10185056));
        Font a1ButtonFont = this.$$$getFont$$$(null, -1, 28, a1Button.getFont());
        if (a1ButtonFont != null) a1Button.setFont(a1ButtonFont);
        a1Button.setForeground(new Color(-263681));
        a1Button.setText("1");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        rootPanel.add(a1Button, gbc);
        a2Button = new JButton();
        a2Button.setBackground(new Color(-10185056));
        a2Button.setEnabled(true);
        Font a2ButtonFont = this.$$$getFont$$$(null, -1, 28, a2Button.getFont());
        if (a2ButtonFont != null) a2Button.setFont(a2ButtonFont);
        a2Button.setForeground(new Color(-263681));
        a2Button.setHideActionText(false);
        a2Button.setHorizontalAlignment(0);
        a2Button.setHorizontalTextPosition(0);
        a2Button.setText("2");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        rootPanel.add(a2Button, gbc);
        a3Button = new JButton();
        a3Button.setBackground(new Color(-10185056));
        Font a3ButtonFont = this.$$$getFont$$$(null, -1, 28, a3Button.getFont());
        if (a3ButtonFont != null) a3Button.setFont(a3ButtonFont);
        a3Button.setForeground(new Color(-263681));
        a3Button.setText("3");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        rootPanel.add(a3Button, gbc);
        addButton = new JButton();
        addButton.setBackground(new Color(-5935451));
        Font addButtonFont = this.$$$getFont$$$(null, -1, 28, addButton.getFont());
        if (addButtonFont != null) addButton.setFont(addButtonFont);
        addButton.setForeground(new Color(-263681));
        addButton.setText("+");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        rootPanel.add(addButton, gbc);
        subtractButton = new JButton();
        subtractButton.setBackground(new Color(-5935451));
        Font subtractButtonFont = this.$$$getFont$$$(null, -1, 28, subtractButton.getFont());
        if (subtractButtonFont != null) subtractButton.setFont(subtractButtonFont);
        subtractButton.setForeground(new Color(-263681));
        subtractButton.setText("-");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        rootPanel.add(subtractButton, gbc);
        multiplyButton = new JButton();
        multiplyButton.setBackground(new Color(-5935451));
        Font multiplyButtonFont = this.$$$getFont$$$(null, -1, 28, multiplyButton.getFont());
        if (multiplyButtonFont != null) multiplyButton.setFont(multiplyButtonFont);
        multiplyButton.setForeground(new Color(-263681));
        multiplyButton.setText("*");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        rootPanel.add(multiplyButton, gbc);
        divideButton = new JButton();
        divideButton.setBackground(new Color(-5935451));
        Font divideButtonFont = this.$$$getFont$$$(null, -1, 28, divideButton.getFont());
        if (divideButtonFont != null) divideButton.setFont(divideButtonFont);
        divideButton.setForeground(new Color(-263681));
        divideButton.setText("/");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        rootPanel.add(divideButton, gbc);
        a7Button = new JButton();
        a7Button.setBackground(new Color(-10185056));
        Font a7ButtonFont = this.$$$getFont$$$(null, -1, 28, a7Button.getFont());
        if (a7ButtonFont != null) a7Button.setFont(a7ButtonFont);
        a7Button.setForeground(new Color(-263681));
        a7Button.setText("7");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        rootPanel.add(a7Button, gbc);
        a8Button = new JButton();
        a8Button.setBackground(new Color(-10185056));
        Font a8ButtonFont = this.$$$getFont$$$(null, -1, 28, a8Button.getFont());
        if (a8ButtonFont != null) a8Button.setFont(a8ButtonFont);
        a8Button.setForeground(new Color(-263681));
        a8Button.setText("8");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        rootPanel.add(a8Button, gbc);
        a9Button = new JButton();
        a9Button.setBackground(new Color(-10185056));
        Font a9ButtonFont = this.$$$getFont$$$(null, -1, 28, a9Button.getFont());
        if (a9ButtonFont != null) a9Button.setFont(a9ButtonFont);
        a9Button.setForeground(new Color(-263681));
        a9Button.setText("9");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        rootPanel.add(a9Button, gbc);
        a6Button = new JButton();
        a6Button.setBackground(new Color(-10185056));
        Font a6ButtonFont = this.$$$getFont$$$(null, -1, 28, a6Button.getFont());
        if (a6ButtonFont != null) a6Button.setFont(a6ButtonFont);
        a6Button.setForeground(new Color(-263681));
        a6Button.setText("6");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        rootPanel.add(a6Button, gbc);
        a5Button = new JButton();
        a5Button.setBackground(new Color(-10185056));
        a5Button.setEnabled(true);
        Font a5ButtonFont = this.$$$getFont$$$(null, -1, 28, a5Button.getFont());
        if (a5ButtonFont != null) a5Button.setFont(a5ButtonFont);
        a5Button.setForeground(new Color(-263681));
        a5Button.setText("5");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        rootPanel.add(a5Button, gbc);
        a4Button = new JButton();
        a4Button.setAlignmentY(0.0f);
        a4Button.setBackground(new Color(-10185056));
        Font a4ButtonFont = this.$$$getFont$$$(null, -1, 28, a4Button.getFont());
        if (a4ButtonFont != null) a4Button.setFont(a4ButtonFont);
        a4Button.setForeground(new Color(-263681));
        a4Button.setText("4");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        rootPanel.add(a4Button, gbc);
        calculateButton = new JButton();
        calculateButton.setBackground(new Color(-5935451));
        Font calculateButtonFont = this.$$$getFont$$$(null, -1, 20, calculateButton.getFont());
        if (calculateButtonFont != null) calculateButton.setFont(calculateButtonFont);
        calculateButton.setForeground(new Color(-263681));
        calculateButton.setText("Calculate");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        rootPanel.add(calculateButton, gbc);
        clearButton = new JButton();
        clearButton.setBackground(new Color(-5935451));
        Font clearButtonFont = this.$$$getFont$$$(null, -1, 20, clearButton.getFont());
        if (clearButtonFont != null) clearButton.setFont(clearButtonFont);
        clearButton.setForeground(new Color(-263681));
        clearButton.setText("Clear");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        rootPanel.add(clearButton, gbc);
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return rootPanel;
    }
}
