package Grammar;

<<<<<<< HEAD
=======
import ThompsonBuildNFA.Pair;
import com.sun.source.util.Trees;

>>>>>>> a5f9ef84c40b601659c6e3393520fce9e0b30b5c
import java.io.*;
import java.util.*;

public class Grammar {
    private String start;
    private Set<String> VT;
    private Set<String> VN;
<<<<<<< HEAD
    private Map<String,Set<String>> production;
=======
    private Map<String,Set<List<String>>> production;
>>>>>>> a5f9ef84c40b601659c6e3393520fce9e0b30b5c

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
<<<<<<< HEAD
        for(String s:VT){
            if(!production.containsKey(s)) production.put(s,new HashSet<>());
        }
    }

    public void addProduction(String left,String right) {
        VN.add(left);
        for(char c : right.toCharArray()){
            if(c < 'A' || c > 'Z') VT.add("" + c);
            else VN.add("" + c);
        }
        if(production.containsKey(left)) production.get(left).add(right);
        else production.put(left,new HashSet<>(){{add(right);}});
=======
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
>>>>>>> a5f9ef84c40b601659c6e3393520fce9e0b30b5c
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

<<<<<<< HEAD
    public Map<String, Set<String>> getProduction() {
=======
    public Map<String, Set<List<String>>> getProduction() {
>>>>>>> a5f9ef84c40b601659c6e3393520fce9e0b30b5c
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
