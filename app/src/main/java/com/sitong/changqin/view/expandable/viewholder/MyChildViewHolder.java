package com.sitong.changqin.view.expandable.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.huajianjiang.expandablerecyclerview.widget.ChildViewHolder;
import com.sitong.changqin.view.TextViewVertical;
import com.sitong.changqin.view.expandable.model.MyChild;
import com.stringedzithers.sitong.R;


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
