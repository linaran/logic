package com.example.deni.connect.aStar;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.Log;

import com.example.deni.connect.Connect;
import com.example.deni.connect.Line;
import com.example.deni.globalUtility.Constants;
import com.example.deni.logicComponentView.BasicComponentView;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Set;

/**
 *
 */
public class AStar {

    private float mOffset;

    private PointF mStart;
    private PointF mEnd;

    private PriorityQueue<Node> mOpen;
    private ArrayList<Node> mClose;
    private Rect mHitRect;

    private PointF mCircle;
    private int mTurns;


    public AStar(Context context, PointF circle){
        mHitRect = new Rect();
        mOffset = Constants.SCHEME_OFFSET.getDPValue(context);
        mCircle = circle;
        mTurns = 0;
    }

    /**
     * Method takes any given position and calculates manhattan distance to
     * the end of the whole AStar algorithm.
     * @param position any PointF.
     * @return floating manhattan distance.
     */
    private float manhattan(PointF position){
        return (Math.abs(mEnd.x - position.x) + Math.abs(mEnd.y - position.y));
    }

    /**
     * Method checks if the end for the new line segment enters
     * a logic component. If it does method returns true.
     * @param newPosition end for the new line segment.
     * @return true if the end for the new line segment is inside logic component.
     */
    private Boolean isInsideComponent(PointF newPosition){
        for(BasicComponentView componentView : Connect.getInstance().getComponentViews()){
            mHitRect.set(
                    (int) (componentView.getComponent().getPosition().x - mOffset + 1),
                    (int) (componentView.getComponent().getPosition().y - mOffset + 1),
                    (int) (componentView.getComponent().getPosition().x + componentView.getComponent().getWidth() + mOffset - 1),
                    (int) (componentView.getComponent().getPosition().y + componentView.getComponent().getHeight() + mOffset - 1)
            );
            if (mHitRect.contains((int)newPosition.x, (int)newPosition.y)){ return true; }
        }
        return false;
    }

    private String checkDirection(PointF oldP, PointF newP){
        if(newP.x - oldP.x > 0){
            return "xR";
        }
        if(newP.x - oldP.x < 0){
            return "xL";
        }
        if(newP.y - oldP.y > 0){
            return "yD";
        }
        if(newP.y - oldP.y < 0){
            return "yU";
        }
        throw new UnsupportedOperationException("Impossible situation");
    }

    /**
     * Method calculates the cost for the path to make a new line segment.
     * If the path stays true to its direction the cost will be single scheme offset.
     * If the path decides to turn the direction by 90 degrees the cost will be doubled.
     * If the path turns around by 180 degrees that will be punished. This shouldn't happen
     * at all. Will be subsequently removed.
     * @param currentNode current node, used to determine the direction of the path.
     * @param newPosition position of the new node, used to determine how the path will change direction.
     * @return cost which is one, two or four scheme offsets.
     */
    private float calculateCost(Node currentNode, PointF newPosition){
        Node parentNode = currentNode.getParent();
        if (parentNode == null){
            return mOffset;
        }

        String oldDirection = checkDirection(parentNode.getPosition(), currentNode.getPosition());
        String newDirection = checkDirection(currentNode.getPosition(), newPosition);

        if(oldDirection.charAt(0) == newDirection.charAt(0)){
            if(oldDirection.charAt(1) != newDirection.charAt(1)) { return 5*mOffset; }
            else { return mOffset; }
        } else {
            return mOffset;
        }
    }

    /**
     * Method for checking if two line segments overlap in 2D environment where
     * lines are only vertical and horizontal.
     * @param a edge of line segment 1.
     * @param b edge of line segment 1.
     * @param c edge of line segment 2.
     * @param d edge of line segment 2.
     * @return true if two line segments overlap.
     */
    private boolean overlap(PointF a, PointF b, PointF c, PointF d){
//        Are they on the same line.
        boolean onSameLine = a.x == c.x && a.x == d.x && b.x == c.x ||
                a.y == c.y && a.y == d.y && b.y == c.y;
        if (!onSameLine) { return false; }

//        Do they actually overlap.
        if (a.x == c.x){
            float A = Math.min(a.y, b.y);
            float B = Math.max(a.y, b.y);
            float C = Math.min(c.y, d.y);
            float D = Math.max(c.y, d.y);
            if (B < C || A > D) { return false; }
        } else {
            float A = Math.min(a.x, b.x);
            float B = Math.max(a.x, b.x);
            float C = Math.min(c.x, d.x);
            float D = Math.max(c.x, d.x);
            if (B < C || A > D) { return false; }
        }

        return true;
    }

    /**
     * Method checks if a new line segment overlaps with any existing lines
     * in the logic scheme.
     * @param newStart start of the new line segment.
     * @param newEnd end of the new line segment.
     * @return true if the new line segment overlaps somewhere.
     */
    private boolean newLineSegmentOverlap(PointF newStart, PointF newEnd){
        Set<Line> lines = Connect.getInstance().getLines();
        for (Line line : lines){
            for (int i = line.getLinePath().size() - 1; i > 0; i--){
                if (overlap(newStart, newEnd, line.getLinePath().get(i), line.getLinePath().get(i - 1))){
                    Log.d("HASCIRCLE", newStart.toString() + " " + newEnd.toString() + "\n" + line.getLinePath().get(i) + " " + line.getLinePath().get(i-1));
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Method was made as a helper for line overlapping algorithm.
     * Method takes a point, push values and pushes the point with
     * the push value divided by coef.
     * @param point point to be pushed.
     * @param push values doing the pushing.
     * @param coef coef to soften up push values.
     *             Exists because push values are used somewhere else as well
     *             and should not be lowered.
     * @return new PointF that was pushed.
     */
    private PointF pushABit(PointF point, PointF push, float coef){
        return new PointF(
                point.x + push.x / coef,
                point.y + push.y / coef
        );
    }

    private void nextPosition(Node currentNode, PointF offset, ArrayList<Node> expandList){
        PointF newPosition = new PointF(
                currentNode.getPosition().x + offset.x,
                currentNode.getPosition().y + offset.y
        );

        Node newNode;

//        Checking if next node is inside a logic component
        PointF mmStart = new PointF(mStart.x + mOffset, mStart.y);
        PointF mmEnd = new PointF(mEnd.x - mOffset, mEnd.y);
        boolean moreCondition = !newPosition.equals(mmStart) && !newPosition.equals(mmEnd);
        boolean condition =
                moreCondition
                && !newPosition.equals(mStart)
                && !newPosition.equals(mEnd)
                && isInsideComponent(newPosition);
        if (condition) { return; }
//        Checking if next node is inside a logic component

//        No overlapping lines and circle setter.
        if (currentNode.isInsideMainLine()) {
            boolean isNewSegmentInside = newLineSegmentOverlap(
                    pushABit(currentNode.getPosition(), offset, 10f),
                    pushABit(currentNode.getPosition(), offset, 5f)
            );
            if (isNewSegmentInside) {
                newNode = new Node(
                        newPosition,
                        null,
                        true,
                        currentNode.getCost() + calculateCost(currentNode, newPosition),
                        manhattan(newPosition),
                        currentNode
                );
                expandList.add(newNode);
            } else {
                if (!newLineSegmentOverlap(pushABit(currentNode.getPosition(), offset, 10f), newPosition)) {
//                    Log.d("CIRCLED", "Ok -- " + currentNode.getPosition().toString());
                    newNode = new Node(
                            newPosition,
                            new PointF(currentNode.getPosition().x, currentNode.getPosition().y),
                            false,
                            currentNode.getCost() + calculateCost(currentNode, newPosition),
                            manhattan(newPosition),
                            currentNode
                    );
                    expandList.add(newNode);
                }
            }
        } else {
            if (!newLineSegmentOverlap(currentNode.getPosition(), newPosition)){
                newNode = new Node(
                        newPosition,
                        null,
                        false,
                        currentNode.getCost() + calculateCost(currentNode, newPosition),
                        manhattan(newPosition),
                        currentNode
                );
                expandList.add(newNode);
            }
        }
//        No overlapping lines and circle setter.
    }

    private ArrayList<Node> expand(Node currentNode){
        ArrayList<Node> retValue = new ArrayList<>();

        PointF direction = new PointF();
        float offset = mOffset;
        if(manhattan(currentNode.getPosition()) > 18 * mOffset){
            offset = 3 * mOffset;
        }

        direction.set(0, offset); // Down
        nextPosition(currentNode, direction, retValue);
        direction.set(0, -offset); // Up
        nextPosition(currentNode, direction, retValue);

        direction.set(offset, 0); // Right
        nextPosition(currentNode, direction, retValue);
        direction.set(-offset, 0); // Left
        nextPosition(currentNode, direction, retValue);

        return retValue;
    }

    private Node findOldDuplicate(Node currentNode){
        for (Node node : mOpen){
            if (node.getPosition().equals(currentNode.getPosition())){
                return node;
            }
        }
        for (Node node : mClose){
            if (node.getPosition().equals(currentNode.getPosition())){
                return node;
            }
        }
        return null;
    }

    private ArrayList<PointF> getPathFromSolution(Node finalNode){
        ArrayList<PointF> list = new ArrayList<>();
        Node node = finalNode;
        mCircle.set(Constants.CIRCLE_IGNORE.getValue(), 0);
        while(true){
            list.add(node.getPosition());
            if (node.getCircle() != null){
                mCircle.set(node.getCircle().x, node.getCircle().y);
//                Log.d("CIRCLED", "Circle was set.");
            }
            if (node.getParent() == null){
                break;
            }
            node = node.getParent();
        }
        return list;
    }

    private void wipe(){
        mOpen = null;
        mClose = null;
    }

    private void init(PointF start, PointF end, boolean isInsideLine){
        mStart = start;
        mEnd = end;
        mOpen = new PriorityQueue<>(10, new NodeComparator());
        mOpen.add(new Node(
                start,
                null,
                isInsideLine,
                0,
                manhattan(start),
                null
        ));
        mClose = new ArrayList<>();
    }

    /**
     * Algorithm finds best path from one point to another. One point should always
     * be the coordinate position of the output while other point should always be the
     * position of the input gate. If the output input contract is broken algorithm won't
     * work as expected.
     * @param start coordinate position of output.
     * @param end coordinate position of input.
     * @return ArrayList of PointF.
     */
    public ArrayList<PointF> aStarSearch(PointF start, PointF end){
        int haltProblem = 0;
        Node currentNode;

//        Modified
        boolean isInsideMainLine = newLineSegmentOverlap(start, new PointF(
                start.x + mOffset / 10,
                start.y
        )) || newLineSegmentOverlap(start, new PointF(
                start.x,
                start.y + mOffset / 10
        )) || newLineSegmentOverlap(start, new PointF(
                start.x,
                start.y - mOffset / 10
        ));
        init(start, end, isInsideMainLine);
        if (!isInsideMainLine) { mCircle.set(Constants.CIRCLE_IGNORE.getValue(), 0); }
//        Log.d("HASCIRCLE", isInsideMainLine + "");

        while(mOpen.size() != 0){
            if (haltProblem == 1000){
                Log.d("HALT", "HALT");
                throw new UnsupportedOperationException("HALT PROBLEM.");
            }
            haltProblem++;

            currentNode = mOpen.poll();
            if (currentNode.getPosition().equals(mEnd)){
//                Avoiding memory leak.
                wipe();
//                Log.d("HALT", haltProblem + "");
                return getPathFromSolution(currentNode);
            }
            mClose.add(currentNode);
            for (Node node : expand(currentNode)){
                Node oldDuplicate = findOldDuplicate(node);
                if (oldDuplicate != null) {
                    float fullCostOld = oldDuplicate.getCost() + oldDuplicate.getHeuristic();
                    float fullCostNew = node.getCost() + node.getHeuristic();
                    if (fullCostOld <= fullCostNew){ continue; }
                    else{
                        mOpen.remove(oldDuplicate);
                        mClose.remove(oldDuplicate);
                    }
                }
                mOpen.add(node);
            }
        }
        Log.d("HALT", "Exception");
        Log.d("HALT", mStart.toString() + "\n" + "Start");
        Log.d("HALT", mEnd.toString() + "\n" + "End");
        Log.d("HALT", mClose.toString() + "\n" + "Closed");
        throw new UnsupportedOperationException("AStar algorithm failed to find a path.");
    }
}
