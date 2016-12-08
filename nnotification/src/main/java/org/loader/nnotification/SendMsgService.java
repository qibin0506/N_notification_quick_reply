package org.loader.nnotification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.RemoteInput;
import android.util.Log;

/**
 * <p>function: </p>
 * <p>description:  </p>
 * <p>history:  1. 2016/12/8</p>
 * <p>Author: qibin</p>
 * <p>modification:</p>
 */
public class SendMsgService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 获取RemoteInput中的Result
        Bundle replyBundle = RemoteInput.getResultsFromIntent(intent);
        if (replyBundle != null) {
            // 根据key拿到回复的内容
            String reply = replyBundle.getString(MainActivity.RESULT_KEY);
            reply(reply);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void reply(final String reply) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 模拟延迟1000ms，然后调用onReply
                SystemClock.sleep(1000);
                Log.d("reply", "reply: " + reply);
                onReply();
            }
        }).start();
    }

    private void onReply() {
        final NotificationManager nm = getSystemService(NotificationManager.class);
        final Handler handler = new Handler(getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                // 更新通知为“回复成功”
                Notification notification = new NotificationCompat.Builder(SendMsgService.this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentText("回复成功")
                        .build();
                nm.notify(MainActivity.NOTIFICATION_ID, notification);
            }
        });

        // 最后将通知取消
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                nm.cancel(MainActivity.NOTIFICATION_ID);
            }
        }, 2000);
    }
}
