package fa.model;

/**
 * Represents a transition of a state.
 */
public class Transition implements Comparable<Transition> {
    private final State source;
    private final State target;
    private final char symbol;

    /**
     * Create a new transition of a state with the given source and
     * target, and symbol.
     *
     * @param source The source state.
     * @param target The target state.
     * @param symbol The given symbol.
     */
    public Transition(State source, State target, char symbol) {
        this.source = source;
        this.target = target;
        this.symbol = symbol;
    }

    /**
     * Returns a string representation of the transition as
     * this form (i, j) c.
     *
     * @return Transition as string.
     */
    @Override
    public String toString() {
        return "(" + source + ", " + target + ") " + symbol;
    }

    /**
     * Returns a negative integer, zero, or a positive integer as this
     * transition is less than, equal to, or greater than the specified
     * transition.Comparing transitions according to the target state.
     *
     * @param other The transition to be compared.
     * @return Negative number if id target greater than other target id,
     *          positive if id target less than other target id
     *          and 0 if id target equal to other target id.
     */
    @Override
    public int compareTo(Transition other) {
        if (this.target.getId() < other.target.getId()) {
            return -1;
        } else if (this.target.getId() > other.target.getId()) {
            return 1;
        }
        return 0;
    }

    /**
     * Returns the symbol in the transition.
     *
     * @return symbol
     */
    public char getSymbol() {
        return symbol;
    }

    /**
     * Returns the target in the transition.
     *
     * @return target.
     */
    public State getTarget() {
        return target;
    }

}