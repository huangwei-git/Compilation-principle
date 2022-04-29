package LexicalAnalysis;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

public class Separate{
    public StringBuilder text;          // 读取的文本
    public int begin;                   // 当前读取到的下标位置

    public void load(String filePath){
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

    public HashMap<String,HashSet<String>> work(String filePath){
        load(filePath);
        return separateText();
    }

    public HashMap<String,HashSet<String>> separateText(){
        HashMap<String,HashSet<String>> map = new HashMap<>();
        while(!isOutOfBounds()){
            Type type =  separateWord();
            if(type != null){
                if(!map.containsKey(type.sign)){
                    map.put(type.sign,new HashSet<>(){{add(type.value);}});
                }
                else map.get(type.sign).add(type.value);
            }
        }
        return map;
    }

    public Type separateWord(){
        StringBuilder restore = new StringBuilder();
        Type res = null;
        if(isOutOfBounds()) return null;
        char c = getChar();
        while(c == '\n' || c == ' ' || c == '\t'){
            if(isOutOfBounds()) return null;
            c = getChar();
        }
        if(isLetter(c)){// 是否为字母
            // 若是字母、数字、下划线则继续读取
            while(isDigit(c) || isLetter(c) || c == '_'){
                restore.append(c);// 存储已读取且符合要求的字符
                if(isOutOfBounds()){
                    break;
                }
                c = getChar();
            }
            if(!isOutOfBounds()) retract();// 回退一个字符
            // 判断是否为保留字
            if(Sign.keyword_set.contains(restore.toString())){
                // 判断是否为include关键字
                if(restore.toString().equals("include")){
                    //跳至'>'处
                    while(getChar() != '>');
                }
                //返回类型为：保留字
                res = new Type(Sign.KEYWORD,restore.toString());
            }
            // 返回类型为：标识符
            else res = new Type(Sign.ID,restore.toString());
            // 清空缓存的字符串
            restore.setLength(0);
        }else if(isDigit(c)){
            restore.append(c);// 存储读取的字符
            while(isDigit(c = getChar())){// 读取字符至出现非数字
                restore.append(c);// 存储读取的字符
            }
            retract();// 回退
            res = new Type(Sign.INTEGER,restore.toString());
            restore.setLength(0);
        }else{
                switch (c){
                    case '!':
                        c = getChar();
                        if(c == '=')
                            res = new Type(Sign.RELATIONAL_OPERATOR,"!=");
                        else{
                            retract();
                            res = new Type(Sign.LOGICAL_OPERATOR,"!");
                        }
                        break;
                    case '<':
                        c = getChar();
                        if(c != '<'){
                            if(c == '=')
                                res = new Type(Sign.RELATIONAL_OPERATOR,"<=");
                            else {
                                retract();
                                res = new Type(Sign.RELATIONAL_OPERATOR,"<");
                            }
                        }
                        break;
                    case '>':
                        c = getChar();
                        if(c != '>'){
                            if(c == '=')
                                res = new Type(Sign.RELATIONAL_OPERATOR,">=");
                            else {
                                retract();
                                res = new Type(Sign.RELATIONAL_OPERATOR, ">");
                            }
                        }
                        break;
                    case '=':
                        c = getChar();
                        if(c == '=')
                            res = new Type(Sign.RELATIONAL_OPERATOR,"==");
                        else{
                            retract();
                            res = new Type(Sign.ASSIGN_OPERATOR,"=");
                        }
                        break;
                    case '+':
                        c = getChar();
                        if(c == '=')
                            res = new Type(Sign.ASSIGN_OPERATOR,"+=");
                        else if(c == '+')
                            res = new Type(Sign.ARITHMETIC_OPERATOR,"++");
                        else{
                            retract();
                            res = new Type(Sign.ARITHMETIC_OPERATOR,"+");
                        }
                        break;
                    case '-':
                        c = getChar();
                        if(c == '=')
                            res = new Type(Sign.ASSIGN_OPERATOR,"-=");
                        else if(c == '-')
                            res = new Type(Sign.ARITHMETIC_OPERATOR,"--");
                        else if(c == '>')
                            res = new Type(Sign.ADDRESS_OPERATOR,"->");
                        else{
                            retract();
                            res = new Type(Sign.ARITHMETIC_OPERATOR,"-");
                        }
                        break;
                    case '*':
                        c = getChar();
                        if(c == '=')
                            res = new Type(Sign.ASSIGN_OPERATOR,"*=");
                        else if(c == '*'){
                            restore.append("**");
                            while(c == '*'){
                                c = getChar();
                                if(c == '*') restore.append(c);
                            }
                            retract();
                            res = new Type(Sign.ADDRESS_OPERATOR,restore.toString());
                            restore.setLength(0);
                        }
                        else{
                            retract();
                            res = new Type(Sign.ARITHMETIC_OPERATOR,"+");
                        }
                        break;
                    case '/':
                        c = getChar();
                        if(c == '=')
                            res = new Type(Sign.ASSIGN_OPERATOR,"/=");
                        else if (c == '/') res = new Type(Sign.NOTES,"//");
                        else if(c == '*' && skip(c)){
                            res = new Type(Sign.NOTES,"/**/");
                        }else{
                            retract();
                            res = new Type(Sign.ARITHMETIC_OPERATOR,"/");
                        }
                        break;
                    case '~':
                        c = getChar();
                        if(c == '=')
                            res = new Type(Sign.ASSIGN_OPERATOR,"~=");
                        else{
                            retract();
                            res = new Type(Sign.BIT_OPERATOR,"~");
                        }
                        break;
                    case '^':
                        c = getChar();
                        if(c == '=')
                            res = new Type(Sign.ASSIGN_OPERATOR,"^=");
                        else{
                            retract();
                            res = new Type(Sign.BIT_OPERATOR,"^");
                        }
                        break;
                    case '&':
                        c = getChar();
                        if(c == '=')
                            res = new Type(Sign.ASSIGN_OPERATOR,"&=");
                        else if (c == '&'){
                            res = new Type(Sign.LOGICAL_OPERATOR,"&&");
                        }
                        else{
                            retract();
                            res = new Type(Sign.BIT_OPERATOR,"&");
                        }
                        break;
                    case '|':
                        c = getChar();
                        if(c == '=')
                            res = new Type(Sign.ASSIGN_OPERATOR,"|=");
                        else if(c == '|'){
                            res = new Type(Sign.LOGICAL_OPERATOR,"||");
                        }
                        else{
                            retract();
                            res = new Type(Sign.BIT_OPERATOR,"|");
                        }
                        break;
                    case '\'':
                        c = getChar();
                        if(c == 'n') res = new Type(Sign.DELIMITER,"\n");
                        else if(c == 't') res = new Type(Sign.DELIMITER,"\t");
                        break;
                    case '"':
                        if (skip(c)) res = new Type(Sign.DELIMITER,"" + '"');
                        break;
                    case '(':
                        res = new Type(Sign.DELIMITER,"(");
                        break;
                    case ')':
                        res = new Type(Sign.DELIMITER,")");
                        break;
                    case '[':
                        res = new Type(Sign.DELIMITER,"[");
                        break;
                    case ']':
                        res = new Type(Sign.DELIMITER,"]");
                        break;
                    case '{':
                        res = new Type(Sign.DELIMITER,"{");
                        break;
                    case '}':
                        res = new Type(Sign.DELIMITER,"}");
                        break;
                    case ',':
                        res = new Type(Sign.DELIMITER,",");
                        break;
                    case ';':
                        res = new Type(Sign.DELIMITER,";");
                        break;
            }
            restore.setLength(0);
        }
        return res;
    }

    public boolean isLetter(char c){
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    public boolean isDigit(char c){
        return (c >= '0' && c <= '9');
    }

    public char getChar(){
        return text.charAt(++begin);
    }

    public char retract(){
        return text.charAt(--begin);
    }

    public boolean isOutOfBounds(){
        if(begin == text.length() - 1) return true;
        else return false;
    }

    public boolean skip(char c){
        boolean flag = false;
        char find = c;
        if(c == find) {
            while(!isOutOfBounds()){
                if ((c = text.charAt(++begin)) == find){
                    flag = true;
                    break;
                }
            };
        }else if(c == '*'){
            while(!isOutOfBounds()){
                if((c = text.charAt(++begin)) == '*'){
                    if(!isOutOfBounds() && (c = text.charAt(++begin)) == '/'){
                        flag = true;
                        break;
                    }
                }
            }
        }
        return flag;
    }
}

