package com.ashdot.safeount;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;
import com.appsflyer.attribution.AppsFlyerRequestListener;
import com.ashdot.safeount.model.Afkey;

import java.util.Map;

public class SLOTOTERRAApplication extends Application {
    Context appContext;
    String TAG = "DemoApplication";
    //String afKey = "RS4u5njpypNsWxbgRX6p7F";
    public static SharedPreferences mPreferences;
    public static boolean isAd = false;//广告流量
    public static String appid = "com.ashdot.safeount";

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
        mPreferences = getSharedPreferences(appid, MODE_PRIVATE);
        initAf();
    }

    /**
     * 初始化AppsFlyer
     * 包名：com.apansrat.pusblotan
     * AF Dev Key：ZqdFx3sCpWzXmJLjDaesHJ
     * 跳转链接：https://9f021662.xyz/
     */

    private void initAf() {
        //Log.d(TAG, "initAf");
        AppsFlyerLib.getInstance().setMinTimeBetweenSessions(0);
        AppsFlyerLib.getInstance().setDebugLog(true);

        // app flay初始化
        AppsFlyerLib.getInstance().init(AppMyRSAUtils.getDecodeStr(Afkey.mAfkey), new AppsFlyerConversionListener() {
            @Override
            public void onConversionDataSuccess(Map<String, Object> map) {
                //map={install_time=2023-09-25 13:27:12.578, af_status=Organic, af_message=organic install, is_first_launch=true}
                //Log.d(TAG, "onConversionDataSuccess map=" + map);
                String afType = (String) map.get("af_status");
                if (TextUtils.equals(afType, "Organic")) {
                    isAd = false;
                } else {
                    isAd = true;
                    mPreferences.edit().putBoolean(appid, true).apply();
                }
            }

            @Override
            public void onConversionDataFail(String s) {
                //Log.d(TAG, "onConversionDataFail=" + s);

            }

            @Override
            public void onAppOpenAttribution(Map<String, String> map) {
                //Log.d(TAG, "onAppOpenAttribution=" + map);

            }

            @Override
            public void onAttributionFailure(String s) {
                //Log.d(TAG, "onAttributionFailure=" + s);

            }
        }, appContext);

        AppsFlyerLib.getInstance().start(appContext, AppMyRSAUtils.getDecodeStr(Afkey.mAfkey), new AppsFlyerRequestListener() {
            @Override
            public void onSuccess() {
                Log.e(TAG, "Launch sent successfully, got 200 response code from server");
            }

            @Override
            public void onError(int i, @NonNull String s) {
                Log.e(TAG, "Launch failed to be sent:\n" + "Error code: " + i + "\n" + "Error description: " + s);
            }
        });

    }
}
