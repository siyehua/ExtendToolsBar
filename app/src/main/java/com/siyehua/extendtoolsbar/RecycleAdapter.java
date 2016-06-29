package com.siyehua.extendtoolsbar;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder> {
    public static final int VIEWTYPE = 0x10086;

    private String[] mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;

        public ViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.name);
        }
    }

    public RecycleAdapter(String[] myDataset) {
        mDataset = myDataset;
    }

    @Override
    public RecycleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId;
        if (viewType == VIEWTYPE) {
            layoutId = R.layout.fragment_scrollview;
        } else {
            layoutId = R.layout.list_item;
        }
        View v = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecycleAdapter.ViewHolder holder, int position) {
        if (position != 0) {
            String nameStr = mDataset[position];
            holder.name.setText(nameStr);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return VIEWTYPE;
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
