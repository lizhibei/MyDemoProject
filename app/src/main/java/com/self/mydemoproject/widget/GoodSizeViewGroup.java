package com.self.mydemoproject.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.log;
import com.self.mydemoproject.R;
import com.self.mydemoproject.entity.GoodSize;

import java.util.ArrayList;

public class GoodSizeViewGroup extends ViewGroup {

    private int HORIZONTAL_INTERVAL=20;
    private int VEERTICAL_INTERVAL=20;

    private int normalcolor,selectcolor,mtextsize;
    private Drawable selectBackground,normalBackground;

    private ArrayList<GoodSize> data;

    public GoodSizeViewGroup(Context context) {
        super(context);
        init(context,null,0);
    }

    public GoodSizeViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs,0);
    }

    public GoodSizeViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs,defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr){
        TypedArray typedArray=context.getTheme().obtainStyledAttributes(attrs, R.styleable.goodsize,defStyleAttr,0);
        int count=typedArray.getIndexCount();
        for(int i=0;i<count;i++){
            int attr=typedArray.getIndex(i);
            switch (attr){
                //正常文本颜色
                case R.styleable.goodsize_normalcolor:
                    normalcolor=typedArray.getColor(attr, Color.BLACK);
                    break;
                    //选中文本颜色
                case R.styleable.goodsize_selectcolor:
                    selectcolor=typedArray.getColor(attr, Color.RED);
                    break;
                    //文本大小
                case R.styleable.goodsize_mtextsize:
                    mtextsize=typedArray.getDimensionPixelSize(attr,(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                            16,getResources().getDisplayMetrics()));
                    break;
                    //选择颜色背景
                case R.styleable.goodsize_selectbackgroud:
                    selectBackground=typedArray.getDrawable(attr);
                    break;
                    //正常颜色背景
                case R.styleable.goodsize_normalbackgroud:
                    normalBackground=typedArray.getDrawable(attr);
                    break;
                    //水平间隔
                case R.styleable.goodsize_horizontal_interval:
                    HORIZONTAL_INTERVAL=typedArray.getDimensionPixelSize(attr,(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,20,
                            getResources().getDisplayMetrics()));
                    break;
                    //垂直间隔
                case R.styleable.goodsize_vertical_interval:
                    VEERTICAL_INTERVAL=typedArray.getDimensionPixelSize(attr,(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,20,
                            getResources().getDisplayMetrics()));
                    break;
            }
        }
    }

    /**
     * 添加数据
     */
    public void setData(Context context, final ArrayList<GoodSize> list){
        if(list!=null&&list.size()>0){
            data=list;
            removeAllViews();
            for(int i=0;i<data.size();i++){
                GoodSize gs=data.get(i);
                TextView textView=new TextView(context);
                textView.setText(gs.getName());
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,mtextsize);
                if(gs.isSelect()){
                    textView.setTextColor(selectcolor);
                    if(selectBackground!=null){
                        if(Build.VERSION.SDK_INT<16){
                            textView.setBackgroundDrawable(selectBackground);
                        }else {
                            textView.setBackground(selectBackground);
                        }
                    }
                }else {
                    textView.setTextColor(normalcolor);
                    if(normalBackground!=null){
                        if(Build.VERSION.SDK_INT<16){
                            textView.setBackgroundDrawable(normalBackground);
                        }else {
                            textView.setBackground(normalBackground);
                        }
                    }
                }
                final  int pos=i;
                textView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setSelect(pos);
                    }
                });
                addView(textView);
            }
        }else {
            Toast.makeText(context,"添加的数据存在问题", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 选择规格
     * @param position
     */
    public void setSelect(int position){
        for(int i=0;i<data.size();i++){
            GoodSize gs=data.get(i);
            TextView textView= (TextView) getChildAt(i);
            log.e("textView:"+textView+",i:"+i);
            if(i==position){
                gs.setSelect(true);
                textView.setTextColor(selectcolor);
                if(selectBackground!=null){
                    if(Build.VERSION.SDK_INT<16){
                        textView.setBackgroundDrawable(selectBackground);
                    }else {
                        textView.setBackground(selectBackground);
                    }
                }
            }else {
                gs.setSelect(false);
                textView.setTextColor(normalcolor);
                if(normalBackground!=null){
                    if(Build.VERSION.SDK_INT<16){
                        textView.setBackgroundDrawable(normalBackground);
                    }else {
                        textView.setBackground(normalBackground);
                    }
                }
            }
        }
        if(listener!=null){
            listener.onChange();
        }
    }

    /**
     * 获得选择的规格
     */
    public GoodSize getSelectObject(){
        for(int i=0;i<data.size();i++){
            GoodSize goodSize=data.get(i);
            if(goodSize.isSelect()){
                return goodSize;
            }
        }
        return null;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int stages=1; //标签行数
        int stageHeight=0;
        int stageWidth=0;

        int wholeWidth= MeasureSpec.getSize(widthMeasureSpec);

        for(int i=0;i<getChildCount();i++){
            View child=getChildAt(i);
            measureChild(child,widthMeasureSpec,heightMeasureSpec);
            stageWidth += (child.getMeasuredWidth() + HORIZONTAL_INTERVAL);
            stageHeight = child.getMeasuredHeight();
            if (stageWidth >= wholeWidth) {
                stages++;
                //reset stageWidth
                stageWidth = child.getMeasuredWidth();
            }
        }

        int wholeHeight = (stageHeight + VEERTICAL_INTERVAL) * stages;
        setMeasuredDimension(resolveSize(wholeWidth, widthMeasureSpec),
                resolveSize(wholeHeight, heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count=getChildCount();
        int row=0;
        int lengthX=l;
        int lengthY=t;
        for(int i=0;i<count;i++){
            View child=getChildAt(i);
            int width=child.getMeasuredWidth();
            int height=child.getMeasuredHeight();
            if(i==0){
                lengthX+=width;
            }else {
                lengthX+=width+HORIZONTAL_INTERVAL;
            }
            lengthY=row*(VEERTICAL_INTERVAL+height)+VEERTICAL_INTERVAL+height;
            if(lengthX>r){
                lengthX=l+width;
                row++;
                lengthY=row*(VEERTICAL_INTERVAL+height)+VEERTICAL_INTERVAL+height;
            }
            child.layout(lengthX-width,lengthY-height,lengthX,lengthY);
        }
    }

    //选择变化监听
    public interface OnSelectChangeListener{
        public void onChange();
    }
    public OnSelectChangeListener listener;
    public void setOnSelectChangeListener(OnSelectChangeListener l){
        listener=l;
    }
}
