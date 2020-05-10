package MathParser;

/**
 * Exception, die geworfen wird, wenn Term nicht gueltig ist.
 * @see java.lang.Exception Exception
 * @author Stefan Schlaghuber
 * @version 1.2
 */
public class TermNotValidException extends Exception {
    public TermNotValidException(String cause){
        super(cause);
    }
}
