package com.dr.xg.myapplication.smsMob;



/**
 * @date        2017/4/27
 * @description mob短信发送结束消息通知
 * @remark
 */
public interface MobNotice {

    /**
     * @description 获取到验证码
     * @remark
     *
     * @param bSmart 是否自动验证
     */
    void getVerifyCode(boolean bSmart);

    /**
     * @description 结束发送，如用户取消等
     * @remark
     */
    void smsFinish();


    /**
     * @description 错误信息通知，一般用于提示用户
     * @remark
     *
     * @param error 错误信息
     */
    void smsError(String error);

}
