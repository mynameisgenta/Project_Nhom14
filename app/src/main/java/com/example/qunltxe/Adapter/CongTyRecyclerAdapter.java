package com.example.qunltxe.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qunltxe.Data_Models.CongTy;
import com.example.qunltxe.Database.DBCongTy;
import com.example.qunltxe.R;
import com.example.qunltxe.View_Models.QuanLyCongTy.ChinhSuaCongTy;
import com.example.qunltxe.View_Models.QuanLyCongTy.DanhSachCongTy;

import java.util.ArrayList;
import java.util.List;

public class CongTyRecyclerAdapter extends RecyclerView.Adapter<CongTyRecyclerAdapter.CongTyViewHolder> {

    List<CongTy> listCongTy;
    List<CongTy> listCongTy_filteredList;
    Context context;

    public CongTyRecyclerAdapter(Context context, List<CongTy> listCongTy) {
        this.context = context;
        this.listCongTy = listCongTy;
        this.listCongTy_filteredList = listCongTy;
    }

    @Override
    public CongTyRecyclerAdapter.CongTyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.congty_recycleview, parent, false);

        return new CongTyRecyclerAdapter.CongTyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CongTyRecyclerAdapter.CongTyViewHolder holder, int position) {
        final CongTy congTy = listCongTy_filteredList.get(position);
        holder.txt_maloai.setText(congTy.getMaLoai());
        holder.txt_tenloai.setText(congTy.getTenLoai());
        holder.txt_xuatxu.setText(congTy.getXuatXu());
        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(context)
                        .setMessage("Bạn muốn xóa công ty " + congTy.getTenLoai() + " ?")
                        .setCancelable(false)
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                DBCongTy dbCongTy = new DBCongTy(context);
                                dbCongTy.deleteCongty(congTy);
                                Toast.makeText(context, "Xóa thành công !", Toast.LENGTH_SHORT).show();
                                DanhSachCongTy danhSach = (DanhSachCongTy) context;
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
                Intent intent = new Intent(context, ChinhSuaCongTy.class);
                Bundle bundle = new Bundle();
                bundle.putString("maloai", congTy.getMaLoai());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listCongTy_filteredList.size();
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    listCongTy_filteredList = listCongTy;
                } else {
                    List<CongTy> filteredList = new ArrayList<>();
                    for (CongTy row : listCongTy) {
                        if (row.getMaLoai().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    listCongTy_filteredList = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = listCongTy_filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listCongTy_filteredList = (ArrayList<CongTy>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class CongTyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_maloai, txt_tenloai, txt_xuatxu;
        ImageView img_delete;
        ImageView img_edit;

        public CongTyViewHolder(View view) {
            super(view);
            txt_maloai = view.findViewById(R.id.txt_maloai);
            txt_tenloai = view.findViewById(R.id.txt_tenloai);
            txt_xuatxu = view.findViewById(R.id.txt_xuatxu);
            img_delete = view.findViewById(R.id.img_delete);
            img_edit = view.findViewById(R.id.img_edit);
        }
    }

}