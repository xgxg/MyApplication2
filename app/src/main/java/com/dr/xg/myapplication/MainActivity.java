package com.dr.xg.myapplication;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.dr.xg.myapplication.custom.CustomActivity;
import com.dr.xg.myapplication.eventBus.EventMessage;
import com.dr.xg.myapplication.floatWindow.FloatWindowService;
import com.dr.xg.myapplication.myViewDragHelper.ViewDragActivity;
import com.dr.xg.myapplication.okhttp.ActivityLifeCycleEvent;
import com.dr.xg.myapplication.okhttp.otherRequest.CountryDTO;
import com.dr.xg.myapplication.okhttp.otherRequest.CountryService;
import com.dr.xg.myapplication.okhttp.HttpUtil;
import com.dr.xg.myapplication.okhttp.ProgressSubscriber;
import com.dr.xg.myapplication.okhttp.RetrofitServiceManager;
import com.dr.xg.myapplication.recyclerView.RecyclerActivity;
import com.dr.xg.myapplication.smsMob.SmsActivity;
import com.dr.xg.myapplication.myMediaRecode.MyMediaActivity;
import com.dr.xg.myapplication.zxing.CaptureActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

public class MainActivity extends BaseActivity {

    private List<String> functions = new ArrayList<>();
    private ListView listView;
    private ImageView imageView;
    private final int IMAGE_MY = 101,CROP_PHOTO = 9;
    private CommonListAdapter<String> commonListAdapter;

    private Uri imageUri;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list_activity);
        imageView = (ImageView) findViewById(R.id.image);
        functions.add("FloatWindow 悬浮框");
        functions.add("Zxing 二维码");
        functions.add("SmsMob 短信");
        functions.add("Custom 自定义控件");
        functions.add("Media 录视频");
        functions.add("ViewDragHelper 拽动助手");
        functions.add("RecyclerView 流式布局");
        functions.add("aidl android接口定义语言");
        functions.add("eventBus 观察者模式处理消息");
        functions.add("okhttp+retrofit+Rxjava");
        functions.add("design包控件");
//        functions.add(dongJNI());

        //注册eventBus事件
        EventBus.getDefault().register(this);


        commonListAdapter = new CommonListAdapter<String>(this,functions,R.layout.item_function) {
            @Override
            public void convert(ViewHolder helper, String item, int position) {
                helper.setText(R.id.test,item);
            }
        };

        listView.setAdapter(commonListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0://FloatWindow 悬浮框
                        Intent intent = new Intent(MainActivity.this, FloatWindowService.class);
                        startService(intent);
                        finish();
                        break;
                    case 1://Zxing 二维码
                        CaptureActivity.naviToActivity(MainActivity.this, true);
                        break;
                    case 2://SmsMob 短信
                        Intent intent1 = new Intent(MainActivity.this, SmsActivity.class);
                        startActivity(intent1);
                        break;
                    case 3://Custom 自定义控件
                        Intent intent2 = new Intent(MainActivity.this, CustomActivity.class);
                        startActivity(intent2);
                        break;
                    case 4://Media 录视频
                        Intent intent3 = new Intent(MainActivity.this, MyMediaActivity.class);
                        startActivity(intent3);
                        break;
                    case 5://ViewDragHelper 拽动助手
                        Intent intent4 = new Intent(MainActivity.this, ViewDragActivity.class);
                        startActivity(intent4);
                        break;
                    case 6://RecyclerView 流式布局
                        Intent intent5 = new Intent(MainActivity.this, RecyclerActivity.class);
                        startActivity(intent5);
                        break;
                    case 7://aidl android接口定义语言
                        Intent intentAidl = new Intent();
                        intentAidl.setAction("com.test.xg.aidl");
                        intentAidl.setPackage("com.test.xg.aidl");
                        bindService(intentAidl, connection, Context.BIND_AUTO_CREATE);
                        break;
                    case 8://eventBus 观察者模式处理消息
                        EventBus.getDefault().post(new EventMessage("我改的" + Math.random(), 8));
                        break;
                    case 9://okhttp+retrofit+Rxjava
                        CountryService countryService = RetrofitServiceManager.getInstance().create(CountryService.class);
                        Observable ob = countryService.login("en");
                        HttpUtil.getInstance().toSubscribe(ob, new ProgressSubscriber<List<CountryDTO>>(MainActivity.this) {
                            @Override
                            protected void _onError(String message) {
                                Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
                            }

                            @Override
                            protected void _onNext(List<CountryDTO> list) {
                                String str = "";
                                for (int i = 0; i < list.size(); i++) {
                                    str += "国家：" + list.get(i).getName();
                                }
                                Log.e("clientid", str);
                            }
                        }, "cacheKey", ActivityLifeCycleEvent.DESTROY, lifecycleSubject, false, false);
                        break;
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //EventBus处理
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void doEventBus(EventMessage eventMessage){
        functions.set(eventMessage.getNum(),eventMessage.getMess());
        commonListAdapter.notifyDataSetChanged();
    }


    //服务通信
    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            IMyAidlInterface bookManager = (IMyAidlInterface) IMyAidlInterface.Stub.asInterface(service);

            try {
                 List<Book> books = bookManager.getBooks();
                        Toast.makeText(MainActivity.this,books.get(0).getName(),Toast.LENGTH_LONG).show();

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };




}
