package Grammar;

import ThompsonBuildNFA.Pair;
import com.sun.source.util.Trees;

import java.io.*;
import java.util.*;

public class Grammar {
    private String start;
    private Set<String> VT;
    private Set<String> VN;
    private Map<String,Set<List<String>>> production;

    public Grammar(){
        start = null;
        VT = new HashSet<>();
        VN = new HashSet<>();
        production = new HashMap<>();
    }

    public Grammar(File file){
        start = null;
        VT = new HashSet<>();
        VN = new HashSet<>();
        production = new HashMap<>();
        loadGrammar(file);
    }

    public void loadGrammar(File file){
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            StringBuilder str = new StringBuilder();
            while(!(str.append(br.readLine())).toString().equals("null")){
                if(start == null){
                    int pos = str.indexOf("(") + 1;
                    start = str.substring(pos,pos + 1);
                }else{
                    int pos = str.indexOf("->");
                    String left = str.substring(0,pos).trim();
                    String[] tok = str.substring(pos +2).split("\\|");
                    for(String right : tok){
                        addProduction(left,right.trim());
                    }
                }
                str.delete(0,str.length());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            // 关闭BufferedReader
            if(br != null){
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void addProduction(String left,String right) {
        StringBuilder str = new StringBuilder(left + right);
        List<String> list = new LinkedList<>();
        char[] chars = str.toString().toCharArray();
        for(int i = 0;i < chars.length;){
            char c = chars[i];
            int j = getNext(chars,i);
            String s = str.substring(i,j);
            if(i >= left.length()) list.add(s);
            if(c >= 'A' && c <= 'Z') VN.add(s);
            else VT.add(s);
            i = j;
        }
        if(production.containsKey(left)){
            production.get(left).add(list);
        }else{
            production.put(left,new HashSet<>(){{add(list);}});
        }
    }

    public int getNext(char[] chars,int index){
        int len = chars.length;
        int j = index;
        while(++j < len)
            if(chars[j] != '`') break;
        return j;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public Set<String> getVT() {
        return VT;
    }

    public void setVT(Set<String> VT) {
        this.VT = VT;
    }

    public Set<String> getVN() {
        return VN;
    }

    public void setVN(Set<String> VN) {
        this.VN = VN;
    }

    public Map<String, Set<List<String>>> getProduction() {
        return production;
    }


    @Override
    public String toString() {
        return "Grammar{" +
                "start='" + start + '\'' +
                ", VT=" + VT +
                ", VN=" + VN +
                ", production=" + production +
                '}';
    }
}
