package StopWords;

public class Run {
    public static void main(String[] args) {
        FilterText filterText = new FilterText();
        filterText.generateShieldedListFile(true);
        filterText.generateTextBeforeFilterFile(true);
        filterText.work();
    }
}
