package com.self.mydemoproject.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


public class BaseActivity extends AppCompatActivity {
    private RequestQueue requestQueue;
    private ProgressDialog pbDialog;
//    private DialogUtil dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_base);
        requestQueue= Volley.newRequestQueue(this);
    }
    public static void ShowToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void startIntent(Context context, Class<?> c) {
        Intent intent = new Intent();
        intent.setClass(context, c);
        context.startActivity(intent);
    }

    public static void startIntentPost(Context context, Class<?> c, Bundle b) {
        Intent intent = new Intent();
        intent.setClass(context, c);
        intent.putExtras(b);
        context.startActivity(intent);
    }

    /***
     * 跳转Web页面
     * @param mContext
     * @param id 协议id 1：售后规则 2：注册协议 3:关于我们
     * @param title
     */
//    public void GotoWeb(final Context mContext, String id, final String title){
//        HashMap<String, String> map= GetMap.getMap(this);
//        map.put("id",id);
//        GsonRequest<GetUrlInfo> request=new GsonRequest<GetUrlInfo>(WebSite.GETCONTENT,
//                GetUrlInfo.class, new Listener<GetUrlInfo>() {
//
//            @Override
//            public void onResponse(GetUrlInfo result) {
//                if (result.getResult().getCode()==10000){
//                    String url =  result.getData().getUrl();
//                    if(null!=url&&!url.equals("")){
//                        Intent toRuleDescriptionActivity = new Intent(mContext,
//                                RuleDescriptionActivity.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putString("url", url);
//                        bundle.putString("title",
//                                title);
//                        toRuleDescriptionActivity.putExtras(bundle);
//                        startActivity(toRuleDescriptionActivity);
//                    }else{
//                        ShowToast(mContext,getResources().getString(R.string.get_info_fail));
//                    }
//                }else{
//                    ShowToast(mContext,getResources().getString(R.string.get_info_fail));
//                    return;
//                }
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError arg0) {
//                ShowToast(mContext,getResources().getString(R.string.get_info_fail));
//                return;
//            }
//        }, map, this);
//        requestQueue.add(request);
//    }
//
//    public void showToast(Context context, String msg){
//        Toast.makeText(context,msg, Toast.LENGTH_SHORT).show();
//    }
//
//    /**
//     * 检查版本更新
//     *
//     * @param context
//     * @param isShowNo
//     *            true:无更新时提示，false：无更新时不提示
//     */
//    protected void checkVersion(final Context context, final Boolean isShowNo) {
//
//        if (isShowNo) {
//            pbDialog = new ProgressDialog(context);
//            pbDialog.setMessage("正在检查更新,请稍候...");
//            pbDialog.setCancelable(false);
//            pbDialog.show();
//        }
//        HashMap<String, String> map=GetMap.getMap(this);
//        map.put("version_code", ApplicationUtil.getLocalVersionCode(this) + "");
//        map.put("os", "android");
//        GsonRequest<UpdataAppInfo> request=new GsonRequest<UpdataAppInfo>(WebSite.VERSION_UPGRADE,
//                UpdataAppInfo.class, new Listener<UpdataAppInfo>() {
//
//            @Override
//            public void onResponse(UpdataAppInfo result) {
//                if (isShowNo) {
//                    pbDialog.dismiss();
//                }
//                if(result.getResult().getCode()==10000){
//                    updataData updatainfo = result.getData();
//                    if (updatainfo.getIs_update().equals("0")) {
//
//                        if (isShowNo) {
//                            dialog = new DialogUtil(context, "更新", 1,
//                                    new DialogUtil.OnCancleAndConfirmListener() {
//
//                                        @Override
//                                        public void confirm() {
//                                            dialog.dismiss();
//                                        }
//
//                                        @Override
//                                        public void cancle() {
//                                        }
//                                    });
//                            TextView content = new TextView(context);
//                            content.setText("暂无更新");
//                            dialog.setContent(content);
//
//                        }
//                    } else if (updatainfo.getIs_update().equals("1")) {
//                        // 非强制更新
//                        // showUpdata(context, updatainfo, 2);
//                        UpdateManager updateManager = new UpdateManager(
//                                BaseActivity.this, updatainfo, false);
//                        updateManager.checkUpdateInfo();
//
//                    } else if (updatainfo.getIs_update().equals("2")) {
//                        // 强制更新
//                        // showUpdata(context, updatainfo, 1);
//                        UpdateManager updateManager = new UpdateManager(
//                                BaseActivity.this, updatainfo, true);
//                        updateManager.checkUpdateInfo();
//                    }
//                }
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError arg0) {
//                if (isShowNo) {
//                    pbDialog.dismiss();
//                }
//            }
//        }, map, this);
//        requestQueue.add(request);
//    }
//
//    private void showUpdata(final Context context, final updataData info,
//                            int flag) {
//        DialogUtil dialog = new DialogUtil(context, "更新", flag,
//                new DialogUtil.OnCancleAndConfirmListener() {
//
//                    @Override
//                    public void confirm() {
//                        // TODO Auto-generated method stub
//                        Intent intent = new Intent();
//                        intent.setData(Uri.parse(info.getDown_url()));
//                        intent.setAction(Intent.ACTION_VIEW);
//                        context.startActivity(intent);
//                    }
//
//                    @Override
//                    public void cancle() {
//                        // TODO Auto-generated method stub
//
//                    }
//                });
//        StringBuilder describe = new StringBuilder();
//        String des_str = info.getVersion_desc();
//        if (des_str.indexOf(";") > 0) {
//            des_str = des_str.replace(";", "\n");
//        }
//        describe.append(des_str);
//        TextView content = new TextView(context);
//        content.setText(describe.toString());
//        dialog.setContent(content);
//    }
}
