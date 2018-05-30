package com.self.mydemoproject.function.selectimage;

import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.self.mydemoproject.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GeImageFromPhoneAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<String> date;
	private Context context;
	private boolean[] isSelect;
	private String parentPath;
	public  static List<String> selectdata=new LinkedList<String>();
	private int number;
	private GetImageFromMobile instance;
	
	public GeImageFromPhoneAdapter(Context context, List<String> date, String parentPath,
                                   int number, GetImageFromMobile instance) {
		super();
		this.context=context;
		inflater= LayoutInflater.from(context);
		this.date=date;
		isSelect=new boolean[date.size()];
		this.parentPath=parentPath;
		this.number=number;
		this.instance=instance;
	}

	@Override
	public int getCount() {

		return date.size();
	}

	@Override
	public Object getItem(int position) {

		return date.get(position);
	}

	@Override
	public long getItemId(int arg0) {

		return arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder holder=null;
		if(convertView==null){
			holder=new ViewHolder();
			convertView=inflater.inflate(R.layout.imageitem, null);
			holder.image=(ImageView) convertView.findViewById(R.id.ItemImage);
			holder.selected=(ImageView) convertView.findViewById(R.id.isselected);
			convertView.setTag(holder);
		}else {
			holder=(ViewHolder) convertView.getTag();
		}
//		holder.image.setImageResource(R.drawable.load_biaozhi);
		if(!date.get(position).equals(holder.image.getTag())){
			holder.image.setImageResource(R.mipmap.image);
		}
		DisplayMetrics metrics=new DisplayMetrics();
		instance.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		holder.image.setTag(parentPath+"/"+date.get(position));
		int width=metrics.widthPixels/3-20;
		LayoutParams params=new LayoutParams(width, width);
		holder.image.setLayoutParams(params);
//		ImageLoader1.getInstance().getBitmap(parentPath+"/"+date.get(position), 150,
//				150, holder.image,FileCache1.LOCAL);
        ImageLoader.getInstance().displayImage("file://"+parentPath+"/"+date.get(position),
        		holder.image);
		
		if(selectdata.contains(parentPath+"/"+date.get(position))){
			holder.selected.setImageResource(R.mipmap.icon_select);
			holder.selected.setColorFilter(Color.parseColor("#77000000"));
		}else {
			holder.selected.setImageResource(R.mipmap.icon_unselect);
			holder.selected.setColorFilter(null);
		}
		
		final ImageView imageView=holder.selected;
		final ImageView photo=holder.image;
		holder.image.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
					if(selectdata.contains(parentPath+"/"+date.get(position))){
						imageView.setImageResource(R.mipmap.icon_unselect);
						photo.setColorFilter(null);
						selectdata.remove(parentPath+"/"+date.get(position));
					}else {
						if(number>selectdata.size()){
							selectdata.add(parentPath+"/"+date.get(position));
							imageView.setImageResource(R.mipmap.icon_select);
							photo.setColorFilter(Color.parseColor("#77000000"));
						}else {
							Toast.makeText(context, "已经达到图片上限", Toast.LENGTH_SHORT).show();
						}
						
					}
					instance.showSelectNumber(selectdata.size());
				
				
			}
		});
		return convertView;
	}
	
	class ViewHolder {
		ImageView image,selected;
	}
	
	/**
	 * 更新选择
	 * @param position
	 */
	public void update(int position,boolean isAddGood){
		if(!isAddGood){  //选择店铺头像和背景
			for(int i=0;i<isSelect.length;i++){
				if(i==position){
					isSelect[i]=true;
				}else {
					isSelect[i]=false;
				}
			}
		}else {  //选择商品图
			if(isSelect[position]){
				isSelect[position]=false;
			}else {
				isSelect[position]=true;
			}
		}
		
		notifyDataSetChanged();
	}
	
	/**
	 * 获得选择结果
	 * @return
	 */
	public ArrayList<Integer> getSelectResult(){
		ArrayList<Integer> result=new ArrayList<Integer>();
		for(int i=0;i<isSelect.length;i++){
			if(isSelect[i]==true){
				result.add(i);
			}
		}
		return result;
	}
	
	/**
	 * 清空选择数据
	 */
	public void resetData(){
		selectdata.clear();
	}

}
