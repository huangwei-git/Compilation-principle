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
    // 解析表达式的堆栈
    Stack<String> parseStack = null;
    Stack<String> expressionStack = null;
    Stack<String> productionStack = null;
    String parseFile = "src/LL1/ExpressionForecastParser/resources/parse.md";
    int count;//步骤计数

    public ExpressionForecastParser(File propertiesFile,File expressionFile) {
        String parentPath = propertiesFile.getParentFile().getPath();
        // 初实例化对象
        table = new HashMap<>();
        properties = new Properties();
        loadExpression(expressionFile);

        // entry用于获取配置文件中的键值对集合
        Set entry = null;
        File file = new File(parentPath + "\\grammar");
        BufferedWriter bw = null;
        try {
            properties.load(new FileReader(propertiesFile));
            entry = properties.entrySet();
            // 将配置文件中的文法写出到文件grammar中，用于实例化Grammar对象
            bw = new BufferedWriter(new FileWriter(file));
            bw.write("G[E]:\n");
            // 将产生式写入文件中
            for(Object production : properties.keySet()){
                if(production instanceof String){
                    bw.write((String)production + "\n");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            // 关闭缓冲输出流
            if(bw != null){
                try {
                    bw.flush();
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // 实例化文法对象
        grammar = new Grammar(file);
        // 根据配置文件加载预测分析表
        generatePredictTable();

        // 将预测分析表用markdown输出
        generateMarkdown(propertiesFile.getParentFile().getPath() + "\\table.md");

    }


    public void parse() throws IOException {
        count = 0;
        BufferedWriter bw = new BufferedWriter(new FileWriter(parseFile));
        bw.write("|步骤|分析栈|余留输入串|所用产生式|\n");
        bw.write("|:---:|:---|:---|:---:|\n");
        // 初始化操作栈、分析栈、所用产生式栈
        initStack();
        while(expressionStack.size() > 0 && parseStack.size() > 0){
            printStack(bw);
            String exp = expressionStack.peek();// 输入出栈顶元素
            String par = parseStack.peek();// 分析串栈顶元素
            if(parseStack.size() == 1){
                if(expressionStack.size() == 1) System.out.println("分析成功");
                else System.out.println("分析失败");
                break;
            }
            String next = null;
            try {
                next = table.get(parseStack.pop()).get(exp);
                if(next == null || next.equals(ERROR)){
                    System.out.println("分析失败");
                    break;
                }
                next = next.substring(next.indexOf("->") + 2);
            }catch (NullPointerException e){}
            if(next == null) next = par;
            // M[A,a] = yXXX
            String head = next.substring(0,1);
            if(grammar.getVT().contains(head)){
                // epsilon 会被略过
                // M[E,i] = yXXXX
                // if y=i
                if(exp.equals(head)){
                    expressionStack.pop();//表达式栈pop
                    for(int i = next.length() - 1;i >= 1 ;i--){
                        parseStack.add(next.substring(i,i + 1));
                    }
                }
                productionStack.add(par + "→" + next);
            }else if (grammar.getVN().contains(head)){
                for(int i = next.length() - 1;i >= 0 ;i--) parseStack.add(next.substring(i,i + 1));
                    productionStack.add(par + "→" + next);
            }
        }
        bw.flush();
        bw.close();
    }

    public void initStack(){
        // 初始化分析栈
        parseStack = new Stack();
        parseStack.add("#");
        parseStack.add(grammar.getStart());

        // 初始化输入串栈
        expressionStack = new Stack();
        expressionStack.push("#");
        for(int i = expression.length() - 1;i >= 0;i--) expressionStack.push(expression.substring(i,i + 1));

        // 初始化分析栈
        productionStack = new Stack();
    }

    // 装载待解析表达式
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

    // 生成预测分析表
    public void generatePredictTable(){
        Map<String,String> tmpMap = new HashMap<>();
        // 初始化预测分析表，创建行表头和列表头
        for(String T : grammar.getVT()){
            if(T.equals("ε")) T = "#";
            tmpMap.put(T,ERROR);
        }
        for(String N : grammar.getVN()){
            table.put(N,new HashMap<>(tmpMap));
        }

        // 根据配置文件的内容，装载预测分析表
        for(Object o : properties.entrySet()){
            if(o instanceof Entry){
                Entry entry = (Entry)o;
                String key = (String) entry.getKey();
                String value = (String) entry.getValue();
                // 遍历value的每一个字符，令 table[key(0), c] = key
                for(char c : value.toCharArray()){
                    table.get(key.substring(0,1)).put("" + c,key);
                }
            }
        }
    }

    // 打印栈内信息
    public void printStack(BufferedWriter bw) throws IOException {
        StringBuilder msg = new StringBuilder();
        msg.append("分析栈:" + parseStack + "\n");
        msg.append("余留输入串:" + expressionStack + "\n");
        msg.append("所用产生式:" + productionStack + "\n");
//        if(productionStack.size() > 0) msg.append("所用产生式:" + productionStack.peek() + "\n");
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
            out.append("|　**" + entry.getKey() + "**　|");
            for(String s : entry.getValue().keySet()){
                String production = entry.getValue().get(s);
                if(!production.equals(ERROR)){
                    out.append("<font style='color:red'>");
                    out.append(production.substring(production.indexOf("->") + 2));
                    out.append("</font>|");
                }else
                    out.append("　　|");

            }
            out.append("\n");
        }

        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(path));
            bw.write(out.toString().replaceAll("->","→"));
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            // 关闭缓冲字节流
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
