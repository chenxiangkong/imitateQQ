package com.chenxk.www.imitateqq;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RadioGroup;

import com.chenxk.www.imitateqq.drag.DragLayout;
import com.chenxk.www.imitateqq.fragment.FriendsFragment;
import com.chenxk.www.imitateqq.fragment.MessageFragment;
import com.chenxk.www.imitateqq.fragment.ZoomFragment;

/**
 * Created by chenxiangkong 2015/11/29 0029.
 */
public class MainActivity extends Activity implements RadioGroup.OnCheckedChangeListener {

    private DragLayout mDragLayout;
    private RadioGroup rg_btn_menu;
    private MessageFragment messageFragment;
    private FriendsFragment friendsFragment;
    private ZoomFragment zoomFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout);
        rg_btn_menu = (RadioGroup) findViewById(R.id.rg_btn_menu);
        rg_btn_menu.setOnCheckedChangeListener(this);

        initFragment();
        getFragmentManager().beginTransaction().replace(R.id.fl_content, messageFragment).commit();
    }

    private void initFragment() {
        messageFragment = new MessageFragment();
        friendsFragment = new FriendsFragment();
        zoomFragment = new ZoomFragment();
    }

    @Override
    public void onBackPressed() {
        if (messageFragment != null) {
            mDragLayout = messageFragment.getDragLayout();
        }
        if (mDragLayout != null && mDragLayout.getStatus() != DragLayout.Status.Close) {
            mDragLayout.close();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_msg:
                getFragmentManager().beginTransaction().replace(R.id.fl_content, messageFragment).commit();
                break;
            case R.id.rb_friends:
                getFragmentManager().beginTransaction().replace(R.id.fl_content, friendsFragment).commit();
                break;
            case R.id.rb_zoom:
                getFragmentManager().beginTransaction().replace(R.id.fl_content, zoomFragment).commit();
                break;
        }
    }
}
