package com.self.mydemoproject.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.self.mydemoproject.R;
import com.self.mydemoproject.entity.GoodSize;
import com.self.mydemoproject.widget.GoodSizeViewGroup;

import java.util.ArrayList;

/**
 * 商品详情  规格显示
 */
public class GoodDetailSizeActivity extends BaseActivity implements View.OnClickListener{
    private TextView head_text_title;
    private GoodSizeViewGroup gsvg_vg;

    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.good_detail_size_activity);
        mContext=this;
        initview();
    }

    private void initview() {
        findViewById(R.id.head_img_left).setOnClickListener(this);
        head_text_title=findViewById(R.id.head_text_title);
        head_text_title.setText("商品规格标签");

        gsvg_vg=findViewById(R.id.gsvg_vg);
        ArrayList<GoodSize> list=new ArrayList<>();
        list.add(new GoodSize("0","事实上看得开",true));
        list.add(new GoodSize("1","的看看付款",false));
        gsvg_vg.setData(mContext,list);
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
