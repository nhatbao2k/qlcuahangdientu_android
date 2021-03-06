package com.example.btl_ltuddd_electricstore.Activity;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.btl_ltuddd_electricstore.Fragment.DatePickerFragment;
import com.example.btl_ltuddd_electricstore.NetworkChangeReciever;
import com.example.btl_ltuddd_electricstore.R;
import com.example.btl_ltuddd_electricstore.object.ThongTinCaNhan;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class dang_ky_Activity extends AppCompatActivity implements View.OnFocusChangeListener {

    Button btn_thoat, btn_dangky;
    EditText edit_email,edit_mk1,edit_mk2, edit_hoTen, edit_cmt, eNgaySinhdk;
    RadioGroup gioiTinh;
    RadioButton gTinh;
    DatePickerDialog.OnDateSetListener listener;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    BroadcastReceiver broadcastReceiver;
//    ImageView imageView;
//    TextView textView;
//    int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dang_ky);


        btn_thoat = (Button) findViewById(R.id.btn_Exit);
        btn_dangky = (Button) findViewById(R.id.btn_dongY);
        edit_email = (EditText) findViewById(R.id.Edit_email_dk);
        edit_mk1 = (EditText) findViewById(R.id.edit_pass1);
        edit_mk2 = (EditText) findViewById(R.id.edit_pass2);
        edit_hoTen = (EditText) findViewById(R.id.Edit_hoTen);
        eNgaySinhdk = (EditText) findViewById(R.id.Edit_date);
        edit_cmt = (EditText) findViewById(R.id.Edit_cmND);
        gioiTinh = (RadioGroup) findViewById(R.id.gt);


        eNgaySinhdk.setOnFocusChangeListener(this);


        mDatabase = FirebaseDatabase.getInstance().getReference();
        // thuc hien viec dang ky
        mAuth = FirebaseAuth.getInstance();
        btn_dangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edit_email.getText().toString();
                String ht = edit_hoTen.getText().toString();
                String mk1 = edit_mk1.getText().toString();
                String mk2 = edit_mk2.getText().toString();
                String ngay = eNgaySinhdk.getText().toString();
                String cmt = edit_cmt.getText().toString();
                int selected = gioiTinh.getCheckedRadioButtonId();
                gTinh = (RadioButton) findViewById(selected);
                String gt = gTinh.getText().toString();
                //Ktra nh???p th??ng tin ????y ????? ch??a
                if (email.length() != 0 && mk1.length() != 0 && mk2.length() != 0 && ht.length() != 0 && gt.length() != 0 && ngay.length() != 0 && cmt.length() != 0 ){
                    //Ktra nh???p m???t kh???u
                    if (mk1.equals(mk2) ){//ktra mat khau co trung khop hay khong
                        if (mk1.length() >= 6 ){// yeu cau nhap mat khau toi thieu 6 k?? t??? tr??? l??n m???i cho ph??p ????ng k??

                            try {
                                dangKy();
                            }catch (Exception e){

                            }
                        }
                        else {
                            Toast.makeText(dang_ky_Activity.this,"M???t kh???u t???i thi???u 6 k?? t???",Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(dang_ky_Activity.this, "M???t kh???u kh??ng tr??ng kh???p", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(dang_ky_Activity.this, "Th??ng tin nh???p c??n thi???u", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_thoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(dang_ky_Activity.this, dang_nhap_Activity.class));
            }
        });

        broadcastReceiver = new NetworkChangeReciever();
        registerNetWorkBroadcastReciver();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        int id = v.getId();
        switch (id){
            case R.id.Edit_date:
                if (hasFocus){
                    DatePickerFragment datePickerFragment = new DatePickerFragment();
                    datePickerFragment.show(getSupportFragmentManager(),"Ng??y sinh");
                }
                break;
        }
    }

    // d??ng createUserWithEmailAndPassword T???o t??i kho???n m???i b???ng c??ch chuy???n ?????a ch??? email v?? m???t kh???u c???a ng?????i d??ng m???i
    private void dangKy(){
        String email = edit_email.getText().toString();
        String password = edit_mk1.getText().toString();
        String ht = edit_hoTen.getText().toString();
        String ngay = eNgaySinhdk.getText().toString();
        String cmt = edit_cmt.getText().toString();
        int selected = gioiTinh.getCheckedRadioButtonId();
        gTinh = (RadioButton) findViewById(selected);
        String gt = gTinh.getText().toString();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(dang_ky_Activity.this,"????ng k?? th??nh c??ng",Toast.LENGTH_SHORT).show();
                            mDatabase.child("ThongTinCaNhan").push().setValue(new ThongTinCaNhan(email,ht,gt,ngay,cmt,"Kh??ch h??ng"));
                            startActivity(new Intent(dang_ky_Activity.this, dang_nhap_Activity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(dang_ky_Activity.this,"????ng k?? th???t b???i",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    //
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
