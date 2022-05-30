package LL1;

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
    }
}
