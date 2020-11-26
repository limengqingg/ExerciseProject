package com.tti.myapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.tti.myapp.R;
import com.tti.myapp.adapter.MyPagerAdapter;
import com.tti.myapp.entity.TabEntity;
import com.tti.myapp.fragment.CollectFragment;
import com.tti.myapp.fragment.HomeFragment;
import com.tti.myapp.fragment.MyFragment;
import com.tti.myapp.fragment.NewsFragment;

import java.util.ArrayList;

public class HomeActivity extends BaseActivity {

    private String[] mTitles = {"首页", "资讯", "我的"};
    //未选中时候的图片
    private int[] mIconUnselectIds = {
            R.mipmap.home_unselect, R.mipmap.collect_unselect,
            R.mipmap.my_unselect
    };
    //选中时候的图片
    private int[] mIconSelectIds = {
            R.mipmap.home_selected, R.mipmap.collect_selected,
            R.mipmap.my_selected
    };
    //Fragment集合
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    //几个title就有几个对象
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private ViewPager viewPager;
    private CommonTabLayout commonTabLayout;

    @Override
    protected int initLayout() {
        //setContentView(R.layout.activity_home);
        return R.layout.activity_home;

    }

    @Override
    protected void initView() {
        viewPager = findViewById(R.id.viewpager);
        commonTabLayout = findViewById(R.id.commonTabLayout);
    }

    @Override
    protected void initData() {
        mFragments.add(HomeFragment.newInstance());
        mFragments.add(NewsFragment.newInstance());
        mFragments.add(MyFragment.newInstance());
        for (int i = 0; i < mTitles.length; i++) {
            //把mTitles,未选中状态的图标，选中时的图标添加到mTabEntities
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }

        commonTabLayout.setTabData(mTabEntities);
        //绑定点击事件
        commonTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                //切换viewPager里的Fragment
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        //设置预加载，当启动页面的时候有多少个Fragments就加载多少个Fragments
        viewPager.setOffscreenPageLimit(mFragments.size());

        //当滑动页面的时候图标也要改变
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                commonTabLayout.setCurrentTab(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //绑定
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), mTitles, mFragments));
    }




    //@Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_home);
//        viewPager = findViewById(R.id.viewpager);
//        commonTabLayout = findViewById(R.id.commonTabLayout);
        //把三个Fragment添加到mFragments里

//        mFragments.add(HomeFragment.newInstance());
//        mFragments.add(CollectFragment.newInstance());
//        mFragments.add(MyFragment.newInstance());
//        for (int i = 0; i < mTitles.length; i++) {
//            //把mTitles,未选中状态的图标，选中时的图标添加到mTabEntities
//            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
//        }
//
//        commonTabLayout.setTabData(mTabEntities);
//        //绑定点击事件
//        commonTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
//            @Override
//            public void onTabSelect(int position) {
//                //切换viewPager里的Fragment
//                viewPager.setCurrentItem(position);
//            }
//
//            @Override
//            public void onTabReselect(int position) {
//
//            }
//        });
//        //当滑动页面的时候图标也要改变
//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                commonTabLayout.setCurrentTab(position);
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
//        //绑定
//        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(),mTitles,mFragments));
//
//   }
}