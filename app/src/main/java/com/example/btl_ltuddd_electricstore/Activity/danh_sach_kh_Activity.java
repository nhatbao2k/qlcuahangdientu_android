package com.example.btl_ltuddd_electricstore.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.btl_ltuddd_electricstore.Adapter.DanhSachKHAdapter;
import com.example.btl_ltuddd_electricstore.NetworkChangeReciever;
import com.example.btl_ltuddd_electricstore.R;
import com.example.btl_ltuddd_electricstore.object.Chat;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;

public class danh_sach_kh_Activity extends AppCompatActivity {

    ListView lv;
    DanhSachKHAdapter danhSachKHAdapter;
    ArrayList<Chat> chatArrayList;
    DatabaseReference mData;
    BroadcastReceiver broadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_kh_);

        mData = FirebaseDatabase.getInstance().getReference();
        AnhXa();

        danhSachKHAdapter = new DanhSachKHAdapter(danh_sach_kh_Activity.this, chatArrayList, R.layout.item_kh);
        lv.setAdapter(danhSachKHAdapter);

        DanhSachKH();
        ClickItemChat();

        returnQL();
        //ktra ket noi mang
        broadcastReceiver = new NetworkChangeReciever();
        registerNetWorkBroadcastReciver();
    }

    private void returnQL() {
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar_kh);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void AnhXa(){
        lv = (ListView) findViewById(R.id.lv_ds_kh);
        chatArrayList = new ArrayList<>();
    }

    private void DanhSachKH(){
        ArrayList<String> key = new ArrayList<>();
        mData.child("HoTroChat").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                key.add(snapshot.getKey());
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
                for (int i = 0; i < key.size(); i++){
                    chatArrayList.add(new Chat(Long.parseLong(key.get(i)),"","",""));
                    danhSachKHAdapter.notifyDataSetChanged();
                }
            }
        }.start();
    }

    private void ClickItemChat(){
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Chat chat = chatArrayList.get(position);
                Intent intent = new Intent(danh_sach_kh_Activity.this, chat_ho_tro_kh_Activity.class);
                intent.putExtra("getid",String.valueOf(chatArrayList.get(position).getId()));
                startActivity(intent);
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