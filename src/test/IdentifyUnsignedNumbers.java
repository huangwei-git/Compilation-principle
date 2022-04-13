package test;

import LexicalAnalysis.Analysis;
import LexicalAnalysis.Separate;

public class IdentifyUnsignedNumbers extends Analysis {
    @Override
    public void loadWords(String filePath) {
        SeparateNum separateNum = new SeparateNum();
        result = separateNum.work(filePath);
    }
}
