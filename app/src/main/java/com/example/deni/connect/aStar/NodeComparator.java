package com.example.deni.connect.aStar;

import java.util.Comparator;

/**
 * Simple comparator for a priority queue in AStar algorithm.
 */
public class NodeComparator implements Comparator<Node> {

    @Override
    public int compare(Node lhs, Node rhs) {
        float lNumber = lhs.getCost() + lhs.getHeuristic();
        float rNumber = rhs.getCost() + rhs.getHeuristic();
        if (lNumber < rNumber){
            return -1;
        } else if (lNumber > rNumber){
            return 1;
        }
        return 0;
    }

}
