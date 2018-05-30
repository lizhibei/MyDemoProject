package out.com.network.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;

public class ImageUtil {

    /**
     * 获得压缩图片  mTargetWidth  mTargetHeight其中一个为0  将不压缩返回原图的Bitmap
     * @param inputStream
     * @return
     */
    public static Bitmap getZoomBitmap(InputStream inputStream,int mTargetWidth,int mTargetHeight){
        if(mTargetWidth==0||mTargetHeight==0){  //不压缩直接返回Bitmap
           return BitmapFactory.decodeStream(inputStream);
        }
        byte[] data=null;
        try {
            data=StreamUtil.convertInputStreamToByte(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeByteArray(data,0,data.length,options);
        int picWidth=options.outWidth;
        int picHeight=options.outHeight;
        int sampleSize=1;
        int widthRatio=0;
        int heightRatio=0;
        widthRatio=(int) Math.floor((float)picWidth/(float)mTargetWidth);
        heightRatio=(int) Math.floor((float)picHeight/(float)mTargetHeight);
        if(heightRatio>1||widthRatio>1){
            sampleSize=Math.max(heightRatio,widthRatio);
        }
        options.inSampleSize=sampleSize;
        options.inJustDecodeBounds=false;
        Bitmap bitmap=BitmapFactory.decodeByteArray(data,0,data.length,options);
        if(bitmap==null){
            throw new RuntimeException("Failed to decode stream.");
        }
        return bitmap;
    }
}
