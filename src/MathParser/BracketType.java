package MathParser;

/**
 * Auflistung der Klammerarten.
 * es gibt folgende Klammern:
 * <ol>
 *     <li>OPEN</li>
 *     <li>CLOSE</li>
 * </ol>
 * @see Bracket Bracket
 * @author Stefan Schlaghuber
 * @version 1.2
 */
public enum BracketType {
    OPEN(1),
    CLOSE(2);
    private int nr;
    BracketType(int nr){
        this.nr=nr;
    }
}
