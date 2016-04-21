package com.Home.HelloworldOm2;


import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.Region;
import com.estimote.sdk.BeaconManager;

import java.util.List;
import java.util.UUID;

public class MyApplication extends Application {

    private BeaconManager beaconManager;


    @Override
    public void onCreate() {
        super.onCreate();

        //EstimoteSDK.initialize(getApplicationContext(), "helloworld-om2", "f647cad922e2a121de30ee8e7bb614b9");
        beaconManager = new BeaconManager(getApplicationContext());
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startMonitoring(new Region(
                        "My office",
                        UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),
                        62538, 8206));
            }
        });

        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, List<Beacon> list) {
                region.
                showNotification(
                        "Welcome to office",
                        "takes lot of time");
            }

            @Override
            public void onExitedRegion(Region region) {
                showNotification(
                        "did you lock your machine?",
                        "please lock your machine, take care of your purse keys and bag");
            }
        });
    }

    public void showNotification(String title, String message) {
        Intent notifyIntent = new Intent(this, MainActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0,
                new Intent[]{notifyIntent}, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }
}
