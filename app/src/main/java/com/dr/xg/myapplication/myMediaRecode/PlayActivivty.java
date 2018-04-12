package com.dr.xg.myapplication.myMediaRecode;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.dr.xg.myapplication.R;

import java.io.IOException;

/**
 * @author 黄冬榕
 * @date 2018/1/12
 * @description
 * @remark
 */

public class PlayActivivty extends Activity implements SurfaceHolder.Callback{

    private SurfaceView mSurfaceView;
    private SurfaceHolder mHolder;
    private String fileName;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        mSurfaceView = (SurfaceView) findViewById(R.id.surface_play);
        mHolder = mSurfaceView.getHolder();
        // 设置播放时打开屏幕
        mHolder.setKeepScreenOn(true);
        mHolder.addCallback(this);


        fileName = getIntent().getStringExtra("fileName");
        play();
    }

    private void play(){
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource("/storage/emulated/0/1/" + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try{
            mediaPlayer.prepare();
        }catch (Exception e){
            e.printStackTrace();
        }
        //等待surfaceHolder初始化完成才能执行mPlayer.setDisplay(surfaceHolder)
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                // 把视频画面输出到SurfaceView
                mediaPlayer.setDisplay(mHolder);
                mediaPlayer.start();
            }
        });



    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mHolder = holder;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mHolder = holder;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
