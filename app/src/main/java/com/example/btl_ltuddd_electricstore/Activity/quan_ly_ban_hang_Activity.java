package com.example.btl_ltuddd_electricstore.Activity;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.btl_ltuddd_electricstore.Adapter.ItemQuanLyAdapter;
import com.example.btl_ltuddd_electricstore.Adapter.MenuQlAdapter;
import com.example.btl_ltuddd_electricstore.Adapter.XacNhanDonHangAdapter;
import com.example.btl_ltuddd_electricstore.NetworkChangeReciever;
import com.example.btl_ltuddd_electricstore.R;
import com.example.btl_ltuddd_electricstore.object.ItemMenuQL;
import com.example.btl_ltuddd_electricstore.object.LichSuMuaHang;
import com.example.btl_ltuddd_electricstore.object.PhuongThucDonHang;
import com.example.btl_ltuddd_electricstore.object.SanPham;
import com.example.btl_ltuddd_electricstore.object.TenUser;
import com.example.btl_ltuddd_electricstore.service.service_notification;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class quan_ly_ban_hang_Activity extends AppCompatActivity {

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ListView listView,lvsp;
    ArrayList<ItemMenuQL> arrayList;
    MenuQlAdapter adapter;
    ArrayList<SanPham> mangSanPham;
    DatabaseReference mData;
    ItemQuanLyAdapter itemQuanLyAdapter;
    BroadcastReceiver broadcastReceiver;
    TextView txt_ten;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_ban_hang);


        mData = FirebaseDatabase.getInstance().getReference();
        anhXa();
        actionToolBar();
        getSupportActionBar().setTitle("");
        actionMenu();



        //đổ dữ liệu các sản phẩm có trên firebase vào listview
        itemQuanLyAdapter = new ItemQuanLyAdapter(quan_ly_ban_hang_Activity.this,R.layout.dong_item_san_pham,mangSanPham);
        lvsp.setAdapter(itemQuanLyAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ItemMenuQL itemMenuQL = arrayList.get(position);
                if (itemMenuQL.tenIcon.equals("Đăng xuất")){
                    finish();
                    startActivity(new Intent(quan_ly_ban_hang_Activity.this, dang_nhap_Activity.class));
                }else if (itemMenuQL.tenIcon.equals("Quản lý điện thoại")){
                    LoadDienThoai();
                }else if (itemMenuQL.tenIcon.equals("Quản lý Laptop")){
                    LoadLaptop();
                }else if (itemMenuQL.tenIcon.equals("Xác nhận đơn hàng")){
                    PhuongThucDonHang.phuongThuc = "ChoXacNhan";
                    startActivity(new Intent(quan_ly_ban_hang_Activity.this, xac_nhan_don_hang_Activity.class));
                }else if (itemMenuQL.tenIcon.equals("Xác nhận giao hàng")){
                    PhuongThucDonHang.phuongThuc = "DangGiao";
                    startActivity(new Intent(quan_ly_ban_hang_Activity.this, xac_nhan_don_hang_Activity.class));
                }else if (itemMenuQL.tenIcon.equals("Đơn hàng đã giao")){
                    PhuongThucDonHang.phuongThuc = "DaGiao";
                    startActivity(new Intent(quan_ly_ban_hang_Activity.this, xac_nhan_don_hang_Activity.class));
                }else if (itemMenuQL.tenIcon.equals("Đơn hàng đã hủy")){
                    PhuongThucDonHang.phuongThuc = "DaHuy";
                    startActivity(new Intent(quan_ly_ban_hang_Activity.this, xac_nhan_don_hang_Activity.class));
                }else if (itemMenuQL.tenIcon.equals("Xác nhận trả hàng")){
                    PhuongThucDonHang.phuongThuc = "TraHang";
                    startActivity(new Intent(quan_ly_ban_hang_Activity.this, xac_nhan_don_hang_Activity.class));
                }else if (itemMenuQL.tenIcon.equals("Hỗ trợ khách hàng")){
                    startActivity(new Intent(quan_ly_ban_hang_Activity.this, danh_sach_kh_Activity.class));
                }
            }
        });


        //load dữ liệu danh sách các sản phẩm
        LoadData();

        //nhan biet truyen phuong thức sửa hoặc xóa để làm một số nút bên layout ko cần thiết bị ẩn đi
        lvsp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                method = "e_d";
                Intent intent = new Intent(quan_ly_ban_hang_Activity.this, add_Activity.class);
                Bundle bundle = new Bundle();
                bundle.putString("method",method);
                SanPham sanPham = mangSanPham.get(position);
                bundle.putSerializable("sanpham", sanPham);

                intent.putExtra("dulieu",bundle);
                startActivity(intent);
            }
        });

//        Toast.makeText(quan_ly_ban_hang_Activity.this, TenUser.name, Toast.LENGTH_SHORT).show();
        // nhan thong bao
        Intent service = new Intent(this, service_notification.class);
        startService(service);
        //ktra ket noi mang
        broadcastReceiver = new NetworkChangeReciever();
        registerNetWorkBroadcastReciver();
    }


    //Menu Navigation
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
        toolbar = (Toolbar) findViewById(R.id.toolbar2);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawLayout2);
        navigationView = (NavigationView) findViewById(R.id.navigationView2);
        listView = (ListView) findViewById(R.id.lv2);
        txt_ten  = (TextView) findViewById(R.id.txt_name_manager);
        txt_ten.setText(TenUser.name);
        mangSanPham = new ArrayList<>();
        lvsp = (ListView) findViewById(R.id.lvQL);
    }

    //hien thi danh sách các tùy chọn trong menu
    private void actionMenu(){
        arrayList = new ArrayList<>();
        arrayList.add(new ItemMenuQL("Hỗ trợ khách hàng", R.drawable.support));
        arrayList.add(new ItemMenuQL("Quản lý điện thoại",R.drawable.phone));
        arrayList.add(new ItemMenuQL("Quản lý Laptop", R.drawable.laptop));
        arrayList.add(new ItemMenuQL("Xác nhận đơn hàng", R.drawable.confirm));
        arrayList.add(new ItemMenuQL("Xác nhận giao hàng", R.drawable.confirm));
        arrayList.add(new ItemMenuQL("Đơn hàng đã giao", R.drawable.confirm));
        arrayList.add(new ItemMenuQL("Đơn hàng đã hủy", R.drawable.confirm));
        arrayList.add(new ItemMenuQL("Xác nhận trả hàng", R.drawable.confirm));
        arrayList.add(new ItemMenuQL("Đăng xuất", R.drawable.dangxuat));
        adapter = new MenuQlAdapter(quan_ly_ban_hang_Activity.this,arrayList,R.layout.dong_item_quan_ly_ban_hang);
        listView.setAdapter(adapter);
    }
    //tạo nút add
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_data,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //bam vao menu để thêm 1 sản phẩm mới
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuAdd){
            method = "add";
            Intent intent = new Intent(quan_ly_ban_hang_Activity.this, add_Activity.class);
            Bundle bundle = new Bundle();
            bundle.putString("method",method);

            intent.putExtra("dulieu", bundle);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public String method="";
    // load toàn dữ liệu các sản phẩm từ firebase vào listview
    private void LoadData(){
        mangSanPham.clear();
        mData.child("SanPham").child("DT").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                SanPham sanPham = snapshot.getValue(SanPham.class);
                mangSanPham.add(new SanPham(sanPham.getMaSP(),sanPham.getTenSP(),sanPham.getMaLoaiSP(),sanPham.getGia(), sanPham.getMoTa(), sanPham.getHinhAnh()));
                itemQuanLyAdapter.notifyDataSetChanged();
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
                SanPham sanPham = snapshot.getValue(SanPham.class);
                mangSanPham.add(new SanPham(sanPham.getMaSP(),sanPham.getTenSP(),sanPham.getMaLoaiSP(),sanPham.getGia(), sanPham.getMoTa(), sanPham.getHinhAnh()));
                itemQuanLyAdapter.notifyDataSetChanged();
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

    // load  dữ liệu các sản phẩm về điện thoại từ firebase vào listview
    private void LoadDienThoai(){
        mangSanPham.clear();
        mData.child("SanPham").child("DT").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                SanPham sanPham = snapshot.getValue(SanPham.class);
                    mangSanPham.add(new SanPham(sanPham.getMaSP(),sanPham.getTenSP(),sanPham.getMaLoaiSP(),sanPham.getGia(), sanPham.getMoTa(), sanPham.getHinhAnh()));
                    itemQuanLyAdapter.notifyDataSetChanged();
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

    //load dữ liệu từ laptop từ firebase về listview
    private void LoadLaptop(){
        mangSanPham.clear();
        mData.child("SanPham").child("LT").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                SanPham sanPham = snapshot.getValue(SanPham.class);
                    mangSanPham.add(new SanPham(sanPham.getMaSP(), sanPham.getTenSP(), sanPham.getMaLoaiSP(), sanPham.getGia(), sanPham.getMoTa(), sanPham.getHinhAnh()));
                    itemQuanLyAdapter.notifyDataSetChanged();
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