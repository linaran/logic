package com.example.deni.connect.aStar;

import android.graphics.PointF;

/**
 *
 */
public class Node {

    private PointF mPosition;
    private PointF mCircle;
    private boolean mIsInsideMainLine;
    private float mCost;
    private float mHeuristic;
    private Node mParent;

    public Node(PointF position, PointF circle, boolean isInsideMainLine, float cost, float heuristic, Node parent){
        mPosition = position;
        mCircle = circle;
        mIsInsideMainLine = isInsideMainLine;
        mCost = cost;
        mHeuristic = heuristic;
        mParent = parent;
    }

    public PointF getPosition() {
        return mPosition;
    }

    public void setPosition(PointF position) {
        mPosition = position;
    }

    public PointF getCircle() {
        return mCircle;
    }

    public void setCircle(PointF circle) {
        mCircle = circle;
    }

    public boolean isInsideMainLine() {
        return mIsInsideMainLine;
    }

    public void setIsInsideLine(boolean isInsideLine) {
        mIsInsideMainLine = isInsideLine;
    }

    public float getCost() {
        return mCost;
    }

    public void setCost(float cost) {
        mCost = cost;
    }

    public float getHeuristic() {
        return mHeuristic;
    }

    public void setHeuristic(float heuristic) {
        mHeuristic = heuristic;
    }

    public Node getParent() {
        return mParent;
    }

    public void setParent(Node parent) {
        mParent = parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        if (Float.compare(node.mCost, mCost) != 0) return false;
        if (Float.compare(node.mHeuristic, mHeuristic) != 0) return false;
        if (!mPosition.equals(node.mPosition)) return false;
        return !(mParent != null ? !mParent.equals(node.mParent) : node.mParent != null);
    }

    @Override
    public int hashCode() {
        int result = mPosition.hashCode();
        result = 31 * result + (mCost != +0.0f ? Float.floatToIntBits(mCost) : 0);
        result = 31 * result + (mHeuristic != +0.0f ? Float.floatToIntBits(mHeuristic) : 0);
        result = 31 * result + (mParent != null ? mParent.hashCode() : 0);
        return result;
    }

    public String toString(){
        return mPosition.toString() + " " + mCost + " " + mHeuristic;
    }
}
