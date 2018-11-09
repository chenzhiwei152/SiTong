package com.sitong.changqin.utils;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.sitong.changqin.view.expandable.model.MyChild;
import com.sitong.changqin.view.expandable.model.MyParent;
import com.stringedzithers.sitong.R;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by jhj_Plus on 2016/9/2.
 */
public class AppUtil {
    private static final String TAG = "AppUtil";

    private static Random sRandom = new Random();

    public static final int[] TYPE_PARENT = {R.layout.item_parent_1, R.layout.item_parent_2};
    public static final int[] TYPE_CHILD = {R.layout.item_child_1, R.layout.item_child_2};

    public static boolean checkLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    // sRandom.nextInt(16) + 5
    public static ArrayList<MyParent> getListData() {
        ArrayList<MyParent> myParents = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            MyParent myParent = getParent();
            myParents.add(myParent);
        }
        return myParents;
    }

    @NonNull
    public static MyParent getParent() {
        MyParent myParent = new MyParent();
        myParent.setType(TYPE_PARENT[sRandom.nextInt(2)]);
        if (sRandom.nextBoolean()) {
            ArrayList<MyChild> myChildren = new ArrayList<>();
            for (int j = 0; j < sRandom.nextInt(6); j++) {
                MyChild myChild = new MyChild();
                myChild.setType(TYPE_CHILD[sRandom.nextInt(2)]);
                myChildren.add(myChild);
            }
            myParent.setMyChildren(myChildren);
        }
        return myParent;
    }

    public static void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

}
