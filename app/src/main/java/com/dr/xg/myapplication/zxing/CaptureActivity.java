package com.dr.xg.myapplication.zxing;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dr.xg.myapplication.R;
import com.dr.xg.myapplication.utils.ToastUtil;
import com.dr.xg.myapplication.zxing.camera.CameraManager;
import com.dr.xg.myapplication.zxing.decoding.CaptureActivityHandler;
import com.dr.xg.myapplication.zxing.decoding.InactivityTimer;
import com.dr.xg.myapplication.zxing.decoding.RGBLuminanceSource;
import com.dr.xg.myapplication.zxing.view.ViewfinderView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import java.io.IOException;
import java.util.Vector;

public class CaptureActivity extends Activity implements Callback,
        View.OnClickListener{

    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;

    private static final int REQUEST_CODE = 100;
    private static final int PARSE_BARCODE_SUC = 300;
    private static final int PARSE_BARCODE_FAIL = 303;
    private ProgressDialog mProgress;
    private String photo_path;


    //定义标题栏弹窗按钮
    private TitlePopup titlePopup;


    public static void naviToActivity(Activity act, boolean bPop){
        Intent intent = new Intent(act, CaptureActivity.class);
        intent.putExtra("titlePopup", bPop);
        act.startActivity(intent);
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTranslucentSystemBar();
        setContentView(R.layout.activity_capture);

        CameraManager.init(getApplication());
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);

        ImageButton mButtonBack = (ImageButton) findViewById(R.id.ibtn_left);
        mButtonBack.setOnClickListener(this);
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);

        TextView rightTv = (TextView) findViewById(R.id.tv_right);
        boolean bPop = getIntent().getBooleanExtra("titlePopup", true);
        if (bPop){
            rightTv.setVisibility(View.VISIBLE);
        }
        rightTv.setOnClickListener(this);
        rightTv.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.title_btn_function), null);
        findViewById(R.id.ibtn_left).setOnClickListener(this);

        ((TextView)findViewById(R.id.tv_title)).setText(R.string.Scan_QR_Code);

        //实例化标题栏弹窗
        titlePopup = new TitlePopup(this, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        titlePopup.setItemOnClickListener(titlePopupListener);
        initTitlePopupData();
    }


    @Override
    protected void onResume() {
        super.onResume();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;

    }




    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibtn_left:
                finish();
                break;
            case R.id.tv_right:
                titlePopup.show(v);
                break;
        }
    }



    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            mProgress.dismiss();
            switch (msg.what) {
                case PARSE_BARCODE_SUC:
                    onResultHandler((String) msg.obj, null);
                    break;
                case PARSE_BARCODE_FAIL:
                    Toast.makeText(CaptureActivity.this, (String) msg.obj, Toast.LENGTH_LONG).show();
                    break;
            }
        }

    };

    /**
     * @author      陈希
     * @description 获取图片路径
     * @remark
     *
     * @param data onActivityResult  Intent对象
     * @return 获取图片路径
     */
    private String getImagePath(Intent data){
        String imgPath = "";
        Uri selectedImage = data.getData();
        String[] filePathColumn = { MediaStore.Images.Media.DATA };

        Cursor cursor = getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        if(cursor!=null)
        {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            imgPath = picturePath;
        }
        else
        {
            imgPath = selectedImage.getPath();
        }

        return imgPath;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE:
//                    //获取选中图片的路径
                    photo_path = getImagePath(data);
                    if (TextUtils.isEmpty(photo_path)){
                        Log.e("*****", "获取图片路径 为空");
                        return;
                    }

                    mProgress = new ProgressDialog(CaptureActivity.this);
                    mProgress.setMessage(getString(R.string.Scaning_));
                    mProgress.setCancelable(false);
                    mProgress.show();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Result result = scanningImage(photo_path);
                            if (result != null) {
                                Message m = mHandler.obtainMessage();
                                m.what = PARSE_BARCODE_SUC;
                                m.obj = result.getText();
                                mHandler.sendMessage(m);
                            } else {
                                Message m = mHandler.obtainMessage();
                                m.what = PARSE_BARCODE_FAIL;
                                m.obj = getString(R.string.Scan_failed);
                                mHandler.sendMessage(m);
                            }
                        }
                    }).start();

                    break;


            }
        }
    }

    /**
     * 扫描二维码图片的方法
     *
     * @param path
     * @return
     */
    public Result scanningImage(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }

        Bitmap scanBitmap = getSmallBitmap(path);
        RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap);
        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();
        try {
            Result result = reader.decode(bitmap1);
            if (scanBitmap != null){
                scanBitmap.recycle();
            }
            return result;
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (ChecksumException e) {
            e.printStackTrace();
        } catch (FormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap getSmallBitmap(String path) {
        BitmapFactory.Options op = new BitmapFactory.Options();
        //inJustDecodeBounds
        //If set to true, the decoder will return null (no bitmap), but the out…
        op.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(path, op); //获取尺寸信息
        //获取比例大小
        int wRatio = (int)Math.ceil(op.outWidth/768);
        int hRatio = (int)Math.ceil(op.outHeight/1024);
        //如果超出指定大小，则缩小相应的比例
        if(wRatio > 1 && hRatio > 1){
            if(wRatio > hRatio){
                op.inSampleSize = wRatio;
            }else{
                op.inSampleSize = hRatio;
            }
        }
        op.inJustDecodeBounds = false;
        bmp = BitmapFactory.decodeFile(path, op);
        return bmp;
    }




    /**
     * 处理扫描结果
     *
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        String resultString = result.getText();
        onResultHandler(resultString, barcode);
    }


    private void onResultHandler(final String resultString, Bitmap bitmap) {
        if (TextUtils.isEmpty(resultString)) {
            Toast.makeText(CaptureActivity.this, R.string.Scan_failed, Toast.LENGTH_SHORT).show();
            if (handler != null) {
                handler.restartPreviewAndDecode();
            }
            return;
        }

        try {
            ToastUtil.showMessage(CaptureActivity.this, resultString);
        }catch (Exception e) {
            e.printStackTrace();
            if (handler != null) {
                handler.restartPreviewAndDecode();
            }
        }
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (RuntimeException e) {//oppo在7.0一下会进入
            ToastUtil.showMessage(CaptureActivity.this, R.string.Please_Allow_access_Camera);
            finish();
            return;
        }catch (Exception ioe) {
            ToastUtil.showMessage(CaptureActivity.this, R.string.Please_Allow_access_Camera);
            finish();
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats,characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };



    private TitlePopup.OnItemOnClickListener titlePopupListener = new TitlePopup.OnItemOnClickListener(){
        public void onItemClick(ActionItem item, int position) {
            if (position == 0){
                ImageQrcode();
            }else if (position == 1){
                InputQrcode();
            }
        }
    };

    //弹出菜单
    /**
     * 初始化数据
     */
    private void initTitlePopupData(){
        //给标题栏弹窗添加子类
        titlePopup.addAction(new ActionItem(this, getString(R.string.image_qrcode), R.drawable.mm_title_btn_qrcode_normal));
        titlePopup.addAction(new ActionItem(this, getString(R.string.input_qrcode), R.drawable.mm_title_btn_qrcode_no));
    }


    private void ImageQrcode(){
        //打开手机中的相册
        Intent innerIntent = new Intent(); //"android.intent.action.GET_CONTENT"
        if (Build.VERSION.SDK_INT < 19) {
            innerIntent.setAction(Intent.ACTION_GET_CONTENT);
        } else {
            innerIntent.setAction(Intent.ACTION_PICK);
        }
        innerIntent.setType("image/*");
        Intent wrapperIntent = Intent.createChooser(innerIntent, getString(R.string.select_qrcode_image));
        this.startActivityForResult(wrapperIntent, REQUEST_CODE);
    }



    /**
     * @author      陈希
     * @description 弹出选项对话框
     * @remark
     */
    private void InputQrcode(){
        onPause();
        final InputDialog inputDialog = new InputDialog(this, getString(R.string.input_qrcode),"", "");
        InputDialog.OnClickListener listener = new InputDialog.OnClickListener() {
            @Override
            public void onConfirm(String content) {
                if (TextUtils.isEmpty(content)) {
                    ToastUtil.showMessage(CaptureActivity.this,R.string.name_cant_empty);
                    inputDialog.show();
                    return;
                }
                hideSoftKeyBoard(CaptureActivity.this);
                Intent intent = new Intent(CaptureActivity.this,ShowQrcodeActivity.class);
                intent.putExtra("qrInfor",content);
                startActivity(intent);
                onResume();
            }

            @Override
            public void onCancel() {
                inputDialog.dismiss();
                onResume();
            }
        };
        inputDialog.setCancleText(getString(R.string.cancel));
        inputDialog.setConfirmText(getString(R.string.ok));
        inputDialog.setOnClickListener(listener);
        inputDialog.setCanceledOnTouchOutside(true);
        inputDialog.setCancelable(true);
        inputDialog.setHint(getString(R.string.input_imei));
        inputDialog.show();
    }

    public static void hideSoftKeyBoard(Activity context) {
        View v = context.getWindow().getCurrentFocus();
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }


}