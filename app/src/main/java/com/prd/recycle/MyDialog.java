package com.prd.recycle;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;

/**
 * 创建自定义的Dialog，主要学习实现原理
 * Created by admin on 2017/8/30.
 */

public class MyDialog extends Dialog {
    private static final String TAG = "MyDialog";
    private Button yes;//确定按钮
//    private Button no;//取消按钮
    private TextView titleTV;//消息标题文本
    private TextView message;//消息提示文本
    private ImageView imageView;//
    private TextView protocol;//
    private String titleStr;//从外界设置的title文本
    private String messageStr;//从外界设置的消息文本
    //确定文本和取消文本的显示的内容
    private String yesStr, noStr;
    private onNoOnclickListener noOnclickListener;//取消按钮被点击了的监听器
    private onYesOnclickListener yesOnclickListener;//确定按钮被点击了的监听器

    public MyDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        Log.i(TAG, "MyDialog: ");

        //初始化界面控件
        initView(context);
    }

    /**
     * 设置取消按钮的显示内容和监听
     *
     * @param str
     * @param onNoOnclickListener
     */
    public void setNoOnclickListener(String str, onNoOnclickListener onNoOnclickListener) {
        if (str != null) {
            noStr = str;
        }
        this.noOnclickListener = onNoOnclickListener;
    }

    public void setOnClickListener(View.OnClickListener listener) {
        Log.i(TAG, "setCheckBoxListener: ");
        imageView.setOnClickListener(listener);
    }

    /**
     * 设置确定按钮的显示内容和监听
     *
     * @param str
     * @param yesOnclickListener
     */
    public void setYesOnclickListener(String str, onYesOnclickListener yesOnclickListener) {
        if (str != null) {
            yesStr = str;
        }
        this.yesOnclickListener = yesOnclickListener;
    }

    public void setSpannableString (SpannableString spannableString) {
        Log.i(TAG, "setSpannableString: " + spannableString.toString());
        protocol.setText(spannableString);
        protocol.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void setButtonEnbale(boolean enable) {
        Log.i(TAG, "setButtonEnbale: " + enable);
        yes.setEnabled(enable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog);
    }

    /**
     * 初始化界面控件
     */
    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog, null, false);
        yes = view.findViewById(R.id.yes);
        titleTV = (TextView) view.findViewById(R.id.title);
        message = (TextView) view.findViewById(R.id.message);
        imageView = view.findViewById(R.id.cb);
        protocol = view.findViewById(R.id.protocol);
    }

    /**
     * 从外界Activity为Dialog设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        titleStr = title;
    }

    /**
     * 从外界Activity为Dialog设置message
     *
     * @param message
     */
    public void setMessage(String message) {
        messageStr = message;
    }

    public interface onNoOnclickListener {
        public void onNoClick();
    }

    public interface onYesOnclickListener {
        public void onYesOnclick();
    }
}
