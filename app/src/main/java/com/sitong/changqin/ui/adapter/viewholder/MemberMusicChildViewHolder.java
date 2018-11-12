package com.sitong.changqin.ui.adapter.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.huajianjiang.expandablerecyclerview.widget.ChildViewHolder;
import com.sitong.changqin.mvp.model.bean.MemberMusciChild;
import com.sitong.changqin.view.TextViewVertical;
import com.sitong.changqin.view.expandable.model.MyChild;
import com.stringedzithers.sitong.R;


/**
 * Created by jhj_Plus on 2016/9/2.
 */
public class MemberMusicChildViewHolder extends ChildViewHolder<MemberMusciChild> {
    private static final String TAG = "MyChildViewHolder";

    public MemberMusicChildViewHolder(View itemView) {
        super(itemView);
    }

    public void bind(MemberMusciChild data) {
        TextView tv_info = getView(R.id.tv_music_name);
        tv_info.setText(data.getName());
    }

}
