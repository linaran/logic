package com.example.deni.logicViewScheme;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.deni.connect.Connect;
import com.example.deni.connect.Line;
import com.example.deni.globalUtility.Constants;
import com.example.deni.globalUtility.DeviceDimensionsHelper;
import com.example.deni.globalUtility.GraphPoint;
import com.example.deni.logic.R;
import com.example.deni.logic.SchemeFragment;
import com.example.deni.logicComponentDrawable.DrawableScheme;
import com.example.deni.logicComponentModel.single.BasicComponentModel;
import com.example.deni.logicComponentView.BasicComponentView;
import com.example.deni.logicComponentView.logicViewComponentsFactory.BasicComponent;
import com.example.deni.logicComponentView.logicViewComponentsFactory.BasicComponentFactory;

import java.util.ArrayList;

/**
 * SchemeView is a special layout manager for managing {@link BasicComponentView}.
 * It represents the logic scheme itself. It's responsible for drawing anything and
 * everything that a user should eventually see on screen when making a logic scheme.
 * SchemeView is also responsible for scaling and translating the logic scheme.
 * @see ViewGroup
 */
public class SchemeView extends ViewGroup{
    /**OnLayout optimization*/
    GraphPoint xy;

//    /**IsUserScrolling*/
//    private boolean mIsScrolling;
//    private ViewConfiguration mVC;
//    private int mTouchSlop;

    /**Scaling data*/
    private ScaleGestureDetector mScaleGestureDetector;
    private float mScaleFactor;

    /**Panning data*/
    private PointF mTranslate;

    /**GestureDetector*/
    private GraphPoint mScalePivot;
    private GestureDetector mGestureDetector;

    /**Scheme drawable data*/
    private Drawable mSchemeDrawable;

    /**Scheme buttons*/
    private LinearLayout mEditButtons;

    public SchemeView(Context context) {
        super(context);
        init(null, 0);
    }

    public SchemeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public SchemeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
//        mIsScrolling = false;
//        mVC = ViewConfiguration.get(getContext());
//        mTouchSlop = mVC.getScaledTouchSlop();

        xy = new GraphPoint();

        mScaleFactor = 1.f;
        mScaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
        mScalePivot = new GraphPoint(
                DeviceDimensionsHelper.getDisplayWidth(getContext())/2,
                DeviceDimensionsHelper.getDisplayHeight(getContext())/2
        );

        mGestureDetector = new GestureDetector(getContext(), new MyGestureListener());

        mTranslate = new PointF();

        mSchemeDrawable = new DrawableScheme(getContext());

        setWillNotDraw(false);
        setDrawingCacheEnabled(true);
    }

    @Override
    protected void onFinishInflate() {
        mEditButtons = (LinearLayout) findViewById(R.id.edit_component_layout);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        if (count == 0){
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

        for(int i = 0; i < count; i++){
            final View child = getChildAt(i);

            if (child.getVisibility() != GONE) {
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
            }

            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        if (count == 0){ return; }

        for(int i = 0; i < count; i++){
            View checkChild = getChildAt(i);

            if (checkChild.getTag().equals("Edit")){
                final int offset = Constants.SCHEME_OFFSET.getDPValue(getContext());
                final int elementWidth = checkChild.getMeasuredWidth();
                final int elementHeight = checkChild.getMeasuredHeight();
                final int positionX =
                                DeviceDimensionsHelper.getDisplayWidth(getContext()) -
                                elementWidth -
                                2*offset;
                final int positionY =
                                DeviceDimensionsHelper.getDisplayHeight(getContext()) -
                                elementHeight -
                                2*offset;
                if (checkChild.getVisibility() != GONE){
                    checkChild.layout(
                            positionX,
                            positionY,
                            positionX + elementWidth,
                            positionY + elementHeight
                    );
                }
            } else {
                final BasicComponentView child = (BasicComponentView) checkChild;
//                Log.d("TEST", child.getWidth() +" "+ child.getComponent().getWidth());
                xy.set(
                        child.getComponent().getPosition().x,
                        child.getComponent().getPosition().y
                );
                xy.
                        translate(-(mScalePivot.x - mTranslate.x), -(mScalePivot.y - mTranslate.y)).
                        scale(mScaleFactor, mScaleFactor).
                        translate(mScalePivot.x - mTranslate.x, mScalePivot.y - mTranslate.y).
                        translate(mTranslate.x, mTranslate.y);
                if (child.getVisibility() != GONE) {
                    child.layout(
                            (int) (xy.x),
                            (int) (xy.y),
                            Math.round(xy.x + child.getMeasuredWidth() * mScaleFactor),
                            Math.round(xy.y + child.getMeasuredHeight() * mScaleFactor)
                    );
                }
            }
        }
    }

//    private float calculateDistance(MotionEvent ev){
//        float distance = 0;
//        float startX = ev.getHistoricalX(0);
//        float startY = ev.getHistoricalY(0);
//        for (int h = 0; h < ev.getHistorySize(); h++){
//            float hx = ev.getHistoricalX(h);
//            float hy = ev.getHistoricalY(h);
//
//            float dx = hx - startX;
//            float dy = hy - startY;
//
//            distance += Math.abs(dx*dx + dy*dy);
//        }
//
//        return distance;
//    }
//
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        final int action = MotionEventCompat.getActionMasked(ev);
//
//        if(action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP){
//            mIsScrolling = false;
//            return false;
//        }
//
//        switch (action){
//            case MotionEvent.ACTION_MOVE:
//                if (mIsScrolling){ return true; }
//                final float diff = calculateDistance(ev);
//                if ( diff > mTouchSlop){
//                    mIsScrolling = true;
//                    return true;
//                }
//                break;
//        }
//
//        return false;
//    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean retValue = mScaleGestureDetector.onTouchEvent(event);
        retValue = mGestureDetector.onTouchEvent(event) || retValue;
        if(mEditButtons.getVisibility() != GONE){
            mEditButtons.setVisibility(GONE);
        }
        if(retValue){
            requestLayout();
            invalidate();
        }
        return retValue;
    }

    private void drawLines(Canvas canvas){
        for(Line line : Connect.getInstance().getLines()){
            line.drawLine(canvas);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();

        canvas.scale(
                mScaleFactor,
                mScaleFactor,
                mScalePivot.x,
                mScalePivot.y
        );
        canvas.translate(mTranslate.x, mTranslate.y);

        mSchemeDrawable.draw(canvas);
        drawLines(canvas);

        canvas.restore();
    }

    /**
     * This is a method that should be used when removing an {@link BasicComponentView}
     * from the {@link SchemeView}. Method makes sure the component is removed from the {@link SchemeView}
     * and some other data structures in {@link Connect}.
     * @param view any {@link BasicComponentView}.
     * @see Connect
     */
    public void completeRemoveView(BasicComponentView view){
        ArrayList<BasicComponentView> components = Connect.getInstance().getComponentViews();
        components.remove(view);
        removeView(view);
        Log.d("UNLOCK", components.size() + " removeView");
    }

    /**
     * This is a method that should be used when adding an {@link BasicComponentView}
     * to the {@link SchemeView}. Method makes sure the component is added to the {@link SchemeView}
     * and some other data structures in {@link Connect}.
     * @param view any {@link BasicComponentView}.
     * @see Connect
     */
    public void completeAddView(BasicComponentView view){
        ArrayList<BasicComponentView> components = Connect.getInstance().getComponentViews();
        components.add(view);
        addView(view);
        Log.d("UNLOCK", components.size() + " addView");
    }

    private void rectSetter(PointF position, BasicComponentModel model, float offset, Rect givenRect){
        givenRect.set(
                ((int) (position.x - offset)),
                ((int) (position.y - offset)),
                ((int) (position.x + model.getWidth() + offset)),
                ((int) (position.y + model.getHeight() + offset))
        );
        Log.d("RECT", givenRect.toString());
    }

    private boolean isInsideAnotherView(PointF position, BasicComponentModel model){
        float offset = Constants.SCHEME_OFFSET.getDPValue(getContext());
        Rect viewRect = new Rect();
        rectSetter(position, model, offset, viewRect);
        Rect otherRect = new Rect();
        for (BasicComponentView otherView : Connect.getInstance().getComponentViews()){
            rectSetter(otherView.getComponent().getPosition(), otherView.getComponent(), offset, otherRect);
            if(Rect.intersects(viewRect, otherRect)){
                Log.d("RECT", viewRect.toString() + "\n" + otherRect.toString());
                return true;
            }
        }
        return false;
    }

    private GraphPoint adjustEventXY(GraphPoint event){
        event.
                translate(-mTranslate.x, -mTranslate.y).
                translate(-(mScalePivot.x - mTranslate.x), -(mScalePivot.y - mTranslate.y)).
                antiScale(mScaleFactor, mScaleFactor).
                translate(mScalePivot.x - mTranslate.x, mScalePivot.y - mTranslate.y);
        return event;
    }

    private PointF softDrop(GraphPoint event, BasicComponentModel componentModel){
        event = adjustEventXY(event);
        PointF position = new PointF();

        position.x = event.x - componentModel.getWidth()/2;
        position.y = event.y - componentModel.getHeight()/2;
        float offset = Constants.SCHEME_OFFSET.getDPValue(getContext());
        position.x = offset * Math.round(position.x / offset);
        position.y = offset * Math.round(position.y / offset);

        if(isInsideAnotherView(position, componentModel)){
            Toast toast = Toast.makeText(
                    getContext(),
                    "Components too close to each other.",
                    Toast.LENGTH_SHORT
            );
            toast.show();
            return null;
        }
        return position;
    }

    private boolean prepareComponent(BasicComponentView componentView, DragEvent event){
        PointF position = softDrop(
                new GraphPoint(event.getX(), event.getY()),
                componentView.getComponent()
        );
        if (position == null){
            return false;
        } else {
            componentView.getComponent().setPosition(position);
            return true;
        }
    }

    @Override
    public boolean onDragEvent(DragEvent event) {
        final int action = event.getAction();

        switch(action){
            case DragEvent.ACTION_DRAG_STARTED:
                if(event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)){
                    invalidate();
                    return true;
                }
                return false;
            case DragEvent.ACTION_DRAG_ENTERED:
                invalidate();
                return true;
            case DragEvent.ACTION_DROP:
                ClipData.Item item = event.getClipData().getItemAt(0);
                BasicComponentView view = (BasicComponentView)event.getLocalState();

                if(item.getText().toString().equals(SchemeFragment.SOURCE)){
                    BasicComponentView componentView =
                            new BasicComponentFactory().create((BasicComponent)view.getTag(), getContext());
                    if (prepareComponent(componentView, event)){
                        completeAddView(componentView);
                    } else {
                        break;
                    }
                } else {
                    view.setVisibility(View.GONE);
                    Connect.getInstance().getComponentViews().remove(view);
                    if(prepareComponent(view, event)){
                        Connect.getInstance().getComponentViews().add(view);
                        view.getComponent().updateAllLinesFromGates();
                    } else {
                        Connect.getInstance().getComponentViews().add(view);
                    }
                    view.setVisibility(View.VISIBLE);
                }

                invalidate();
                return true;
            case DragEvent.ACTION_DRAG_ENDED:
                invalidate();
                return true;
        }
        return false;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener{
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();
            mScaleFactor =
                    Math.max(
                            ScaleConstants.MINSCALE.getValue(),
                            Math.min(mScaleFactor, ScaleConstants.MAXSCALE.getValue())
                    );
            SchemeContext.scaleFactor = mScaleFactor;
            return true;
        }
    }

    private class MyGestureListener implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener{
        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            mTranslate.x -= distanceX /mScaleFactor;
            mTranslate.y -= distanceY /mScaleFactor;
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            mScaleFactor = 1;
            SchemeContext.scaleFactor = mScaleFactor;
            requestLayout();
            invalidate();
            return true;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return false;
        }
    }
}
