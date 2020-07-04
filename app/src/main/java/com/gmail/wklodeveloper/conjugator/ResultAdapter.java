package com.gmail.wklodeveloper.conjugator;

import android.graphics.Color;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ResultViewHolder> {
    List<Record> data = new ArrayList<>();
    OnResultClick onClick;
    GestureDetector gestureDetector;

    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ConstraintLayout v = (ConstraintLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_result, parent, false);
        return new ResultViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder holder, final int position) {
        final Record r = data.get(position);
        holder.root.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (gestureDetector.onTouchEvent(event)) {
                    onClick.click(r);
                }
                int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundColor(Color.parseColor("#ffdddddd"));
                    return true;
                }
                if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
                    v.setBackgroundColor(Color.parseColor("#00ffffff"));
                    return true;
                }
                return false;
            }
        });

        if (r.split) {
            holder.tvLeft.setVisibility(View.VISIBLE);
            holder.tvLeftFurigana.setVisibility(View.VISIBLE);
            holder.tvArrow.setVisibility(View.VISIBLE);
            holder.tvRight.setVisibility(View.VISIBLE);
            holder.tvRightFurigana.setVisibility(View.VISIBLE);
            holder.tvCenter.setVisibility(View.GONE);
            holder.tvCenterFurigana.setVisibility(View.GONE);
            holder.tvLeft.setText(r.left);
            Utils.drawFuriganaView(holder.tvLeft, holder.tvLeftFurigana, r.left, r.leftFurigana);
            holder.tvRight.setText(r.right);
            Utils.drawFuriganaView(holder.tvRight, holder.tvRightFurigana, r.right, r.rightFurigana);
        } else {
            holder.tvLeft.setVisibility(View.GONE);
            holder.tvLeftFurigana.setVisibility(View.GONE);
            holder.tvArrow.setVisibility(View.GONE);
            holder.tvRight.setVisibility(View.GONE);
            holder.tvRightFurigana.setVisibility(View.GONE);
            holder.tvCenter.setVisibility(View.VISIBLE);
            holder.tvCenterFurigana.setVisibility(View.VISIBLE);
            holder.tvCenter.setText(r.left);
            Utils.drawFuriganaView(holder.tvCenter, holder.tvCenterFurigana, r.left, r.leftFurigana);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ResultViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout root;
        TextView tvLeft;
        TextView tvRight;
        TextView tvLeftFurigana;
        TextView tvRightFurigana;
        TextView tvArrow;
        TextView tvCenter;
        TextView tvCenterFurigana;

        public ResultViewHolder(ConstraintLayout v) {
            super(v);
            root = v;
            tvLeft = root.findViewById(R.id.tv_left);
            tvRight = root.findViewById(R.id.tv_right);
            tvLeftFurigana = root.findViewById(R.id.tv_left_furigana);
            tvRightFurigana = root.findViewById(R.id.tv_right_furigana);
            tvArrow = root.findViewById(R.id.arrow);
            tvCenter = root.findViewById(R.id.tv_center);
            tvCenterFurigana = root.findViewById(R.id.tv_center_furigana);
        }
    }
}
