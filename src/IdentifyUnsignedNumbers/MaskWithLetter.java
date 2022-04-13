package IdentifyUnsignedNumbers;

import java.util.HashMap;
import java.util.HashSet;

public class MaskWithLetter {
    HashMap<String,String> mask = new HashMap<>();

    public static void main(String[] args) {
        SeparateNum separateNum = new SeparateNum();
        MaskWithLetter maskWithLetter = new MaskWithLetter();
        separateNum.setDigits(maskWithLetter.MaskDigit("ABCDEFGHIJ"),1);
        separateNum.setDot(maskWithLetter.maskDot("K"),0);
        separateNum.setExponents(maskWithLetter.maskExponents("L"),0);
        separateNum.setPlusAndMinus(maskWithLetter.maskPlusAndMinus("MN"),1 );
        separateNum.setMask(maskWithLetter.mask);
        separateNum.work("./src/IdentifyUnsignedNumbers/resource/a.txt");
    }

    //0 to 9
    public HashSet MaskDigit(String mask){
        HashSet<String> digits = new HashSet<>();
        for(int i = 0;i < mask.length();i++){
            String s = mask.substring(i,i+1);
            digits.add(s);
            this.mask.put(s,"" + i);
        }
        return digits;
    }

    //* / %
    public HashSet maskOperator(String mask){
        HashSet<String> opSymbols = new HashSet<>();
        String tmp = "*/%";
        for(int i = 0;i < mask.length();i++){
            String s = mask.substring(i,i+1);
            opSymbols.add(s);
            this.mask.put(s,tmp.substring(i,i+1));
        }
        return opSymbols;
    }

    public HashSet maskExponents(String mask){
        HashSet<String> exponents = new HashSet<String>();
        String exp = "Ee";
        for(int i = 0;i < mask.length();i++){
            String s = mask.substring(i,i+1);
            exponents.add(s);
            this.mask.put(s,exp.substring(i,i+1));
        }
        return exponents;
    }

    public HashSet maskPlusAndMinus(String mask){
        HashSet<String> plusAndMinus = new HashSet<String>();
        String exp = "+-";
        for(int i = 0;i < mask.length();i++){
            String s = mask.substring(i,i+1);
            plusAndMinus.add(s);
            this.mask.put(s,exp.substring(i,i+1));
        }
        return plusAndMinus;
    }

    public HashSet maskDot(String mask){
        HashSet<String> dots = new HashSet<>();
        for(int i = 0;i < mask.length();i++){
            String s = mask.substring(i,i+1);
            dots.add(s);
            this.mask.put(s,".");
        }
        return dots;
    }
}
