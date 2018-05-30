package com.self.mydemoproject.activity;

import android.content.Context;
import android.os.Bundle;

import com.self.mydemoproject.R;

public class AndroidHttpClientDemoActivity extends BaseActivity {
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.httpurl_connection_activity);
        mContext=this;
        initview();
    }

    private void initview() {

    }

    private void httpGet(){
        String _URI="www.baidu.com";
    }
}
