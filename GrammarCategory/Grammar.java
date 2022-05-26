package GrammarCategory;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Grammar {

    private HashMap<String, Collection<String>> productions = new HashMap<>();
    private int grammarType = -1;

    public void loadProductions(String filePath) throws ProductionException {
        Production production = new Production();
        grammarType = production.work(filePath,productions);
    }

    public int getGrammarType(){
        return grammarType;
    }
}