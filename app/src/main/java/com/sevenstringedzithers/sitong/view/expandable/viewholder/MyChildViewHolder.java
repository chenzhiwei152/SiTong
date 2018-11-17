package com.sevenstringedzithers.sitong.view.expandable.viewholder;

import android.view.View;
import android.widget.ImageView;

import com.github.huajianjiang.expandablerecyclerview.widget.ChildViewHolder;
import com.sevenstringedzithers.sitong.R;
import com.sevenstringedzithers.sitong.view.TextViewVertical;
import com.sevenstringedzithers.sitong.view.expandable.model.MyChild;


/**
 * Created by jhj_Plus on 2016/9/2.
 */
public class MyChildViewHolder extends ChildViewHolder<MyChild> {
    private static final String TAG = "MyChildViewHolder";

    public MyChildViewHolder(View itemView) {
        super(itemView);
    }

    public void bind(MyChild data) {
        TextViewVertical tv_info = getView(R.id.tv_title);
        tv_info.setText(data.getTitle());
        ImageView iv=getView(R.id.android);
        iv.setImageResource(data.getImage());
    }

}
