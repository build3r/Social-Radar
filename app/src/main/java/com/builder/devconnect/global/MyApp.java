package com.builder.devconnect.global;

import android.app.Application;
import android.content.Context;

import com.builder.devconnect.manager.APIManager;
import com.parse.Parse;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;

import java.util.Arrays;

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
        Beacon beacon = new Beacon.Builder()
                .setId1("2f234454-cf6d-4a0f-adf2-f4911ba9ffa6")
                .setId2("uW0ZrdtriC")
                .setId3("2")
                .setManufacturer(0x0118)
                .setTxPower(-59)
                .setDataFields(Arrays.asList(new Long[] {0l}))
                .build();
        BeaconParser beaconParser = new BeaconParser()
                .setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25");
        BeaconTransmitter beaconTransmitter = new BeaconTransmitter(getApplicationContext(), beaconParser);
        beaconTransmitter.startAdvertising(beacon);
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
