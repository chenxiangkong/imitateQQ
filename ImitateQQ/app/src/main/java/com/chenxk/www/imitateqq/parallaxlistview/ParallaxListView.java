package com.chenxk.www.imitateqq.parallaxlistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.ListView;

import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;

/**
 * Created by chenxiangkong 2015/11/29 0029.
 */
public class ParallaxListView extends ListView {

    public ParallaxListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ParallaxListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ParallaxListView(Context context) {
        super(context);
    }

    private ImageView parallaxImageView;
    private int maxHeight;//最大高度
    int originalHeight;//ImageVIew最初的高度

    public void setParallaxImage(ImageView imageView) {
        this.parallaxImageView = imageView;

        final int drawableHeight = parallaxImageView.getDrawable().getIntrinsicHeight();

        imageView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                parallaxImageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                originalHeight = parallaxImageView.getHeight();

                if (originalHeight < drawableHeight) {
                    maxHeight = drawableHeight;//如果ImageView没有图片高，那么最大高度就是图片的高度
                } else {
                    maxHeight = drawableHeight * 2;//如果ImageView比图片高，那么最大高度就是图片的2倍
                }
            }
        });
    }

    /**
     * 当listview滚动到头的时候会调用,可以获取到继续滚动的距离
     * deltaY: y方向继续滚动的距离 ， 正值表示底部到头，  负值表示顶部到头
     * maxOverScrollY: y方向最大可以滚动的距离
     * isTouchEvent: true:表示手指拖动到头的      false:表示靠惯性滑动到头的
     */
    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX,
                                   int scrollY, int scrollRangeX, int scrollRangeY,
                                   int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        //如果是顶部到头并且是手指拖动到头，那么可以让ImageView的高度变高
        if (deltaY < 0 && isTouchEvent) {
            int newHeight = parallaxImageView.getHeight() - deltaY / 3;
            if (newHeight > maxHeight) {
                newHeight = maxHeight;
            }
            parallaxImageView.getLayoutParams().height = newHeight;
            parallaxImageView.requestLayout();//让ImageView的布局参数生效
        }

        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX,
                scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                //让ImageView恢复到最初的高度
                ValueAnimator animator = ValueAnimator.ofInt(parallaxImageView.getHeight(), originalHeight);
                animator.addUpdateListener(new AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        //获取动画的值，更改ImageView的高度
                        int animatedValue = (Integer) animator.getAnimatedValue();
                        parallaxImageView.getLayoutParams().height = animatedValue;
                        parallaxImageView.requestLayout();
                    }
                });
                animator.setInterpolator(new OvershootInterpolator(5));
                animator.setDuration(350);
                animator.start();
                break;
        }
        return super.onTouchEvent(ev);
    }

}
