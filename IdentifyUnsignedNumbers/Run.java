package IdentifyUnsignedNumbers;

import java.math.BigDecimal;

public class Run{

    public static void main(String[] args) {
        NumberAnalysis iun = new NumberAnalysis();
        iun.work("./src/IdentifyUnsignedNumbers/resource/a.txt",true);
    }
}

