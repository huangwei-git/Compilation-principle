import java.util.HashSet;
import java.util.Iterator;

public class Run{
    public static void main(String[] args) {
        HashSet<Integer> set = new HashSet<>(){{
            add(1);
            add(2);
            add(3);
            add(4);
        }};
        Iterator<Integer> iter = set.iterator();
        int tmp = 5;
        for(Object i : set.toArray()){
            System.out.println(i);
            set.add(tmp++);
        }
    }
}