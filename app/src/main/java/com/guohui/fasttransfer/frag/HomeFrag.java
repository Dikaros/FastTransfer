package com.guohui.fasttransfer.frag;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.guohui.fasttransfer.R;
import com.guohui.fasttransfer.aty.AcceptActivity;
import com.guohui.fasttransfer.aty.MainActivity;
import com.guohui.fasttransfer.aty.SendActivity;


/**
 * Created by Administrator on 2016/4/5 0005.
 */
public class HomeFrag extends Fragment {
    private AnimationDrawable animationDrawable;
    private MyHomeFragButtonClickListner myHomeFragButtonClickListner;
    private ImageButton downarrow;
    //发送和接受按钮
    Button sendbtn;
    Button acceptbtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.main_home_frag,container,false);
        initView(v);

        return v;
    }

    private void initView(View v) {
        //设置下滑箭头的帧动画
        downarrow = (ImageButton) v.findViewById(R.id.downarrow);
        downarrow.setImageResource(R.drawable.down_arrow_animation);
        animationDrawable = (AnimationDrawable) downarrow.getDrawable();
        animationDrawable.start();
        //设置发送接受按钮的跳转方法
        myHomeFragButtonClickListner = new MyHomeFragButtonClickListner();
        sendbtn = (Button) v.findViewById(R.id.sendbtn);
        acceptbtn = (Button) v.findViewById(R.id.acceptbtn);
        sendbtn.setOnClickListener(myHomeFragButtonClickListner);
        acceptbtn.setOnClickListener(myHomeFragButtonClickListner);
        downarrow.setOnClickListener(myHomeFragButtonClickListner);
    }

    private class MyHomeFragButtonClickListner implements View.OnClickListener {
        Intent i ;
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.sendbtn:
                   i = new Intent(getContext(),SendActivity.class);
                    MainActivity.mainActivity.overridePendingTransition(R.anim.initactivity_open, 0);
                    startActivity(i);
                    break;
                case R.id.acceptbtn:
                    i = new Intent(getContext(),AcceptActivity.class);
                    MainActivity.mainActivity.overridePendingTransition(R.anim.initactivity_open, 0);

                    startActivity(i);
                    break;
                case R.id.downarrow:
                    i = new Intent(getContext(),SendActivity.class);
                    MainActivity.mainActivity.overridePendingTransition(R.anim.initactivity_open, 0);
                    startActivity(i);
                    break;
            }
        }
    }


}
