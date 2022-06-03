package LL1.SyntaxTree;

import Grammar.Grammar;
import LL1.SimpleExpressionParser.ExpressionParser;

import java.io.File;

public class Run {
    public static void main(String[] args) {
        GrammarTree grammarTree = new GrammarTree(new Grammar(new File("src/LL1/SyntaxTree/resources/test")),
                "./src/LL1/SyntaxTree/resources/grammarTree.md");
    }
}
