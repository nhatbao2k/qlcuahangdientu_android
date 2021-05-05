package com.example.btl_ltuddd_electricstore.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.btl_ltuddd_electricstore.NetworkChangeReciever;
import com.example.btl_ltuddd_electricstore.R;
import com.example.btl_ltuddd_electricstore.object.TenUser;

public class profile_Activity extends AppCompatActivity {

    Button btn_lienhe, btn_guitinnhan, btn_hotline, btn_lichsu, btn_dangxuat, btn_doi_mk;
    TextView txt_ten_user;
    int ktra = 0;
    private static final int REQUEST_CALL = 1;
    BroadcastReceiver broadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_);

        returnHome();
        AnhXa();
        anHien();
        //tao an button hien ra va bien mat
        chiTietLienHe();
        lsMuaHang();
        // thuc hien cuoc goi
        callOut();
        Chat();
        dangXuat();
        doiPass();

        broadcastReceiver = new NetworkChangeReciever();
        registerNetWorkBroadcastReciver();

    }


    private void doiPass() {
        btn_doi_mk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(profile_Activity.this, doi_pass_Activity.class));
            }
        });
    }

    private void AnhXa() {
        btn_dangxuat = (Button) findViewById(R.id.btn_dangxuat_profile);
        btn_lichsu = (Button) findViewById(R.id.btn_lichsu_muahang);
        btn_guitinnhan = (Button) findViewById(R.id.btn_chat);
        btn_hotline = (Button) findViewById(R.id.btn_hotline);
        btn_lienhe = (Button) findViewById(R.id.btn_lienhe_admin);
        txt_ten_user = (TextView) findViewById(R.id.txt_user_profile);
        txt_ten_user.setText(TenUser.name);
        btn_doi_mk = (Button) findViewById(R.id.btn_dat_lai_mk);
    }

    private void returnHome() {
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar_profilereturn);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(profile_Activity.this, banhang_Activity.class));
            }
        });
    }

    //neu ktr=0 thi lam an button con neu ktra =1 thi hien thi button
    private void anHien(){
        if (ktra == 0){
            btn_hotline.setVisibility(View.GONE);
            btn_guitinnhan.setVisibility(View.GONE);
        }
        if (ktra == 1){
            btn_hotline.setVisibility(View.VISIBLE);
            btn_guitinnhan.setVisibility(View.VISIBLE);
        }
    }
    private void chiTietLienHe(){
        btn_lienhe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ktra == 0){
                    ktra = 1;
                }else {
                    ktra = 0;
                }
                anHien();
            }
        });
    }

    private void lsMuaHang(){
        btn_lichsu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(profile_Activity.this, lich_su_mua_hang_Activity.class));
            }
        });
    }

    //thuc hien cuoc goi
    private void callOut(){
        btn_hotline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(profile_Activity.this);
                builder.setTitle("Thông báo");
                builder.setMessage("Bạn có muốn thực hiện cuộc gọi đến tổng đài");
                builder.setIcon(R.drawable.ic_andoid);
                builder.setCancelable(true);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        goiDien();
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    // tham khao https://codinginflow.com/tutorials/android/in-app-phone-call
    private void goiDien(){
        if (ContextCompat.checkSelfPermission(profile_Activity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(profile_Activity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        }else {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:0985056924"));
            startActivity(intent);
        }
    }
//    Sau khi người dùng phản hồi hộp thoại quyền hệ thống, hệ thống sẽ gọi triển khai ứng dụng của bạn onRequestPermissionsResult()
//    Hệ thống chuyển phản hồi của người dùng tới hộp thoại quyền, cũng như mã yêu cầu mà bạn đã xác định, như được hiển thị trong đoạn mã sau:
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL){
            // Permission is granted. Continue the action or workflow
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                goiDien();
            }else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // vao doan chat voi nha phat trien
    private void Chat(){
        btn_guitinnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(profile_Activity.this, ChatHoTro.class));
            }
        });
    }

    //dang xuat
    private void dangXuat(){
        btn_dangxuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(profile_Activity.this, dang_nhap_Activity.class));
            }
        });
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