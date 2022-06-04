package Translate;

import java.io.*;

public class Translate {
    private static int count = 1;
    private static BufferedWriter bw = null;
    private static String[] expressions;
    private static String[] lefts;
    private static Node[] roots;

    public static void getTranslate(String readPath,String writePath) throws IOException {
        // ����һ�������ַ�����������ȡ�洢��ֵ�����ļ�����
        BufferedReader br = new BufferedReader(new FileReader(readPath));
        bw = new BufferedWriter(new FileWriter(writePath));
        // read���ڴ洢����������
        StringBuilder read = new StringBuilder();
        // buf����ÿ�ζ�ȡ��������
        String buf;
        while ((buf = br.readLine()) != null) {
            read.append(buf);
        }
        // ����ȡ�����ַ���ȥ��������
        read.replace(0,read.length(),read.toString().replaceAll("(\\r\\n|\\n|\\\\n|\\s)", ""));
        // ��";"�ָ��ȡ�����ַ���
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
        // �������и�ֵ���ʽ
        for(int i = 0;i < roots.length;i++){
            // ������Ӧ��������
            roots[i] = InversePolish.getTree(expressions[i]);
            // ����getTranslate()�����������������������������Ԫ��д���ļ�
            String res = getTraslate(roots[i]);
            System.out.println(lefts[i] + " = " + res);
            bw.write("(" + lefts[0] + ", " + res + ", " + "null, =)\n");
        }
        // ˢ������ܵ�
        bw.flush();
        bw.close();
    }

    private static  String getTraslate(Node root) throws IOException {
        String res = "";
        if(root != null){
            // �ݹ�������
            String s1 = getTraslate(root.getRightChild());
            // �ݹ�������
            String s2 = getTraslate(root.getLeftChild());
            if(s1.length() > 0){
                res = "t" + count;
                // ���������+1
                count++;
                System.out.println(res + " = " + s2 + " " + root.getValue() + " " + s1);
                // ������Ԫ�飬д�뻺���ֽ��������
                bw.write("(" + res + " ,"+s2 + " ," + s1 + " ," + root.getValue() + ")\n");
            }else res = root.getValue();

        }
        return res;
    }
}
