package com.sevenstringedzithers.sitong.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayoutManager;
import com.jyall.android.common.utils.ImageLoadedrManager;
import com.jyall.android.common.utils.LogUtils;
import com.jyall.android.common.utils.UIUtil;
import com.sevenstringedzithers.sitong.R;
import com.sevenstringedzithers.sitong.mvp.model.bean.MusicDetailBean;
import com.sevenstringedzithers.sitong.utils.ImageUtils;
import com.sevenstringedzithers.sitong.view.dragselectrecyclerview.IDragSelectAdapter;
import com.sevenstringedzithers.sitong.view.dragselectrecyclerview.RectangleView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

/**
 * @author Aidan Follestad (afollestad)
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder>
        implements IDragSelectAdapter {
    private final List<Integer> selectedIndices;
    private Context mContext;
    private boolean isSelected = false;
    private TreeMap<Integer, Integer> yanyinSet = new TreeMap<>();
    private TreeMap<Integer, Integer> yanyinSetWithNum = new TreeMap<>();
    private ArrayList<MusicDetailBean.Score> list;
    private boolean isScrolling = false;

    public void setScrolling(boolean scrolling) {
        isScrolling = scrolling;
    }

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


    public List<Integer> getSelectedIndices() {
        return selectedIndices;
    }

    public void toggleSelected(int index) {

        if (selectedIndices.contains(index)) {
            selectedIndices.remove((Integer) index);
            clearSelected();
        } else {
            clearSelected();
            selectedIndices.add(index);
        }
        Collections.sort(selectedIndices);
        notifyItemChanged(index);
        if (callback != null) {
            callback.onSelectionChanged(selectedIndices.size());
        }
    }

    public void clearSelected() {
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

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        isSelected = selectedIndices.contains(position);
        holder.ll_center.removeAllViews();
        holder.ll_right.removeAllViews();
        holder.ll_left_center.removeAllViews();
        holder.ll_center_dowm.removeAllViews();
        holder.ll_center_top.removeAllViews();
        holder.ll_right_down.removeAllViews();
        holder.ll_left_down.removeAllViews();
        holder.ll_limit_top.removeAllViews();
        holder.ll_limit_right.removeAllViews();
        holder.ll_limit_left.removeAllViews();
        holder.ll_left_top.removeAllViews();
//        最下面的图片
        if (!isScrolling && !TextUtils.isEmpty(list.get(position).getJianzipu())) {

            String url = list.get(position).getJianzipu();
            if (isSelected) {
                url = url.replace("normal", "highlight");
            }
            ImageLoadedrManager.getInstance().display(mContext, url, holder.iv_shoushi, R.drawable.bg_transparent);
            if (list.get(position).getJianziwidth() > 0) {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.iv_shoushi.getLayoutParams();
                params.width = (int) (list.get(position).getJianziwidth() * 2.5);
                params.height = (int) (list.get(position).getJianziheight() * 2.5);
                holder.iv_shoushi.setLayoutParams(params);
            }
        } else {
            holder.iv_shoushi.setImageResource(R.drawable.bg_transparent);
        }

        if (list.get(position).getNumbered_music().equals("-1") || list.get(position).getNumbered_music().equals("8")) {
            TextView textview = new TextView(mContext);
            textview.setText("");
            holder.ll_center.addView(textview);
        }
        if (list.get(position).getNumbered_music().equals("8")) {
            holder.ll_right.addView(ImageUtils.Companion.getImageView(mContext, R.drawable.line_black_5, 0, 0));
        }
        if (list.get(position).getSound_type() == 0) {
            //        中间的数字
            ImageUtils.Companion.getNumber(holder.ll_center, mContext, list.get(position).getNumbered_music(), 1, isSelected);
        } else if (list.get(position).getSound_type() == 1) {
            ImageUtils.Companion.getNumber(holder.ll_center, mContext, list.get(position).getNumbered_music_up(), 2, isSelected);
            ImageUtils.Companion.getNumber(holder.ll_center, mContext, list.get(position).getNumbered_music(), 2, isSelected);
        } else {
            ImageUtils.Companion.getNumber(holder.ll_center, mContext, list.get(position).getNumbered_music_up(), 3, isSelected);
            ImageUtils.Companion.getNumber(holder.ll_center, mContext, list.get(position).getNumbered_music_middle(), 3, isSelected);
            ImageUtils.Companion.getNumber(holder.ll_center, mContext, list.get(position).getNumbered_music(), 3, isSelected);
        }

//下方的
        for (int i = 0; i < list.get(position).getSymbol().size(); i++) {
            MusicDetailBean.Score.Symbol symbol = list.get(position).getSymbol().get(i);
            switch (symbol.getPositioncode()) {
//符号位置
                case 0:
//                        小结顶部
                    switch (symbol.getNamecode()) {
                        case 17:
                            try {
                                String ss[] = new String[2];
                                if (!TextUtils.isEmpty(symbol.getParam()))
                                    ss = symbol.getParam().split("\\.");
                                if (ss.length > 0) {
                                    yanyinSet.put(position, Integer.parseInt(ss[0]));
                                }
                            } catch (Exception e) {
                                LogUtils.e("解析异常", e.getMessage());
                            }
                            break;
                        case 18:
                            try {
                                String ss[] = new String[2];
                                if (!TextUtils.isEmpty(symbol.getParam()))
                                    ss = symbol.getParam().split("\\.");
                                if (ss.length > 0) {
                                    yanyinSetWithNum.put(position, Integer.parseInt(ss[0]));
                                }
                            } catch (Exception e) {
                                LogUtils.e("解析异常", e.getMessage());
                            }
                            break;
                    }

                    break;
                case 1:
//                    左上
                    switch (symbol.getNamecode()) {
                        case 0:
                            holder.ll_left_top.addView(ImageUtils.Companion.getImageView(mContext, R.mipmap.ic_point_black, 0, 0));
                            break;
                    }
                    break;
                case 2:
//                    上
                    switch (symbol.getNamecode()) {
                        case 13:
                            ImageView v3 = new ImageView(mContext);
                            v3.setImageResource(R.mipmap.ic_point_black);
                            holder.ll_center_top.addView(v3);
                            LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) v3.getLayoutParams();
                            params1.topMargin = UIUtil.dip2px(mContext, 2);
                            v3.setLayoutParams(params1);
                            break;
                        case 14:
//泛音
                            ImageView v2 = new ImageView(mContext);
                            v2.setImageResource(R.mipmap.ic_point_virtual);
                            holder.ll_center_top.addView(v2);
                            break;
                        case 15:
//延音
//                                ImageView v1 = new ImageView(mContext);
//                                v1.setImageResource(R.drawable.bg_transparent);
                            ImageView v5 = new ImageView(mContext);
                            v5.setImageResource(R.mipmap.ic_yanyin);
//                                holder.ll_center_top.addView(v1);
                            holder.ll_center_top.addView(v5);
                            LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) v5.getLayoutParams();
                            params2.topMargin = UIUtil.dip2px(mContext, 2);
                            v5.setLayoutParams(params2);
                            break;
                        case 23:
                            ImageView v = new ImageView(mContext);
                            v.setImageResource(R.drawable.line_black_vertical_5);
                            holder.ll_center_top.addView(v);
                            LinearLayout.LayoutParams params3 = (LinearLayout.LayoutParams) v.getLayoutParams();
                            params3.topMargin = UIUtil.dip2px(mContext, 2);
                            v.setLayoutParams(params3);
                            break;
                    }

                    break;
                case 3:
//                    右上
                    break;
                case 4:
//                    右
                    switch (symbol.getNamecode()) {
                        case 6:
                            ImageView v3 = new ImageView(mContext);
                            v3.setImageResource(R.mipmap.ic_point_black);
                            holder.ll_right.addView(v3);
                            LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) v3.getLayoutParams();
                            params1.leftMargin = UIUtil.dip2px(mContext, 2);
                            v3.setLayoutParams(params1);
                            break;
                        case 9:
                            int image = ImageUtils.Companion.getJiePai(symbol.getParam());
                            if (image != 0) {
                                ImageView v1 = new ImageView(mContext);
                                v1.setImageResource(image);
                                holder.ll_right.addView(v1);
                                LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) v1.getLayoutParams();
                                params2.leftMargin = UIUtil.dip2px(mContext, 2);
                                v1.setLayoutParams(params2);
                            }
                            break;

                        case 23:
                            ImageView v = new ImageView(mContext);
                            v.setImageResource(R.drawable.line_black_vertical_20);
                            holder.ll_right.addView(v);
                            LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) v.getLayoutParams();
                            params2.leftMargin = UIUtil.dip2px(mContext, 4);
                            params2.rightMargin = UIUtil.dip2px(mContext, 4);
                            v.setLayoutParams(params2);
                            break;
                        case 24:
                            ImageView v4 = new ImageView(mContext);
                            v4.setImageResource(R.mipmap.ic_line_double);
                            holder.ll_right.addView(v4);
                            LinearLayout.LayoutParams params3 = (LinearLayout.LayoutParams) v4.getLayoutParams();
                            params3.leftMargin = UIUtil.dip2px(mContext, 4);
                            v4.setLayoutParams(params3);
                            break;
                        case 26:
                            ImageView v8 = new ImageView(mContext);
                            v8.setImageResource(R.mipmap.ic_repeat_end);
                            holder.ll_right.addView(v8);
                            LinearLayout.LayoutParams params4 = (LinearLayout.LayoutParams) v8.getLayoutParams();
                            params4.leftMargin = UIUtil.dip2px(mContext, 4);
                            v8.setLayoutParams(params4);
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
                            Boolean hasLineNext = false;
                            ImageView v1;
                            if (position < list.size() - 1) {
                                if (list.get(position + 1).getSymbol().size() > 0) {
                                    //判断下一个音符
                                    for (int j = 0; j < list.get(position + 1).getSymbol().size(); j++) {
                                        if (list.get(position + 1).getSymbol().get(j).getNamecode() == 7) {
                                            hasLineNext = true;
                                            break;
                                        }
                                    }
                                }
                            }

                            if (hasLineNext) {
                                v1 = new ImageView(mContext);
                                v1.setImageResource(R.drawable.line_black_10);
                                holder.ll_right_down.addView(v1);
                                LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) v1.getLayoutParams();
                                params1.topMargin = UIUtil.dip2px(mContext, 2);
                                v1.setLayoutParams(params1);
                            }


                            Boolean hasLinePre = false;
                            ImageView v2;
                            if (position > 0) {
                                if (list.get(position - 1).getSymbol().size() > 0) {
//                                            判断上一个音符
                                    for (int j = 0; j < list.get(position - 1).getSymbol().size(); j++) {
                                        if (list.get(position - 1).getSymbol().get(j).getNamecode() == 7) {
                                            hasLinePre = true;
                                            break;
                                        }
                                    }
                                }
                            }

                            if (hasLinePre) {
                                v2 = new ImageView(mContext);
                                v2.setImageResource(R.drawable.line_black_10);
                                holder.ll_left_down.addView(v2);
                                LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) v2.getLayoutParams();
                                params1.topMargin = UIUtil.dip2px(mContext, 2);
                                v2.setLayoutParams(params1);
                            }

                            ImageView v = new ImageView(mContext);
                            v.setImageResource(R.drawable.line_black_10);
                            holder.ll_center_dowm.addView(v);
                            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) v.getLayoutParams();
                            params.topMargin = UIUtil.dip2px(mContext, 2);
                            v.setLayoutParams(params);

                            break;
                        case 8:
                            ImageView v3 = new ImageView(mContext);
                            v3.setImageResource(R.mipmap.ic_point_black);
                            holder.ll_center_dowm.addView(v3);
                            LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) v3.getLayoutParams();
                            params1.topMargin = UIUtil.dip2px(mContext, 2);
                            v3.setLayoutParams(params1);
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
                            int image = ImageUtils.Companion.getJiePai(symbol.getParam());
                            if (image != 0) {
                                ImageView v = new ImageView(mContext);
                                v.setImageResource(image);
                                holder.ll_left_center.addView(v);
                                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) v.getLayoutParams();
                                params.rightMargin = UIUtil.dip2px(mContext, 2);
                                v.setLayoutParams(params);
                            }

                            break;
                        case 11:
                            ImageView v1 = new ImageView(mContext);
                            v1.setImageResource(R.mipmap.ic_shanghuayin);
                            holder.ll_left_center.addView(v1);
                            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) v1.getLayoutParams();
                            params.rightMargin = UIUtil.dip2px(mContext, 2);
                            v1.setLayoutParams(params);
                            break;
                        case 12:
                            ImageView v2 = new ImageView(mContext);
                            v2.setImageResource(R.mipmap.ic_xiahuaxin);
                            holder.ll_left_center.addView(v2);
                            LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) v2.getLayoutParams();
                            params1.rightMargin = UIUtil.dip2px(mContext, 2);
                            v2.setLayoutParams(params1);
                            break;
                        case 25:
                            ImageView v8 = new ImageView(mContext);
                            v8.setImageResource(R.mipmap.ic_repeat_start);
                            holder.ll_left_center.addView(v8);
                            LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) v8.getLayoutParams();
                            params2.rightMargin = UIUtil.dip2px(mContext, 4);
                            v8.setLayoutParams(params2);
                            break;
                    }
                    break;
            }
        }
        boolean isAdded = false;//是否加载了，加载一次就跳出，减少循环次数
        for (Integer key : yanyinSet.keySet()) {
//            LogUtils.e("key:" + key + "----value:" + yanyinSet.get(key));
//            System.out.println("Key = " + key);
            float value = 10;

            if (position == key) {

                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.ll_limit_left.getLayoutParams();
                params.width = UIUtil.dip2px(mContext, value);
                holder.ll_limit_left.setLayoutParams(params);
                LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) holder.ll_limit_top.getLayoutParams();
                params1.width = UIUtil.dip2px(mContext, value);
                holder.ll_limit_top.setLayoutParams(params1);
                LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) holder.ll_limit_right.getLayoutParams();
                params2.width = UIUtil.dip2px(mContext, value);
                holder.ll_limit_right.setLayoutParams(params2);

                ImageView imageView2 = new ImageView(mContext);
                imageView2.setImageResource(R.drawable.bg_transparent_5);
                holder.ll_limit_left.addView(imageView2);


                ImageView imageView = new ImageView(mContext);
//                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setBackgroundResource(R.mipmap.ic_oval_left_small);
                holder.ll_limit_top.addView(imageView);


                ImageView imageView1 = new ImageView(mContext);
//                imageView1.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView1.setBackgroundResource(R.mipmap.ic_oval_middle_small);
                holder.ll_limit_right.addView(imageView1);
                isAdded = true;
                break;
            } else if (position == (key + yanyinSet.get(key) - 1)) {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.ll_limit_left.getLayoutParams();
                params.width = UIUtil.dip2px(mContext, value);
                holder.ll_limit_left.setLayoutParams(params);
                LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) holder.ll_limit_top.getLayoutParams();
                params1.width = UIUtil.dip2px(mContext, value);
                holder.ll_limit_top.setLayoutParams(params1);
                LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) holder.ll_limit_right.getLayoutParams();
                params2.width = UIUtil.dip2px(mContext, value);
                holder.ll_limit_right.setLayoutParams(params2);

                ImageView imageView1 = new ImageView(mContext);
                imageView1.setBackgroundResource(R.mipmap.ic_oval_middle_small);
                holder.ll_limit_left.addView(imageView1);

                ImageView imageView = new ImageView(mContext);
                imageView.setBackgroundResource(R.mipmap.ic_oval_right_small);
                holder.ll_limit_top.addView(imageView);

                ImageView imageView2 = new ImageView(mContext);
                imageView2.setBackgroundResource(R.drawable.bg_transparent_5);
                holder.ll_limit_right.addView(imageView2);

                isAdded = true;
                break;
            } else if (position > key && position < (key + yanyinSet.get(key) - 1)) {

//
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.ll_limit_left.getLayoutParams();
                params.width = UIUtil.dip2px(mContext, value);
                holder.ll_limit_left.setLayoutParams(params);
                LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) holder.ll_limit_top.getLayoutParams();
                params1.width = UIUtil.dip2px(mContext, value);
                holder.ll_limit_top.setLayoutParams(params1);
                LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) holder.ll_limit_right.getLayoutParams();
                params2.width = UIUtil.dip2px(mContext, value);
                holder.ll_limit_right.setLayoutParams(params2);


                ImageView imageView1 = new ImageView(mContext);
                imageView1.setBackgroundResource(R.mipmap.ic_oval_middle_small);
                holder.ll_limit_left.addView(imageView1);

                ImageView imageView = new ImageView(mContext);
                imageView.setBackgroundResource(R.mipmap.ic_oval_middle_small);
                holder.ll_limit_top.addView(imageView);

                ImageView imageView2 = new ImageView(mContext);
                imageView2.setBackgroundResource(R.mipmap.ic_oval_middle_small);
                holder.ll_limit_right.addView(imageView2);
                isAdded = true;
                break;
            }
            if (isAdded) {
                break;
            }
        }
        if (!isAdded) {
            for (Integer key : yanyinSetWithNum.keySet()) {
//            LogUtils.e("key:" + key + "----value:" + yanyinSet.get(key));
//            System.out.println("Key = " + key);
                float value = 10;
                boolean isAdded1 = false;//是否加载了，加载一次就跳出，减少循环次数

                if (position == key) {

                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.ll_limit_left.getLayoutParams();
                    params.width = UIUtil.dip2px(mContext, value);
                    holder.ll_limit_left.setLayoutParams(params);
                    LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) holder.ll_limit_top.getLayoutParams();
                    params1.width = UIUtil.dip2px(mContext, value);
                    holder.ll_limit_top.setLayoutParams(params1);
                    LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) holder.ll_limit_right.getLayoutParams();
                    params2.width = UIUtil.dip2px(mContext, value);
                    holder.ll_limit_right.setLayoutParams(params2);

                    ImageView imageView2 = new ImageView(mContext);
                    imageView2.setImageResource(R.drawable.bg_transparent_5);
                    holder.ll_limit_left.addView(imageView2);


                    ImageView imageView = new ImageView(mContext);
//                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    imageView.setBackgroundResource(R.mipmap.ic_oval_left_small);
                    holder.ll_limit_top.addView(imageView);


                    ImageView imageView1 = new ImageView(mContext);
//                imageView1.setScaleType(ImageView.ScaleType.FIT_XY);
                    imageView1.setBackgroundResource(R.mipmap.ic_oval_middle_small);
                    holder.ll_limit_right.addView(imageView1);
                    isAdded1 = true;
                    break;
                } else if (position == (key + yanyinSetWithNum.get(key) - 1)) {
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.ll_limit_left.getLayoutParams();
                    params.width = UIUtil.dip2px(mContext, value);
                    holder.ll_limit_left.setLayoutParams(params);
                    LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) holder.ll_limit_top.getLayoutParams();
                    params1.width = UIUtil.dip2px(mContext, value);
                    holder.ll_limit_top.setLayoutParams(params1);
                    LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) holder.ll_limit_right.getLayoutParams();
                    params2.width = UIUtil.dip2px(mContext, value);
                    holder.ll_limit_right.setLayoutParams(params2);

//需要显示数字
                    if (yanyinSetWithNum.get(key) % 2 == 0) {
                        //偶数
                        int needShowNum = key + yanyinSetWithNum.get(key) / 2;
                        if (position == needShowNum) {
                            TextView v = new TextView(mContext);
                            v.setTextSize(UIUtil.sp2px(mContext, 3));
                            v.setText(yanyinSetWithNum.get(key) + "");
                            holder.ll_limit_left.addView(v);
                            LinearLayout.LayoutParams params3 = (LinearLayout.LayoutParams) v.getLayoutParams();
                            params3.topMargin = -UIUtil.dip2px(mContext, 3.5f);
                            v.setLayoutParams(params3);
                        }
                    } else {

                    }

                    ImageView imageView1 = new ImageView(mContext);
                    imageView1.setBackgroundResource(R.mipmap.ic_oval_middle_small);
                    holder.ll_limit_left.addView(imageView1);

                    ImageView imageView = new ImageView(mContext);
                    imageView.setBackgroundResource(R.mipmap.ic_oval_right_small);
                    holder.ll_limit_top.addView(imageView);

                    ImageView imageView2 = new ImageView(mContext);
                    imageView2.setBackgroundResource(R.drawable.bg_transparent_5);
                    holder.ll_limit_right.addView(imageView2);

                    isAdded1 = true;
                    break;
                } else if (position > key && position < (key + yanyinSetWithNum.get(key) - 1)) {

//
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.ll_limit_left.getLayoutParams();
                    params.width = UIUtil.dip2px(mContext, value);
                    holder.ll_limit_left.setLayoutParams(params);
                    LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) holder.ll_limit_top.getLayoutParams();
                    params1.width = UIUtil.dip2px(mContext, value);
                    holder.ll_limit_top.setLayoutParams(params1);
                    LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) holder.ll_limit_right.getLayoutParams();
                    params2.width = UIUtil.dip2px(mContext, value);
                    holder.ll_limit_right.setLayoutParams(params2);

                    boolean isCenterHasNum = false;
                    boolean isLeftHasNum = false;
//需要显示数字
                    if (yanyinSetWithNum.get(key) % 2 == 0) {
                        //偶数
                        int needShowNum = key + yanyinSetWithNum.get(key) / 2;
                        if (position == needShowNum) {
                            TextView v = new TextView(mContext);
                            v.setTextSize(UIUtil.sp2px(mContext, 3));
                            v.setText(yanyinSetWithNum.get(key) + "");
                            holder.ll_limit_left.addView(v);
                            holder.ll_limit_left.setGravity(Gravity.RIGHT);
                            LinearLayout.LayoutParams params3 = (LinearLayout.LayoutParams) v.getLayoutParams();
                            params3.topMargin = -UIUtil.dip2px(mContext, 3.5f);
                            v.setLayoutParams(params3);
                            isLeftHasNum = true;
                        }
                    } else {
                        int needShowNum = key + (yanyinSetWithNum.get(key) - 1) / 2;
                        if (position == needShowNum) {

                            TextView v = new TextView(mContext);
                            v.setTextSize(UIUtil.sp2px(mContext, 3));
                            v.setText(yanyinSetWithNum.get(key) + "");
                            holder.ll_limit_top.addView(v);
                            LinearLayout.LayoutParams params3 = (LinearLayout.LayoutParams) v.getLayoutParams();
                            params3.topMargin = -UIUtil.dip2px(mContext, 3.5f);
                            v.setLayoutParams(params3);
                            isCenterHasNum = true;
                        }
                    }

                    if (!isLeftHasNum) {
                        ImageView imageView1 = new ImageView(mContext);
                        imageView1.setBackgroundResource(R.mipmap.ic_oval_middle_small);
                        holder.ll_limit_left.addView(imageView1);
                    }

                    if (!isCenterHasNum) {
                        ImageView imageView = new ImageView(mContext);
                        imageView.setBackgroundResource(R.mipmap.ic_oval_middle_small);
                        holder.ll_limit_top.addView(imageView);
                    }

                    ImageView imageView2 = new ImageView(mContext);
                    imageView2.setBackgroundResource(R.mipmap.ic_oval_middle_small);
                    holder.ll_limit_right.addView(imageView2);
                    isAdded1 = true;
                    break;
                }
                if (isAdded1) {
                    break;
                }
            }
        }


        if (isSelected) {
            holder.ll_all.setBackgroundResource(R.color.color_99d0a670);
        } else {
            holder.ll_all.setBackgroundResource(R.color.albumTransparent);
        }


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

//是否换行
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
        Collections.sort(selectedIndices);
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

    private void getItemView(int position) {
    }

    static class MainViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {

        final RectangleView colorSquare;
        private final Listener callback;
        private TextView left;
        private TextView right;
        private LinearLayout ll_center;
        private LinearLayout ll_center_dowm;
        private LinearLayout ll_center_top;
        private LinearLayout ll_limit_top;
        private LinearLayout ll_left_center;
        private LinearLayout ll_left_down;
        private LinearLayout ll_right;
        private LinearLayout ll_right_down;
        private LinearLayout ll_limit_left;
        private LinearLayout ll_limit_right;
        private LinearLayout ll_right_content;
        private LinearLayout ll_left_content;
        private LinearLayout ll_center_content;
        private LinearLayout ll_left_top;
        private LinearLayout ll_all;
        private ImageView iv_shoushi;

        MainViewHolder(View itemView, Listener callback) {
            super(itemView);
            this.callback = callback;
            this.colorSquare = itemView.findViewById(R.id.colorSquare);
            this.right = itemView.findViewById(R.id.right);
            this.left = itemView.findViewById(R.id.left);
            this.ll_center = itemView.findViewById(R.id.ll_center);
            this.ll_center_dowm = itemView.findViewById(R.id.ll_center_dowm);
            this.iv_shoushi = itemView.findViewById(R.id.iv_shoushi);
            this.ll_left_center = itemView.findViewById(R.id.ll_left_center);
            this.ll_right = itemView.findViewById(R.id.ll_right);
            this.ll_center_top = itemView.findViewById(R.id.ll_center_top);
            this.ll_right_down = itemView.findViewById(R.id.ll_right_down);
            this.ll_left_down = itemView.findViewById(R.id.ll_left_down);
            this.ll_limit_top = itemView.findViewById(R.id.ll_limit_top);
            this.ll_limit_left = itemView.findViewById(R.id.ll_limit_left);
            this.ll_limit_right = itemView.findViewById(R.id.ll_limit_right);
            this.ll_right_content = itemView.findViewById(R.id.ll_right_content);
            this.ll_left_content = itemView.findViewById(R.id.ll_left_content);
            this.ll_center_content = itemView.findViewById(R.id.ll_center_content);
            this.ll_left_top = itemView.findViewById(R.id.ll_left_top);
            this.ll_all = itemView.findViewById(R.id.ll_all);

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
