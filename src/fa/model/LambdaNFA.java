package fa.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * A class for finite automatons accepting formal,regular
 * languages.
 */
public class LambdaNFA implements Automaton {
    private final List<State> states;
    /**
     * Lambda symbol.
     */
    static final char LAMBDA = '~';
    private final State start;
    private final int numOfStates;

    /**
     * Generate a finite automaton with the given number of states.
     *
     * @param numOfStates Number of states in the automaton.
     */
    public LambdaNFA(int numOfStates) {
        this.numOfStates = numOfStates;
        this.states = new ArrayList<>();
        generate();
        this.start = states.get(0);
    }

    /**
     * Fill id for each state in the automaton start from 1.
     */
    private void generate() {
        for (int i = 0; i < numOfStates; i++) {
            states.add(i, new State(i + 1));
        }
    }

    /**
     * Checks a transition on validity, if it can be part of the
     * automaton.
     *
     * @param source The id of the source state.
     * @param target The id of the target state.
     * @param symbol The symbol to be read.
     * @return {@code true} if and only if the parameters represent a valid
     *         transition of this automation.
     */
    @Override
    public boolean isValidTransition(int source, int target, char symbol) {
        return (source > 0 && source <= numOfStates)
                && (target > 0 && target <= numOfStates)
                && ((symbol >= Automaton.FIRST_SYMBOL
                && symbol <= Automaton.LAST_SYMBOL)
                || symbol == LambdaNFA.LAMBDA);
    }

    /**
     * Adds a transition to the automaton. Multitransitions, i.e. transitions
     * with equal {@code source}s, {@code target}s and {@code symbol}s, and self
     * loops, i.e., with {@code source} equal to {@code target} are explicitly
     * allowed.
     *
     * @param source The source state.
     * @param target The target state.
     * @param symbol The symbol to read.
     */
    @Override
    public void addTransition(int source, int target, char symbol) {
        if (isValidTransition(source, target, symbol)) {
            Transition t = new Transition(states.get(source - 1),
                    states.get(target - 1), symbol);
            states.get(source - 1).addTransition(t);
        }
    }

    /**
     * Decides the element problem for the regular language defined by this
     * automaton.If the given word is the same as the longest prefix then the
     * given word is in language.
     *
     * @param word The word to check.
     * @return {@code true} if and only if {@code word} is in the language.
     */
    @Override
    public boolean isElement(String word) {
        String longestPre = longestPrefix(word);
        return word.equals(longestPre);
    }

    /**
     * Compute the longest prefix in the given word, if exists.
     *
     * @param word The word whose prefixes will be checked.
     * @return The longest prefix, or {@code null} if none exists.
     */
    @Override
    public String longestPrefix(String word) {
        LinkedList<State> queue = new LinkedList<>();
        State state;
        char symbol = LAMBDA;
        String currLongestPrefix = null;
        State dollar = new State(-1);
        queue.offer(dollar);
        queue.offer(start);
        queue.addAll(start.getNextSet());
        int cursor = -1;
        while (!queue.isEmpty()) {
            state = queue.poll();
            if (state == dollar) {
                ++cursor;
                if (cursor < word.length()) {
                    queue.offer(dollar);
                    symbol = word.charAt(cursor);
                }
            } else if (state == states.get(states.size() - 1)
                    && cursor <= word.length()) {
                currLongestPrefix = word.substring(0, cursor);
            }
            if (cursor < word.length()) {
                for (State target : state.getTargets(symbol)) {
                    queue.offer(target);
                    queue.addAll(target.getNextSet());
                }
            }
        }
        return currLongestPrefix;
    }

    /**
     * Returns a string representation of the automaton as sorted list of all
     * (lexicographically sorted) transitions.
     *
     * @return Automaton as string.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (State state : states) {
            for (Transition t : state.getOrderedTransitions()) {
                sb.append(t).append(System.lineSeparator());
            }
        }
        return sb.toString();
    }

}
