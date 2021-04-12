package com.example.btl_ltuddd_electricstore.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.btl_ltuddd_electricstore.R;
import com.example.btl_ltuddd_electricstore.Activity.thongtin_sp_Activity;
import com.example.btl_ltuddd_electricstore.object.Chat;
import com.example.btl_ltuddd_electricstore.object.TenUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ChatAdapter extends BaseAdapter {

    private thongtin_sp_Activity context;
    private List<Chat> chatList;
    private int layout;

    public ChatAdapter(thongtin_sp_Activity context, List<Chat> chatList, int layout) {
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
        TextView txt_ten, txt_comment;
        ImageView imageView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder viewHolder;
        if (convertView == null){
            convertView = inflater.inflate(layout,null);
            viewHolder = new ViewHolder();

            viewHolder.txt_ten = (TextView) convertView.findViewById(R.id.txt_ten_nguoibinhluan);
            viewHolder.txt_comment = (TextView) convertView.findViewById(R.id.txt_binhluan);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.img_anh_binh_luan);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (chatList.get(position).getAnh().length() != 0){
            viewHolder.txt_ten.setText(chatList.get(position).getTen()+": ");
            viewHolder.txt_comment.setText(chatList.get(position).getComment());
            Picasso.get().load(chatList.get(position).getAnh()).resize(300,300).into(viewHolder.imageView);
        }else {
            viewHolder.txt_ten.setText(chatList.get(position).getTen()+": ");
            viewHolder.txt_comment.setText(chatList.get(position).getComment());
        }

        //xoa binh luan
        viewHolder.txt_ten.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Thông báo");
                builder.setMessage("Bạn muỗn xóa bình luận này");
                builder.setCancelable(true);
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        reference.child("BinhLuan").child(context.getMal()).child(context.getma()).addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                Chat chat= snapshot.getValue(Chat.class);
                                if(chat.getId()==chatList.get(position).getId()){
//                                    reference.child("BinhLuan").child(context.getMal()).child(context.getma()).child(snapshot.getKey()).removeValue();
                                    context.XoaBinhLuan(snapshot.getKey());
                                }
                            }

                            @Override
                            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                            }

                            @Override
                            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                            }

                            @Override
                            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            return false;
            }
        });
        return convertView;
    }
}
