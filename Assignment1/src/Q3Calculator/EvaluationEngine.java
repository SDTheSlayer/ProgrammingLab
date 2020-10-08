package Q3Calculator;

import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

/**
 * Class for evaluating a Mathematical expression given as a string. Based on JavaScript script engine
 */
public class EvaluationEngine {

    /**
     * Checks if a String can be converted to a Interger or a Double
     *
     * @param strNum The number to parse in String format
     * @return boolean value to indicate if the input string is parsable
     */
    private static boolean isNumeric(String strNum) {
        // Null checks
        if (strNum == null) {
            return false;
        } else if (strNum.equals("")) {
            return false;
        }
        int a = 0;

        // try to parse as double
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            a += 1;
        }

        // try to parse as Integer
        try {
            int d = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            a += 1;
        }

        // if cant do both , return false
        return a != 2;

    }

    /**
     * Checks if a char is a operand
     */
    private static boolean uniSymbols(char check) {
        String symbols = "+-/*";
        for (int i = 0; i < symbols.length(); i++) {
            if (symbols.charAt(i) == check) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a char is valid pair of operands
     */
    private static boolean pairSymbols(String check) {
        String[] pair_symbols = new String[]{"+-", "/-", "*-", "--"};
        for (String s : pair_symbols) {
            if (s.equals(check)) {
                return true;
            }
        }
        return false;
    }


    /**
     * Checks if a given input has a group of 3 operands or a pair of invalid operands
     */
    private static Boolean conscSymbol(String input) {
        for (int i = 0; i < input.length() - 2; i++) {
            if (uniSymbols(input.charAt(i)) && uniSymbols(input.charAt(i + 1)) && uniSymbols(input.charAt(i + 2))) {
                return false;
            }
        }
        for (int i = 0; i < input.length() - 1; i++) {
            if (uniSymbols(input.charAt(i)) && uniSymbols(input.charAt(i + 1))) {
                if (!pairSymbols(input.substring(i, i + 2))) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Pre processing the input string to remove precedding zeros, as the parser considers these as hexadecimal values
     */
    private static String removeZeros(String input) {

        // Bool input find the zeros to remove from the input string
        boolean[] boolInput = new boolean[input.length()];

        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == '0') {
                // Exceptional cases for first and last char
                if (i == 0) {
                    boolInput[i] = false;
                } else if (i == input.length() - 1) {
                    boolInput[i] = true;
                }
                // Remove zeros if they train operands. However if they precede an operand retain a single 0
                else {
                    char prev = input.charAt(i - 1);
                    char next = input.charAt(i + 1);
                    if (prev == '0') {
                        boolInput[i] = boolInput[i - 1];
                        if (next == '*' || next == '/' || next == '-' || next == '+') {
                            boolInput[i] = true;
                        }
                    } else if (prev == '*' || prev == '/' || prev == '-' || prev == '+') {
                        boolInput[i] = next == '*' || next == '/' || next == '-' || next == '+';
                    } else {
                        boolInput[i] = true;
                    }
                }
            } else {
                boolInput[i] = true;
            }
        }

        // Create a new string removing all the unnecessary zeros
        StringBuilder input2 = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            if (boolInput[i]) {
                input2.append(input.charAt(i));
            }
        }
        return input2.toString();
    }


    /**
     * Fucntion exposed to evaluate a given expression
     *
     * @param input Mathematical expression in form of string
     * @return String either containing evaluated answer or Invalid input.
     */
    public static String eval(String input) {

        // Perform various preprocessing steps, described in the functions
        if (input.length() == 0) {
            return ("");
        }
        input = removeZeros(input);
        if (!conscSymbol(input)) {
            return ("Invalid Input!!");
        }

        // Final answer generated using the JavaScript Engine
        String ans = "";
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("JavaScript");

        try {
            ans = String.valueOf(scriptEngine.eval(input));
        } catch (ScriptException e) {
            return ("Invalid Input!!");
        }
        if (!(ans.equals("Infinity") || isNumeric(ans))) {
            return ("Invalid Input!!");
        }
        return (ans);
    }

}
