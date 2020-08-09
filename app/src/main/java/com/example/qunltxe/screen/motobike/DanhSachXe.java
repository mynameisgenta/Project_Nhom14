package com.example.qunltxe.screen.motobike;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qunltxe.R;
import com.example.qunltxe.adapter.XeRecyclerAdapter;
import com.example.qunltxe.data_models.Xe;
import com.example.qunltxe.database.DBXe;
import com.example.qunltxe.screen.home.TrangChu;

import java.util.ArrayList;
import java.util.List;

public class DanhSachXe extends AppCompatActivity {
    RecyclerView recyclerViewXe;
    ImageView img_add;
    List<Xe> listXe = new ArrayList<>();
    XeRecyclerAdapter xeRecyclerAdapter;
    DBXe dbXe;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.danh_sach_xe);
        getSupportActionBar().setTitle("Danh sách xe");
        setControl();
        setEvent();
    }

    private void setControl() {
        recyclerViewXe = findViewById(R.id.recyclerViewXe);
        img_add = findViewById(R.id.img_add);
    }

    private void giaoDienDanhSach() {
        listXe = new ArrayList<>();
        xeRecyclerAdapter = new XeRecyclerAdapter(this, listXe);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewXe.setLayoutManager(mLayoutManager);
        recyclerViewXe.setItemAnimator(new DefaultItemAnimator());
        recyclerViewXe.setHasFixedSize(true);
        recyclerViewXe.setAdapter(xeRecyclerAdapter);
        dbXe = new DBXe(this);
    }

    private void setEvent() {

        img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DanhSachXe.this, ThemXe.class);
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
                listXe.clear();
                listXe.addAll(dbXe.layDuLieuXe());

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                xeRecyclerAdapter.notifyDataSetChanged();
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
                xeRecyclerAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                xeRecyclerAdapter.getFilter().filter(query);
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
        Intent intent = new Intent(DanhSachXe.this, TrangChu.class);
        startActivity(intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }

}

