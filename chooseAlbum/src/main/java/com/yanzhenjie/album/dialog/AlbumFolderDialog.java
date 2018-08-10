/*
 * AUTHOR：Yan Zhenjie
 *
 * DESCRIPTION：create the File, and add the content.
 *
 * Copyright © ZhiMore. All Rights Reserved
 *
 */
package com.yanzhenjie.album.dialog;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;

import com.yanzhenjie.album.R;
import com.yanzhenjie.album.adapter.DialogFolderAdapter;
import com.yanzhenjie.album.entity.AlbumFolder;
import com.yanzhenjie.album.impl.OnCompatItemClickListener;
import com.yanzhenjie.album.task.Poster;
import com.yanzhenjie.album.util.DisplayUtils;
import com.yanzhenjie.album.util.SelectorUtils;

import java.util.List;

/**
 */
public class AlbumFolderDialog extends BottomSheetDialog {

    private BottomSheetBehavior bottomSheetBehavior;
    private OnCompatItemClickListener mItemClickListener;

    private boolean isOpen = true;

    public AlbumFolderDialog(@NonNull Context context, @ColorInt int toolbarColor, @Nullable List<AlbumFolder> albumFolders, @Nullable OnCompatItemClickListener itemClickListener) {
        super(context, R.style.AlbumDialogStyle_Folder);
        setContentView(R.layout.album_dialog_album_floder);
        mItemClickListener = itemClickListener;

        fixRestart();

        RecyclerView rvContentList = (RecyclerView) findViewById(R.id.rv_content_list);
        rvContentList.setHasFixedSize(true);
        rvContentList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvContentList.setAdapter(new DialogFolderAdapter("", SelectorUtils.createColorStateList(ContextCompat.getColor(context, R.color.albumPrimaryBlack), toolbarColor), albumFolders, new OnCompatItemClickListener() {
            @Override
            public void onItemClick(final View view, final int position) {
                if (isOpen) { // 反应太快，按钮点击效果出不来，故加延迟。
                    isOpen = false;
                    Poster.getInstance().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (mItemClickListener != null) {
                                mItemClickListener.onItemClick(view, position);
                            }
                            isOpen = true;
                            dismiss();
                        }
                    }, 200);
                }
            }
        }));
        setStatusBarColor(toolbarColor);
    }

    private void setStatusBarColor(@ColorInt int color) {
        if (Build.VERSION.SDK_INT >= 21) {
            final Window window = getWindow();
            if (window != null) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(color);
                window.setNavigationBarColor(ContextCompat.getColor(getContext(), R.color.albumPrimaryBlack));
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        final WindowManager.LayoutParams attrs = window.getAttributes();
        attrs.width = WindowManager.LayoutParams.MATCH_PARENT;
        attrs.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(attrs);
    }


    @Override
    public void show() {
        bottomSheetBehavior.setPeekHeight(DisplayUtils.screenHeight * 2 / 3);
        super.show();
    }

    View bottom_View;

    /**
     * 修复不能重新显示的bug。
     */
    private void fixRestart() {
        bottom_View = findViewById(R.id.design_bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottom_View);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            boolean isDiss = false;

            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                Log.e("-onStateChanged->", newState + "");
             /*   if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    dismiss();
                }*/
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                if (slideOffset == 0 && bottomSheetBehavior.getPeekHeight() != 0) {
                    bottomSheetBehavior.setPeekHeight(0);
                }

                if (slideOffset == 0 && bottomSheetBehavior.getPeekHeight() == 0) {
                    if (isDiss) {
                        dismiss();
                    }
                    isDiss = true;
                }

                Log.e("-onSlide->", "--slideOffset->" + slideOffset + "--->" + bottomSheetBehavior.getPeekHeight() + "--->" + bottomSheetBehavior.getState());
            }
        });
    }


}
