package com.example.j.msscanner;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

class CustomAdaptor extends ArrayAdapter<result>{
    public CustomAdaptor(@NonNull Context context, ArrayList<result> results) {
        super(context,R.layout.custom_row, results);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final String title = getItem(position).getTitle();
        final String msurl = getItem(position).getMsurl();
        final String qpurl = getItem(position).getQpurl();

        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View customView = layoutInflater.inflate(R.layout.custom_row, parent, false);

        TextView titleview = customView.findViewById(R.id.titleview);
        TextView qpview = customView.findViewById(R.id.qpview);
        TextView msview = customView.findViewById(R.id.msview);
        titleview.setText(title);
        qpview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(qpurl));
                getContext().startActivity(i);
            }
        });
        msview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(msurl));
                getContext().startActivity(i);
            }
        });
        return customView;
    }



}
