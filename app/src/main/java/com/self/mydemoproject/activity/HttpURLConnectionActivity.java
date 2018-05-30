package com.self.mydemoproject.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.log;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.self.mydemoproject.R;
import com.self.mydemoproject.appconfig.Constans;
import com.self.mydemoproject.appconfig.Website;
import com.self.mydemoproject.function.selectimage.GetImageFromMobile;
import com.self.mydemoproject.util.StreamUtil;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by dengjinwen on 2018/3/19.
 */

public class HttpURLConnectionActivity extends BaseActivity implements View.OnClickListener{
    private TextView head_text_title;
    private ImageView down_image_iv;

    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.httpurl_connection_activity);
        mContext=this;
        initview();
    }

    private void initview() {

        findViewById(R.id.head_img_left).setOnClickListener(this);
        head_text_title=findViewById(R.id.head_text_title);
        head_text_title.setText("HttpURLConnection");

        findViewById(R.id.get_b).setOnClickListener(this); //get 请求方式
        findViewById(R.id.post_not_b).setOnClickListener(this); //post   未带参数
        findViewById(R.id.post__b).setOnClickListener(this);  //post  带参数
        findViewById(R.id.post_json_b).setOnClickListener(this);//post 参数格式为json
        findViewById(R.id.upload_file_b).setOnClickListener(this); //上传文件

        findViewById(R.id.down_file_b).setOnClickListener(this);
        down_image_iv=findViewById(R.id.down_image_iv);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(resultCode==RESULT_OK){
            switch (requestCode){
                case Constans.SELECT_IMAGE:
                    Bundle bundle=intent.getExtras();
                    final ArrayList<String> urls=bundle.getStringArrayList("data");
                    new Thread(new Runnable() {
                         @Override
                         public void run() {
                              uploadFile(urls.get(0));
                         }
                    }).start();
                    break;
            }
        }
    }

    /**
     * HttpURLConnection  get  请求实例
     */
    private void httpURLConnectionByGet(){
        try {
            URL url=new URL(Website.HTTP_URL_CONNECTION);
            HttpURLConnection connection= (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");  //必须大写
            connection.connect();
            int responseCode=connection.getResponseCode();
            if(responseCode==HttpURLConnection.HTTP_OK){
                InputStream inputStream=connection.getInputStream();
                String s= StreamUtil.convertInputStreamToString(inputStream);
                log.e("s:"+s);
            }else {
                ShowToast(mContext,"请求网络失败");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            log.e("url:"+e.toString());
        } catch (IOException e) {
            log.e("io:"+e.toString());
            e.printStackTrace();
        }
    }

    /**
     * HttpURLConnection  post  请求实例
     */
    private void httpURLConnectionByPost(){
        try {
            URL url=new URL(Website.HTTP_URL_CONNECTION);
            HttpURLConnection connection= (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST"); //必须大写
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.connect();
            int responseCode=connection.getResponseCode();
            if(responseCode==HttpURLConnection.HTTP_OK){
                InputStream inputStream=connection.getInputStream();
                String result=StreamUtil.convertInputStreamToString(inputStream);
                log.e("result:"+result);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *  HttpURLConnection  post 带参数的 请求实例
     */
    private void httpURLConnectionByPostParams(){
        try {
            URL url=new URL(Website.HTTP_URL_CONNECTION);
            HttpURLConnection connection= (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST"); //必须大写
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.connect();
            String body="ursrname=lizhi&password=123456";
            BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(),"UTF-8"));
            writer.write(body);
            writer.close();
            int responseCode=connection.getResponseCode();
            if(responseCode==HttpURLConnection.HTTP_OK){
                InputStream inputStream=connection.getInputStream();
                String result=StreamUtil.convertInputStreamToString(inputStream);
                log.e("result:"+result);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * post  请求参数为json格式
     */
    private void httpURLConnectionByPostJsonParams(){
        try {
            URL url=new URL(Website.HTTP_URL_CONNECTION);
            HttpURLConnection connection= (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestProperty("Content-Type","application/json;charset=utf-8");
            String body="{username:lizhi,password:123456}";
            BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(),"UTF-8"));
            writer.write(body);
            writer.close();
            int responseCode=connection.getResponseCode();
            if(responseCode==HttpURLConnection.HTTP_OK){
                InputStream inputStream=connection.getInputStream();
                String result=StreamUtil.convertInputStreamToString(inputStream);
                log.e("result:"+result);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传文件
     */
    private void uploadFile(String path){
        try {
            String BOUNDARY = java.util.UUID.randomUUID().toString();
            String TWO_HYPHENS = "--";
            String LINE_END = "\r\n";
            URL url=new URL(Website.UPLOAD_FILE);
            HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            httpURLConnection.setRequestProperty("Content-Type","multipart/form-data;boundary=" + BOUNDARY);
            httpURLConnection.setRequestProperty("Cache-Control", "no-cache");
            httpURLConnection.connect();

            DataOutputStream outputStream=new DataOutputStream(httpURLConnection.getOutputStream());
            StringBuffer strBufparam = new StringBuffer();
            //封装键值对数据一
            strBufparam.append(TWO_HYPHENS);
            strBufparam.append(BOUNDARY);
            strBufparam.append(LINE_END);
            strBufparam.append("Content-Disposition: form-data; name=\"" + "groupId" + "\"");
            strBufparam.append(LINE_END);
            strBufparam.append("Content-Type: " + "text/plain" );
            strBufparam.append(LINE_END);
            strBufparam.append("Content-Lenght: "+"groupId".length());
            strBufparam.append(LINE_END);
            strBufparam.append(LINE_END);
            strBufparam.append("groupId");
            strBufparam.append(LINE_END);

            //封装键值对数据二
            strBufparam.append(TWO_HYPHENS);
            strBufparam.append(BOUNDARY);
            strBufparam.append(LINE_END);
            strBufparam.append("Content-Disposition: form-data; name=\"" + "title" + "\"");
            strBufparam.append(LINE_END);
            strBufparam.append("Content-Type: " + "text/plain" );
            strBufparam.append(LINE_END);
            strBufparam.append("Content-Lenght: "+"kwwl".length());
            strBufparam.append(LINE_END);
            strBufparam.append(LINE_END);
            strBufparam.append("kwwl");
            strBufparam.append(LINE_END);

            //拼接完成后，一块写入
            outputStream.write(strBufparam.toString().getBytes());

            File file=new File(path);
            StringBuffer strBuffile=new StringBuffer();
            strBuffile.append(LINE_END);
            strBuffile.append(TWO_HYPHENS);
            strBuffile.append(BOUNDARY);
            strBuffile.append(LINE_END);
            strBuffile.append("Content-Disposition: form-data; name=\"" + "image" + "\"; filename=\"" + file.getName() + "\"");
            strBuffile.append(LINE_END);
            strBuffile.append("Content-Type: " + "image/*" );
            strBuffile.append(LINE_END);
            strBuffile.append("Content-Lenght: "+file.length());
            strBuffile.append(LINE_END);
            strBuffile.append(LINE_END);

            outputStream.write(strBuffile.toString().getBytes());

            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] buffer = new byte[1024*2];
            int length = -1;
            while ((length = fileInputStream.read(buffer)) != -1){
                outputStream.write(buffer,0,length);
            }
            outputStream.flush();
            fileInputStream.close();
            //写入标记结束位
            byte[] endData = (LINE_END + TWO_HYPHENS + BOUNDARY + TWO_HYPHENS + LINE_END).getBytes();//写结束标记位
            outputStream.write(endData);
            outputStream.flush();
            //得到响应
            int responseCode = httpURLConnection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                InputStream inputStream = httpURLConnection.getInputStream();
                String result=StreamUtil.convertInputStreamToString(inputStream);
                log.e("result:"+result);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载文件
     */
    private void downFile(String path){
        try {
            URL url=new URL(path);
            HttpURLConnection connection= (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int responseCode=connection.getResponseCode();
            if(responseCode==HttpURLConnection.HTTP_OK){
                InputStream is=connection.getInputStream();
                File dir=new File(Environment.getExternalStorageDirectory().toString()+"/image");
                if(!dir.exists()){
                    dir.mkdirs();
                }
                File file=new File(dir,"downimage.png");
                FileOutputStream fos=new FileOutputStream(file);
                byte[] buf=new byte[1024*8];
                int len=-1;
                while ((len=is.read(buf))!=-1){
                    fos.write(buf,0,len);
                }
                ImageLoader.getInstance().displayImage("file://"+file.getAbsolutePath(),down_image_iv);
                fos.flush();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //返回
            case R.id.head_img_left:
                finish();
                break;
                //get 请求方式
            case R.id.get_b:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        httpURLConnectionByGet();
                    }
                }).start();
                break;
                //post  未带参数
            case R.id.post_not_b:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        httpURLConnectionByPost();
                    }
                }).start();
                break;
                //post  带参数
            case R.id.post__b:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        httpURLConnectionByPostParams();
                    }
                }).start();
                //post 参数格式为json
            case R.id.post_json_b:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        httpURLConnectionByPostJsonParams();
                    }
                }).start();
                break;
                //上传文件
            case R.id.upload_file_b:
                Intent toGetImageFromMobile=new Intent(mContext,GetImageFromMobile.class);
                Bundle bundle=new Bundle();
                bundle.putInt("number",1);
                toGetImageFromMobile.putExtras(bundle);
                startActivityForResult(toGetImageFromMobile, Constans.SELECT_IMAGE);
                break;
                //下载文件
            case R.id.down_file_b:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        downFile("http://img.zcool.cn/community/0142135541fe180000019ae9b8cf86.jpg@1280w_1l_2o_100sh.png");
                    }
                }).start();
                break;
        }
    }
}
