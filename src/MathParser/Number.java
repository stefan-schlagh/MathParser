package MathParser;
/**
 * Dieser Knoten ist f&uuml;r Zahlen gedacht. Diese werden als double gespeichert.
 * @see Term Term
 * @see TermKnot TermKnot
 * @author Stefan Schlaghuber
 * @version 1.2
 */
public class Number extends TermKnot{
    /**
     * Zahl, die im Knoten gespeichert wird.
     */
    private double value;
    /**
     * Konstruktor f&uuml;r Zahl.
     * @param value Zahl
     */
    public Number(double value){
        this.value=value;
    }

    /**
     * Zahl wird zur&uuml;ckgegeben.
     * @return Zahl
     */
    public double getValue() {
        return value;
    }

    /**
     * Methode zum &auml;ndern der Zahl.
     * @param value Zahl
     */
    public void setValue(double value) {
        this.value = value;
    }
}
