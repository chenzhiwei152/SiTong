package com.sevenstringedzithers.sitong.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.github.huajianjiang.expandablerecyclerview.widget.Parent;

import java.util.List;

public class MemberMusciParent implements Parent<MemberMusciChild>, Parcelable {
    private List<MemberMusciChild> childList;
    private String levelName;
    private int type;
    private boolean isExpandable = true;
    private boolean isInitiallyExpanded = false;
    public MemberMusciParent() {
    }

    public MemberMusciParent(List<MemberMusciChild> childList, String levelName, int type) {
        this.childList = childList;
        this.levelName = levelName;
        this.type = type;
    }

    public List<MemberMusciChild> getChildList() {
        return childList;
    }

    public void setChildList(List<MemberMusciChild> childList) {
        this.childList = childList;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    protected MemberMusciParent(Parcel in) {
        type = in.readInt();
        levelName = in.readString();
        childList = in.createTypedArrayList(MemberMusciChild.CREATOR);

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(type);
        dest.writeString(levelName);
        dest.writeTypedList(childList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MemberMusciParent> CREATOR = new Creator<MemberMusciParent>() {
        @Override
        public MemberMusciParent createFromParcel(Parcel in) {
            return new MemberMusciParent(in);
        }

        @Override
        public MemberMusciParent[] newArray(int size) {
            return new MemberMusciParent[size];
        }
    };

    @Override
    public List<MemberMusciChild> getChildren() {
        return childList;
    }

    @Override
    public boolean isInitiallyExpandable() {
        return isExpandable;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return isInitiallyExpanded;
    }
}
