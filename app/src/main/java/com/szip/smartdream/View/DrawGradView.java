package com.szip.smartdream.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.szip.smartdream.R;


public class DrawGradView extends View {

    private float gap_grid;//网格间距
    private int width,height;//本页面宽，高
    private int xori;//原点x坐标
    private int grid_hori,grid_ver;//横、纵线条数
    private float gap_x;//两点间横坐标间距
    private int dataNum_per_grid = 18;//每小格内的数据个数
    private int color = R.color.white;


    public DrawGradView(Context context) {
        super(context);
        this.setBackgroundColor(getResources().getColor(R.color.nullColor));
    }

    public DrawGradView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.setBackgroundColor(getResources().getColor(R.color.nullColor));
    }

    public DrawGradView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setBackgroundColor(getResources().getColor(R.color.nullColor));
    }


    public void setColor(int color){
        this.color = color;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (changed){
            xori = 0;
            width = getWidth();
            height = getHeight();
            grid_hori = 9;
            gap_grid = height/9;
            grid_ver = width/(int)gap_grid;
            gap_x = gap_grid/dataNum_per_grid;
            Log.e("json","本页面宽： " + width +"  高:" + height);
        }

        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        DrawGrid(canvas);
    }


    /**
     * 画背景网格
     */
    private void DrawGrid(Canvas canvas){
        //横线
        for (int i = 4 ; i < grid_hori ; i ++){
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(getResources().getColor(color)); //<color name="data_pr">#0a7b14</color>
            paint.setStrokeWidth(1.5f);
            Path path = new Path();
            path.moveTo(xori, gap_grid * (i-1) + (height-grid_hori*gap_grid)/2);
            path.lineTo(width,gap_grid * (i-1) + (height-grid_hori*gap_grid)/2);
            canvas.drawPath(path,paint);
        }
//        //竖线
//        for (int i = 1 ; i < grid_ver + 2 ; i ++){
//            Paint paint = new Paint();
//            paint.setStyle(Paint.Style.STROKE);
//            paint.setColor(getResources().getColor(R.color.brown));
//            paint.setStrokeWidth(1.0f);
//            Path path = new Path();
//            path.moveTo(gap_grid * (i-1) + (width-grid_ver*gap_grid)/2, 0);
//            path.lineTo(gap_grid * (i-1) + (width-grid_ver*gap_grid)/2,height);
////            if ( i % 5 != 0 ){
//                PathEffect effect = new DashPathEffect(new float[]{10,10},5);
//                paint.setPathEffect(effect);
////            }
//            canvas.drawPath(path,paint);
//        }
    }
}
