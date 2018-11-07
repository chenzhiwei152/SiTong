package com.sitong.changqin.ui.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayoutManager;
import com.sitong.changqin.mvp.model.bean.MusicDetailBean;
import com.sitong.changqin.view.dragselectrecyclerview.IDragSelectAdapter;
import com.sitong.changqin.view.dragselectrecyclerview.RectangleView;
import com.stringedzithers.sitong.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Aidan Follestad (afollestad)
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder>
        implements IDragSelectAdapter {
    private final List<Integer> selectedIndices;

    private ArrayList<MusicDetailBean.Score> list;

    public interface Listener {
        void onClick(int index);

        void onLongClick(int index);

        void onSelectionChanged(int count);
    }

    private final Listener callback;

    public MainAdapter(Listener callback) {
        super();
        this.selectedIndices = new ArrayList<>(16);
        this.callback = callback;
    }


    List<Integer> getSelectedIndices() {
        return selectedIndices;
    }

    void toggleSelected(int index) {

        if (selectedIndices.contains(index)) {
            selectedIndices.remove((Integer) index);
            clearSelected();
        } else {
            clearSelected();
            selectedIndices.add(index);
        }
        notifyItemChanged(index);
        if (callback != null) {
            callback.onSelectionChanged(selectedIndices.size());
        }
    }

    void clearSelected() {
        if (selectedIndices.isEmpty()) {
            return;
        }
        selectedIndices.clear();
        notifyDataSetChanged();
        if (callback != null) {
            callback.onSelectionChanged(0);
        }
    }


    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.griditem_main, parent, false);
        return new MainViewHolder(v, callback);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
//        holder.label.setText(getItem(position));

        final Drawable d;
        final Context c = holder.itemView.getContext();

        if (selectedIndices.contains(position)) {
            d = new ColorDrawable(ContextCompat.getColor(c, R.color.grid_foreground_selected));
            holder.label.setTextColor(ContextCompat.getColor(c, R.color.grid_label_text_selected));
        } else {
            d = null;
            holder.label.setTextColor(ContextCompat.getColor(c, R.color.color_d0a670));
        }
        Collections.sort(selectedIndices);
        if (selectedIndices.size() > 0 && position >= selectedIndices.get(0) && position <= selectedIndices.get(selectedIndices.size() - 1)) {
            if (selectedIndices.size() == 1) {
                if (selectedIndices.get(0) == position) {
                    holder.left.setVisibility(View.VISIBLE);
                    holder.right.setVisibility(View.VISIBLE);
                } else {
                    holder.left.setVisibility(View.INVISIBLE);
                    holder.right.setVisibility(View.INVISIBLE);
                }
            } else if (position >= selectedIndices.get(0) && position <= selectedIndices.get(selectedIndices.size() - 1)) {
                if (selectedIndices.get(0) == position) {
                    holder.left.setVisibility(View.VISIBLE);
                    holder.right.setVisibility(View.INVISIBLE);
                } else if (selectedIndices.get(selectedIndices.size() - 1) == position) {
                    holder.right.setVisibility(View.VISIBLE);
                    holder.left.setVisibility(View.INVISIBLE);
                } else {
                    holder.left.setVisibility(View.INVISIBLE);
                    holder.right.setVisibility(View.INVISIBLE);
                }

            } else {
                holder.left.setVisibility(View.INVISIBLE);
                holder.right.setVisibility(View.INVISIBLE);
            }

        } else {
            holder.left.setVisibility(View.INVISIBLE);
            holder.right.setVisibility(View.INVISIBLE);
        }

        //noinspection RedundantCast
        ((LinearLayout) holder.ll_center).setBackground(d);
//        holder.colorSquare.setBackgroundColor(COLORS[position]);

        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp instanceof FlexboxLayoutManager.LayoutParams) {
            FlexboxLayoutManager.LayoutParams flexboxLp =
                    (FlexboxLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            if (position == 1 || position == 3 || position == 13) {

                flexboxLp.setWrapBefore(true);
            } else {
                flexboxLp.setWrapBefore(false);
            }
        }
    }

    @Override
    public void setSelected(int index, boolean selected) {
        Log.d("MainAdapter", "setSelected(" + index + ", " + selected + ")");
        if (!selected) {
            selectedIndices.remove((Integer) index);
        } else if (!selectedIndices.contains(index)) {
            selectedIndices.add(index);
        }
        notifyItemChanged(index);
        if (callback != null) {
            callback.onSelectionChanged(selectedIndices.size());
        }
    }

    @Override
    public boolean isIndexSelectable(int index) {
        return true;
    }

    @Override
    public int getItemCount() {
        if (list==null){
            return 0;
        }else {
           list.size();
        }
        return 0;
    }

    static class MainViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {

        private final TextView label;
        final RectangleView colorSquare;
        private final Listener callback;
        private LinearLayout ll_center;
        private TextView left;
        private TextView right;

        MainViewHolder(View itemView, Listener callback) {
            super(itemView);
            this.callback = callback;
            this.label = itemView.findViewById(R.id.label);
            this.colorSquare = itemView.findViewById(R.id.colorSquare);
            this.right = itemView.findViewById(R.id.right);
            this.left = itemView.findViewById(R.id.left);
            this.ll_center = itemView.findViewById(R.id.ll_center);
            this.itemView.setOnClickListener(this);
            this.itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (callback != null) {
                callback.onClick(getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (callback != null) {
                callback.onLongClick(getAdapterPosition());
            }
            return true;
        }
    }
}
