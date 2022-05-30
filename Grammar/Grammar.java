package Grammar;

import java.io.*;
import java.util.*;

public class Grammar {
    private String start;
    private Set<String> VT;
    private Set<String> VN;
    private Map<String,Set<String>> production;

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

    public Map<String, Set<String>> getProduction() {
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
