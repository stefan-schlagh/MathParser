package MathParser;

/**
 * Auflistung der Operationsarten.
 * es gibt folgende Operationen:
 * <ol>
 *     <li>MULTIPLY</li>
 *     <li>DIVIDE</li>
 *     <li>ADD</li>
 *     <li>SUBTRACT</li>
 *     <li>POW: hoch</li>
 * </ol>
 * @see OperationType OperationType
 * @author Stefan Schlaghuber
 * @version 1.2
 */
public enum OperationType {
    MULTIPLY(1),
    DIVIDE(2),
    ADD(3),
    SUBTRACT(4),
    POW(5);
    private int nr;
    OperationType(int nr){
        this.nr=nr;
    }
}
