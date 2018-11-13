package com.sitong.changqin.ui.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.huajianjiang.expandablerecyclerview.widget.ExpandableAdapter;
import com.sevenstringedzithers.sitong.R;
import com.sitong.changqin.mvp.model.bean.MemberMusciChild;
import com.sitong.changqin.mvp.model.bean.MemberMusciParent;
import com.sitong.changqin.ui.adapter.viewholder.MemberMusicChildViewHolder;
import com.sitong.changqin.ui.adapter.viewholder.MemberMusicParentViewHolder;

import java.util.List;

/**
 * Created by jhj_Plus on 2016/9/2.
 */
public class MemberMusicListAdapter
        extends ExpandableAdapter<MemberMusicParentViewHolder, MemberMusicChildViewHolder, MemberMusciParent, MemberMusciChild>
{
    private static final String TAG = "MyAdapter";
    private List<MemberMusciParent> mData;
    private Context mContext;
    private LayoutInflater mInflater;

    public MemberMusicListAdapter(Context context, List<MemberMusciParent> data) {
        super(data);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mData = data;
    }
    public void updateList(List<MemberMusciParent> data){
        mData = data;
        invalidate(mData);
    }

    public List<MemberMusciParent> getData() {
        return mData;
    }

    @Override
    public MemberMusicParentViewHolder onCreateParentViewHolder(ViewGroup parent, int parentType) {
        View itemView = mInflater.inflate(parentType, parent, false);
        return new MemberMusicParentViewHolder(itemView);
    }

    @Override
    public MemberMusicChildViewHolder onCreateChildViewHolder(ViewGroup child, int childType) {
        View itemView = mInflater.inflate(childType, child, false);
        return new MemberMusicChildViewHolder(itemView);
    }

    @Override
    public void onBindParentViewHolder(MemberMusicParentViewHolder pvh, int parentPosition)
    {
        MemberMusciParent parent = getParent(parentPosition);
        pvh.bind(parent);
    }

    @Override
    public void onBindChildViewHolder(MemberMusicChildViewHolder cvh, int parentPosition, int childPosition)
    {
        MemberMusciChild child = getChild(parentPosition, childPosition);
        cvh.bind(child);
    }

    @Override
    public int getParentType(int parentPosition) {
        MemberMusciParent myParent = mData.get(parentPosition);
        return myParent.getType();
    }

    @Override
    public int getChildType(int parentPosition, int childPosition) {
        MemberMusciChild myChild = mData.get(parentPosition).getChildren().get(childPosition);
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
