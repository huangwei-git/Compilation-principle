package IdentifyUnsignedNumbers;

import LexicalAnalysis.Separate;
import LexicalAnalysis.Type;

import java.util.HashMap;
import java.util.HashSet;

public class SeparateNum extends Separate {
    private SetDefinition setDefinition = new SetDefinition();

    @Override
    public HashMap<String, HashSet<String>> work(String filePath) {
        load(filePath);
        return separateText();
    }

    @Override
    public HashMap<String, HashSet<String>> separateText() {
        HashMap<String,HashSet<String>> map = new HashMap<>();
        while(!isOutOfBounds()){
            Type type =  separateWord();
            if(type != null){
                maskOff(type);
                if(!map.containsKey(type.sign)){
                    System.out.println(type.sign + " " + type.value);
                    map.put(type.sign,new HashSet<>(){{add(type.value);}});
                }
                else{
                    System.out.println(type.sign + " " + type.value);
                    map.get(type.sign).add(type.value);
                }
            }
        }
        return map;
    }

    @Override
    public Type separateWord() {
        StringBuilder store = new StringBuilder();
        Type res = null;
        if(isOutOfBounds()) return null;
        char c = getChar();
        while(c == '\n' || c == ' ' || c == '\t'){
            store.append(c);
            if(isOutOfBounds()) return null;
            c = getChar();
        }

        /*第一个字符非数字：*/
        if(!isDigit(c)){
            readNotOpSymbol(c,store);
            return new Type("其他",store.toString());
        }

        /*第一个字符是数字:*/
        else{
            c = readNumber(c,store);
            //数字之后是运算符
            if(c == '\0') return new Type("数字", store.toString());
            if(setDefinition.operators.contains("" + c)){
                retract();
                return new Type("数字",store.toString());
            }
            // 小数点
            else if(isDot(c)){
                store.append(c);
                if(isOutOfBounds()) return new Type("异常",store.toString());
                c = getChar();
                // 小数点之后有数字
                if(isDigit(c)){
                    c = readNumber(c,store);
                    if(c == '\0') return new Type("数字",store.toString());
                    else if(isExponents(c)){
                        store.append(c);
                        if(isOutOfBounds()) return new Type("异常",store.toString());
                        c = getChar();
                        if(isAddOrSubtract(c)){
                            store.append(c);
                            if(isOutOfBounds()) return new Type("异常",store.toString());
                            c = getChar();
                            if(!isDigit(c)){//E+非数字
                                readNotOpSymbol(c,store);
                                return new Type("异常",store.toString());
                            }
                            else {// E+数字
                                c = readNumber(c,store);
                                if(c == '\0') return new Type("数字",store.toString());
                                if(setDefinition.operators.contains("" + c)){
                                    retract();
                                    return new Type("数字",store.toString());
                                }
                                else{
                                    readNotOpSymbol(c,store);
                                    return new Type("异常",store.toString());
                                }
                            }
                        }
                        else{
                            readNotOpSymbol(c,store);
                            return new Type("异常",store.toString());
                        }
                    }
                    else if(setDefinition.operators.contains("" + c)){
                        retract();
                        return new Type("数字",store.toString());
                    }
                    else{
                        readNotOpSymbol(c,store);
                        return new Type("异常",store.toString());
                    }
                }
                // 小数点之后非数字
                else {
                    readNotOpSymbol(c,store);
                    return new Type("异常",store.toString());
                }
            }
            //数字之后是‘E’或‘e’
            else if(isExponents(c)){
                store.append(c);
                if(isOutOfBounds()) return new Type("异常",store.toString());
                c = getChar();
                if(isAddOrSubtract(c)){
                    store.append(c);
                    if(isOutOfBounds()) return new Type("异常",store.toString());
                    c = getChar();
                    if(!isDigit(c)){//E+非数字
                        readNotOpSymbol(c,store);
                        return new Type("异常",store.toString());
                    }
                    else {// E+数字
                        c = readNumber(c,store);
                        if(c == '\0') return new Type("数字",store.toString());
                        if(setDefinition.operators.contains("" + c)){
                            retract();
                            return new Type("数字",store.toString());
                        }
                        else{
                            readNotOpSymbol(c,store);
                            return new Type("异常",store.toString());
                        }
                    }
                }
                else{
                    readNotOpSymbol(c,store);
                    return new Type("异常",store.toString());
                }
            }
        }
        return res;
    }

    // 循环读取数字，返回第一个遇到的非数字；若返回为'\0'，则文本内容读完
    public char readNumber(char c,StringBuilder store){
        while(isDigit(c)){
            store.append(c);
            if(isOutOfBounds()) return '\0';
            c = getChar();
        }
        return c;
    }

    // 循环读取非运算符
    public void readNotOpSymbol(char c, StringBuilder store){
        while(!isDigit(c)){
            store.append(c);
            if(isOutOfBounds()) return ;
            c = getChar();
        }
        retract();
        return ;
    }

    @Override
    public boolean isDigit(char c) {
        if(setDefinition.digits.contains("" + c)) return true;
        else return false;
    }

    public boolean isOperator(char c){
        if(setDefinition.operators.contains("" + c)) return true;
        else return false;
    }

    public boolean isExponents(char c){
        if(setDefinition.exponents.contains("" + c)) return true;
        else return false;
    }

    public boolean isDot(char c){
        if(setDefinition.dots.contains("" + c)) return true;
        else return false;
    }

    public boolean isAddOrSubtract(char c){
        if(setDefinition.plusAndMinus.contains("" + c)) return true;
        else return false;
    }

    public SetDefinition getSetDefinition(){
        return this.setDefinition;
    }

    public void maskOff(Type type){
        StringBuilder newVlue = new StringBuilder();
        for(int i = 0;i < type.value.length();i++){
            String tmp = type.value.substring(i,i + 1);
            if(setDefinition.mask.containsKey(tmp)) newVlue.append(setDefinition.mask.get(tmp));
            else newVlue.append(tmp);
        }
        type.value = newVlue.toString();
    }
}