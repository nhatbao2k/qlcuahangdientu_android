package com.example.btl_ltuddd_electricstore.Adapter;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.btl_ltuddd_electricstore.R;
import com.example.btl_ltuddd_electricstore.Activity.gio_hang_Activity;
import com.example.btl_ltuddd_electricstore.object.GioHang;
import com.example.btl_ltuddd_electricstore.object.TenUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GioHangAdapter extends BaseAdapter {

    private gio_hang_Activity context;
    private List<GioHang> gioHangList;
    private int layout;

    DatabaseReference mData;
    public String key;
    public long giaBanDau;
    private long sumPrice = 0;

    public GioHangAdapter(gio_hang_Activity context, List<GioHang> gioHangList, int layout) {
        this.context = context;
        this.gioHangList = gioHangList;
        this.layout = layout;
        /*long temp = 0;
        for (int i=0; i< gioHangList.size(); i++){
            temp += Long.parseLong(gioHangList.get(i).getGia());


        }
        this.sumPrice = temp;*/
    }

    /*public long getSumPrice() {

        return this.sumPrice;
    }*/

    @Override
    public int getCount() {
        return gioHangList.size();
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
        ImageView img, img_delete;
        TextView txt_tenSP, txt_gia;
        ElegantNumberButton elegantNumberButton;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder viewHolder;

        if (convertView == null){
            convertView = inflater.inflate(layout,null);
            viewHolder = new ViewHolder();

            viewHolder.img = (ImageView) convertView.findViewById(R.id.img_anh_item_gio_hang);
            viewHolder.txt_tenSP = (TextView) convertView.findViewById(R.id.txt_tensp_item_giohang);
            viewHolder.txt_gia = (TextView) convertView.findViewById(R.id.txt_gia_item_gio_hang);
            viewHolder.img_delete = (ImageView) convertView.findViewById(R.id.img_delete_item);
            viewHolder.elegantNumberButton = (ElegantNumberButton) convertView.findViewById(R.id.number_sum);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Picasso.get().load(gioHangList.get(position).getHinhAnh()).into(viewHolder.img);
        viewHolder.txt_tenSP.setText(gioHangList.get(position).getTenSP());
        viewHolder.txt_gia.setText(gioHangList.get(position).getGia()+" vnđ");
        viewHolder.elegantNumberButton.setNumber(gioHangList.get(position).getSoLuong()+"");

        String ma = gioHangList.get(position).getMaSP();
        //tìm mã key để xóa sp
        mData = FirebaseDatabase.getInstance().getReference();

        viewHolder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mData.child("GioHang").child(TenUser.ID).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        GioHang gioHang = snapshot.getValue(GioHang.class);
                        if (ma.equals(gioHang.getMaSP())){
                            key = snapshot.getKey();
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
                new CountDownTimer(400, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        context.showDialogXoaItem(key);
                    }
                }.start();
            }
        });
        viewHolder.elegantNumberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                String num = viewHolder.elegantNumberButton.getNumber();
                mData.child("GioHang").child(TenUser.ID).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        GioHang gioHang = snapshot.getValue(GioHang.class);

                        if (viewHolder.txt_tenSP.getText().toString().equals(gioHang.getTenSP())){
                            GioHang gioHang1;
                            gioHang1 = new GioHang(gioHangList.get(position).getMaSP(), gioHangList.get(position).getTenSP(), Long.parseLong(gioHangList.get(position).getGiaMacDinh()) * Long.parseLong(num)+"", gioHangList.get(position).getHinhAnh(), Integer.parseInt(num),gioHangList.get(position).getGiaMacDinh());
                            mData.child("GioHang").child(TenUser.ID).child(snapshot.getKey()).setValue(gioHang1);
                            Log.i("DDD",num);
                            viewHolder.txt_gia.setText(Long.parseLong(gioHangList.get(position).getGiaMacDinh()) * Long.parseLong(num)+" vnđ");
                            tinhTongTien();
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

        return convertView;
    }

    long tien;
    // tinh tong tien
    public String tinhTongTien(){
        tien = 0;

        mData.child("GioHang").child(TenUser.ID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                GioHang gioHang = snapshot.getValue(GioHang.class);
                tien += Long.parseLong(gioHang.getGia());
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
        new CountDownTimer(500, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                context.giaThayDoi(tien);
            }
        }.start();
        return tien+"";
    }
}
