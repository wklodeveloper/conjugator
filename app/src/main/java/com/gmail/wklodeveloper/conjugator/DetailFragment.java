package com.gmail.wklodeveloper.conjugator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


public class DetailFragment extends Fragment {
    Record r;
    int[] types = new int[] {R.string.detail_group_1, R.string.detail_group_2, R.string.detail_group_3, R.string.detail_i_adj, R.string.detail_na_adj};
    int[][] labels = {
            {R.string.detail_1_1, R.string.detail_1_2, R.string.detail_1_3, R.string.detail_1_4, R.string.detail_1_5, R.string.detail_1_6, R.string.detail_1_7, R.string.detail_1_8, R.string.detail_1_9, R.string.detail_1_10, R.string.detail_1_11, R.string.detail_1_12},
            {R.string.detail_2_1, R.string.detail_2_2, R.string.detail_2_3, R.string.detail_2_4, R.string.detail_2_5, R.string.detail_2_6, R.string.detail_2_7, R.string.detail_2_8, R.string.detail_2_9},
            {R.string.detail_3_1, R.string.detail_3_2, R.string.detail_3_3, R.string.detail_3_4, R.string.detail_3_5, R.string.detail_3_6, R.string.detail_3_7, R.string.detail_3_8, R.string.detail_3_9}
    };
    ArrayList<Form> forms = new ArrayList<>();
    boolean isMarkAccent = true;

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        if (b != null && b.containsKey("record") && b.containsKey("isMarkAccent")) {
            r = b.getParcelable("record");
            isMarkAccent = b.getBoolean("isMarkAccent");
            String[] fields = r.data.split(";");
            int whichLabel;
            if (r.type == 1 || r.type == 2 || r.type == 3) {
                whichLabel = 0;
            } else if (r.type == 4) {
                whichLabel = 1;
            } else {
                whichLabel = 2;
            }
            for (int i = 1, k = 0; i < fields.length - 1; i = i + 3, k++) {
                if (fields[i].isEmpty())
                    continue;
                Form form = new Form();
                form.name = getString(labels[whichLabel][k]);
                form.kanji = fields[i];
                form.hiragana = fields[i + 1];
                try {
                    JSONArray array = new JSONArray(fields[i + 2]);
                    form.accents = new String[array.length()];
                    for (int j = 0; j < array.length(); j++) {
                        form.accents[j] = array.getString(j);
                    }
                } catch (JSONException e) {}
                form.furigana = Utils.findFurigana(fields, k + 1);
                forms.add(form);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView root = (RecyclerView) inflater.inflate(R.layout.fragment_detail, container, false);
        root.setLayoutManager(new LinearLayoutManager(getContext()));
        FormAdapter adapter = new FormAdapter(r, getString(types[r.type - 1]), forms, isMarkAccent);
        root.setAdapter(adapter);
        return root;
    }
}
