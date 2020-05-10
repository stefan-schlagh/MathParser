package MathParser;

/**
 * Dieser Knoten ist f&uuml;r Klammern gedacht.
 * @see Term Term
 * @see TermKnot TermKnot
 * @see BracketType Brackettype
 * @author Stefan Schlaghuber
 * @version 1.2
 */
public class Bracket extends TermKnot{
    /**
     * Die Klammer, die im Knoten gespeichert wird.
     */
    private BracketType bracketType;
    /**
     * Konstruktor f&uuml;r eine Klammer.
     * @param bracketType Die Art der Klammer (open oder close).
     */
    public Bracket(BracketType bracketType){
        this.bracketType=bracketType;
    }

    /**
     * Gibt Art der Klammer zur&uuml;ck.
     * @return Art der Klammer.
     */
    public BracketType getBracketType() {
        return bracketType;
    }

    /**
     * Klammerart aendern.
     * @param bracketType Art der Klammer.
     */
    public void setBracketType(BracketType bracketType) {
        this.bracketType = bracketType;
    }
}
