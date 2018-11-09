package com.sitong.changqin.view.expandable.viewholder;

import android.view.View;
import android.widget.TextView;

import com.github.huajianjiang.expandablerecyclerview.widget.ChildViewHolder;
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
        String info = data.getInfo();
        TextView tv_info = getView(R.id.info);
        tv_info.setText(info);
        getView(R.id.dot).setBackgroundColor(data.getDot());
    }

}
