package com.test.sampleandroidmediaplayer.ui.widget;

import com.test.sampleandroidmediaplayer.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;


public class StateFrameLayout extends FrameLayout {

    private AttributeSet attrs;
    private View mContentView;
    private View mProgressView;
    private View mErrorView;
    private State mState = State.CONTENT;

    public static enum State {
        CONTENT,
        PROGRESS,
        ERORR;
    }

    public State getState() {
        return mState;
    }

    public boolean isStateContent() {
        return (mState == State.CONTENT);
    }

    public boolean isStateProgress() {
        return (mState == State.PROGRESS);
    }

    public boolean isStateError() {
        return (mState == State.ERORR);
    }

    public StateFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.attrs = attrs;
    }

    @Override
    public void onFinishInflate() {
        setupView();
    }

    private void setupView() {

        List<View> children = getAllChildren();
        removeAllViews();

        mContentView = inflateContentView(children);
        mProgressView = View.inflate(getContext(), R.layout.progress_view, null);
        mErrorView = View.inflate(getContext(), R.layout.error_view, null);

        addView(mContentView);
        addView(mProgressView);
        addView(mErrorView);
    }

    private List<View> getAllChildren() {
        List<View> children = new ArrayList<View>();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            children.add(getChildAt(i));
        }

        return children;
    }

    private View inflateContentView(List<View> viewList) {
        FrameLayout frameLayout = new FrameLayout(getContext());
        for (View view : viewList) {
            frameLayout.addView(view);
        }
        return frameLayout;
    }

    public void showProgress() {
        mContentView.setVisibility(View.VISIBLE);
        mProgressView.setVisibility(View.VISIBLE);
        mErrorView.setVisibility(View.GONE);

        mState = State.PROGRESS;
    }

    public void showError() {
        mContentView.setVisibility(View.GONE);
        mProgressView.setVisibility(View.GONE);
        mErrorView.setVisibility(View.VISIBLE);

        mState = State.ERORR;
    }

    public void showContent() {
        mContentView.setVisibility(View.VISIBLE);
        mProgressView.setVisibility(View.GONE);
        mErrorView.setVisibility(View.GONE);

        mState = State.CONTENT;
    }
}

