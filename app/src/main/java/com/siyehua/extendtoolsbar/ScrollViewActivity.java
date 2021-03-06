package com.siyehua.extendtoolsbar;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.Field;

public class ScrollViewActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_scroll_view);

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
        findViewById(R.id.iv_personal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ScrollViewActivity.this, "viewbottom:" + v.getBottom() + "   " +
                        "bottom:" + appBarLayout.getBottom() + "  " +
                        "scrollTop:" + findViewById(R.id.vp_main).getTop() + " xuanfu:" +
                        (findViewById(R.id._abc).getTop() + findViewById(R.id._abc).getPaddingTop
                                ()), Toast.LENGTH_SHORT).show();
            }
        });
//        findViewById(R.id.iv_personal).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(ScrollViewActivity.this, "height:" + v.getBottom() + " img:" +
//                        (findViewById(R.id._abc).getTop() + findViewById(R.id._abc).getPaddingTop
//                                ()) + "statusbar:" + getStatusBarHeight(ScrollViewActivity.this)
//                        + " 22dp:" + TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 22,
//                        getResources().getDisplayMetrics()), Toast.LENGTH_SHORT).show();
//            }
//        });

    }

    public static int getStatusBarHeight(Context context) {
        int x = 0;
        int sbar = 0;
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = context.getResources().getDimensionPixelSize(x);
            return sbar;
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return 0;
    }

}
