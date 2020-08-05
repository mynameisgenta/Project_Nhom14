package com.example.qunltxe.screen.company;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qunltxe.R;
import com.example.qunltxe.adapter.CongTyRecyclerAdapter;
import com.example.qunltxe.data_models.CongTy;
import com.example.qunltxe.database.DBCongTy;
import com.example.qunltxe.screen.home.TrangChu;

import java.util.ArrayList;
import java.util.List;

public class DanhSachCty extends AppCompatActivity {
    RecyclerView recyclerViewCty;
    ImageView img_add;
    List<CongTy> listCty = new ArrayList<>();
    CongTyRecyclerAdapter congTyRecyclerAdapter;
    DBCongTy dbCongTy;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.danh_sach_cty);
        setControl();
        setEvent();
    }

    private void setControl() {
        recyclerViewCty = findViewById(R.id.recyclerViewCty);
        img_add = findViewById(R.id.img_add);
    }

    private void giaoDienDanhSach() {

        listCty = new ArrayList<>();
        congTyRecyclerAdapter = new CongTyRecyclerAdapter(this, listCty);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewCty.setLayoutManager(mLayoutManager);
        recyclerViewCty.setItemAnimator(new DefaultItemAnimator());
        recyclerViewCty.setHasFixedSize(true);
        recyclerViewCty.setAdapter(congTyRecyclerAdapter);
        dbCongTy = new DBCongTy(this);
    }

    private void setEvent() {
        img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DanhSachCty.this, ThemCongTy.class);
                startActivity(intent);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }
        });

        giaoDienDanhSach();
        UpdateData();
    }

    @SuppressLint("StaticFieldLeak")
    public void UpdateData() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                listCty.clear();
                listCty.addAll(dbCongTy.layDuLieuCongTy());

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                congTyRecyclerAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                congTyRecyclerAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                congTyRecyclerAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.menuItemHome:
                backHomePage();
                break;

            case R.id.action_search:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void backHomePage() {
        new AlertDialog.Builder(this)
                .setMessage("Về trang chính ?")
                .setCancelable(false)
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(DanhSachCty.this, TrangChu.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Perform Your Task Here--When No is pressed
                        dialog.cancel();
                    }
                }).show();
    }
}
