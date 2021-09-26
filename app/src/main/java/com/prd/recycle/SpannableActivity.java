package com.prd.recycle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

public class SpannableActivity extends AppCompatActivity {

    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spannable);
        SpannableString span = new SpannableString("我已阅读并同意《隐私政策》和《用户协议》");
        span.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                enterWebActivity("", "https://h5.yqhuan.com/#/pages/about/about?type=3");
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#5693e2"));
                ds.setUnderlineText(false);
            }
        }, 7, 13, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                enterWebActivity("", "https://h5.yqhuan.com/#/pages/about/about?type=1");
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#5693e2"));
                ds.setUnderlineText(false);
            }
        }, 14, 20, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        TextView textView = findViewById(R.id.tvs);
        textView.setText(span);
        textView.setMovementMethod(LinkMovementMethod.getInstance());

        button = findViewById(R.id.button);
        CheckBox checkBox = findViewById(R.id.cb);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                button.setEnabled(isChecked);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void enterWebActivity(String param, String url) {
        Intent intent = new Intent(SpannableActivity.this,
                WebActivity.class);
        intent.setAction(url);
        startActivity(intent);
        if ("".equals(param)) {
            SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
            sp.edit().putBoolean(param, true).commit();
        }
    }

}