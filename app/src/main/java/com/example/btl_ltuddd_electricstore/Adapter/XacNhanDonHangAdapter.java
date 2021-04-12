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
        viewHolder.txt_gia.setText(lichSuMuaHangList.get(position).getGia() + " vnđ");
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
            //xác nhận những đơn hàng vừa được tạo
            viewHolder.btn_xacnhan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mData.child("LichSuMuaHang").child("ChoXacNhan").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            LichSuMuaHang lichSuMuaHang = snapshot.getValue(LichSuMuaHang.class);
                            // đối chiếu dữ liệu cần đưa sang child DangGiao và đồng thời lấy key để xóa dữ liệu đã được chuyển sang child DangGiao
                            if (lichSuMuaHangList.get(position).getTenSP().equals(lichSuMuaHang.getTenSP()) && lichSuMuaHangList.get(position).getTenKH().equals(lichSuMuaHang.getTenKH()) && lichSuMuaHangList.get(position).getNgayMua().equals(lichSuMuaHang.getNgayMua()) && lichSuMuaHangList.get(position).getCMT().equals(lichSuMuaHang.getCMT())){
                                mData.child("LichSuMuaHang").child("DangGiao").push().setValue(new LichSuMuaHang(lichSuMuaHang.getCMT(), "Đang giao", lichSuMuaHang.getTenKH(), lichSuMuaHang.getSdt(), lichSuMuaHang.getDiaChi(),lichSuMuaHang.getTenSP(), lichSuMuaHang.getHinhAnh(), lichSuMuaHang.getGia(), lichSuMuaHang.getSoLuong(), lichSuMuaHang.getNgayMua(), date));
                                key = snapshot.getKey();
                                mData.child("ThongBao").child("TrangThai").child(lichSuMuaHang.getCMT()).setValue(new ThongBao("",lichSuMuaHang.getCMT(), " đơn hàng đã được xác nhận", "DangGiao", date));
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
                        // thực hiện việc xóa và cập nhật lại ds
                        @Override
                        public void onFinish() {
                            mData.child("ThongBao").child("ChoXacNhan").child(lichSuMuaHangList.get(position).getCMT()).removeValue();
                            context.capNhatLai(key);
                        }
                    }.start();
                }
            });
            //hủy đơn hàng
            viewHolder.btn_huy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mData.child("LichSuMuaHang").child("ChoXacNhan").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            LichSuMuaHang lichSuMuaHang = snapshot.getValue(LichSuMuaHang.class);
                            //lấy key để xóa data ở child ChoXacNhan và truyền dữ liệu sang child DaHuy
                            if (lichSuMuaHangList.get(position).getTenSP().equals(lichSuMuaHang.getTenSP()) && lichSuMuaHangList.get(position).getTenKH().equals(lichSuMuaHang.getTenKH()) && lichSuMuaHangList.get(position).getNgayMua().equals(lichSuMuaHang.getNgayMua()) && lichSuMuaHangList.get(position).getCMT().equals(lichSuMuaHang.getCMT())){
                                mData.child("LichSuMuaHang").child("DaHuy").push().setValue(new LichSuMuaHang(lichSuMuaHang.getCMT(), "Hủy đơn hàng", lichSuMuaHang.getTenKH(), lichSuMuaHang.getSdt(), lichSuMuaHang.getDiaChi(),lichSuMuaHang.getTenSP(), lichSuMuaHang.getHinhAnh(), lichSuMuaHang.getGia(), lichSuMuaHang.getSoLuong(), lichSuMuaHang.getNgayMua(),date));
                                key2 = snapshot.getKey();
                                mData.child("ThongBao").child("TrangThai").child(lichSuMuaHang.getCMT()).setValue(new ThongBao("",lichSuMuaHang.getCMT(), " đơn hàng đã được hủy bỏ", "HuyBo", date));
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
                        // thực hiện việc xóa và cập nhật lại ds
                        @Override
                        public void onFinish() {
                            mData.child("ThongBao").child("ChoXacNhan").child(lichSuMuaHangList.get(position).getCMT()).removeValue();
                            context.capNhatLai(key2);
                        }
                    }.start();
                }
            });
        }else if (PhuongThucDonHang.phuongThuc.equals("DaHuy")){
            //làm ẩn các button khi không dùng đến
            viewHolder.btn_xacnhan.setVisibility(View.GONE);
            viewHolder.btn_huy.setVisibility(View.GONE);
        }else if (PhuongThucDonHang.phuongThuc.equals("DangGiao")){
            //xác nhận đơn hàng đã giao đến tay người dùng
            viewHolder.btn_xacnhan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mData.child("LichSuMuaHang").child("DangGiao").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            LichSuMuaHang lichSuMuaHang = snapshot.getValue(LichSuMuaHang.class);
                            //lấy key để xóa data ở child DangGiao và truyền dữ liệu sang child DaGiao
                            if (lichSuMuaHangList.get(position).getTenSP().equals(lichSuMuaHang.getTenSP()) && lichSuMuaHangList.get(position).getTenKH().equals(lichSuMuaHang.getTenKH()) && lichSuMuaHangList.get(position).getNgayMua().equals(lichSuMuaHang.getNgayMua()) && lichSuMuaHangList.get(position).getCMT().equals(lichSuMuaHang.getCMT())){
                                mData.child("LichSuMuaHang").child("DaGiao").push().setValue(new LichSuMuaHang(lichSuMuaHang.getCMT(), "Đã Giao", lichSuMuaHang.getTenKH(), lichSuMuaHang.getSdt(), lichSuMuaHang.getDiaChi(),lichSuMuaHang.getTenSP(), lichSuMuaHang.getHinhAnh(), lichSuMuaHang.getGia(), lichSuMuaHang.getSoLuong(), lichSuMuaHang.getNgayMua(),date));
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
                        //Thực hiện cập nhật lại ds và xóa dữ liệu cũ
                        @Override
                        public void onFinish() {
                            context.capNhatLai(key);
                        }
                    }.start();
                }
            });
            //hủy đơn hàng
            viewHolder.btn_huy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mData.child("LichSuMuaHang").child("DangGiao").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            LichSuMuaHang lichSuMuaHang = snapshot.getValue(LichSuMuaHang.class);
                            //lấy key để xóa data ở child DangGiao và truyền dữ liệu sang child DaHuy
                            if (lichSuMuaHangList.get(position).getTenSP().equals(lichSuMuaHang.getTenSP()) && lichSuMuaHangList.get(position).getTenKH().equals(lichSuMuaHang.getTenKH()) && lichSuMuaHangList.get(position).getNgayMua().equals(lichSuMuaHang.getNgayMua()) && lichSuMuaHangList.get(position).getCMT().equals(lichSuMuaHang.getCMT())){
                                mData.child("LichSuMuaHang").child("DaHuy").push().setValue(new LichSuMuaHang(lichSuMuaHang.getCMT(), "Hủy đơn hàng", lichSuMuaHang.getTenKH(), lichSuMuaHang.getSdt(), lichSuMuaHang.getDiaChi(),lichSuMuaHang.getTenSP(), lichSuMuaHang.getHinhAnh(), lichSuMuaHang.getGia(), lichSuMuaHang.getSoLuong(), lichSuMuaHang.getNgayMua(),date));
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
            viewHolder.btn_huy.setText("Xác nhận trả hàng");
            // Thực hiện xác nhận hoàn trả lại hàng
            viewHolder.btn_huy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mData.child("LichSuMuaHang").child("DaGiao").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            LichSuMuaHang lichSuMuaHang = snapshot.getValue(LichSuMuaHang.class);
                            //lấy key để xóa data ở child DaGiao và truyền dữ liệu sang child TraHang
                            if (lichSuMuaHangList.get(position).getTenSP().equals(lichSuMuaHang.getTenSP()) && lichSuMuaHangList.get(position).getTenKH().equals(lichSuMuaHang.getTenKH()) && lichSuMuaHangList.get(position).getNgayMua().equals(lichSuMuaHang.getNgayMua()) && lichSuMuaHangList.get(position).getCMT().equals(lichSuMuaHang.getCMT())){
                                mData.child("LichSuMuaHang").child("TraHang").push().setValue(new LichSuMuaHang(lichSuMuaHang.getCMT(), "Trả hàng", lichSuMuaHang.getTenKH(), lichSuMuaHang.getSdt(), lichSuMuaHang.getDiaChi(),lichSuMuaHang.getTenSP(), lichSuMuaHang.getHinhAnh(), lichSuMuaHang.getGia(), lichSuMuaHang.getSoLuong(), lichSuMuaHang.getNgayMua(),date));
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
            //làm ẩn các button khi ko dùng đến
            viewHolder.btn_xacnhan.setVisibility(View.GONE);
            viewHolder.btn_huy.setVisibility(View.GONE);
        }
        return convertView;
    }
}
