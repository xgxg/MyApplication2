package com.dr.xg.myapplication.recyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.dr.xg.myapplication.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 黄冬榕
 * @date 2018/1/23
 * @description
 * @remark
 */

public class RecyclerActivity extends Activity{

    RecyclerView mRecyclerView;
    HomeAdapter mHomeAdapter;
    List<String> mList = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);

        mList.add("ed1");
        mList.add("ed2");
        mList.add("ed3");
        mList.add("ed4");
        mList.add("ed5");
        mList.add("ed6");
        mList.add("ed7");
        mList.add("ed8");
        mList.add("edswd1");
        mList.add("ed2we");
        mList.add("ewed3");
        mList.add("eed4");
        mList.add("efwd5");
        mList.add("edfe6");
        mList.add("edcfed7");
        mList.add("edsw8");

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        mHomeAdapter = new HomeAdapter(RecyclerActivity.this,mList);

//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(RecyclerActivity.this);
//        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

//        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,4);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this));
        mRecyclerView.setAdapter(mHomeAdapter);




    }
}
