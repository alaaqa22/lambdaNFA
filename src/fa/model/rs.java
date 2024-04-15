package fa.model;

import java.util.*;
import java.util.regex.Pattern;

public class rs {
     List<State> states = new ArrayList<>();


    private static final String CHARS_REX
            = "[" + Automaton. FIRST_SYMBOL + "-" + Automaton.LAST_SYMBOL +
            "]*";
   // String s = "^$";
    private static final Pattern CHARS = Pattern.compile(CHARS_REX);

    public static void main(String[] args) {

        String w = "Alaabb";
        String w1 = "Alaabb";
        System.out.println(w.substring(0,4));
        String currLongestPrefix = w.replace(w.charAt(w.length()-1),' ');
        System.out.println(currLongestPrefix);

    }

}
