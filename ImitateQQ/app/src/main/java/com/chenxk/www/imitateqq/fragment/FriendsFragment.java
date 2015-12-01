package com.chenxk.www.imitateqq.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ListView;
import android.widget.TextView;

import com.chenxk.www.imitateqq.R;
import com.chenxk.www.imitateqq.adapter.FriendAdapter;
import com.chenxk.www.imitateqq.domain.Cheeses;
import com.chenxk.www.imitateqq.domain.Friend;
import com.chenxk.www.imitateqq.quickindexbar.QuickIndexBar;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by chenxiangkong 2015/11/29.
 */
public class FriendsFragment extends Fragment {

    private QuickIndexBar quickIndexBar;
    private TextView currentWord;
    private ListView listview;
    private ArrayList<Friend> friends = new ArrayList<Friend>();

    private boolean isAnimaing = false;
    private Handler handler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_friends, null);

        quickIndexBar = (QuickIndexBar) view.findViewById(R.id.quickIndexBar);
        currentWord = (TextView) view.findViewById(R.id.currentWord);
        listview = (ListView) view.findViewById(R.id.listview);

        //通过缩小currentWord来隐藏
        ViewHelper.setScaleX(currentWord, 0);
        ViewHelper.setScaleY(currentWord, 0);

        quickIndexBar.setOnLetterChangeListener(new QuickIndexBar.OnLetterChangeListener() {
            public void onLetterChange(String letter) {
                //去friends中寻找那个的首字母和letter相同，找到后将其放置到屏幕顶端
                for (int i = 0; i < friends.size(); i++) {
                    String word = friends.get(i).getPinyin().charAt(0) + "";
                    if (word.equals(letter)) {
                        listview.setSelection(i);
                        break;//找到之后立即中断
                    }
                }
                showCurrentWord(letter);//显示当前所触摸的字母
            }
        });

        //给listview填充数据
        fillList();//填充集合
        Collections.sort(friends); //对数据进行排序

        listview.setAdapter(new FriendAdapter(getActivity(), friends));
        return view;
    }

    /**
     * 显示当前的字母
     *
     * @param letter
     */
    private void showCurrentWord(String letter) {
        currentWord.setText(letter);
        if (!isAnimaing) {
            isAnimaing = true;
            ViewPropertyAnimator.animate(currentWord).scaleX(1f)
                    .setInterpolator(new OvershootInterpolator())
                    .setDuration(400).start();
            ViewPropertyAnimator.animate(currentWord).scaleY(1f)
                    .setInterpolator(new OvershootInterpolator())
                    .setDuration(400).start();
        }

        handler.removeCallbacksAndMessages(null); //先移除之前的
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ViewPropertyAnimator.animate(currentWord).scaleX(0f)
                        .setDuration(400).start();
                ViewPropertyAnimator.animate(currentWord).scaleY(0f)
                        .setDuration(400).start();
                isAnimaing = false;
            }
        }, 1000);
    }

    private void fillList() {
        // 虚拟数据
        for (int i = 0; i < Cheeses.NAMES.length; i++) {
            friends.add(new Friend(Cheeses.NAMES[i]));
        }
    }
}
