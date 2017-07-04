package com.mywork.henry.henry_smarthome;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;

/**
 * Created by Henry on 2017/1/10.
 */

public class NestedScrollExpandableListView extends ExpandableListView implements View.OnTouchListener,
        AbsListView.OnScrollListener {
    private int listViewTouchAction;
    private static final int MAXIMUM_LIST_ITEMS_VIEWABLE = 99;

    public NestedScrollExpandableListView(Context context) {
        super(context);
        listViewTouchAction = -1;
        setOnScrollListener(this);
        setOnTouchListener(this);
    }

    public NestedScrollExpandableListView(Context context, AttributeSet attrs) {
        super(context,attrs);
        listViewTouchAction = -1;
        setOnScrollListener(this);
        setOnTouchListener(this);
    }

    public NestedScrollExpandableListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context,attrs,defStyleAttr);
        listViewTouchAction = -1;
        setOnScrollListener(this);
        setOnTouchListener(this);
    }

    public NestedScrollExpandableListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        listViewTouchAction = -1;
        setOnScrollListener(this);
        setOnTouchListener(this);
    }


    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        if (getAdapter() != null
                && getAdapter().getCount() > MAXIMUM_LIST_ITEMS_VIEWABLE) {
            if (listViewTouchAction == MotionEvent.ACTION_MOVE) {
                scrollBy(0, -1);
            }
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (getAdapter() != null
                && getAdapter().getCount() > MAXIMUM_LIST_ITEMS_VIEWABLE) {
            if (listViewTouchAction == MotionEvent.ACTION_MOVE) {
                scrollBy(0, 1);
            }
        }
        return false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int newHeight = 0;
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (heightMode != MeasureSpec.EXACTLY) {
            ListAdapter listAdapter = getAdapter();
            if (listAdapter != null && !listAdapter.isEmpty()) {
                int listPosition = 0;
                for (listPosition = 0; listPosition < listAdapter.getCount()
                        && listPosition < MAXIMUM_LIST_ITEMS_VIEWABLE; listPosition++) {
                    View listItem = listAdapter.getView(listPosition, null,
                            this);
                    // now it will not throw a NPE if listItem is a ViewGroup
                    // instance
                    if (listItem instanceof ViewGroup) {
                        listItem.getLayoutParams().height= RelativeLayout.LayoutParams.WRAP_CONTENT;
                        listItem.getLayoutParams().width= RelativeLayout.LayoutParams.WRAP_CONTENT;
                        listItem.requestLayout();
                        //listItem.setLayoutParams(new LayoutParams(
                        //        LayoutParams.WRAP_CONTENT,
                        //        LayoutParams.WRAP_CONTENT));
                    }
                    listItem.measure(widthMeasureSpec, heightMeasureSpec);
                    newHeight += listItem.getMeasuredHeight();
                }
                newHeight += getDividerHeight() * listPosition;
            }
            if ((heightMode == MeasureSpec.AT_MOST) && (newHeight > heightSize)) {
                if (newHeight > heightSize) {
                    newHeight = heightSize;
                }
            }
        } else {
            newHeight = getMeasuredHeight();
        }
        setMeasuredDimension(getMeasuredWidth(), newHeight);
    }
}
