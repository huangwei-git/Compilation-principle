package ThompsonBuildNFA;

import java.util.*;

public class InputIdentification {
    public static Set epsilon(String s, NFA nfa, Set<String> set){
        HashSet<String> res = new HashSet<>();
        set.add(s);
        Pair pair = new Pair(s,"ε");
        if(nfa.getTransfer().containsKey(pair)){
            Set<String> toes = nfa.getTransfer().get(pair);
            for(String toe : toes){
                if(!res.contains(toe)){
                    res.add(toe);
                    res.addAll(epsilon(toe,nfa,set));
                }
            }
        }
        return res;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        HashSet<String> set = new HashSet<>();
        Thompson thompson = new Thompson("b(acd)*");
        String input = "bacdacdacdac";
        NFA nfa = thompson.getNfa();
        epsilon(nfa.getHead(),nfa,set);
        for(char c : input.toCharArray()){
            if(set.isEmpty()) break;
            Iterator<String> iter = set.iterator();
            HashSet<String> newSet = new HashSet<>(set);
            HashSet<String> result = new HashSet<>();
            while(iter.hasNext()){
                String start = iter.next();
                newSet.remove(start);
                Pair pair = new Pair(start,"" + c);
                if(nfa.getTransfer().containsKey(pair)){
                    for(String toes : nfa.getTransfer().get(pair)) {
                        result.add(toes);
                        result.addAll(epsilon(toes, nfa, newSet));
                    }
                }
            }
            set = result;
            int t = 1;
        }
        if(set.contains(nfa.getToe())) System.out.println("YES");
        else System.out.println("NO");
        in.close();
    }
}
