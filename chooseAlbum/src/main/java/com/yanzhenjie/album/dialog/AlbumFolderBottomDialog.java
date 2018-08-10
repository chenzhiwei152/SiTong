/*
 * AUTHOR：Yan Zhenjie
 *
 * DESCRIPTION：create the File, and add the content.
 *
 * Copyright © ZhiMore. All Rights Reserved
 *
 */
package com.yanzhenjie.album.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.yanzhenjie.album.AlbumActivity;
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
public class AlbumFolderBottomDialog extends Dialog implements View.OnClickListener {

    private OnCompatItemClickListener mItemClickListener;

    private boolean isOpen = true;
    List<AlbumFolder> albumFolders;
    int size;
    AlbumActivity albumActivity;
    TextView tv_right, tv_title;
    LinearLayoutManager linearLayoutManager;
    DialogFolderAdapter adapter;
    RecyclerView rvContentList;
    DialogDismiss dialogDismiss;

    public AlbumFolderBottomDialog(@NonNull AlbumActivity context, @Nullable List<AlbumFolder> albumFolders, @Nullable OnCompatItemClickListener itemClickListener) {
        super(context, R.style.AlbumDialogStyle_Folder);
        albumActivity = context;
        setContentView(R.layout.album_dialog_floder_bottom);
        mItemClickListener = itemClickListener;
        this.albumFolders = albumFolders;
        size = albumFolders == null ? 0 : albumFolders.size();
        findViewById(R.id.img_back).setOnClickListener(this);
        tv_right = (TextView) findViewById(R.id.tv_right);
        tv_right.setOnClickListener(this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        albumActivity.setDialogTitle(tv_title, tv_right);
        setTitleRightIcon(R.drawable.title_up);
        rvContentList = (RecyclerView) findViewById(R.id.rv_content_list);
        rvContentList.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getContext());
        rvContentList.setLayoutManager(linearLayoutManager);
        adapter = new DialogFolderAdapter(tv_title.getText().toString(), SelectorUtils.createColorStateList(ContextCompat.getColor(context, R.color.albumWhite), ContextCompat.getColor(context, R.color.albumColorPrimary)), albumFolders, new OnCompatItemClickListener() {
            @Override
            public void onItemClick(final View view, final int position) {
                albumActivity.setDialogChoosePosition(position);
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
        });
        rvContentList.setAdapter(adapter);
        tv_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if(null!=dialogDismiss){
            dialogDismiss.albumDialogDismiss();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (linearLayoutManager.findLastCompletelyVisibleItemPosition() < albumActivity.choosePosition) {
            linearLayoutManager.scrollToPositionWithOffset(albumActivity.choosePosition, DisplayUtils.dip2px(50));
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.img_back) {
            dismiss();
        } else if (i == R.id.tv_right) {
            dismiss();
            albumActivity.toResult(false);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        final WindowManager.LayoutParams attrs = window.getAttributes();
        attrs.width = WindowManager.LayoutParams.MATCH_PARENT;
        int oneItem = DisplayUtils.dip2px(100);
        int screenH = DisplayUtils.screenHeight;
        int height = 0;
        if (size * oneItem >= screenH * 2 / 3) {
            height = screenH * 2 / 3;
        } else {
            height = size * oneItem;
        }
        attrs.height = height;
//        attrs.verticalMargin = 300;
//        attrs.y = 300;
        window.setAttributes(attrs);
        window.setGravity(Gravity.TOP);
    }

    public void setAlbumDismissListener(DialogDismiss dismissListener){
        this.dialogDismiss=dismissListener;
    }

    public interface DialogDismiss{
         void albumDialogDismiss();

    }

    /**
     * @param id icon资源id  设置标题右侧的图片
     */
    private void setTitleRightIcon(int id){
        Drawable nav_up=albumActivity.getResources().getDrawable(id);
        nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
        tv_title.setCompoundDrawables(null, null, nav_up, null);
    }

    public void setRightVisivle(int visivle){
        tv_right.setVisibility(visivle);
    }
}
