package com.chenxk.www.imitateqq.swipe;

import android.content.Context;
import android.graphics.PointF;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chenxk.www.imitateqq.R;
import com.chenxk.www.imitateqq.domain.Cheeses;
import com.chenxk.www.imitateqq.reminder.GooViewListener;
import com.chenxk.www.imitateqq.util.Utils;

import java.util.HashSet;

/**
 * Created by chenxiangkong 2015/11/29.
 */
public class SwipeListAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    HashSet<Integer> mRemoved = new HashSet<Integer>();
    HashSet<SwipeLayout> mUnClosedLayouts = new HashSet<SwipeLayout>();

    public SwipeListAdapter(Context mContext) {
        super();
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return 120;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static final int[] HEAD_IDS = new int[]{
            R.drawable.head_1,
            R.drawable.head_2,
            R.drawable.head_3,
            R.drawable.head_4,
            R.drawable.head_5,
            R.drawable.head_6,
            R.drawable.head_7,
            R.drawable.head_8,
            R.drawable.head_9,
            R.drawable.head,
            R.drawable.ic_launcher,
    };

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = (SwipeLayout) mInflater.inflate(R.layout.list_item_swipe, null);
        }
        ViewHolder holder = ViewHolder.fromValues(convertView);

        SwipeLayout view = (SwipeLayout) convertView;

        view.close(false, false);

        view.getFrontView().setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Utils.showToast(mContext, "item click: " + position);
            }
        });

        view.setSwipeListener(mSwipeListener);

        holder.mImage.setImageResource(HEAD_IDS[position % HEAD_IDS.length]);
        holder.mName.setText(Cheeses.NAMES[position % Cheeses.NAMES.length]);
        holder.mMessage.setText(Cheeses.sCheeseStrings[position % Cheeses.sCheeseStrings.length]);

        holder.mButtonCall.setTag(position);
        holder.mButtonCall.setOnClickListener(onActionClick);

        holder.mButtonDel.setTag(position);
        holder.mButtonDel.setOnClickListener(onActionClick);


        TextView mUnreadView = holder.mReminder;
        boolean visiable = !mRemoved.contains(position);
        mUnreadView.setVisibility(visiable ? View.VISIBLE : View.GONE);

        if (visiable) {
            mUnreadView.setText(String.valueOf(position));
            mUnreadView.setTag(position);
            GooViewListener mGooListener = new GooViewListener(mContext, mUnreadView) {
                @Override
                public void onDisappear(PointF mDragCenter) {
                    super.onDisappear(mDragCenter);

                    mRemoved.add(position);
                    notifyDataSetChanged();
                    Utils.showToast(mContext,
                            "Cheers! We have get rid of it!");
                }

                @Override
                public void onReset(boolean isOutOfRange) {
                    super.onReset(isOutOfRange);

                    notifyDataSetChanged();
                    Utils.showToast(mContext,
                            isOutOfRange ? "Are you regret?" : "Try again!");
                }
            };
            mUnreadView.setOnTouchListener(mGooListener);
        }

        return view;
    }

    OnClickListener onActionClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            Integer p = (Integer) v.getTag();
            int id = v.getId();
            if (id == R.id.bt_call) {
                closeAllLayout();
                Utils.showToast(mContext, "position: " + p + " call");
            } else if (id == R.id.bt_delete) {
                closeAllLayout();
                Utils.showToast(mContext, "position: " + p + " del");
            }
        }
    };
    SwipeLayout.SwipeListener mSwipeListener = new SwipeLayout.SwipeListener() {
        @Override
        public void onOpen(SwipeLayout swipeLayout) {
            mUnClosedLayouts.add(swipeLayout);
        }

        @Override
        public void onClose(SwipeLayout swipeLayout) {
            mUnClosedLayouts.remove(swipeLayout);
        }

        @Override
        public void onStartClose(SwipeLayout swipeLayout) {
        }

        @Override
        public void onStartOpen(SwipeLayout swipeLayout) {
            closeAllLayout();
            mUnClosedLayouts.add(swipeLayout);
        }

    };

    public int getUnClosedCount() {
        return mUnClosedLayouts.size();
    }

    public void closeAllLayout() {
        if (mUnClosedLayouts.size() == 0)
            return;

        for (SwipeLayout l : mUnClosedLayouts) {
            l.close(true, false);
        }
        mUnClosedLayouts.clear();
    }

    static class ViewHolder {

        public ImageView mImage;
        public Button mButtonCall;
        public Button mButtonDel;
        public TextView mReminder;
        public TextView mName;
        public TextView mMessage;

        private ViewHolder(View convertView) {
            this.mImage = (ImageView) convertView.findViewById(R.id.iv_head);
            this.mButtonCall = (Button) convertView.findViewById(R.id.bt_call);
            this.mButtonDel = (Button) convertView.findViewById(R.id.bt_delete);
            this.mReminder = (TextView) convertView.findViewById(R.id.point);
            this.mName = (TextView) convertView.findViewById(R.id.tv_name);
            this.mMessage = (TextView) convertView.findViewById(R.id.tv_message);
        }

        public static ViewHolder fromValues(View convertView) {
            ViewHolder holder = (ViewHolder) convertView.getTag();
            if(holder==null){
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }
            return holder;
        }
    }

}