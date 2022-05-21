package LL1;

import java.io.File;

public class Run {
    public static void main(String[] args) {
//        Grammar grammar = new Grammar(new File("./src/LL1/resources/text_G"));
        FIRSTandFOLLOW first = new FIRSTandFOLLOW(new File("./src/LL1/resources/text_G"));
        first.print();
    }
}
