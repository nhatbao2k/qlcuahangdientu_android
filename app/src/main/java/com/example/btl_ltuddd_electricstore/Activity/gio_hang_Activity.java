package com.example.btl_ltuddd_electricstore.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.btl_ltuddd_electricstore.Adapter.GioHangAdapter;
import com.example.btl_ltuddd_electricstore.NetworkChangeReciever;
import com.example.btl_ltuddd_electricstore.R;
import com.example.btl_ltuddd_electricstore.object.GioHang;
import com.example.btl_ltuddd_electricstore.object.TenUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class gio_hang_Activity extends AppCompatActivity {

    ArrayList<GioHang> gioHangArrayList;
    ListView listView;
    GioHangAdapter gioHangAdapter;
    ImageView img_delete_item;
    DatabaseReference mData;
    TextView txt_gia;
    long tonggia = 0;
    Button btn_muahang;
    BroadcastReceiver broadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);

        toolbarReturn();
        AnhXa();


        mData = FirebaseDatabase.getInstance().getReference();

        // su dung try catch de tranh chuong trinh bi out khi chua load duoc du lieu, vi load du lieu tu firebase thuong se bi tre
        try {
            // hien thi thong tin san pham
            hienTSP();
        }catch (Exception e){

        }
        new CountDownTimer(500, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                gioHangAdapter = new GioHangAdapter(gio_hang_Activity.this, gioHangArrayList, R.layout.item_gio_hang);
                listView.setAdapter(gioHangAdapter);
            }
        }.start();
        // lay tong tien cua tat ca cac san pham hien thi len textview
        new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
//                Log.i("size", gioHangArrayList.size()+"");
                for (int i = 0; i< gioHangArrayList.size(); i++){
//                    Log.i("A22222",Long.parseLong(gioHangArrayList.get(i).getGia())+"");
                    tonggia += Long.parseLong(gioHangArrayList.get(i).getGia());
                }
//                Log.i("A1",tonggia+"");
                txt_gia.setText(tonggia + " vnđ");
            }
        }.start();

        // thuc hien mua san pham va chuyen sang activity mua hang
        btn_muahang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(gio_hang_Activity.this);
                setContentView(R.layout.progress_bar);
                dialog.show();
                new CountDownTimer(1000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        dialog.dismiss();
                        startActivity(new Intent(gio_hang_Activity.this, mua_hang_Activity.class));
                    }
                }.start();
            }
        });

        //
        broadcastReceiver = new NetworkChangeReciever();
        registerNetWorkBroadcastReciver();
    }

    //tạo sự kiện quay trở lại trang chính
    private void toolbarReturn(){
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar_giohang);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void AnhXa(){
        gioHangArrayList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.lv_gio_san_pham);
        img_delete_item = (ImageView) findViewById(R.id.img_delete_item);
        txt_gia = (TextView) findViewById(R.id.txt_gia_giohang);
        btn_muahang = (Button) findViewById(R.id.btn_mua_giohang);
    }

    //hiển thị các sản phẩm đã được thêm vào giỏ hàng
    private void hienTSP(){
        mData.child("GioHang").child(TenUser.ID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                GioHang gioHang = snapshot.getValue(GioHang.class);
                gioHangArrayList.add(gioHang);
//                gioHangAdapter.notifyDataSetChanged();
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

    // xóa item của giỏ hàng
    public void showDialogXoaItem(String key){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_yes_no);
        dialog.show();

        Button btn_ok = (Button) dialog.findViewById(R.id.btn_yes);
        Button btn_no = (Button) dialog.findViewById(R.id.btn_no);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mData.child("GioHang").child(TenUser.ID).child(key).removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        if (error == null){
                            Toast.makeText(gio_hang_Activity.this,"Xóa giỏ hàng thành công",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(gio_hang_Activity.this,"Xóa thất bại",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.dismiss();
                Intent intent = getIntent();
                startActivity(intent);
            }
        });

        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    //tong tien tien thay doi qua so luong
    public void giaThayDoi(long a){
        txt_gia.setText(a+" vnđ");
        Log.i("aaaa", a+"");
    }

    protected void registerNetWorkBroadcastReciver(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    protected void unregisterNetwork(){
        try {
            unregisterReceiver(broadcastReceiver);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterNetwork();
    }
}