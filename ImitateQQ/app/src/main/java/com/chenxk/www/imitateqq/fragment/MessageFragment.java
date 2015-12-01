package com.chenxk.www.imitateqq.fragment;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.chenxk.www.imitateqq.MainActivity;
import com.chenxk.www.imitateqq.R;
import com.chenxk.www.imitateqq.domain.Cheeses;
import com.chenxk.www.imitateqq.drag.DragLayout;
import com.chenxk.www.imitateqq.drag.DragRelativeLayout;
import com.chenxk.www.imitateqq.swipe.SwipeListAdapter;
import com.chenxk.www.imitateqq.util.Utils;
import com.nineoldandroids.view.ViewHelper;

import java.util.Random;

/**
 * Created by chenxiangkong 2015/11/29 0029.
 */
public class MessageFragment extends Fragment implements View.OnClickListener {

    private View mView;
    private View mBtRight;
    private DragLayout mDragLayout;
    private ImageView mHeader;
    private SwipeListAdapter adapter;
    private MainActivity mContext;
    private ListView mListViewLeft;
    private ListView mListViewMain;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = (MainActivity) getActivity();
        mView = View.inflate(mContext, R.layout.activity_main, null);
        mListViewLeft = (ListView)mView.findViewById(R.id.lv_left);
        mListViewMain = (ListView)mView.findViewById(R.id.list);

        initLeftContent();
        initMainContent();
        return mView;
    }

    private void initMainContent() {

        mDragLayout = (DragLayout) mView.findViewById(R.id.dsl);
        mDragLayout.setDragListener(mDragListener);

        DragRelativeLayout mMainView = (DragRelativeLayout) mView.findViewById(R.id.rl_main);
        mMainView.setDragLayout(mDragLayout);
        mHeader = (ImageView) mView.findViewById(R.id.iv_head);
        mHeader.setOnClickListener(this);

        mBtRight = mView.findViewById(R.id.iv_head_right);
        mBtRight.setOnClickListener(this);
        mHeader.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                mDragLayout.switchScaleEnable();
                return true;
            }
        });


        mListViewLeft.setAdapter(new ArrayAdapter<String>(mContext,
                android.R.layout.simple_list_item_1, Cheeses.sCheeseStrings));
        adapter = new SwipeListAdapter(mContext);
        mListViewMain.setAdapter(adapter);
        mDragLayout.setAdapterInterface(adapter);

        mListViewMain.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    adapter.closeAllLayout();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
            }
        });

    }


    private void initLeftContent() {

        mListViewLeft.setAdapter(new ArrayAdapter<String>(mContext,
                android.R.layout.simple_list_item_1, Cheeses.sCheeseStrings) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView mText = (TextView) view.findViewById(android.R.id.text1);
                mText.setTextColor(Color.WHITE);
                return view;
            }
        });
    }

    private DragLayout.OnDragListener mDragListener = new DragLayout.OnDragListener() {

        @Override
        public void onOpen() {
            mListViewLeft.smoothScrollToPosition(new Random().nextInt(30));
        }

        @Override
        public void onClose() {
            shakeHeader();
            mBtRight.setSelected(false);
        }

        @Override
        public void onDrag(final float percent) {
            ViewHelper.setAlpha(mHeader, 1 - percent); // 主界面左上角头像渐渐消失
        }

        @Override
        public void onStartOpen(DragLayout.Direction direction) {
            Utils.showToast(mContext, "onStartOpen: " + direction.toString());
        }
    };

    private void shakeHeader() {
        mHeader.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.shake));
    }

    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_head:
                mDragLayout.open(true);
                break;
            case R.id.iv_head_right:
                mDragLayout.open(true, DragLayout.Direction.Right);
                mBtRight.setSelected(true);
                break;
        }
    }

    public DragLayout getDragLayout(){
        return mDragLayout;
    }
}
