package com.voyah.plugin_calanda;

import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;

import androidx.lifecycle.Observer;

import com.pluginrule.BaseService;

public class CalandaService extends BaseService {

    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent();
            intent.setAction("com.voyah.plugin_calanda.CalandaReceiver");
            intent.putExtra("data", "当前时间：" + SystemClock.currentThreadTimeMillis());
            service.sendBroadcast(intent);

            handler.post(this);
        }
    };


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler.postDelayed(runnable, 1000);
        return super.onStartCommand(intent, flags, startId);
    }
}