package com.example.btl_ltuddd_electricstore.Activity;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;

import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;


import com.example.btl_ltuddd_electricstore.NetworkChangeReciever;
import com.example.btl_ltuddd_electricstore.R;
import com.example.btl_ltuddd_electricstore.object.SanPham;
import com.google.android.gms.tasks.OnFailureListener;
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

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;


public class add_Activity extends AppCompatActivity {

    ImageView img;
    Button btnAdd, btnHuy, btnChupAnh, btnChonAnh, btn_sua, btn_xoa;
    EditText edit_maSP, edit_tenSP, edit_gia, edit_mota;
    Spinner cbb_maLSP;
    int REQUEST_CODE_CAMERA = 123;
    int REQUEST_CHOOSE_PHOTO = 321;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    DatabaseReference mData;
    String maL;
    public String ktra="";
    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        img = (ImageView) findViewById(R.id.imgSP);
        btnAdd = (Button) findViewById(R.id.btn_dongYcapNhat);
        btnHuy = (Button) findViewById(R.id.btn_thoat_capnhat);
        btnChupAnh = (Button) findViewById(R.id.btn_chupAnh);
        btnChonAnh = (Button) findViewById(R.id.btn_chonAnh);
        edit_maSP =(EditText) findViewById(R.id.Edit_masp);
        cbb_maLSP = (Spinner) findViewById(R.id.cbb_maloai);
        edit_tenSP = (EditText) findViewById(R.id.Edit_tensp);
        edit_gia = (EditText) findViewById(R.id.Edit_gia);
        edit_mota = (EditText) findViewById(R.id.Edit_mota);
        btn_sua = (Button) findViewById(R.id.btn_sua);
        btn_xoa = (Button) findViewById(R.id.btn_xoa);



        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.cbbmal, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cbb_maLSP.setAdapter(adapter);
        cbb_maLSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                maL = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //lấy dữ liệu để ktra sử dụng method sửa hoặc xóa để làm ẩn đi một số button
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("dulieu");

        if (bundle != null){
            String kq = bundle.getString("method");
            if (kq.equals("add")){
                btn_sua.setVisibility(View.GONE);
                btn_xoa.setVisibility(View.GONE);
            }
            if (kq.equals("e_d")){
                edit_maSP.setEnabled(false);
                cbb_maLSP.setEnabled(false);
                btnAdd.setVisibility(View.GONE);
                SanPham sp = (SanPham) bundle.getSerializable("sanpham");
                edit_maSP.setText(sp.getMaSP());
                edit_tenSP.setText(sp.getTenSP());
                if (sp.getMaLoaiSP().equals("DT")){
                    cbb_maLSP.setSelection(0);
                }
                if (sp.getMaLoaiSP().equals("LT")){
                    cbb_maLSP.setSelection(1);
                }
                edit_gia.setText(sp.getGia());
                edit_mota.setText(sp.getMoTa());
                Picasso.get().load(sp.getHinhAnh()).into(img);
            }
        }
        //chụp hình ảnh
        btnChupAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
            }
        });
        //chọn hình ảnh
        btnChonAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CHOOSE_PHOTO);
            }
        });
        //lưu ảnh dưới dạng link và thông tin sản phẩm trên realtime database, đồng thời lưu ảnh vào Storge của firebase
        mData = FirebaseDatabase.getInstance().getReference();

        final StorageReference storageRef = storage.getReferenceFromUrl("gs://cuahangdientu-894b2.appspot.com");
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ma = edit_maSP.getText().toString();
                String ten = edit_tenSP.getText().toString();
                String gia = edit_gia.getText().toString();
                String mota = edit_mota.getText().toString();
                Calendar calendar = Calendar.getInstance();
                mData.child("SanPham").child(maL).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        SanPham sp = snapshot.getValue(SanPham.class);
                        if (ma.equals(sp.getMaSP())){
                            ktra = "trung";
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
                new CountDownTimer(400,1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        if (ktra.equals("trung")){
                            Toast.makeText(add_Activity.this, "Mã sản phẩm bị trùng",Toast.LENGTH_SHORT).show();
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);
                        }
                        else {
                            StorageReference mountainsRef = storageRef.child("image" + calendar.getTimeInMillis() + ".png");
                            // Get the data from an ImageView as bytes

                            if (ma.length() != 0 && ten.length() != 0 && maL.length() != 0 && gia.length() != 0 && mota.length() != 0){
                                img.setDrawingCacheEnabled(true);
                                img.buildDrawingCache();
                                Bitmap bitmap = ((BitmapDrawable) img.getDrawable()).getBitmap();
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                byte[] data = baos.toByteArray();
                                UploadTask uploadTask = mountainsRef.putBytes(data);
                                uploadTask.addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        // Handle unsuccessful uploads
                                        Toast.makeText(add_Activity.this,"Lỗi",Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                                        // ...
                                        Toast.makeText(add_Activity.this,"Lưu ảnh vào storage thành công",Toast.LENGTH_SHORT).show();
                                        if (taskSnapshot.getMetadata() != null) {
                                            if (taskSnapshot.getMetadata().getReference() != null) {
                                                Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        String imageUrl = uri.toString();
                                                        SanPham sanPham = new SanPham(ma,ten,maL,gia,mota, String.valueOf(imageUrl));
                                                        mData.child("SanPham").child(maL).child(ma).setValue(sanPham, new DatabaseReference.CompletionListener() {
                                                            @Override
                                                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                                                if (error == null){
                                                                    Toast.makeText(add_Activity.this, "Thêm dữ liệu thành công",Toast.LENGTH_SHORT).show();
                                                                    startActivity(new Intent(add_Activity.this, quan_ly_ban_hang_Activity.class));
                                                                    finish();
                                                                }else {
                                                                    Toast.makeText(add_Activity.this, "Lỗi",Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        }
                                    }
                                });
                            }
                            else {
                                Toast.makeText(add_Activity.this,"Nhập thiếu dữ liệu",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }.start();



            }
        });

        //nếu không muốn thay đổi gì thì có thể thoát khỏi activity
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(add_Activity.this,quan_ly_ban_hang_Activity.class));
                finish();
            }
        });
        //sửa dữ liệu
        btn_sua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogUpdate();
//                getKey();
            }
        });

        //xóa dữ liệu
        btn_xoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getKey();
                showDialogDelete();
            }
        });

        //ktra ket noi internet
        broadcastReceiver = new NetworkChangeReciever();
        registerNetWorkBroadcastReciver();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK && data != null){
//            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
//            img.setImageBitmap(bitmap);
//        }
        if (resultCode == RESULT_OK){
            if (requestCode == REQUEST_CHOOSE_PHOTO){
                try {
                    Uri imageUri = data.getData();
                    InputStream is = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    img.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }else if (requestCode == REQUEST_CODE_CAMERA){
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                img.setImageBitmap(bitmap);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // goi dialog sửa
    private void showDialogUpdate(){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_yes_no);
        dialog.show();

        Button btn_dongy = (Button) dialog.findViewById(R.id.btn_yes);
        Button btn_ko = (Button) dialog.findViewById(R.id.btn_no);

        final StorageReference storageRef = storage.getReferenceFromUrl("gs://cuahangdientu-894b2.appspot.com");

        String ma = edit_maSP.getText().toString();
        String ten = edit_tenSP.getText().toString();
        String gia = edit_gia.getText().toString();
        String mota = edit_mota.getText().toString();
        Calendar calendar = Calendar.getInstance();
        StorageReference mountainsRef = storageRef.child("image" + calendar.getTimeInMillis() + ".png");

        btn_dongy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img.setDrawingCacheEnabled(true);
                img.buildDrawingCache();
                Bitmap bitmap = ((BitmapDrawable) img.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
                byte[] data = baos.toByteArray();

                UploadTask uploadTask = mountainsRef.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(add_Activity.this,"Lỗi",Toast.LENGTH_SHORT).toString();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        if (taskSnapshot.getMetadata() != null) {
                            if (taskSnapshot.getMetadata().getReference() != null) {
                                Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imageUrl = uri.toString();
                                        SanPham sanPham = new SanPham(ma,ten,maL,gia,mota, String.valueOf(imageUrl));
                                        mData.child("SanPham").child(maL).child(ma).setValue(sanPham, new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                                if (error == null){
                                                    Toast.makeText(add_Activity.this, "Update dữ liệu thành công",Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(add_Activity.this,quan_ly_ban_hang_Activity.class));
                                                    finish();
                                                }else {
                                                    Toast.makeText(add_Activity.this, "Lỗi",Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                });
                            }
                        }
                    }
                });
            }
        });

        //thoát khỏi dialog nếu không muốn thực hiện sửa, xóa sản phẩm
        btn_ko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

//    private String key ="";
//    private void getKey(){
//        String ma = edit_maSP.getText().toString();
//        mData.child("SanPham").addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                SanPham sp = snapshot.getValue(SanPham.class);
//                if (sp.getMaSP().equals(ma) && sp.getMaLoaiSP().equals(maL)){
//                    key = snapshot.getKey();
//                }
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

    // gọi dialog xóa
    private void showDialogDelete(){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_yes_no);
        dialog.show();

        Button btn_dongy = (Button) dialog.findViewById(R.id.btn_yes);
        Button btn_ko = (Button) dialog.findViewById(R.id.btn_no);

        btn_ko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        String ma = edit_maSP.getText().toString();
        btn_dongy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mData.child("SanPham").child(maL).child(ma).removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Toast.makeText(add_Activity.this, "Xóa dữ liệu thành công", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        startActivity(new Intent(add_Activity.this, quan_ly_ban_hang_Activity.class));
                    }
                });
            }
        });
    }

    //ktra ket noi Internet
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