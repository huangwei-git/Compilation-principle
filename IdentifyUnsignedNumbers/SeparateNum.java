package IdentifyUnsignedNumbers;

import LexicalAnalysis.Separate;
import LexicalAnalysis.Type;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;

public class SeparateNum extends Separate {
    private SetDefinition setDefinition = new SetDefinition();
    private boolean isMask = false;
    private HashMap<String,String> maskOff = new HashMap<>(){{
        put("A","0");put("B","1");put("C","2");put("D","3");put("E","4");put("F","5");
        put("G","6");put("H","7");put("I","8");put("J","9");put("K",".");put("L","E");

    }};

    @Override
    public HashMap<String, HashSet<String>> work(String filePath) {
        load(filePath);
        return separateText();
    }

    // 载入路径文件的内容
    @Override
    public void load(String filePath) {
        try {
            // 初始化成员变量
            text = new StringBuilder();
            begin = -1;
            FileReader fr = new FileReader(filePath);//创建字符输入流对象
            BufferedReader br = new BufferedReader(fr);// 创建缓冲流
            StringBuilder buffer = new StringBuilder();// 存储读取的每行内容
            // 读取内容至其为空
            while(!(buffer.append(br.readLine())).toString().equals("null")){
                if(buffer.length() > 0){// 空行不读入
                    if(isMask)
                        for(int i = 0; i < buffer.length();i++){
                            String sub = buffer.substring(i,i + 1);
                            if(maskOff.containsKey(sub))
                                buffer.replace(i,i + 1,maskOff.get(sub));
                        }
                    text.append(buffer);// 存储读取到的内容
                    buffer = new StringBuilder();// 清空缓存内容
                }
            }
            fr.close();// 关闭字符输入流
            br.close();// 关闭缓冲流
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMask(boolean mask){this.isMask = mask;}

    // 存储seperateWord的返回结果
    @Override
    public HashMap<String, HashSet<String>> separateText() {
        HashMap<String,HashSet<String>> map = new HashMap<>();
        while(!isOutOfBounds()){
            Type type =  separateWord();
            if(type != null){
                String value = type.value;
                if(type.sign.equals("数字")){
                    int index = value.indexOf("E") + value.indexOf("e");
                    if(index != -2){
                        index++;
                        String F = value.substring(0,index);
                        index++;
                        String E = "" + Math.pow(10,Integer.parseInt(value.substring(index)));
                        value = new BigDecimal(F).multiply(new BigDecimal(E)).toString();
                    }
                }else if(isMask){
                    continue;
                }
                if(!map.containsKey(type.sign)){
                    System.out.println(type.sign + ":" + type.value);
                    String finalValue = value;
                    map.put(type.sign,new HashSet<>(){{add(finalValue);}});
                }
                else{
                    System.out.println(type.sign + " " + type.value);
                    map.get(type.sign).add(value);
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

}