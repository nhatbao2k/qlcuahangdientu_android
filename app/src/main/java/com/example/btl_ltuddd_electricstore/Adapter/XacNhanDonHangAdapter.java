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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.btl_ltuddd_electricstore.Activity.quan_ly_ban_hang_Activity;
import com.example.btl_ltuddd_electricstore.Activity.xac_nhan_don_hang_Activity;
import com.example.btl_ltuddd_electricstore.R;
import com.example.btl_ltuddd_electricstore.object.LichSuMuaHang;
import com.example.btl_ltuddd_electricstore.object.PhuongThucDonHang;
import com.example.btl_ltuddd_electricstore.object.TenUser;
import com.example.btl_ltuddd_electricstore.object.ThongBao;
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

public class XacNhanDonHangAdapter extends BaseAdapter {

    private xac_nhan_don_hang_Activity context;
    private List<LichSuMuaHang> lichSuMuaHangList;
    private int layout;
    DatabaseReference mData;

    public XacNhanDonHangAdapter(xac_nhan_don_hang_Activity context, List<LichSuMuaHang> lichSuMuaHangList, int layout) {
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
        TextView txt_tensp, txt_gia, txt_soLuong, txt_nguoimua, txt_diachi, txt_ngay, txt_sdt, txt_ngay_xn, txt_tinhtrang;
        Button btn_xacnhan, btn_huy;
    }
    public String key;
    public String key2;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder viewHolder;
        if (convertView == null){
            convertView = inflater.inflate(layout,null);
            viewHolder = new ViewHolder();

            viewHolder.img = (ImageView) convertView.findViewById(R.id.img_item_xnql);
            viewHolder.txt_tensp = (TextView) convertView.findViewById(R.id.txt_ten_sp_xnql);
            viewHolder.txt_soLuong = (TextView) convertView.findViewById(R.id.txt_sol_xnql);
            viewHolder.txt_gia = (TextView) convertView.findViewById(R.id.txt_gia_sp_xnql);
            viewHolder.txt_nguoimua = (TextView) convertView.findViewById(R.id.txt_ten_nguoi_mua_xnql);
            viewHolder.txt_diachi = (TextView) convertView.findViewById(R.id.txt_dia_chi_xnql);
            viewHolder.txt_ngay = (TextView) convertView.findViewById(R.id.txt_ngay_xnql);
            viewHolder.btn_xacnhan = (Button) convertView.findViewById(R.id.btn_xac_nhan_hang);
            viewHolder.btn_huy = (Button) convertView.findViewById(R.id.btn_huy_xac_nhan_hang);
            viewHolder.txt_sdt = (TextView) convertView.findViewById(R.id.txt_sdt_xnql);
            viewHolder.txt_ngay_xn = (TextView) convertView.findViewById(R.id.txt_ngayxn_xnql);
            viewHolder.txt_tinhtrang = (TextView) convertView.findViewById(R.id.txt_tinhtrang_xnql);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Picasso.get().load(lichSuMuaHangList.get(position).getHinhAnh()).into(viewHolder.img);
        viewHolder.txt_tensp.setText(lichSuMuaHangList.get(position).getTenSP());
        viewHolder.txt_soLuong.setText(lichSuMuaHangList.get(position).getSoLuong()+"");
        viewHolder.txt_gia.setText(lichSuMuaHangList.get(position).getGia() + " vn??");
        viewHolder.txt_nguoimua.setText(lichSuMuaHangList.get(position).getTenKH());
        viewHolder.txt_diachi.setText(lichSuMuaHangList.get(position).getDiaChi());
        viewHolder.txt_ngay.setText(lichSuMuaHangList.get(position).getNgayMua());
        viewHolder.txt_sdt.setText(lichSuMuaHangList.get(position).getSdt());
        viewHolder.txt_ngay_xn.setText(lichSuMuaHangList.get(position).getNgayXacNhan());
        viewHolder.txt_tinhtrang.setText(lichSuMuaHangList.get(position).getTrangThai());

        mData = FirebaseDatabase.getInstance().getReference();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy _ HH:mm:ss", Locale.getDefault());
        String date = simpleDateFormat.format(new Date());
        if (PhuongThucDonHang.phuongThuc.equals("ChoXacNhan")){
            //x??c nh???n nh???ng ????n h??ng v???a ???????c t???o
            viewHolder.btn_xacnhan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mData.child("LichSuMuaHang").child("ChoXacNhan").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            LichSuMuaHang lichSuMuaHang = snapshot.getValue(LichSuMuaHang.class);
                            // ?????i chi???u d??? li???u c???n ????a sang child DangGiao v?? ?????ng th???i l???y key ????? x??a d??? li???u ???? ???????c chuy???n sang child DangGiao
                            if (lichSuMuaHangList.get(position).getTenSP().equals(lichSuMuaHang.getTenSP()) && lichSuMuaHangList.get(position).getTenKH().equals(lichSuMuaHang.getTenKH()) && lichSuMuaHangList.get(position).getNgayMua().equals(lichSuMuaHang.getNgayMua()) && lichSuMuaHangList.get(position).getCMT().equals(lichSuMuaHang.getCMT())){
                                mData.child("LichSuMuaHang").child("DangGiao").push().setValue(new LichSuMuaHang(lichSuMuaHang.getCMT(), "??ang giao", lichSuMuaHang.getTenKH(), lichSuMuaHang.getSdt(), lichSuMuaHang.getDiaChi(),lichSuMuaHang.getTenSP(), lichSuMuaHang.getHinhAnh(), lichSuMuaHang.getGia(), lichSuMuaHang.getSoLuong(), lichSuMuaHang.getNgayMua(), date));
                                key = snapshot.getKey();
                                mData.child("ThongBao").child("TrangThai").child(lichSuMuaHang.getCMT()).setValue(new ThongBao("",lichSuMuaHang.getCMT(), " ????n h??ng ???? ???????c x??c nh???n", "DangGiao", date));
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
                        // th???c hi???n vi???c x??a v?? c???p nh???t l???i ds
                        @Override
                        public void onFinish() {
                            mData.child("ThongBao").child("ChoXacNhan").child(lichSuMuaHangList.get(position).getCMT()).removeValue();
                            context.capNhatLai(key);
                        }
                    }.start();
                }
            });
            //h???y ????n h??ng
            viewHolder.btn_huy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mData.child("LichSuMuaHang").child("ChoXacNhan").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            LichSuMuaHang lichSuMuaHang = snapshot.getValue(LichSuMuaHang.class);
                            //l???y key ????? x??a data ??? child ChoXacNhan v?? truy???n d??? li???u sang child DaHuy
                            if (lichSuMuaHangList.get(position).getTenSP().equals(lichSuMuaHang.getTenSP()) && lichSuMuaHangList.get(position).getTenKH().equals(lichSuMuaHang.getTenKH()) && lichSuMuaHangList.get(position).getNgayMua().equals(lichSuMuaHang.getNgayMua()) && lichSuMuaHangList.get(position).getCMT().equals(lichSuMuaHang.getCMT())){
                                mData.child("LichSuMuaHang").child("DaHuy").push().setValue(new LichSuMuaHang(lichSuMuaHang.getCMT(), "H???y ????n h??ng", lichSuMuaHang.getTenKH(), lichSuMuaHang.getSdt(), lichSuMuaHang.getDiaChi(),lichSuMuaHang.getTenSP(), lichSuMuaHang.getHinhAnh(), lichSuMuaHang.getGia(), lichSuMuaHang.getSoLuong(), lichSuMuaHang.getNgayMua(),date));
                                key2 = snapshot.getKey();
                                mData.child("ThongBao").child("TrangThai").child(lichSuMuaHang.getCMT()).setValue(new ThongBao("",lichSuMuaHang.getCMT(), " ????n h??ng ???? ???????c h???y b???", "HuyBo", date));
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
                        // th???c hi???n vi???c x??a v?? c???p nh???t l???i ds
                        @Override
                        public void onFinish() {
                            mData.child("ThongBao").child("ChoXacNhan").child(lichSuMuaHangList.get(position).getCMT()).removeValue();
                            context.capNhatLai(key2);
                        }
                    }.start();
                }
            });
        }else if (PhuongThucDonHang.phuongThuc.equals("DaHuy")){
            //l??m ???n c??c button khi kh??ng d??ng ?????n
            viewHolder.btn_xacnhan.setVisibility(View.GONE);
            viewHolder.btn_huy.setVisibility(View.GONE);
        }else if (PhuongThucDonHang.phuongThuc.equals("DangGiao")){
            //x??c nh???n ????n h??ng ???? giao ?????n tay ng?????i d??ng
            viewHolder.btn_xacnhan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mData.child("LichSuMuaHang").child("DangGiao").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            LichSuMuaHang lichSuMuaHang = snapshot.getValue(LichSuMuaHang.class);
                            //l???y key ????? x??a data ??? child DangGiao v?? truy???n d??? li???u sang child DaGiao
                            if (lichSuMuaHangList.get(position).getTenSP().equals(lichSuMuaHang.getTenSP()) && lichSuMuaHangList.get(position).getTenKH().equals(lichSuMuaHang.getTenKH()) && lichSuMuaHangList.get(position).getNgayMua().equals(lichSuMuaHang.getNgayMua()) && lichSuMuaHangList.get(position).getCMT().equals(lichSuMuaHang.getCMT())){
                                mData.child("LichSuMuaHang").child("DaGiao").push().setValue(new LichSuMuaHang(lichSuMuaHang.getCMT(), "???? Giao", lichSuMuaHang.getTenKH(), lichSuMuaHang.getSdt(), lichSuMuaHang.getDiaChi(),lichSuMuaHang.getTenSP(), lichSuMuaHang.getHinhAnh(), lichSuMuaHang.getGia(), lichSuMuaHang.getSoLuong(), lichSuMuaHang.getNgayMua(),date));
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
                        //Th???c hi???n c???p nh???t l???i ds v?? x??a d??? li???u c??
                        @Override
                        public void onFinish() {
                            context.capNhatLai(key);
                        }
                    }.start();
                }
            });
            //h???y ????n h??ng
            viewHolder.btn_huy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mData.child("LichSuMuaHang").child("DangGiao").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            LichSuMuaHang lichSuMuaHang = snapshot.getValue(LichSuMuaHang.class);
                            //l???y key ????? x??a data ??? child DangGiao v?? truy???n d??? li???u sang child DaHuy
                            if (lichSuMuaHangList.get(position).getTenSP().equals(lichSuMuaHang.getTenSP()) && lichSuMuaHangList.get(position).getTenKH().equals(lichSuMuaHang.getTenKH()) && lichSuMuaHangList.get(position).getNgayMua().equals(lichSuMuaHang.getNgayMua()) && lichSuMuaHangList.get(position).getCMT().equals(lichSuMuaHang.getCMT())){
                                mData.child("LichSuMuaHang").child("DaHuy").push().setValue(new LichSuMuaHang(lichSuMuaHang.getCMT(), "H???y ????n h??ng", lichSuMuaHang.getTenKH(), lichSuMuaHang.getSdt(), lichSuMuaHang.getDiaChi(),lichSuMuaHang.getTenSP(), lichSuMuaHang.getHinhAnh(), lichSuMuaHang.getGia(), lichSuMuaHang.getSoLuong(), lichSuMuaHang.getNgayMua(),date));
                                key2 = snapshot.getKey();
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
                            context.capNhatLai(key2);
                        }
                    }.start();
                }
            });
        }else if (PhuongThucDonHang.phuongThuc.equals("DaGiao")){
            viewHolder.btn_xacnhan.setVisibility(View.GONE);
            viewHolder.btn_huy.setText("X??c nh???n tr??? h??ng");
            // Th???c hi???n x??c nh???n ho??n tr??? l???i h??ng
            viewHolder.btn_huy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mData.child("LichSuMuaHang").child("DaGiao").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            LichSuMuaHang lichSuMuaHang = snapshot.getValue(LichSuMuaHang.class);
                            //l???y key ????? x??a data ??? child DaGiao v?? truy???n d??? li???u sang child TraHang
                            if (lichSuMuaHangList.get(position).getTenSP().equals(lichSuMuaHang.getTenSP()) && lichSuMuaHangList.get(position).getTenKH().equals(lichSuMuaHang.getTenKH()) && lichSuMuaHangList.get(position).getNgayMua().equals(lichSuMuaHang.getNgayMua()) && lichSuMuaHangList.get(position).getCMT().equals(lichSuMuaHang.getCMT())){
                                mData.child("LichSuMuaHang").child("TraHang").push().setValue(new LichSuMuaHang(lichSuMuaHang.getCMT(), "Tr??? h??ng", lichSuMuaHang.getTenKH(), lichSuMuaHang.getSdt(), lichSuMuaHang.getDiaChi(),lichSuMuaHang.getTenSP(), lichSuMuaHang.getHinhAnh(), lichSuMuaHang.getGia(), lichSuMuaHang.getSoLuong(), lichSuMuaHang.getNgayMua(),date));
                                key2 = snapshot.getKey();
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
                            context.capNhatLai(key2);
                        }
                    }.start();
                }
            });
        }else if (PhuongThucDonHang.phuongThuc.equals("TraHang")){
            //l??m ???n c??c button khi ko d??ng ?????n
            viewHolder.btn_xacnhan.setVisibility(View.GONE);
            viewHolder.btn_huy.setVisibility(View.GONE);
        }
        return convertView;
    }
}
