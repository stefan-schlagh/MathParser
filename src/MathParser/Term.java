package MathParser;

import java.text.ParseException;

/**
 * ein Term ist eine Liste bestehend aus mathematischen Ausdr&uuml;cken.
 * <p>Da Term selbst ein TermKnoten ist, kann dieser verschachtelt werden.
 * Diese Funktionsweise wird intern verwendet und sollte nicht von außerhalb verwendet werden.</p>
 * @see TermKnot TermKnot
 * @see TermNotValidException TermNotValidException
 * @author Stefan Schlaghuber
 * @version 1.2
 */
public class Term extends TermKnot{
    /**
     * Start des Terms, erster Termknoten.
     */
    private TermKnot start;
    /**
     * Ende des Terms, letzter Termknoten.
     */
    private TermKnot end;
    /**
     * Hier wird der Ausgangsterm gespeichert, wenn term berechnet wird.
     */
    private Term originalTerm;
    /**
     * Gibt an, ob dieser Term die Wurzel ist.
     */
    private boolean isRoot;
    /**
     * Mit diesem Konstrukter erstellte Terme sind automatisch root, prev und next sind null.
     */
    public Term(){
        //start = new Number(2);
        /*
            prev und next ist null, wenn term root ist
         */
        this.isRoot=true;
        //this.prev=null;
        //this.next=null;
    }

    /**
     * Mit diesem Konstruktor kann ein Sting mit Mathematischen Ausdrücken geparst werden.
     * @param mathString mathematischer Ausdruck
     * @throws ParseException ParseException
     */
    public Term(String mathString)throws ParseException{
        this.isRoot=true;
        parseMath(mathString);
    }

    /**
     * Konstruktor, um Terme zu verschachteln. Er benötigt next und prev. Mit diesem Konstuktor erstellte Terme sind automatisch nicht-root.
     * Nicht f&uuml;r den Eigengebrauch geeignet, nur zur internen Verwendung.
     * @param prev  vorheriger TermKnoten
     * @param next  folgender TermKnoten
     */
    public Term(TermKnot prev,TermKnot next){
        isRoot=false;
        this.prev=prev;
        this.next=next;
    }

    /**
     * Knoten wird hinten angef&uuml;gt.
     * @param knot Knoten, der angef&uuml;gt werden soll.
     */
    public void add(TermKnot knot){
        if(start==null){
            start=knot;
            end=knot;
            knot.next=null;
            knot.prev=null;
        }else if(start==end){
            end=knot;
            knot.next=null;
            end.prev=start;
            start.next=end;
        }else{
            TermKnot h = this.end;
            h.next=knot;
            knot.prev=h;
            knot.next=null;
            end=knot;
        }
    }

    /**
     * Knoten wird aus Liste gel&ouml;scht.
     * @param tk zu l&ouml;schende Referenz.
     */
    public void delete(TermKnot tk){
        if (tk.prev != null) {
            tk.prev.next = tk.next;
        }
        if(tk.next!=null) {
            tk.next.prev = tk.prev;
        }
    }

    /**
     * TermKnoten wird mit neuem Term &uuml;berschrieben.
     * @param currentTerm Term, der &uuml;berschrieben werden soll.
     */
    private void overrideTermKnotWithTerm(TermKnot currentTerm){
        Term newTerm = new Term(currentTerm.prev,currentTerm.next);
        newTerm.add(currentTerm);
        if(currentTerm==start){
            start=newTerm;
            start.next.prev=newTerm;
        }
        if(currentTerm==end){
            end=newTerm;
            end.prev.next=newTerm;
        }
        currentTerm=newTerm;
        if(currentTerm.prev!=null)
            currentTerm.prev.next=currentTerm;
        if(currentTerm.next!=null)
            currentTerm.next.prev=currentTerm;
    }

    /**
     * Mit dieser Methode wird der Ausdruck geordnet, sodass alle Rechenregeln eingehalten werden. wird nur intern von calculate() aufgerufen.
     */
    private void rankAfterPriority(){
        /*
            Bei dieser Methode wird angenommen, dass Term bereits validiert ist

            1. Klammernausdrücke höchster Priorität in Term verfrachten
            2. Multiplikationen und Divisionen ausserhalb von Klammernausdrücken in Term verfrachten
            3. rankAfterPriority bei neuen Termen ausführen

            nach rank after priority folgt of Number o. Term immer Operation und umgekehrt
         */
        /*
            Klammern:
         */
        int bracketLayer = 0;
        boolean termOpen = false;
        for(TermKnot i=start;i!=null;i=i.next) {
            /*
                es wird nach Brackets gesucht.
             */
            if (i instanceof Bracket) {
                Bracket bracket = (Bracket) i;
                if (bracket.getBracketType() == BracketType.OPEN) {
                    bracketLayer++;
                } else if (bracket.getBracketType() == BracketType.CLOSE) {
                    bracketLayer--;
                }
            }
            /*
                wenn Bracketlayer > 0 && termOpen = false
                --> neuer Term wird auf derzeitigen Index gespeichert,
                    derzeitiger Inhalt = Klammer Layer 1 --> wird nicht gespeichert, da aufgelöst
             */
            if (bracketLayer > 0 && !termOpen) {
                /*
                    i=open-bracket;
                    i wird überschrieben, da next und prev nicht übernommen werden, werden sie neu gespeichert
                 */
                TermKnot h=i;
                i=new Term(i.prev,i.next);
                termOpen = true;
                if(h==start){
                    start=i;
                    start.next.prev=i;
                }
                if(h==end){
                    end=i;
                    end.prev.next=i;
                }
                if(i.prev!=null)
                    i.prev.next=i;
                if(i.next!=null)
                    i.next.prev=i;
            }
            /*
                sonst wenn Bracketlayer > 0 term weiter befüllen
                --> derzeitiger index wird gelöscht
             */
            else if(bracketLayer > 0 && termOpen){
                /*
                    da i deleted --> i = vorhiger TermKnoten
                 */
                TermKnot h=i;
                this.delete(i);
                i=h.prev;
                /*
                    hier muss direkt davor ein Term stehen -> dieser wird weiter befüllt
                 */
                if(i instanceof Term){
                    ((Term) i).add(h);
                }
            }
            /*
                wenn bracketLayer = 0, termOpen aber noch true
                --> termOpen wird auf false gesetzt, derzeitiger Index wird gelöscht
             */
            if(bracketLayer == 0 && termOpen){
                /*
                    i = close-bracket
                    da i deleted --> i = vorhiger TermKnoten
                 */
                TermKnot h=i;
                this.delete(i);
                i=h.prev;
                /*
                    Term vor close-bracket wird gerankt (rekursion dieser Methode)
                 */
                if(i instanceof Term){
                    ((Term) i).rankAfterPriority();
                }
                termOpen = false;
            }
        }
        /*
            TODO:   neue Schleife mit hoch
         */
        termOpen = false;
        for(TermKnot i = start;i!=null;i=i.next){
            /*
                wenn nächste Rechenoperation hoch:
                --> jede Operation mit hoch kommt in einen eigenen term, da nach Klammern die höchste Priorität.
             */
            if(i.next instanceof Operation){
                Operation o = (Operation)i.next;
                if(o.isPow() && !termOpen){
                    overrideTermKnotWithTerm(i);
                    termOpen=true;
                }
                else if(o.isPow() && termOpen){
                    TermKnot h=i;
                    this.delete(i);
                    i=h.prev;
                    if(i instanceof Term){
                        ((Term) i).add(h);
                    }
                }else if(termOpen){
                    TermKnot h=i;
                    this.delete(i);
                    i=h.prev;
                    if(i instanceof Term){
                        ((Term) i).add(h);
                    }
                    termOpen=false;
                }
            }else if(i.next!=null){// i instance of Operation && OperationType=pow
                Operation o = (Operation)i;
                if(o.isPow()) {
                    TermKnot h = i;
                    this.delete(i);
                    i = h.prev;
                    if (i instanceof Term) {
                        ((Term) i).add(h);
                    }
                }
            }else if(i.prev instanceof Term){//i.next==null
                TermKnot h=i;
                this.delete(i);
                i=h.prev;
                end=i;
                if(i instanceof Term){
                    ((Term) i).add(h);
                }
                termOpen=false;
            }
        }
        /*
        TODO END
         */
        /*
            neue for Schleife: Punktrechnung - Strichrechnung
        */
        termOpen = false;
        for(TermKnot i = start;i!=null;i=i.next) {
            /*
                wenn nächste Operation Multiplikation oder Division
                --> neuer Term wird auf derzeitigen Index gespeichert, inkl. derzeitigem Inhalt
             */
            if(i.next instanceof Operation){
                Operation o = (Operation)i.next;
                if(o.isMultiplicationOrDivision()&& !termOpen){
                    /*
                        es wird ein neuer Term erstellt, diesem wird der Wert von i eingefügt und anschließend wird i mit h überschrieben
                        prev kann jetzt schon eingetragen werden, next noch nicht
                     */
                    overrideTermKnotWithTerm(i);
                    termOpen=true;
                }else if(o.isMultiplicationOrDivision()&& termOpen){
                    /*
                        nächster TermKnoten wird gelöscht und in vorherige Termlist eingetragen
                     */
                    TermKnot h=i;
                    this.delete(i);
                    i=h.prev;
                    if(i instanceof Term){
                        ((Term) i).add(h);
                    }
                }else if(o.isAdditionOrSubtraction() && termOpen){
                    /*
                        da nächstes Zeichen + oder - ist, wird TermOpem wieder false gesetzt,
                        davor wird noch i in diese gespeichert
                     */
                    TermKnot h=i;
                    this.delete(i);
                    i=h.prev;
                    if(i instanceof Term){
                        ((Term) i).add(h);
                    }
                    termOpen=false;
                }
                /*
                    wenn Termopen false und nächstes Zeichen + oder - wird nichts gemacht
                 */
            }
            /*
                * und / werden auch in term gespeichert
             */
            else if(i.next!=null){//(i instanceof Operation){
                Operation o = (Operation)i;
                /*
                    Statement sollte nur erreicht werden, wenn o Multiplication oder Division
                     -> wenn addition o. division -> nix
                 */
                if(o.isMultiplicationOrDivision()) {
                    TermKnot h = i;
                    this.delete(i);
                    i = h.prev;
                    if (i instanceof Term) {
                        ((Term) i).add(h);
                    }
                }
            }else if(i.prev instanceof Term){//i.next==null
                TermKnot h=i;
                this.delete(i);
                i=h.prev;
                end=i;
                if(i instanceof Term){
                    ((Term) i).add(h);
                }
                termOpen=false;
            }
        }
    }

    /**
     * Ausdruck wird berechnet.
     * Vorher wird dieser noch &uuml;berpr&uuml;ft, ob in Ordnung. Wenn nicht, wird eine TermNotValidException mit dem Grund geworfen.
     * @return das Ergebnis
     * @throws TermNotValidException wird geworfen, wenn term nicht g&uuml;ltig
     */
    public double calculate() throws TermNotValidException,ArithmeticException{
        /*
            folgende Operationen werden nur ausgeführt, wenn Term auch root-Term ist
         */
        if(isRoot) {
            isValidTerm();
            try {
                originalTerm = (Term) this.clone();
            } catch (CloneNotSupportedException e) {

            }
            rankAfterPriority();
        }
        /*
            calculate:
            +/- ganz normal
            wenn Term: calculateAlreadyOrderedTerm()
         */
        double result = 0;
        if(start instanceof Number){
            result = ((Number) start).getValue();
        }else {//start instanceof Term
            //Rekursion
            result = ((Term)start).calculate();
        }
        try {
            for (TermKnot i = start.next.next; i != null; i = i.next.next) {
                Operation operation = (Operation) i.prev;
                double num = 0;
                if (i instanceof Number) {
                    num = ((Number) i).getValue();
                } else {//start instanceof Term
                    //Rekursion
                    num = ((Term) i).calculate();
                }
                if(operation.isPow()){
                   result = Math.pow(result,num);
                } else if (operation.isMultiplication()) {
                    result *= num;
                } else if (operation.isDivision()) {
                    if(num==0) throw new ArithmeticException();
                    result /= num;
                } else if (operation.isAddition()) {
                    result += num;
                } else if (operation.isSubtraction()) {
                    result -= num;
                }
            }
        }catch(NullPointerException e){

        }
        return result;
    }

    /**
     * &uuml;berpr&uuml;ft ob Term gueltig ist. Ist das nicht der Fall wird Exception geworfen
     * @throws TermNotValidException Wird geworfen, wenn der Term kein g&uuml;ltiger mathematischer Ausdruck ist.
     */
    public void isValidTerm()throws TermNotValidException{
        int bracketsOpen=0;
        if(start instanceof Operation){
            throw new TermNotValidException("Start cannot be operation!");
        }
        else if(end instanceof Operation){
            throw new TermNotValidException("End cannot be operation!");
        }
        for(TermKnot i=start;i!=null;i=i.next){
            if(i instanceof Number && i.next instanceof Number){
                throw new TermNotValidException("Two Operators without Separator!");
            }
            else if(i instanceof Operation && i.next instanceof Operation){
                throw new TermNotValidException("Two Operators without Separator!");
            }
            else if(i instanceof Bracket){
                Bracket bracket = (Bracket)i;
                if(bracket.getBracketType()==BracketType.OPEN){
                    bracketsOpen++;
                }
                else if(bracket.getBracketType()==BracketType.CLOSE){
                    if (bracketsOpen<1)
                        throw new TermNotValidException("Number of close & open Brackets does not match!");
                    bracketsOpen--;
                }
            }
        }
    }

    /**
     * Ein String bestehend aus einem mathematischer Ausdruck wird geparst.
     * folgende Zeichen sind zul&auml;ssig:
     * <ul>
     *     <li>Zahlen:</li>
     *     <ul>
     *         <li>
     *             <p>
     *                 bestehend aus einem Vorzeichen [-], kann auch keines sein. Danach kommt eine Gleitkommazahl mit oder ohne Komma [.].
     *             </p>
     *         </li>
     *     </ul>
     *     <li>Rechenoperationen:</li>
     *     <ul>
     *         <li>hoch:            ^</li>
     *         <li>Multiplikation:  *</li>
     *         <li>Division:        /</li>
     *         <li>Addition:        +</li>
     *         <li>Subtraktion:     -</li>
     *     </ul>
     *     <li>Klammern:</li>
     *     <ul>
     *         <li>Klammer auf:     (</li>
     *         <li>Klammer zu:      )</li>
     *     </ul>
     * </ul>
     * <p>Ob der Ausdruck in Ordnung ist, wird noch nicht vollst&auml;ndig &uuml;berpr&uuml;ft.
     * Daher kann es seine, dass erst der Aufruf der Methode calculate() eine Exception wirft.</p>
     * @param mathString String, der geparst werden soll.
     * @throws ParseException Wird geworfen, wenn beim parsen ein Fehler auftritt.
     */
    public void parseMath(String mathString)throws ParseException {
        /*
            String wird der Reihe nach durchgegangen und die Operatoren werden dem Objekt hinzugefügt.
         */
        boolean wasOperationBefore=true;
        boolean wasOpenBracketBefore=false;
        for(int i=0;i<mathString.length();i++){
            char charNow=mathString.charAt(i);
            if(charNow=='('){
                this.add(new Bracket(BracketType.OPEN));
                wasOperationBefore=false;
                wasOpenBracketBefore=true;
            }else if(charNow==')') {
                this.add(new Bracket(BracketType.CLOSE));
                wasOperationBefore = false;
            }else if(charNow=='^'){
                if(wasOperationBefore)
                    throw new ParseException("Two Operations cannot be behind each other!",i);
                if(wasOpenBracketBefore)
                    throw new ParseException("Operation cannot be behind Openbracket!",i);
                this.add(new Operation(OperationType.POW));
            }else if(charNow=='*'){
                if(wasOperationBefore)
                    throw new ParseException("Two Operations cannot be behind each other!",i);
                if(wasOpenBracketBefore)
                    throw new ParseException("Operation cannot be behind Openbracket!",i);
                this.add(new Operation(OperationType.MULTIPLY));
                wasOperationBefore=true;
            }else if(charNow=='/'){
                if(wasOperationBefore)
                    throw new ParseException("Two Operations cannot be behind each other!",i);
                if(wasOpenBracketBefore)
                    throw new ParseException("Operation cannot be behind Openbracket!",i);
                this.add(new Operation(OperationType.DIVIDE));
                wasOperationBefore=true;
            }else if(charNow=='+'){
                if(wasOperationBefore)
                    throw new ParseException("Two Operations cannot be behind each other!",i);
                if(wasOpenBracketBefore)
                    throw new ParseException("Operation cannot be behind Openbracket!",i);
                this.add(new Operation(OperationType.ADD));
                wasOperationBefore=true;
                /*
                    wenn operationBefore -> minus vor number
                 */
            }else if(charNow=='-'&&!wasOperationBefore&&!wasOpenBracketBefore){
                this.add(new Operation(OperationType.SUBTRACT));
                wasOperationBefore=true;
            }else if(Character.isDigit(charNow)||charNow=='-'){
                boolean hasComma=false;
                String number=Character.toString(charNow);
                i++;
                for(;i<mathString.length();i++){
                    charNow=mathString.charAt(i);
                    if(charNow=='.'&&!hasComma)
                        hasComma=true;
                    else if(charNow=='.')
                        throw new ParseException("Number with two commas not allowed!",i);
                    else if(!Character.isDigit(charNow)) {
                        i--;
                        break;
                    }
                    number=number+charNow;
                }
                wasOperationBefore=false;
                wasOpenBracketBefore=false;
                this.add(new Number(Double.parseDouble(number)));
            }else{
                throw new ParseException("Character not allowed!",i);
            }
        }
    }
}