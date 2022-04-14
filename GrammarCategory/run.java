package GrammarCategory;

public class run {
    public static void main(String[] args) {
        Grammar grammar = new Grammar();
        try {
            grammar.loadProductions("./src/GrammarCategory/resources/a.txt");
            System.out.println(grammar.getGrammarType());
        } catch (ProductionException e) {
            e.printStackTrace();
        }
    }
}
