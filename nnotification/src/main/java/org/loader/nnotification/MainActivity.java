package org.loader.nnotification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.RemoteInput;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public static final String RESULT_KEY = "remote_input";
    public static final int NOTIFICATION_ID = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void click(View view) {
        final NotificationManager nm = getSystemService(NotificationManager.class);
        Notification notification = buildNotification(true);
        // step 5 notify
        nm.notify(NOTIFICATION_ID, notification);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                nm.notify(NOTIFICATION_ID, buildNotification(false));
            }
        }, 2000);
    }

    /**
     * 创建通知
     * @param isFirst 是否是第一次通知
     * @return
     */
    private Notification buildNotification(boolean isFirst) {
        // step 1 创建RemoteInput
        RemoteInput remoteInput = new RemoteInput.Builder(RESULT_KEY)
                .setLabel("回复这条消息")
                .build();

        // step 2 创建pendingintent, 当发送时调用什么
        Intent intent = new Intent(this, SendMsgService.class);
        PendingIntent pi = PendingIntent.getService(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // step 3 创建快捷回复 Action
        NotificationCompat.Action act = new NotificationCompat.Action.Builder(R.mipmap.ic_launcher, "回复", pi)
                .addRemoteInput(remoteInput).build();

        // step 4 创建notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("请问是否需要信用卡?")
                .setContentText("您好，我是XX银行的XX经理， 请问你需要办理信用卡吗？")
                .setColor(Color.CYAN)
                .addAction(act)
                .setCategory(Notification.CATEGORY_MESSAGE);

        if (isFirst) {
            builder.setDefaults(Notification.DEFAULT_SOUND);
            builder.setFullScreenIntent(pi, false);
        }
        return builder.build();
    }
}
