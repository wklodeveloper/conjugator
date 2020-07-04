package com.gmail.wklodeveloper.conjugator;

import android.graphics.Color;
import android.os.Build;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FormAdapter extends RecyclerView.Adapter<FormAdapter.FormViewHolder> {
    Record r;
    String typeStr;
    List<Form> forms;
    boolean showAccent;

    public FormAdapter(Record r, String typeStr, List<Form> forms, boolean showAccent) {
        this.r = r;
        this.typeStr = typeStr;
        this.forms = forms;
        this.showAccent = showAccent;
    }

    @NonNull
    @Override
    public FormViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ConstraintLayout v = (ConstraintLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_form, parent, false);
        return new FormViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final FormViewHolder holder, int position) {
        holder.rightPanel.removeAllViews();
        if (position == 0) {
            RecyclerView.LayoutParams rootParams = (RecyclerView.LayoutParams) holder.root.getLayoutParams();
            rootParams.topMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, holder.rightPanel.getContext().getResources().getDisplayMetrics());
            holder.root.setLayoutParams(rootParams);
            holder.tvForm.setText(r.origin);
            holder.tvForm.setVisibility(View.VISIBLE);
            holder.tvType.setText(typeStr);
            holder.tvType.setVisibility(View.VISIBLE);
            holder.leftPanel.setVisibility(View.GONE);
            holder.rightPanel.setVisibility(View.GONE);
            holder.tvRightFurigana.setVisibility(View.GONE);
            holder.tvLeft.setVisibility(View.GONE);
            holder.tvRight.setVisibility(View.GONE);
        } else {
            holder.tvType.setVisibility(View.GONE);
            RecyclerView.LayoutParams rootParams = (RecyclerView.LayoutParams) holder.root.getLayoutParams();
            rootParams.topMargin = 0;
            holder.root.setLayoutParams(rootParams);
            Form form = forms.get(position - 1);
            if (showAccent) {
                holder.tvForm.setVisibility(View.VISIBLE);
                holder.leftPanel.setVisibility(View.VISIBLE);
                holder.rightPanel.setVisibility(View.VISIBLE);
                holder.tvLeft.setVisibility(View.GONE);
                holder.tvRight.setVisibility(View.GONE);
                holder.tvRightFurigana.setVisibility(View.GONE);
                holder.rightPanel.setVisibility(View.VISIBLE);
                holder.tvForm.setText(form.name);
                holder.tvKanji.setText(form.kanji);
                Utils.drawFuriganaView(holder.tvKanji, holder.tvFurigana, form.kanji, form.furigana);
                for (int i = 0; i < form.accents.length; i++) {
                    OverLineTextView tv = new OverLineTextView(holder.rightPanel.getContext());
                    tv.setText(form.hiragana);
                    tv.setTextSize(20);
                    tv.setTextColor(Color.BLACK);
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
                        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    else
                        tv.setGravity(Gravity.CENTER);
                    tv.start = form.accents[i].indexOf("(");
                    int end = form.accents[i].indexOf(")");
                    tv.end = end == -1 ? form.accents[i].length() : end - 1;
                    holder.rightPanel.addView(tv);
                }
            } else {
                holder.tvForm.setVisibility(View.GONE);
                holder.leftPanel.setVisibility(View.GONE);
                holder.rightPanel.setVisibility(View.GONE);
                holder.tvLeft.setVisibility(View.VISIBLE);
                holder.tvRight.setVisibility(View.VISIBLE);
                holder.tvRightFurigana.setVisibility(View.VISIBLE);
                holder.tvLeft.setText(form.name);
                holder.tvRight.setText(form.kanji);
                Utils.drawFuriganaView(holder.tvRight, holder.tvRightFurigana, form.kanji, form.furigana);
            }
        }
    }

    @Override
    public int getItemCount() {
        return forms.size() + 1;
    }

    public static class FormViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout root;
        ConstraintLayout leftPanel;
        LinearLayout rightPanel;
        TextView tvForm;
        TextView tvType;
        TextView tvKanji;
        TextView tvFurigana;
        TextView tvLeft;
        TextView tvRight;
        TextView tvRightFurigana;

        public FormViewHolder(ConstraintLayout root) {
            super(root);
            this.root = root;
            leftPanel = root.findViewById(R.id.left_panel);
            rightPanel = root.findViewById(R.id.right_panel);
            tvForm = root.findViewById(R.id.tv_form);
            tvType = root.findViewById(R.id.tv_type);
            tvKanji = root.findViewById(R.id.tv_kanji);
            tvFurigana = root.findViewById(R.id.tv_furigana);
            tvLeft = root.findViewById(R.id.tv_left);
            tvRight = root.findViewById(R.id.tv_right);
            tvRightFurigana = root.findViewById(R.id.tv_right_furigana);
        }
    }
}
