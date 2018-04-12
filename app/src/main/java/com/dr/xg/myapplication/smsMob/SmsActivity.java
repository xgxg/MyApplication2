package com.dr.xg.myapplication.smsMob;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dr.xg.myapplication.R;

/**
 * @author 黄冬榕
 * @date 2018/1/8
 * @description
 * @remark
 */

public class SmsActivity extends Activity implements MobNotice{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        final EditText phoneEdit = (EditText) findViewById(R.id.edit_sms);
        final EditText codeEdit = (EditText)findViewById(R.id.edit_code);
        Button getCodeBtn =(Button) findViewById(R.id.btn_getcode);
        Button submitCodeBtn = (Button) findViewById(R.id.btn_submitCode);

        getCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SmsUtils.getInstence(SmsActivity.this).sendCode("86",phoneEdit.getText().toString().trim());
            }
        });

        submitCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SmsUtils.getInstence(SmsActivity.this).submitCode("86",phoneEdit.getText().toString().trim(),codeEdit.getText().toString().trim());
            }
        });


    }


    @Override
    public void getVerifyCode(boolean bSmart) {

    }

    @Override
    public void smsFinish() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(SmsActivity.this,"成功",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void smsError(final String error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(SmsActivity.this,error,Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onStart() {
        Log.e("cid","start");
        super.onStart();
    }

    @Override
    protected void onPause() {
        Log.e("cid","Pause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.e("cid","Stop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.e("cid","Destroy");
        super.onDestroy();
    }
}
