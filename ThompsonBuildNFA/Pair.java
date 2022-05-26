package ThompsonBuildNFA;

public class Pair implements Comparable<Pair>{
    private String state;
    private String input;

    public Pair(String start, String input) {
        this.state = start;
        this.input = input;
    }

    @Override
    public int hashCode() {
        String s = state +  "-" + input;
        return s.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        boolean res = false;
        if(obj instanceof Pair){
            Pair pair = (Pair)obj;
            if(state.equals(((Pair) obj).getState()) && input.equals(((Pair) obj).getInput())){
                res = true;
            }
        }
        return res;
    }

    @Override
    public int compareTo(Pair o) {
        int compare = getState().compareTo(o.getState());
        if(compare == 0){
            compare = getInput().compareTo(o.getInput());
        }
        return compare;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getState() {
        return state;
    }

    public String getInput() {
        return input;
    }
}
