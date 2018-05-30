package com.self.mydemoproject.function.selectimage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.log;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.self.mydemoproject.R;

import java.io.File;
import java.util.List;

public class SelectFileAdapter extends BaseAdapter {

	private List<ImageFloder> mImageFloders;
	private LayoutInflater inflater;
	private File mImgDir;
	
	public SelectFileAdapter(List<ImageFloder> mImageFloders, Context context, File mImgDir) {
		super();
		inflater= LayoutInflater.from(context);
		this.mImageFloders=mImageFloders;
		this.mImgDir=mImgDir;
	}

	@Override
	public int getCount() {

		return mImageFloders.size();
	}

	@Override
	public Object getItem(int position) {

		return mImageFloders.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder=null;
		if(convertView==null){
			holder=new ViewHolder();
			convertView=inflater.inflate(R.layout.selectfile_item, null);
			holder.photo_iv=(ImageView) convertView.findViewById(R.id.firstimage_iv);
			holder.select_iv=(ImageView) convertView.findViewById(R.id.select_iv);
			holder.filename_tv=(TextView) convertView.findViewById(R.id.filename_tv);
			holder.number_tv=(TextView) convertView.findViewById(R.id.number_tv);
			convertView.setTag(holder);
		}else {
			holder=(ViewHolder) convertView.getTag();
		}
		
		ImageFloder imageFloder=mImageFloders.get(position);
		if(!imageFloder.getFirstImagePath().equals((String)holder.photo_iv.getTag())){
			holder.photo_iv.setImageResource(R.mipmap.image);
		}
		holder.photo_iv.setTag(imageFloder.getFirstImagePath());
		log.i("path="+imageFloder.getFirstImagePath());
		holder.filename_tv.setText(imageFloder.getName());
		holder.number_tv.setText(""+imageFloder.getCount());
		holder.select_iv.setImageResource(R.mipmap.icon_select);
		if(imageFloder.getName().equals(mImgDir.getName())){
			holder.select_iv.setVisibility(View.VISIBLE);
		}else {
			holder.select_iv.setVisibility(View.GONE);
		}
//		ImageLoader1.getInstance().getBitmap(imageFloder.getFirstImagePath(),
//				80, 80, holder.photo_iv,FileCache1.LOCAL);
		ImageLoader.getInstance().displayImage("file://"+imageFloder.getFirstImagePath(),
				holder.photo_iv);
		return convertView;
	}
	
	private class ViewHolder{
		ImageView photo_iv,select_iv;
		TextView filename_tv,number_tv;
	}

}
