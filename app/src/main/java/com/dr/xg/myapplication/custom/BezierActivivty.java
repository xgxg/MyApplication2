package com.dr.xg.myapplication.custom;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.widget.ViewDragHelper;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.dr.xg.myapplication.R;

/**
 * @author 黄冬榕
 * @date 2018/1/15
 * @description
 * @remark
 */

public class BezierActivivty extends Activity{
    private BezierView mBezierView;
    private RadioGroup mRadioGroup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bzier);
        mBezierView = (BezierView) findViewById(R.id.bezier);
        mRadioGroup = (RadioGroup) findViewById(R.id.radio_group);

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
              switch (checkedId){
                  case R.id.radio_first:
                      mBezierView.setMode(true);
                      break;
                  case R.id.radio_second:
                      mBezierView.setMode(false);
                      break;
              }
          }
      });
    }
}
