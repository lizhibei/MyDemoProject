package out.com.network.httpurl;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import out.com.network.util.ImageUtil;
import out.com.network.util.StreamUtil;


public abstract class CallBackUtil<T> {
    static Handler mMainHandler =new Handler(Looper.getMainLooper());

    public void onProgress(float progress,long total){}

    public void onError(final RealResponse response){
        final String errorMessage;
        if(response.inputStream!=null){
            errorMessage=StreamUtil.convertInputStreamToStringByBR(response.inputStream);
        }else if(response.errorStream!=null){
            errorMessage=StreamUtil.convertInputStreamToStringByBR(response.errorStream);
        }else if(response.exception!=null){
            errorMessage=response.exception.getMessage();
        }else {
            errorMessage="";
        }
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                onFailure(response.code,errorMessage);
            }
        });
    }

    public void onSuccess(RealResponse response){
        final T obj=onParseResponse(response);
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                onResponse(obj);
            }
        });
    }

    /**
     * 解析response，执行在子线程
     */
    public abstract T onParseResponse(RealResponse response);

    /**
     * 访问网络失败后被调用，执行在UI线程
     */
    public abstract void onFailure(int code,String errorMessage);

    public abstract void onResponse(T response);

    /**
     * 默认回调
     */
    public static abstract class CallBackDefault extends CallBackUtil<RealResponse>{
        @Override
        public RealResponse onParseResponse(RealResponse response) {
            return response;
        }
    }

    /**
     * 字符串类型 回调
     */
    public static abstract class CallBackString extends CallBackUtil<String>{
        @Override
        public String onParseResponse(RealResponse response) {
            try {
                return StreamUtil.convertInputStreamToStringByBR(response.inputStream);
            } catch (Exception e) {
                throw new RuntimeException("failure");
            }
        }
    }

    /**
     * 位图回调
     */
    public static abstract class CallBackBitmap extends CallBackUtil<Bitmap>{
        private int mTargetWidth;
        private int mTargetHeight;

        public CallBackBitmap(){};

        public CallBackBitmap(int mTargetWidth, int mTargetHeight) {
            this.mTargetWidth = mTargetWidth;
            this.mTargetHeight = mTargetHeight;
        }

        public CallBackBitmap(ImageView imageView){
            int width=imageView.getWidth();
            int height=imageView.getHeight();
            if(width<=0||height<=0){
                throw new RuntimeException("无法获取ImageView的width或height");
            }
            mTargetWidth=width;
            mTargetHeight=height;
        }

        @Override
        public Bitmap onParseResponse(RealResponse response) {
            return ImageUtil.getZoomBitmap(response.inputStream,mTargetWidth,mTargetHeight);
        }
    }

    /**
     * 下载文件时回调
     */
    public static abstract class CallBackFile extends CallBackUtil<File>{

        private String mDestFileDir;  //文件目录
        private String mdestFileName; //文件名

        public CallBackFile(String mDestFileDir, String mdestFileName) {
            this.mDestFileDir = mDestFileDir;
            this.mdestFileName = mdestFileName;
        }

        @Override
        public File onParseResponse(RealResponse response) {
            InputStream is=null;
            byte[] buf=new byte[1024*8];
            int len=0;
            FileOutputStream fos=null;
            try {
                is=response.inputStream;
                final long total=response.contentLength;
                long sum=0;
                File dir=new File(mDestFileDir);
                if(!dir.exists()){
                    dir.mkdirs();
                }
                File file=new File(dir,mdestFileName);
                fos=new FileOutputStream(file);
                while ((len=is.read(buf))!=-1){
                    sum+=len;
                    fos.write(buf,0,len);
                    final long finalsum=sum;
                    mMainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            onProgress(finalsum*100.0f/total,total);
                        }
                    });
                }
                fos.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                try {
                    if(fos!=null){
                        fos.close();
                    }
                    if(is!=null){
                        is.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }
}
