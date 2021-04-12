package com.example.btl_ltuddd_electricstore.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.btl_ltuddd_electricstore.object.ItemMenuKhachHang;
import com.example.btl_ltuddd_electricstore.R;

import java.util.List;

public class MenuKhachHangAdapter extends BaseAdapter {
    private Context context;
    private int layout;

    private List<ItemMenuKhachHang> list;

    public MenuKhachHangAdapter(Context context, int layout, List<ItemMenuKhachHang> list) {
        this.context = context;
        this.layout = layout;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private  class ViewHolder{
        TextView tv;
        ImageView img;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout,null);
            viewHolder = new ViewHolder();

            viewHolder.tv = (TextView)convertView.findViewById(R.id.tvItem);
            viewHolder.img = (ImageView)convertView.findViewById(R.id.imgicon);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv.setText(list.get(position).tenItem);
        viewHolder.img.setImageResource(list.get(position).icon);
        return convertView;
    }
}
