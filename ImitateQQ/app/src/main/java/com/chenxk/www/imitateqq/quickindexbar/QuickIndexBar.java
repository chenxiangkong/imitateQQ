package com.chenxk.www.imitateqq.quickindexbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by chenxiangkong 2015/11/29.
 */
public class QuickIndexBar extends View{
	private String tag = QuickIndexBar.class.getSimpleName();
	private String[] indexArr = { "A", "B", "C", "D", "E", "F", "G", "H",
			"I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
			"V", "W", "X", "Y", "Z" };
	private Paint paint;
	private int width;//当前view的宽度
	private float cellHeight;//每个格子的高度
	public QuickIndexBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	public QuickIndexBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	public QuickIndexBar(Context context) {
		super(context);
		init();
	}
	
	/**
	 * 初始化方法
	 */
	private void init(){
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);//设置抗锯齿
		paint.setColor(Color.WHITE);
		paint.setTextSize(16);
		paint.setTextAlign(Align.CENTER);//设置文本绘制的起点
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		width = getMeasuredWidth();
		cellHeight = getMeasuredHeight()*1f/indexArr.length;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		//将26个字母绘制到view上
		for (int i = 0; i < indexArr.length; i++) {
			float x = width/2;
			float y = cellHeight/2 + getTextHeight(indexArr[i])/2 + i*cellHeight;
			
			//当前触摸的字母和正在绘制的如果是同一个字母，那么就改变颜色
			paint.setColor(i==lastIndex?Color.DKGRAY:Color.WHITE);
			
			canvas.drawText(indexArr[i],x, y, paint);
		}
	}
	
	private int lastIndex = -1;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_MOVE:
			int y = (int) event.getY();
			int index = (int) (y/cellHeight);
			if(index!=lastIndex){
				//对index的值进行安全性的检查
				if(index>=0 && index<indexArr.length){
					if(listener!=null){
						listener.onLetterChange(indexArr[index]);
					}
				}
				lastIndex = index;
			}
			break;
		case MotionEvent.ACTION_UP:
			lastIndex = -1;//重置
			break;
		}
		//引起重绘
		invalidate();
		return true;
	}
	
	/**
	 * 获取文本的高度
	 * @param text
	 * @return
	 */
	private int getTextHeight(String text){
		Rect bounds = new Rect();
		paint.getTextBounds(text,0,text.length(), bounds);//只要执行完，那么bounds就有值
		return bounds.height();
	}
	
	private OnLetterChangeListener listener;
	public void setOnLetterChangeListener(OnLetterChangeListener listener){
		this.listener = listener;
	}
	/**
	 * 当前字母改变的监听器
	 * @author chenxiangkong
	 *
	 */
	public interface OnLetterChangeListener{
		void onLetterChange(String letter);
	}
}
