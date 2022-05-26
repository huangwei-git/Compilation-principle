package IdentifyUnsignedNumbers;

import LexicalAnalysis.Analysis;

import java.io.File;

public class NumberAnalysis extends Analysis {

    public void work(String readPath,boolean mask){
        File file = new File(readPath);
        String writePath = file.getParentFile().getPath() +  "/" + file.getName() + "_analysis.txt";
        loadWords(readPath,mask);
        writeFile(writePath);
    }

    @Override
    public void loadWords(String filePath) {
        SeparateNum separateNum = new SeparateNum();
        result = separateNum.work(filePath);
    }

    public void loadWords(String filePath,boolean mask) {
        SeparateNum separateNum = new SeparateNum();
        separateNum.setMask(mask);
        result = separateNum.work(filePath);
    }
}
