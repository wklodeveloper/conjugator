package com.gmail.wklodeveloper.conjugator;

import android.os.Parcel;
import android.os.Parcelable;

public class Furigana implements Parcelable {
    String furigana;
    String furiganaEquivalentKanjiForm;
    int begin; // index of string to be added with furigana
    int end;

    public Furigana() {}

    public String getFurigana() {
        return furigana;
    }

    public void setFurigana(String furigana) {
        this.furigana = furigana;
    }

    public String getFuriganaEquivalentKanjiForm() {
        return furiganaEquivalentKanjiForm;
    }

    public void setFuriganaEquivalentKanjiForm(String furiganaEquivalentKanjiForm) {
        this.furiganaEquivalentKanjiForm = furiganaEquivalentKanjiForm;
    }

    public int getBegin() {
        return begin;
    }

    public void setBegin(int begin) {
        this.begin = begin;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public Furigana(Parcel p) {
        furigana = p.readString();
        furiganaEquivalentKanjiForm = p.readString();
        begin = p.readInt();
        end = p.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(furigana);
        dest.writeString(furiganaEquivalentKanjiForm);
        dest.writeInt(begin);
        dest.writeInt(end);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        @Override
        public Furigana createFromParcel(Parcel in) {
            return new Furigana(in);
        }

        @Override
        public Object[] newArray(int size) {
            return new Furigana[size];
        }
    };
}
