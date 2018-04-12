package com.dr.xg.myapplication.zxing;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.dr.xg.myapplication.R;

import java.io.File;

/**
 * @author      陈希
 * @date        2016
 * @description 显示二维码界面
 * @remark
 */
public class ShowQrcodeActivity extends Activity {

    private ImageView qrcodeIv;
    private TextView qrCodeTv;

    private String qrInfor = "";

    private View main_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_qrcode);
        initData();
    }


    protected void initData() {
        qrcodeIv = (ImageView) findViewById(R.id.iv_qrcode);
        qrCodeTv = (TextView) findViewById(R.id.tv_qrcode);
        main_view = findViewById(R.id.main_view);
        main_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        qrInfor = intent.getStringExtra("qrInfor");

        qrCodeTv.setText(qrInfor);

        final String filePath = getFilesDir().toString() + "/" + "1.jpg";
        File qrFile = new File(filePath);
        if (qrFile.exists()) {
            qrFile.delete();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap faceBmp = null;
                //构建二维码图片
                boolean success = QRCodeUtil.createQRImage(qrInfor, 400, 400,faceBmp, filePath);
                if (success) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            qrcodeIv.setImageBitmap(BitmapFactory.decodeFile(filePath));
                        }
                    });
                }
            }
        }).start();
    }
}
