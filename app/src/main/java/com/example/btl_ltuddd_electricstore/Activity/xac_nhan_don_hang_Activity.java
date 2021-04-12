package com.example.btl_ltuddd_electricstore.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ListView;
import android.widget.Toast;

import com.example.btl_ltuddd_electricstore.Adapter.XacNhanDonHangAdapter;
import com.example.btl_ltuddd_electricstore.NetworkChangeReciever;
import com.example.btl_ltuddd_electricstore.R;
import com.example.btl_ltuddd_electricstore.object.LichSuMuaHang;
import com.example.btl_ltuddd_electricstore.object.PhuongThucDonHang;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class xac_nhan_don_hang_Activity extends AppCompatActivity {

    ArrayList<LichSuMuaHang> lichSuMuaHangArrayList;
    XacNhanDonHangAdapter xacNhanDonHangAdapter;
    ListView lv;
    DatabaseReference mData;
    public String child;
    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xac_nhan_don_hang);

        mData = FirebaseDatabase.getInstance().getReference();
        AnhXa();
        try {
            child = PhuongThucDonHang.phuongThuc;
            if (PhuongThucDonHang.phuongThuc.equals("ChoXacNhan")){
                HTDH(child);
            }
            else if (PhuongThucDonHang.phuongThuc.equals("DaHuy")){
                HTDH(child);
            }else if (PhuongThucDonHang.phuongThuc.equals("DangGiao")){
                HTDH(child);
            }else if (PhuongThucDonHang.phuongThuc.equals("DaGiao")){
                HTDH(child);
            }else if (PhuongThucDonHang.phuongThuc.equals("TraHang")){
                HTDH(child);
            }
        }catch (Exception e){

        }
        returnQuanlyActivity();

        //ktra ket noi mang
        broadcastReceiver = new NetworkChangeReciever();
        registerNetWorkBroadcastReciver();
    }


    private void returnQuanlyActivity() {
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar_xac_nhan_dh);
        setSupportActionBar(toolbar);
        if (child.equals("DaHuy")){
            getSupportActionBar().setTitle("Đơn hàng đã hủy");
        }else if (child.equals("DangGiao")){
            getSupportActionBar().setTitle("Xác nhận giao hàng");
        }else if (child.equals("DaGiao")){
            getSupportActionBar().setTitle("Hàng đã được giao");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void AnhXa() {
        lichSuMuaHangArrayList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.lv_xac_don);
    }

    // xác nhận các đơn hàng vừa được đặt
    private void HTDH(String a) {
        mData.child("LichSuMuaHang").child(a).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                LichSuMuaHang lichSuMuaHang = snapshot.getValue(LichSuMuaHang.class);
                lichSuMuaHangArrayList.add(new LichSuMuaHang(lichSuMuaHang.getCMT(), lichSuMuaHang.getTrangThai(), lichSuMuaHang.getTenKH(),lichSuMuaHang.getSdt(), lichSuMuaHang.getDiaChi(),lichSuMuaHang.getTenSP(),lichSuMuaHang.getHinhAnh(), lichSuMuaHang.getGia(), lichSuMuaHang.getSoLuong(),lichSuMuaHang.getNgayMua(), lichSuMuaHang.getNgayXacNhan()));
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
                xacNhanDonHangAdapter = new XacNhanDonHangAdapter(xac_nhan_don_hang_Activity.this, lichSuMuaHangArrayList, R.layout.item_quan_ly_xac_nhan_hang);
                lv.setAdapter(xacNhanDonHangAdapter);
            }
        }.start();
    }

    public void capNhatLai(String key){
        lichSuMuaHangArrayList.clear();
        if (child.equals("ChoXacNhan")){
            if (key.length() != 0){
                mData.child("LichSuMuaHang").child("ChoXacNhan").child(key).removeValue();
            }else {
                Toast.makeText(xac_nhan_don_hang_Activity.this, "Lỗi"+key, Toast.LENGTH_SHORT).show();
            }
        }else if (child.equals("DangGiao")){
            if (key.length() != 0){
                mData.child("LichSuMuaHang").child("DangGiao").child(key).removeValue();
            }else {
                Toast.makeText(xac_nhan_don_hang_Activity.this, "Lỗi"+key, Toast.LENGTH_SHORT).show();
            }
        }else if (child.equals("DaGiao")){
            if (key.length() != 0){
                mData.child("LichSuMuaHang").child("DaGiao").child(key).removeValue();
            }else {
                Toast.makeText(xac_nhan_don_hang_Activity.this, "Lỗi"+key, Toast.LENGTH_SHORT).show();
            }
        }
        HTDH(child);
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