import MathParser.Term;

public class Test {
    public static void main(String[] args) {
        try {
            Term t = new Term("10^(-2)");
            System.out.println(t.calculate());
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}