package com.dr.xg.myapplication.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.dr.xg.myapplication.R;
import com.dr.xg.myapplication.zxing.InputDialog;

import static android.graphics.Color.*;

/**
 * @author 黄冬榕
 * @date 2018/1/9
 * @description
 * @remark
 */

public class CustomView extends View {
    private String mTitle;
    private boolean mIsShow;
    private int mColor;
    private int mtitleSize;
    private Paint paint;
    private Canvas canvas;
    private Region circleRegion;
    private Path circlePath;

    public CustomView(Context context) {
        super(context);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);

    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomView,defStyleAttr,0);
        for (int i =0; i<typedArray.length();i++) {
            int id = typedArray.getIndex(i);
            switch (id){
                case R.styleable.CustomView_title:
                    mTitle =typedArray.getString(id);
                    break;
                case R.styleable.CustomView_isshow:
                    mIsShow = typedArray.getBoolean(id,false);
                    break;
                case R.styleable.CustomView_color:
                    mColor = typedArray.getColor(id, RED);
                    break;
                case R.styleable.CustomView_size:
                    mtitleSize = typedArray.getDimensionPixelSize(id,(int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                    break;
            }
        }

        paint = new Paint();
        paint.setColor(BLUE);
        circlePath = new Path();
        circleRegion = new Region();


        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                paint.setColor(RED);
                mTitle = Math.random() +"";
                invalidate();
            }
        });

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        circlePath.addCircle(w/2,h/2,300, Path.Direction.CW);
        Region region = new Region(w/2-300,h/2-300,w/2+300,h/2+300);
        circleRegion.setPath(circlePath,region);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;
        canvas.drawPath(circlePath,paint);
        paint.setColor(mColor);
        paint.setTextSize(mtitleSize);

        int startWidth = getWidth()/2;
        int startHeight = getHeight()/2;

        for (int i = 0 ; i<mTitle.length();i++){
            float textlong =  paint.measureText(mTitle,i,i+1);//一个字符的大小
            if (circleRegion.contains((int)(startWidth+textlong),startHeight)) {//加上这个字符还是否在区域内
                canvas.drawText(mTitle.substring(i, i + 1), startWidth, startHeight, paint);
                startWidth = (int)(startWidth + textlong);//下一个字开始x
            }else {
                startWidth = getWidth()/2;
                startHeight = (int)(startHeight + textlong*2);
                canvas.drawText(mTitle.substring(i,i + 1),startWidth,startHeight,paint);
                startWidth = (int)(startWidth + textlong);//下一个字开始x
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                paint.setColor(Color.BLUE);
                invalidate();
                break;

        }
        return super.onTouchEvent(event);
    }

    public void setText(String text){
        paint.setColor(RED);
        mTitle = text;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }


}
