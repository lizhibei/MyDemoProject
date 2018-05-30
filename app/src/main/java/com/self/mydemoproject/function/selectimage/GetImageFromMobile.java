package com.self.mydemoproject.function.selectimage;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.log;
import com.self.mydemoproject.R;
import com.self.mydemoproject.activity.BaseActivity;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * 从手机中选择图片
 * @author lizhi
 *
 */
public class GetImageFromMobile extends BaseActivity implements OnClickListener {

	private TextView head_text_title,right_tv,filename_tv,left_tv;
	private GridView show_image_gv;
	private RelativeLayout bottom_rl;
	
	private Context mContext;
	private ProgressDialog mProgressDialog;
	private HashSet< String> mdirpath=new HashSet<String>();
	private List<ImageFloder> mImageFloders = new ArrayList<ImageFloder>();
	private int totalCount=0,imageNumber=0;
	private int mPicsSize; // 存储文件夹中的图片数量
	private File mImgDir;  //图片数量最多的文件夹
	private List<String> images;  //存储要显示的图片
	private GeImageFromPhoneAdapter adapter;
	private SelectFileAdapter selectAdapter;
	private PopupWindow mPopupWindow;
	private boolean isHaveImage=true;

	private Callback callback=new Callback() {
		
		@Override
		public boolean handleMessage(Message msg) {
			mProgressDialog.dismiss();
			dataToView();
			if(mImgDir!=null){
				filename_tv.setText(mImgDir.getName());
			}
			popwindow();
			return false;
		}
	};	
	private Handler mHandler=new Handler(callback);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.get_image_from_mobile);
		mContext=this;
		initview();
		AndPermission.with(this)
				.requestCode(100)
				.permission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
				.callback(new PermissionListener() {
					@Override
					public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
						if(requestCode==100){
							getImages();
						}
					}

					@Override
					public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
						if(requestCode==100){
							AndPermission.defaultSettingDialog(GetImageFromMobile.this,400).show();
						}
					}
				})
				.rationale(new RationaleListener() {
					@Override
					public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
						AndPermission.rationaleDialog(mContext,rationale).show();
					}
				})
				.start();
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if(keyCode== KeyEvent.KEYCODE_BACK){
			adapter.resetData();
		}
		return super.onKeyDown(keyCode, event);
	}



	private void initview(){
		
		Bundle bundle=getIntent().getExtras();
		if(bundle!=null){
			imageNumber=bundle.getInt("number",0);
		}
		
		head_text_title=(TextView) findViewById(R.id.head_text_title);
		head_text_title.setText("选择图片");    //标题
		
		findViewById(R.id.head_img_left).setOnClickListener(this);   //返回
		left_tv= (TextView) findViewById(R.id.left_tv);
		left_tv.setVisibility(View.VISIBLE);
		left_tv.setText("返回");
		left_tv.setOnClickListener(this);
		
		right_tv=(TextView) findViewById(R.id.right_tv);
		right_tv.setText("完成 0/"+imageNumber);
		right_tv.setVisibility(View.VISIBLE);
		right_tv.setOnClickListener(this);  //完成
		
		show_image_gv=(GridView) findViewById(R.id.show_image_gv);
		filename_tv=(TextView) findViewById(R.id.filename_tv);   //文件名字
		filename_tv.setOnClickListener(this);
		
		bottom_rl=(RelativeLayout) findViewById(R.id.bottom_rl);
	}
	
	/**
	 * 显示选择的数量
	 */
	public void showSelectNumber(int selectnumber){
		if(imageNumber>0&&selectnumber<=imageNumber){
			right_tv.setText("完成 "+selectnumber+"/"+imageNumber);
		}else {
			Toast.makeText(mContext, "已经达到图片上限", Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * 获得显示数据
	 */
	private void dataToView(){
		if(mImgDir==null){
			isHaveImage=false;
			Toast.makeText(mContext, "您手机中没有一张图片",
					Toast.LENGTH_SHORT).show();
			return ;
		}else {
			isHaveImage=true;
		}
		images= Arrays.asList(mImgDir.list());
		adapter=new GeImageFromPhoneAdapter(mContext, images,mImgDir.getAbsolutePath(),imageNumber,this);
		show_image_gv.setAdapter(adapter);
	}

	/**
	 * 获得图片
	 */
	private void getImages(){
		
		if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			Toast.makeText(mContext,"没有外部存储",
					Toast.LENGTH_SHORT).show();
			return ;
		}
		
		mProgressDialog= ProgressDialog.show(mContext, null, "正在加载...");
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				String firstImageName=null;
				
				//查找手机中的jpeg png 格式的图片
				Uri imageUri= MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				ContentResolver contentResolver=GetImageFromMobile.this.getContentResolver();
				Cursor cursor = contentResolver.query(imageUri, null,
						MediaStore.Images.Media.MIME_TYPE + "=? or "
								+ MediaStore.Images.Media.MIME_TYPE + "=?",
						new String[] { "image/jpeg", "image/png" },
						MediaStore.Images.Media.DATE_MODIFIED);
				while(cursor.moveToNext()){
					String path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
					if(firstImageName==null){
						firstImageName=path;
					}
					File parentFile=new File(path).getParentFile();
					if(parentFile==null){
						continue;
					}
					String parentPath=parentFile.getAbsolutePath();
					ImageFloder imageFloder=null;
					if(mdirpath.contains(parentPath)){
						continue;
					}else {
						mdirpath.add(parentPath);
						imageFloder=new ImageFloder();
						imageFloder.setDir(parentPath);
						imageFloder.setFirstImagePath(path);
					}
					String[] ss=parentFile.list(new FilenameFilter() {

						@Override
						public boolean accept(File dir, String filename) {
							if(filename.endsWith(".png")||filename.endsWith("jpg")||filename.endsWith("jpeg")){
								return true;
							}
							return false;
						}
					});
					int picSize=0;
					if(ss!=null&&ss.length>0){
						picSize=ss.length;
					}
					totalCount+=picSize;
					imageFloder.setCount(picSize);
					mImageFloders.add(imageFloder);
					if (picSize > mPicsSize)
					{
						mPicsSize = picSize;
						mImgDir = parentFile;
					}
					
				}
				mdirpath = null;
				mHandler.sendEmptyMessage(0);
			}
		}).start();
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.left_tv||v.getId()==R.id.head_img_left){ //返回
			if(adapter!=null){
				adapter.resetData();
			}
			finish();
		}else if(v.getId()==R.id.right_tv){  //完成
			if(GeImageFromPhoneAdapter.selectdata.size()>0){
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				ArrayList<String> urls=new ArrayList<String>();
				for(int i=0;i<GeImageFromPhoneAdapter.selectdata.size();i++){
					urls.add(GeImageFromPhoneAdapter.selectdata.get(i));
				}
				bundle.putStringArrayList("data", urls);
				intent.putExtras(bundle);
				if(adapter!=null){
					adapter.resetData();
				}
				setResult(RESULT_OK, intent);
				finish();
			}else {
				Toast.makeText(mContext, "选择照片",
						Toast.LENGTH_SHORT).show();
			}
		}else if(v.getId()==R.id.filename_tv){ //选择文件
			if(isHaveImage){
				mPopupWindow.showAsDropDown(bottom_rl,0,0);
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = .3f;
				getWindow().setAttributes(lp);
			}else {
				Toast.makeText(mContext, "未找到您手机中的图片",
						Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	/**
	 * 选择文件夹
	 */
	private void popwindow(){
		mPopupWindow=new PopupWindow(mContext);
		View view= LayoutInflater.from(mContext).inflate(R.layout.listview, null);
		ListView listView=(ListView) view.findViewById(R.id.listview);
		selectAdapter=new SelectFileAdapter(mImageFloders, mContext, mImgDir);
		listView.setBackgroundResource(android.R.color.black);
		listView.setAdapter(selectAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
				
				log.i("item");
				ImageFloder imageFloder= mImageFloders.get(position);
				filename_tv.setText(imageFloder.getName() + "");//选择之后更新文件夹名
				mImgDir=new File(imageFloder.getDir());
				mImgDir = new File(imageFloder.getDir());
				images = Arrays.asList(mImgDir.list(new FilenameFilter()
				{
					@Override
					public boolean accept(File dir, String filename)
					{
						if (filename.endsWith(".jpg") || filename.endsWith(".png")
								|| filename.endsWith(".jpeg"))
							return true;
						return false;
					}
				}));
				adapter=new GeImageFromPhoneAdapter(mContext,
						images,mImgDir.getAbsolutePath(),imageNumber,GetImageFromMobile.this);
				show_image_gv.setAdapter(adapter);
				mPopupWindow.dismiss();
			}
		});
		mPopupWindow.setContentView(view);
		mPopupWindow.setAnimationStyle(R.style.anim_popup_dir);
		
		DisplayMetrics metrics=new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		mPopupWindow.setWidth(metrics.widthPixels);
		mPopupWindow.setHeight((int) (metrics.heightPixels*0.7));
		mPopupWindow.setFocusable(true);
		mPopupWindow.setTouchable(true);
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss() {
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = 1f;
				getWindow().setAttributes(lp);
			}
		});
		
	}
}
