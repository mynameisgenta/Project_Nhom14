package com.example.qunltxe.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qunltxe.R;
import com.example.qunltxe.data_models.DonHang;
import com.example.qunltxe.data_models.Xe;
import com.example.qunltxe.database.DBDonDatHang;
import com.example.qunltxe.database.DBXe;
import com.example.qunltxe.screen.order.ChiTietDonHang;
import com.example.qunltxe.screen.order.DanhSachDonHang;

import java.util.ArrayList;
import java.util.List;

public class DonHangRecyclerAdapter extends RecyclerView.Adapter<DonHangRecyclerAdapter.DonHangViewHolder> {

    List<DonHang> listDonHang;
    List<DonHang> listDonHang_filteredList;
    Context context;
    String maXe;
    DBXe dbXe;
    int soLuongXeDB, soLuongXeDat;

    public DonHangRecyclerAdapter(Context context, List<DonHang> listDonHang) {
        this.context = context;
        this.listDonHang = listDonHang;
        this.listDonHang_filteredList = listDonHang;
    }

    @Override
    public DonHangRecyclerAdapter.DonHangViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.donhang_recyclerview, parent, false);
        return new DonHangRecyclerAdapter.DonHangViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DonHangRecyclerAdapter.DonHangViewHolder holder, int position) {
        final DonHang donHang = listDonHang_filteredList.get(position);
        holder.tvMaddh.setText(donHang.getMaDonHang());
        holder.tvNgaydat.setText(donHang.getNgayDat());

        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(context)
                        .setMessage("Bạn muốn xóa đơn hàng " + donHang.getMaDonHang() + " ?")
                        .setCancelable(false)
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                maXe = donHang.getMaXe();
                                soLuongXeDat = donHang.getSoLuongDat();
                                DBDonDatHang dbDonDatHang = new DBDonDatHang(context);
                                dbDonDatHang.deleteDonHang(donHang);
                                Toast.makeText(context, "Xóa thành công !", Toast.LENGTH_SHORT).show();
                                updateSoLuongXe();
                                DanhSachDonHang danhSach = (DanhSachDonHang) context;
                                danhSach.UpdateData();
                            }
                        })
                        .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).show();
            }
        });

        holder.img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChiTietDonHang.class);
                Bundle bundle = new Bundle();
                bundle.putString("madonhang", donHang.getMaDonHang());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

    }

    public int soLuongSauKhiXoa() {
        dbXe = new DBXe(context);
        soLuongXeDB = dbXe.getSoLuongByMaXe(maXe);
        Log.d("abcd", "soLuongSauKhiXoa: " + soLuongXeDB);
        int soLuong = 0;
        int soLuongDatHang = soLuongXeDat;
        int soLuongXeHienCo = soLuongXeDB;
        soLuong = soLuongXeHienCo + soLuongDatHang;
        return soLuong;
    }

    public Xe getXe() {
        int soLuongXeConLai = soLuongSauKhiXoa();
        Xe xe = new Xe();
        xe.setMaXe(maXe);
        xe.setSoLuong(soLuongXeConLai);
        return xe;
    }

    public void updateSoLuongXe() {
        Xe xe = getXe();
        dbXe.updateSoLuongXe(xe);
    }

    @Override
    public int getItemCount() {
        return listDonHang_filteredList.size();
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    listDonHang_filteredList = listDonHang;
                } else {
                    List<DonHang> filteredList = new ArrayList<>();
                    for (DonHang row : listDonHang) {
                        if (row.getMaDonHang().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    listDonHang_filteredList = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = listDonHang_filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listDonHang_filteredList = (ArrayList<DonHang>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class DonHangViewHolder extends RecyclerView.ViewHolder {

        TextView tvMaddh, tvNgaydat;
        ImageView img_delete;
        ImageView img_edit;

        public DonHangViewHolder(View view) {
            super(view);
            tvMaddh = view.findViewById(R.id.tvMaddh);
            tvNgaydat = view.findViewById(R.id.tvNgaydat);
            img_delete = view.findViewById(R.id.img_delete);
            img_edit = view.findViewById(R.id.img_edit);
        }
    }
}