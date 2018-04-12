package com.dr.xg.myapplication.okhttp.otherRequest;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author 黄冬榕
 * @date 2018/3/27
 * @description
 * @remark
 */

public interface CountryService {
    @POST("PetAdmin/appUserMgr/getCountryList.do")
    @FormUrlEncoded
//读参数进行urlEncoded
    Observable<BaseResponse<List<CountryDTO>>> login(@Field("lang") String  username);
}
