package com.prd.recycle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private ViewPager vp;
    private GuideViewPagerAdapter adapter;
    private List<View> views;
    private Button startBtn;
    private boolean choosen = false;

    // 引导页图片资源
    private static final int[] pics = { R.layout.guid_view1,
            R.layout.guid_view2, R.layout.guid_view3, R.layout.guid_view4 };

    // 底部小点图片
    private ImageView[] dots;

    // 记录当前选中位置
    private int currentIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_main);
        SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
        if (sp.getBoolean("FIRST_OPEN", false)) {
            if (!sp.getBoolean("PROTOCOL", false)) {
                Intent intent = new Intent(MainActivity.this, SpannableActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim1, R.anim.anim2);
            } else {
                enterWebActivity("", "https://h5.yqhuan.com/");
            }
        }

        startBtn = findViewById(R.id.btn_enter);
        startBtn.setOnClickListener(this);
        views = new ArrayList<View>();

        // 初始化引导页视图列表
        for (int i = 0; i < pics.length; i++) {
            View view = LayoutInflater.from(this).inflate(pics[i], null);
            views.add(view);
        }

        vp = (ViewPager) findViewById(R.id.vp_guide);
        // 初始化adapter
        adapter = new GuideViewPagerAdapter(views);
        vp.setAdapter(adapter);
        vp.setOnPageChangeListener(new PageChangeListener());

        initDots();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 如果切换到后台，就设置下次不进入功能引导页
        SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
        sp.edit().putBoolean("FIRST_OPEN", true).commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initDots() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
        dots = new ImageView[pics.length];

        // 循环取得小点图片
        for (int i = 0; i < pics.length; i++) {
            // 得到一个LinearLayout下面的每一个子元素
            dots[i] = (ImageView) ll.getChildAt(i);
            dots[i].setEnabled(false);// 都设为灰色
            dots[i].setOnClickListener(this);
            dots[i].setTag(i);// 设置位置tag，方便取出与当前位置对应
        }

        currentIndex = 0;
        dots[currentIndex].setEnabled(true); // 设置为白色，即选中状态

    }

    /**
     * 设置当前view
     *
     * @param position
     */
    private void setCurView(int position) {
        if (position < 0 || position >= pics.length) {
            return;
        }
        vp.setCurrentItem(position);
    }

    /**
     * 设置当前指示点
     *
     * @param position
     */
    private void setCurDot(int position) {
        if (position < 0 || position > pics.length || currentIndex == position) {
            return;
        } else if (position == pics.length-1) {
            startBtn.setVisibility(View.VISIBLE);
        }
        dots[position].setEnabled(true);
        dots[currentIndex].setEnabled(false);
        currentIndex = position;
    }

    @Override
    public void onClick(View v) {
        Log.i(TAG, "onClick: ");
        if (v.getId() == R.id.btn_enter) {
            enterWebActivity("FIRST_OPEN", "https://h5.yqhuan.com/");
            return;
        }

        int position = (Integer) v.getTag();
        setCurView(position);
        setCurDot(position);
    }


    private void enterWebActivity(String param, String url) {
        Intent intent = new Intent(MainActivity.this,
                WebActivity.class);
        intent.setAction(url);
        startActivity(intent);
        if ("".equals(param)) {
            SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
            sp.edit().putBoolean(param, true).commit();
        }
        finish();
    }

    private class PageChangeListener implements ViewPager.OnPageChangeListener {
        // 当滑动状态改变时调用
        @Override
        public void onPageScrollStateChanged(int position) {
            // arg0 ==1的时辰默示正在滑动，arg0==2的时辰默示滑动完毕了，arg0==0的时辰默示什么都没做。

        }

        // 当前页面被滑动时调用
        @Override
        public void onPageScrolled(int position, float arg1, int arg2) {
            // arg0 :当前页面，及你点击滑动的页面
            // arg1:当前页面偏移的百分比
            // arg2:当前页面偏移的像素位置

        }

        // 当新的页面被选中时调用
        @Override
        public void onPageSelected(int position) {
            // 设置底部小点选中状态
            setCurDot(position);
        }

    }
    private MyDialog myDialog;
    private void showDialog(Context context){
        myDialog=new MyDialog(context,R.style.MyDialog);
//        myDialog.setTitle("《隐私政策》和《用户协议》");
//        myDialog.setMessage("欢迎使用通通回收。在您访问通通回收、使用通通回收服务前，您需要通过点击同意的方式在线签署相关协议。请您务必仔细阅限读、充分理解协议的条款内容后再点击同意(特别是以加粗或下划线方式标注的条款，因为这些条款可能会明确您应履行的义务或对您的权利有所限制)。");
        myDialog.setYesOnclickListener("确定", new MyDialog.onYesOnclickListener() {
            @Override
            public void onYesOnclick() {
                SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
                sp.edit().putBoolean("PROTOCOL", true).commit();
                myDialog.dismiss();
            }
        });
//        myDialog.setNoOnclickListener("取消", new MyDialog.onNoOnclickListener() {
//            @Override
//            public void onNoClick() {
//                myDialog.dismiss();
//                finish();
//            }
//        });
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
        }, 8, 13, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
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
        }, 15, 20, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        myDialog.setSpannableString(span);
        myDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: ");
                if (v.getId() == R.id.cb) {
                    choosen = !choosen;
                    myDialog.setButtonEnbale(choosen);
                }
            }
        });
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.setCancelable(false);
        myDialog.show();
        Log.i(TAG, "showDialog: ");
    }
}