package com.gmail.wklodeveloper.conjugator;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Record implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    int id;

    @ColumnInfo
    String origin;

    @ColumnInfo
    int type;

    @ColumnInfo
    String data;

    @Ignore
    boolean split;

    @Ignore
    String left;

    @Ignore
    String right;

    @Ignore
    Furigana leftFurigana;

    @Ignore
    Furigana rightFurigana;

    public Record() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isSplit() {
        return split;
    }

    public void setSplit(boolean split) {
        this.split = split;
    }

    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public String getRight() {
        return right;
    }

    public void setRight(String right) {
        this.right = right;
    }

    public Furigana getLeftFurigana() {
        return leftFurigana;
    }

    public void setLeftFurigana(Furigana leftFurigana) {
        this.leftFurigana = leftFurigana;
    }

    public Furigana getRightFurigana() {
        return rightFurigana;
    }

    public void setRightFurigana(Furigana rightFurigana) {
        this.rightFurigana = rightFurigana;
    }

    public Record(Parcel p) {
        id = p.readInt();
        origin = p.readString();
        type = p.readInt();
        data = p.readString();
        split = p.readByte() != 0;
        left = p.readString();
        right = p.readString();
        leftFurigana = p.readParcelable(Furigana.class.getClassLoader());
        rightFurigana = p.readParcelable(Furigana.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(origin);
        dest.writeInt(type);
        dest.writeString(data);
        dest.writeByte((byte) (split ? 1 : 0));
        dest.writeString(left);
        dest.writeString(right);
        dest.writeParcelable(leftFurigana, flags);
        dest.writeParcelable(rightFurigana, flags);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        @Override
        public Record createFromParcel(Parcel in) {
            return new Record(in);
        }

        @Override
        public Object[] newArray(int size) {
            return new Record[size];
        }
    };
}
