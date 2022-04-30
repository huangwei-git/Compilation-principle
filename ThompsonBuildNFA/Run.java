import ThompsonBuildNFA.Pair;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class Run{
    public static void main(String[] args) {
        Pair p1 = new Pair("AM","a");
        Pair p2 = new Pair("AM","a");
        System.out.println(p1.equals(p2));
        String s1 = "123455";
        String s2 = "123455";
        System.out.println(p1.hashCode());
        System.out.println(p2.hashCode());
    }
}