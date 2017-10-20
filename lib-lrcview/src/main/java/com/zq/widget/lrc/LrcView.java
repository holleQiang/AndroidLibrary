package com.zq.widget.lrc;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.OverScroller;
import android.widget.Scroller;

import java.util.List;

/**
 * Created by zhangqiang on 2017/9/30.
 */

public class LrcView extends View {

    private Lrc lrc;
    private Paint playingPaint, normalPaint;
    private float verticalOffset;
    private long playingTime;
    private float mLastMotionY;
    private boolean isBeingDragged;
    private int mTouchSlop;
    private VelocityTracker velocityTracker;
    private LrcFlingRunnable lrcFlingRunnable;
    private int minFlingVelocity, maxFlingVelocity;
    private float totalHeight;
    private int lineSpacing = 20;
    private boolean isResetAfterDrag;
    private int resetDelayTime;
    private int playingTextSize, normalTextSize;
    private int playingTextColor, normalTextColor;
    private Lrc.Item lastPlayingItem;

    public LrcView(Context context) {
        super(context);
        init(null);
    }

    public LrcView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public LrcView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    /**
     * 初始化
     * @param attrs
     */
    private void init(AttributeSet attrs) {

        if (attrs != null) {
            float density = getResources().getDisplayMetrics().density;
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.LrcView);
            playingTextSize = ta.getDimensionPixelSize(R.styleable.LrcView_playingTextSize, (int) (density * 20 + 0.5f));
            normalTextSize = ta.getDimensionPixelSize(R.styleable.LrcView_normalTextSize, (int) (density * 20 + 0.5f));
            playingTextColor = ta.getColor(R.styleable.LrcView_playingTextColor, Color.RED);
            normalTextColor = ta.getColor(R.styleable.LrcView_normalTextColor, Color.DKGRAY);
            lineSpacing = ta.getDimensionPixelSize(R.styleable.LrcView_lineSpacing, (int) (10 * density + 0.5f));
            isResetAfterDrag = ta.getBoolean(R.styleable.LrcView_resetAfterDrag, true);
            resetDelayTime = ta.getInt(R.styleable.LrcView_resetDelayTime, 2000);
            ta.recycle();
        }

        playingPaint = new TextPaint();
        playingPaint.setColor(playingTextColor);
        playingPaint.setTextSize(playingTextSize);

        normalPaint = new TextPaint();
        normalPaint.setTextSize(normalTextSize);
        normalPaint.setColor(normalTextColor);

        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

        lrcFlingRunnable = new LrcFlingRunnable();
        minFlingVelocity = ViewConfiguration.get(getContext()).getScaledMinimumFlingVelocity();
        maxFlingVelocity = ViewConfiguration.get(getContext()).getScaledMaximumFlingVelocity();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (lrc == null) {
            return;
        }

        float startPosition = verticalOffset + getPaddingTop();

        startPosition = drawTitle(canvas, startPosition);

        startPosition = drawArtist(canvas, startPosition);

        startPosition = drawAlbum(canvas, startPosition);

        drawLrc(canvas, startPosition);
    }

    /**
     * 绘制歌词内容
     * @param canvas 画布
     * @param startPosition 绘制的纵向起始位置
     */
    private void drawLrc(Canvas canvas, float startPosition) {

        List<Lrc.Item> itemList = lrc.getItemList();
        if (itemList == null || itemList.isEmpty()) {
            return;
        }

        Lrc.Item playingItem = getPlayingItem();
        for (Lrc.Item item : itemList) {

            String itemText = item.getText();

            if (item.equals(playingItem)) {
                startPosition = drawText(canvas, playingPaint, itemText, startPosition);
            } else {
                startPosition = drawText(canvas, normalPaint, itemText, startPosition);
            }
        }
    }

    /**
     * 绘制专辑
     * @param canvas 画布
     * @param startPosition 绘制的纵向起始位置
     * @return 高度
     */
    private float drawAlbum(Canvas canvas, float startPosition) {

        String album = lrc.getAlbum();

        return drawText(canvas, normalPaint, album, startPosition);
    }

    /**
     * 绘制艺术家
     * @param canvas 画布
     * @param startPosition 绘制的纵向起始位置
     * @return 高度
     */
    private float drawArtist(Canvas canvas, float startPosition) {

        String artist = lrc.getArtist();

        return drawText(canvas, normalPaint, artist, startPosition);
    }

    /**
     * 绘制标题
     * @param canvas 画布
     * @param startPosition 绘制的纵向起始位置
     * @return 高度
     */
    private float drawTitle(Canvas canvas, float startPosition) {

        String title = lrc.getTitle();

        return drawText(canvas, normalPaint, title, startPosition);
    }

    /**
     * 绘制文本
     * @param canvas 画布
     * @param textPaint 画笔
     * @param text 文本
     * @param startPosition 绘制的纵向起始位置
     * @return 文本的高度
     */
    private float drawText(Canvas canvas, Paint textPaint, String text, float startPosition) {

        float singleLineTextHeight = textPaint.descent() - textPaint.ascent() + lineSpacing;

        if (text == null || text.isEmpty()) {
            return startPosition + singleLineTextHeight;
        }

        int maxTextWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        int textLength = text.length();
        int offset = 0;
        float lastTextWidth = 0;
        for (int i = 1; i <= textLength; i++) {

            float textWidth = textPaint.measureText(text, offset, i);
            if (lastTextWidth < maxTextWidth && textWidth > maxTextWidth) {

                //如果上次测量的宽度小于最大宽度，本次测量的宽度大于最大宽度，绘制上次测量的文字
                drawLineText(canvas, textPaint, text, offset, i - 1, startPosition, startPosition + singleLineTextHeight);
                offset = i - 1;
                startPosition += singleLineTextHeight;
            }
            lastTextWidth = textWidth;
        }

        drawLineText(canvas, textPaint, text, offset, textLength, startPosition, startPosition + singleLineTextHeight);
        return startPosition + singleLineTextHeight;
    }

    /**
     * 绘制一行文字
     * @param canvas 画布
     * @param textPaint 画笔
     * @param text 所有文本
     * @param start 文本的起始位置
     * @param end 文本的结束位置
     * @param startPosition 绘制的纵向起始位置
     * @param endPosition 绘制的纵向结束位置
     */
    private void drawLineText(Canvas canvas, Paint textPaint, String text, int start, int end, float startPosition, float endPosition) {

        if (isOutOfBounds(startPosition, endPosition)) {
            return;
        }
        String drawText = text.substring(start, end);
        float x = getHorizontalStartPosition(textPaint.measureText(text, start, end));
        float y = startPosition - textPaint.ascent();
        canvas.drawText(drawText, x, y, textPaint);
    }

    /**
     * 设置歌词
     * @param lrc
     */
    public void setLrc(Lrc lrc) {
        this.lrc = lrc;
        calculateTotalHeight();
    }

    /**
     * 设置播放的时间
     * @param playingTime
     */
    public void setPlayingTime(long playingTime) {

        long offsetTime = lrc == null ? 0 : lrc.getOffset();
        this.playingTime = playingTime + offsetTime;
        invalidate();
        Lrc.Item playingItem = getPlayingItem();
        if (!isBeingDragged && playingItem != null && !playingItem.equals(lastPlayingItem)) {

            lastPlayingItem = playingItem;
            if (getHeight() <= 0) {
                ViewCompat.postOnAnimation(this, scrollToPlayingItemRunnable);
            } else {
                ViewCompat.postOnAnimation(this, smoothScrollToPlayingItemRunnable);
            }
        }
    }

    /**
     * 获取正在播放的item
     * @return
     */
    public Lrc.Item getPlayingItem() {

        if (lrc == null) {
            return null;
        }

        List<Lrc.Item> itemList = lrc.getItemList();
        if (itemList == null || itemList.isEmpty()) {
            return null;
        }

        for (Lrc.Item item : itemList) {

            if (isItemPlaying(item)) {
                return item;
            }
        }
        return null;
    }

    /**
     * 获取item的时间
     * @param item item
     * @return 时间
     */
    private long getItemLrcTime(Lrc.Item item) {

        return item.getMinute() * 60 * 1000 + item.getSecond() * 1000 + item.getMilliSecond();
    }

    /**
     * 获取文本的实际高度
     * @param text 文本
     * @param textPaint 画笔
     * @return
     */
    private float getTextHeight(String text, Paint textPaint) {

        float startPosition = 0;
        float singleLineTextHeight = textPaint.descent() - textPaint.ascent() + lineSpacing;

        if (text == null || text.isEmpty()) {
            return startPosition + singleLineTextHeight;
        }
        int maxTextWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        int textLength = text.length();
        int offset = 0;
        float lastTextWidth = 0;
        for (int i = 1; i <= textLength; i++) {

            float textWidth = textPaint.measureText(text, offset, i);
            if (lastTextWidth < maxTextWidth && textWidth > maxTextWidth) {

                //如果上次测量的宽度小于最大宽度，本次测量的宽度大于最大宽度，绘制上次测量的文字
                offset = i - 1;
                startPosition += singleLineTextHeight;
            }
            lastTextWidth = textWidth;
        }
        return startPosition + singleLineTextHeight;
    }

    /**
     * 是否超过纵向的范围
     * @param startPosition 起始位置
     * @param endPosition 结束位置
     * @return
     */
    private boolean isOutOfBounds(float startPosition, float endPosition) {

        return endPosition < 0 || startPosition > getHeight() - getPaddingBottom();
    }

    /**
     * 获取绘制水平方向的起始位置
     * @param textWidth 文本宽度
     * @return
     */
    private float getHorizontalStartPosition(float textWidth) {

        return (getWidth() - getPaddingLeft() - getPaddingRight() - textWidth) / 2 + getPaddingLeft();
    }


    /**
     * 触摸事件
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }
        velocityTracker.addMovement(event);

        float currY = event.getY();

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:

                mLastMotionY = currY;
                lrcFlingRunnable.stop();
                removeCallbacks(smoothScrollToPlayingItemRunnable);
                removeCallbacks(scrollToPlayingItemRunnable);
                break;
            case MotionEvent.ACTION_MOVE:

                float deltaY = mLastMotionY - currY;

                if (!isBeingDragged && Math.abs(deltaY) > mTouchSlop) {

                    isBeingDragged = true;
                    if (deltaY > 0) {
                        deltaY -= mTouchSlop;
                    } else {
                        deltaY += mTouchSlop;
                    }
                }

                if (isBeingDragged) {

                    float result = clamp(verticalOffset - deltaY);

                    if (verticalOffset != result) {
                        verticalOffset = result;
                        invalidate();
                    }
                    mLastMotionY = currY;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                velocityTracker.computeCurrentVelocity(1000);
                float yVelocity = velocityTracker.getYVelocity();
                if (Math.abs(yVelocity) > minFlingVelocity && Math.abs(yVelocity) < maxFlingVelocity) {
                    lrcFlingRunnable.fling((int) yVelocity);
                }
                velocityTracker.recycle();
                velocityTracker = null;

                if (isBeingDragged && isResetAfterDrag) {
                    ViewCompat.postOnAnimationDelayed(this, smoothScrollToPlayingItemRunnable, resetDelayTime);
                }

                isBeingDragged = false;
                break;
        }

        getParent().requestDisallowInterceptTouchEvent(isBeingDragged);
        return true;
    }

    /**
     * 滑动辅助类
     */
    private class LrcFlingRunnable implements Runnable {

        private OverScroller mScroller;
        private boolean isStop;


        LrcFlingRunnable() {

            mScroller = new OverScroller(getContext());
        }

        @Override
        public void run() {

            if (isStop) {
                mScroller.abortAnimation();
                return;
            }
            if (mScroller.computeScrollOffset()) {

                verticalOffset = mScroller.getCurrY();
                invalidate();
                ViewCompat.postOnAnimation(LrcView.this, this);
            }
        }

        /**
         * 快速滑动
         * @param velocity
         */
        void fling(int velocity) {

            isStop = false;
            int minY = (int) (getHeight() - totalHeight);
            int maxY = 0;
            mScroller.fling(mScroller.getCurrX(), (int) verticalOffset, 0, velocity, 0, 0, minY, maxY);
            ViewCompat.postOnAnimation(LrcView.this, this);
        }

        /**
         * 停止滚动
         */
        void stop() {

            isStop = true;
        }

        /**
         * 缓慢滚动到item的中点
         * @param item
         */
        void smoothScrollToItem(Lrc.Item item) {

            if (item == null) {
                return;
            }
            isStop = false;
            float targetOffset = clamp(getItemScrollOffset(item));
            mScroller.startScroll(mScroller.getCurrX(), (int) verticalOffset, 0, (int) (targetOffset - verticalOffset));
            ViewCompat.postOnAnimation(LrcView.this, this);
        }
    }

    /**
     * 滚动到指定item的中点
     * @param item item
     */
    void scrollToItem(Lrc.Item item) {

        verticalOffset = clamp(getItemScrollOffset(item));
        invalidate();
    }

    /**
     * 获取从起点滚动到item的偏移量总和
     * @param item item
     * @return 偏移量
     */
    private float getItemScrollOffset(Lrc.Item item) {

        float fixCenterOffset = isItemPlaying(item) ? getTextHeight(item.getText(),playingPaint) / 2 : getTextHeight(item.getText(),normalPaint) / 2;
        return getCenterPosition() - fixCenterOffset - getItemPosition(item);
    }

    /**
     * 计算总高度
     */
    private void calculateTotalHeight() {

        totalHeight = getTotalHeight();
    }

    /**
     * 获取所有歌词的高度总和
     * @return
     */
    private float getTotalHeight() {

        float totalHeight = 0;

        totalHeight += getTextHeight(lrc.getTitle(), normalPaint);

        totalHeight += getTextHeight(lrc.getArtist(), normalPaint);

        totalHeight += getTextHeight(lrc.getAlbum(), normalPaint);

        List<Lrc.Item> itemList = lrc.getItemList();
        if (itemList == null || itemList.isEmpty()) {
            return totalHeight;
        }
        Lrc.Item playingItem = getPlayingItem();
        for (Lrc.Item item :
                itemList) {

            if (item.equals(playingItem)) {
                totalHeight += getTextHeight(item.getText(), playingPaint);
            } else {
                totalHeight += getTextHeight(item.getText(), normalPaint);
            }
        }
        return totalHeight;
    }

    /**
     * 获取item的位置
     * @param targetItem item
     * @return item的上边界位置
     */
    private float getItemPosition(Lrc.Item targetItem) {

        if (lrc == null) {
            return 0;
        }

        float position = getPaddingTop();

        position += getTextHeight(lrc.getTitle(), normalPaint);

        position += getTextHeight(lrc.getArtist(), normalPaint);

        position += getTextHeight(lrc.getAlbum(), normalPaint);


        List<Lrc.Item> itemList = lrc.getItemList();
        if (itemList == null || itemList.isEmpty()) {
            return position;
        }

        Lrc.Item playingItem = getPlayingItem();
        for (Lrc.Item item :
                itemList) {

            if (item.equals(targetItem)) {
                return position;
            }

            if (item.equals(playingItem)) {
                position += getTextHeight(item.getText(), playingPaint);
            } else {
                position += getTextHeight(item.getText(), normalPaint);
            }
        }
        return 0;
    }

    /**
     * 返回滚动的合理范围
     * @param position 不合理范围
     * @return 合理范围
     */
    private float clamp(float position) {

        if (position > 0) {
            return 0;
        }
        if (position < getHeight() - totalHeight) {
            return getHeight() - totalHeight;
        }
        return position;
    }

    /**
     * item是否是正在播放的
     * @param item item
     * @return 是否播放
     */
    private boolean isItemPlaying(Lrc.Item item) {

        if (lrc == null || item == null) {
            return false;
        }

        List<Lrc.Item> itemList = lrc.getItemList();
        if (lrc.getItemList() == null || lrc.getItemList().isEmpty()) {
            return false;
        }
        final int itemCount = itemList.size();
        long itemTime = getItemLrcTime(item);
        int itemIndex = itemList.indexOf(item);
        if (itemIndex - 1 >= 0 && itemIndex < itemCount) {
            Lrc.Item preItem = itemList.get(itemIndex - 1);
            long preItemTime = getItemLrcTime(preItem);
            return preItemTime < playingTime && itemTime >= playingTime;
        } else if (itemIndex == 0) {

            Lrc.Item nextItem = itemList.get(itemIndex + 1);
            long nextItemTime = getItemLrcTime(nextItem);
            return itemTime >= playingTime && playingTime < nextItemTime;
        } else if (itemIndex == itemCount - 1) {
            return itemTime > playingTime;
        }
        return false;
    }

    /**
     * 获取view中间的位置
     * @return
     */
    private float getCenterPosition() {

        return (getHeight() - getPaddingTop() - getPaddingBottom()) / 2f;
    }


    /**
     * 缓慢滚动到播放item的run实现
     */
    private Runnable smoothScrollToPlayingItemRunnable = new Runnable() {

        @Override
        public void run() {

            lrcFlingRunnable.smoothScrollToItem(getPlayingItem());
        }
    };

    /**
     * 直接滚动到播放item的run实现
     */
    private Runnable scrollToPlayingItemRunnable = new Runnable() {

        @Override
        public void run() {

            scrollToItem(getPlayingItem());
        }
    };
}
