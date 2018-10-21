package com.nawinc27.mac.blooddonorfrontend;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment{
    private ArrayList<ListItemHistory> items = new ArrayList<>();
    public MainFragment(){
        //test

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ListView menuList = (ListView) getView().findViewById(R.id.main_history_donor);
        ListItemHistory list1 = new ListItemHistory(1, "วันที่ 2 มกราคม 2561" , "สุพรรณบุรี" , "โรงพยาบาลเดิมบางนางบวช");
        ListItemHistory list2 = new ListItemHistory(2, "วันที่ 3 มกราคม 2561" , "สุพรรณบุรี" , "โรงพยาบาลเดิมบางนางบวช");
        ListItemHistory list3 = new ListItemHistory(3, "วันที่ 4 มกราคม 2561" , "สุพรรณบุรี" , "โรงพยาบาลเดิมบางนางบวช");
        ListItemHistory list4 = new ListItemHistory(4, "วันที่ 5 มกราคม 2561" , "สุพรรณบุรี" , "โรงพยาบาลเดิมบางนางบวช");
        ListItemHistory list5 = new ListItemHistory(5, "วันที่ 6 มกราคม 2561" , "สุพรรณบุรี" , "โรงพยาบาลเดิมบางนางบวช");
        ListItemHistory list6 = new ListItemHistory(6, "วันที่ 7 มกราคม 2561" , "สุพรรณบุรี" , "โรงพยาบาลเดิมบางนางบวช");

        items.add(list6);
        items.add(list5);
        items.add(list4);
        items.add(list3);
        items.add(list2);
        items.add(list1);

        ListHistoryAdapter adapter = new ListHistoryAdapter(getActivity(),R.layout.list_item_history,items);
        menuList.setAdapter(adapter);


    }
}
