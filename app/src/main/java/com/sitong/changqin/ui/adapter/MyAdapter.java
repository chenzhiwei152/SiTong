package com.sitong.changqin.ui.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.github.huajianjiang.expandablerecyclerview.widget.ExpandableAdapter;
import com.sitong.changqin.view.expandable.model.MyChild;
import com.sitong.changqin.view.expandable.model.MyParent;
import com.sitong.changqin.view.expandable.viewholder.MyChildViewHolder;
import com.sitong.changqin.view.expandable.viewholder.MyParentViewHolder;
import com.stringedzithers.sitong.R;

import java.util.List;

/**
 * Created by jhj_Plus on 2016/9/2.
 */
public class MyAdapter
        extends ExpandableAdapter<MyParentViewHolder, MyChildViewHolder, MyParent, MyChild>
{
    private static final String TAG = "MyAdapter";
    private List<MyParent> mData;
    private Context mContext;
    private LayoutInflater mInflater;

    public MyAdapter(Context context, List<MyParent> data) {
        super(data);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mData = data;
    }
    public void updateList(List<MyParent> data){
        mData = data;
        invalidate(mData);
//        notifyAllChanged();
    }

    public List<MyParent> getData() {
        return mData;
    }

    @Override
    public MyParentViewHolder onCreateParentViewHolder(ViewGroup parent, int parentType) {
        View itemView = mInflater.inflate(parentType, parent, false);
        return new MyParentViewHolder(itemView);
    }

    @Override
    public MyChildViewHolder onCreateChildViewHolder(ViewGroup child, int childType) {
        View itemView = mInflater.inflate(childType, child, false);
        return new MyChildViewHolder(itemView);
    }

    @Override
    public void onBindParentViewHolder(MyParentViewHolder pvh, int parentPosition)
    {
        MyParent parent = getParent(parentPosition);
        final String parentType = "0x" + Integer.toHexString(pvh.getType());
        String info = mContext.getString(R.string.parent_type, parentType, parentPosition,
                pvh.getAdapterPosition());
        parent.setInfo(info);
        pvh.bind(parent);
    }

    @Override
    public void onBindChildViewHolder(MyChildViewHolder cvh, int parentPosition, int childPosition)
    {
        MyChild child = getChild(parentPosition, childPosition);
        final String childType = "0x" + Integer.toHexString(cvh.getType());
        String info = mContext
                .getString(R.string.child_type, childType, childPosition, cvh.getAdapterPosition());
        child.setInfo(info);
        cvh.bind(child);
    }

    @Override
    public int getParentType(int parentPosition) {
        MyParent myParent = mData.get(parentPosition);
        return myParent.getType();
    }

    @Override
    public int getChildType(int parentPosition, int childPosition) {
        MyChild myChild = mData.get(parentPosition).getChildren().get(childPosition);
        return myChild.getType();
    }

//    @Override
//    public long getItemId(int position) {
//        return position;
//    }

    public ItemDecoration getItemDecoration() {
        return new ItemDecoration();
    }

    private class ItemDecoration extends RecyclerView.ItemDecoration {

        int itemOffset = mContext.getResources().getDimensionPixelSize(R.dimen.album_dp_10);

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state)
        {
            final int childAdapterPos = parent.getChildAdapterPosition(view);
            outRect.set(0, itemOffset, 0, childAdapterPos == getItemCount() - 1 ? itemOffset : 0);
        }
    }

}
