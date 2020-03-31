package com.orange.og_lite.Util;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import androidx.appcompat.widget.AppCompatEditText;

import com.orange.og_lite.callback.textchange;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * 带清除按钮的EditText
 * Created by haide.yin() on 2019/1/23 15:38.
 */
public class Util_ClearEditText extends AppCompatEditText implements View.OnFocusChangeListener, TextWatcher {
    public com.orange.og_lite.callback.textchange textchange=null;
    private String mLastText;
    private boolean mFormat;
    private boolean mInvalid;
    private int mSelection;
    private Drawable mClearDrawable; // 删除按钮的引用
    private boolean hasFocus; // 控件是否有焦点
    private ClearStatusListener mClearStatusListener;//回调接口
    private int maxWords = 30;//限制最大输入的个数

    /**
     * 构造函数
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public Util_ClearEditText(Context context, AttributeSet attrs) {
        super(context, attrs, android.R.attr.editTextStyle);
        init();
    }

    /**
     * 设置回调函数
     *
     * @param clearStatusListener 回调接口
     */
    public void setClearStatusListener(ClearStatusListener clearStatusListener) {
        this.mClearStatusListener = clearStatusListener;
    }

    /**
     * 初始化
     */
    private void init() {
        //设置最大数字限制
        mFormat = false;
        mInvalid = false;
        mLastText = "";
        setMaxWords(maxWords);
        //获取EditText的DrawableEnd和DrawableRight
        mClearDrawable = getCompoundDrawablesRelative()[2];
        if (mClearDrawable == null) {
            mClearDrawable = getCompoundDrawables()[2];
            if (mClearDrawable == null) {
                //假如没有设置我们就使用默认的图片
                //mClearDrawable = getResources().getDrawable(android.R.drawable.ic_menu_close_clear_cancel);
            }
        }
        if(mClearDrawable!=null){
            mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(), mClearDrawable.getIntrinsicHeight());
        }
        //默认设置隐藏图标
        setClearIconVisible(false);
        //设置焦点改变的监听
        setOnFocusChangeListener(this);
        //设置输入框里面内容发生改变的监听
        addTextChangedListener(this);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mClearDrawable != null && event.getAction() == MotionEvent.ACTION_UP) {
            int x = (int) event.getX();
            //判断触摸点是否在水平范围内
            boolean isInnerWidth = (x > (getWidth() - getTotalPaddingRight())) && (x < (getWidth() - getPaddingRight()));
            //获取删除图标的边界，返回一个Rect对象
            Rect rect = mClearDrawable.getBounds();
            //获取删除图标的高度
            int height = rect.height();
            int y = (int) event.getY();
            //计算图标底部到控件底部的距离
            int distance = (getHeight() - height) / 2;
            //判断触摸点是否在竖直范围内(可能会有点误差)
            //触摸点的纵坐标在distance到（distance+图标自身的高度）之内，则视为点中删除图标
            boolean isInnerHeight = (y > distance) && (y < (distance + height));
            if (isInnerHeight && isInnerWidth) {
                this.setText("");
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 设置最大数字限制
     * @param maxWords 设置最大数字限制
     */
    public void setMaxWords(int maxWords){
        this.maxWords = maxWords;
        setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxWords)});
    }

    /**
     * 最大数字限制
     * @return 设置最大数字限制
     */
    public int getMaxWords(){
        return this.maxWords;
    }

    /**
     * 设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
     *
     * @param visible 是否可见
     */
    private void setClearIconVisible(boolean visible) {
        onStatus(visible);
        Drawable right = (visible && isEnabled()) ? mClearDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }

    /**
     * 设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
     *
     * @param visible 是否可见
     */
    public void hideClearIcon(boolean visible) {
        Drawable right = visible ? mClearDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }

    /**
     * 当ClearEditText焦点发生变化的时候，判断里面字符串长度设置清除图标的显示与隐藏
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        this.hasFocus = hasFocus;
        if (hasFocus) {
            String content = getText().toString().trim();
            setClearIconVisible(!TextUtils.isEmpty(content));
        } else {
            setClearIconVisible(false);
        }
    }

    /**
     * 当输入框里面内容发生变化的时候回调的方法
     */
    @Override
    public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {

        try {

            String temp = text.toString();

            // Set selection.
            if (mLastText.equals(temp)) {
                if (mInvalid) {
                    mSelection -= 1;
                } else {
                    if ((mSelection >= 1) && (temp.length() > mSelection - 1)
                            && (temp.charAt(mSelection - 1)) == ' ') {
                        mSelection += 1;
                    }
                }
                int length = mLastText.length();
                if (mSelection > length) {

                    this.setSelection(length);
                } else {

                    this.setSelection(mSelection);
                }
                mFormat = false;
                mInvalid = false;
                return;
            }

            mFormat = true;
            mSelection = start;

            // Delete operation.
            if (lengthAfter == 0) {
                if ((mSelection >= 1) && (temp.length() > mSelection - 1)
                        && (temp.charAt(mSelection - 1)) == ' ') {
                    mSelection -= 1;
                }

                return;
            }

            // Input operation.
            mSelection += lengthAfter;
            char[] lastChar = (temp.substring(start, start + lengthAfter))
                    .toCharArray();
            int mid = lastChar[0];
            if (mid >= 48 && mid <= 57) {
            } else if (mid >= 65 && mid <= 70) {
            } else if (mid >= 97 && mid <= 102) {
            } else {
                mInvalid = true;
                temp = temp.substring(0, start)
                        + temp.substring(start + lengthAfter);
                this.setText(temp);
                return;
            }

        } catch (Exception e) {
            Log.i(TAG, e.toString());
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (mFormat) {
            StringBuilder text = new StringBuilder();
            text.append(s.toString().replace(" ", "").toUpperCase());
            mLastText = text.toString();
            this.setText(text);
            if(textchange!=null){
                textchange.callback();
            }
        }
    }


    /**
     * 是否显示清除按钮
     *
     * @param show the show
     */
    private void onStatus(boolean show) {
        if (mClearStatusListener != null) {
            mClearStatusListener.onStatus(show);
        }
    }

    /*************** 接口 *******************/

    public interface ClearStatusListener {

        /**
         * 是否显示清除按钮
         *
         * @param empty 是否是空的
         */
        void onStatus(boolean empty);
    }
}