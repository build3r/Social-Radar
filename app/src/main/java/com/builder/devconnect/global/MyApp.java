package com.builder.devconnect.global;

import android.app.Application;
import android.content.Context;

import com.builder.devconnect.manager.APIManager;
import com.parse.Parse;

/**
 * Created by Weloft Labs on 04-05-2016.
 */
public class MyApp extends Application {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static MyApp mAppInstance;
    public boolean isDebug = true;

    public boolean isDebug() {
        return isDebug;
    }

//    @Override
//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(base);
//        MultiDex.install(this);
//    }

    @Override
    public void onCreate() {
        super.onCreate();

        mAppInstance = this;

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("fUDC7KyGWsJQF81RfRjfwDCH4nFqMDD0vWdb0aLL")
                .clientKey("hzsgVvKHY6NFIZAJDXoFOHkNKu9WCa10AOl8seN1")
                .server("https://parseapi.back4app.com/").build()
        );

       APIManager.newInstance().registerDevice();
    }

    public static Context getInstance(){
        return mAppInstance;
    }
}
