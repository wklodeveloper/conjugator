package com.gmail.wklodeveloper.conjugator;

import android.util.Log;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

public class Utils {
    public static Furigana findFurigana(String[] array, int form) {
        String kanjiForm = array[form * 3 - 2];
        String hiragana = array[form * 3 - 1];
        int begin = -1;
        for (int i = 0; i < kanjiForm.length(); i++) {
            char c = kanjiForm.charAt(i);
            if (begin == -1 && !isHiragana(c)) {
                begin = i;
                break;
            }
        }
        if (begin == -1)
            return null;
        int end = kanjiForm.length();
        for (int i = kanjiForm.length() - 1; i >= 0; i--) {
            char c = kanjiForm.charAt(i);
            if (end == kanjiForm.length() && !isHiragana(c)) {
                end = i + 1;
                break;
            }
        }
        Furigana f = new Furigana();
        String pre = kanjiForm.substring(0, begin);
        String post = kanjiForm.substring(end);
        String replacePre = hiragana.replaceFirst(pre, "");
        f.furigana = replaceLast(replacePre, post, "");
        replacePre = kanjiForm.replaceFirst(pre, "");
        f.furiganaEquivalentKanjiForm = replaceLast(replacePre, post, "");
//        Log.d("Utils", "original string: " + kanjiForm + ", furigana: " + f.furigana + ", pre: " + pre + ", post: " + post);
        f.begin = begin;
        f.end = begin + f.furiganaEquivalentKanjiForm.length();
        return f;
    }

    public static String replaceLast(String text, String regex, String replacement) {
        return text.replaceFirst("(?s)(.*)" + regex, "$1" + replacement);
    }


    public static int findForm(String[] array, String keyword) {
//        Log.d("Utils", "keyword: " + keyword);
        for (int i = 1, j = 1; i < array.length; i = i + 3, j++) {
//            Log.d("Utils",  "array[i]: " + array[i]);
            if (array[i].equals(keyword) || array[i + 1].equals(keyword))
                return j;
        }
        return -1;
    }

    public static boolean isSpecialCase(int id) {
        return id == 2574 || id == 2846;
    }

    public static boolean isHiragana(char c) {
        return (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.HIRAGANA);
    }

    public static boolean isKatakana(char c) {
        return (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.KATAKANA);
    }

    public static void drawFuriganaView(TextView tv, TextView tvFurigana, String s, Furigana f) {
        if (f == null) {
            tvFurigana.setText("");
            return;
        }
        tvFurigana.setText(f.furigana);
        // Find offset from left
        float offsetFromLeft = tv.getPaint().measureText(s.substring(0, f.begin));
        // Find target substring width to mark furigana above
        float targetWidth = tv.getPaint().measureText(s.substring(f.begin, f.end));
        // Find furigana width
        float furiganaWidth = tvFurigana.getPaint().measureText(f.furigana);
        // Center align furigana
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) tvFurigana.getLayoutParams();
        layoutParams.leftMargin = (int) (offsetFromLeft + (targetWidth - furiganaWidth) / 2f);
        tvFurigana.setLayoutParams(layoutParams);
    }
}
