/*
 * This source code is licensed under the MIT-style license found in the
 * LICENSE file in the root directory of this source tree.
 */
package com.netease.hearttouch.htrefreshrecyclerview.viewimpl;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.netease.hearttouch.htrefreshrecyclerview.R;
import com.netease.hearttouch.htrefreshrecyclerview.base.HTBaseRecyclerView;
import com.netease.hearttouch.htrefreshrecyclerview.base.HTBaseViewHolder;
import com.netease.hearttouch.htrefreshrecyclerview.base.HTOrientation;
import com.netease.hearttouch.htrefreshrecyclerview.base.HTViewHolderTracker;

/**
 * 默认水平方向的刷新样式实现
 */
public class HTDefaultHorizontalRefreshViewHolder extends HTBaseViewHolder {
    private TextView mTvRefreshStatus;
    private ImageView mIvRefreshArrow;
    private ProgressBar mRefreshProgressBar;
    private View mVLoadMore;
    private View mVNoMore;
    private RotateAnimation mLeftAnim;
    private RotateAnimation mRightAnim;

    public HTDefaultHorizontalRefreshViewHolder(Context context) {
        super(context);
        initAnimation();
    }

    private void initAnimation() {
        mLeftAnim = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mLeftAnim.setDuration(200);
        mLeftAnim.setFillAfter(true);

        mRightAnim = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRightAnim.setDuration(200);
        mRightAnim.setFillAfter(true);
    }

    @Override
    public View onInitRefreshView() {
        View refreshView = View.inflate(mContext, R.layout.ht_view_horizontal_refresh_default, null);
        mTvRefreshStatus = (TextView) refreshView.findViewById(R.id.tv_refresh_status);
        mIvRefreshArrow = (ImageView) refreshView.findViewById(R.id.iv_refresh_arrow);
        mRefreshProgressBar = (ProgressBar) refreshView.findViewById(R.id.pb_loading);
        return refreshView;
    }

    @Override
    public View onInitLoadMoreView() {
        View loadMoreView = View.inflate(mContext, R.layout.ht_view_horizontal_load_more_default, null);
        mVLoadMore = loadMoreView.findViewById(R.id.liner_loading);
        mVNoMore = loadMoreView.findViewById(R.id.tv_no_more);
        return loadMoreView;
    }

    @Override
    public void onLoadMoreStart(boolean hasMore) {
    }

    @Override
    public void onLoadMoreComplete(boolean hasMore) {
        if (hasMore) {
            mVLoadMore.setVisibility(View.VISIBLE);
            mVNoMore.setVisibility(View.GONE);
        } else {
            mVLoadMore.setVisibility(View.GONE);
            mVNoMore.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onReset() {
        hideRotateView();
        mRefreshProgressBar.setVisibility(View.GONE);
        mTvRefreshStatus.setText(R.string.refresh_complete);
    }


    private void hideRotateView() {
        mIvRefreshArrow.clearAnimation();//清空动画才能隐藏
        mIvRefreshArrow.setVisibility(View.GONE);
    }

    @Override
    public void onRefreshPrepare() {
        mRefreshProgressBar.setVisibility(View.GONE);
        mIvRefreshArrow.setVisibility(View.VISIBLE);
        mTvRefreshStatus.setText(R.string.pull_to_refresh);
    }

    @Override
    public void onRefreshing() {
        hideRotateView();
        mRefreshProgressBar.setVisibility(View.VISIBLE);
        mTvRefreshStatus.setText(R.string.refresh);
    }

    @Override
    public void onRefreshComplete() {
        hideRotateView();
        mRefreshProgressBar.setVisibility(View.GONE);
        mTvRefreshStatus.setText(R.string.refresh_complete);
    }

    @Override
    public void onRefreshPositionChange(float scale, float moveDistance, int refreshStatus, HTViewHolderTracker viewHolderTracker) {
        final int mOffsetToRefresh = viewHolderTracker.getOffsetToRefresh();
        final int currentPos = viewHolderTracker.getCurrentPos();
        final int lastPos = viewHolderTracker.getLastPos();
        final boolean isTouch = viewHolderTracker.isUnderTouch();
        if (isTouch && refreshStatus == HTBaseRecyclerView.RefreshStatus.REFRESH_PREPARE) {
            if (currentPos < mOffsetToRefresh && lastPos >= mOffsetToRefresh) {
                mTvRefreshStatus.setText(R.string.pull_to_refresh);
                mIvRefreshArrow.startAnimation(mRightAnim);

            } else if (currentPos > mOffsetToRefresh && lastPos <= mOffsetToRefresh) {
                mTvRefreshStatus.setText(R.string.release_to_refresh);
                mIvRefreshArrow.startAnimation(mLeftAnim);
            }
        }
    }

    public void setDefaultRefreshViewArrow(int orientation) {
        if (mIvRefreshArrow == null) return;
        switch (orientation) {
            case HTOrientation.HORIZONTAL_LEFT:
                mIvRefreshArrow.setImageResource(R.drawable.ht_left_arrow_default);
                break;
            case HTOrientation.HORIZONTAL_RIGHT:
            default:
                mIvRefreshArrow.setImageResource(R.drawable.ht_right_arrow_default);
                break;
        }
    }
}
