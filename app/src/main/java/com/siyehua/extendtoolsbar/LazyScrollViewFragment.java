package com.siyehua.extendtoolsbar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Lacy ScrollView
 */
public class LazyScrollViewFragment extends BaseFragment {
    public static LazyScrollViewFragment newInstance() {
        return new LazyScrollViewFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
    Bundle savedInstanceState) {
        return rootView = new NestedScrollView(getActivity());
    }


    private TextView mTextView;
    private View mView;

    @Override
    public void findView() {
        //add layout
        ((ViewGroup) rootView).addView(LayoutInflater.from(getActivity()).inflate(R.layout
                .fragment_lazy_scrollview, (ViewGroup) rootView, false));

        //find view
        mTextView = (TextView) findViewById(R.id.tv_content);
        mView = findViewById(R.id._001);
    }

    @Override
    public void init() {
        // if you want refresh data, when the fragment view was destroy.
        // getDataFlag = false;
        // mTextView.setText("数据还没加载...");

        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextView.setVisibility(mTextView.getVisibility() == View.GONE ? View.VISIBLE :
                        View.GONE);
//                startActivity(new Intent(getActivity(), ScrollViewActivity.class));
            }
        });
    }

    @Override
    public void getData() {
        if (getDataFlag) return;
        getDataFlag = true;
        Toast.makeText(getActivity(), "数据加载完毕", Toast.LENGTH_SHORT).show();
        mTextView.setText("数据加载完毕");
    }
}
