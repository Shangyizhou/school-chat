package com.example.framework;

import android.content.Context;

import com.example.framework.bmob.BmobManager;
//import com.example.framework.cloud.CloudManager;
//import com.example.framework.helper.NotificationHelper;
//import com.example.framework.helper.WindowHelper;
//import com.example.framework.manager.KeyWordManager;
//import com.example.framework.manager.MapManager;
import com.example.framework.helper.NotificationHelper;
import com.example.framework.helper.WindowHelper;
import com.example.framework.manager.KeyWordManager;
import com.example.framework.utils.LogUtils;
import com.example.framework.utils.SpUtils;
import com.tencent.bugly.crashreport.CrashReport;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * FileName: com.example.framework.Framework
 * Founder: LiuGuiLin
 * Profile: com.example.framework.Framework 入口
 */
public class Framework {

    private volatile static Framework mFramework;

    private String BUGLY_KEY = "d51bdd38bd";

    private Framework() {

    }

    public static Framework getFramework() {
        if (mFramework == null) {
            synchronized (Framework.class) {
                if (mFramework == null) {
                    mFramework = new Framework();
                }
            }
        }
        return mFramework;
    }

    /**
     * 初始化框架 Model
     *
     * @param mContext
     */
    public void initFramework(Context mContext) {
        LogUtils.i("initFramework");
        SpUtils.getInstance().initSp(mContext);
        BmobManager.getInstance().initBmob(mContext);
//        CloudManager.getInstance().initCloud(mContext);
//        LitePal.initialize(mContext);
//        MapManager.getInstance().initMap(mContext);
        WindowHelper.getInstance().initWindow(mContext);
        CrashReport.initCrashReport(mContext, BUGLY_KEY, BuildConfig.LOG_DEBUG);
        ZXingLibrary.initDisplayOpinion(mContext);
        NotificationHelper.getInstance().createChannel(mContext);
        KeyWordManager.getInstance().initManager(mContext);

        //全局捕获RxJava异常
        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                LogUtils.e("RxJava：" + throwable.toString());
            }
        });
    }
}
