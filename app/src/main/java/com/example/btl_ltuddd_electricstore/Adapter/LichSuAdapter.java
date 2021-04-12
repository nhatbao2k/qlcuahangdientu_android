package com.example.btl_ltuddd_electricstore.Adapter;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.btl_ltuddd_electricstore.Activity.lich_su_mua_hang_Activity;
import com.example.btl_ltuddd_electricstore.R;
import com.example.btl_ltuddd_electricstore.object.LichSuMuaHang;
import com.example.btl_ltuddd_electricstore.object.PhuongThucDonHang;
import com.example.btl_ltuddd_electricstore.object.TenUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LichSuAdapter extends BaseAdapter {

    private lich_su_mua_hang_Activity context;
    private List<LichSuMuaHang> lichSuMuaHangList;
    private int layout;
    DatabaseReference mData;

    public LichSuAdapter(lich_su_mua_hang_Activity context, List<LichSuMuaHang> lichSuMuaHangList, int layout) {
        this.context = context;
        this.lichSuMuaHangList = lichSuMuaHangList;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return lichSuMuaHangList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder{
        ImageView img;
        TextView txt_tensp, txt_gia, txt_soLuong, txt_nguoimua, txt_diachi, txt_ngay, txt_sdt, txt_ngayxn, txt_tinh_trang;
        Button btn;
    }

    public String key;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder viewHolder;
        if (convertView == null){
            convertView = inflater.inflate(layout,null);
            viewHolder = new ViewHolder();

            viewHolder.img = (ImageView) convertView.findViewById(R.id.img_item_lichsu);
            viewHolder.txt_tensp = (TextView) convertView.findViewById(R.id.txt_ten_sp_ls);
            viewHolder.txt_soLuong = (TextView) convertView.findViewById(R.id.txt_sol_ls);
            viewHolder.txt_gia = (TextView) convertView.findViewById(R.id.txt_gia_sp_ls);
            viewHolder.txt_nguoimua = (TextView) convertView.findViewById(R.id.txt_ten_nguoi_mua_ls);
            viewHolder.txt_diachi = (TextView) convertView.findViewById(R.id.txt_dia_chi_ls);
            viewHolder.txt_ngay = (TextView) convertView.findViewById(R.id.txt_ngay_ls);
            viewHolder.txt_sdt = (TextView) convertView.findViewById(R.id.txt_sdt_ls);
            viewHolder.txt_ngayxn = (TextView) convertView.findViewById(R.id.txt_ngay_xn_ls);
            viewHolder.btn = (Button) convertView.findViewById(R.id.btn_mua_lai_ls);
            viewHolder.txt_tinh_trang = (TextView) convertView.findViewById(R.id.txt_tinh_trang_ls);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Picasso.get().load(lichSuMuaHangList.get(position).getHinhAnh()).into(viewHolder.img);
        viewHolder.txt_tensp.setText(lichSuMuaHangList.get(position).getTenSP());
        viewHolder.txt_soLuong.setText(lichSuMuaHangList.get(position).getSoLuong()+"");
        viewHolder.txt_gia.setText(lichSuMuaHangList.get(position).getGia() + " vnđ");
        viewHolder.txt_nguoimua.setText(lichSuMuaHangList.get(position).getTenKH());
        viewHolder.txt_diachi.setText(lichSuMuaHangList.get(position).getDiaChi());
        viewHolder.txt_ngay.setText(lichSuMuaHangList.get(position).getNgayMua());
        viewHolder.txt_sdt.setText(lichSuMuaHangList.get(position).getSdt());
        viewHolder.txt_ngayxn.setText(lichSuMuaHangList.get(position).getNgayXacNhan());
        viewHolder.txt_tinh_trang.setText(lichSuMuaHangList.get(position).getTrangThai());

        mData = FirebaseDatabase.getInstance().getReference();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy _ HH:mm:ss", Locale.getDefault());
        String date = simpleDateFormat.format(new Date());
        try{
            if (PhuongThucDonHang.phuongThuc.equals("ChoXacNhan")){
                mData.child("ThongBao").child("TrangThai").child(TenUser.ID).removeValue();
                viewHolder.btn.setText("Hủy đơn hàng");
                viewHolder.btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mData.child("LichSuMuaHang").child("ChoXacNhan").addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                LichSuMuaHang lichSuMuaHang = snapshot.getValue(LichSuMuaHang.class);
                                if (lichSuMuaHangList.get(position).getTenSP().equals(lichSuMuaHang.getTenSP()) && lichSuMuaHangList.get(position).getTenKH().equals(lichSuMuaHang.getTenKH()) && lichSuMuaHangList.get(position).getNgayMua().equals(lichSuMuaHang.getNgayMua()) && lichSuMuaHangList.get(position).getCMT().equals(lichSuMuaHang.getCMT())){
                                    mData.child("LichSuMuaHang").child("DaHuy").push().setValue(new LichSuMuaHang(lichSuMuaHang.getCMT(), "Hủy đơn hàng", lichSuMuaHang.getTenKH(), lichSuMuaHang.getSdt(), lichSuMuaHang.getDiaChi(),lichSuMuaHang.getTenSP(), lichSuMuaHang.getHinhAnh(), lichSuMuaHang.getGia(), lichSuMuaHang.getSoLuong(), lichSuMuaHang.getNgayMua(),date));
                                    key = snapshot.getKey();
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
                        new CountDownTimer(500, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {

                            }

                            @Override
                            public void onFinish() {
                                context.XoaDonHang(key);
                            }
                        }.start();
                    }
                });
            }else if (PhuongThucDonHang.phuongThuc.equals("DangGiao")){
                viewHolder.btn.setVisibility(View.GONE);
            }else if (PhuongThucDonHang.phuongThuc.equals("TraHang")){
                viewHolder.btn.setVisibility(View.GONE);
            }else if(PhuongThucDonHang.phuongThuc.equals("DaGiao") || PhuongThucDonHang.phuongThuc.equals("DaHuy")){
                viewHolder.btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String cmt = lichSuMuaHangList.get(position).getCMT();
                        String tensp = lichSuMuaHangList.get(position).getTenSP();
                        String gia = lichSuMuaHangList.get(position).getGia();
                        String ten = lichSuMuaHangList.get(position).getTenKH();
                        String diachi = lichSuMuaHangList.get(position).getDiaChi();
                        int soL = lichSuMuaHangList.get(position).getSoLuong();
                        String anh = lichSuMuaHangList.get(position).getHinhAnh();
                        String sdt = lichSuMuaHangList.get(position).getSdt();
                        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd-MM-yyyy _ HH:mm:ss", Locale.getDefault());
                        String date2 = simpleDateFormat2.format(new Date());
                        LichSuMuaHang lichSuMuaHang = new LichSuMuaHang(cmt, "Chờ xác nhận", ten, sdt, diachi, tensp, anh, gia, soL, date2, "");
                        mData.child("LichSuMuaHang").child("ChoXacNhan").push().setValue(lichSuMuaHang, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                if (error == null){
                                    context.ThongBaoMuaLaiHang();
                                }
                            }
                        });
                    }
                });
            }
        }catch (Exception e){

        }

        return convertView;
    }
}
