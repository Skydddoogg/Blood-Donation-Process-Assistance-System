package com.example.blooddonorapp.blooddonorrecorder.history;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.blooddonorapp.blooddonorrecorder.R;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends ArrayAdapter<History> {
    List<History> histories = new ArrayList<History>();
    Context context;

    public HistoryAdapter(@NonNull Context context, int resource, List<History> objects) {
        super(context, resource, objects);
        this.histories = objects;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View historyItem = LayoutInflater.from(context)
                .inflate(R.layout.fragment_history_item, parent, false);
        TextView date = historyItem.findViewById(R.id.history_item_date);
        TextView hospital = historyItem.findViewById(R.id.history_item_location);
        TextView checker = historyItem.findViewById(R.id.history_item_keeper);

        History row = histories.get(position);
        date.setText("วันที่ : " + row.getDate());
        hospital.setText("สถานที่บริจาค : " + row.getHospital());
        checker.setText("ผู้เก็บ : " + row.getChecker());

        return historyItem;

    }
}
