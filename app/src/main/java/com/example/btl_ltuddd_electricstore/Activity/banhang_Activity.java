package com.example.btl_ltuddd_electricstore.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.btl_ltuddd_electricstore.Adapter.ItemGridviewAdapter;
import com.example.btl_ltuddd_electricstore.NetworkChangeReciever;
import com.example.btl_ltuddd_electricstore.R;
import com.example.btl_ltuddd_electricstore.object.ItemMenuKhachHang;
import com.example.btl_ltuddd_electricstore.Adapter.MenuKhachHangAdapter;
import com.example.btl_ltuddd_electricstore.object.SanPham;
import com.example.btl_ltuddd_electricstore.object.TenUser;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class banhang_Activity extends AppCompatActivity {

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ListView listView;
    ArrayList<ItemMenuKhachHang> arrayList;
    MenuKhachHangAdapter menuKhachHangAdapter;
    ViewFlipper viewFlipper;

    ArrayList<SanPham> mangSanPham;
    ItemGridviewAdapter itemGridviewAdapter;
    GridView gridView;
    DatabaseReference mData;

    ImageView imgPhone, imgLap;
    TextView txt_ten;
    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banhang);

        Toast.makeText(banhang_Activity.this, "Hi "+TenUser.name, Toast.LENGTH_SHORT).show();
        anhXa();
        actionToolBar();
        actionMenu();
        quangCao();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ItemMenuKhachHang itemMenuKhachHang = arrayList.get(position);
                if (itemMenuKhachHang.tenItem.equals("Đăng xuất")){
                    startActivity(new Intent(banhang_Activity.this, dang_nhap_Activity.class));
                    finish();
                }
            }
        });

        mData = FirebaseDatabase.getInstance().getReference();
        LoadData();


        itemGridviewAdapter = new ItemGridviewAdapter(banhang_Activity.this,mangSanPham,R.layout.item_san_pham_gridview);
        gridView.setAdapter(itemGridviewAdapter);

        BottomNavigationView bt = (BottomNavigationView) findViewById(R.id.menu_bottom);
        bt.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.bottom_profile:
                        startActivity(new Intent(banhang_Activity.this, profile_Activity.class));
                        break;
                    case R.id.bottom_home:
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                        break;
                    case R.id.bottom_cart:
                        startActivity(new Intent(banhang_Activity.this, gio_hang_Activity.class));
                        break;
                }
                return true;
            }
        });
        //xem thông tin sản phẩm
        hienThiThongTinSP();
        //hien thi dien thoai neu bam img phone o phan catory
        imgPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hienThiDT();
            }
        });
        //hien thi laptop neu bam img laptop o phan catory
        imgLap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hienThiLT();
            }
        });

        //gan ten nguoi dang nhap vao  menu Navigation
        ten = TenUser.name;
        txt_ten.setText(ten);

        broadcastReceiver = new NetworkChangeReciever();
        registerNetWorkBroadcastReciver();
    }

    public String ten ="";


    // tao menu navigation khi an vao toolbar
    private void actionToolBar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_action_menu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }
    private void anhXa(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawLayout);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        listView = (ListView) findViewById(R.id.lv);
        gridView = (GridView) findViewById(R.id.gridview_item);
        mangSanPham = new ArrayList<>();
        imgPhone = (ImageView) findViewById(R.id.img_smartphone);
        imgLap = (ImageView) findViewById(R.id.img_laptop);
        txt_ten = (TextView) findViewById(R.id.txt_name_user_menu);
    }
    private void actionMenu(){
        arrayList = new ArrayList<>();
        arrayList.add(new ItemMenuKhachHang("home",R.drawable.home));
        arrayList.add(new ItemMenuKhachHang("dienThoai",R.drawable.phone));
        arrayList.add(new ItemMenuKhachHang("lapTop",R.drawable.laptop));
        arrayList.add(new ItemMenuKhachHang("profile",R.drawable.profile));
        arrayList.add(new ItemMenuKhachHang("Đăng xuất", R.drawable.dangxuat));

        menuKhachHangAdapter = new MenuKhachHangAdapter(banhang_Activity.this,R.layout.dong_item_khach_hang,arrayList);
        listView.setAdapter(menuKhachHangAdapter);
    }

    //Chuyển động ảnh quảng cáo
    private void quangCao(){
        viewFlipper = (ViewFlipper) findViewById(R.id.view_quangcao);
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);
    }

    // lay du lieu tu firebase truyen vao arraylist de hien thi cac san pham
    private void LoadData(){
        mangSanPham.clear();
        mData.child("SanPham").child("DT").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                SanPham sp = snapshot.getValue(SanPham.class);
                mangSanPham.add(new SanPham(sp.getTenSP(),sp.getMaSP(),sp.getMaLoaiSP(),sp.getGia(),sp.getMoTa(),sp.getHinhAnh()));
                itemGridviewAdapter.notifyDataSetChanged();
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
        mData.child("SanPham").child("LT").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                SanPham sp = snapshot.getValue(SanPham.class);
                mangSanPham.add(new SanPham(sp.getTenSP(),sp.getMaSP(),sp.getMaLoaiSP(),sp.getGia(),sp.getMoTa(),sp.getHinhAnh()));
                itemGridviewAdapter.notifyDataSetChanged();
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

    //click vào item của gridview
    private void hienThiThongTinSP(){
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(banhang_Activity.this, thongtin_sp_Activity.class);
                Bundle bundle = new Bundle();
                SanPham sp = mangSanPham.get(position);
                bundle.putSerializable("ttsp", sp);
                intent.putExtra("data",bundle);
                startActivity(intent);
            }
        });
    }

    private void hienThiDT(){
        mangSanPham.clear();
        mData.child("SanPham").child("DT").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                SanPham sp = snapshot.getValue(SanPham.class);
                mangSanPham.add(new SanPham(sp.getMaSP(),sp.getTenSP(), sp.getMaLoaiSP(),sp.getGia(), sp.getMoTa(), sp.getHinhAnh()));
                itemGridviewAdapter.notifyDataSetChanged();
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

    private void hienThiLT(){
        mangSanPham.clear();
        mData.child("SanPham").child("LT").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                SanPham sp = snapshot.getValue(SanPham.class);
                mangSanPham.add(new SanPham(sp.getMaSP(),sp.getTenSP(), sp.getMaLoaiSP(),sp.getGia(), sp.getMoTa(), sp.getHinhAnh()));
                itemGridviewAdapter.notifyDataSetChanged();
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