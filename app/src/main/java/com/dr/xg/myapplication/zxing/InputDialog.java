package com.dr.xg.myapplication.zxing;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dr.xg.myapplication.R;


public class InputDialog extends Dialog implements OnClickListener {

    public interface OnClickListener {
        public void onConfirm(String content);
        public void onCancel();
    }

    private Context context;
    private TextView tv_title;
    private EditText et_content;
    private TextView mInfoTv;
    private Button btn_cancel, btn_ok;
    private String mTitle, mHint,mInfo;
    private String mbtnCancletext="", mbtnOktext="";
    private int iMaxLen = Integer.MAX_VALUE;
    private int icontentType = -1;
    private boolean bEmoji = false;

    private OnClickListener mListener;

    public InputDialog(Context context) {
        super(context, R.style.AlertDialogStyle);
        this.context = context;
        this.mHint = "";
        this.mTitle = "";
        this.mInfo = "";
    }

    public InputDialog(Context context, String title, String info, String message) {
        this(context, title,info, message, null);
    }

    public InputDialog(Context context, String title, String info, String hint, OnClickListener listener) {
        super(context, R.style.AlertDialogStyle);
        this.context = context;
        this.mTitle = title;
        this.mHint = hint;
        this.mListener = listener;
        this.mInfo = info;
    }

    @Override
    public void onClick(View v) {
        dismiss();
        switch (v.getId()) {
            case R.id.btn_dialog_confirm:
                if (mListener == null) return;
                String content = et_content.getText().toString();
                mListener.onConfirm(content);
                break;
            case R.id.btn_dialog_cancle:
                if (mListener == null) return;
                mListener.onCancel();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dialog_input);

        setCanceledOnTouchOutside(false);// Touch Other Dismiss

        tv_title = (TextView) findViewById(R.id.tv_dialog_title);
        et_content = (EditText) findViewById(R.id.et_dialog_content);
        mInfoTv = (TextView) findViewById(R.id.tv_info);
        if (TextUtils.isEmpty(mTitle)) tv_title.setVisibility(View.GONE);
        if (TextUtils.isEmpty(mInfo)) mInfoTv.setVisibility(View.GONE);
        tv_title.setText(mTitle);
        mInfoTv.setText(mInfo);
        et_content.setHint(mHint);
        setiMaxLen(iMaxLen);
        setInputType(icontentType);

        btn_ok = (Button) findViewById(R.id.btn_dialog_confirm);
        btn_cancel = (Button) findViewById(R.id.btn_dialog_cancle);
        btn_ok.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        btn_ok.setText(mbtnOktext);
        btn_cancel.setText(mbtnCancletext);

        // Set Dialog Gravity
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.CENTER);

        // Set Dialog Width
        WindowManager.LayoutParams p = dialogWindow.getAttributes();
        // p.width = WindowManager.LayoutParams.MATCH_PARENT;

        dialogWindow.setAttributes(p);
    }

    public InputDialog setTitle(String title) {
        this.mTitle = title;
        return this;
    }

    public InputDialog setInfo(String info) {
        this.mInfo = info;
        return this;
    }

    public InputDialog setHint(String hint) {
        this.mHint = hint;
        return this;
    }

    public InputDialog setOnClickListener(OnClickListener listener) {
        this.mListener = listener;
        return this;
    }

    public InputDialog setCancleText(String text){
        this.mbtnCancletext = text;
        return  this;
    }

    public InputDialog setConfirmText(String text){
        this.mbtnOktext=text;
        return  this;
    }

    public InputDialog setiMaxLen(int iMaxLen) {
        this.iMaxLen = iMaxLen;
        if (et_content != null && iMaxLen>0){
            et_content.setFilters(new InputFilter[]{new InputFilter.LengthFilter(iMaxLen)});
        }
        return  this;
    }

    public InputDialog setInputType(int type){
        if (type == -1){
            return this;
        }

        icontentType = type;
        if (et_content != null){
            et_content.setInputType(type);
        }
        return this;
    }


}
