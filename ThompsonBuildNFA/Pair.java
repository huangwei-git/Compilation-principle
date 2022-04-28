package ThompsonBuildNFA;

public class Pair implements Comparable<Pair>{
    private String start;
    private String input;

    public Pair() {
    }

    public Pair(String start, String input) {
        this.start = start;
        this.input = input;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getStart() {
        return start;
    }

    public String getInput() {
        return input;
    }

    @Override
    public boolean equals(Object obj) {
        boolean res = false;
        if(obj instanceof Pair){
            Pair pair = (Pair)obj;
            if(start == pair.start && input.equals(pair.input)) return true;
        }
        return res;
    }

    @Override
    public int compareTo(Pair o) {
        int compare = getStart().compareTo(o.getStart());
        if(compare == 0){
            compare = getInput().compareTo(o.getInput());
        }
        return compare;
    }
}
