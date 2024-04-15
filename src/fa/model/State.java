package fa.model;

import java.util.Collection;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Queue;

/**
 * Class to represent the state in the automaton.
 */
public class State {
    private final int id;
    private static final int ALPHABET_LENGTH =
            Automaton.LAST_SYMBOL - Automaton.FIRST_SYMBOL;
    private final List<Collection<Transition>> charAdj =
            new ArrayList<>(ALPHABET_LENGTH + 1);
    private Collection<State> nextSet;

    /**
     * Create a new state with the given id.
     *
     * @param id Id of State.
     */
    public State(int id) {
        this.id = id;
        for (int i = 0; i < ALPHABET_LENGTH + 1; i++) {
            charAdj.add(i, null);
        }
    }

    /**
     * Returns the next set of a state after calling a help method to compute
     * the next set.
     *
     * @return The next set of a state.
     */
    protected Collection<State> getNextSet() {
        precomputeNextSet();
        return nextSet;
    }

    /**
     * Adding a transition into a list of transitions according to the symbol
     * in the transition,if the symbol is Lambda then the transition will be
     * stored at the index 0 in the list otherwise it will be stored
     * according to alphabet from index 1.
     *
     * @param transition The transition to store in the list.
     */
    protected void addTransition(Transition transition) {
        int index = convertToIndex(transition.getSymbol());
        Collection<Transition> transitions = charAdj.get(index);
        if (transitions == null) {
            transitions = new LinkedList<>();
            charAdj.set(index, transitions);
        }
        transitions.add(transition);
    }

    /**
     * Returns collection of target states that could be reached from a state
     * with the given symbol, to determine the index of the transitions list
     * after checking if it is a valid symbol.
     *
     * @param symbol The symbol determine the index of the list.
     * @return Collection of target states.
     */
    protected Collection<State> getTargets(char symbol) {
        Collection<State> targets = new LinkedList<>();
        int index = convertToIndex(symbol);
        if (charAdj.get(index) != null) {
            for (Transition t : charAdj.get(index)) {
                targets.add(t.getTarget());
            }
        }
        return targets;
    }

    /**
     * Returns Ordered transitions of a state according to the target state.
     *
     * @return Ordered transitions of a state.
     */
    protected List<Transition> getOrderedTransitions() {
        List<Transition> adj = new LinkedList<>();
        for (Collection<Transition> transitions : charAdj) {
            if (transitions == null) {
                continue;
            }
            adj.addAll(transitions);
        }
        Collections.sort(adj);
        return adj;
    }

    /**
     * Returns a string representation of the state as id.
     *
     * @return State as string.
     */
    @Override
    public String toString() {
        return "" + id;
    }

    /**
     * Returns id of a state.
     *
     * @return Id of a state.
     */
    public int getId() {
        return id;
    }

    /**
     * Method to compute the next set of a state.
     */
    private void precomputeNextSet() {
        nextSet = new LinkedList<>();

        // Perform breadth first search.
        Map<State, Boolean> visited = new HashMap<>();
        Queue<State> bfsQueue = new LinkedList<>();
        bfsQueue.offer(this);
        visited.put(this, true);
        while (!bfsQueue.isEmpty()) {
            State state = bfsQueue.poll();

            // State itself does not belong to its next set.
            if (state != this) {
                nextSet.add(state);
            }
            Collection<State> lambdaTargets =
                    state.getTargets(LambdaNFA.LAMBDA);
            for (State lambdaTarget : lambdaTargets) {
                if (!visited.getOrDefault(lambdaTarget, false)) {
                    bfsQueue.offer(lambdaTarget);
                    visited.put(lambdaTarget, true);
                }
            }
        }
    }

    /**
     * Returns index according to the given character.
     *
     * @param ch The character to convert.
     * @return The index number of the character.
     */
    private int convertToIndex(char ch) {

        // Adding one because index 0 reserved for Lambda.
        int index = ch - Automaton.FIRST_SYMBOL + 1;
        if (ch == LambdaNFA.LAMBDA) {
            index = 0;
        }
        return index;
    }

}




