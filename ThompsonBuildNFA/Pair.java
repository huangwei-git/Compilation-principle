package ThompsonBuildNFA;

public class Pair implements Comparable<Pair>{
    private String head;
    private String input;

    public Pair(String start, String input) {
        this.head = start;
        this.input = input;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getHead() {
        return head;
    }

    public String getInput() {
        return input;
    }

    @Override
    public int compareTo(Pair o) {
        int compare = getHead().compareTo(o.getHead());
        if(compare == 0){
            compare = getInput().compareTo(o.getInput());
        }
        return compare;
    }
}
