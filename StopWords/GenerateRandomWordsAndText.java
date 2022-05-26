package StopWords;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

public class GenerateRandomWordsAndText {
    private int wordsSize;                  // 生成随机字符串的数量
    private int stopWordsSize;              // 生成随机停用词的数量
    private final int u = 5;                // 期望
    private final int v = 1;                // 标准差
    private int maxLength = 0;              // 随机生成的单词中，最大长度
    private Random random = new Random();
    private final char[] charSet = {        // 字符集合
            'a','b','c','d','e','f','g', 'h','i','j','k','l','m',
            'n', 'o','p','q','r','s','t','u', 'v','w','x','y','z',
            'A','B','C','D','E','F','G','H','I','J','K','L','M',
            'N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
            '\n','\t',' '};

    public GenerateRandomWordsAndText(int stopWordsSize, double ratio){
        this.stopWordsSize = stopWordsSize;
        this.wordsSize = (int) (stopWordsSize * 1.0 / ratio);
    }

    // 生成指定数量的停用词存入stopWords集合中，生成待过滤文本存入text中
    public int GenerateStopWordsAndText(Collection<String> stopWords, StringBuilder text){
        HashSet<String> words = new HashSet<>();
        while(words.size() < wordsSize){
            words.add(GenrateWord());
        }
        Iterator<String> iterator = words.iterator();
        while(stopWordsSize-- > 0){
            stopWords.add(iterator.next());
        }

        // 在words集组合成文本内容合中，随机选择字符串，；
        Object[] wordArray = words.toArray();
        Random random = new Random();
        for(int i = 0;i < (wordsSize << 3);i++){
            int index = random.nextInt(wordsSize);
            text.append(wordArray[index]);
        }

        return maxLength;
    }

    // 生成一个随机字符串
    public String GenrateWord(){
        int wordlength = getGaussian();// 随机字符串的长度
        maxLength = Math.max(maxLength,wordlength);
        StringBuilder word = new StringBuilder();
        while(wordlength-- > 0){
            int index = random.nextInt(charSet.length);// 在字符集中随机选取一个字符
            word.append(charSet[index]);
        }
        return word.toString();
    }

    // 生成服从高斯分布X~N(u,v^2)的随机数
    public int getGaussian(){
        int res = -1;
        while(res <= 0)
            res = (int)(v * random.nextGaussian() + u);
        return res;
    }
}
