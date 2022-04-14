package Thompson;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class NFA {
    public HashSet<String> VN = new HashSet();
    public HashSet<String> VT = new HashSet<>();
    public String start;
    public String end;
    public HashMap<Pair<String,String>,String> tranfer = new HashMap<>();

    public void addVN(String vn){
        VN.add(vn);
    }

    public void addVT(String vt){
        VT.add(vt);
    }

    public void setExtremePoint(String start,String end){
        this.start = start;
        this.end = end;
    }

    public void addTransfer(String start,String c,String end){
        tranfer.put(new Pair<String,String>(start,c),end);
    }

}