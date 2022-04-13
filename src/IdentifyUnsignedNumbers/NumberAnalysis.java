package IdentifyUnsignedNumbers;

import LexicalAnalysis.Analysis;

public class NumberAnalysis extends Analysis {
    @Override
    public void loadWords(String filePath) {
        SeparateNum separateNum = new SeparateNum();
        result = separateNum.work(filePath);
    }
}
