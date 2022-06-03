package LL1.ExpressionForecastParser;

import Grammar.Grammar;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

public class ExpressionForecastParser {
    Properties properties;
    String ERROR = "ERROR";
    Grammar grammar;
    Map<String,Map<String,String>> table;
    String expression = "";
    // �������ʽ�Ķ�ջ
    Stack<String> parseStack = null;
    Stack<String> expressionStack = null;
    Stack<String> productionStack = null;
    String parseFile = "src/LL1/ExpressionForecastParser/resources/parse.md";
    int count;//�������

    public ExpressionForecastParser(File propertiesFile,File expressionFile) {
        String parentPath = propertiesFile.getParentFile().getPath();
        // ��ʵ��������
        table = new HashMap<>();
        properties = new Properties();
        loadExpression(expressionFile);

        // entry���ڻ�ȡ�����ļ��еļ�ֵ�Լ���
        Set entry = null;
        File file = new File(parentPath + "\\grammar");
        BufferedWriter bw = null;
        try {
            properties.load(new FileReader(propertiesFile));
            entry = properties.entrySet();
            // �������ļ��е��ķ�д�����ļ�grammar�У�����ʵ����Grammar����
            bw = new BufferedWriter(new FileWriter(file));
            bw.write("G[E]:\n");
            // ������ʽд���ļ���
            for(Object production : properties.keySet()){
                if(production instanceof String){
                    bw.write((String)production + "\n");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            // �رջ��������
            if(bw != null){
                try {
                    bw.flush();
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // ʵ�����ķ�����
        grammar = new Grammar(file);
        // ���������ļ�����Ԥ�������
        generatePredictTable();

        // ��Ԥ���������markdown���
        generateMarkdown(propertiesFile.getParentFile().getPath() + "\\table.md");

    }


    public void parse() throws IOException {
        count = 0;
        BufferedWriter bw = new BufferedWriter(new FileWriter(parseFile));
        bw.write("|����|����ջ|�������봮|���ò���ʽ|\n");
        bw.write("|:---:|:---|:---|:---:|\n");
        // ��ʼ������ջ������ջ�����ò���ʽջ
        initStack();
        while(expressionStack.size() > 0 && parseStack.size() > 0){
            printStack(bw);
            String exp = expressionStack.peek();// �����ջ��Ԫ��
            String par = parseStack.peek();// ������ջ��Ԫ��
            if(parseStack.size() == 1){
                if(expressionStack.size() == 1) System.out.println("�����ɹ�");
                else System.out.println("����ʧ��");
                break;
            }
            String next = null;
            try {
                next = table.get(parseStack.pop()).get(exp);
                if(next == null || next.equals(ERROR)){
                    System.out.println("����ʧ��");
                    break;
                }
                next = next.substring(next.indexOf("->") + 2);
            }catch (NullPointerException e){}
            if(next == null) next = par;
            // M[A,a] = yXXX
            String head = next.substring(0,1);
            if(grammar.getVT().contains(head)){
                // epsilon �ᱻ�Թ�
                // M[E,i] = yXXXX
                // if y=i
                if(exp.equals(head)){
                    expressionStack.pop();//���ʽջpop
                    for(int i = next.length() - 1;i >= 1 ;i--){
                        parseStack.add(next.substring(i,i + 1));
                    }
                }
                productionStack.add(par + "��" + next);
            }else if (grammar.getVN().contains(head)){
                for(int i = next.length() - 1;i >= 0 ;i--) parseStack.add(next.substring(i,i + 1));
                    productionStack.add(par + "��" + next);
            }
        }
        bw.flush();
        bw.close();
    }

    public void initStack(){
        // ��ʼ������ջ
        parseStack = new Stack();
        parseStack.add("#");
        parseStack.add(grammar.getStart());

        // ��ʼ�����봮ջ
        expressionStack = new Stack();
        expressionStack.push("#");
        for(int i = expression.length() - 1;i >= 0;i--) expressionStack.push(expression.substring(i,i + 1));

        // ��ʼ������ջ
        productionStack = new Stack();
    }

    // װ�ش��������ʽ
    public void loadExpression(File filePath){
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filePath));
            String buf = null;
            while((buf = br.readLine()) != null){
                expression += buf;
            }
            expression = expression.replaceAll("(\\r\\n|\\n|\\\\n|\\s)", "");
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(br != null){
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // ����Ԥ�������
    public void generatePredictTable(){
        Map<String,String> tmpMap = new HashMap<>();
        // ��ʼ��Ԥ������������б�ͷ���б�ͷ
        for(String T : grammar.getVT()){
            if(T.equals("��")) T = "#";
            tmpMap.put(T,ERROR);
        }
        for(String N : grammar.getVN()){
            table.put(N,new HashMap<>(tmpMap));
        }

        // ���������ļ������ݣ�װ��Ԥ�������
        for(Object o : properties.entrySet()){
            if(o instanceof Entry){
                Entry entry = (Entry)o;
                String key = (String) entry.getKey();
                String value = (String) entry.getValue();
                // ����value��ÿһ���ַ����� table[key(0), c] = key
                for(char c : value.toCharArray()){
                    table.get(key.substring(0,1)).put("" + c,key);
                }
            }
        }
    }

    // ��ӡջ����Ϣ
    public void printStack(BufferedWriter bw) throws IOException {
        StringBuilder msg = new StringBuilder();
        msg.append("����ջ:" + parseStack + "\n");
        msg.append("�������봮:" + expressionStack + "\n");
        msg.append("���ò���ʽ:" + productionStack + "\n");
//        if(productionStack.size() > 0) msg.append("���ò���ʽ:" + productionStack.peek() + "\n");
        msg.append("--------------------");
        System.out.println(msg.toString());

        /*====*/
        StringBuilder out = new StringBuilder();
        out.append("|" + (count++) + "|");
        for(int i = 0;i < parseStack.size();i++){
            out.append(parseStack.elementAt(i));
        }
        out.append("|");
        for(int i = 0;i < expressionStack.size();i++){
            out.append(expressionStack.elementAt(i));
        }
        out.append("|");
        if(productionStack.size() > 0) out.append(productionStack.peek() + "|\n");
        else out.append("|\n");
        bw.write(out.toString());
    }

    private void generateMarkdown(String path){
        StringBuilder out = new StringBuilder();
        String seperate = "|:---:|";
        out.append("| |");
        grammar.getStart();
        Map map = table.get(grammar.getStart());
        table.get(grammar.getStart()).keySet();
        for(String row : table.get(grammar.getStart()).keySet()){
            out.append(row + "|");
            seperate += ":---:|";
        }
        out.append("\n" + seperate + "\n");
        for(Map.Entry<String,Map<String,String>> entry : table.entrySet()){
            out.append("|��**" + entry.getKey() + "**��|");
            for(String s : entry.getValue().keySet()){
                String production = entry.getValue().get(s);
                if(!production.equals(ERROR)){
                    out.append("<font style='color:red'>");
                    out.append(production.substring(production.indexOf("->") + 2));
                    out.append("</font>|");
                }else
                    out.append("����|");

            }
            out.append("\n");
        }

        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(path));
            bw.write(out.toString().replaceAll("->","��"));
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            // �رջ����ֽ���
            if (bw != null){
                try {
                    bw.flush();
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        ExpressionForecastParser efp = null;
        efp = new ExpressionForecastParser(new File("src/LL1/ExpressionForecastParser/resources/table.properties"),
                new File("src/LL1/ExpressionForecastParser/resources/expression"));
        System.out.println(efp.grammar);
        efp.parse();
    }
}
