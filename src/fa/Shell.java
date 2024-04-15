package fa;

import fa.model.Automaton;
import fa.model.LambdaNFA;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

/**
 * Implements the shell functionality to interact
 * with lambda nondeterministic finite automaton.
 */
public final class Shell {
    private static final String CHARS_REX = "[" + Automaton.FIRST_SYMBOL
            + "-" + Automaton.LAST_SYMBOL + "]*";
    private static final Pattern CHARS = Pattern.compile(CHARS_REX);
    private static final String PROMPT = "nfa> ";
    private static Automaton automaton;

    private Shell() {
        throw new UnsupportedOperationException("Illegal call of utility "
                + "class constructor.");
    }

    /**
     * Reads and processes input until the quit command has been entered.
     *
     * @param args Command line arguments.
     * @throws IOException Error reading from stdin.
     */
    public static void main(String[] args) throws IOException {
        BufferedReader b = new BufferedReader(new InputStreamReader(System.in));
        boolean quit = false;
        while (!quit) {
            System.out.print(PROMPT);
            String input = b.readLine();
            if (input == null) {
                break;
            }
            String[] tokens = input.trim().split("\\s+");
            if (tokens.length < 1 || tokens[0].isEmpty()) {
                printError("Empty Input.");
            } else {
                switch (tokens[0].toUpperCase().charAt(0)) {
                    case 'I':
                        generate(tokens);
                        break;
                    case 'A':
                        addTransition(tokens);
                        break;
                    case 'C':
                        checkWord(tokens);
                        break;
                    case 'P':
                        getPrefix(tokens);
                        break;
                    case 'D':
                        display();
                        break;
                    case 'G':
                        debug();
                        break;
                    case 'H':
                        help();
                        break;
                    case 'Q':
                        quit = true;
                        break;
                    default:
                        printError("Unknown command.");
                        break;
                }
            }
        }
    }

    private static void generate(String[] tokens) {
        if (tokens.length < 2) {
            printError("Wrong number of arguments.");
            return;
        }
        Integer numOfStates;
        try {
            numOfStates = Integer.parseInt(tokens[1]);
        } catch (NumberFormatException e) {
            printError("Could not parse number.");
            return;
        }
        if (numOfStates > 0) {
            automaton = new LambdaNFA(numOfStates);
        } else {
            printError("Only positive numbers accepted.");
        }
    }

    private static void addTransition(String[] tokens) {
        if (automaton == null) {
            printError("No automaton.");
        } else if (tokens.length < 4) {
            printError("Two numbers and one character expected.");
        } else {
            int source, target;
            char symbol;
            if (tokens[3].length() == 1) {
                symbol = tokens[3].charAt(0);
            } else {
                printError("Unacceptable symbol.");
                return;
            }
            try {
                source = Integer.parseInt(tokens[1]);
                target = Integer.parseInt(tokens[2]);
            } catch (NumberFormatException e) {
                printError("Could not parse number.");
                return;
            }
            if (automaton.isValidTransition(source, target, symbol)) {
                automaton.addTransition(source, target, symbol);
            } else {
                printError("Unacceptable transition.");
            }
        }
    }

    private static void checkWord(String[] tokens) {
        if (automaton == null) {
            printError("No automaton.");
        } else if (tokens.length < 2) {
            printError("Wrong number of arguments.");
        } else {
            String input = tokens[1];
            if (isValidForm(input)) {
                String word = input.substring(1, input.length() - 1);
                if (isValidWord(word)) {
                    if (automaton.isElement(word)) {
                        System.out.println("In language.");
                    } else {
                        System.out.println("Not in language.");
                    }
                }
            } else {
                printError("Word must be surrounded with double quotation.");
            }
        }

    }

    private static void getPrefix(String[] tokens) {
        if (automaton == null) {
            printError("No automaton.");
        } else if (tokens.length < 2) {
            printError("Wrong number of arguments.");
        } else {
            String input = tokens[1];
            if (isValidForm(input)) {

                //remove double quotation.
                String word = input.substring(1, input.length() - 1);
                if (isValidWord(word)) {
                    String longestPre = automaton.longestPrefix(word);
                    if (longestPre != null) {
                        System.out.println("\"" + longestPre + "\"");
                    } else {
                        System.out.println("No prefix in language.");
                    }
                }
            } else {
                printError("Word must be surrounded by double quotation.");
            }
        }
    }

    /**
     * Print the automaton.
     */
    private static void display() {
        if (automaton == null) {
            printError("No automaton");
        } else {
            System.out.print(automaton);
        }
    }

    /**
     * Check a word on validity.
     *
     * @param word The word to check.
     * @return {@code true} if and only if the word contains allowed
     * characters.
     */
    private static boolean isValidWord(String word) {
        boolean valid = CHARS.matcher(word).matches();
        if (!valid) {
            printError("Word contains illegal characters.");
        }
        return valid;
    }

    /**
     * Check a form on validity.
     *
     * @param input The input to be read.
     * @return {@code true} if and only if the word surrounded by double
     * quotation.
     */
    private static boolean isValidForm(String input) {
        return input != null && input.length() >= 2
                && input.charAt(0) == '\"'
                && input.charAt(input.length() - 1) == '\"';
    }

    /**
     * Print the error.
     *
     * @param message  error message.
     */
    private static void printError(String message) {
        System.err.println("Error! " + message);
    }

    private static void debug() {
        automaton = new LambdaNFA(5);
        automaton.addTransition(1, 1, 'a');
        automaton.addTransition(1, 1, 'b');
        automaton.addTransition(1, 2, '~');
        automaton.addTransition(2, 3, 'b');
        automaton.addTransition(2, 4, 'a');
        automaton.addTransition(3, 4, '~');
        automaton.addTransition(3, 5, '~');
        automaton.addTransition(4, 5, 'b');
    }

    /**
     * Print the help.
     */
    private static void help() {
        System.out.println("Lambda nondeterministic finite automaton.");
        System.out.println("Accepted commands: ");
        System.out.println("INITIALIZE <number>:  Initialize new automaton "
                + "with the given number of states.");
        System.out.println("ADD <number> <number> <character>: "
                + "Enter a transition.");
        System.out.println("CHECK <\"word\">:  Check if the given word "
                + "in language.");
        System.out.println("PREFIX<\"word\">:  Computes the longest "
                + "prefix in the given word.");
        System.out.println("DISPLAY:         Show the automaton as "
                + "sorted List of transitions.");
        System.out.println("GENERATE:        Generate a hard coded automaton.");
        System.out.println("QUIT:            Exit the program.");

    }

}



