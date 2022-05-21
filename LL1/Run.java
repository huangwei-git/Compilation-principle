package LL1;

import Grammar.Grammar;

import java.io.File;

public class Run {
    public static void main(String[] args) {
//        Grammar grammar = new Grammar(new File("./src/LL1/resources/text_G"));
        FIRST first = new FIRST(new File("./src/LL1/resources/text_G"));
        first.print();
    }
}
