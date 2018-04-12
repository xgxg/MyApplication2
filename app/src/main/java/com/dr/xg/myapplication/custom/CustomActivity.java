package com.dr.xg.myapplication.custom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.dr.xg.myapplication.R;

/**
 * @author 黄冬榕
 * @date 2018/1/9
 * @description
 * @remark
 */

public class CustomActivity extends Activity{
    private Button button;
    private CustomView customView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);

        button = (Button) findViewById(R.id.btn_nest);
        customView = (CustomView) findViewById(R.id.custom);
        button.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          Intent intent = new Intent(CustomActivity.this,BezierActivivty.class);
                                          startActivity(intent);
                                      }
                                  }
        );
    }
}
