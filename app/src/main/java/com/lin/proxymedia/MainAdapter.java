package com.lin.proxymedia;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by linchen on 2018/3/16.
 * mail: linchen@sogou-inc.com
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.VV> {
    private List<String> mList;

    public MainAdapter(List<String> list) {
        mList = list;
    }

    @Override
    public VV onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lai, parent, false);
        return new VV(view);
    }

    @Override
    public void onBindViewHolder(VV holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
    static class VV extends RecyclerView.ViewHolder {
        public VV(View itemView) {
            super(itemView);
        }
    }
}
