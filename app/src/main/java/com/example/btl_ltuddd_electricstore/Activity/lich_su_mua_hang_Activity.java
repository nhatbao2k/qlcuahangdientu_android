package com.example.btl_ltuddd_electricstore.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.btl_ltuddd_electricstore.Adapter.LichSuAdapter;
import com.example.btl_ltuddd_electricstore.NetworkChangeReciever;
import com.example.btl_ltuddd_electricstore.R;
import com.example.btl_ltuddd_electricstore.object.LichSuMuaHang;
import com.example.btl_ltuddd_electricstore.object.PhuongThucDonHang;
import com.example.btl_ltuddd_electricstore.object.TenUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class lich_su_mua_hang_Activity extends AppCompatActivity {

    TextView txt_cho_xn,txt_giao_hang, txt_nhan_hang, txt_huy_hang, txt_tra_hang;
    ArrayList<LichSuMuaHang> lichSuMuaHangArrayList;
    ListView lv;
    LichSuAdapter lichSuAdapter;
    ArrayList<LichSuMuaHang> dao;
    DatabaseReference mData;
    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lich_su_mua_hang);

        mData = FirebaseDatabase.getInstance().getReference();
        AnhXa();
        returnProfile();
        try {
            // hien thi don hang dang cho xac nhan
            HTXacNhan();
            // hien thi don hang da huy
            HTDaHuy();
            // hien thi don hang dang duoc giao
            HTDangGiao();
            // hien thi don hang da duoc nhan
            HTDaGiao();
            //hien thi nhung don hang dcuoc tra lai
            HTTraHang();
        }catch (Exception e){

        }
        //
        broadcastReceiver = new NetworkChangeReciever();
        registerNetWorkBroadcastReciver();
    }

    private void returnProfile(){
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar_lichsu);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void AnhXa(){
        txt_cho_xn = (TextView) findViewById(R.id.txt_cho_xac_nhan);
        txt_giao_hang = (TextView) findViewById(R.id.txt_dang_giao);
        txt_nhan_hang = (TextView) findViewById(R.id.txt_da_giao);
        txt_huy_hang = (TextView) findViewById(R.id.txt_da_huy);
        txt_tra_hang = (TextView) findViewById(R.id.txt_tra_hang);
        lichSuMuaHangArrayList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.lv_lich_su_mua_hang);
        dao =  new ArrayList<>();
    }

    private void LoadDL(String a){
        lichSuMuaHangArrayList.clear();
        dao.clear();
        mData.child("LichSuMuaHang").child(a).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                LichSuMuaHang lichSuMuaHang = snapshot.getValue(LichSuMuaHang.class);
                lichSuMuaHangArrayList.add(new LichSuMuaHang(lichSuMuaHang.getCMT(), lichSuMuaHang.getTrangThai(), lichSuMuaHang.getTenKH(), lichSuMuaHang.getSdt(),lichSuMuaHang.getDiaChi(),lichSuMuaHang.getTenSP(),lichSuMuaHang.getHinhAnh(),lichSuMuaHang.getGia(), lichSuMuaHang.getSoLuong(), lichSuMuaHang.getNgayMua(), lichSuMuaHang.getNgayXacNhan()));
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
                try {
                    for (int i = 0; i < lichSuMuaHangArrayList.size(); i ++){
                        if (TenUser.ID.equals(lichSuMuaHangArrayList.get(i).getCMT())){
                            dao.add(lichSuMuaHangArrayList.get(i));
                        }
                    }
                    lichSuAdapter = new LichSuAdapter(lich_su_mua_hang_Activity.this, dao, R.layout.item_lich_su_mua_hang);
                    lv.setAdapter(lichSuAdapter);
                }catch (Exception e){

                }
//                        Log.i("ls", lichSuMuaHangArrayList.size()+"");
            }
        }.start();
    }
    public String child;
    private void HTXacNhan(){
        txt_cho_xn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhuongThucDonHang.phuongThuc = "ChoXacNhan";
                child = PhuongThucDonHang.phuongThuc;
                LoadDL(child);
            }
        });
    }

    private void HTDangGiao(){
        txt_giao_hang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhuongThucDonHang.phuongThuc = "DangGiao";
                child = PhuongThucDonHang.phuongThuc;
                LoadDL(child);
            }
        });
    }

    private void HTDaHuy() {
        txt_huy_hang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhuongThucDonHang.phuongThuc = "DaHuy";
                child = PhuongThucDonHang.phuongThuc;
                LoadDL(child);
            }
        });
    }

    private void HTDaGiao(){
        txt_nhan_hang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhuongThucDonHang.phuongThuc = "DaGiao";
                child = PhuongThucDonHang.phuongThuc;
                LoadDL(child);
            }
        });
    }

    private void HTTraHang(){
        txt_tra_hang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhuongThucDonHang.phuongThuc = "TraHang";
                child = PhuongThucDonHang.phuongThuc;
                LoadDL(child);
            }
        });
    }

    public void XoaDonHang(String key){
        if (key.length() != 0){
            mData.child("LichSuMuaHang").child("ChoXacNhan").child(key).removeValue();
            LoadDL(child);
        }
    }

    public void ThongBaoMuaLaiHang(){
        Toast.makeText(lich_su_mua_hang_Activity.this, "Bạn đã tạo đơn hàng thành công, vào xác nhận đơn hàng để kiểm tra lại",Toast.LENGTH_SHORT).show();
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