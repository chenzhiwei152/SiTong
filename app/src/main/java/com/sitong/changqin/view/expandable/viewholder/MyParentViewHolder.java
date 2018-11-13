package com.sitong.changqin.view.expandable.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.huajianjiang.expandablerecyclerview.widget.ParentViewHolder;
import com.sevenstringedzithers.sitong.R;
import com.sitong.changqin.view.expandable.model.MyParent;


/**
 * Created by jhj_Plus on 2016/9/2.
 */
public class MyParentViewHolder extends ParentViewHolder<MyParent> {
    private static final String TAG = "MyParentViewHolder";

    public MyParentViewHolder(View itemView) {
        super(itemView);
    }

    public void bind(MyParent data) {
//        String info = data.getInfo();
//        TextView tv_info = getView(R.id.info);
//        tv_info.setText(info);
//        getView(R.id.dot).setBackgroundColor(data.getDot());
//        View arrow = getView(R.id.arrow);
//        arrow.setVisibility(isExpandable() ? View.VISIBLE : View.GONE);
//        if (isExpandable()) {
//            arrow.setRotation(isExpanded() ? 180 : 0);
//        }
        ImageView iv = getView(R.id.android);
        TextView tv = getView(R.id.tv_title);
        if (isExpanded()){
            iv.setImageResource(data.getImage()[1]);
        }else {
        iv.setImageResource(data.getImage()[0]);
        }
        tv.setText(data.getTitle());
    }
}
