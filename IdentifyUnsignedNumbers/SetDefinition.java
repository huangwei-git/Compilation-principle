package IdentifyUnsignedNumbers;

import java.util.HashMap;
import java.util.HashSet;

public class SetDefinition {
    public HashMap<String,String> mask = new HashMap<>();

    public HashSet<String> operators = new HashSet<String>(){{
        add("+");add("-");add("*");add("/");add("%");
    }};

    public HashSet<String> exponents = new HashSet<String>(){{
        add("E");add("e");
    }};
    public HashSet<String> plusAndMinus = new HashSet<String>(){{
        add("+");add("-");
    }};
    public HashSet<String> dots = new HashSet<String>(){{
        add(".");
    }};
    public HashSet<String> digits = new HashSet<String>(){{
        add("0");add("1");add("2");add("3");add("4");add("5");add("6");add("7");add("8");add("9");
    }};

    public void setOpSymbols(HashSet<String> opSymbols,int mode) {
        if(mode == 0) this.operators = opSymbols;
        else this.operators.addAll(opSymbols);
    }

    public void setExponents(HashSet<String> exponents,int mode) {
        if(mode == 0) this.exponents = exponents;
        else this.exponents.addAll(exponents);
    }

    public void setDot(HashSet<String> dots,int mode) {
        if(mode == 0) this.dots = dots;
        else this.dots.addAll(dots);
    }

    public void setDigits(HashSet<String> digits,int mode) {
        if(mode == 0) this.digits = digits;
        else this.digits.addAll(digits);
    }

    public void setPlusAndMinus(HashSet<String> plusAndMinus,int mode) {
        if(mode == 0) this.plusAndMinus = plusAndMinus;
        else this.plusAndMinus.addAll(plusAndMinus);
    }

    public void setMask(HashMap<String, String> mask) {
        this.mask.putAll(mask);
    }
}
