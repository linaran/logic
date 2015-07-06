package com.example.deni.logicComponentView;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.deni.connect.Connect;
import com.example.deni.globalUtility.Constants;
import com.example.deni.logic.GateInputDialog;
import com.example.deni.logic.GateOutputDialog;
import com.example.deni.logic.InputOutputDialog;
import com.example.deni.logic.R;
import com.example.deni.logicComponentDrawable.DrawableComponent;
import com.example.deni.logicComponentModel.single.BasicComponentDefault;
import com.example.deni.logicComponentModel.single.BasicComponentModel;
import com.example.deni.logicComponentView.logicViewComponentsFactory.BasicComponent;
import com.example.deni.logicViewScheme.SchemeContext;
import com.example.deni.logicViewScheme.SchemeView;

import java.io.Serializable;
import java.util.UUID;

/**
 *
 */
public abstract class BasicComponentView extends View implements Serializable{
    protected DrawableComponent mComponentDrawable;
    protected BasicComponentModel mComponent;
    protected UUID mId;
    public static String DIALOG_TAG_IO = "InputOutputDialog";
    public static String DIALOG_TAG_GID = "GateInputDialog";
    public static String DIALOG_TAG_GOD = "GateOutputDialog";

    GestureDetector mGestureDetector;

    public BasicComponentView(BasicComponentModel component, DrawableComponent drawableComponent) {
        super(component.getContext());
        init(null, 0, component, drawableComponent);
    }

    public BasicComponentView(AttributeSet attrs, BasicComponentModel component, DrawableComponent drawableComponent) {
        super(component.getContext(), attrs);
        init(attrs, 0, component, drawableComponent);
    }

    public BasicComponentView(AttributeSet attrs, BasicComponentModel component, DrawableComponent drawableComponent, int defStyleAttr) {
        super(component.getContext(), attrs, defStyleAttr);
        init(attrs, defStyleAttr, component, drawableComponent);
    }

    private void init(AttributeSet attrs, int defStyle, BasicComponentModel component, DrawableComponent drawableComponent){
//        Silent killers
        mComponent = component;
        mComponentDrawable = drawableComponent;
        mId = UUID.randomUUID();

        mGestureDetector = new GestureDetector(getContext(), new MyGestureListener());
        setWillNotDraw(false);
    }

//    TODO: Method for deserialization required.

    /**
     * Views method that parents call in order to calculate its dimensions.
     * Basically this is needed for listView only. Exceptionally good for
     * supporting different screen sizes.
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredWidth = mComponent.getWidth();
        int desiredHeight = mComponent.getHeight();

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        width = setDimension(widthMode, widthSize, desiredWidth);
        height = setDimension(heightMode, heightSize, desiredHeight);

        setMeasuredDimension(width, height);
    }

    /**
     * Helper method to onMeasure method.
     * @param dimensionMode
     * @param dimensionSize
     * @param desiredDimension
     * @return int
     */
    private int setDimension(int dimensionMode, int dimensionSize, int desiredDimension){
        int ret;

        if (dimensionMode == MeasureSpec.EXACTLY) {
            ret = dimensionSize;
        } else if (dimensionMode == MeasureSpec.AT_MOST){
            ret = Math.min(desiredDimension, dimensionSize);
        } else {
            ret = desiredDimension;
        }
        return ret;
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        super.onTouchEvent(event);

        if(getParent().getClass().getSimpleName().equals("SchemeView")){
            mGestureDetector.onTouchEvent(event);
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mComponentDrawable.draw(canvas);
    }

    public BasicComponentModel getComponent() {
        return mComponent;
    }

    private void updateComponentHeight(int inputGateCount, int outputGateCount){
        int offset = Constants.SCHEME_OFFSET.getDPValue(getContext());
        int maxGateCount = Math.max(inputGateCount, outputGateCount);

        if (maxGateCount <= 3){
            mComponent.setHeight(BasicComponentDefault.N_HEIGHT.getValue() * offset);
        } else {
            mComponent.setHeight((maxGateCount + 1) * offset);
        }
    }

    /**
     * Called by InputOutputDialog.
     * @param inputGateCount
     * @param outputGateCount
     */
    public void setGateCount(int inputGateCount, int outputGateCount) {
        if (inputGateCount == mComponent.getInputGateCount() && outputGateCount == mComponent.getOutputGateCount()){
            return;
        }

        updateComponentHeight(inputGateCount, outputGateCount);

        mComponent.setInputGateCount(inputGateCount);
        mComponent.setOutputGateCount(outputGateCount);
        mComponentDrawable.setInputOutputGateNumbers(
                inputGateCount,
                outputGateCount
        );
        requestLayout();
    }

    private class MyGestureListener implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener{
        private Vibrator vb = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (getTag().equals(BasicComponent.IN)){
                if(mComponent.getOutputGate(0).getValue()){
                    mComponentDrawable.setComponentName("0");
                    mComponent.getOutputGate(0).setValue(false);
                } else{
                    mComponentDrawable.setComponentName("1");
                    mComponent.getOutputGate(0).setValue(true);
                }
                invalidate();
                return true;
            }
            return false;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            LinearLayout editButtons;
            SchemeView parent = (SchemeView)getParent();
            editButtons = (LinearLayout) parent.findViewById(R.id.edit_component_layout);
            editButtons.bringToFront();
            if (Build.VERSION.SDK_INT < 19){
                requestLayout();
                invalidate();
            }

            ImageButton editConnectionsInputButton = (ImageButton) editButtons.findViewById(R.id.edit_input_connect);
            if (BasicComponentView.this.getComponent().getInputGateCount() == 0){
                editConnectionsInputButton.setVisibility(GONE);
            } else {
                editConnectionsInputButton.setVisibility(VISIBLE);
            }
            editConnectionsInputButton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    FragmentManager fm = ((FragmentActivity)getContext()).getSupportFragmentManager();
                    DialogFragment dialog = new GateInputDialog();
                    LinearLayout buttonParent = (LinearLayout) v.getParent();
                    buttonParent.setVisibility(View.GONE);

                    SchemeContext.componentView = BasicComponentView.this;
                    dialog.show(fm, DIALOG_TAG_GID);
                }

            });

            ImageButton editConnectionsOutputButton = (ImageButton) editButtons.findViewById(R.id.edit_output_connect);
            if (BasicComponentView.this.getComponent().getOutputGateCount() == 0){
                editConnectionsOutputButton.setVisibility(GONE);
            } else {
                editConnectionsOutputButton.setVisibility(VISIBLE);
            }
            editConnectionsOutputButton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    FragmentManager fm = ((FragmentActivity)getContext()).getSupportFragmentManager();
                    DialogFragment dialog = new GateOutputDialog();
                    LinearLayout buttonParent = (LinearLayout) v.getParent();
                    buttonParent.setVisibility(View.GONE);

                    SchemeContext.componentView = BasicComponentView.this;
                    dialog.show(fm, DIALOG_TAG_GOD);
                }

            });

            ImageButton editButton = (ImageButton) editButtons.findViewById(R.id.edit_component);
            editButton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    FragmentManager fm = ((FragmentActivity)getContext()).getSupportFragmentManager();
                    InputOutputDialog dialog = new InputOutputDialog();
                    SchemeContext.componentView = BasicComponentView.this;
                    dialog.show(fm, DIALOG_TAG_IO);
                }

            });

            ImageButton deleteButton = (ImageButton) editButtons.findViewById(R.id.delete_component);
            deleteButton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    BasicComponentView deadView = BasicComponentView.this;
                    SchemeView parent = (SchemeView) deadView.getParent();
                    deadView.getComponent().clearAllInOutLines();
                    parent.completeRemoveView(deadView);
//                    Prevents user from connecting a component he just deleted.
                    Connect.getInstance().clearSelection();
                    LinearLayout buttonParent = (LinearLayout) v.getParent();
                    buttonParent.setVisibility(GONE);
                }

            });

            editButtons.setVisibility(VISIBLE);
            return true;
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
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            vb.vibrate(Constants.VIBRATE.getValue());

            ClipData.Item item = new ClipData.Item("");
            ClipData dragData = new ClipData(
                    "",
                    new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN},
                    item
            );

            View.DragShadowBuilder shadow = new View.DragShadowBuilder(BasicComponentView.this);

//                        View is passed incorrectly, correct when you learn how to implement Parcelable
            BasicComponentView.this.startDrag(
                    dragData,
                    shadow,
                    BasicComponentView.this,
                    0
            );
//            BasicComponentView.this.setVisibility(View.GONE);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    }
}
