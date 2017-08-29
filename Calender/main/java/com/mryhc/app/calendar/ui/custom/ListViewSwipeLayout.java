package com.mryhc.app.calendar.ui.custom;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.mryhc.app.calendar.R;
import com.mryhc.app.calendar.utils.Common;
import com.mryhc.app.calendar.utils.MyLog;

/**
 * Created by mryhc on 2017/7/23.
 * Description 滑动删除中ListView中的item布局；
 */

public class ListViewSwipeLayout extends FrameLayout {

    private ViewDragHelper viewDragHelper;

    private LayoutInflater inflater;
    private int contentLayoutId;

    private View contentView;
    private FrameLayout frontView;
    private View backView;

    private int frontViewWidth;
    private int frontViewHeight;

    private int moveDistance;

    /**
     * 默认状态是关闭
     */
    private Status status = Status.CLOSE;
    private OnSwipeListener swipeListener;

    public ListViewSwipeLayout(@NonNull Context context) {
        super(context);
        initialize(context, null, 0);
    }

    public ListViewSwipeLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs, 0);
    }

    public ListViewSwipeLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(Context context, AttributeSet attrs, int defStyleAttr){
        inflater = LayoutInflater.from(context);
        contentView = inflater.inflate(R.layout.notice_item_in_swipe_layout, null);
        viewDragHelper = ViewDragHelper.create(this, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return true;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                if(child == frontView){
                    if(left>0){
                        return 0;
                    }else if(left < -moveDistance){
                        return -moveDistance;
                    }
                }else if(child == backView){
                    if(left > frontViewWidth){
                        return frontViewWidth;
                    }else if(left < frontViewWidth - moveDistance){
                        return frontViewWidth - moveDistance;
                    }
                }
                return  left;
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                super.onViewPositionChanged(changedView, left, top, dx, dy);
                if(changedView == frontView){
                    backView.offsetLeftAndRight(dx);
                }else if(changedView == backView){
                    frontView.offsetLeftAndRight(dx);
                }
                dispatchSwipeEvent();
                invalidate();
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                if (xvel == 0 && frontView.getLeft() < -moveDistance / 2.0f) {
                    open();
                } else if (xvel < 0) {
                    open();
                } else {
                    close();
                }
            }


        });
    }

    /**
     * 关闭
     */
    public void close() {
        close(true);
    }
    //打开
    public void open() {
        open(true);
    }

    /**
     * 关闭
     * @param isSmooth
     */
    public void close(boolean isSmooth) {
        int finalLeft = 0;
        if (isSmooth) {
            //开始动画 如果返回true表示没有完成动画
            if (viewDragHelper.smoothSlideViewTo(frontView, finalLeft, 0)) {
                ViewCompat.postInvalidateOnAnimation(this);
            }
        } else {
            layoutContent(false);
        }
    }

    /**
     * 打开
     * @param isSmooth
     */
    public void open(boolean isSmooth) {
        int finalLeft = -moveDistance;
        if (isSmooth) {
            // 开始动画
            if (viewDragHelper.smoothSlideViewTo(frontView, finalLeft, 0)) {
                ViewCompat.postInvalidateOnAnimation(this);
            }
        } else {
            layoutContent(true);
        }
    }
    /**
     * 持续动画
     */
    @Override
    public void computeScroll() {
        super.computeScroll();
        // 这个是固定的
        if (viewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try{
            viewDragHelper.processTouchEvent(event);
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        frontView = (FrameLayout) getChildAt(1);
        backView = getChildAt(0);

//        View deleteView = backView.findViewById(R.id.list_view_swipe_layout_delete_view);
//        deleteView.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getContext(), "删除", Toast.LENGTH_SHORT).show();
//            }
//        });

        frontView.addView(contentView);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        frontViewWidth = frontView.getMeasuredWidth();
        frontViewHeight= frontView.getMeasuredHeight();

        moveDistance = backView.getMeasuredWidth();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        layoutContent(false);
    }

    private void layoutContent(boolean isOpen){
        // 摆放前面的view
        Rect frontRect = computeFrontViewRect(isOpen);
        frontView.layout(frontRect.left, frontRect.top, frontRect.right, frontRect.bottom);
        // 摆放后面的view
        Rect backRect = computeBackViewRect(frontRect);
        backView.layout(backRect.left,backRect.top,backRect.right,backRect.bottom);
        // 前置前view
        bringChildToFront(frontView);
    }

    private Rect computeBackViewRect(Rect frontRect) {
        int left = frontRect.right;
        return new Rect(left, 0, left + moveDistance, 0 + frontViewHeight);
    }
    private Rect computeFrontViewRect(boolean isOpen) {
        int left = 0;
        if (isOpen) {
            left = -moveDistance;
        }
        return new Rect(left, 0, left + frontViewWidth, 0 + frontViewHeight);
    }

    protected void dispatchSwipeEvent() {
        //判断是否为空
        if (swipeListener != null) {
            swipeListener.onDragging(this);
        }
        // 记录上一次的状态
        Status preStatus = status;
        // 更新当前状态
        status = updateStatus();
        if (preStatus != status && swipeListener != null) {
            if (status == Status.CLOSE) {
                swipeListener.onClose(this);
            } else if (status == Status.OPEN) {
                swipeListener.onOpen(this);
            } else if (status == Status.DRAGGING) {
                if (preStatus == Status.CLOSE) {
                    swipeListener.startOpen(this);
                } else if (preStatus == Status.OPEN) {
                    swipeListener.startClose(this);
                }
            }
        }
    }

    /**
     * 更新状态
     * @return
     */
    private Status updateStatus() {
        //得到前view的左边位置
        int left = frontView.getLeft();
        if (left == 0) {
            //如果位置是0，就是关闭状态
            return Status.CLOSE;
        } else if (left == -moveDistance) {
            //如果左侧边距是后view的宽度的负值，状态为开
            return Status.OPEN;
        }
        //其他状态就是拖拽
        return Status.DRAGGING;
    }

    public void addViewToFront(int layoutId){
        contentView = inflater.inflate(R.layout.notice_item_in_swipe_layout, null);
    }

    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
    public OnSwipeListener getSwipeListener() {
        return swipeListener;
    }
    public void setSwipeListener(OnSwipeListener swipeListener) {
        this.swipeListener = swipeListener;
    }

    public enum Status{
        CLOSE, OPEN, DRAGGING
    }

    public interface OnSwipeListener{
        void onClose(ListViewSwipeLayout swipeLayout);
        void onOpen(ListViewSwipeLayout swipeLayout);
        void onDragging(ListViewSwipeLayout swipeLayout);
        void startClose(ListViewSwipeLayout swipeLayout);
        void startOpen(ListViewSwipeLayout swipeLayout);
    }

    public interface OnOperateListener{
        void hide();
        void delete();
    }

}
