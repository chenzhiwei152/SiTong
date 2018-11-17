package com.sevenstringedzithers.sitong.utils;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.sevenstringedzithers.sitong.R;
import com.sevenstringedzithers.sitong.view.expandable.model.MyChild;
import com.sevenstringedzithers.sitong.view.expandable.model.MyParent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by jhj_Plus on 2016/9/2.
 */
public class AppUtil {
    private static final String TAG = "AppUtil";

    private static Random sRandom = new Random();

    private static List<int[]> parentListRight = new ArrayList<>();
    private static List<String> parentListRightDescribe = new ArrayList<>();
    private static List<Integer> parentListRightAll = new ArrayList<>();


    private static List<int[]> parentListLeft = new ArrayList<>();
    private static List<String> parentListLeftDescribe = new ArrayList<>();
    private static List<Integer> parentListLeftAll = new ArrayList<>();
    public static final int[] TYPE_PARENT = {R.layout.item_parent_1, R.layout.item_parent_2};
    public static final int[] TYPE_CHILD = {R.layout.item_child_1, R.layout.item_child_2};

    public static boolean checkLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    // sRandom.nextInt(16) + 5
    public static ArrayList<MyParent> getListData(int type) {
        if (type==0){
//            右手势

        if (parentListRightAll.size() <= 0) {
            parentListRightAll.add(R.mipmap.ic_right_tuo_normal);
            parentListRightAll.add(R.mipmap.ic_right_tuo_pressed);

            parentListRightAll.add(R.mipmap.ic_right_pi_normal);
            parentListRightAll.add(R.mipmap.ic_right_pi_pressed);

            parentListRightAll.add(R.mipmap.ic_right_tiao2_normal);
            parentListRightAll.add(R.mipmap.ic_right_tiao2_pressed);

            parentListRightAll.add(R.mipmap.ic_right_mo_normal);
            parentListRightAll.add(R.mipmap.ic_right_mo_pressed);

            parentListRightAll.add(R.mipmap.ic_right_ti_normal);
            parentListRightAll.add(R.mipmap.ic_right_ti_pressed);

            parentListRightAll.add(R.mipmap.ic_right_gou_normal);
            parentListRightAll.add(R.mipmap.ic_right_gou_pressed);

            parentListRightAll.add(R.mipmap.ic_right_zhai_normal);
            parentListRightAll.add(R.mipmap.ic_right_zhai_pressed);

            parentListRightAll.add(R.mipmap.ic_right_big_zuo_normal);
            parentListRightAll.add(R.mipmap.ic_right_big_zuo_pressed);

            parentListRightAll.add(R.mipmap.ic_right_fan_zuo_normal);
            parentListRightAll.add(R.mipmap.ic_right_fan_zuo_pressed);

            parentListRightAll.add(R.mipmap.ic_right_small_zuo_normal);
            parentListRightAll.add(R.mipmap.ic_right_small_zuo_pressed);

            parentListRightAll.add(R.mipmap.ic_right_lunzhi_normal);
            parentListRightAll.add(R.mipmap.ic_right_lunzhi_pressed);
        }

        if (parentListRight.size() > 0) {
            parentListRight.clear();
        }
        for (int i = 0; i < parentListRightAll.size(); i += 2) {
            int[] image = {parentListRightAll.get(i), parentListRightAll.get(i + 1)};
            parentListRight.add(image);
        }
            parentListRightDescribe.add("托");
            parentListRightDescribe.add("劈");
            parentListRightDescribe.add("挑");
            parentListRightDescribe.add("抹");
            parentListRightDescribe.add("剔");
            parentListRightDescribe.add("勾");
            parentListRightDescribe.add("摘");
            parentListRightDescribe.add("打");
            parentListRightDescribe.add("大撮");
            parentListRightDescribe.add("反撮");
            parentListRightDescribe.add("小撮");
            parentListRightDescribe.add("轮指");
        ArrayList<MyParent> myParents = new ArrayList<>();
        for (int i = 0; i < parentListRight.size(); i++) {
            MyParent myParent = getParent(type);
            myParent.setImage(parentListRight.get(i));
            myParent.setTitle(parentListRightDescribe.get(i));
            myParents.add(myParent);
        }
            return myParents;
        }else {
//            左手勢
            if (parentListLeftAll.size() <= 0) {
                parentListLeftAll.add(R.mipmap.ic_left_big_zhi_normal);
                parentListLeftAll.add(R.mipmap.ic_left_big_zhi_pressed);

                parentListLeftAll.add(R.mipmap.ic_left_shi_zhi_normal);
                parentListLeftAll.add(R.mipmap.ic_left_shi_zhi_pressed);

                parentListLeftAll.add(R.mipmap.ic_left_middle_normal);
                parentListLeftAll.add(R.mipmap.ic_left_middle_pressed);

                parentListLeftAll.add(R.mipmap.ic_left_wuming_zhi_normal);
                parentListLeftAll.add(R.mipmap.ic_left_wuming_zhi_pressed);

                parentListLeftAll.add(R.mipmap.ic_left_fan_zhi_normal);
                parentListLeftAll.add(R.mipmap.ic_left_fan_zhi_pressed);

                parentListLeftAll.add(R.mipmap.ic_left_gui_zhi_normal);
                parentListLeftAll.add(R.mipmap.ic_left_gui_zhi_pressed);


                parentListLeftAll.add(R.mipmap.ic_left_qia_qi_normal);
                parentListLeftAll.add(R.mipmap.ic_left_qia_qi_pressed);

                parentListLeftAll.add(R.mipmap.ic_left_dai_qi_normal);
                parentListLeftAll.add(R.mipmap.ic_left_dai_qi_pressed);
            }

            if (parentListLeft.size() > 0) {
                parentListLeft.clear();
            }

            for (int i = 0; i < parentListLeftAll.size(); i += 2) {
                int[] image = {parentListLeftAll.get(i), parentListLeftAll.get(i + 1)};
                parentListLeft.add(image);
            }

            parentListLeftDescribe.add("大指按弦");
            parentListLeftDescribe.add("食指");
            parentListLeftDescribe.add("中指");
            parentListLeftDescribe.add("无名指");
            parentListLeftDescribe.add("泛指");
            parentListLeftDescribe.add("跪指");
            parentListLeftDescribe.add("掐起");
            parentListLeftDescribe.add("罨");

            ArrayList<MyParent> myParents = new ArrayList<>();
            for (int i = 0; i < parentListLeft.size(); i++) {
                MyParent myParent = getParent(type);
                myParent.setImage(parentListLeft.get(i));
                myParent.setTitle(parentListLeftDescribe.get(i));
                myParents.add(myParent);
            }
            return myParents;
        }


    }

    @NonNull
    public static MyParent getParent(int type) {
        MyParent myParent = new MyParent();
        myParent.setType(TYPE_PARENT[0]);
        if (type==0){
            ArrayList<MyChild> myChildren = new ArrayList<>();
            MyChild myChild = new MyChild();
            myChild.setType(TYPE_CHILD[0]);
            myChild.setImage(R.mipmap.ic_right_big_zuo_normal);
            myChild.setTitle("测试测试测试");
            myChildren.add(myChild);
            myParent.setMyChildren(myChildren);

        }else {
            ArrayList<MyChild> myChildren = new ArrayList<>();
            MyChild myChild = new MyChild();
            myChild.setType(TYPE_CHILD[0]);
            myChild.setImage(R.mipmap.ic_right_big_zuo_normal);
            myChild.setTitle("测试测试测试");
            myChildren.add(myChild);
            myParent.setMyChildren(myChildren);
        }
        return myParent;
    }

    public static void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

}
