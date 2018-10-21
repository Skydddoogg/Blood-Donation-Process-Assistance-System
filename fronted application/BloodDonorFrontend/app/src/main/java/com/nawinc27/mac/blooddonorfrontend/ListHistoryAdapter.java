package com.nawinc27.mac.blooddonorfrontend;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListHistoryAdapter extends ArrayAdapter<ListItemHistory> {

    Context context;
    List<ListItemHistory> items = new ArrayList<>();

    public ListHistoryAdapter(@NonNull Context context, int resource, @NonNull List<ListItemHistory> list) {
        super(context, resource, list);
        this.context = context;
        this.items = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View item = LayoutInflater.from(context).inflate(
                R.layout.list_item_history,
                parent,
                false
        );

        TextView counter = (TextView) item.findViewById(R.id.history_count);
        TextView time = (TextView) item.findViewById(R.id.history_time);
        TextView location = (TextView) item.findViewById(R.id.history_location);
        TextView hospital = (TextView) item.findViewById(R.id.history_hospital);

        String time_str = Integer.toString(items.get(position).getTimes());
        counter.setText(time_str);
        time.setText(items.get(position).getDate());
        location.setText(items.get(position).getLocation());
        hospital.setText(items.get(position).getHospital());





        return item;
    }
}
