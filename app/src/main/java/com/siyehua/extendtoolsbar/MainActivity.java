package com.siyehua.extendtoolsbar;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private Button button1, button2;
    private int currentOffY = 0;
    private int maxOffY = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewPager = (ViewPager) findViewById(R.id.vp_main);
        button1 = (Button) findViewById(R.id.bt_1);
        button2 = (Button) findViewById(R.id.bt_2);
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(RecycleFragment.newInstance("1", "1-1"));
        fragments.add(RecycleFragment.newInstance("1", "1-1"));
        fragments.add(RecycleFragment.newInstance("1", "1-1"));
        fragments.add(RecycleFragment.newInstance("1", "1-1"));
        MainViewPagerAdapter mainViewPagerAdapter = new MainViewPagerAdapter
                (getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(mainViewPagerAdapter);


        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id
                .collapsing_toolbar);
        assert collapsingToolbar != null;
        collapsingToolbar.setTitle("");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        final AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        assert appBarLayout != null;
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                currentOffY = Math.abs(verticalOffset);
                maxOffY = currentOffY > maxOffY ? currentOffY : maxOffY;
                Log.e("siyehua", currentOffY + "  ");
            }
        });

        final MyCoordinatorLayout myCoordinatorLayout = (MyCoordinatorLayout) findViewById(R.id
                .root);
        assert myCoordinatorLayout != null;
        myCoordinatorLayout.setActionListener(new MyCoordinatorLayout.ActionListener() {
            @Override
            public void action(MotionEvent ev) {
                Log.e("siyehua", currentOffY + "  " + maxOffY / 2);

                if (currentOffY > maxOffY / 2) {
                    appBarLayout.setExpanded(false);
                } else {
                    appBarLayout.setExpanded(true);
                }
            }
        });


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(MainActivity.this, "被点击了", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("siyehua", event.getAction() + " 123456");
        return super.onTouchEvent(event);
    }
}