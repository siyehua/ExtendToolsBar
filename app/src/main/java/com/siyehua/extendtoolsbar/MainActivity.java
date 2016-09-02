package com.siyehua.extendtoolsbar;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;

import java.util.ArrayList;

/**
 * 数据懒加载
 */
public class MainActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    /**
     * head current off y
     */
    private int currentOffY = 0;
    /**
     * head max y
     */
    private int maxOffY = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //设置标题栏为空字符串,用自定义标题栏替换
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id
                .collapsing_toolbar);
        if (collapsingToolbar != null) {
            collapsingToolbar.setTitle("");
        }
        //替换ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("");
        }
        setSupportActionBar(toolbar);

        //监听顶部的滑动距离
        final AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        if (appBarLayout != null) {
            appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                    currentOffY = Math.abs(verticalOffset);
                    maxOffY = currentOffY > maxOffY ? currentOffY : maxOffY;
                }
            });
        }

        //根据滑动距离决定 收缩/扩展 头部
        final MyCoordinatorLayout myCoordinatorLayout = (MyCoordinatorLayout) findViewById(R.id
                .root);
        if (myCoordinatorLayout != null) {
            myCoordinatorLayout.setActionListener(new MyCoordinatorLayout.ActionListener() {
                @Override
                public void action(MotionEvent ev) {
                    if (appBarLayout != null) {
                        appBarLayout.setExpanded(!(currentOffY > maxOffY / 2));
                    }
                }
            });
        }


        //set viewPager data
        mViewPager = (ViewPager) findViewById(R.id.vp_main);
        ArrayList<Fragment> fragments = new ArrayList<>();
//        fragments.add(LazyScrollViewFragment.newInstance());
        fragments.add(RecycleFragment.newInstance("1", "1-1"));
        fragments.add(LazyScrollViewFragment.newInstance());
        fragments.add(RecycleFragment.newInstance("1", "1-1"));
        fragments.add(RecycleFragment.newInstance("1", "1-1"));
        fragments.add(RecycleFragment.newInstance("1", "1-1"));
        fragments.add(RecycleFragment.newInstance("1", "1-1"));
//        fragments.add(ScrollViewFragment.newInstance());
//        fragments.add(ScrollViewFragment.newInstance());
//        fragments.add(ScrollViewFragment.newInstance());
//        fragments.add(ScrollViewFragment.newInstance());
        MainViewPagerAdapter mainViewPagerAdapter = new MainViewPagerAdapter
                (getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(mainViewPagerAdapter);
        mViewPager.setOffscreenPageLimit(1);
    }

}