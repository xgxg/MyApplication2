package com.dr.xg.myapplication.recyclerView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dr.xg.myapplication.R;

import java.util.List;

/**
 * @author 黄冬榕
 * @date 2018/1/23
 * @description
 * @remark
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {
    Context mContext;
    List<String> mData;

    public HomeAdapter(Context context, List<String> datas){
        mContext = context;
        mData = datas;
    }
    @Override
    public HomeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder myViewHolder =  new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.recycler_item,parent,false));

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(HomeAdapter.MyViewHolder holder, int position) {
        holder.textView.setText(mData.get(position));
        holder.textView.setHeight((int)(50+Math.random()*50));

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text_recycler);
        }
    }
}
