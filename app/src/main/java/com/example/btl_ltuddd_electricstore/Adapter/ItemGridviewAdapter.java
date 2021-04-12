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
import com.squareup.picasso.Picasso;

import java.util.List;

public class ItemGridviewAdapter extends BaseAdapter {
    private Context context;
    private List<SanPham> sanPhamList;
    private int layout;

    public ItemGridviewAdapter(Context context, List<SanPham> sanPhamList, int layout) {
        this.context = context;
        this.sanPhamList = sanPhamList;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return sanPhamList.size();
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
        TextView txt_tensp, txt_gia;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder viewHolder;
        if (convertView == null){
            convertView = inflater.inflate(layout,null);
            viewHolder = new ViewHolder();

            viewHolder.img = (ImageView) convertView.findViewById(R.id.img_item_gridview);
            viewHolder.txt_tensp = (TextView) convertView.findViewById(R.id.txt_tensp_gridview);
            viewHolder.txt_gia = (TextView) convertView.findViewById(R.id.txt_giasp_gridview);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Picasso.get().load(sanPhamList.get(position).getHinhAnh()).into(viewHolder.img);
        viewHolder.txt_tensp.setText(sanPhamList.get(position).getTenSP());
        viewHolder.txt_gia.setText(sanPhamList.get(position).getGia()+" vnđ");
        return convertView;
    }
}
