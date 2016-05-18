package com.guohui.fasttransfer.aty;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RadioButton;

import com.guohui.fasttransfer.R;
import com.guohui.fasttransfer.frag.FindFrag;
import com.guohui.fasttransfer.frag.HomeFrag;
import com.guohui.fasttransfer.frag.UserFrag;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends FragmentActivity implements ViewPager.OnPageChangeListener {
    public static MainActivity mainActivity;
   // private MleftMenu mLeftMenu;
    private ImageButton toobal_rightbtn;
    private MainBtnClickListner mainBtnClickListner;
    private CircleImageView leftbtn;
//    private Toolbar mtoolbar;

    // 主界面适配器
    FragmentPagerAdapter fPagerAdapter;
    // 所有的Tab
    private List<View> views;
    // 碎片每个碎片为一个布局
    private ArrayList<Fragment> fragments;

    // Viewpager
    private ViewPager vp;

    //三个radiobtn按钮
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //设置沉浸式标题栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        initView();
    }

    private void initView() {
        // 创建碎片集合
        fragments = new ArrayList<Fragment>();
        mainActivity = this;
//        mtoolbar = (Toolbar) findViewById(R.id.main_toobar);
        mainBtnClickListner = new MainBtnClickListner();
        leftbtn = (CircleImageView) findViewById(R.id.leftbtn);
        //设置SlidingMenu
        //mLeftMenu = new MleftMenu(this);

        toobal_rightbtn = (ImageButton) findViewById(R.id.man_toobal_rightbtn);
        toobal_rightbtn.setOnClickListener(mainBtnClickListner);
        leftbtn.setOnClickListener(mainBtnClickListner);




        LayoutInflater inflater = LayoutInflater.from(this);
        // 添加滑动
        views = new ArrayList<View>();
        views.add(inflater.inflate(R.layout.main_home_frag, null));
        views.add(inflater.inflate(R.layout.main_find_frag, null));
        views.add(inflater.inflate(R.layout.main_user_frag, null));
        vp = (ViewPager) findViewById(R.id.main_viewPager);
        fPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                return fragments.get(arg0);
            }
        };
        // 声明各个Tab的实例
        HomeFrag homeFrag = new HomeFrag();
        FindFrag schoolFrag = new FindFrag();
        UserFrag userFrag = new UserFrag();
        fragments.add(homeFrag);
        fragments.add(schoolFrag);
        fragments.add(userFrag);
        vp.setAdapter(fPagerAdapter);

        vp.setOnPageChangeListener(this);
        //注意，设置Page 即缓存页面的个数，数过小时会出现fragment重复加载的问题
        vp.setOffscreenPageLimit(3);
    }

    class MainBtnClickListner implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.man_toobal_rightbtn:
                    Intent i = new Intent(MainActivity.this,RightActivity.class);
                    overridePendingTransition(R.anim.initactivity_open, 0);
                    startActivity(i);
                break;
                case R.id.leftbtn:
                 //   mLeftMenu.toggle();
                    break;
            }
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }


    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        switch (state) {
            case 0:

                break;
            case 1:

                break;
            case 2:

                break;
        }
    }

    // 更换标签
    private void changeTagView(int change) {
        vp.setCurrentItem(change, false);
    }
}
