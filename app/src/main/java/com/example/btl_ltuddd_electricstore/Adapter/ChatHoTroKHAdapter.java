package com.example.btl_ltuddd_electricstore.Adapter;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.btl_ltuddd_electricstore.Activity.ChatHoTro;
import com.example.btl_ltuddd_electricstore.Activity.chat_ho_tro_kh_Activity;
import com.example.btl_ltuddd_electricstore.R;
import com.example.btl_ltuddd_electricstore.object.Chat;
import com.example.btl_ltuddd_electricstore.object.TenUser;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ChatHoTroKHAdapter extends BaseAdapter {

    private chat_ho_tro_kh_Activity context;
    private List<Chat> chatList;
    private int layout;

    public ChatHoTroKHAdapter(chat_ho_tro_kh_Activity context, List<Chat> chatList, int layout) {
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
        ImageView img_trai, img_phai;
        TextView txt_dong_chat, txt_ten;
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder viewHolder;
        if (convertView == null){
            convertView = inflater.inflate(layout,null);
            viewHolder = new ViewHolder();

            viewHolder.img_trai = (ImageView) convertView.findViewById(R.id.img_trai);
            viewHolder.img_phai = (ImageView) convertView.findViewById(R.id.img_phai);
            viewHolder.txt_dong_chat = (TextView) convertView.findViewById(R.id.txt_hien_thi_chat_lhe);
            viewHolder.txt_ten = (TextView) convertView.findViewById(R.id.txt_ten_chat);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (TenUser.name.equals(chatList.get(position).getTen())){
            viewHolder.txt_dong_chat.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.user, 0);
            viewHolder.txt_dong_chat.setGravity(Gravity.RIGHT);
            viewHolder.txt_ten.setGravity(Gravity.RIGHT);
            viewHolder.img_trai.setVisibility(View.INVISIBLE);
            if (chatList.get(position).getAnh().length() != 0){
                Picasso.get().load(chatList.get(position).getAnh()).resize(300,500).into(viewHolder.img_trai);
                Picasso.get().load(chatList.get(position).getAnh()).resize(300,500).into(viewHolder.img_phai);
                viewHolder.txt_dong_chat.setText(chatList.get(position).getComment());
                viewHolder.txt_ten.setText(chatList.get(position).getTen());
            }else {
                viewHolder.img_phai.setVisibility(View.GONE);
                viewHolder.img_trai.setVisibility(View.GONE);
                viewHolder.txt_dong_chat.setText(chatList.get(position).getComment());
                viewHolder.txt_ten.setText(chatList.get(position).getTen());
            }
        }else {
            viewHolder.txt_dong_chat.setCompoundDrawablesWithIntrinsicBounds(R.drawable.user,0,0, 0);
            viewHolder.txt_dong_chat.setGravity(Gravity.LEFT);
            viewHolder.txt_ten.setGravity(Gravity.LEFT);
            viewHolder.img_phai.setVisibility(View.INVISIBLE);
            if (chatList.get(position).getAnh().length() != 0){
                Picasso.get().load(chatList.get(position).getAnh()).resize(300,500).into(viewHolder.img_trai);
                Picasso.get().load(chatList.get(position).getAnh()).resize(300,500).into(viewHolder.img_phai);
                viewHolder.txt_dong_chat.setText(chatList.get(position).getComment());
                viewHolder.txt_ten.setText(chatList.get(position).getTen());
            }else {
                viewHolder.img_phai.setVisibility(View.GONE);
                viewHolder.img_trai.setVisibility(View.GONE);
                viewHolder.txt_dong_chat.setText(chatList.get(position).getComment());
                viewHolder.txt_ten.setText(chatList.get(position).getTen());
            }
        }
        return convertView;
    }
}
