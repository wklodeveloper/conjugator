package com.gmail.wklodeveloper.conjugator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.TypedValue;

import androidx.appcompat.widget.AppCompatTextView;


public class OverLineTextView extends AppCompatTextView {

    Paint paint;
    int start = -1; // 0 to length - 1
    int end = -1; // 1 to length

    public OverLineTextView(Context context) {
        super(context);
        init();
    }

    public OverLineTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public OverLineTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics()));
        paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Layout layout = getLayout();
        if (start == -1 || end == -1 || start >= end)
            return;
        int last = Math.min(end, getText().length());
        boolean isEndLast = last == getText().length();
        for (int offset = start; offset < last; offset++) {
            float startX = layout.getPrimaryHorizontal(offset);
            float startY = 0;
            float endX = startX + super.getPaint().measureText(getText().subSequence(offset, offset + 1).toString());
            canvas.drawLine(startX, startY, endX, startY, paint);
            if (offset == last - 1 && !isEndLast) {
                canvas.drawLine(
                        endX,
                        startY,
                        endX,
                        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics()),
                        paint);
            }
        }
    }
}
