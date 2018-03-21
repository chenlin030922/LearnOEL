package com.lin.proxymedia;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

/**
 * Created by linchen on 2018/3/16.
 * mail: linchen@sogou-inc.com
 */

public class StickyItemDecoration<T> extends RecyclerView.ItemDecoration {
    private List<T> mList;
    private final static int height = 200;
    private Paint mPaint;
    private int count = 1;

    public StickyItemDecoration(List<T> list) {
        mList = list;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.parseColor("#40EE4035"));
        mPaint.setTextSize(100);

    }

    /**
     * 绘制*除Item内容*以外的东西,这个方法是在****Item的内容绘制之后****才执行的,
     * 所以该方法绘制的东西会将Item的内容覆盖住,既显示在Item之上.
     * 一般配合getItemOffsets来绘制分组的头部等.
     *
     * @param c      Canvas 画布
     * @param parent RecyclerView
     * @param state  RecyclerView的状态
     */

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mList == null || mList.size() == 0) {
            return;
        }
        for (int i = 0; i < parent.getChildCount(); i++) {
            View childView = parent.getChildAt(i);
            if (childView == null) {
                continue;
            }
            int adapterPosition = parent.getChildAdapterPosition(childView);
            if (adapterPosition < 0) {
                continue;
            }
            if (adapterPosition % 4 == 0) {
                int firstVisitPos = ((LinearLayoutManager) parent.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
                if (firstVisitPos == adapterPosition) {
                    mPaint.setColor(Color.DKGRAY);
                    c.drawRect(0, 0, 0, height, mPaint);
                    mPaint.setColor(Color.RED);
                    c.drawText(String.valueOf(adapterPosition + 1 / 4), 0, 0, mPaint);
                } else {
                    mPaint.setColor(Color.DKGRAY);
                    c.drawRect((float) (parent.getLeft()), childView.getTop() - height, (float) (parent.getRight()), childView.getTop(), mPaint);
                    mPaint.setColor(Color.RED);
                    c.drawText(String.valueOf(adapterPosition + 1 / 4), 0, (2 * childView.getTop() - height) / 2, mPaint);
                }


            }
        }
    }

    /**
     * 设置Item的布局四周的间隔.
     *
     * @param outRect 确定间隔 Left  Top Right Bottom 数值的矩形.
     * @param view    RecyclerView的ChildView也就是每个Item的的布局.
     * @param parent  RecyclerView本身.
     * @param state   RecyclerView的各种状态.
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (mList == null || mList.size() == 0) {
            return;
        }
        int adapterPosition = parent.getChildAdapterPosition(view);
        T bean = mList.get(adapterPosition);
        if (bean == null) {
            return;
        }
        if (adapterPosition > -1) {
            if (adapterPosition % 4 == 0) {
                outRect.top = height;
            } else {
                outRect.set(0, 0, 0, 0);
            }
        }
    }
}
