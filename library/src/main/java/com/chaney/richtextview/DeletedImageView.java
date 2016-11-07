package com.chaney.richtextview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Created by Chaney on 2016/11/6.
 */

public class DeletedImageView extends ImageView implements GestureDetector.OnGestureListener{



    private static final float DEFAULT_D_WIDTH = 30.0f;

    private static final float DEFAULT_D_HEIGHT = 20.0f;

    private static final int DEFAULT_D_BG_COLOR = Color.parseColor("#50000000");

    private static final int DEFAULT_D_COLOR = Color.WHITE;

    private static final float DEFAULT_PAINT_WIDTH = 1.0f;

    private float deleteWidth;

    private float deleteHeight;

    private Paint paint;

    private int deleteBgColor;

    private Rect rect;

    private int deleteColor;

    private int dLeft;

    private int dRight;

    private int dTop;

    private int dBottom;

    private GestureDetector gestureDetector;

    private boolean isShowDeleteView = true;


    public DeletedImageView(Context context) {
        this(context,null);
    }

    public DeletedImageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DeletedImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        paint = new Paint();
        gestureDetector = new GestureDetector(context,this);

        parseAttrs(context.obtainStyledAttributes(attrs,R.styleable.DeletedImageView));
    }

    public void setShowDeleteView(boolean showDeleteView) {
        isShowDeleteView = showDeleteView;
    }

    private void parseAttrs(TypedArray typedArray) {
        if(null != typedArray){
            try {
                setDeleteWidth(typedArray.getDimension(R.styleable.DeletedImageView_deleteWidth, ConvertUtil.dp2px(getContext(),DEFAULT_D_WIDTH)));
                setDeleteHeight(typedArray.getDimension(R.styleable.DeletedImageView_deleteHeight,ConvertUtil.dp2px(getContext(),DEFAULT_D_HEIGHT)));
                setDeleteBgColor(typedArray.getColor(R.styleable.DeletedImageView_deleteBackgroundColor,DEFAULT_D_BG_COLOR));
                setDeleteColor(typedArray.getColor(R.styleable.DeletedImageView_deleteColor,DEFAULT_D_COLOR));
            }finally {
                typedArray.recycle();
            }
        }
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(isShowDeleteView){
            dLeft = (int) (getWidth()-getDeleteWidth());
            dBottom = (int) getDeleteHeight();
            dRight = getWidth();
            dTop = 0;

            Log.e("TAG--",dLeft +" ; " + dTop + " ; " + dRight + " ; " + dBottom);

            rect = new Rect(dLeft,dTop,dRight,dBottom);

            paint.setColor(getDeleteBgColor());

            canvas.drawRect(rect,paint);

            float padding = Math.min(getDeleteWidth(),getDeleteHeight())/4;

            float startX = dLeft + padding;
            float startY = dTop + padding;
            float endX = dRight - padding;
            float endY = dBottom - padding;

            paint.setColor(getDeleteColor());
            paint.setStrokeWidth(ConvertUtil.dp2px(getContext(),DEFAULT_PAINT_WIDTH));
            canvas.drawLine(startX,startY,endX,endY,paint);
            canvas.drawLine(startX,endY,endX,startY,paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        Log.e("TAG--","x : " +x+"  ;  y : "+y);
        if(x > dLeft && y < dBottom){
            Log.e("TAG--","Delete");
            return performClick();
        }
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    public void setDeleteBgColor(int deleteBgColor) {
        this.deleteBgColor = deleteBgColor;
    }

    public void setDeleteHeight(float deleteHeight) {
        this.deleteHeight = deleteHeight;
    }

    public void setDeleteWidth(float deleteWidth) {
        this.deleteWidth = deleteWidth;
    }

    public float getDeleteHeight() {
        return deleteHeight;
    }

    public float getDeleteWidth() {
        return deleteWidth;
    }

    public int getDeleteBgColor() {
        return deleteBgColor;
    }

    public int getDeleteColor() {
        return deleteColor;
    }

    public void setDeleteColor(int deleteColor) {
        this.deleteColor = deleteColor;
    }


}
