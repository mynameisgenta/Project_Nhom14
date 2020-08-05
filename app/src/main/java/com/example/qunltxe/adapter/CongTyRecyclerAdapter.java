package com.example.qunltxe.adapter;

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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qunltxe.R;
import com.example.qunltxe.data_models.CongTy;
import com.example.qunltxe.database.DBCongTy;
import com.example.qunltxe.screen.company.ChinhSuaCongTy;
import com.example.qunltxe.screen.company.DanhSachCty;

import java.util.ArrayList;
import java.util.List;

public class CongTyRecyclerAdapter extends RecyclerView.Adapter<CongTyRecyclerAdapter.CongTyViewHolder> {
    Context context;
    List<CongTy> listCty;
    List<CongTy> listCty_filteredList;

    public CongTyRecyclerAdapter(Context context, List<CongTy> listCty) {
        this.context = context;
        this.listCty = listCty;
        this.listCty_filteredList = listCty;
    }

    @Override
    public CongTyRecyclerAdapter.CongTyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cty_recyclerview, parent, false);

        return new CongTyRecyclerAdapter.CongTyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CongTyRecyclerAdapter.CongTyViewHolder holder, int position) {
        final CongTy congTy = listCty_filteredList.get(position);
        holder.tv_maloai.setText(congTy.getMaLoai());
        holder.tv_tenloai.setText(congTy.getTenLoai());
        holder.tv_xuatxu.setText(congTy.getXuatXu());
        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(context)
                        .setMessage("Bạn muốn xóa công ty " + congTy.getTenLoai() + " ?")
                        .setCancelable(false)
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                DBCongTy dbCongTy = new DBCongTy(context);
                                dbCongTy.deleteCongty(congTy);
                                Toast.makeText(context, "Xóa thành công !", Toast.LENGTH_SHORT).show();
                                DanhSachCty danhSach = (DanhSachCty) context;
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
        return listCty_filteredList.size();
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    listCty_filteredList = listCty;
                } else {
                    List<CongTy> filteredList = new ArrayList<>();
                    for (CongTy row : listCty) {
                        if (row.getMaLoai().toLowerCase().contains(charString.toLowerCase()) || row.getTenLoai().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    listCty_filteredList = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = listCty_filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listCty_filteredList = (ArrayList<CongTy>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class CongTyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_maloai, tv_tenloai, tv_xuatxu;
        ImageView img_delete, img_edit;

        public CongTyViewHolder(View view) {
            super(view);
            tv_maloai = view.findViewById(R.id.tv_maloai);
            tv_tenloai = view.findViewById(R.id.tv_tenloai);
            tv_xuatxu = view.findViewById(R.id.tv_xuatxu);
            img_delete = view.findViewById(R.id.img_delete);
            img_edit = view.findViewById(R.id.img_edit);
        }

    }
}
