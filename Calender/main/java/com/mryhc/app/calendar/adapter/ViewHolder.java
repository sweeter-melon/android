package com.mryhc.app.calendar.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mryhc.app.calendar.R;

/**
 * Created by mryhc on 2017/7/21.
 * Description: CommonAdapter 的viewHolder
 */
public class ViewHolder {
    private final SparseArray<View> mViews;
    private View mConvertView;
    private int mPosition;

    public ViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
        mPosition = position;
        mViews = new SparseArray<View>();
        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        mConvertView.setTag(this);
    }

    public static ViewHolder get(Context context, View convertView, ViewGroup parent, int layoutId, int position) {
        if (convertView == null) {
            return new ViewHolder(context, parent, layoutId, position);
        } else {
            return (ViewHolder) convertView.getTag();
        }
    }

    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public View getConvertView() {
        return mConvertView;
    }

    public ViewHolder setText(int viewId, String text) {
        TextView textView = getView(viewId);
        textView.setText(text);
        return this;
    }

    public ViewHolder setText(int viewId, String text, int color) {
        TextView textView = getView(viewId);
        textView.setText(text);
        textView.setTextColor(color);
        return this;
    }

    public ViewHolder setText(int viewId, String text, int color, int status) {
        TextView textView = getView(viewId);
        if (status == 0) {
            textView.setVisibility(View.INVISIBLE);
        } else {
            textView.setVisibility(View.VISIBLE);
            textView.setText(text);
            textView.setTextColor(color);
        }
        return this;
    }

    public ViewHolder setShow(int viewId) {
        LinearLayout checkBox = getView(viewId);
        checkBox.setVisibility(View.VISIBLE);
        return this;
    }

    public ViewHolder setShowTv(int viewId) {
        TextView tv = getView(viewId);
        tv.setVisibility(View.VISIBLE);
        return this;
    }

    public ViewHolder setImageResource(int viewId, int imageId) {
        ImageView imageView = getView(viewId);
        imageView.setImageResource(imageId);
        return this;
    }

    public ViewHolder setImageVisiable(int viewId, int visibility) {
        ImageView imageView = getView(viewId);
        imageView.setVisibility(visibility);
        return this;
    }

    public ViewHolder setImageBitmap(int viewId, Bitmap bm) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bm);
        return this;
    }

    public ViewHolder setChecked(int viewId, Boolean status) {
        CheckBox cb = getView(viewId);
        cb.setChecked(status);
        return this;
    }

    public int getPosition() {
        return mPosition;
    }

    public ViewHolder setImagOnclick(int viewId, View.OnClickListener listener) {
        Button btn = getView(viewId);
        if (btn != null) {
            btn.setOnClickListener(listener);
        }
        return this;
    }

    public ViewHolder setViewEnable(int viewId, boolean isEnable) {
        View view = getView(viewId);
        if (view != null) {
            view.setEnabled(isEnable);
        }
        return this;
    }

    public ViewHolder setViewVisibility(int viewId, int visibility) {
        View view = getView(viewId);
        if (view != null) {
            view.setVisibility(visibility);
        }
        return this;
    }

    public ViewHolder setViewPosTag(int viewId, int pos){
        View view = getView(viewId);
        if(view != null){
            view.setTag(R.id.view_tag_01, pos);
        }
        return this;
    }

    public ViewHolder setViewClickListener(int viewId, View.OnClickListener listener){
        View view = getView(viewId);
        if(view != null){
            view.setOnClickListener(listener);
        }
        return this;
    }
}
