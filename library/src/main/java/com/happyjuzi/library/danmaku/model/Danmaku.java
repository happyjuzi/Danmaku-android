package com.happyjuzi.library.danmaku.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tangh on 16/3/24.
 */
public class Danmaku implements Parcelable {

    public int id;//弹幕id
    public String avatar;//弹幕头像
    public String content;//弹幕内容

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.avatar);
        dest.writeString(this.content);
    }

    public Danmaku() {
    }

    protected Danmaku(Parcel in) {
        this.id = in.readInt();
        this.avatar = in.readString();
        this.content = in.readString();
    }

    public static final Parcelable.Creator<Danmaku> CREATOR = new Parcelable.Creator<Danmaku>() {
        public Danmaku createFromParcel(Parcel source) {
            return new Danmaku(source);
        }

        public Danmaku[] newArray(int size) {
            return new Danmaku[size];
        }
    };
}
