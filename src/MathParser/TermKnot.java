package MathParser;

/**
 * Die Superklasse aller Termknoten.
 * @see Bracket Bracket
 * @see Number Number
 * @see Operation Operation
 * @see Term Term
 * @author Stefan Schlaghuber
 * @version 1.2
 */
public class TermKnot {
    /**
     * Der Knoten davor in der Liste
     */
    public TermKnot prev;
    /**
     * Der knoten danach in der Liste.
     */
    public TermKnot next;
}
