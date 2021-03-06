package com.example.qunltxe.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.example.qunltxe.data_models.User;
import com.example.qunltxe.database.DBUser;
import com.example.qunltxe.screen.register.ChinhSuaTaiKhoan;
import com.example.qunltxe.screen.register.DanhSachTaiKhoan;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.qunltxe.screen.login.DangNhap.getPref;

public class UserRecyclerAdapter extends RecyclerView.Adapter<UserRecyclerAdapter.UserViewHolder> {

    List<User> listUsers;
    List<User> listUsers_filteredList;
    Context context;

    public UserRecyclerAdapter(Context context, List<User> listUsers) {
        this.context = context;
        this.listUsers = listUsers;
        this.listUsers_filteredList = listUsers;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_recycleview, parent, false);

        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        final User user = listUsers_filteredList.get(position);
        holder.textViewUsername.setText(user.getUsername());
        holder.textViewFullName.setText(user.getFullname());
        holder.textViewPassword.setText(user.getPassword());
        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String current_username = getPref("username", context.getApplicationContext());
                String username_data = user.getUsername();
                boolean resultDeleteUser = current_username.contentEquals(username_data);

                if (resultDeleteUser == true) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle("Lỗi");
                    alert.setMessage("Bạn đang sử dụng tài khoản này");
                    alert.setPositiveButton("OK", null);
                    alert.show();
                } else {
                    showDeleteUserDialog(user);
                }
            }
        });

        holder.img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChinhSuaTaiKhoan.class);
                Bundle bundle = new Bundle();
                bundle.putString("user_name", user.getUsername());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        if (user.getImage() != null) {
            holder.img_Profile.setImageBitmap(convertByteArrayToBitmap(user.getImage()));
        }
    }

    public Bitmap convertByteArrayToBitmap(byte[] byteArray) {
        ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(byteArray);
        Bitmap bitmap = BitmapFactory.decodeStream(arrayInputStream);
        return bitmap;
    }

    private void showDeleteUserDialog(final User user) {
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setMessage("Bạn muốn xóa tài khoản " + user.getUsername() + " ?")
                .setCancelable(false)
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        DBUser dbUser = new DBUser(context);
                        dbUser.xoaTaiKhoan(user);
                        Toast.makeText(context, "Xóa thành công !", Toast.LENGTH_SHORT).show();
                        DanhSachTaiKhoan danhSach = (DanhSachTaiKhoan) context;
                        danhSach.UpdateData();
                    }
                })
                .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
    }

    @Override
    public int getItemCount() {
        return listUsers_filteredList.size();
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    listUsers_filteredList = listUsers;
                } else {
                    List<User> filteredList = new ArrayList<>();
                    for (User row : listUsers) {
                        if (row.getUsername().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    listUsers_filteredList = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = listUsers_filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listUsers_filteredList = (ArrayList<User>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        TextView textViewUsername;
        TextView textViewFullName;
        TextView textViewPassword;
        ImageView img_delete;
        ImageView img_edit;
        CircleImageView img_Profile;

        public UserViewHolder(View view) {
            super(view);
            textViewUsername = view.findViewById(R.id.textViewUsername);
            textViewFullName = view.findViewById(R.id.textViewFullName);
            textViewPassword = view.findViewById(R.id.textViewPassword);
            img_Profile = view.findViewById(R.id.imgUserPicture);
            img_delete = view.findViewById(R.id.img_delete);
            img_edit = view.findViewById(R.id.img_edit);
        }
    }
}