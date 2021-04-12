package com.example.btl_ltuddd_electricstore.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.btl_ltuddd_electricstore.R;
import com.example.btl_ltuddd_electricstore.object.Chat;

import java.util.List;

public class DanhSachKHAdapter extends BaseAdapter {

    private Context context;
    private List<Chat> chatList;
    private int layout;

    public DanhSachKHAdapter(Context context, List<Chat> chatList, int layout) {
        this.context = context;
        this.chatList = chatList;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return chatList.size();
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
        TextView txt_id;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder viewHolder;

        if (convertView == null){
            convertView = inflater.inflate(layout,null);
            viewHolder = new ViewHolder();
            viewHolder.txt_id = (TextView) convertView.findViewById(R.id.txt_ds_id);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txt_id.setText(chatList.get(position).getId()+"");

        return convertView;
    }
}
