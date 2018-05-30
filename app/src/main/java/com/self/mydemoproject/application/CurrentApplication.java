package com.self.mydemoproject.application;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.log;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;


public class CurrentApplication extends Application {
	private static final String TAG = "JPush";
	public static RequestQueue queue;
	public static Context applicationContext;
	private static CurrentApplication instance;
	// login user name
	public final String PREF_USERNAME = "username";

	private Context appContext;

	@Override
	public void onCreate() {

		super.onCreate();
		log.setInLog(log.E);
//		log.isOutput=false;
//		selfStartManagerSettingIntent(this);
//		JPushInterface.init(this);
//		JPushInterface.setDebugMode(true);
		initImageLoader(getApplicationContext());
//		initData();
	}

	private void initData(){

	}

	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				// 线程数 默认5个
				.denyCacheImageMultipleSizesInMemory()
//				.diskCache(new MyDiskCache())
				// 缓存大小
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

//	private void selfStartManagerSettingIntent(Context context){
//
//		Intent intent = new Intent();
//		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		ComponentName componentName = new ComponentName("com.huawei.systemmanager","com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity");
//		intent.setComponent(componentName);
//		try{
//			context.startActivity(intent);
//		}catch (Exception e){//抛出异常就直接打开设置页面
//			intent=new Intent(Settings.ACTION_SETTINGS);
//			context.startActivity(intent);
//		}
//
//	}

}
