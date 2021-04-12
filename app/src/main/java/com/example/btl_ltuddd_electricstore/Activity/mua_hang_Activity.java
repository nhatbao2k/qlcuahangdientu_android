package com.example.btl_ltuddd_electricstore.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.btl_ltuddd_electricstore.NetworkChangeReciever;
import com.example.btl_ltuddd_electricstore.R;
import com.example.btl_ltuddd_electricstore.notification;
import com.example.btl_ltuddd_electricstore.object.GioHang;
import com.example.btl_ltuddd_electricstore.object.LichSuMuaHang;
import com.example.btl_ltuddd_electricstore.object.TenUser;
import com.example.btl_ltuddd_electricstore.object.ThongBao;
import com.example.btl_ltuddd_electricstore.service.service_notification;
import com.example.btl_ltuddd_electricstore.service.service_notification_kh;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class mua_hang_Activity extends AppCompatActivity {

    EditText edit_hoTen, edit_diachi, edit_sdt;
    TextView txt_gia;
    Button btn_xacnhan;
    DatabaseReference mData;
    ArrayList<GioHang> gioHangArrayList;
    public long tongTien;
    BroadcastReceiver broadcastReceiver;
    private static final int NOTIFICATION_ID = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mua_hang_);


        mData = FirebaseDatabase.getInstance().getReference();
        AnhXa();
        LoadData();
        xacNhanMuaHang();
        returnGioHang();

        broadcastReceiver = new NetworkChangeReciever();
        registerNetWorkBroadcastReciver();

        //nhan thong bao đon hang
        Intent service = new Intent(this, service_notification_kh.class);
        startService(service);
    }

    private void AnhXa(){
        edit_hoTen = (EditText) findViewById(R.id.Edit_tenkh_muahang);
        edit_diachi = (EditText) findViewById(R.id.Edit_diaChi_muahang);
        edit_sdt = (EditText) findViewById(R.id.Edit_sdt_muahang);
        txt_gia = (TextView) findViewById(R.id.txt_gia_tien_mua_hang);
        btn_xacnhan = (Button) findViewById(R.id.btn_mua_sp);
        gioHangArrayList = new ArrayList<>();
        edit_hoTen.setText(TenUser.name);
    }

    private void returnGioHang() {
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar_muahang);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //lay du lieu tu firebase ve arrayList
    private void LoadData(){
        mData.child("GioHang").child(TenUser.ID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                GioHang gioHang = snapshot.getValue(GioHang.class);
                gioHangArrayList.add(gioHang);
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
                tongTien = 0;
                for (int i = 0; i < gioHangArrayList.size(); i++){
                    tongTien += Long.parseLong(gioHangArrayList.get(i).getGia());
                }
                txt_gia.setText(tongTien+" vnđ");
            }
        }.start();
    }

    //ktra thong tin nhap vao
    private boolean checkThongtinNhap(){
        if (edit_hoTen.getText().toString().equals("") || edit_diachi.getText().toString().equals("") || edit_sdt.getText().toString().equals("")){
            return false;
        }
        return true;
    }

    private void xacNhanMuaHang(){
        btn_xacnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkThongtinNhap()){
                    if (gioHangArrayList.size() != 0){
                        //tao 1 dialog
                        AlertDialog.Builder builder = new AlertDialog.Builder(mua_hang_Activity.this);
                        builder.setTitle("Thông báo");
                        builder.setMessage("Bạn chắc chắn đã nhập chính xác nội dung");
                        builder.setIcon(R.drawable.ic_andoid);
                        builder.setCancelable(true);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String hoTen = edit_hoTen.getText().toString();
                                String sdt = edit_sdt.getText().toString();
                                String diaChi = edit_diachi.getText().toString();
                                //lay thoi gian va ngay mua hang hien tai
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy _ HH:mm:ss", Locale.getDefault());
                                String date = simpleDateFormat.format(new Date());
                                for (int i = 0; i < gioHangArrayList.size(); i++){// dua tung san pham len firebase sau khi xac nhan thanh toan
                                    String gia = gioHangArrayList.get(i).getGia();
                                    String tensp = gioHangArrayList.get(i).getTenSP();
                                    String hinhAnh = gioHangArrayList.get(i).getHinhAnh();
                                    int soL = gioHangArrayList.get(i).getSoLuong();
                                    LichSuMuaHang lichSuMuaHang = new LichSuMuaHang(TenUser.ID, "Chưa xác nhận", hoTen, sdt, diaChi, tensp, hinhAnh, gia, soL, date,"");
                                    mData.child("LichSuMuaHang").child("ChoXacNhan").push().setValue(lichSuMuaHang, new DatabaseReference.CompletionListener() {
                                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                        @Override
                                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                            if (error == null){
//                                                Toast.makeText(mua_hang_Activity.this, "Xác nhận đơn hàng đã được tạo thành công", Toast.LENGTH_SHORT).show();
                                                sendNotification();
                                                //xoa du lieu o trong gio hang
                                                mData.child("GioHang").child(TenUser.ID).removeValue();
                                                mData.child("ThongBao").child("ChoXacNhan").child(TenUser.ID).setValue(new ThongBao(TenUser.name, TenUser.ID, TenUser.name+" đã đặt hàng","ChoXacNhan", date));
                                                finish();
                                                startActivity(new Intent(mua_hang_Activity.this, banhang_Activity.class));

//                                                txt_gia.setText("");
//                                                edit_diachi.setText("");
//                                                edit_hoTen.setText("");
//                                                edit_sdt.setText("");
                                            }
                                            else {
                                                Toast.makeText(mua_hang_Activity.this, "Thất bại", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                                gioHangArrayList.clear();
                            }
                        });
                        builder.setNegativeButton("Kiểm tra lại", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }else {
                        Toast.makeText(mua_hang_Activity.this, "Giỏ hàng của bạn không có gì, vui lòng kiểm tra lại", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(mua_hang_Activity.this, "Nhập thiếu thông tin, vui lòng kiểm tra lại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void sendNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, notification.CHANNEL_ID);
        builder.setContentTitle("Thông báo");
        builder.setContentText("Bạn đã đặt hàng thành công");
        builder.setSmallIcon(R.drawable.ic_andoid);
        builder.setColor(getResources().getColor(R.color.colorAccent));
        Notification notification = builder.build();
        // goi service thong bao

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null){
            notificationManager.notify(getNotificationId(), notification);
        }
    }

    private int getNotificationId(){
        return (int) new Date().getTime();
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