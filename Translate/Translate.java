package Translate;

import java.io.*;

public class Translate {
    private static int count = 1;
    private static BufferedWriter bw = null;
    private static String[] expressions;
    private static String[] lefts;
    private static Node[] roots;

    public static void getTranslate(String readPath,String writePath) throws IOException {
        // 创建一个缓冲字符输入流，读取存储赋值语句的文件内容
        BufferedReader br = new BufferedReader(new FileReader(readPath));
        bw = new BufferedWriter(new FileWriter(writePath));
        // read用于存储读出的内容
        StringBuilder read = new StringBuilder();
        // buf缓冲每次读取的行内容
        String buf;
        while ((buf = br.readLine()) != null) {
            read.append(buf);
        }
        // 将读取到的字符串去除白噪音
        read.replace(0,read.length(),read.toString().replaceAll("(\\r\\n|\\n|\\\\n|\\s)", ""));
        // 以";"分割读取到的字符串
        expressions = read.toString().split(";");

        int len = expressions.length;
        roots = new Node[len];
        lefts = new String[len];
        for(int i = 0;i < len;i++){
            lefts[i] = expressions[i].substring(0,1);
            expressions[i] = expressions[i].substring(2);
        }

        work();
    }

    private static void work() throws IOException {
        // 遍历所有赋值表达式
        for(int i = 0;i < roots.length;i++){
            // 构建对应的运算树
            roots[i] = InversePolish.getTree(expressions[i]);
            // 调用getTranslate()处理运算树，输出翻译结果，并把四元组写入文件
            String res = getTraslate(roots[i]);
            System.out.println(lefts[i] + " = " + res);
            bw.write("(" + lefts[0] + ", " + res + ", " + "null, =)\n");
        }
        // 刷新输出管道
        bw.flush();
        bw.close();
    }

    private static  String getTraslate(Node root) throws IOException {
        String res = "";
        if(root != null){
            // 递归右子树
            String s1 = getTraslate(root.getRightChild());
            // 递归左子树
            String s2 = getTraslate(root.getLeftChild());
            if(s1.length() > 0){
                res = "t" + count;
                // 步骤计数器+1
                count++;
                System.out.println(res + " = " + s2 + " " + root.getValue() + " " + s1);
                // 构造四元组，写入缓冲字节输出流中
                bw.write("(" + res + " ,"+s2 + " ," + s1 + " ," + root.getValue() + ")\n");
            }else res = root.getValue();

        }
        return res;
    }
}
