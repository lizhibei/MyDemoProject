package com.self.mydemoproject.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.self.mydemoproject.R;
import com.self.mydemoproject.adapter.HorizontalScrollviewAdapter;
import com.self.mydemoproject.widget.MyHorizontalScrollView;

import java.util.ArrayList;


public class HorizontalScrollViewDemo extends BaseActivity implements View.OnClickListener {
    private TextView head_text_title;
    private MyHorizontalScrollView mhs_sv;

    private Context mContext;
    private ArrayList<Integer> data=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.horizontal_scrollview_demo);
        mContext=this;
        initdata();
        initview();
    }

    private void initview() {
        findViewById(R.id.head_img_left).setOnClickListener(this);
        head_text_title=findViewById(R.id.head_text_title);
        head_text_title.setText("HorizontalScrollViewDemo");

        mhs_sv=findViewById(R.id.mhs_sv);
        mhs_sv.setMaxShowCount(1);
        mhs_sv.setAdapter(new HorizontalScrollviewAdapter(mContext,data));
    }

    private void initdata(){
        data.add(R.mipmap.c);
        data.add(R.mipmap.cd);
        data.add(R.mipmap.ly);
        data.add(R.mipmap.sf);
        data.add(R.mipmap.ysg);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.head_img_left:
                finish();
                break;
        }
    }
}
