package com.gmail.wklodeveloper.conjugator;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    MotionLayout root;
    TextView tvTitle;
    TextView tvDescription;
    AppCompatEditText etSearch;
    RecyclerView rvResult;
    ResultAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ProgressBar loading;
    TextView tvEmptyStr;
    ImageView vDismiss;
    TextView tvSwitchLabel;
    SwitchCompat accentSwitch;
    boolean isEtSearchMoveUp = false;
    View.OnClickListener dismissListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (page == 1) {
                moveDown();
            } else {
                page = 1;
                root.setTransition(R.id.detail_to_end);
                root.transitionToEnd();
                getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(0, R.anim.exit_to_right)
                        .remove(detailFragment)
                        .commit();
            }
        }
    };
    String lastKeyword;
    int page = 1;
    DetailFragment detailFragment;
    AppDatabase db;
    Thread thread;
    boolean isTouched = false;
    boolean isMarkAccent = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        root = findViewById(R.id.root);
        tvTitle = findViewById(R.id.tv_title);
        tvDescription = findViewById(R.id.tv_description);
        etSearch = findViewById(R.id.et_search);
        etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showKeyboard(v);
                    etSearch.setHint("");
                } else {
                    hideKeyboard(v);
                    doSearch();
                }
            }
        });
        etSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                    doSearch();
                    return true;
                }
                return false;
            }
        });
        rvResult = findViewById(R.id.rv_result);
        layoutManager = new LinearLayoutManager(this);
        rvResult.setLayoutManager(layoutManager);
        adapter = new ResultAdapter();
        adapter.gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
        adapter.onClick = new OnResultClick() {
            @Override
            public void click(Record r) {
                goToDetails(r);
            }
        };
        rvResult.setAdapter(adapter);
        vDismiss = findViewById(R.id.v_dismiss);
        tvSwitchLabel = findViewById(R.id.tv_switch_label);
        accentSwitch = findViewById(R.id.accent_switch);
        final SharedPreferences preferences = getApplicationContext().getSharedPreferences("com.gmail.wklodeveloper.conjugator", android.content.Context.MODE_PRIVATE);
        isMarkAccent = preferences.getBoolean("markAccent", true);
        accentSwitch.setChecked(isMarkAccent);
        accentSwitch.jumpDrawablesToCurrentState();
        accentSwitch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isTouched = true;
                return false;
            }
        });
        accentSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isTouched) {
                    isTouched = false;
                    isMarkAccent = isChecked;
                    preferences.edit().putBoolean("markAccent", isChecked).apply();
                }
            }
        });
        loading = findViewById(R.id.loading);
        tvEmptyStr = findViewById(R.id.empty_str);
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "conjugator.db")
                .createFromAsset("conjugator.db")
                .fallbackToDestructiveMigration()
                .build();
    }

    @Override
    protected void onDestroy() {
        if (thread != null)
            thread.interrupt();
        super.onDestroy();
    }

    void doSearch() {
        etSearch.clearFocus();
        final String keyword = etSearch.getText().toString().replaceAll("[\\p{Punct}a-zA-Z0-9 　]", "");
        etSearch.setText(keyword);
        if (keyword.isEmpty()) {
            etSearch.setHint(getResources().getString(R.string.main_hint));
            moveDown();
            return;
        }
        if (lastKeyword != null && lastKeyword.equals(keyword)) {
            return;
        }
        lastKeyword = keyword;
        moveUp();
        tvEmptyStr.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
        if (thread != null)
            thread.interrupt();
        thread = new Thread() {
            @Override
            public void run() {
                final List<Record> records;
                if (keyword.substring(keyword.length() - 1).equals("な")) {
                    records = db.recordDao().findByKeyword(keyword.replace("な", "[な]"));
                } else {
                    records = db.recordDao().findByKeyword(keyword);
                }
                for (Record r : records) {
                    int i = r.origin.indexOf("・");
                    if (i == -1)
                        continue;
                    String originalStr = r.origin.substring(0, i);
                    String[] array = r.data.split(";");
                    r.left = originalStr;
                    r.leftFurigana = Utils.findFurigana(array, 1);
                    String sqBracketsRemoved = originalStr.replaceAll("\\[|\\]", "");
                    String secondForm = r.origin.substring(i + 1);
//                    Log.d("MainActivity", "secondForm: " + secondForm);
                    if (sqBracketsRemoved.contains(keyword)) {
                        r.split = false;
                    } else if (secondForm.contains(keyword)) {
                        r.split = true;
                        r.right = secondForm;
                        r.rightFurigana = Utils.findFurigana(array, 2);
                    } else {
                        int form = Utils.findForm(array, keyword);
                        if (form == 1) {
                            r.split = false;
                        } else {
                            r.split = true;
                            r.right = array[form * 3 - 2];
                            r.rightFurigana = Utils.findFurigana(array, form);
                        }
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loading.setVisibility(View.GONE);
                        adapter.data = records;
                        adapter.notifyDataSetChanged();
                        if (records.size() == 1) {
                            // Go to details
                            goToDetails(records.get(0));
                        } if (records.size() == 0) {
                            // Show empty str
                            tvEmptyStr.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        };
        thread.start();
    }

    void moveUp() {
        if (isEtSearchMoveUp)
            return;
        vDismiss.setOnClickListener(dismissListener);
        accentSwitch.setEnabled(false);
        accentSwitch.jumpDrawablesToCurrentState();
        root.setTransition(R.id.start_to_end);
        root.transitionToEnd();
        isEtSearchMoveUp = true;
    }

    void moveDown() {
        if (!isEtSearchMoveUp)
            return;
        lastKeyword = "";
        etSearch.setText("");
        etSearch.setHint(getResources().getString(R.string.main_hint));
        adapter.data.clear();
        adapter.notifyDataSetChanged();
        vDismiss.setOnClickListener(null);
        accentSwitch.setEnabled(true);
        accentSwitch.jumpDrawablesToCurrentState();
        root.setTransition(R.id.end_to_start);
        root.transitionToEnd();
        isEtSearchMoveUp = false;
    }

    void goToDetails(Record r) {
        if (page == 1) {
            page = 2;
            root.setTransition(R.id.end_to_detail);
            root.transitionToEnd();
            detailFragment = new DetailFragment();
            Bundle b = new Bundle();
            b.putParcelable("record", r);
            b.putBoolean("isMarkAccent", isMarkAccent);
            detailFragment.setArguments(b);
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, 0)
                    .add(R.id.container, detailFragment)
                    .commit();
        }
    }

    void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager)   getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
