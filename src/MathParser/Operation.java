package MathParser;
/**
 * Dieser Knoten ist f&uuml;r Rechenoperationen gedacht.
 * @see Term Term
 * @see TermKnot TermKnot
 * @see OperationType OperationType
 * @author Stefan Schlaghuber
 * @version 1.2
 */
public class Operation extends TermKnot{
    /**
     * Operation, die im Knoten gespeichert wird.
     */
    private OperationType operation;

    /**
     * Konstruktor f&uuml;r Operation.
     * @param operation Operation
     */
    public Operation(OperationType operation){
        this.operation=operation;
    }

    /**
     * Operation wird zurueckgegeben
     * @return Operation
     */
    public OperationType getOperation() {
        return operation;
    }

    /**
     * Operation wird ge&auml;ndert.
     * @param operation neue Operation.
     */
    public void setOperation(OperationType operation) {
        this.operation = operation;
    }

    /**
     * ist Operation Multiplikation oder Division
     * @return true wenn ja
     */
    public boolean isMultiplicationOrDivision(){
        return operation==OperationType.MULTIPLY||operation==OperationType.DIVIDE;
    }
    /**
     * ist Operation Addition oder Subtraktion
     * @return true wenn ja
     */
    public boolean isAdditionOrSubtraction(){
        return operation==OperationType.ADD||operation==OperationType.SUBTRACT;
    }
    /**
     * ist Operation Multiplikation
     * @return true wenn ja
     */
    public boolean isMultiplication(){
        return operation==OperationType.MULTIPLY;
    }
    /**
     * ist Operation Division
     * @return true wenn ja
     */
    public boolean isDivision(){
        return operation==OperationType.DIVIDE;
    }
    /**
     * ist Operation Addition
     * @return true wenn ja
     */
    public boolean isAddition(){
        return operation==OperationType.ADD;
    }
    /**
     * ist Operation Subtraktion
     * @return true wenn ja
     */
    public boolean isSubtraction(){
        return operation==OperationType.SUBTRACT;
    }

    /**
     * ist Operation hoch
     * @return true wenn ja
     */
    public boolean isPow(){
        return  operation==OperationType.POW;
    }
}
