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

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qunltxe.R;
import com.example.qunltxe.data_models.Xe;
import com.example.qunltxe.database.DBXe;
import com.example.qunltxe.screen.motobike.ChinhSuaXe;
import com.example.qunltxe.screen.motobike.DanhSachXe;

import java.util.ArrayList;
import java.util.List;

public class XeRecyclerAdapter extends RecyclerView.Adapter<XeRecyclerAdapter.XeViewHolder> {

    List<Xe> listXe;
    List<Xe> listXe_filteredList;
    Context context;

    public XeRecyclerAdapter(Context context, List<Xe> listXe) {
        this.context = context;
        this.listXe = listXe;
        this.listXe_filteredList = listXe;
    }

    @Override
    public XeRecyclerAdapter.XeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.xe_recycleview, parent, false);

        return new XeRecyclerAdapter.XeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(XeRecyclerAdapter.XeViewHolder holder, int position) {
        final Xe xe = listXe_filteredList.get(position);
        holder.tvMaloai.setText(xe.getMaLoai());
        holder.tvMaxe.setText(xe.getMaXe());
        holder.tvTenxe.setText(xe.getTenXe());
        holder.tvDungtich.setText(String.valueOf(xe.getDungTich()));
        holder.tvSoluong.setText(String.valueOf(xe.getSoLuong()));
        holder.tvDongia.setText(String.valueOf(xe.getDonGia()));
        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(context)
                        .setMessage("Bạn muốn xóa xe " + xe.getTenXe() + " ?")
                        .setCancelable(false)
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                DBXe dbXe = new DBXe(context);
                                dbXe.deleteMoto(xe);
                                Toast.makeText(context, "Xóa thành công !", Toast.LENGTH_SHORT).show();
                                DanhSachXe danhSach = (DanhSachXe) context;
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
                Intent intent = new Intent(context, ChinhSuaXe.class);
                Bundle bundle = new Bundle();
                bundle.putString("maxe", xe.getMaXe());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listXe_filteredList.size();
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    listXe_filteredList = listXe;
                } else {
                    List<Xe> filteredList = new ArrayList<>();
                    for (Xe row : listXe) {
                        if (row.getMaXe().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    listXe_filteredList = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = listXe_filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listXe_filteredList = (ArrayList<Xe>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class XeViewHolder extends RecyclerView.ViewHolder {

        TextView tvMaloai, tvMaxe, tvTenxe, tvDungtich, tvSoluong, tvDongia;
        ImageView img_delete;
        ImageView img_edit;

        public XeViewHolder(View view) {
            super(view);
            tvMaloai = view.findViewById(R.id.tvMaloai);
            tvMaxe = view.findViewById(R.id.tvMaxe);
            tvTenxe = view.findViewById(R.id.tvTenxe);
            tvDungtich = view.findViewById(R.id.tvDungtich);
            tvSoluong = view.findViewById(R.id.tvSoluong);
            tvDongia = view.findViewById(R.id.tvDongia);
            img_delete = view.findViewById(R.id.img_delete);
            img_edit = view.findViewById(R.id.img_edit);
        }
    }
}
