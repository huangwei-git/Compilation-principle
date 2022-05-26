package StopWords;

/*
 BKDRHash算法：
    将字符串变成K进制的数值，
* */

public class StringHash {
    int K = 131; // K进制数
    long[] HashCodes;// 字符串前缀hash值 HashCodes[i] = [0,i - 1]子串的哈希码
    long[] k;    // k[i] = k^i

    // 获得某个字符串的哈希码
    public long getHashCode(String s){
        long code = 0;
        for(int i = 0;i < s.length();i++){
            code = code * K + s.charAt(i);
        }
        return code;
    }

    // 对文本进行字符串hash
    public void textHashCode(String s){
        k = new long[s.length() + 1];
        HashCodes = new long[s.length() + 1];
        k[0] = 1;
        for(int i = 0;i < s.length();i++){
            k[i + 1] = k[i] * K;
            HashCodes[i + 1] = HashCodes[i] * K + s.charAt(i);
        }
    }

    // 查询区间[l,r]子串的哈希值
    public long query(int l,int r){
        return HashCodes[r] - HashCodes[l - 1] * k[r - l + 1];
    }
}
