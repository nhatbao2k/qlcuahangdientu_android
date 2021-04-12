package com.example.btl_ltuddd_electricstore.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.btl_ltuddd_electricstore.R;
import com.example.btl_ltuddd_electricstore.object.SanPham;
import com.example.btl_ltuddd_electricstore.Activity.quan_ly_ban_hang_Activity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ItemQuanLyAdapter extends BaseAdapter {
    private quan_ly_ban_hang_Activity context;
    private int layout;
    private List<SanPham> sanPhamList;

    public ItemQuanLyAdapter(quan_ly_ban_hang_Activity context, int layout, List<SanPham> sanPhamList) {
        this.context = context;
        this.layout = layout;
        this.sanPhamList = sanPhamList;
    }

    @Override
    public int getCount() {
        return sanPhamList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
//        return sanPhamList.get(position);
    }

    @Override
    public long getItemId(int position) {
//        return position;
        return 0;
    }

    private class ViewHolder {
        ImageView imgHinhAnh;
        TextView txt_maSP, txt_tenSP, txt_maLSP, txt_gia, txt_mota;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = convertView;
        ViewHolder viewHolder;
        if (rowView == null)
        {
            rowView = inflater.inflate(layout, null);
            viewHolder = new ViewHolder();
            viewHolder.imgHinhAnh =(ImageView) rowView.findViewById(R.id.img_item);
            viewHolder.txt_maSP =(TextView) rowView.findViewById(R.id.txt_masp);
            viewHolder.txt_maLSP = (TextView) rowView.findViewById(R.id.txt_maloai);
            viewHolder.txt_tenSP = (TextView) rowView.findViewById(R.id.txt_tensp);
            viewHolder.txt_gia = (TextView) rowView.findViewById(R.id.txt_gia);
            viewHolder.txt_mota = (TextView) rowView.findViewById(R.id.txt_mota_item);

            rowView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) rowView.getTag();
        }

        viewHolder.txt_maSP.setText(sanPhamList.get(position).getMaSP());
        viewHolder.txt_tenSP.setText(sanPhamList.get(position).getTenSP());
        viewHolder.txt_maLSP.setText(sanPhamList.get(position).getMaLoaiSP());
        viewHolder.txt_gia.setText(sanPhamList.get(position).getGia()+" vnđ");
        viewHolder.txt_mota.setText(sanPhamList.get(position).getMoTa());

        Picasso.get().load(sanPhamList.get(position).getHinhAnh()).into(viewHolder.imgHinhAnh);

        return rowView;
    }
}
