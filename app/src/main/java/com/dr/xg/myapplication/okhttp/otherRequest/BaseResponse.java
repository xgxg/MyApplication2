package com.dr.xg.myapplication.okhttp.otherRequest;

/*
*
*
* 网络请求结果 基类
* Created by zhouwei on 16/11/10.
*/
public class BaseResponse<T> {
    public ResponseHeader header;
    public T data;

    public ResponseHeader getHeader() {
        return header;
    }

    public void setHeader(ResponseHeader header) {
        this.header = header;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
