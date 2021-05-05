package com.example.btl_ltuddd_electricstore.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.btl_ltuddd_electricstore.Adapter.ChatAdapter;
import com.example.btl_ltuddd_electricstore.NetworkChangeReciever;
import com.example.btl_ltuddd_electricstore.R;
import com.example.btl_ltuddd_electricstore.object.Chat;
import com.example.btl_ltuddd_electricstore.object.GioHang;
import com.example.btl_ltuddd_electricstore.object.SanPham;
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

public class thongtin_sp_Activity extends AppCompatActivity {

    ImageView img, img_ahbinhluan, img_send;
    TextView txt_ten, txt_ma, txt_maL, txt_gia, txt_mota;
    EditText edit_soanTinNhan;
    int REQUEST_CHOOSE_PHOTO = 123;
    DatabaseReference mData;
    public String ten = TenUser.name;
    ArrayList<Chat> chatArrayList;
    ChatAdapter chatAdapter;
    ListView listView;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    Button btn_muaHang, btn_themVaoGioHang;
    public String check = "";
    ArrayList<Chat> binhLuanMoi;
    BroadcastReceiver broadcastReceiver;
    LinearLayout linearLayout;
    RelativeLayout main_screen;
    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thongtin_sp);

        anhXa();
        hienThiTTSP();
        actionToolbarReturn();


        mData = FirebaseDatabase.getInstance().getReference();
        chonAnhBL();
        guiBinhLuan();

        htBinhLuan();

        chatAdapter = new ChatAdapter(thongtin_sp_Activity.this, chatArrayList, R.layout.binh_luan_sp);
        listView.setAdapter(chatAdapter);


        themSPvaoGioHang();
        muaHangNgay();
        DaoBinhLuan();
        //edit_soanTinNhan.requestFocus();


        broadcastReceiver = new NetworkChangeReciever();
        registerNetWorkBroadcastReciver();
    }


    public void DaoBinhLuan(){
        binhLuanMoi.clear();
        new CountDownTimer(500, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                for (int i = chatArrayList.size()-1; i >= 0; i--){
                    binhLuanMoi.add(chatArrayList.get(i));
                }
                chatAdapter = new ChatAdapter(thongtin_sp_Activity.this, binhLuanMoi, R.layout.binh_luan_sp);
                listView.setAdapter(chatAdapter);
            }
        }.start();
    }

    public String getma(){
        return txt_ma.getText().toString();
    }
    public String getMal(){
        return txt_maL.getText().toString();
    }

    public void XoaBinhLuan(String id){
        mData.child("BinhLuan").child(txt_maL.getText().toString()).child(txt_ma.getText().toString()).child(id).removeValue();
        Toast.makeText(thongtin_sp_Activity.this, "Bình luận của bạn đã được xóa", Toast.LENGTH_SHORT).show();
    }
    private void actionToolbarReturn() {
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toll);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void anhXa() {
        img = (ImageView) findViewById(R.id.img_thongtinsp);
        txt_ten = (TextView) findViewById(R.id.thongtin_tensp);
        txt_ma = (TextView) findViewById(R.id.thongtin_masp);
        txt_maL = (TextView) findViewById(R.id.thongtin_maLsp);
        txt_gia = (TextView) findViewById(R.id.thongtin_giasp);
        txt_mota = (TextView) findViewById(R.id.thongtin_mota);
        img_ahbinhluan = (ImageView) findViewById(R.id.img_binhluan);
        img_send = (ImageView) findViewById(R.id.img_gui_binhluan);
        edit_soanTinNhan = (EditText) findViewById(R.id.Edit_binhluan);
        listView = (ListView) findViewById(R.id.lv_binh_luan);
        chatArrayList = new ArrayList<>();
        binhLuanMoi  = new ArrayList<>();
        btn_muaHang = (Button) findViewById(R.id.btn_mua_hang_ngay);
        btn_themVaoGioHang = (Button) findViewById(R.id.btn_add_gio_hang);
        linearLayout = (LinearLayout) findViewById(R.id.layout_btn_bottom);
        main_screen = (RelativeLayout) findViewById(R.id.main_screen);
        scrollView =(ScrollView) findViewById(R.id.scrollView);
    }

    public String hinhAnh = "";
    public String giaSP = "";

    //lấy dữ liệu sản phẩm banhang_Activity thông qua bundle và intent để hiển thj thông tin lên thongtin_sp_Activity
    private void hienThiTTSP() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");
        if (bundle != null) {
            SanPham sp = (SanPham) bundle.getSerializable("ttsp");
            Picasso.get().load(sp.getHinhAnh()).into(img);
            txt_ten.setText(sp.getTenSP());
            txt_ma.setText(sp.getMaSP());
            txt_maL.setText(sp.getMaLoaiSP());
            txt_gia.setText(sp.getGia() + " vnđ");
            txt_mota.setText(sp.getMoTa());
            txt_mota.setMovementMethod(new ScrollingMovementMethod());

            hinhAnh = sp.getHinhAnh();
            giaSP = sp.getGia();
        }

    }

    private void chonAnhBL() {
        img_ahbinhluan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CHOOSE_PHOTO);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CHOOSE_PHOTO) {
            try {
                final StorageReference storageRef = storage.getReferenceFromUrl("gs://cuahangdientu-894b2.appspot.com");
                Calendar calendar = Calendar.getInstance();
                Uri uri = data.getData();
                InputStream is = getContentResolver().openInputStream(uri);
                //Bitmap bitmap = BitmapFactory.decodeStream(is);
//                luuAnh = data;
//                img_ahbinhluan.setImageBitmap(bitmap);
                Picasso.get().load(uri).resize(100, 100).into(img_ahbinhluan);

//                String filename = System.currentTimeMillis() + "";
                if (uri != null) {
                    // dua anh len firebase
                    UploadTask uploadTask = storageRef.child("image" + calendar.getTimeInMillis() + ".png").putFile(uri);
                    uploadTask.addOnSuccessListener(thongtin_sp_Activity.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {// gui anh len firebase
                            Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();// lấy url để download
                            while (!uri.isComplete()) ;
                            Uri url = uri.getResult();
//                            Log.i("urrlllll", url.toString());
                            Chat chat = new Chat(System.currentTimeMillis(),ten, "", url.toString());
                            mData.child("BinhLuan").child(txt_maL.getText().toString()).child(txt_ma.getText().toString()).push().setValue(chat, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                    Toast.makeText(thongtin_sp_Activity.this,"Bình luận đã được đăng", Toast.LENGTH_SHORT).show();
                                    DaoBinhLuan();
                                    img_ahbinhluan.setImageResource(R.drawable.ic_img);
                                    edit_soanTinNhan.setText("");
                                }
                            });
                        }
                    });
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // gửi bình luận
    private void guiBinhLuan() {

        img_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Chat chat = new Chat(System.currentTimeMillis(),ten, edit_soanTinNhan.getText().toString(), "");
                mData.child("BinhLuan").child(txt_maL.getText().toString()).child(txt_ma.getText().toString()).push().setValue(chat, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Toast.makeText(thongtin_sp_Activity.this, "Bình luận đã được đăng", Toast.LENGTH_SHORT).show();
                        DaoBinhLuan();
                        edit_soanTinNhan.setText("");
                    }
                });

            }
        });
    }

    // hien thi binh luan
    private void htBinhLuan() {
        mData.child("BinhLuan").child(txt_maL.getText().toString()).child(txt_ma.getText().toString()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Chat chat = snapshot.getValue(Chat.class);
                chatArrayList.add(chat);
                chatAdapter.notifyDataSetChanged();
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


    //them sp vao gio hang
    private void themSPvaoGioHang() {
        btn_themVaoGioHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mData.child("GioHang").child(TenUser.ID).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        GioHang gioHang = snapshot.getValue(GioHang.class);
                        if (txt_ma.getText().toString().equals(gioHang.getMaSP())) {
                            check = "trung";
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

                        if (check.equals("trung")) {
                            Toast.makeText(thongtin_sp_Activity.this, "Sản phẩm đã trong giỏ hàng", Toast.LENGTH_SHORT).show();
                        }
                        if (check.equals("")) {
                            GioHang gioHang = new GioHang(txt_ma.getText().toString(), txt_ten.getText().toString(), giaSP, hinhAnh,1, giaSP);
                            mData.child("GioHang").child(TenUser.ID).push().setValue(gioHang, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                    if (error == null) {
                                        Toast.makeText(thongtin_sp_Activity.this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(thongtin_sp_Activity.this, "Thất bại", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                }.start();

            }
        });
    }

    private void muaHangNgay() {
        btn_muaHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mData.child("GioHang").child(TenUser.ID).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        GioHang gioHang = snapshot.getValue(GioHang.class);
                        if (txt_ma.getText().toString().equals(gioHang.getMaSP())) {
                            check = "trung";
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

                        if (check.equals("trung")) {
                            finish();
                            startActivity(new Intent(thongtin_sp_Activity.this, gio_hang_Activity.class));
                        }
                        if (check.equals("")) {
                            GioHang gioHang = new GioHang(txt_ma.getText().toString(), txt_ten.getText().toString(), giaSP, hinhAnh,1, giaSP);
                            mData.child("GioHang").child(TenUser.ID).push().setValue(gioHang, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                    if (error == null) {
                                        finish();
                                        startActivity(new Intent(thongtin_sp_Activity.this, gio_hang_Activity.class));
                                    } else {
                                        Toast.makeText(thongtin_sp_Activity.this, "Thất bại", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                }.start();
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