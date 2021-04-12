package com.example.btl_ltuddd_electricstore.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.btl_ltuddd_electricstore.Adapter.ChatHoTroAdapter;
import com.example.btl_ltuddd_electricstore.Adapter.ChatHoTroKHAdapter;
import com.example.btl_ltuddd_electricstore.NetworkChangeReciever;
import com.example.btl_ltuddd_electricstore.R;
import com.example.btl_ltuddd_electricstore.object.Chat;
import com.example.btl_ltuddd_electricstore.object.TenUser;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class chat_ho_tro_kh_Activity extends AppCompatActivity {

    ImageView img_gui_anh, img_send;
    EditText edit_soan_tin;
    ListView lv;
    DatabaseReference mData;
    ChatHoTroKHAdapter chatHoTroKHAdapter;
    ArrayList<Chat> chatArrayList;
    int REQUEST_CHOSE_IMAGE = 1;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_ho_tro);

        mData = FirebaseDatabase.getInstance().getReference();
        AnhXa();
        returnProfile();
        LayID();
        guiVanBan();
        guiAnh();

        chatHoTroKHAdapter = new ChatHoTroKHAdapter(chat_ho_tro_kh_Activity.this,chatArrayList, R.layout.dong_chat_ho_tro);
        lv.setAdapter(chatHoTroKHAdapter);
        Toast.makeText(chat_ho_tro_kh_Activity.this, TenUser.name+"/"+TenUser.ID, Toast.LENGTH_SHORT).show();
        HTChat();

        broadcastReceiver = new NetworkChangeReciever();
        registerNetWorkBroadcastReciver();
    }

    public String id;
    private void LayID(){
        Intent intent = getIntent();
        id = intent.getStringExtra("getid");
    }
    private void AnhXa() {
        img_gui_anh = (ImageView) findViewById(R.id.img_anh_lienhe);
        img_send = (ImageView) findViewById(R.id.img_gui_tin_nhan_lien_he);
        edit_soan_tin = (EditText) findViewById(R.id.Edit_chat_lien_he);
        lv = (ListView) findViewById(R.id.lv_chat_ho_tro);
        chatArrayList = new ArrayList<>();
    }

    private void returnProfile(){
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar_chat_ho_tro);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    //gui tin nhan van ban
    private void guiVanBan(){
        img_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit_soan_tin.getText().toString().trim().length() > 0){
                    Chat chat = new Chat(Long.parseLong(TenUser.ID), TenUser.name, edit_soan_tin.getText().toString(), "");
                    mData.child("HoTroChat").child(id).push().setValue(chat);
                    edit_soan_tin.setText("");
                }
            }
        });
    }

    //gui hinh anh
    private void guiAnh(){
        img_gui_anh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(intent, REQUEST_CHOSE_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CHOSE_IMAGE && resultCode == RESULT_OK){
            try {
                final StorageReference storageRef = storage.getReferenceFromUrl("gs://cuahangdientu-894b2.appspot.com");
                Calendar calendar = Calendar.getInstance();
                Uri uri = data.getData();
                InputStream is = getContentResolver().openInputStream(uri);
                Picasso.get().load(uri).resize(100,100).into(img_gui_anh);
                if (uri != null){
                    // dua anh len firebase
                    UploadTask uploadTask = storageRef.child("image" + calendar.getTimeInMillis() + ".png").putFile(uri);
                    uploadTask.addOnSuccessListener(chat_ho_tro_kh_Activity.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {// gui anh len firebase
                            Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();// lấy url để download
                            while (!uri.isComplete()) ;
                            Uri url = uri.getResult();
                            Chat chat = new Chat(Long.parseLong(TenUser.ID),TenUser.name, "", url.toString());
                            mData.child("HoTroChat").child(id).push().setValue(chat, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                    // dua anh ve anh mac dinh
                                    img_gui_anh.setImageResource(R.drawable.ic_img);
                                }
                            });
                        }
                    });
                }
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void HTChat(){
        mData.child("HoTroChat").child(id).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Chat chat = snapshot.getValue(Chat.class);
                Log.i("Chh",snapshot.toString());
                chatArrayList.add(new Chat(chat.getId(), chat.getTen(), chat.getComment(), chat.getAnh()));
                chatHoTroKHAdapter.notifyDataSetChanged();
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