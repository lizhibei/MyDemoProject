package com.self.mydemoproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.self.mydemoproject.R;

import java.util.ArrayList;

public class HorizontalScrollviewAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Integer> data;
    private LayoutInflater inflater;

    public HorizontalScrollviewAdapter(Context mContext, ArrayList<Integer> data) {
        this.mContext = mContext;
        this.data = data;
        inflater=LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
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
            convertView=inflater.inflate(R.layout.horizontal_scrollview_item,null);
            holder.item_iv=convertView.findViewById(R.id.item_iv);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.item_iv.setImageResource(data.get(position));
        return convertView;
    }

    private class ViewHolder{
        ImageView item_iv;
    }
}
