package com.siyehua.extendtoolsbar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

public abstract class BaseFragment extends Fragment {
    /**
     * root view
     */
    protected View rootView;

    /**
     * init view flag
     */
    protected boolean initViewFlag = false;
    /**
     * get data flag
     */
    protected boolean getDataFlag = false;
    protected boolean onActivityCreatedFlag = false;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onActivityCreatedFlag = true;
        if (getUserVisibleHint()) {
            findView();
            init();
            initViewFlag = true;
            getData();
        }
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && onActivityCreatedFlag && !initViewFlag) {
            findView();
            init();
            initViewFlag = true;
            getData();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        initViewFlag = false;
        onActivityCreatedFlag = false;
    }

    protected View findViewById(int id) {
        return rootView.findViewById(id);
    }

    public abstract void findView();

    public abstract void init();

    public abstract void getData();
}
