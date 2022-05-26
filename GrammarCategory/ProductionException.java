package GrammarCategory;

public class ProductionException extends Exception{
    private int type;

    public ProductionException(int type){this.type = type;}

    @Override
    public String toString() {
        if(type == -2) return "The left side of a production cannot have only ε.";
        else return "Production can only produce empty sentences.";
    }
}
