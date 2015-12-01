package com.chenxk.www.imitateqq.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chenxk.www.imitateqq.R;
import com.chenxk.www.imitateqq.domain.Friend;

import java.util.ArrayList;

/**
 * Created by chenxiangkong 2015/11/29.
 */
public class FriendAdapter extends BaseAdapter{
	private Context context;
	private ArrayList<Friend> friends;
	public FriendAdapter(Context context, ArrayList<Friend> friends) {
		super();
		this.context = context;
		this.friends = friends;
	}
	@Override
	public int getCount() {
		return friends.size();
	}
	@Override
	public Object getItem(int position) {
		return null;
	}
	@Override
	public long getItemId(int position) {
		return 0;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView = View.inflate(context, R.layout.adapter_friend,null);
		}
		ViewHolder holder = ViewHolder.getHolder(convertView);
		
		//设置数据
		Friend friend = friends.get(position);
		
		String firstWord = friend.getPinyin().charAt(0)+"";
		if(position>0){
			//获取上一个item的首字母
			String lastWord = friends.get(position-1).getPinyin().charAt(0)+"";
			if(firstWord.equals(lastWord)){
				//当前首字母和上一个首字母相同，应该隐藏当前的首字母
				holder.first_word.setVisibility(View.GONE);
			}else {
				//不相同，应该显示
				holder.first_word.setVisibility(View.VISIBLE);
				holder.first_word.setText(firstWord);
			}
		}else {
			holder.first_word.setVisibility(View.VISIBLE);
			holder.first_word.setText(firstWord);
		}
		holder.tv_name.setText(friend.getName());
		
		return convertView;
	}
	
	static class ViewHolder{
		TextView first_word;
		TextView tv_name;
		public ViewHolder(View convertView){
			first_word = (TextView) convertView.findViewById(R.id.first_word);
			tv_name = (TextView) convertView.findViewById(R.id.tv_name);
		}
		public static ViewHolder getHolder(View convertView){
			ViewHolder holder = (ViewHolder) convertView.getTag();
			if(holder==null){
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			}
			return holder;
		}
	}

}
