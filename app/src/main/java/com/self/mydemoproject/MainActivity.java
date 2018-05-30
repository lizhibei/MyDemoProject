package com.self.mydemoproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.self.mydemoproject.activity.BaseActivity;
import com.self.mydemoproject.activity.GoodDetailSizeActivity;
import com.self.mydemoproject.activity.HorizontalScrollViewDemo;
import com.self.mydemoproject.activity.HttpURLConnectionActivity;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext=this;
        initview();
    }

    private void initview() {
        findViewById(R.id.httpurl_connection_tv).setOnClickListener(this); //HttpURLConnection
        findViewById(R.id.goodsize_tv).setOnClickListener(this); //商品详情标签
        findViewById(R.id.horizontal_tv).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //HttpURLConnection
            case R.id.httpurl_connection_tv:
                Intent toHttpURLConnectionActivity=new Intent(mContext,HttpURLConnectionActivity.class);
                startActivity(toHttpURLConnectionActivity);
                break;
                //商品详情标签
            case R.id.goodsize_tv:
                Intent toGoodDetailSizeActivity=new Intent(mContext,GoodDetailSizeActivity.class);
                startActivity(toGoodDetailSizeActivity);
                break;
                //
            case R.id.horizontal_tv:
                Intent toHorizontalScrollViewDemo=new Intent(mContext,HorizontalScrollViewDemo.class);
                startActivity(toHorizontalScrollViewDemo);
                break;
        }
    }
}
