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
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.Gravity;
import android.view.KeyEvent;
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
import com.yanzhenjie.album.util.SelectorUtils;

import java.util.List;


/**
 * <p>用户点击某个图片，浏览这个图片所在文件夹内的大图。</p>
 */
public class AlbumImageErrorDialog extends AppCompatDialog {
    Activity mActicity;

    public AlbumImageErrorDialog(Activity albumActivity) {
        super(albumActivity, R.style.AlbumDialogStyle_Folder);
        setContentView(R.layout.album_dialog_album_imageerror);
        this.mActicity = albumActivity;
        findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setLayout(-1, -2);
//        getWindow().setLayout(-1, -1);
        Window window = getWindow();
//        final WindowManager.LayoutParams attrs = window.getAttributes();
//        attrs.width = WindowManager.LayoutParams.MATCH_PARENT;
//        attrs.height = WindowManager.LayoutParams.MATCH_PARENT;
//        window.setAttributes(attrs);
        window.setGravity(Gravity.CENTER);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            dismiss();
        }
        return true;
    }
}