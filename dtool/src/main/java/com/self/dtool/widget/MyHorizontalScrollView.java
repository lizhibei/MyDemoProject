package com.self.dtool.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.android.volley.log;

import java.util.HashMap;
import java.util.Map;

public class MyHorizontalScrollView extends HorizontalScrollView {

    private OnItemClickListener mOnClickListener;

    //屏幕宽度
    private int mScrenWidth;

    //子元素宽度
    private int mChildWidth;

    //子元素的高度
    private int mChildHeight;

    //屏幕显示item数量
    private int mShowNumber;

    //显示的第一个位置
    private int mShowFirstPos;

    //显示最后一个位置
    private int mShowLastPos;

    //当前最左边item 的位置
    private int mLeftIndex;

    //当前最右边item 的位置
    private int mRightIndex=-1;

    //保存View 与位置的键值对
    private Map<View,Integer> mViewPos=new HashMap<>();

    private LinearLayout mContainer;

    private BaseAdapter mAdapter;

    private static OnItemClickListener mListener;

    public MyHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //获得屏幕宽度
        WindowManager wm= (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics=new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        mScrenWidth=metrics.widthPixels;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mContainer= (LinearLayout) getChildAt(0);
    }

    public void setAdapter(BaseAdapter adapter){
        mAdapter=adapter;
        initDatas();
    }

    /**
     * 设置一个屏幕显示item数量
     * @param count
     */
    public void setMaxShowCount(int count){
        mShowNumber=count;
    }

    public void initDatas(){
        mContainer= (LinearLayout) getChildAt(0);

        View view=mAdapter.getView(0,null,mContainer);
        mContainer.addView(view);

        if(mChildWidth==0&&mChildHeight==0){
            int w= MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
            int h= MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
            view.measure(w,h);

            mChildHeight=view.getMeasuredHeight();
            mChildWidth=view.getMeasuredWidth();

            if(mShowNumber==0){
                mShowNumber=mScrenWidth/mChildWidth;
            }else {
                mChildWidth=mScrenWidth/mShowNumber;
            }
        }

        initFirstScreenChildren();
    }

    /**
     * 初始化第一页
     */
    public void initFirstScreenChildren(){
        mContainer= (LinearLayout) getChildAt(0);
        mContainer.removeAllViews();
        mViewPos.clear();
        mRightIndex=-1;

        int temp=0;
        int count=mAdapter.getCount();
        if(count<=mShowNumber){
            temp=mAdapter.getCount();
        }else if(count>mShowNumber&&count<mShowNumber+2){
            temp=mShowNumber+1;
        }else {
            if(mShowFirstPos==mLeftIndex){
                temp=mShowNumber+1;
            }else {
                temp=mShowNumber+2;
            }
        }

        for(int i=mLeftIndex;i<temp+mLeftIndex;i++){
            final View view=mAdapter.getView(i,null,mContainer);
            final int position=i;
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClick(view,position);
                }
            });
            mContainer.addView(view);
            mRightIndex++;
            mViewPos.put(view,i);
        }
        if(mRightIndex-mShowFirstPos-mShowNumber>=0){
            mShowLastPos=mRightIndex-1;
        }else {
            mShowLastPos=mRightIndex;
        }
        if(mShowFirstPos!=mLeftIndex){
            scrollTo((mShowFirstPos-mLeftIndex)*mChildWidth,0);
        }else {
            scrollTo(0,0);
        }
    }

    /**
     * 加载下一页
     */
    protected void loadNext(){
//        log.e("mShowFirstPos:"+mShowFirstPos+",mShowLastPos:"+mShowLastPos+",mRightIndex:"+mRightIndex+",mLeftIndex:"+mLeftIndex);
        //达到右边界
        if(mShowLastPos>=mAdapter.getCount()-1){
            return;
        }

        //移除第一个item ，且将水平滚动位置0
        if(mShowFirstPos!=0){
            scrollTo(0,0);
            mViewPos.remove(mContainer.getChildAt(0));
            mContainer.removeViewAt(0);
        }

        if(mRightIndex<mAdapter.getCount()-1){
            //加载下一个
            final View view=mAdapter.getView(++mRightIndex,null,mContainer);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClick(view,mRightIndex);
                }
            });
            mContainer.addView(view);
            mViewPos.put(view,mRightIndex);
        }

        if(mShowFirstPos==0){
            scrollTo(mChildWidth,0);
        }else {
            mLeftIndex++;
        }
        mShowFirstPos++;
        mShowLastPos++;
//        log.e("mShowFirstPos:"+mShowFirstPos+",mShowLastPos:"+mShowLastPos+",mRightIndex:"+mRightIndex+",mLeftIndex:"+mLeftIndex);
    }

    /**
     * 加载前一页
     */
    protected void loadPre(){
//        log.e("mShowFirstPos:"+mShowFirstPos+",mShowLastPos:"+mShowLastPos+",mRightIndex:"+mRightIndex+",mLeftIndex:"+mLeftIndex);
        //当前已经是第一张了
        if(mShowFirstPos==0){
            return;
        }

        //需要移除最后一个
        if(mRightIndex>mShowLastPos){
            //移除最后一个item
            int oldViewPos=mContainer.getChildCount()-1;
            mViewPos.remove(mContainer.getChildAt(oldViewPos));
            mContainer.removeViewAt(oldViewPos);
            mRightIndex--;
        }

        if(mLeftIndex>0){
            mLeftIndex--;
            final View view=mAdapter.getView(mLeftIndex,null,mContainer);
            mViewPos.put(view,mLeftIndex);
            mContainer.addView(view,0);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClick(view,mLeftIndex);
                }
            });
        }

        scrollTo(mChildWidth,0);
        mShowFirstPos--;
        if(mShowFirstPos==0){
            if(mContainer.getChildCount()<mShowNumber){
                mShowLastPos++;
            }else {
                mShowLastPos--;
            }
        }else {
            if(mContainer.getChildCount()<mShowNumber+1){
                mShowLastPos++;
            }else {
                mShowLastPos--;
            }
        }
//        log.e("mShowFirstPos:"+mShowFirstPos+",mShowLastPos:"+mShowLastPos+",mRightIndex:"+mRightIndex+",mLeftIndex:"+mLeftIndex);
    }

    /**
     * 数据发生变化
     */
    public void notifyDataSetChanged(int selectPos){
        log.e("selectPos:"+selectPos+",mShowFirstPos:"+mShowFirstPos+",mShowLastPos:"+mShowLastPos+",mRightIndex:"+mRightIndex
        +",mLeftIndex:"+mLeftIndex);
        if(selectPos>=mAdapter.getCount()||selectPos<0){
            new Throwable("IndexOutOfBoundsException");
        }
        if(selectPos<mShowFirstPos){
            mShowFirstPos=selectPos;
            if(selectPos==0){
                mLeftIndex=mShowFirstPos;
            }else {
                mLeftIndex=mShowFirstPos-1;
            }
//            mRightIndex=mShowFirstPos+mShowNumber;
//            mShowLastPos=mShowFirstPos+mShowNumber-1;
        }
        if(selectPos>mShowLastPos){
            mShowFirstPos=selectPos-mShowNumber+1;
            if(mShowFirstPos==0){
                mLeftIndex=mShowFirstPos;
            }else {
                mLeftIndex=mShowFirstPos-1;
            }
        }
        log.e("mLeftIndex:"+mLeftIndex+",mShowFirstPos:"+mShowFirstPos);
        initFirstScreenChildren();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_MOVE:
                int scrollX=getScrollX();
                if(scrollX>=mChildWidth){
                    loadNext();
                }
                if(scrollX==0){
                    loadPre();
                }
                break;
        }
        return super.onTouchEvent(ev);
    }


    /**
     * 设置一个屏幕显示几个item
     * @param number
     */
    private void setShowNumber(int number){
        mShowNumber=number;
    }

    public interface OnItemClickListener{
        void onClick(View view, int pos);
    }

    public static void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }
}
