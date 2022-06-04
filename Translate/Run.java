package Translate;

import java.io.IOException;

public class Run{
    public static void main(String[] args) throws IOException {
        Translate.getTranslate("src/Translate/resources/expression.txt", "src/Translate/resources/output.txt");
    }
}