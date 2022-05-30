package LL1;

<<<<<<< HEAD
import Grammar.Grammar;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class Run {
    public static void main(String[] args) {
//        FIRSTandFOLLOW first = new FIRSTandFOLLOW(new Grammar(new File("./src/LL1/resources/text_G")));
//        System.out.println(first);
        GrammarTree grammarTree = new GrammarTree(new Grammar(new File("./src/LL1/resources/test")),
                "./src/LL1/resources/grammarTree.md");
=======
import java.io.File;

public class Run {
    public static void main(String[] args) {
//        Grammar grammar = new Grammar(new File("./src/LL1/resources/text_G"));
        FIRSTandFOLLOW first = new FIRSTandFOLLOW(new File("./src/LL1/resources/text_G"));
        first.print();
>>>>>>> a5f9ef84c40b601659c6e3393520fce9e0b30b5c
    }
}
