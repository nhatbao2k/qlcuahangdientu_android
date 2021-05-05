package com.example.btl_ltuddd_electricstore.Activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_ltuddd_electricstore.NetworkChangeReciever;
import com.example.btl_ltuddd_electricstore.ChuyenDong.OnSwipeTouchListener;
import com.example.btl_ltuddd_electricstore.R;

import com.example.btl_ltuddd_electricstore.object.TenUser;
import com.example.btl_ltuddd_electricstore.object.ThongTinCaNhan;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class dang_nhap_Activity extends AppCompatActivity {

    Button bt_dn,bt_dk, btn_quen_mk;
    EditText edit_tkdn, edit_mkdn;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    public String ten = "";
    public String quyen = "";
    public String nameID = "";
    public String gmail = "";
    BroadcastReceiver broadcastReceiver;
    ImageView imageView;
    TextView textView;
    int count = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.dang_nhap_layout);
        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);


        bt_dk = (Button) findViewById(R.id.btn_dk);
        bt_dk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(dang_nhap_Activity.this, dang_ky_Activity.class));
                finish();
            }
        });


        edit_tkdn = (EditText) findViewById(R.id.Edit_email_DN);
        edit_mkdn = (EditText) findViewById(R.id.edit_mkDN);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        bt_dn = (Button) findViewById(R.id.btn_DN);

        bt_dn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit_tkdn.getText().toString().length() != 0 &&  edit_mkdn.getText().toString().length() != 0){
                    String email = edit_tkdn.getText().toString();
                    //lay quyen de xac minh dang nhap
                    mDatabase.child("ThongTinCaNhan").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            ThongTinCaNhan thongTinCaNhan = snapshot.getValue(ThongTinCaNhan.class);
                            if (email.equals(thongTinCaNhan.getEmail())){
                                quyen = thongTinCaNhan.getChucVu();
                                ten = thongTinCaNhan.getHoTen();
                                nameID = thongTinCaNhan.getChungMinhNhanDan();
                                gmail = thongTinCaNhan.getEmail();
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
                    Dialog dialog = new Dialog(dang_nhap_Activity.this);
                    dialog.setContentView(R.layout.progress_bar);
                    dialog.show();

                    //thực hiện đăng nhập với signInWithEmailAndPassword
                    new CountDownTimer(1000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            dialog.dismiss();
                        }
                    }.start();
                    new CountDownTimer(200, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            try {
                                dangNhap();
                            }catch (Exception e){

                            }
                        }
                    }.start();
                }
                else {
                    Toast.makeText(dang_nhap_Activity.this, "Chưa nhập thông tin tài khoản",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //chuyen sang trang quen mk
        btn_quen_mk = (Button) findViewById(R.id.btn_quenMk);
        btn_quen_mk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(dang_nhap_Activity.this, quen_mat_khau_Activity.class));
            }
        });

        //
        broadcastReceiver = new NetworkChangeReciever();
        registerNetWorkBroadcastReciver();

        //
        imageView.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
            public void onSwipeTop() {
            }

            public void onSwipeRight() {
                if (count == 0) {
                    imageView.setImageResource(R.drawable.good_night_img);
                    textView.setText("Night");
                    count = 1;
                } else {
                    imageView.setImageResource(R.drawable.good_morning_img);
                    textView.setText("Morning");
                    count = 0;
                }
            }

            public void onSwipeLeft() {
                if (count == 0) {
                    imageView.setImageResource(R.drawable.good_night_img);
                    textView.setText("Night");
                    count = 1;
                } else {
                    imageView.setImageResource(R.drawable.good_morning_img);
                    textView.setText("Morning");
                    count = 0 ;
                }
            }

            public void onSwipeBottom() {
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

    private void dangNhap(){
        String email = edit_tkdn.getText().toString();
        String password = edit_mkdn.getText().toString();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            TenUser.name = ten;
                            TenUser.ID = nameID;
                            TenUser.quyen = quyen;
                            TenUser.email = gmail;
                            // Sign in success, update UI with the signed-in user's information
                            new CountDownTimer(500, 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {

                                }

                                @Override
                                public void onFinish() {
                                    if (quyen.equals("Khách hàng")){
                                        Intent intent = new Intent(dang_nhap_Activity.this, banhang_Activity.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                    if (quyen.equals("Nhân viên")){
                                        startActivity(new Intent(dang_nhap_Activity.this, quan_ly_ban_hang_Activity.class));
                                        finish();
                                    }
                                }
                            }.start();
                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(dang_nhap_Activity.this,"Đăng nhập thất bại",Toast.LENGTH_SHORT).show();
                            // ...
                        }
                    }
                });
    }

}
