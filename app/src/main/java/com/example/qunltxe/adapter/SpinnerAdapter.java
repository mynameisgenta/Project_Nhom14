package com.example.qunltxe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.qunltxe.R;
import com.example.qunltxe.data_models.Xe;

import java.util.List;

public class SpinnerAdapter extends ArrayAdapter<Xe> {

    int maXe;
    LayoutInflater inflater;
    private Context context;
    private List<Xe> values;

    public SpinnerAdapter(Context context, int textViewResourceId, int maXe, List<Xe> values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
        this.maXe = maXe;
        inflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public Xe getItem(int position) {
        return values.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertview, ViewGroup parent) {

        if (convertview == null) {
            convertview = inflater.inflate(R.layout.spinner_maloai_xe, parent, false);
            TextView maxe = (TextView) convertview.findViewById(R.id.text_spinner);
            maxe.setText(values.get(position).getMaXe());
        }
        return convertview;
    }
}
