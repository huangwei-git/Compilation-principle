package LexicalAnalysis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class Analysis {
    // 各个词性的集合
    public HashMap<String, HashSet<String>> result;


    public void work(String readPath){
        File file = new File(readPath);
        String writePath = file.getParentFile().getPath() +  "/" + file.getName() + "_analysis.txt";
        loadWords(readPath);
        writeFile(writePath);
    }

    public void work(String readPath,String writePath){
        loadWords(readPath);
        writeFile(writePath);
    }

    public void loadWords(String filePath){
        Separate separate = new Separate();
        result = separate.work(filePath);
    }

    public void writeFile(String filePath){
        clearFile(filePath);
        BufferedWriter bw = null;
        Iterator<String> iteratorKey = result.keySet().iterator();
        StringBuilder type = new StringBuilder();
        try {
            //创建缓存输出流
            bw = new BufferedWriter(new FileWriter(filePath,true));
            while(iteratorKey.hasNext()){// 迭代类型
                type.append(iteratorKey.next());
                HashSet<String> set = result.get(type.toString());
                Iterator<String> iteratorValue = set.iterator();
                while(iteratorValue.hasNext()){
                    String word = iteratorValue.next();
                    //写入文件
                    bw.write(type.toString() + " := " + word + "\n");
                }
                type.setLength(0);
            }
            bw.flush();// 刷新缓冲管道
            bw.close();// 关闭缓冲流
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clearFile(String filePath){
        try {
            new BufferedWriter(new FileWriter(filePath)).write("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}