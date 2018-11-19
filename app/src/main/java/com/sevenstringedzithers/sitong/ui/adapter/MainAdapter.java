package com.sevenstringedzithers.sitong.ui.adapter;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayoutManager;
import com.jyall.android.common.utils.ImageLoadedrManager;
import com.jyall.android.common.utils.UIUtil;
import com.sevenstringedzithers.sitong.R;
import com.sevenstringedzithers.sitong.mvp.model.bean.MusicDetailBean;
import com.sevenstringedzithers.sitong.view.dragselectrecyclerview.IDragSelectAdapter;
import com.sevenstringedzithers.sitong.view.dragselectrecyclerview.RectangleView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Aidan Follestad (afollestad)
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder>
        implements IDragSelectAdapter {
    private final List<Integer> selectedIndices;
    private Context mContext;
    private ArrayList<MusicDetailBean.Score> list;

    public void setList(ArrayList<MusicDetailBean.Score> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public interface Listener {
        void onClick(int index);

        void onLongClick(int index);

        void onSelectionChanged(int count);
    }

    private final Listener callback;

    public MainAdapter(Context context, Listener callback) {
        super();
        this.mContext = context;
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

        holder.ll_center.removeAllViews();
        holder.ll_right.removeAllViews();
        holder.ll_left_center.removeAllViews();
        holder.ll_center_dowm.removeAllViews();


        if (list.get(position).getNumbered_music().equals("-1")) {
            TextView textview = new TextView(mContext);
            textview.setText("");
            holder.ll_center.addView(textview);
            return;
        }
        if (list.get(position).getNumbered_music().equals("8")) {
            ImageView v = new ImageView(mContext);
            v.setImageResource(R.drawable.line_black_5);
            ImageView v1 = new ImageView(mContext);
            v1.setImageResource(R.drawable.line_black_vertical_20);
            ImageView v2 = new ImageView(mContext);
            v2.setImageResource(R.drawable.bg_transparent);
            holder.ll_right.addView(v);
            holder.ll_right.addView(v2);
            holder.ll_right.addView(v1);
        } else {
            //        中间的数字
            TextView textview = new TextView(mContext);
            textview.setText(list.get(position).getNumbered_music());
            holder.ll_center.addView(textview);
//下方的
            for (int i = 0; i < list.get(position).getSymbol().size(); i++) {
                MusicDetailBean.Score.Symbol symbol = list.get(position).getSymbol().get(i);
                switch (symbol.getPositioncode()) {
//符号位置
                    case 1:
//                    左上

                        break;
                    case 2:
//                    上
                        switch (symbol.getNamecode()) {
                            case 14:
//泛音
                                break;
                        }

                        break;
                    case 3:
//                    右上
                        break;
                    case 4:
//                    右
                        switch (symbol.getNamecode()) {
                            case 23:
                                ImageView v2 = new ImageView(mContext);
                                v2.setImageResource(R.drawable.bg_transparent);
                                ImageView v = new ImageView(mContext);
                                v.setImageResource(R.drawable.line_black_vertical_20);
                                holder.ll_right.addView(v2);
                                holder.ll_right.addView(v);
                                break;
                            case 9:
                                ImageView v1 = new ImageView(mContext);
                                v1.setImageResource(R.mipmap.ic_jiepai);
                                holder.ll_right.addView(v1);
                                break;
                        }
                        break;
                    case 5:
//                    右下
                        break;
                    case 6:
//                    下
                        switch (symbol.getNamecode()) {
                            case 7:
                                if (list.get(position).getToline()!=0){

                                }
                                ImageView v = new ImageView(mContext);
                                v.setImageResource(R.drawable.line_black_5);
                                holder.ll_center_dowm.addView(v);
                                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) v.getLayoutParams();
                                params.topMargin = UIUtil.dip2px(mContext, 2);
                                v.setLayoutParams(params);
                                break;
                            case 8:
                                ImageView v1 = new ImageView(mContext);
                                v1.setImageResource(R.mipmap.ic_point_black);
                                holder.ll_center_dowm.addView(v1);
                                LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) v1.getLayoutParams();
                                params1.topMargin = UIUtil.dip2px(mContext, 2);
                                v1.setLayoutParams(params1);
                                break;
                        }
                        break;
                    case 7:
//                    左下
                        break;
                    case 8:
//                    左
                        switch (symbol.getNamecode()) {
                            case 9:
                                ImageView v = new ImageView(mContext);
                                v.setImageResource(R.mipmap.ic_jiepai);
                                holder.ll_left_center.addView(v);
                                break;
                            case 11:
                                ImageView v1 = new ImageView(mContext);
                                v1.setImageResource(R.mipmap.ic_shanghuayin);
                                holder.ll_left_center.addView(v1);
                                break;
                            case 12:
                                ImageView v2 = new ImageView(mContext);
                                v2.setImageResource(R.mipmap.ic_xiahuaxin);
                                holder.ll_left_center.addView(v2);
                                break;
                        }
                        break;
                }

            }


//        最下面的图片
            ImageLoadedrManager.getInstance().display(mContext, list.get(position).getJianzipu(), holder.iv_shoushi);

        }


        final Drawable d;
        final Context c = holder.itemView.getContext();
        if (selectedIndices.contains(position)) {
            d = new ColorDrawable(ContextCompat.getColor(c, R.color.grid_foreground_selected));
//            holder.label.setTextColor(ContextCompat.getColor(c, R.color.grid_label_text_selected));
        } else {
            d = null;
//            holder.label.setTextColor(ContextCompat.getColor(c, R.color.color_d0a670));
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
//        ((LinearLayout) holder.ll_center_content).setBackground(d);
//        holder.colorSquare.setBackgroundColor(COLORS[position]);

        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp instanceof FlexboxLayoutManager.LayoutParams) {
            FlexboxLayoutManager.LayoutParams flexboxLp =
                    (FlexboxLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            if (position > 0) {
                if (list.get(position - 1).getIslinefeed() == 1) {
                    flexboxLp.setWrapBefore(true);
                } else {
                    flexboxLp.setWrapBefore(false);
                }
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
        if (list == null) {
            return 0;
        } else {
            return list.size();
        }
    }

    static class MainViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {

        final RectangleView colorSquare;
        private final Listener callback;
        private LinearLayout ll_center_content;
        private TextView left;
        private TextView right;
        private LinearLayout ll_center;
        private LinearLayout ll_center_dowm;
        private LinearLayout ll_left_center;
        private LinearLayout ll_right;
        private ImageView iv_shoushi;

        MainViewHolder(View itemView, Listener callback) {
            super(itemView);
            this.callback = callback;
            this.colorSquare = itemView.findViewById(R.id.colorSquare);
            this.right = itemView.findViewById(R.id.right);
            this.left = itemView.findViewById(R.id.left);
            this.ll_center_content = itemView.findViewById(R.id.ll_center_content);
            this.ll_center = itemView.findViewById(R.id.ll_center);
            this.ll_center_dowm = itemView.findViewById(R.id.ll_center_dowm);
            this.iv_shoushi = itemView.findViewById(R.id.iv_shoushi);
            this.ll_left_center = itemView.findViewById(R.id.ll_left_center);
            this.ll_right = itemView.findViewById(R.id.ll_right);

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
