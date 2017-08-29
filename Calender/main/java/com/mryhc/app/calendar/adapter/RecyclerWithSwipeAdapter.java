package com.mryhc.app.calendar.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mryhc.app.calendar.R;
import com.mryhc.app.calendar.io.Attributes;
import com.mryhc.app.calendar.io.SwipeAdapterInterface;
import com.mryhc.app.calendar.io.SwipeItemMangerImpl;
import com.mryhc.app.calendar.io.SwipeItemMangerInterface;
import com.mryhc.app.calendar.ui.custom.SwipeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mryhc on 2017/7/26.
 */

public abstract class RecyclerWithSwipeAdapter<T> extends RecyclerView.Adapter<RecyclerWithSwipeAdapter.ViewHolder>
        implements View.OnClickListener, View.OnLongClickListener, SwipeItemMangerInterface, SwipeAdapterInterface {

    private List<T> dataList;
    private int itemLayoutId;
    private List<Boolean> openStateList;

    private OnOperateListener listener;

    protected SwipeItemMangerImpl mItemManger = new SwipeItemMangerImpl(this);


    public int getSwipeLayoutResourceId(int position){
        return R.id.notice_swipe_layout;
    }

    public RecyclerWithSwipeAdapter(List<T> dataList, final int itemLayoutId){
        this.dataList = dataList;
        openStateList = new ArrayList<>();
        for(int i=0; i<dataList.size(); i++){
            openStateList.add(false);
        }
        this.itemLayoutId = itemLayoutId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.new_notice_item_swipe_layout, parent, false);
        FrameLayout contentLayout = (FrameLayout) view.findViewById(R.id.new_notice_item_content);
        contentLayout.addView(inflater.inflate(itemLayoutId, contentLayout, false));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemParentLayout.setTag(R.id.view_tag_01, position);
        holder.itemParentLayout.setOnClickListener(this);
        holder.itemParentLayout.setOnLongClickListener(this);

        holder.deleteView.setTag(R.id.view_tag_01, position);
        holder.deleteView.setOnClickListener(this);

        holder.hideView.setTag(R.id.view_tag_01, position);
        holder.hideView.setOnClickListener(this);

        holder.swipeLayout.close(true);

        mItemManger.bind(holder.swipeLayout, position);

        convertView(holder, position, dataList.get(position));
    }

    public abstract void convertView(ViewHolder holder, int pos, T item);

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.new_notice_item_content:
                clickItem(v);
                break;
            case R.id.new_notice_item_delete:
                deleteItem(v);
                break;
            case R.id.new_notice_item_hide:
                hideItem(v);
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()){
            case R.id.new_notice_item_content:
                longClickItem(v);
                break;
        }
        return true;
    }

    private void clickItem(View v){
        if(listener != null){
            listener.clickItem(getViewPos(v));
        }
    }

    private void deleteItem(View v){
        if(listener != null){
            listener.delete(getViewPos(v));
        }
    }

    private void hideItem(View v){
        if(listener != null){
            listener.hide(getViewPos(v));
        }
    }

    private void longClickItem(View v){
        if(listener != null){
            listener.longClickItem(getViewPos(v));
        }
    }

    private int getViewPos(View v){
        return (int) v.getTag(R.id.view_tag_01);
    }


    @Override
    public void myNotifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public void openItem(int position) {
        mItemManger.openItem(position);
    }

    @Override
    public void closeItem(int position) {
        mItemManger.closeItem(position);
    }

    @Override
    public void closeAllExcept(SwipeLayout layout) {
        mItemManger.closeAllExcept(layout);
    }

    @Override
    public void closeAllItems() {
        mItemManger.closeAllItems();
    }

    @Override
    public List<Integer> getOpenItems() {
        return mItemManger.getOpenItems();
    }

    @Override
    public List<SwipeLayout> getOpenLayouts() {
        return mItemManger.getOpenLayouts();
    }

    @Override
    public void removeShownLayouts(SwipeLayout layout) {
        mItemManger.removeShownLayouts(layout);
    }

    @Override
    public boolean isOpen(int position) {
        return mItemManger.isOpen(position);
    }

    @Override
    public Attributes.Mode getMode() {
        return mItemManger.getMode();
    }

    @Override
    public void setMode(Attributes.Mode mode) {
        mItemManger.setMode(mode);
    }

    public void setListener(OnOperateListener listener){
        this.listener = listener;
    }

    public interface OnOperateListener{
        void delete(int pos);
        void hide(int pos);
        void clickItem(int pos);
        void longClickItem(int pos);
    }

    /**
     * SwipeListView holder
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final SparseArray<View> mViews;
        SwipeLayout swipeLayout;
        FrameLayout itemParentLayout;
        ViewGroup itemLayout;

        View deleteView;
        View hideView;

        public ViewHolder(View view){
            super(view);
            mViews = new SparseArray<View>();
            swipeLayout = (SwipeLayout) view.findViewById(R.id.notice_swipe_layout);
            itemParentLayout = (FrameLayout) view.findViewById(R.id.new_notice_item_content);
            hideView = view.findViewById(R.id.new_notice_item_hide);
            deleteView = view.findViewById(R.id.new_notice_item_delete);

            itemLayout = (ViewGroup) itemParentLayout.getChildAt(0);
        }

        public <T extends View> T getView(int viewId) {
            View view = mViews.get(viewId);
            if (view == null) {
                view = itemLayout.findViewById(viewId);
                mViews.put(viewId, view);
            }
            return (T) view;
        }

        /**
         * 设置文本框文字
         * @param viewId
         * @param text
         */
        public void setTVText(int viewId, String text){
            TextView tv = getView(viewId);
            tv.setText(text);
        }

        public void setVisible(int viewId, int visibility){
            View view = getView(viewId);
            view.setVisibility(visibility);
        }

        public void setCheckBoxCheckState(int viewId, boolean checked){
            CheckBox checkBox = getView(viewId);
            checkBox.setChecked(checked);
        }
    }
}
