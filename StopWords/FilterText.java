package StopWords;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class FilterText {
    private int stopWordsSize; // 停用词数量
    private double ratio; // 停用词占比
    private int maxLength;// 停用词的最大长度
    private HashSet<String> stopWords;// 停用词
    private StringBuilder text;// 待过滤文本
    private boolean generateShieldedList = false; // 是否生成被屏蔽词列表
    private boolean generateTextBeforeFilter = false; // 是否生成被屏蔽词列表
    private String stopWordsFilePath;// 停用词文件写入路径
    private String textBeforeFilterPath;// 过滤前文本文件写入路径
    private String shieldedWordsFilePath;// 被遮蔽词列表文件写入路径
    private String textAfterFilterPath;// 过滤后文本文件写入路径

    // 无参构造
    public FilterText() {
        String rootPath = "./src/" + this.getClass().getPackageName() + "/resources/";
        String[] paths = {
                rootPath + "stopWords.txt",
                rootPath + "textBeforeFilter.txt",
                rootPath + "shieldedWords.txt",
                rootPath + "textAfterFilter.txt"
        };
        // 创建路径所需目录
        for(String path : paths) CreateFile(new File(path).getParentFile());
        stopWordsFilePath = paths[0];
        textBeforeFilterPath = paths[1];
        shieldedWordsFilePath = paths[2];
        textAfterFilterPath = paths[3];

        stopWords = new HashSet<>();
        text = new StringBuilder();
        this.ratio = 0.05;
        this.stopWordsSize = 50;
    }

    // 生成停用词与待过滤文本，并将过滤结果写入默认路径文件中
    public void work(){
        loadStopWordsAndText();
        filterText();
    }

    // 设置文件路径
    public void setFilePath(String stopWordsFilePath,
                     String textBeforeFilterPath,
                    String shieldedWordsFilePath,
                     String textAfterFilterPath){
        if(stopWordsFilePath != null) this.stopWordsFilePath = stopWordsFilePath;
        if(textBeforeFilterPath != null) this.textBeforeFilterPath = textBeforeFilterPath;
        if(shieldedWordsFilePath != null) this.shieldedWordsFilePath = shieldedWordsFilePath;
        if(textAfterFilterPath != null) this.textAfterFilterPath = textAfterFilterPath;
    }

    // 载入随机停用词与待处理文本
    public void loadStopWordsAndText(){
        //生成预设数量的停用词，并返回词语的最大长度
        maxLength = new GenerateRandomWordsAndText(stopWordsSize,ratio).GenerateStopWordsAndText(stopWords,text);
        writeToFile(stopWords,stopWordsFilePath);
        if(generateTextBeforeFilter) writeToFile(text,textBeforeFilterPath);
    }

    // 过滤文本
    public void filterText(){// 过滤文本
        StringHash stringHash = new StringHash();
        // 存储过滤词的字符串哈希值
        HashSet<Long> stopWordCode = new HashSet<>();
        // 对停用词字符串哈希
        for(String s : stopWords) stopWordCode.add(stringHash.getHashCode(s));
        // 对文本字符串哈希
        stringHash.textHashCode(text.toString());
        // 用于屏蔽停用词的串
        StringBuilder shieldWord = new StringBuilder();
        for(int i = 0;i < maxLength;i++) shieldWord.append("*");
        // 用于存储文本内容中，被屏蔽的词
        List<String> ShieldedList = new LinkedList<>();
        // 对文本进行过滤
        for(int i = 1;i <= text.length();i++){
            // j从后往前扫
            int j = Integer.min(i + maxLength - 1 , text.length());
            for(;j >= i;j--){
                // 如果是被停用词，则将其屏蔽，同时记录下被屏蔽的词
                // 退出循环，下次循环时i = j
                if(stopWordCode.contains(stringHash.query(i,j))){
                    ShieldedList.add(text.substring(i - 1,j));
                    text.replace(i-1,j,shieldWord.substring(0,j - i + 1));
                    i = j;
                    break;
                }
            }
        }
        // 将文本中被屏蔽的词的列表写入文件
        if(generateShieldedList) writeToFile(ShieldedList,shieldedWordsFilePath);
        // 将过滤后的文本的写入文件
        writeToFile(text,textAfterFilterPath);
    }

    // 是否生成被遮蔽词列表文件
    public void generateShieldedListFile(boolean generate){
        this.generateShieldedList = generate;
    }

    // 是否生成过滤前文本文件
    public void generateTextBeforeFilterFile(boolean generate){
        this.generateTextBeforeFilter = generate;
    }

    public void setStopWordsSize(int stopWordsSize){this.stopWordsSize = stopWordsSize;};

    public void setRatio(int ratio){this.ratio = ratio;}

    // 将Collection对象的内容写入路径对应文件
    public void writeToFile(Collection<String> set, String filePath){
        clearFile(filePath);
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(filePath,true));
            Iterator<String> iterator = set.iterator();
            int cnt = 1;
            while(iterator.hasNext()){
                bw.write(cnt + " := (" + iterator.next() + ")\n===============\n");
                cnt++;
            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // 将StringBuilder的内容写入路径对应文件
    public void writeToFile(StringBuilder str,String filePath){
        clearFile(filePath);
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(filePath,true));
            bw.write(str.toString());
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 清空路径所在的文件内容
    public void clearFile(String filePath){
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
            bw.write("");
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 创建路径中缺少的目录和文件
    public void CreateFile(File file){
        if(file.exists()) return ;
        File pf = file.getParentFile();
        if(!pf.exists()){
            CreateFile(pf);
        }
        file.mkdir();
    }
}
