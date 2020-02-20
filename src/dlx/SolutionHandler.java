package dlx;

import dlx.DancingLinks.*;
import java.util.*;

public interface SolutionHandler{
    void handleSolution(List<DancingNode> solution, List<List<Integer>> res);
}



class MyHandler implements SolutionHandler{
    public void handleSolution(List<DancingNode> answer, List<List<Integer>> res){
        List<Integer> solution = new ArrayList<>();
        for (DancingNode n: answer) {
            solution.add(n.rowId);
        }
        res.add(new ArrayList<>(solution));
    }
}