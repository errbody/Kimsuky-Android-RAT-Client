package com.support.samsg;

import android.content.Context;
import android.os.Looper;

public class body implements Runnable {

    protected final Context mContext;
    public static int interval = 300;

    public boolean run = true;
    public body(Context context , int n_interval) {
        mContext = context;
        interval = n_interval;
        if (n_interval == 0){
            interval = 300;
        }

    }

    @Override
    public void run() {
        Looper.prepare();
        int index = 1;

        while (run) {
            try {

                phone_state myPhoneLogs = new phone_state(mContext);
                myPhoneLogs.start_process();

                Thread.sleep(1000 * interval);
            } catch (InterruptedException e) {
                run = false;
            }
        }
        Looper.loop();
    }
    public void stop_run() {
        run =false;
    }

}