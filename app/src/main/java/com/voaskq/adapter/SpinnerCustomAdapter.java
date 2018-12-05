package com.voaskq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.voaskq.R;

public class SpinnerCustomAdapter  extends BaseAdapter {
    Context context;
    String[] itemname_arr;
    LayoutInflater inflter;

    public SpinnerCustomAdapter(Context applicationContext, String[] itemname_arr) {
        this.context = applicationContext;
        this.itemname_arr = itemname_arr;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return itemname_arr.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.custom_spinner_items, null);
        TextView names = (TextView) view.findViewById(R.id.textView);

        names.setText(itemname_arr[i]);
        return view;
    }
}