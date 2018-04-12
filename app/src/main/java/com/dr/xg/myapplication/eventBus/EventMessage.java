package com.dr.xg.myapplication.eventBus;

/**
 * @author 黄冬榕
 * @date 2018/3/27
 * @description
 * @remark
 */

public class EventMessage {

    private String mess;
    private int num;


    public EventMessage(String mess, int num) {
        this.mess = mess;
        this.num = num;
    }

    public String getMess() {
        return mess;
    }

    public void setMess(String mess) {
        this.mess = mess;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}


