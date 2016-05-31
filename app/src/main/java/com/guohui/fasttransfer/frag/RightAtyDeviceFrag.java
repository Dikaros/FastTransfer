package com.guohui.fasttransfer.frag;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.guohui.fasttransfer.R;
import com.guohui.fasttransfer.utils.DBUtils;

import java.util.ArrayList;

/**
 * Created by nangua on 2016/5/13.
 */
public class RightAtyDeviceFrag extends Fragment {
    ArrayList<String> filenames;
    TextView device_show;
    String result = " ";
    ListView rightfrag_device_lv;
    ArrayAdapter<String> rightfragdeviceadapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.rightfrag_device_layout,container,false);
        initView(v);
        return v;
    }
    private void initView(View v) {
        rightfrag_device_lv = (ListView) v.findViewById(R.id.rightfrag_device_lv);
        device_show = (TextView) v.findViewById(R.id.device_show);
        filenames = new ArrayList<>();
        DBUtils utils = new DBUtils(getContext(),"Device");
        filenames = utils.queryDevice();
        device_show.setVisibility(View.INVISIBLE);
        if (filenames.size()!=0) {
            rightfragdeviceadapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,filenames);
            rightfrag_device_lv.setAdapter(rightfragdeviceadapter);
        }
    }
}
