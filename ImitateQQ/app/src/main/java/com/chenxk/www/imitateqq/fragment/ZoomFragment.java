package com.chenxk.www.imitateqq.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.chenxk.www.imitateqq.R;
import com.chenxk.www.imitateqq.domain.Cheeses;
import com.chenxk.www.imitateqq.parallaxlistview.ParallaxListView;

/**
 * Created by chenxiangkong 2015/11/29.
 */
public class ZoomFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_zoom, null);
        ParallaxListView listView = (ParallaxListView) view.findViewById(R.id.listview);

        listView.setOverScrollMode(AbsListView.OVER_SCROLL_NEVER);
        View headerView = View.inflate(getActivity(), R.layout.layout_header, null);
        ImageView parallaxImage = (ImageView) headerView.findViewById(R.id.parallaxImage);
        listView.addHeaderView(headerView);

        listView.setParallaxImage(parallaxImage);//设置图片
        listView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, Cheeses.sCheeseStrings));

        return view;
    }
}
