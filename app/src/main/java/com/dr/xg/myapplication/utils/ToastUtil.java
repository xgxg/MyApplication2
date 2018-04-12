package com.dr.xg.myapplication.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dr.xg.myapplication.R;

public class ToastUtil {

    private static String oldMsg;
    private static long lastTime = 0;
    private static final int SHORT_DELAY = 2000; // 2 seconds

    protected static Toast toast = null;
    public static void showMessage(Context context, String message) {
        if (message == null || context == null){
            return;
        }

        long tempTime = System.currentTimeMillis();
        if (tempTime - lastTime < SHORT_DELAY && message.equals(oldMsg)){
            return;
        }

        cancel();
        toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
        lastTime = System.currentTimeMillis();
        oldMsg = message;
    }


    public static void showMessage(Context context, int resId) {
        if (context == null){
            return;
        }
        showMessage(context, context.getString(resId));
    }

    public static void showDebugDialog(Activity ac, String content) {
        final AlertDialog dialog = new AlertDialog.Builder(ac).create();
        View viewDialog = LayoutInflater.from(ac).inflate(R.layout.layout_dialog_confirm, null);
        final TextView tvTitle = (TextView) viewDialog.findViewById(R.id.tv_title);
        final Button btnCancle = (Button) viewDialog.findViewById(R.id.btn_left);
        final Button btnConfirm = (Button) viewDialog.findViewById(R.id.btn_right);

        tvTitle.setText(content);
        btnCancle.setText("Y");
        btnConfirm.setText("N");
        dialog.show();
        dialog.setContentView(viewDialog);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });
    }

    public static void cancel(){
        if (toast != null){
            toast.cancel();
        }
    }
}  
