package com.example.btl_ltuddd_electricstore.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.btl_ltuddd_electricstore.R;
import com.example.btl_ltuddd_electricstore.object.ItemMenuQL;

import java.util.List;

public class MenuQlAdapter extends BaseAdapter {

    private Context context;
    private List<ItemMenuQL> list;
    private int layout;

    public MenuQlAdapter(Context context, List<ItemMenuQL> list, int layout) {
        this.context = context;
        this.list = list;
        this.layout = layout;
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

    private class ViewHolder{
        ImageView img;
        TextView tv;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout,null);

            viewHolder = new ViewHolder();
            viewHolder.img = (ImageView) convertView.findViewById(R.id.imgIconQl);
            viewHolder.tv = (TextView) convertView.findViewById(R.id.tvItemQl);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.img.setImageResource(list.get(position).icon);
        viewHolder.tv.setText(list.get(position).tenIcon);
        return convertView;
    }
}
