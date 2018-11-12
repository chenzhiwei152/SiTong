package com.sitong.changqin.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class MemberMusciChild implements Parcelable {
    private String name;
    private String id;
    private int type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.id);
        dest.writeInt(this.type);
    }

    public MemberMusciChild() {
    }

    protected MemberMusciChild(Parcel in) {
        this.name = in.readString();
        this.id = in.readString();
        this.type = in.readInt();
    }

    public static final Parcelable.Creator<MemberMusciChild> CREATOR = new Parcelable.Creator<MemberMusciChild>() {
        @Override
        public MemberMusciChild createFromParcel(Parcel source) {
            return new MemberMusciChild(source);
        }

        @Override
        public MemberMusciChild[] newArray(int size) {
            return new MemberMusciChild[size];
        }
    };
}
