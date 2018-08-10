/*
 * AUTHOR：Yan Zhenjie
 *
 * DESCRIPTION：create the File, and add the content.
 *
 * Copyright © ZhiMore. All Rights Reserved
 *
 */
package com.yanzhenjie.album.dialog;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yanzhenjie.album.AlbumActivity;
import com.yanzhenjie.album.R;
import com.yanzhenjie.album.adapter.PreviewAdapter;
import com.yanzhenjie.album.entity.AlbumImage;
import com.yanzhenjie.album.impl.OnCompatCompoundCheckListener;
import com.yanzhenjie.album.task.Poster;
import com.yanzhenjie.album.util.DisplayUtils;
import com.yanzhenjie.album.util.SelectorUtils;

import java.util.List;


/**
 * <p>用户点击某个图片，浏览这个图片所在文件夹内的大图。</p>
 */
public class AlbumPreviewDialog extends AppCompatDialog {

    private AlbumActivity mAlbumActivity;

    private FrameLayout mToolbar;
//    private MenuItem mFinishMenuItem;

    private AppCompatCheckBox mCheckBox;
    private OnCompatCompoundCheckListener mCheckListener;
    private int mCheckedImagePosition;

    private ViewPager mViewPager;
    private List<AlbumImage> mAlbumImages;

    private boolean isOpen = true;
    TextView tv_title;
    ImageView img_back;
    TextView tv_right;

    public AlbumPreviewDialog(AlbumActivity albumActivity, int toolbarColor, List<AlbumImage> albumImages, OnCompatCompoundCheckListener checkListener, int clickItemPosition, int contentHeight) {
        super(albumActivity, R.style.AlbumDialogStyle_Preview);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.album_dialog_album_preview);
        this.mActicity = albumActivity;
        this.mAlbumActivity = albumActivity;
        this.mCheckListener = checkListener;
        this.mAlbumImages = albumImages;

        mToolbar = (FrameLayout) findViewById(R.id.toolbar);
        mCheckBox = (AppCompatCheckBox) findViewById(R.id.cb_album_check);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        tv_title = (TextView) findViewById(R.id.tv_title);
        img_back = (ImageView) findViewById(R.id.img_back);
        tv_right = (TextView) findViewById(R.id.tv_right);

        initializeToolbar(toolbarColor);
        initializeCheckBox(toolbarColor);
        initializeViewPager(clickItemPosition, contentHeight);
        setMenuItemTitle();
    }


    Activity mActicity;

    public AlbumPreviewDialog(Activity albumActivity, List<AlbumImage> albumImages, int clickItemPosition, int contentHeight) {
        super(albumActivity, R.style.AlbumDialogStyle_Preview);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.album_dialog_album_preview);
        this.mActicity = albumActivity;
        this.mAlbumImages = albumImages;

        mToolbar = (FrameLayout) findViewById(R.id.toolbar);
        RelativeLayout layout_gallery_preview_bottom = (RelativeLayout) findViewById(R.id.layout_gallery_preview_bottom);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mToolbar.setVisibility(View.GONE);
        layout_gallery_preview_bottom.setVisibility(View.GONE);
        initializeViewPager(clickItemPosition, contentHeight);
        ImageView ic_finish = (ImageView) findViewById(R.id.ic_finish);
        ic_finish.setVisibility(View.VISIBLE);
        ic_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    private void initializeToolbar(int toolbarColor) {
        mToolbar.setBackgroundColor(toolbarColor);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOpen) {
                    isOpen = false;
                    Poster.getInstance().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dismiss();
                            isOpen = true;
                        }
                    }, 200);
                }
            }
        });
        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAlbumActivity.toResult(false);
            }
        });
    }

    private void initializeCheckBox(int checkColor) {
        mCheckBox.setSupportButtonTintList(SelectorUtils.createColorStateList(Color.WHITE, checkColor));
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCheckListener.onCheck(buttonView, mCheckedImagePosition, isChecked);
                setMenuItemTitle();
            }
        });
    }

    private void initializeViewPager(int currentItem, int contentHeight) {
        if (mAlbumImages.size() > 2)
            mViewPager.setOffscreenPageLimit(2);

        PreviewAdapter previewAdapter = new PreviewAdapter(mAlbumImages, contentHeight);
        mViewPager.setAdapter(previewAdapter);
        previewAdapter.setOnImageClickListener(new PreviewAdapter.OnImageClickListener() {
            @Override
            public void onImageClick(int position) {
                dismiss();
            }
        });
        ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mCheckedImagePosition = position;
                AlbumImage albumImage = mAlbumImages.get(mCheckedImagePosition);
                if (mCheckBox != null) {
                    mCheckBox.setChecked(albumImage.isChecked());
                }
                if (tv_title != null) {
                    tv_title.setText(mCheckedImagePosition + 1 + " / " + mAlbumImages.size());
                }
            }
        };
        mViewPager.addOnPageChangeListener(pageChangeListener);
        pageChangeListener.onPageSelected(0);
        mViewPager.setCurrentItem(currentItem);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setLayout(-1, -2);
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);
        final WindowManager.LayoutParams attrs = window.getAttributes();
        attrs.width = DisplayUtils.screenWidth;
        attrs.height = DisplayUtils.screenHeight - DisplayUtils.dip2px(25) - 1;
        window.setAttributes(attrs);
    }

    /**
     * 设置Menu的选中文字。
     */
    private void setMenuItemTitle() {
        String finishStr = mAlbumActivity.getString(R.string.album_menu_finish);
        finishStr += "(" + mAlbumActivity.getCheckedImagesSize() + "/" + mAlbumActivity.getAllowCheckCount() + ")";
        tv_right.setText(finishStr);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            dismiss();
        }
        return true;
    }
}