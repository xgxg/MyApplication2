package com.dr.xg.myapplication.okhttp.otherRequest;

import java.io.Serializable;

/**
 * Created by cx on 2016/11/8.
 */
public class CountryDTO implements Serializable {
    private int id;
    private String name;
    private String code;

    private String tel_code;
    private String tel_showCode;
    private String reg_iconUrl;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTel_code() {
        return tel_code;
    }

    public void setTel_code(String tel_code) {
        this.tel_code = tel_code;
    }

    public String getTel_showCode() {
        return tel_showCode;
    }

    public void setTel_showCode(String tel_showCode) {
        this.tel_showCode = tel_showCode;
    }

    public String getReg_iconUrl() {
        return reg_iconUrl;
    }

    public void setReg_iconUrl(String reg_iconUrl) {
        this.reg_iconUrl = reg_iconUrl;
    }

}
